package glostrainer.view;

import java.awt.*;
import java.awt.event.ItemEvent;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EditWordlistPanel extends JPanel
{

    // word list table column indices
    public static int SWEDISH_DICTIONARY_FORM_COLUMN = 0;
    public static int DEFINITION_COLUMN = 1;
    public static int WORD_CLASS_COLUMN = 2;
    public static int OPTIONAL_FORMS_COLUMN = 3;

    private static final String TABLE_TOOLTIP = "<html>Click the <i>New Entry</i> button or "
            + "double-click on the empty row to add a new entry.<br/>"
            + "To edit an entry, double-click on the corresponding row or select "
            + "the row with your mouse or keyboard and click the <i>Edit Entry</i> button.</html>";

    public EditWordlistPanel()
    {
        initComponents();
        initLayout();
    }

    private void initComponents()
    {
        entryPanel = new JPanel();
        newEntryButton = new JButton();
        editEntryButton = new JButton();
        deleteEntryButton = new JButton();
        filterTextField = new JTextField();
        exactMatchCheckBox = new JCheckBox("Exact match", true);
        allFormsCheckButton = new JCheckBox("Include opt. forms", true);
        wordlistTable = new NewLineTable();
        wordlistScrollPane = new JScrollPane(wordlistTable);
        bottomPanel = new JPanel();
        exportButton = new JButton();
        importButton = new JButton();
        printListButton = new JButton();
        newListButton = new JButton();

        entryCountLabel = new JLabel();

        entryPanel.setBorder(new CustomTitledBorder("Entry"));

        filterLabel = new JLabel("Filter:");
        filterTextField.setText("");

        exactMatchCheckBox.addItemListener((ItemEvent e) ->
        {
            DefaultTableModel tm = (DefaultTableModel) wordlistTable.getModel();
            tm.fireTableDataChanged();
        });
        allFormsCheckButton.addItemListener((ItemEvent e) ->
        {
            DefaultTableModel tm = (DefaultTableModel) wordlistTable.getModel();
            tm.fireTableDataChanged();
        });
        allFormsCheckButton.setToolTipText("Search in all forms. If this is turned off, "
                + "the search will only look for the Swedish Dictionary Forms and Definition fields.");

        wordlistTable.setRowSelectionAllowed(true);
        wordlistTable.setToolTipText(TABLE_TOOLTIP);
        wordlistTable.setModel(new DefaultTableModel(
                new Object[][]
                {
                },
                new String[]
                {
                    "Swedish dictionary form", "Definition", "Word class", "Other forms"
                }
        )
        {
            @Override
            public Class getColumnClass(int columnIndex)
            {
                return String.class;
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex)
            {
                return false;
            }
        });

        //wordlistTable.setFillsViewportHeight(true);
        wordlistTable.getColumnModel().getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        wordlistTable.getColumnModel().getColumn(SWEDISH_DICTIONARY_FORM_COLUMN).setPreferredWidth(100);
        wordlistTable.getColumnModel().getColumn(OPTIONAL_FORMS_COLUMN).setPreferredWidth(250);
        wordlistTable.setFont(wordlistTable.getFont().deriveFont(Font.PLAIN, 14));

        bottomPanel.setBorder(new CustomTitledBorder("List"));

    }

    private void initLayout()
    {
        // panel entry layout
        GroupLayout entryPanelLayout = new GroupLayout(entryPanel);
        entryPanel.setLayout(entryPanelLayout);
        entryPanelLayout.setAutoCreateGaps(true);
        entryPanelLayout.setAutoCreateContainerGaps(true);
        entryPanelLayout.setHorizontalGroup(entryPanelLayout.createSequentialGroup()
                .addComponent(newEntryButton)
                .addComponent(editEntryButton)
                .addComponent(deleteEntryButton)
                .addGap(0, 100, Short.MAX_VALUE)
                .addComponent(filterLabel)
                .addComponent(filterTextField, 100, 150, Short.MAX_VALUE)
                .addGroup(entryPanelLayout.createParallelGroup()
                        .addComponent(allFormsCheckButton)
                        .addComponent(exactMatchCheckBox)
                )
        );
        entryPanelLayout.setVerticalGroup(entryPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(newEntryButton)
                .addComponent(editEntryButton)
                .addComponent(deleteEntryButton)
                .addComponent(filterTextField)
                .addComponent(filterLabel)
                .addComponent(allFormsCheckButton)
                .addGroup(entryPanelLayout.createSequentialGroup()
                        .addComponent(allFormsCheckButton)
                        .addComponent(exactMatchCheckBox)
                )
        );

        // bottom panel layout
        GroupLayout importExportLayout = new GroupLayout(bottomPanel);
        bottomPanel.setLayout(importExportLayout);
        importExportLayout.setAutoCreateGaps(true);
        importExportLayout.setAutoCreateContainerGaps(true);
        importExportLayout.setHorizontalGroup(importExportLayout.createSequentialGroup()
                .addComponent(importButton)
                .addComponent(exportButton)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, 50)
                .addComponent(printListButton)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(entryCountLabel)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(newListButton)
        );
        importExportLayout.setVerticalGroup(importExportLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(importButton)
                .addComponent(exportButton)
                .addComponent(printListButton)
                .addComponent(entryCountLabel)
                .addComponent(newListButton)
        );

        // main layout
        GroupLayout mainLayout = new GroupLayout(this);
        this.setLayout(mainLayout);

        mainLayout.setVerticalGroup(
                mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(entryPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(wordlistScrollPane))
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

        mainLayout.setHorizontalGroup(
                mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(entryPanel)
                        .addComponent(wordlistScrollPane)
                        .addComponent(bottomPanel)));
    }

    public JLabel getEntryCountLabel()
    {
        return entryCountLabel;
    }

    /**
     * @return the allFormsCheckButton
     */
    public JCheckBox getAllFormsCheckButton()
    {
        return allFormsCheckButton;
    }

    /**
     * @return the caseSensitiveCheckButton
     */
    public JCheckBox getExactMatchCheckButton()
    {
        return exactMatchCheckBox;
    }

    /**
     * @return the deleteEntryButton
     */
    public JButton getDeleteEntryButton()
    {
        return deleteEntryButton;
    }

    /**
     * @return the editEntryButton
     */
    public JButton getEditEntryButton()
    {
        return editEntryButton;
    }

    /**
     * @return the exportButton
     */
    public JButton getExportButton()
    {
        return exportButton;
    }

    /**
     * @return the importButton
     */
    public JButton getImportButton()
    {
        return importButton;
    }

    public JButton getClearListButton()
    {
        return newListButton;
    }

    /**
     * @return the newEntryButton
     */
    public JButton getNewEntryButton()
    {
        return newEntryButton;
    }

    /**
     * @return the searchField
     */
    public JTextField getFilterField()
    {
        return filterTextField;
    }

    /**
     * @return the wordlistTable
     */
    public JTable getWordlistTable()
    {
        return wordlistTable;
    }

    /**
     *
     * @return
     */
    public JButton getPrintListButton()
    {
        return printListButton;
    }

    private JCheckBox allFormsCheckButton;
    private JCheckBox exactMatchCheckBox;
    private JButton deleteEntryButton;
    private JButton editEntryButton;
    private JPanel entryPanel;
    private JButton exportButton;
    private JButton importButton;
    private JPanel bottomPanel;
    private JButton newEntryButton;
    private JButton newListButton;
    private JButton printListButton;
    private JTextField filterTextField;
    private JScrollPane wordlistScrollPane;
    private JTable wordlistTable;
    private JLabel entryCountLabel;
    private JLabel filterLabel;

}
