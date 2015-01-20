package glostrainer.controller;

import glostrainer.LibHelpers;
import glostrainer.model.NewOrEditEntryModel;
import glostrainer.model.WordEntry;
import glostrainer.model.WordClass;
import glostrainer.view.NewOrEditEntryForm;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// TODO: use SwingWorkers to safely open and close dialogs

/**
 * This controller handles interactions for the user with the
 NewOrEditEntryForm, a modal JDialog. 
 * There are two modes for this form, either for adding a new word or for editing
 * a word from an existing one. Since both cases deal with the same reference to
 * the frame in the view, we modify that reference (set visibility to true/false,
 * modify title and appropriate components) rather than creating new views
 * every time the user chooses to enter a new word or edit a word.
 * 
 * The model here represents the entered data from the GUI's JTextFiedls (and 
 * JComboBox for the word class etc).
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class NewOrEditEntryFormController
{

    /**
     * The model here represents the entered data from the GUI's JTextFiedls (and 
     * JComboBox for the word class etc).
     */
    private final NewOrEditEntryModel model;
    /**
     * The view contains a reference to a modal JDialog, which will be used
     * for the user to enter and display data.
     */
    private final NewOrEditEntryForm view;
    /**
     * Sets a flag to determine if the word entry was saved, e.g. when the user has
     * clicked the OK button. Use thhis flag to determine wether to update
     * the model after the form has been closed or not.
     */
    protected boolean wordEntryWasSaved;

    /**
     * Creates a new NewOrEditFrameController with the given model and view.
     *
     * @param model
     * @param view
     */
    public NewOrEditEntryFormController(NewOrEditEntryModel model, NewOrEditEntryForm view)
    {
        this.model = model;
        this.view = view;
        initComponents();
    }

    /**
     * Initializes the components' actions and requests focus for the dictionary
     * form text field.
     */
    private void initComponents()
    {
        view.getOkButton().setAction(new OkButtonAction());
        view.getDictionaryFormField().requestFocus();;
        view.getCancelButton().setAction(new CancelButtonAction());
        view.getWordClassComboBox().addItemListener((ItemEvent e) ->
        {
            WordClass newSelectedWordClass = (WordClass) view.getWordClassComboBox().getSelectedItem();
            this.model.setCurrentSelectedWordClass(newSelectedWordClass);
            this.view.setWordClassSpecificOptionalFields(newSelectedWordClass);
            this.view.getFrame().pack();
        });
        // patch the JTextArea so the Tab button can be used to change focus
        LibHelpers.patchFocus(view.getUserNotesTextArea());
    }
    
    /**
     * Opens a form for the user to make a new WordEntry.
     */
    public void openNewEntryForm()
    {
        this.wordEntryWasSaved = false;

        JDialog frame = view.getFrame();
        frame.setTitle("New Entry");
        frame.setLocationRelativeTo(null);

        view.getWordClassComboBox().setSelectedIndex(0);
        view.getDictionaryFormField().setText("");
        view.getDefinitionField().setText("");
        view.setWordClassSpecificOptionalFields(WordClass.values()[0]);

        for (JTextField optionalField : view.getOptionalFormTextFields())
        {
            optionalField.setText("");
        }

        frame.setVisible(true);
    }
    
    /**
     * Closes the frame. The method does not modify the <code>wordEntryWasSaved</code> 
     * flag.
     */
    public void closeForm()
    {
        SwingUtilities.invokeLater(() ->
        {
            view.getFrame().setVisible(false);
        });
    }
    
    /**
     * Opens the form in edit mode. This means that its componentss for user
     * entry will be pre-populated from the fields of the given word,
     * e.g.  the Swedish dictionary form text field will be set to the value returned by
     * <code>wordToEdit.getDictionaryFormField()</code>.
     * @param wordToEdit the word to edit.
     */
    public void openEditEntryFrame(WordEntry wordToEdit)
    {
        System.out.println("Opening Edit Entry Frame with " + wordToEdit);
        this.wordEntryWasSaved = false;

        JDialog frame = view.getFrame();
        frame.setTitle("Edit Entry");
        frame.setLocationRelativeTo(null);

        view.getWordClassComboBox().setSelectedItem(wordToEdit.getWordClass());
        view.getDictionaryFormField().setText(wordToEdit.getSwedishDictionaryForm());
        view.getDefinitionField().setText(wordToEdit.getDefinition());
        view.setWordClassSpecificOptionalFields(wordToEdit.getWordClass());

        for (int i = 0; i < wordToEdit.getOptionalForms().length; i++)
        {
            view.getOptionalFormTextFields()[i].setText(wordToEdit.getOptionalForms()[i]);
        }
        view.getFrame().setVisible(true);
    }
    
    /**
     * Validates the form entries. The form is considered valid if the user has entered
     * at least a non-empty dictionary form and a definition for the word.
     * @return true if the entry is valid, otherwise false
     */
    public boolean isEntryValid()
    {
        return (!this.view.getDictionaryFormField().getText().trim().equals("")
                && !this.view.getDefinitionField().getText().trim().equals(""));
    }
    
    /**
     * Pass-through method to retrieve the saved word entry in WordlistFrameController.
     * @return the current WordEntry
     */
    protected WordEntry getCurrentWordEntry()
    {
        return this.model.getCurrentWord();
    }
    
    
    /**
     * Retrieves the user-entered values from the view, and sets the saved word
     * in the model to a word entry with the corresponding fields. Text entry
     * values will be trimmed.
     */
    public void saveWordEntry()
    {
        model.setCurrentWord(new WordEntry(
                model.getCurrentSelectedWordClass(), 
                view.getDictionaryFormField().getText().trim(),
                view.getDefinitionField().getText().trim(),
                this.getOptionalFieldTexts(),
                view.getUserNotesTextArea().getText().trim()
        ));
    }
    
    /**
     * Retrieves an array of <code>String</code> consisting of  all of the 
     * values from user-entered text fields in the optional forms panel
     * of the view. 
     * The values are trimmed, and a field left blank in the form will correspond 
     * to an empty string in the array.
     * @return an array of <code>String</code>s with all the user-entered optional forms
     */
    private String[] getOptionalFieldTexts()
    {
        JTextField[] fields = view.getOptionalFormTextFields();
        String[] result = new String[fields.length];
        for (int i = 0; i < fields.length; i++)
        {
            result[i] = fields[i].getText().trim();
        }
        return result;
    }
    
    /**
     * When the OKButtonAction is invoked, validate the form. If it is valid,
     * set the <code>wordEntryWasSaved</code> to true and close the form. 
     * If it is not valid, we display an error message to the user and do nothing else.
     */
    private class OkButtonAction extends AbstractAction
    {

        public OkButtonAction()
        {
            super("OK", LibHelpers.getIconFromFileName("ok.png"));
            putValue(SHORT_DESCRIPTION, "Closes this dialog and saves the word into the list.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            if (isEntryValid())
            {
                saveWordEntry();
                wordEntryWasSaved = true;
                closeForm();
            } else
            {
                SwingUtilities.invokeLater(() ->
                {
                    JOptionPane.showMessageDialog(view.getFrame(),
                            "The Swedish Dictionary and Definition text fields can't be empty.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }
    
    }
    
    /**
     * When the CancelButtonAction is performed, we close the form without saving.
     */
    private class CancelButtonAction extends AbstractAction
    {
        
        public CancelButtonAction()
        {
            super("Cancel", LibHelpers.getIconFromFileName("delete.png"));
            putValue(SHORT_DESCRIPTION, "Reverts all changes and closes this dialog.");
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            closeForm();
        }
        
    }

}
