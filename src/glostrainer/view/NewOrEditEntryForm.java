package glostrainer.view;

import glostrainer.model.WordClass;
import java.awt.Dimension;
import java.util.LinkedHashMap;
import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

/**
 * A form for adding a new word or editing an existing word in the
 * WordlistFrame. The class does provide a method <code>getFrame()</code> to
 * retrieve the underlying dialog; however, this is intended to be used for the
 * controller to open, close and change the mode (new/edit) of the form, so we
 * do not perform any window manipulations here.
 * The class exposes methods for getting the JTextFields and JComboBox for 
 * retreiving the user's entered values.
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class NewOrEditEntryForm
{

    private final JDialog dialog;

    /**
     * Creates a NewOrEditEntryFrame which is overlaid and acts as a modal
     * dialog to the given JFrame (presumably from the NewOrEditEntryFrame
     * class).
     *
     * @param ownerFrame
     */
    public NewOrEditEntryForm(JFrame ownerFrame)
    {
        dialog = new JDialog(ownerFrame, true);
        SwingUtilities.invokeLater(() ->
        {
            dialog.setLocationRelativeTo(ownerFrame);
        });

        initComponents();
        initLayout();

        dialog.pack();
    }

    /**
     * A reference to the
     *
     * @return
     */
    public JDialog getFrame()
    {
        return dialog;
    }

    private void initComponents()
    {
        dialog.setMinimumSize(new Dimension(400, 300));

        mandatoryFieldsPanel = new JPanel();
        optionalFieldsPanel = new JPanel();
        userNotesPanel = new JPanel();
        okCancelButtonPanel = new JPanel();

        dictionaryFormLabel = new JLabel("Dictionary form:");
        dictionaryFormField = new JTextField();
        dictionaryFormHelperLabel = new JLabel();

        definitionLabel = new JLabel("Definition:");
        definitionField = new JTextField(20);

        wordClassLabel = new JLabel("Word class:");
        wordClassComboBox = new JComboBox(new DefaultComboBoxModel(WordClass.values()));
        wordClassComboBox.setMaximumSize(new Dimension(100, 20));

        userNotesLabel = new JLabel("My notes:");
        userNotesTextArea = new JTextArea(4, 30);
        pronunciationLabel = new JLabel("Pronunciation recording: ");
        pronunciationLabel2 = new JLabel("<html><small>Coming in a future version... maybe ;) </small></html>");

        okButton = new JButton();
        cancelButton = new JButton();
    }

    private void initLayout()
    {
        initMainLayout();

        initMandatoryFieldsLayout();
        setWordClassSpecificOptionalFields(WordClass.NOUN);

        initUserNotesLayout();
        initOKCancelButtonsLayout();
    }

    private void initMainLayout()
    {
        GroupLayout mainLayout = new GroupLayout(dialog.getContentPane());

        optionalFieldsScrollPane = new JScrollPane(optionalFieldsPanel);
        optionalFieldsScrollPane.setBorder(new CustomTitledBorder("Optional fields"));
        optionalFieldsScrollPane.setPreferredSize(new Dimension(400, 200));

        dialog.getContentPane().setLayout(mainLayout);
        mainLayout.setHorizontalGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(mandatoryFieldsPanel)
                .addComponent(optionalFieldsScrollPane)
                .addComponent(userNotesPanel)
                .addComponent(okCancelButtonPanel)
        );
        mainLayout.setVerticalGroup(mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(mainLayout.createSequentialGroup()
                        .addComponent(mandatoryFieldsPanel)
                        .addComponent(optionalFieldsScrollPane)
                        .addComponent(userNotesPanel)
                        .addComponent(okCancelButtonPanel)
                )
        );
    }

    private void initOKCancelButtonsLayout()
    {
        GroupLayout okCancelButtonLayout = new GroupLayout(okCancelButtonPanel);
        okCancelButtonPanel.setLayout(okCancelButtonLayout);
        okCancelButtonLayout.setAutoCreateGaps(true);
        okCancelButtonLayout.setAutoCreateContainerGaps(true);
        okCancelButtonLayout.setHorizontalGroup(okCancelButtonLayout.createSequentialGroup()
                .addComponent(okButton)
                .addComponent(cancelButton)
        );
        okCancelButtonLayout.setVerticalGroup(okCancelButtonLayout.createSequentialGroup()
                .addGroup(okCancelButtonLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(okButton)
                        .addComponent(cancelButton)
                )
        );
    }

    private void initUserNotesLayout()
    {
        userNotesPanel.setBorder(new CustomTitledBorder("My notes"));

        GroupLayout userNotesLayout = new GroupLayout(userNotesPanel);
        userNotesPanel.setLayout(userNotesLayout);
        userNotesLayout.setAutoCreateContainerGaps(true);
        userNotesLayout.setAutoCreateGaps(true);
        userNotesLayout.setHorizontalGroup(userNotesLayout.createSequentialGroup()
                .addGroup(userNotesLayout.createParallelGroup()
                        .addComponent(pronunciationLabel)
                        .addComponent(userNotesLabel)
                )
                .addGroup(userNotesLayout.createParallelGroup()
                        .addComponent(pronunciationLabel2)
                        .addComponent(userNotesTextArea)
                )
        );
        userNotesLayout.setVerticalGroup(userNotesLayout.createSequentialGroup()
                .addGroup(userNotesLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(pronunciationLabel)
                        .addComponent(pronunciationLabel2)
                )
                .addGroup(userNotesLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(userNotesLabel)
                        .addComponent(userNotesTextArea)
                )
        );
    }

    private void initMandatoryFieldsLayout()
    {
        mandatoryFieldsPanel.setBorder(new CustomTitledBorder("Mandatory fields"));
        GroupLayout mandatoryFieldsLayout = new GroupLayout(mandatoryFieldsPanel);
        mandatoryFieldsPanel.setLayout(mandatoryFieldsLayout);
        mandatoryFieldsLayout.setAutoCreateGaps(true);
        mandatoryFieldsLayout.setAutoCreateContainerGaps(true);
        mandatoryFieldsLayout.setHorizontalGroup(mandatoryFieldsLayout.createSequentialGroup()
                .addGroup(mandatoryFieldsLayout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(dictionaryFormHelperLabel)
                        .addGroup(mandatoryFieldsLayout.createSequentialGroup()
                                .addGroup(mandatoryFieldsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(dictionaryFormLabel)
                                        .addComponent(definitionLabel)
                                        .addComponent(wordClassLabel))
                                .addGroup(
                                        mandatoryFieldsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(dictionaryFormField)
                                        .addComponent(definitionField)
                                        .addComponent(wordClassComboBox)))));
        mandatoryFieldsLayout.setVerticalGroup(mandatoryFieldsLayout.createSequentialGroup()
                .addGroup(
                        mandatoryFieldsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(mandatoryFieldsLayout.createSequentialGroup()
                                .addGroup(
                                        mandatoryFieldsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(wordClassLabel)
                                        .addComponent(wordClassComboBox))
                                .addGroup(
                                        mandatoryFieldsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(dictionaryFormLabel)
                                        .addComponent(dictionaryFormField))
                                .addGroup(
                                        mandatoryFieldsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(dictionaryFormHelperLabel))
                                .addGroup(
                                        mandatoryFieldsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(definitionLabel)
                                        .addComponent(definitionField))
                        )
                )
        );
    }

    /**
     * Clears <code>optionalFieldsPanel</code> and dynamically generates JLabels 
     * and JTextFields to add in the panel that correspond to the optional forms
     * from the given word class.
     * @param currentWordClass
     */
    public void setWordClassSpecificOptionalFields(WordClass currentWordClass)
    {
        dictionaryFormHelperLabel.setText("<html>" + currentWordClass.getDictionaryFormTip() + "</html>");
        dialog.pack();

        optionalFieldsPanel.removeAll();
        GroupLayout optionalFieldsLayout = new GroupLayout(optionalFieldsPanel);
        optionalFieldsPanel.setLayout(optionalFieldsLayout);
        optionalFieldsLayout.setAutoCreateGaps(true);
        optionalFieldsLayout.setAutoCreateContainerGaps(true);

        String[] optionalWordForms = currentWordClass.getOptionalForms();
        optionalWordFormsLabels = new LinkedHashMap<>();
        optionalWordFormsTextFields = new LinkedHashMap<>();

        for (String key : optionalWordForms)
        {
            optionalWordFormsLabels.put(key, new JLabel("<html>" + key + ":</html>"));
            optionalWordFormsTextFields.put(key, new JTextField());
            optionalWordFormsTextFields.get(key).setPreferredSize(new Dimension(dialog.getWidth() / 2, 12));
        }

        ParallelGroup labelsGroup = optionalFieldsLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        optionalWordFormsLabels.values().stream().forEach((l) ->
        {
            labelsGroup.addComponent(l);
        });
        ParallelGroup fieldsGroup = optionalFieldsLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        optionalWordFormsTextFields.values().stream().forEach((f) ->
        {
            fieldsGroup.addComponent(f);
        });

        optionalFieldsLayout.setHorizontalGroup(optionalFieldsLayout.createSequentialGroup()
                .addGroup(labelsGroup)
                .addGroup(fieldsGroup)
        );

        SequentialGroup topToBottom = optionalFieldsLayout.createSequentialGroup();
        for (String key : optionalWordForms)
        {
            topToBottom.addGroup(optionalFieldsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(optionalWordFormsLabels.get(key))
                    .addComponent(optionalWordFormsTextFields.get(key)));
        }
        optionalFieldsLayout.setVerticalGroup(topToBottom);
    }
    
    /**
     * Sets the values of the optional form text fields from the given
     * LinkedHashMap. If the corresponding JTextfield does not exist, i.e. if
     * the optional form has changed from an earlier version of this application, 
     * show an alert to the user to update the entry.
     * @param input 
     */
    public void setOptionalFormTextFieldValues(LinkedHashMap<String, String> input)
    {
        //System.out.println("input " + input);
        for (String key : input.keySet())
        {
            JTextField correspondingField = this.optionalWordFormsTextFields.get(key);
            if (correspondingField != null)
            {
                correspondingField.setText(input.get(key));
            } else
            {
                JOptionPane.showMessageDialog(this.dialog, "<html>It looks like you are trying to a edit word with an optional form, "
                        + "<strong>" + key + "</strong>, that existed<br/>"
                        + " in an earlier version of GlosTrainer. The form has been renamed or removed in this version of GlosTrainer,<br/>"
                        + "which means that I have lost the value that you have entered for that form. I apologize for the inconvenience  :(<br/>"
                        + "Please check the Optional Forms panel to see if there are any empty fields that need to be filled in again.<br/><br/>"
                        + "For more details on which fields have been renamed, added or removed, refer to the release notes of GlosTrainer.</html>",
                        "Notice",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Retrieves a <code>LinkedHashMap</code> mapping of
     * <code>String -&gt; String</code> where the keys are the optional forms of
     * the word and the values are the entered values from the user in the text
     * fields in the form. <code>LinkedHashMap</code> preserves insertion order,
     * so we keep the same order for when opening the Edit Entry form. The
     * values are trimmed, and a field left blank in the form will correspond to
     * an empty string in the array.
     *
     * @return a <code>LinkedHashMap</code>s with key-value pairs of the
     * optional forms that the user has entered
     */
    public LinkedHashMap<String, String> getOptionalFormTextFieldsValues()
    {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();
        optionalWordFormsTextFields.keySet().stream().forEach((key) ->
        {
            String value = optionalWordFormsTextFields.get(key).getText().trim();
            if (!value.equals(""))
            {
                result.put(key, value);
            }
        });
        return result;
    }

    /**
     * @return the okButton
     */
    public JButton getOkButton()
    {
        return okButton;
    }

    /**
     * @return the cancelButton
     */
    public JButton getCancelButton()
    {
        return cancelButton;
    }

    /**
     * @return the definitionField
     */
    public JTextField getDefinitionField()
    {
        return definitionField;
    }

    /**
     * @return the dictionaryFormField
     */
    public JTextField getDictionaryFormField()
    {
        return dictionaryFormField;
    }

    /**
     * @return the wordClassComboBox
     */
    public JComboBox getWordClassComboBox()
    {
        return wordClassComboBox;
    }

    public JTextArea getUserNotesTextArea()
    {
        return userNotesTextArea;
    }

    private JPanel mandatoryFieldsPanel;
    private JPanel optionalFieldsPanel;
    private JScrollPane optionalFieldsScrollPane;
    private JPanel okCancelButtonPanel;

    private JTextField definitionField;
    private JLabel definitionLabel;
    private JLabel dictionaryFormHelperLabel;
    private JTextField dictionaryFormField;
    private JLabel dictionaryFormLabel;
    private JComboBox wordClassComboBox;
    private JLabel wordClassLabel;

    private JPanel userNotesPanel;
    private JLabel userNotesLabel;
    private JTextArea userNotesTextArea;

    private JLabel pronunciationLabel;
    private JLabel pronunciationLabel2;

    private JButton okButton;
    private JButton cancelButton;

    private LinkedHashMap<String, JLabel> optionalWordFormsLabels;
    private LinkedHashMap<String, JTextField> optionalWordFormsTextFields;

}
