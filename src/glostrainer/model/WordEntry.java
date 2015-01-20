package glostrainer.model;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class WordEntry implements Serializable
{
    private WordClass wordClass;
    
    private String swedishDictionaryForm;
    
    private String definition;
    
    /**
     * Use a <code>Map</code> to store the optional forms as a key-value pair where the
     * key is the form name and the value is the value of that form for this word.
     * We also need to preserve insertion order to make sure the mapping in the text
     * fields are correct, so a <code>LinkedHashMap</code> should be good for this purpose.
     */
    private LinkedHashMap<String, String>optionalForms;
    
    private String userNotes;
    
    private static final long serialVersionUID = 43L;

    
    public WordEntry()
    {
        this(WordClass.NOUN, "", "", new LinkedHashMap<>(), "");
    }
    
    // copy constructor
    public WordEntry(WordEntry copy)
    {
        this(copy.wordClass, copy.swedishDictionaryForm, copy.definition, copy.optionalForms, copy.userNotes);
    }
    
    public WordEntry(WordClass wordClass, String swedishDictionaryForm, 
            String definition, LinkedHashMap<String, String> optionalForms, String userNotes)
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
                this.optionalForms.toString(),
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
    public LinkedHashMap<String, String> getOptionalForms()
    {
        return optionalForms;
    }
    
    public String getOptionalFormsAsString()
    {
        StringBuilder output = new StringBuilder("");
        this.optionalForms.values().stream().filter((s) -> (s != null && s.length() > 0)).forEach((s) ->
        {
            output.append(s).append(", ");
        });
        // we want to remove the last comma and space, but we need to make sure there actually are
        if (output.length() > 1)
            output.setLength(output.length()-2);
        return output.toString();
    }

    /**
     * @param optionalForms the optionalForms to set
     */
    public void setOptionalForms(LinkedHashMap<String, String> optionalForms)
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
