package glostrainer.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class WordEntry implements Serializable
{
    private WordClass wordClass;
    
    private String swedishDictionaryForm;
    
    private String definition;
    
    private String[] optionalForms;
    
    private String userNotes;
    
    public WordEntry()
    {
        this(WordClass.NOUN, "", "", new String[0], "");
    }
    
    public WordEntry(WordClass wordClass, String swedishDictionaryForm, 
            String definition, String[] optionalForms, String userNotes)
    {
        this.wordClass = wordClass;
        this.swedishDictionaryForm = swedishDictionaryForm;
        this.definition = definition;
        this.optionalForms = optionalForms;
        this.userNotes = userNotes;
    }

    @Override
    public String toString()
    {
        return String.format("Word{Word class=%s, Swedish dictionary form=%s, Definition=%s, Optional forms=%s, Pretty optional forms=%s, userNotes=%s}", 
                this.wordClass, 
                this.swedishDictionaryForm, 
                this.definition,
                Arrays.toString(optionalForms),
                this.getOptionalFormsAsString(),
                this.userNotes);
    }

    /**
     * @return the wordClass
     */
    public WordClass getWordClass()
    {
        return wordClass;
    }

    /**
     * @param wordClass the wordClass to set
     */
    public void setWordClass(WordClass wordClass)
    {
        this.wordClass = wordClass;
    }

    /**
     * @return the swedishDictionaryForm
     */
    public String getSwedishDictionaryForm()
    {
        return swedishDictionaryForm;
    }

    /**
     * @param swedishDictionaryForm the swedishDictionaryForm to set
     */
    public void setSwedishDictionaryForm(String swedishDictionaryForm)
    {
        this.swedishDictionaryForm = swedishDictionaryForm;
    }

    /**
     * @return the definition
     */
    public String getDefinition()
    {
        return definition;
    }

    /**
     * @param definition the definition to set
     */
    public void setDefinition(String definition)
    {
        this.definition = definition;
    }

    /**
     * @return the optionalForms
     */
    public String[] getOptionalForms()
    {
        return optionalForms;
    }
    
    public String getOptionalFormsAsString()
    {
        StringBuilder output = new StringBuilder("");
        for (String s : this.optionalForms)
            if (s != null && s.length() > 0)
                output.append(s).append(", ");
        if (output.length() > 1)
            output.setLength(output.length()-2);
        return output.toString();
    }

    /**
     * @param optionalForms the optionalForms to set
     */
    public void setOptionalForms(String[] optionalForms)
    {
        this.optionalForms = optionalForms;
    }
    
    public String getUserNotes()
    {
        return userNotes;
    }

    /**
     * @param userNotes the userNotes to set
     */
    public void setUserNotes(String userNotes)
    {
        this.userNotes = userNotes;
    }
    
}
