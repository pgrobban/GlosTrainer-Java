package glostrainer.controller;

import glostrainer.LibHelpers;
import glostrainer.model.NewOrEditEntryModel;
import glostrainer.model.WordEntry;
import glostrainer.model.WordlistModel;
import glostrainer.view.NewOrEditEntryFrame;
import glostrainer.view.WordlistFrame;
import static glostrainer.view.WordlistFrame.DEFINITION_COLUMN;
import static glostrainer.view.WordlistFrame.OPTIONAL_FORMS_COLUMN;
import static glostrainer.view.WordlistFrame.SWEDISH_DICTIONARY_FORM_COLUMN;
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
import java.text.Collator;
import java.util.Locale;
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
 * application.
 * It manages a GUI, which here is an instance of a <code>WordlistFrame</code>, and
 * a word list model class, <code>WordlistModel</code>.
 * The <code>WordlistFrameController</code> responds to GUI events such as button
 * clicks and key presses, and reacts accordingly.
 * Since the table view in the GUI cannot be manipulated directly, the
 * <code>WordListFrameController</code> class manages a
 * <code>NewOrEditEntryFrameController</code> instance to show dialogs for the
 * user to create or edit word entries.
 * 
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class WordlistFrameController
{
    /**  
     * Since the table view in the GUI cannot be manipulated directly, the
     * <code>WordListFrameController</code> class manages a
     * <code>NewOrEditEntryFrameController</code> instance to manipulate dialogs for the
     * user to create or edit word entries.
     */
    private NewOrEditEntryFrameController newOrEditEntryFrameController;
    
    /**
     * 
     */
    private WordlistModel model;
    
    /**
     * 
     */
    private WordlistFrame view;

    /**
     * Creates a new WordlistFrameController with the given model and view.
     * @param model
     * @param view
     */
    public WordlistFrameController(WordlistModel model, WordlistFrame view)
    {
        this.model = model;
        this.view = view;

        SwingUtilities.invokeLater(() ->
        {
            this.newOrEditEntryFrameController = new NewOrEditEntryFrameController(
                    new NewOrEditEntryModel(),
                    new NewOrEditEntryFrame(this.view.getFrame())
            );

            setupViewEvents();

            this.view.getFrame().setVisible(true);
        });
    }

    private void setupViewEvents()
    {
        this.view.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.view.getFrame().addWindowListener(new WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                int result = JOptionPane.showConfirmDialog(view.getFrame(),
                        "Do you really want to close the application? All unsaved changes will be lost.",
                        "Save",
                        JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION)
                {
                    System.exit(0);
                }
            }
        });

        this.view.getNewEntryButton().setAction(new NewEntryAction());
        this.view.getNewEntryButton().requestFocus();
        this.view.getEditEntryButton().setAction(new EditEntryAction());
        this.view.getEditEntryButton().setEnabled(false);
        this.view.getDeleteEntryButton().setAction(new DeleteEntryAction());
        this.view.getDeleteEntryButton().setEnabled(false);

        this.view.getImportButton().setAction(new ImportListAction());
        this.view.getExportButton().setAction(new ExportListAction());
        this.view.getPrintListButton().setAction(new PrintListAction());
        this.view.getClearListButton().setAction(new ClearListAction());

        final JTable table = this.view.getWordlistTable();
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
                    case KeyEvent.VK_SPACE:
                    case KeyEvent.VK_ENTER:
                        if (view.getWordlistTable().getSelectedRows()[0] == model.getWordCount())
                        {
                            openNewEntryFrame();
                        } else
                        {
                            tryOpenEditEntryFrame();
                        }
                        break;
                }
            }
        });
        DefaultListSelectionModel listSelectionModel = (DefaultListSelectionModel) table.getSelectionModel();
        listSelectionModel.addListSelectionListener((ListSelectionEvent e) ->
        {
            if (table.getSelectedRowCount() == 0)
            {
                view.getEditEntryButton().setEnabled(false);
                view.getDeleteEntryButton().setEnabled(false);
            } else
            {
                if (table.getSelectedRows()[0] == table.getRowCount() - 1)
                {
                    view.getEditEntryButton().setEnabled(false);
                    view.getDeleteEntryButton().setEnabled(false);
                } else
                {
                    view.getEditEntryButton().setEnabled(true);
                    view.getDeleteEntryButton().setEnabled(true);
                }
            }
        });
        this.view.getWordlistTable().addMouseListener(new WordlistTableMouseListener());

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(view.getWordlistTable().getModel());
        sorter.setComparator(DEFINITION_COLUMN, (String o1, String o2) ->
        {
            Collator collator = Collator.getInstance(new Locale("sv", ""));
            return collator.compare(o1, o2);
        });
        view.getWordlistTable().setRowSorter(sorter);

        RowFilter<TableModel, Integer> rf = new RowFilter()
        {

            @Override
            public boolean include(RowFilter.Entry entry)
            {
                String textToLookFor = view.getFilterField().getText();
                if (textToLookFor.equals(""))
                {
                    return true;
                }

                if (!view.getExactMatchCheckButton().isSelected())
                {
                    boolean wordIsInDictionaryFormOrDefinitionColumn
                            = entry.getStringValue(SWEDISH_DICTIONARY_FORM_COLUMN).contains(textToLookFor)
                            || entry.getStringValue(DEFINITION_COLUMN).contains(textToLookFor);

                    if (!view.getAllFormsCheckButton().isSelected())
                    {
                        return wordIsInDictionaryFormOrDefinitionColumn;
                    } else
                    {

                        if (wordIsInDictionaryFormOrDefinitionColumn)
                        {
                            return true;
                        }

                        return entry.getStringValue(OPTIONAL_FORMS_COLUMN).contains(textToLookFor);
                    }
                } else
                {
                    boolean wordIsInDictionaryFormOrDefinitionColumn
                            = entry.getStringValue(SWEDISH_DICTIONARY_FORM_COLUMN).equalsIgnoreCase(textToLookFor)
                            || entry.getStringValue(DEFINITION_COLUMN).equalsIgnoreCase(textToLookFor);
                    if (!view.getAllFormsCheckButton().isSelected())
                    {
                        return wordIsInDictionaryFormOrDefinitionColumn;
                    } else
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

        this.view.getFilterField().getDocument().addDocumentListener(
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

    ;

    public void openNewEntryFrame()
    {
        this.newOrEditEntryFrameController.openNewEntryFrame();
        if (this.newOrEditEntryFrameController.wordWasSaved)
        {
            WordEntry savedWord = this.newOrEditEntryFrameController.getModel().getCurrentWord();
            WordEntry wordToAdd = new WordEntry(
                    savedWord.getWordClass(),
                    savedWord.getSwedishDictionaryForm(),
                    savedWord.getDefinition(),
                    savedWord.getOptionalForms(),
                    savedWord.getUserNotes()
            );
            this.model.addWord(wordToAdd);
            DefaultTableModel tableModel = (DefaultTableModel) view.getWordlistTable().getModel();

            // because the NewLineTable will remove the empty row for us, we don't need to remove it row here
            // add new row
            tableModel.addRow(new String[]
            {
                savedWord.getSwedishDictionaryForm(),
                savedWord.getDefinition(),
                savedWord.getWordClass().toString(),
                savedWord.getOptionalFormsAsString()
            });
            // because the NewLineTable will add an empty line for us, we don't need to add an empty row here

            this.updateEntryCount();
        }
    }

    /**
     *
     * @param selectedIndex
     */
    public void openEditEntryFrameWithWordAtSelectedIndex(int selectedIndex)
    {
        this.newOrEditEntryFrameController.openEditEntryFrame(this.model.getWordAtIndex(selectedIndex));
        if (this.newOrEditEntryFrameController.wordWasSaved)
        {
            WordEntry savedWord = this.newOrEditEntryFrameController.getModel().getCurrentWord();
            this.model.replaceWordAtIndex(selectedIndex, savedWord);

            // edit table entry
            DefaultTableModel tableModel = (DefaultTableModel) view.getWordlistTable().getModel();
            tableModel.setValueAt(savedWord.getSwedishDictionaryForm(), selectedIndex, WordlistFrame.SWEDISH_DICTIONARY_FORM_COLUMN);
            tableModel.setValueAt(savedWord.getDefinition(), selectedIndex, WordlistFrame.DEFINITION_COLUMN);
            tableModel.setValueAt(savedWord.getWordClass(), selectedIndex, WordlistFrame.WORD_CLASS_COLUMN);
            tableModel.setValueAt(savedWord.getOptionalFormsAsString(), selectedIndex, WordlistFrame.OPTIONAL_FORMS_COLUMN);
        }
    }

    /**
     *
     */
    public void tryOpenEditEntryFrame()
    {
        // Display an error if the user hasn't selected any rows, or more than one row.
        JTable wordListTable = this.view.getWordlistTable();
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
        /*
         OK, the user has only selected one row. But we need to be careful here. if the 
         user selects a row to edit in the view (the table), it might not correspond 
         to the correct item in the model if the table has been sorted or filtered, so we need to 
         convert the user's selected index to the model's index.
         */
        int selectedIndexInModel = wordListTable.convertRowIndexToModel(selectedTableRowIndicesInView[0]);
        // now we can open the edit frame
        openEditEntryFrameWithWordAtSelectedIndex(selectedIndexInModel);
    }

    public void tryDeleteEntries()
    {
        JTable wordListTable = this.view.getWordlistTable();
        int[] selectedIndices = wordListTable.getSelectedRows();
        if (selectedIndices.length == 0 || selectedIndices[0] == this.view.getWordlistTable().getRowCount() - 1)
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
                // delete indexes in reverse order because we need to delete entries in the view's table
                for (int i = selectedIndices.length - 1; i >= 0; i--)
                {
                    /*
                     Just as when we did editing, we must make sure that the selected row corresponds
                     to the right item in the model list, in case the user has sorted or filtered results.
                     */
                    int selectedIndexInModel = wordListTable.convertRowIndexToModel(selectedIndices[i]);
                    deleteWordAtIndex(selectedIndexInModel);
                }
            }
        }
    }

    public void deleteWordAtIndex(int indexToDelete)
    {
        this.model.removeWordAtIndex(indexToDelete);
        // delete table entry
        DefaultTableModel tableModel = (DefaultTableModel) view.getWordlistTable().getModel();
        tableModel.removeRow(indexToDelete);
        this.updateEntryCount();
    }

    public void updateEntryCount()
    {
        int totalEntryCount = this.model.getWordCount();
        String entryOrEntries = (totalEntryCount == 1) ? "entry" : "entries";
        int filteredEntryCount = getFilteredRowCount();
        String filteredEntryOrEntries = (filteredEntryCount == 1) ? "entry" : "entries";

        if (this.view.getFilterField().getText().equals("")) // are we filtering something?
        {
            this.view.getEntryCountLabel().setText(String.format("%d %s in list", totalEntryCount, entryOrEntries));
        } else
        {
            this.view.getEntryCountLabel().setText(String.format("%d %s in list, filtered %d %s in view", totalEntryCount, entryOrEntries, filteredEntryCount, filteredEntryOrEntries));
        }

    }

    private int getFilteredRowCount()
    {
        int filteredRowCount = 0;
        for (int i = 0; i < this.view.getWordlistTable().getModel().getRowCount(); i++)
        {
            if (this.view.getWordlistTable().convertRowIndexToView(i) != -1)
            {
                filteredRowCount++;
            }
        }
        return filteredRowCount;
    }

    public void tryImportList()
    {
        // had to use showOptionDialog instead of showMessageDialog because it wouldn't let me specify the icon
        Object[] options =
        {
            "Yes", "No"
        };
        int result = JOptionPane.showOptionDialog(view.getFrame(), "Loading a file will replace the current list. Any unsaved changes will be lost. Continue?",
                "Warning",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, options, options[1]
        );
        if (result == JOptionPane.YES_OPTION)
        {
            try
            {
                JFileChooser chooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "GlosTrainer list files", "gtl");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(view.getFrame());
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    clearList();
                    model = WordlistModel.loadFromFile(chooser.getSelectedFile());
                    System.out.println(model.getWordCount());
                    populateListFromModel();
                }
            } catch (IOException | ClassNotFoundException ex)
            {
                JOptionPane.showMessageDialog(view.getFrame(), "Something went wrong when loading the file.", "Error", JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(WordlistFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void populateListFromModel()
    {

        for (int i = 0; i < model.getWordCount(); i++)
        {
            DefaultTableModel tbm = (DefaultTableModel) this.view.getWordlistTable().getModel();
            WordEntry w = model.getWordAtIndex(i);
            System.out.println(w);
            tbm.addRow(new String[]
            {
                w.getSwedishDictionaryForm(),
                w.getDefinition(),
                w.getWordClass().toString(),
                w.getOptionalFormsAsString()
            });
        }
        updateEntryCount();
    }

    public void tryExportList()
    {
        try
        {
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "GlosTrainer list files", "gtl");
            chooser.setFileFilter(filter);
            int result = chooser.showSaveDialog(view.getFrame());
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File fileToBeSaved = chooser.getSelectedFile();

                if (!chooser.getSelectedFile().getAbsolutePath().endsWith(".gtl"))
                {
                    fileToBeSaved = new File(chooser.getSelectedFile() + ".gtl");
                }
                model.saveToFile(fileToBeSaved);
            }
        } catch (IOException ex)
        {
            JOptionPane.showMessageDialog(view.getFrame(),
                    "Couldn't save the file. The destination folder might be full or you don't have write access.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(WordlistFrameController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     *
     */
    private class NewEntryAction extends AbstractAction
    {

        public NewEntryAction()
        {
            super("New Entry");
            putValue(SMALL_ICON, LibHelpers.getIconFromFileName("add.png"));
            putValue(SHORT_DESCRIPTION, "Opens a dialog for adding a new entry to the list.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            openNewEntryFrame();
        }
    }

    /**
     *
     */
    private class EditEntryAction extends AbstractAction
    {

        public EditEntryAction()
        {
            super("Edit Entry", LibHelpers.getIconFromFileName("edit.png"));
            putValue(SHORT_DESCRIPTION, "Edits the selected entry from the list.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            tryOpenEditEntryFrame();
        }
    }

    /**
     *
     */
    private class DeleteEntryAction extends AbstractAction
    {

        public DeleteEntryAction()
        {
            super("Delete Entry", LibHelpers.getIconFromFileName("delete.png"));
            putValue(SHORT_DESCRIPTION, "Deletes the selected entry/entries from the list.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            tryDeleteEntries();
        }
    }

    private class WordlistTableMouseListener extends MouseAdapter
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
                    openNewEntryFrame();
                } else
                {
                    tryOpenEditEntryFrame();
                }
            }
        }
    }

    private class ClearListAction extends AbstractAction
    {

        public ClearListAction()
        {
            super("Clear list", LibHelpers.getIconFromFileName("trash.png"));
            putValue(SHORT_DESCRIPTION, "Clears the list.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            tryClearList();
        }
    }

    public void tryClearList()
    {
        int result = JOptionPane.showConfirmDialog(this.view.getFrame(),
                "Are you sure you want to clear the list?",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        if (result == JOptionPane.YES_OPTION)
        {
            clearList();
        }
    }

    public void clearList()
    {
        this.model.removeAllWords();
        DefaultTableModel tableModel = (DefaultTableModel) this.view.getWordlistTable().getModel();
        tableModel.setRowCount(0);
        this.updateEntryCount();
    }

    private class ImportListAction extends AbstractAction
    {

        public ImportListAction()
        {
            super("Load...", LibHelpers.getIconFromFileName("upload.png"));
            putValue(SHORT_DESCRIPTION, "Pick a file to open the list from.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            tryImportList();
        }
    }

    private class ExportListAction extends AbstractAction
    {

        public ExportListAction()
        {
            super("Save...", LibHelpers.getIconFromFileName("download.png"));
            putValue(SHORT_DESCRIPTION, "Save the list to a file.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            tryExportList();
        }
    }

    private class PrintListAction extends AbstractAction
    {

        public PrintListAction()
        {
            super("Print...", LibHelpers.getIconFromFileName("print.png"));
            putValue(SHORT_DESCRIPTION, "Opens the system's dialog for printing the list.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            try
            {
                view.getWordlistTable().print(PrintMode.FIT_WIDTH);
            } catch (PrinterException ex)
            {
                JOptionPane.showMessageDialog(view.getFrame(), "<html>Something went wrong while printing. Give this message to Robban:<br/>" + ex + "</html>");
                Logger.getLogger(WordlistFrameController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
