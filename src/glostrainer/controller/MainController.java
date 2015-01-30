package glostrainer.controller;

import glostrainer.model.NewOrEditEntryModel;
import glostrainer.model.WordlistModel;
import glostrainer.view.NewOrEditEntryForm;
import glostrainer.view.WordlistFrame;

/**
 * Main controller for all controllers.
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
//TODO: make this more useful/clean
public class MainController
{

    private final WordlistFrameController wordlistController;

    private final NewOrEditEntryFormController newOrEditEntryFormController;

    public MainController()
    {
        this.wordlistController = new WordlistFrameController(
                this,
                new WordlistModel(),
                new WordlistFrame());
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

}
