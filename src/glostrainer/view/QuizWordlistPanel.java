package glostrainer.view;

import glostrainer.model.WordlistModel;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

/**
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class QuizWordlistPanel extends JPanel implements IView
{

    private WordlistModel wordlistModel;

    public QuizWordlistPanel()
    {
        SwingUtilities.invokeLater(() ->
        {
            initComponents();
            initLayout();
        });

    }

    private void initComponents()
    {
        helperLabel = new JLabel("<html>Select which words you would like to include in the quiz and then click the <strong>Start Quiz</strong> button below to begin.</html>");

        wordlistTable = new JTable();
        wordlistTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        wordlistScrollPane = new JScrollPane(wordlistTable);
        wordlistTable.setFillsViewportHeight(true);
        wordlistScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        wordlistTable.setFont(wordlistTable.getFont().deriveFont(Font.PLAIN, 14));

        bottomPanel = new JPanel();
        selectedWordsCountLabel = new JLabel();
        startQuizButton = new JButton();
        startQuizButton.setFont(startQuizButton.getFont().deriveFont(Font.PLAIN, 16));
    }

    public JTable getWordlistTable()
    {
        return this.wordlistTable;
    }

    public JButton getStartQuizButton()
    {
        return this.startQuizButton;
    }

    public JLabel getSelectedWordsCountLabel()
    {
        return this.selectedWordsCountLabel;
    }

    private void initLayout()
    {
        GroupLayout bottomPanelLayout = new GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        
        bottomPanelLayout.setHorizontalGroup(bottomPanelLayout.createSequentialGroup()
                .addComponent(selectedWordsCountLabel, 100, 200, 200)
                .addComponent(startQuizButton)
        );
        bottomPanelLayout.setVerticalGroup(bottomPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(selectedWordsCountLabel)
                .addComponent(startQuizButton)
        );

        GroupLayout mainLayout = new GroupLayout(this);
        this.setLayout(mainLayout);

        mainLayout.setVerticalGroup(
                mainLayout.createSequentialGroup()
                .addGap(GUIHelpers.INDENT_SIZE)
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(helperLabel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(GUIHelpers.INDENT_SIZE)
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(wordlistScrollPane))
                .addGap(GUIHelpers.INDENT_SIZE)
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(bottomPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(GUIHelpers.INDENT_SIZE)
        );

        mainLayout.setHorizontalGroup(
                mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(helperLabel)
                        .addComponent(wordlistScrollPane)
                        .addComponent(bottomPanel)
                ));
    }

    private JLabel helperLabel;

    private JTable wordlistTable;
    private JScrollPane wordlistScrollPane;

    private JPanel bottomPanel;
    private JLabel selectedWordsCountLabel;
    private JButton startQuizButton;

}
