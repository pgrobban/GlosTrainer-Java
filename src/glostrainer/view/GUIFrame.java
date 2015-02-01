package glostrainer.view;

import java.awt.*;
import javax.swing.*;

/**
 * Represents the main GUI of the application. For window manipulations, use the
 * <code>getFrame()</code> method. The class exposes methods for retrieving the
 * buttons and other components in the GUI.
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class GUIFrame implements IView
{

    private JFrame frame;

    public GUIFrame()
    {
        SwingUtilities.invokeLater(() ->
        {
            initFrame();
            initComponents();
            initLayout();

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private void initFrame()
    {
        frame = new JFrame("Robban's GlosTrainer v 1.0");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JDesktopPane desktop = new JDesktopPane();
        //desktop.add(newEditFrame.getFrame());
        frame.setContentPane(desktop);

        frame.setMinimumSize(new Dimension(650, 500));
        frame.setPreferredSize(new Dimension(800, 600));
    }

    private void initComponents()
    {
        editWordlistTab = new EditWordlistPanel();
        appTabbedPane = new JTabbedPane();

        appTabbedPane.addTab("Word list ", GUIHelpers.getIconFromFileName("edit.png"), editWordlistTab);
    }

    public void addQuizTab(QuizWordlistPanel p)
    {
        this.appQuizTab = p;
        appTabbedPane.addTab("Quiz ", GUIHelpers.getIconFromFileName("quiz.png"), p);
    }

    private void initLayout()
    {
        // main layout
        GroupLayout mainLayout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(mainLayout);

        mainLayout.setVerticalGroup(
                mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(appTabbedPane, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE))
        );

        mainLayout.setHorizontalGroup(
                mainLayout.createSequentialGroup()
                .addGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(appTabbedPane, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
                ));
    }

    /**
     * Returns a reference to the underlying JFrame of this view to provide
     * window manipulations.
     *
     * @return
     */
    public JFrame getFrame()
    {
        return this.frame;
    }

    public EditWordlistPanel getEditWordlistTab()
    {
        return this.editWordlistTab;
    }

    private EditWordlistPanel editWordlistTab;
    private QuizWordlistPanel appQuizTab;
    private JTabbedPane appTabbedPane;

}
