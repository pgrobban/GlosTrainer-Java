package glostrainer.model;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Represents a word entry from a user's perspective. A <code>WordEntry</code> object
 * has a word class, a Swedish Dictionary Form, a Definition (which could be entered
 * in any language), a <code>LinkedHashMap</code> that maps optional form name &gt;
 * user's entered value) and user notes.
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class WordEntry implements Serializable
{
    /**
     * 
     */
    private WordClass wordClass;
    /**
     * 
     */
    private String swedishDictionaryForm;
    /**
     * 
     */
    private String definition;
    
    /**
     * Use a <code>Map</code> to store the optional forms as a key-value pair where the
     * key is the form name and the value is the value of that form for this word.
     * We also need to preserve insertion order to make sure the mapping in the text
     * fields are correct, so a <code>LinkedHashMap</code> should be good for this purpose.
     */
    private LinkedHashMap<String, String>optionalForms;
    /**
     * Additional details that the user wants to store about the word.
     */
    private String userNotes;
    
    private static final long serialVersionUID = 43L;

    /**
     * Creates a word with the word class set to NOUN and all other fields
     * set to empty Strings.
     */
    public WordEntry()
    {
        this(WordClass.NOUN, "", "", new LinkedHashMap<>(), "");
    }
    
    /**
     * Copy constructor.
     * @param copy the word to copy
     */
    public WordEntry(WordEntry copy)
    {
        this(copy.wordClass, copy.swedishDictionaryForm, copy.definition, copy.optionalForms, copy.userNotes);
    }
    
    /**
     * Creates a new word with the given information.
     * @param wordClass
     * @param swedishDictionaryForm
     * @param definition
     * @param optionalForms
     * @param userNotes 
     */
    public WordEntry(WordClass wordClass, String swedishDictionaryForm, 
            String definition, LinkedHashMap<String, String> optionalForms, String userNotes)
    {
        this.wordClass = wordClass;
        this.swedishDictionaryForm = swedishDictionaryForm;
        this.definition = definition;
        this.optionalForms = optionalForms;
        this.userNotes = userNotes;
    }
    
    /**
     * Returns a human-readable <code>String</code> with all information entered
     * about the word. The string contains the optional forms LinkedHashMap as
     * seen by the software, as well as a concise version with the keys removed,
     * suitable for displaying to the user.
     * @return 
     */
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
     * Retrieves the word class of the word entry.
     * @return the wordClass
     */
    public WordClass getWordClass()
    {
        return wordClass;
    }

    /**
     * Sets the word class of this word entry.
     * @param wordClass the wordClass to set
     */
    public void setWordClass(WordClass wordClass)
    {
        this.wordClass = wordClass;
    }

    /**
     * Retrieves the Swedish dictionary Form of this word entry.
     * @return the Swedish dictionary form
     */
    public String getSwedishDictionaryForm()
    {
        return swedishDictionaryForm;
    }

    /**
     * Sets the Swedish dictionary form of this word entry
     * @param swedishDictionaryForm the Swedish dictionary form to set
     */
    public void setSwedishDictionaryForm(String swedishDictionaryForm)
    {
        this.swedishDictionaryForm = swedishDictionaryForm;
    }

    /**
     * Gets the definition of this word entry.
     * @return the definition
     */
    public String getDefinition()
    {
        return definition;
    }

    /**
     * Sets the definition of this word entry.
     * @param definition the definition to set
     */
    public void setDefinition(String definition)
    {
        this.definition = definition;
    }

    /**
     * Returns a <code>LinkedHashMap</code> of the word's optional forms.
     * The keys should be the optional forms belonging to the word's word class,
     * and its values are the user-entered values for each optional form.
     * @return the optionalForms
     */
    public LinkedHashMap<String, String> getOptionalForms()
    {
        return optionalForms;
    }
    
    /**
     * Returns a "prettified" version of the optional forms of this word as a String,
     * suitable for displaying to the user in a table.
     * In this form, all of the optionalForm Maps' keys are removed, so we will
     * return a comma-separated list of the values. null or empty keys are removed
     * as well, so if there are no optional forms of this word class available, or
     * the user has not entered any values for optional forms, then this method
     * will return an empty String.
     * @return the optional forms of this word as a String
     */
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
     * Sets the optional forms of this word entry.
     * @param optionalForms the optionalForms to set
     */
    public void setOptionalForms(LinkedHashMap<String, String> optionalForms)
    {
        this.optionalForms = optionalForms;
    }
    
    /**
     * Retrieves any user notes stored for this word entry.
     * @return the user notes for this word entry
     */
    public String getUserNotes()
    {
        return userNotes;
    }

    /**
     * Sets the user notes for this word entry.
     * @param userNotes the user notes to set
     */
    public void setUserNotes(String userNotes)
    {
        this.userNotes = userNotes;
    }
    
}
