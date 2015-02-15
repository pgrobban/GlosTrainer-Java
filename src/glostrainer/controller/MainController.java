package glostrainer.controller;

import glostrainer.model.NewOrEditEntryModel;
import glostrainer.model.QuizWordlistModel;
import glostrainer.model.WordlistModel;
import glostrainer.view.NewOrEditEntryForm;
import glostrainer.view.GUIFrame;
import glostrainer.view.QuizWordlistPanel;
import javax.swing.SwingUtilities;

/**
 * Main controller for all controllers.
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
//TODO: make this more useful/clean
public class MainController
{

    private final AppFrameController appFrameController;

    private final NewOrEditEntryFormController newOrEditEntryFormController;

    private final QuizWordlistController quizWordlistController;

    public MainController()
    {

        WordlistModel wlModel = new WordlistModel();
        GUIFrame gui = new GUIFrame();

        this.appFrameController = new AppFrameController(this, wlModel, gui);

        this.quizWordlistController = new QuizWordlistController(
                this,
                new QuizWordlistModel(wlModel),
                new QuizWordlistPanel());

        this.newOrEditEntryFormController = new NewOrEditEntryFormController(
                this,
                new NewOrEditEntryModel(),
                new NewOrEditEntryForm(gui.getFrame())
        );

    }

    /**
     * @return the appFrameController
     */
    public AppFrameController getAppFrameController()
    {
        return appFrameController;
    }

    /**
     * @return the newOrEditEntryFormController
     */
    public NewOrEditEntryFormController getNewOrEditEntryFormController()
    {
        return newOrEditEntryFormController;
    }

    public QuizWordlistController getQuizWordlistController()
    {
        return quizWordlistController;
    }

}
