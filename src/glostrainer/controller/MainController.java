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

    private final WordlistFrameController wordlistController;

    private final NewOrEditEntryFormController newOrEditEntryFormController;

    private final QuizWordlistController quizWordlistController;

    public MainController()
    {

        this.wordlistController = new WordlistFrameController(
                this,
                new WordlistModel(),
                new GUIFrame());
        this.quizWordlistController = new QuizWordlistController(
                this,
                new QuizWordlistModel(this.wordlistController.getModel()),
                new QuizWordlistPanel());
        this.newOrEditEntryFormController = new NewOrEditEntryFormController(
                this,
                new NewOrEditEntryModel(),
                new NewOrEditEntryForm(this.wordlistController.getView().getFrame())
        );

    }

    /**
     * @return the wordlistController
     */
    public WordlistFrameController getWordlistController()
    {
        return wordlistController;
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
