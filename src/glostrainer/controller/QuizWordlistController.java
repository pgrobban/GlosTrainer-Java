package glostrainer.controller;

import glostrainer.model.IModel;
import glostrainer.model.QuizWordlistModel;
import glostrainer.model.WordEntry;
import glostrainer.view.GUIHelpers;
import glostrainer.view.QuizWordlistPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class QuizWordlistController extends AbstractController
{
        
    private QuizWordlistModel model;
    private QuizWordlistPanel view;

    public QuizWordlistController(MainController mainController, 
            QuizWordlistModel model, 
            QuizWordlistPanel view)
    {
        super(mainController, model, view);
        this.model = model;
        this.view = view;
        SwingUtilities.invokeLater(() ->
        {
            setupQuizWordlistTable();
            setupViewEvents();
        });
    }

    private void setupQuizWordlistTable()
    {
        //int numberOfColumns = 4 + getMaximumOptionalFormsCount();
        int numberOfColumns = 21;
        String[] columns = new String[numberOfColumns];
        columns[2] = "Dict. form";
        columns[4] = "Definition";
        Arrays.fill(columns, 6, columns.length, "Opt. form");

        JTable table = this.view.getWordlistTable();

        table.setModel(new DefaultTableModel(
                new Object[0][0],
                columns
        )
        {
            @Override
            public Class getColumnClass(int columnIndex)
            {
                if (columnIndex == 0)
                {
                    return boolean.class;
                }
                return columnIndex % 2 == 1 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                if (columnIndex == 0)
                {
                    return true;
                }
                return columnIndex % 2 == 1;
            }
        });

        // first column
        TableColumn tc = table.getColumnModel().getColumn(0);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
        tc.setHeaderRenderer(new CheckBoxHeader(new MyItemListener(0)));
        tc.setMaxWidth(2 * GUIHelpers.CHECKBOX_SIZE);

        // make checkbox columns smaller
        for (int i = 1; i < numberOfColumns; i += 2)
        {
            table.getColumnModel().getColumn(i).setMaxWidth(GUIHelpers.CHECKBOX_SIZE);
            table.getColumnModel().getColumn(i + 1).setPreferredWidth(150);
        }

        // table headers with clickable checkboxes
        for (int column = 1; column < table.getColumnCount(); column += 2)
        {
            tc = table.getColumnModel().getColumn(column);
            tc.setCellEditor(table.getDefaultEditor(Boolean.class));
            tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
            tc.setHeaderRenderer(new CheckBoxHeader(new MyItemListener(column)));
        }
    }

    private void setupViewEvents()
    {
        view.getStartQuizButton().setAction(new StartQuizAction());
        setupTableRowSelect();
    }

    private void setupTableRowSelect()
    {
        JTable table = view.getWordlistTable();
        TableModel tableModel = table.getModel();
        tableModel.addTableModelListener((TableModelEvent e) ->
        {
            updateSelectedWordsCountLabel();

            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 0)
            {
                for (int i = 1; i < tableModel.getColumnCount(); i += 2)
                {
                    tableModel.setValueAt(tableModel.getValueAt(row, 0), row, i);
                }
            }
        });
    }

    public void addWordEntryToQuizWordlist(WordEntry word)
    {
        JTable table = this.view.getWordlistTable();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        List<Object> rowList = new ArrayList<>();
        rowList.add(true);
        rowList.add(true);
        rowList.add(word.getSwedishDictionaryForm());
        rowList.add(true);
        rowList.add(word.getDefinition());
        word.getOptionalForms().values().stream().forEach(of ->
        {
            rowList.add(true);
            rowList.add(of);
        });

        tableModel.addRow(rowList.toArray());
        updateSelectedWordsCountLabel();
    }

    public void editWordEntryInQuizWordlist(int modelRowIndex, WordEntry word)
    {
        JTable table = this.view.getWordlistTable();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        List<Object> rowList = new ArrayList<>();
        tableModel.setValueAt(true, modelRowIndex, 0);
        tableModel.setValueAt(true, modelRowIndex, 1);
        tableModel.setValueAt(word.getDefinition(), modelRowIndex, 1);
        tableModel.setValueAt(true, modelRowIndex, 3);
        tableModel.setValueAt(word.getDefinition(), modelRowIndex, 3);
        int nextIndex = 5;
        for (int i = 0; i < word.getOptionalForms().size(); i++)
        {
            tableModel.setValueAt(true, modelRowIndex, nextIndex++);
            tableModel.setValueAt(word.getOptionalFormsValuesAsArray()[i], modelRowIndex, nextIndex++);
        }
        updateSelectedWordsCountLabel();

    }

    /*
     private int getMaximumOptionalFormsCount()
     {
     int columnsToReturn;
     if (model.getWordlist().getEntryCount() == 0)
     {
     columnsToReturn = 0;
     } else if (model.getWordlist().getEntryCount() == 1)
     {
     columnsToReturn = 2 * model.getWordlist().getWordEntryAtIndex(0).getOptionalForms().size();
     } else
     {
     int maxOptionalWordForms = model.getWordlist().getAllWordsAsStream().max((w1, w2) -> Integer.compare(w1.getOptionalForms().size(), w2.getOptionalForms().size())).get().getOptionalForms().size();
     columnsToReturn = 2 * maxOptionalWordForms;
     }
     System.out.println(columnsToReturn);
     return columnsToReturn;
     }
     */
    @Override
    public QuizWordlistPanel getView()
    {
        return view;
    }

    @Override
    public IModel getModel()
    {
        return model;
    }

    public void deleteWordEntryAtIndex(int indexInModelToRemove)
    {
        DefaultTableModel tableModel = (DefaultTableModel) view.getWordlistTable().getModel();
        tableModel.removeRow(indexInModelToRemove);
        updateSelectedWordsCountLabel();
    }

    public void updateSelectedWordsCountLabel()
    {
        int selectedCount = 0;
        DefaultTableModel tableModel = (DefaultTableModel) view.getWordlistTable().getModel();
        for (int row = 0; row < tableModel.getRowCount(); row++)
        {
            for (int col = 1; col < tableModel.getColumnCount(); col += 2)
            {
                boolean checkboxChecked = tableModel.getValueAt(row, col) != null && (boolean) tableModel.getValueAt(row, col);
                if (checkboxChecked && tableModel.getValueAt(row, col+1) != null)
                {
                    selectedCount++;
                }
            }
        }

        view.getSelectedWordsCountLabel().setText(selectedCount + " word forms selected");
    }

    /**
     * @author Michael Dunn
     */
    class MyItemListener implements ItemListener
    {

        private int column;

        public MyItemListener(int column)
        {
            this.column = column;
        }

        @Override
        public void itemStateChanged(ItemEvent e)
        {
            Object source = e.getSource();
            JTable table = view.getWordlistTable();
            if (source instanceof AbstractButton == false)
            {
                return;
            }
            boolean checked = e.getStateChange() == ItemEvent.SELECTED;
            for (int row = 0; row < table.getRowCount(); row++)
            {
                table.setValueAt(checked, row, column);
            }
        }

    }

    /**
     * @author Michael Dunn
     */
    class CheckBoxHeader extends JCheckBox
            implements TableCellRenderer, MouseListener
    {

        protected int column;
        protected boolean mousePressed = false;

        public CheckBoxHeader(ItemListener itemListener)
        {
            this.addItemListener(itemListener);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column)
        {
            if (table != null)
            {
                JTableHeader header = table.getTableHeader();
                if (header != null)
                {
                    this.setForeground(header.getForeground());
                    this.setBackground(header.getBackground());
                    this.setFont(header.getFont());
                    header.addMouseListener(this);
                }
            }
            setColumn(column);
            this.setText("All");
            setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            return this;
        }

        protected void setColumn(int column)
        {
            this.column = column;
        }

        public int getColumn()
        {
            return column;
        }

        protected void handleClickEvent(MouseEvent e)
        {
            if (mousePressed)
            {
                mousePressed = false;
                JTableHeader header = (JTableHeader) (e.getSource());
                JTable tableView = header.getTable();
                TableColumnModel columnModel = tableView.getColumnModel();
                int viewColumn = columnModel.getColumnIndexAtX(e.getX());
                int column = tableView.convertColumnIndexToModel(viewColumn);

                if (viewColumn == this.column && e.getClickCount() == 1 && column != -1)
                {
                    doClick();
                }
            }
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            handleClickEvent(e);
            ((JTableHeader) e.getSource()).repaint();
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            mousePressed = true;
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
        }
    }

    private String[] getSelectedWordFormsAsArray()
    {
        List<String> resultList = new ArrayList<>();
        JTable table = view.getWordlistTable();
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        for (int row = 0; row < tableModel.getRowCount(); row++)
        {
            for (int column = 1; column < tableModel.getColumnCount(); column += 2)
            {
                Object checkboxOrNull = tableModel.getValueAt(row, column);
                if (checkboxOrNull == null)
                {
                    continue;
                }
                boolean checkboxChecked = (boolean) checkboxOrNull;

                String wordToAdd = (String) tableModel.getValueAt(row, column + 1);
                if (checkboxChecked && wordToAdd != null && !"".equals(wordToAdd))
                {
                    resultList.add(wordToAdd);
                }
            }
        }
        String[] resultArray = resultList.toArray(new String[resultList.size()]);
        System.out.println("Starting quiz with " + Arrays.toString(resultArray));
        return resultArray;
    }

    class StartQuizAction extends AbstractAction
    {

        public StartQuizAction()
        {
            super("Start Quiz");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            String[] words = getSelectedWordFormsAsArray();
            if (words.length < 5)
                JOptionPane.showMessageDialog(getMainController().getWordlistController().getView().getFrame(), 
                        "Please select at least 5 words to include for the quiz.", 
                        "Too few words selected", 
                        JOptionPane.ERROR_MESSAGE);
        }

    }

}
