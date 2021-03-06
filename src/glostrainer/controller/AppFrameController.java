package glostrainer.controller;

import glostrainer.view.GUIHelpers;
import glostrainer.model.WordEntry;
import glostrainer.model.WordlistModel;
import glostrainer.view.WordlistPanel;
import glostrainer.view.GUIFrame;
import static glostrainer.view.WordlistPanel.DEFINITION_COLUMN;
import static glostrainer.view.WordlistPanel.OPTIONAL_FORMS_COLUMN;
import static glostrainer.view.WordlistPanel.SWEDISH_DICTIONARY_FORM_COLUMN;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTable.PrintMode;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * In the MVC pattern, the controller facilitates the communication between the
 * user and the application. This class represents the main controller for the
 * application. It manages a GUI, which here is an instance of a
 * <code>GUIFrame</code>, and a word list model class,
 * <code>WordlistModel</code>. The <code>AppFrameController</code> responds to
 * GUI events such as button clicks and key presses, and reacts accordingly.
 * Since the table view in the GUI cannot be manipulated directly, the
 * <code>WordListFrameController</code> class manages a
 * <code>NewOrEditEntryFormController</code> instance to show dialogs for the
 * user to create or edit word entries.
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class AppFrameController implements IController
{

    private MainController mainController;

    /**
     *
     */
    private WordlistModel model;

    /**
     *
     */
    private GUIFrame view;

    // keep a reference to the file chooser so we can retain information about last saved folder
    JFileChooser chooser;

    /**
     * Creates a new WordlistFrameController with the given model and
     * view.getEditWordlistTab().
     *
     * @param mainController
     * @param model
     * @param view
     */
    public AppFrameController(MainController mainController, WordlistModel model, GUIFrame view)
    {
        this.mainController = mainController;
        this.model = model;
        this.view = view;

        chooser = new JFileChooser();
        setupViewEvents();
    }

    @Override
    public GUIFrame getView()
    {
        return this.view;
    }

    @Override
    public WordlistModel getModel()
    {
        return this.model;
    }

    /**
     * Set actions and listeners for the view.getEditWordlistTab(). The New
     * Entry button gets the first focus. Edit Entry and Delete Entry will be
     * disabled at first since we assume that no row, or the empty row is
     * selected at first.
     */
    private void setupViewEvents()
    {
        setupActions();
        this.view.getEditWordlistTab().getNewEntryButton().requestFocus();
        this.view.getEditWordlistTab().getEditEntryButton().setEnabled(false);
        this.view.getEditWordlistTab().getDeleteEntryButton().setEnabled(false);

        setupWindowClosingConfirmationEvent();

        setupTableKeyListener();
        setupTableSelectionChangedListener();

        setupTableSorter();
        setupTableRowFilter();
        setupFilterFieldListener();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "GlosTrainer list files", "gtl");
        chooser.setFileFilter(filter);
    }

    /**
     * Sets up actions for the buttons.
     */
    private void setupActions()
    {
        // entry panel
        this.view.getEditWordlistTab().getNewEntryButton().setAction(new NewEntryAction());
        this.view.getEditWordlistTab().getEditEntryButton().setAction(new EditEntryAction());
        this.view.getEditWordlistTab().getDeleteEntryButton().setAction(new DeleteEntryAction());
        // list panel
        this.view.getEditWordlistTab().getImportButton().setAction(new ImportListAction());
        this.view.getEditWordlistTab().getExportButton().setAction(new ExportListAction());
        this.view.getEditWordlistTab().getPrintListButton().setAction(new PrintListAction());
        this.view.getEditWordlistTab().getClearListButton().setAction(new ClearListAction());
    }

    /**
     * We need to force Swedish sorting on the table in case the user does not
     * run a system with the Swedish locale set (which is most likely the case
     * considering the intended target group of this application. The Swedish
     * alphabet consists of the letters A-Z as in English, followed by Å (A with
     * a ring above), Ä (A with two dots above) and Ö (O with two dots above),
     * in this particular order. For the sorting, we ignore letter cases.
     */
    private void setupTableSorter()
    {
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(view.getEditWordlistTab().getWordlistTable().getModel());
        view.getEditWordlistTab().getWordlistTable().setRowSorter(sorter);

        try
        {
            String swedishAlphabet = "< a < b < c < d < e < f < g < h < i"
                    + "< j < k < l < m < n < o < p < q < r < s < t < u"
                    + "< v < w < y < z < \u00E5 < \u00E4 < \u00F6";
            final RuleBasedCollator ruleBasedCollator = new RuleBasedCollator(swedishAlphabet);
            Comparator<String> swedishComparator = (String o1, String o2) ->
            {
                return ruleBasedCollator.compare(o1.toLowerCase(), o2.toLowerCase());
            };
            for (int i = 0; i < view.getEditWordlistTab().getWordlistTable().getColumnCount(); i++)
            {
                sorter.setComparator(i, swedishComparator);
            }

        } catch (ParseException ex)
        {
            // shouldn't happen
            Logger.getLogger(AppFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a filter so that the table shows a subset of its model when the
     * user has entered text in the filter text field. We also need to consider
     * if the checkboxes for searching in optional word forms and complete
     * matches are checked. If the filter text field is empty, no filtering
     * should be done, i.e. all entries should be returned.
     *
     * @return
     */
    private void setupTableRowFilter()
    {
        // TODO: refactor?
        RowFilter<TableModel, Integer> rf = new RowFilter()
        {
            /**
             * Given an entry, return true if is to be included according to the
             * filter, otherwise false.
             *
             * @param entry
             * @return
             */
            @Override
            public boolean include(RowFilter.Entry entry)
            {
                // if the search string is empty, return true for everything
                String textToLookFor = view.getEditWordlistTab().getFilterField().getText();
                if (textToLookFor.equals(""))
                {
                    return true;
                }

                if (!view.getEditWordlistTab().getExactMatchCheckButton().isSelected())
                {
                    boolean wordIsInDictionaryFormOrDefinitionColumn
                            = entry.getStringValue(SWEDISH_DICTIONARY_FORM_COLUMN).contains(textToLookFor)
                            || entry.getStringValue(DEFINITION_COLUMN).contains(textToLookFor);

                    if (!view.getEditWordlistTab().getAllFormsCheckButton().isSelected())
                    {
                        return wordIsInDictionaryFormOrDefinitionColumn;
                    } else // all forms
                    {
                        if (wordIsInDictionaryFormOrDefinitionColumn)
                        {
                            return true;
                        }

                        return entry.getStringValue(OPTIONAL_FORMS_COLUMN).contains(textToLookFor);
                    }
                } else // exact match
                {
                    boolean wordIsInDictionaryFormOrDefinitionColumn
                            = entry.getStringValue(SWEDISH_DICTIONARY_FORM_COLUMN).equalsIgnoreCase(textToLookFor)
                            || entry.getStringValue(DEFINITION_COLUMN).equalsIgnoreCase(textToLookFor);

                    if (!view.getEditWordlistTab().getAllFormsCheckButton().isSelected())
                    {
                        return wordIsInDictionaryFormOrDefinitionColumn;
                    } else // all forms
                    {
                        if (wordIsInDictionaryFormOrDefinitionColumn)
                        {
                            return true;
                        }

                        // ugly hack
                        String[] optionalFormsAsArray = entry.getStringValue(OPTIONAL_FORMS_COLUMN).split(", ");
                        for (String s : optionalFormsAsArray)
                        {
                            if (s.equalsIgnoreCase(textToLookFor))
                            {
                                return true;
                            }
                        }
                        return false;
                    }
                }
            }
        };
        TableRowSorter sorter = (TableRowSorter) view.getEditWordlistTab().getWordlistTable().getRowSorter();
        sorter.setRowFilter(rf);
    }

    /**
     * Listen to changes in the filter text field.
     *
     * @param sorter
     * @param rf
     */
    private void setupFilterFieldListener()
    {
        TableRowSorter sorter = (TableRowSorter) view.getEditWordlistTab().getWordlistTable().getRowSorter();
        RowFilter rf = sorter.getRowFilter();

        this.view.getEditWordlistTab().getFilterField().getDocument().addDocumentListener(
                new DocumentListener()
                {
                    @Override
                    public void changedUpdate(DocumentEvent e)
                    {
                        sorter.setRowFilter(rf);
                        updateEntryCount();
                    }

                    @Override
                    public void insertUpdate(DocumentEvent e)
                    {
                        sorter.setRowFilter(rf);
                        updateEntryCount();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e)
                    {
                        sorter.setRowFilter(rf);
                        updateEntryCount();
                    }
                });
    }

    /**
     * Disable buttons for editing and deleting entries if no rows are selected,
     * or if the empty row is selected.
     */
    private void setupTableSelectionChangedListener()
    {
        JTable table = view.getEditWordlistTab().getWordlistTable();
        // enable/disable edit and delete buttons dependin
        DefaultListSelectionModel listSelectionModel = (DefaultListSelectionModel) table.getSelectionModel();
        listSelectionModel.addListSelectionListener((ListSelectionEvent e) ->
        {
            if (table.getSelectedRowCount() == 0)
            {
                view.getEditWordlistTab().getEditEntryButton().setEnabled(false);
                view.getEditWordlistTab().getDeleteEntryButton().setEnabled(false);
            } else
            {
                if (table.getSelectedRows()[0] == table.getRowCount() - 1)
                {
                    view.getEditWordlistTab().getEditEntryButton().setEnabled(false);
                    view.getEditWordlistTab().getDeleteEntryButton().setEnabled(false);
                } else
                {
                    view.getEditWordlistTab().getEditEntryButton().setEnabled(true);
                    view.getEditWordlistTab().getDeleteEntryButton().setEnabled(true);
                }
            }
        });
        this.view.getEditWordlistTab().getWordlistTable().addMouseListener(new WordlistTableDoubleClickListener());
    }

    /**
     * Currently the table responds to Space, Enter and Delete keys.
     *
     * @return
     */
    private JTable setupTableKeyListener()
    {
        final JTable table = this.view.getEditWordlistTab().getWordlistTable();
        table.addKeyListener(new KeyAdapter()
        {

            @Override
            public void keyPressed(KeyEvent e)
            {
                switch (e.getKeyCode())
                {
                    case KeyEvent.VK_DELETE:
                        tryDeleteEntries();
                        break;
                    /*
                     If user has pressed space or enter, open a new entry frame if
                     they are on the empty row, otherwise try to open the edit frame
                     */
                    case KeyEvent.VK_SPACE:
                    case KeyEvent.VK_ENTER:
                        if (view.getEditWordlistTab().getWordlistTable().getSelectedRows()[0] == model.getEntryCount())
                        {
                            openNewEntryForm();
                        } else
                        {
                            tryOpenEditEntryForm();
                        }
                        break;
                }
            }
        });
        return table;
    }

    /**
     * Ask the user for confirmation when closing the window.
     */
    private void setupWindowClosingConfirmationEvent()
    {
        this.view.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.view.getFrame().addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                SwingUtilities.invokeLater(() ->
                {
                    int result = JOptionPane.showConfirmDialog(view.getFrame(),
                            "Do you really want to close the application? All unsaved changes will be lost.",
                            "Exit",
                            JOptionPane.WARNING_MESSAGE);
                    if (result == JOptionPane.YES_OPTION)
                    {
                        System.exit(0);
                    }
                });

            }
        });
    }

    /**
     * Requests the newOrEditEntryFormController to open a New Entry form. If
     * the user accepts the new entry, we ask the controller for the saved entry
     * and add it to our model and update the view.getEditWordlistTab().
     */
    public void openNewEntryForm()
    {
        mainController.getNewOrEditEntryFormController().openNewEntryForm();
        if (mainController.getNewOrEditEntryFormController().wordEntryWasSaved)
        {
            /* 
             * Copy the word to our model. we can't simply have a reference to the saved word,
             * because then we will run into trouble when adding or deleting words as
             * it will always point to the latest word.
             */
            WordEntry wordToAdd = new WordEntry(mainController.getNewOrEditEntryFormController().getCurrentWordEntry());
            this.model.addWordEntry(wordToAdd);
            addTableRowFromWord(wordToAdd);
            // because the NewLineTable will add an empty line for us, we don't need to add an empty row here

            this.updateEntryCount();
            // reset filter so the user can see their new entry
            view.getEditWordlistTab().getFilterField().setText("");

            // add the new word to the quiz list
            mainController.getQuizWordlistController().addWordEntryToQuizWordlist(wordToAdd);
        }
    }

    /**
     * Adds a row to the table with information from the given word. This does
     * not affect the model that this controller is tied to.
     *
     * @param word the word to retrieve data from
     */
    private void addTableRowFromWord(WordEntry word)
    {
        DefaultTableModel tbm = (DefaultTableModel) this.view.getEditWordlistTab().getWordlistTable().getModel();
        tbm.addRow(new String[]
        {
            word.getSwedishDictionaryForm(),
            word.getDefinition(),
            word.getWordClass().toString(),
            word.getOptionalFormsAsString()
        });
    }

    /**
     * Requests the newOrEditEntryFormController to open an Edit Entry frame
     * with the selected row index from the table view.getEditWordlistTab().
     * Note that this may not be the same index as the table model, so the call
     * to this method might throw an exception if the selected index does not
     * exist in the view.getEditWordlistTab(). Waits until the user has finished
     * editing, and if the user confirms to save the word, we replace the word
     * in the model with the newly saved word, and reset the filter to guarantee
     * that the user will see the new word.
     *
     * @param selectedIndexInView
     */
    public void openEditEntryFormWithWordAtSelectedIndex(int selectedIndexInView)
    {
        /*
         If the user has sorted or filtered the table, we need to convert the selected
         index from the view to the corresponding index in the model.
         */
        int selectedIndexInModel = view.getEditWordlistTab().getWordlistTable().convertRowIndexToModel(selectedIndexInView);

        mainController.getNewOrEditEntryFormController().openEditEntryFrame(this.model.getWordEntryAtIndex(selectedIndexInModel));
        if (mainController.getNewOrEditEntryFormController().wordEntryWasSaved)
        {
            WordEntry savedWord = mainController.getNewOrEditEntryFormController().getCurrentWordEntry();
            this.model.replaceWordEntryAtIndex(selectedIndexInModel, savedWord);

            // edit table entry
            DefaultTableModel tableModel = (DefaultTableModel) view.getEditWordlistTab().getWordlistTable().getModel();
            tableModel.setValueAt(savedWord.getSwedishDictionaryForm(), selectedIndexInModel, WordlistPanel.SWEDISH_DICTIONARY_FORM_COLUMN);
            tableModel.setValueAt(savedWord.getDefinition(), selectedIndexInModel, WordlistPanel.DEFINITION_COLUMN);
            tableModel.setValueAt(savedWord.getWordClass(), selectedIndexInModel, WordlistPanel.WORD_CLASS_COLUMN);
            tableModel.setValueAt(savedWord.getOptionalFormsAsString(), selectedIndexInModel, WordlistPanel.OPTIONAL_FORMS_COLUMN);

            // reset filter so the user can see their new entry
            view.getEditWordlistTab().getFilterField().setText("");

            // update quiz view
            mainController.getQuizWordlistController().editWordEntryInQuizWordlist(selectedIndexInModel, savedWord);
        }
    }

    /**
     * See if we can request to open an Edit Entry form. We can do this if the
     * user has selected exactly one row in the table. The frame will be
     * populated by information from the word entry at that table row (in the
     * view). If zero, or more than one row are selected, display error messages
     * to the user and do nothing. The request to open the Edit Entry form is
     * done by invoking the
     * <code>openEditEntryFormWithWordAtSelectedIndex()</code> method.
     */
    public void tryOpenEditEntryForm()
    {
        // Display an error if the user hasn't selected any rows, or more than one row.
        JTable wordListTable = this.view.getEditWordlistTab().getWordlistTable();
        int[] selectedTableRowIndicesInView = wordListTable.getSelectedRows();
        if (selectedTableRowIndicesInView.length == 0)
        {
            JOptionPane.showMessageDialog(this.view.getFrame(), "Select a row to edit.", "", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (selectedTableRowIndicesInView.length >= 2)
        {
            JOptionPane.showMessageDialog(this.view.getFrame(), "Please only select one row to edit.", "", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        openEditEntryFormWithWordAtSelectedIndex(selectedTableRowIndicesInView[0]);
    }

    /**
     * See if there is at least one row selected. If not, display an error
     * message to the user and do nothing. If there is, ask the user for
     * confirmation about deleting the selected row(s). If confirmed, invoke the
     * <code>deleteSelectedTableEntries()</code> method.
     */
    public void tryDeleteEntries()
    {
        int[] selectedIndices = this.view.getEditWordlistTab().getWordlistTable().getSelectedRows();
        if (selectedIndices.length == 0 || selectedIndices[0] == this.view.getEditWordlistTab().getWordlistTable().getRowCount() - 1)
        {
            JOptionPane.showMessageDialog(null, "No rows are selected.");
            return;
        } else
        {
            String entryOrEntries = (selectedIndices.length == 1) ? "entry" : "entries";

            int result = JOptionPane.showConfirmDialog(null,
                    "Really delete the selected " + entryOrEntries + "?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION)
            {
                deleteSelectedTableEntries();
            }
        }
    }

    /**
     * Deletes the selected rows in the table and removes them from the model.
     * Deletion is performed by invoking <code>deleteWordAtIndex()</code> for
     * each selected row.
     */
    public void deleteSelectedTableEntries()
    {
        JTable table = this.view.getEditWordlistTab().getWordlistTable();
        int[] selectedIndices = table.getSelectedRows();

        // delete indexes in reverse order because we need to delete entries in the view's table
        for (int i = selectedIndices.length - 1; i >= 0; i--)
        {
            /*
             Just as when we did editing, we must make sure that the selected row corresponds
             to the right item in the model list, in case the user has sorted or filtered results.
             */
            int selectedIndexInModel = table.convertRowIndexToModel(selectedIndices[i]);
            deleteWordAtIndex(selectedIndexInModel);
        }
    }

    /**
     * Deletes an entry in the model with the given index.
     *
     * @param indexInModelToDelete the index to delete
     */
    public void deleteWordAtIndex(int indexInModelToDelete)
    {
        this.model.removeWordEntryAtIndex(indexInModelToDelete);
        // delete table entry
        DefaultTableModel tableModel = (DefaultTableModel) view.getEditWordlistTab().getWordlistTable().getModel();
        tableModel.removeRow(indexInModelToDelete);
        this.updateEntryCount();
        mainController.getQuizWordlistController().deleteWordEntryAtIndex(indexInModelToDelete);
    }

    /**
     * Updates the entry count label. Displays entry count and filtered words
     * count. Invoke this whenever you add or remove entries, or whenever the
     * word entry table gets filtered.
     */
    public void updateEntryCount()
    {
        int totalEntryCount = this.model.getEntryCount();
        String entryOrEntries = (totalEntryCount == 1) ? "entry" : "entries";
        int filteredEntryCount = getFilteredRowCount();
        String filteredEntryOrEntries = (filteredEntryCount == 1) ? "entry" : "entries";

        if (this.view.getEditWordlistTab().getFilterField().getText().equals("")) // are we filtering something?
        {
            this.view.getEditWordlistTab().getEntryCountLabel().setText(String.format("%d %s in list", totalEntryCount, entryOrEntries));
        } else
        {
            this.view.getEditWordlistTab().getEntryCountLabel().setText(String.format("%d %s in list, filtered %d %s in view", totalEntryCount, entryOrEntries, filteredEntryCount, filteredEntryOrEntries));
        }
    }

    /**
     * Calculate the number of filtered rows in the word entry table.
     *
     * @return the number of filtered rows
     */
    private int getFilteredRowCount()
    {
        int filteredRowCount = 0;
        for (int i = 0; i < this.view.getEditWordlistTab().getWordlistTable().getModel().getRowCount(); i++)
        {
            if (this.view.getEditWordlistTab().getWordlistTable().convertRowIndexToView(i) != -1)
            {
                filteredRowCount++;
            }
        }
        return filteredRowCount;
    }

    /**
     * If there are words in the model, ask the user for confirmation to
     * overwrite the current list. If the user confirmed, or there are no words
     * in the model, show a JFileChooser dialog where the user gets to choose
     * the file to import from. If the file chosen was invalid, display an error
     * to the user and do nothing. If the file chosen was valid, this will
     * replace the model that this controller is tied to, and update the view by
     * clearing and populating the word entry table from the model. Finally,
     * this clears the filter text field to guarantee that the user will see all
     * the new entries in the table.
     */
    public void tryImportList()
    {
        if (model.getEntryCount() > 0)
        {
            // had to use showOptionDialog instead of showMessageDialog because it wouldn't let me specify the icon
            Object[] options =
            {
                UIManager.getString("OptionPane.yesButtonText"),
                UIManager.getString("OptionPane.noButtonText"),
            };
            int result = JOptionPane.showOptionDialog(view.getFrame(), "Loading a file will replace the current list. Any unsaved changes will be lost. Continue?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[1]
            );

            // did the user confirm to overwrite the list?
            if (result == JOptionPane.NO_OPTION)
            {
                return;
            }
        }

        SwingUtilities.invokeLater(() ->
        {
            try // try to get a valid file from the user
            {
                int returnVal = chooser.showOpenDialog(view.getFrame());
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    clearEntries();
                    model = WordlistModel.loadFromFile(chooser.getSelectedFile());
                    Logger.getLogger(GUIFrame.class.getName()).log(Level.INFO, "Loaded file with {0} words.", model.getEntryCount());
                    populateTableFromModel();
                }

            } catch (IOException | ClassNotFoundException ex) // we didn't get a valid file
            {
                JOptionPane.showMessageDialog(view.getFrame(), "Something went wrong when loading the file.", "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(AppFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }

    /**
     * Adds all rows from the model to the table model. It is recommended to
     * invoke <code>clearEntries()</code> before invoking this method, or there
     * might be inconsistencies with the table model and this controller's
     * model. Also updates the entry count and clears the filter so that the
     * user is guaranteed to see the new entries.
     */
    public void populateTableFromModel()
    {
        model.getAllWordsAsStream().sequential().forEach(word ->
        {
            addTableRowFromWord(word);
            mainController.getQuizWordlistController().addWordEntryToQuizWordlist(word);
        });
        updateEntryCount();
        view.getEditWordlistTab().getFilterField().setText("");
    }

    /**
     * If there are any words in the table, ask the user for confirmation to
     * clear it. If they confirm, invoke the <code>clearEntries()</code> method.
     * If the table is empty, this method does nothing.
     */
    public void tryClearEntries()
    {
        if (model.getEntryCount() == 0)
        {
            return;
        }

        SwingUtilities.invokeLater(() ->
        {
            int result = JOptionPane.showConfirmDialog(this.view.getFrame(),
                    "Are you sure you want to clear the table?",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION)
            {
                clearEntries();
            }
        });

    }

    /**
     * Removes all words from the model and view.getEditWordlistTab().
     */
    public void clearEntries()
    {
        this.model.clear();
        DefaultTableModel tableModel = (DefaultTableModel) this.view.getEditWordlistTab().getWordlistTable().getModel();
        tableModel.setRowCount(0);
        this.updateEntryCount();
    }

    /**
     * Shows a JFileChooser dialog for the user to select a destination and file
     * name for exporting the table to a file. The file extension
     * <code>.gtl</code> will be automatically appended to the file name if the
     * user has not added it. If the user attempts to overwrite an existing
     * file, a confirmation dialog is shown. If the user cancels the overwriting
     * of the file, or an error occurred during saving, the JFileChooser is
     * shown again until a successful save, or until the user closes the window.
     * The actual saving of the file is performed in the model's
     * <code>saveToFile</code> method.
     */
    public void tryExportList()
    {
        SwingUtilities.invokeLater(() ->
        {

            while (true)
            {
                try
                {
                    int saveFileResult = chooser.showSaveDialog(view.getFrame());
                    if (saveFileResult == JFileChooser.APPROVE_OPTION)
                    {
                        File fileToBeSaved = chooser.getSelectedFile();

                        // see if we need to append .gtl
                        if (!chooser.getSelectedFile().getAbsolutePath().endsWith(".gtl"))
                        {
                            fileToBeSaved = new File(chooser.getSelectedFile() + ".gtl");
                        }

                        // ask for confirmation to overwrite
                        if (fileToBeSaved.exists())
                        {
                            int overWriteResult = JOptionPane.showConfirmDialog(view.getFrame(),
                                    String.format("<html>A file with the name %s exists already.<br/>Do you wish to overwrite it?", fileToBeSaved.getName()),
                                    "Overwrite",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
                            if (overWriteResult == JOptionPane.YES_OPTION)
                            {
                                model.saveToFile(fileToBeSaved);
                                break;
                            } else
                            {
                                chooser.setVisible(true);
                            }
                        } else // no overwriting
                        {
                            model.saveToFile(fileToBeSaved);
                            break;
                        }
                    } else // user has chosen cancel
                    {
                        break;
                    }
                } catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(view.getFrame(),
                            "Couldn't save the file. The destination folder might be full or you don't have write access.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(AppFrameController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }

    /**
     * Listens to double clicks that the user did on the table. If they occured
     * on the empty row, invoke <code>openNewEntryForm>/code>, otherwise
     * invoke <code>tryOpenEditEntryForm</code>.
     */
    private class WordlistTableDoubleClickListener extends MouseAdapter
    {

        @Override
        public void mouseClicked(MouseEvent evt)
        {
            if (evt.getClickCount() == 2)
            {
                JTable table = (JTable) evt.getSource();
                Point p = evt.getPoint();
                int row = table.rowAtPoint(p);

                // did we click on the empty row? if  so, open fram to add a new entry
                if (row == table.getModel().getRowCount())
                {
                    openNewEntryForm();
                } else
                {
                    tryOpenEditEntryForm();
                }
            }
        }
    }

// ACTIONS
    /**
     * When the NewEntryAction is invoked, invoke the
     * <code>openNewEntryForm</code> method.
     */
    private class NewEntryAction extends AbstractAction
    {

        public NewEntryAction()
        {
            super("New Entry...");
            putValue(SMALL_ICON, GUIHelpers.getIconFromFileName("add.png"));
            putValue(SHORT_DESCRIPTION, "Opens a form for adding a new entry to the list.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            openNewEntryForm();
        }
    }

    /**
     * When the EditEntryAction is invoked, invoke the
     * <code>tryEditEntryForm</code> method.
     */
    private class EditEntryAction extends AbstractAction
    {

        public EditEntryAction()
        {
            super("Edit Entry...", GUIHelpers.getIconFromFileName("edit.png"));
            putValue(SHORT_DESCRIPTION, "Edits the selected entry from the list.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            tryOpenEditEntryForm();
        }
    }

    /**
     * When the DeleteEntryAction is invoked, invoke the
     * <code>tryDeleteEntries</code> method.
     */
    private class DeleteEntryAction extends AbstractAction
    {

        public DeleteEntryAction()
        {
            super("Delete Entry", GUIHelpers.getIconFromFileName("delete.png"));
            putValue(SHORT_DESCRIPTION, "Deletes the selected entry/entries from the list.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            tryDeleteEntries();
        }
    }

    /**
     * When the ClearEntryAction is invoked, invoke the
     * <code>tryClearEntries</code> method.
     */
    private class ClearListAction extends AbstractAction
    {

        public ClearListAction()
        {
            super("Clear table", GUIHelpers.getIconFromFileName("trash.png"));
            putValue(SHORT_DESCRIPTION, "Clears the list.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            tryClearEntries();
        }
    }

    /**
     * When the ImportListAction is invoked, invoke the
     * <code>tryImportList</code> method.
     */
    private class ImportListAction extends AbstractAction
    {

        public ImportListAction()
        {
            super("Load...", GUIHelpers.getIconFromFileName("upload.png"));
            putValue(SHORT_DESCRIPTION, "Opens a file chooser dialog for you to select a file to load the word list from.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            tryImportList();
        }
    }

    /**
     * When the ExportListAction is invoked, invoke the
     * <code>tryExportList</code> method.
     */
    private class ExportListAction extends AbstractAction
    {

        public ExportListAction()
        {
            super("Save...", GUIHelpers.getIconFromFileName("download.png"));
            putValue(SHORT_DESCRIPTION, "Save the list to a file.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            tryExportList();
        }
    }

    /**
     * When the PrintListAction is invoked, show a print dialog for the user.
     * The width of the table will be adjusted to fit the width of the paper.
     */
    private class PrintListAction extends AbstractAction
    {

        public PrintListAction()
        {
            super("Print...", GUIHelpers.getIconFromFileName("print.png"));
            putValue(SHORT_DESCRIPTION, "Opens the system's dialog for printing the list.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                view.getEditWordlistTab().getWordlistTable().print(PrintMode.FIT_WIDTH);
            } catch (PrinterException ex)
            {
                JOptionPane.showMessageDialog(view.getFrame(), "<html>Something went wrong while printing. The returned error was:<br/>" + ex + "</html>");
                Logger.getLogger(AppFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
