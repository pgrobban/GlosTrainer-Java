package glostrainer.controller;

import glostrainer.model.IModel;
import glostrainer.view.IView;

/**
 * Interface for Controllers. Controllers have a view and model. that they are
 * connected to.
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public interface IController 
{
    
    public IView getView();
    
    public IModel getModel();
    
}
