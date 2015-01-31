package glostrainer.view;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class QuizWordlistPanel extends JPanel
{
    
    public QuizWordlistPanel()
    {
        initComponents();
        initLayout();
    }

    private void initComponents()
    {
        helperLabel = new JLabel("Select which words to include in the quiz and then click Start Quiz to begin.");
        this.add(helperLabel);
    }

    private void initLayout()
    {
    }
    
    private JLabel helperLabel;
    
}
