package glostrainer.view;

import glostrainer.model.WordClass;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;

/**
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class NewOrEditEntryForm
{

    private final JDialog dialog;
    
    /**
     * Creates a NewOrEditEntryFrame which is overlaid and acts as a modal
     * dialog to the given JFrame (presumably from the NewOrEditEntryFrame class).
     * @param ownerFrame 
     */
    public NewOrEditEntryForm(JFrame ownerFrame)
    {
        dialog = new JDialog(ownerFrame, true);
        dialog.setAlwaysOnTop(true);

        initComponents();
        initLayout();
        
        dialog.pack();
    }

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
        userNotesTextArea = new JTextArea(3, 30);
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
        // mandatory fields panel mainLayout
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
     *
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
        JLabel[] optionalWordFormsLabels = new JLabel[optionalWordForms.length];
        JTextField[] optionalWordFormsTextFields = new JTextField[optionalWordForms.length];
        for (int i = 0; i < optionalWordForms.length; i++)
        {
            optionalWordFormsLabels[i] = new JLabel("<html>" + optionalWordForms[i] + ":</html>");
            optionalWordFormsTextFields[i] = new JTextField();
            optionalWordFormsTextFields[i].setPreferredSize(new Dimension(dialog.getWidth()/2, 12));
        }

        ParallelGroup labelsGroup = optionalFieldsLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (JLabel l : optionalWordFormsLabels)
        {
            labelsGroup.addComponent(l);
        }
        ParallelGroup fieldsGroup = optionalFieldsLayout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (JTextField f : optionalWordFormsTextFields)
        {
            fieldsGroup.addComponent(f);
        }

        optionalFieldsLayout.setHorizontalGroup(optionalFieldsLayout.createSequentialGroup()
                .addGroup(labelsGroup)
                .addGroup(fieldsGroup)
        );

        SequentialGroup topToBottom = optionalFieldsLayout.createSequentialGroup();
        for (int i = 0; i < optionalWordForms.length; i++)
        {
            topToBottom.addGroup(optionalFieldsLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(optionalWordFormsLabels[i])
                    .addComponent(optionalWordFormsTextFields[i]));
        }
        optionalFieldsLayout.setVerticalGroup(topToBottom);
    }

    public JTextField[] getOptionalFormTextFields()
    {
        List<JTextField> optionalTextFields = new ArrayList<>();
        for (Component c : optionalFieldsPanel.getComponents())
        {
            if (c instanceof JTextField)
            {
                optionalTextFields.add((JTextField) c);
            }
        }
        return optionalTextFields.toArray(new JTextField[optionalTextFields.size()]);
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

}
