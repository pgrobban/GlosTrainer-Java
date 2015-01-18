package glostrainer.model;

/**
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class NewOrEditEntryModel 
{
    private WordClass currentSelectedWordClass;
    private WordEntry currentWord;
    
    public NewOrEditEntryModel()
    {
        this.currentSelectedWordClass = WordClass.NOUN;
        this.currentWord = new WordEntry();
    }

    /**
     * @return the currentSelectedWordClass
     */
    public WordClass getCurrentSelectedWordClass()
    {
        return currentSelectedWordClass;
    }

    /**
     * @param currentSelectedWordClass the currentSelectedWordClass to set
     */
    public void setCurrentSelectedWordClass(WordClass currentSelectedWordClass)
    {
        this.currentSelectedWordClass = currentSelectedWordClass;
    }

    /**
     * @return the currentWord
     */
    public WordEntry getCurrentWord()
    {
        return currentWord;
    }

    /**
     * @param currentWord the currentWord to set
     */
    public void setCurrentWord(WordEntry currentWord)
    {
        this.currentWord = currentWord;
    }
}
