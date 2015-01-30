package glostrainer.model;

/**
 * This class represents the data currently stored in the <code>NewOrEditEntryFrame</code>
 * user controls. The class holds a reference to a temporary stored word entry,
 * <code>currentWord</code> whose data is filled in when the user is editing 
 * a word entry or clicked OK to saving a new word entry in the view.
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class NewOrEditEntryModel implements IModel
{
    /**
     * 
     */
    private WordClass currentSelectedWordClass;
    /**
     * A word that is populated with data from the view.
     */
    private WordEntry currentWord;
    
    /**
     * Creates a new <code>NewOrEditEntryModel</code>. The currently selected
     * word is empty in all fields, and has a word class set to NOUN.
     */
    public NewOrEditEntryModel()
    {
        this.currentSelectedWordClass = WordClass.NOUN;
        this.currentWord = new WordEntry();
    }

    /**
     * @return the current selected WordClass
     */
    public WordClass getCurrentSelectedWordClass()
    {
        return currentSelectedWordClass;
    }

    /**
     * @param currentSelectedWordClass the current selected word class to set
     */
    public void setCurrentSelectedWordClass(WordClass currentSelectedWordClass)
    {
        this.currentSelectedWordClass = currentSelectedWordClass;
    }

    /**
     * Returns the currently stored word. The word needs to have been set data
     * using the (e.g. via the <code>setCurrentWord()</code> method), otherwise this will return
     * a word entry whose fields are empty strings and a word class set to NOUN.
     * @return the current word entry
     */
    public WordEntry getCurrentWord()
    {
        return currentWord;
    }

    /**
     * Sets the currently stored word to a new data whose fields have values
     * @param currentWord the new word to set
     */
    public void setCurrentWord(WordEntry currentWord)
    {
        this.currentWord = currentWord;
    }
}
