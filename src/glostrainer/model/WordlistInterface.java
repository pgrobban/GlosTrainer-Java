package glostrainer.model;

/**
 * An abstract model for getting and setting word entries in a list-like manner.
 * The interface is supposed to resemble a CRUD (Create, Read, Update, Delete) one.
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public interface WordlistInterface extends IModel
{
  
    /**
     * Adds a word entry to this list.
     * @param wordEntry the word entry to add
     */
    public void addWordEntry(WordEntry wordEntry);
    
    /**
     * Retrieves the word at the given index. The concrete subclasses can
     * determine what Exception to throw for an index that is out of bounds.
     * @param index the index from which to retrieve from.
     * @return the word at the given index
     */
    public WordEntry getWordEntryAtIndex(int index);
    
    /**
     * Replaces a word at the given index with the given word. The concrete
     * subclasses can determine what Exception to throw for an index that is
     * out of bounds.
     * @param index the index to replace at
     * @param word the new word
     */
    public void replaceWordEntryAtIndex(int index, WordEntry word);
    
    /**
     * Removes a word entry from the list with the given index.The concrete
     * subclasses can determine what Exception to throw for an index that is
     * out of bounds.
     * @param index the index to remove.
     */
    public abstract void removeWordEntryAtIndex(int index);
    
    
}
