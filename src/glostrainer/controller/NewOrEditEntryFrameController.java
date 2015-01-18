package glostrainer.controller;

import glostrainer.LibHelpers;
import glostrainer.model.NewOrEditEntryModel;
import glostrainer.model.WordEntry;
import glostrainer.model.WordClass;
import glostrainer.view.NewOrEditEntryFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import javax.swing.AbstractAction;
import static javax.swing.Action.SHORT_DESCRIPTION;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * This controller represents interactions for the user with the NewOrEditEntryFrame,
 * a dialog for entering a new word entry or editing an existing one.
 * The model here represents the entered data from the GUI's text boxes (and combo box
 * for the word class). 
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class NewOrEditEntryFrameController
{
    /**
     * 
     */
    private final NewOrEditEntryModel model;
    /**
     * 
     */
    private final NewOrEditEntryFrame view;
    /**
     * Sets a flag to determine if the word was saved, e.g. when the user has
     * clicked the OK button. 
     */
    protected boolean wordWasSaved;
    
    /**
     * Creates a new NewOrEditFrameController with the given model and view.
     * @param model
     * @param view 
     */
    public NewOrEditEntryFrameController(NewOrEditEntryModel model, NewOrEditEntryFrame view)
    {
        this.model = model;
        this.view = view;
        initComponents();
    }

    private void initComponents()
    {
        view.getOkButton().setAction(new OkButtonAction());
        view.getOkButton().requestFocus();
        view.getCancelButton().setAction(new CancelButtonAction());
        view.getWordClassComboBox().addItemListener((ItemEvent e) ->
        {
            WordClass newSelectedWordClass = (WordClass) view.getWordClassComboBox().getSelectedItem();
            this.getModel().setCurrentSelectedWordClass(newSelectedWordClass);
            this.view.setWordClassSpecificOptionalFields(newSelectedWordClass);
            this.view.getFrame().pack();
        });
    }

    public void openNewEntryFrame()
    {
        this.wordWasSaved = false;
        //SwingUtilities.invokeLater(() ->
        //{
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
        //});
        frame.setVisible(true);

    }

    public void closeFrame()
    {
        SwingUtilities.invokeLater(() ->
        {
            view.getFrame().setVisible(false);
        });
    }

    public void openEditEntryFrame(WordEntry wordToEdit)
    {
        System.out.println("Opening Edit Entry Frame with " + wordToEdit);
        this.wordWasSaved = false;

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
        frame.setVisible(true);
    }

    public boolean isEntryValid()
    {
        return (!this.view.getDictionaryFormField().getText().equals("")
                && !this.view.getDefinitionField().getText().equals(""));
    }

    /**
     * @return the model
     */
    public NewOrEditEntryModel getModel()
    {
        return model;
    }

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
                saveWord();
                wordWasSaved = true;
                closeFrame();
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

    public void saveWord()
    {
        WordEntry savedWord = model.getCurrentWord();
        
        savedWord.setWordClass(model.getCurrentSelectedWordClass());
        savedWord.setSwedishDictionaryForm(view.getDictionaryFormField().getText().trim());
        savedWord.setDefinition(view.getDefinitionField().getText().trim());
        savedWord.setOptionalForms(getOptionalFieldTexts());
        savedWord.setUserNotes(view.getUserNotesTextArea().getText());

        //System.out.println(model.getCurrentWord());
    }

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
            NewOrEditEntryFrameController.this.view.getFrame().setVisible(false);
        }
    }

}
