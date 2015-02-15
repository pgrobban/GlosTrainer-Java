package glostrainer.controller;

import glostrainer.model.IModel;
import glostrainer.view.IView;

/**
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public abstract class AbstractController implements IController
{
    
    private MainController mainController;
    private IModel model;
    private IView view;
    
    public AbstractController(MainController mainController, IModel model, IView view)
    {
        this.mainController = mainController;
        this.model = model;
        this.view = view;
    }
    
    public MainController getMainController()
    {
        return this.mainController;
    }
    
}
