package glostrainer.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * A concrete implementation of the <code>WordlistInterface</code>. In addition
 * to the CRUD-like interface, the class also defines methods for loading the
 * list from and saving the list to files.
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class WordlistModel implements WordlistInterface, Serializable
{

    private static final long serialVersionUID = 43L;

    private List<WordEntry> wordlist;

    /**
     * Crreates a new WordlistModel.
     */
    public WordlistModel()
    {
        wordlist = new ArrayList<>();
    }

    /**
     * Appends the specified word entry to the end of this list..
     *
     * @param word the word entry to add
     */
    @Override
    public void addWordEntry(WordEntry word)
    {
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Adding word to list: {0}", word);
        wordlist.add(word);
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Word list now has {0} entries", getEntryCount());
    }

    /**
     * Returns the number of word entries in this list.
     *
     * @return the word entry count
     */
    public int getEntryCount()
    {
        return wordlist.size();
    }

    /**
     * Returns the word entry at the specified position in this list.
     *
     * @param index - index of the element to return
     * @throws IndexOutOfBoundsException if the index is out of range
     * <code>(index &lt; 0 || index &gt;= getEntryCount())</code>
     * @return the wird entry at the specified position in this list
     */
    @Override
    public WordEntry getWordEntryAtIndex(int index)
    {
        WordEntry word = wordlist.get(index);
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Getting word from index {0}: {1}", new Object[]
        {
            index, word
        });
        return word;
    }

    /**
     * Returns a sequential <code>Stream</code> with this word list as its
     * source.
     *
     * @return
     */
    public Stream<WordEntry> getAllWordsAsStream()
    {
        return wordlist.stream();
    }

    /**
     * Replaces the word entry at the specified position in this list with the
     * given word entry..
     *
     * @param index index of the element to replace
     * @param word word entry to be stored at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     * <code>(index &lt; 0 || index &gt;= getEntryCount())</code>
     */
    @Override
    public void replaceWordEntryAtIndex(int index, WordEntry word)
    {
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Replacing word at index {0}", index);
        wordlist.set(index, word);
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Replacing word at index {0} is {1}: ", new Object[]
        {
            index, word
        });
    }

    /**
     * Removes the word entry at the given position in this list. Shifts any
     * subsequent elements to the left (subtracts one from their indices).
     *
     * @param index the index of the element to be removed
     * @throws IndexOutOfBoundsException if the index is out of range
     * <code>(index &lt; 0 || index &gt;= getEntryCount())</code>
     */
    @Override
    public void removeWordEntryAtIndex(int index)
    {
        WordEntry wordToRemove = wordlist.remove(index);
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Deleting word at index {0}, which is {1}: ", new Object[]
        {
            index, wordToRemove
        });
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Word list now has {0} entries left ", this.getEntryCount());
    }

    /**
     * Removes all of the word entries from this list. The list will be empty
     * after this call returns.
     */
    public void clear()
    {
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Clearing word list ");
        wordlist.clear();
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Word list now has {0} entries", this.getEntryCount());
    }

    /**
     * Loads a word entry list from the given file, and returns a
     * <code>WordlistModel</code> whose list is created from the found words in
     * the file.
     *
     * @param file the file to load from
     * @return a new WordlistModel with a created word list
     * @throws IOException if the file was not found, could not be read etc.
     * @throws ClassNotFoundException if creation of the list failed
     */
    public static WordlistModel loadFromFile(File file) throws IOException, ClassNotFoundException
    {
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Loading file {0} ", file.getCanonicalPath());

        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        List wordList = (ArrayList) in.readObject();
        WordlistModel m = new WordlistModel();
        m.wordlist = wordList;
        return m;
    }

    /**
     * Saves the word list to the given file.
     *
     * @param file the file to save to.
     * @throws IOException if the file could not be saved
     */
    public void saveToFile(File file) throws IOException
    {
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Attempting to save file to {0} ", file.getCanonicalPath());

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file)))
        {
            out.writeObject(wordlist);
        }
    }

}
