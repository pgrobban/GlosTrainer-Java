package glostrainer;

import glostrainer.controller.WordlistFrameController;
import glostrainer.model.WordlistModel;
import glostrainer.view.WordlistFrame;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * The main class for the application.
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class Main
{

    /**
     * The starting point of the application. Sets the default Java Swing
     * Look & Feel to Nimbus, if it exists. Then, initializes the main 
     * controller for the application.
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        try
        {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // If Nimbus is not available, set the GUI to another look and feel
        }
        
        // create the main controller
        new WordlistFrameController(
                new WordlistModel(), 
                new WordlistFrame());

    }

}
