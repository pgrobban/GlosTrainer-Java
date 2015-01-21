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
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class WordlistModel implements WordlistInterface
{

    private static final long serialVersionUID = 42L;
    
    private List<WordEntry> wordlist;

    /**
     *
     */
    public WordlistModel()
    {
        wordlist = new ArrayList<>();
    }

    /**
     *
     * @param word
     */
    @Override
    public void addWordEntry(WordEntry word)
    {
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Adding word to list: {0}", word);
        wordlist.add(word);
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Word list now has {0} entries remaining", getCount());

    }
    
    public int getCount()
    {
        return wordlist.size();
    }

    /**
     *
     * @param index
     * @return
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

    public Stream<WordEntry> getAllWordsAsStream()
    {
        return wordlist.stream();
    }

    /**
     *
     * @param index
     * @param word
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
     *
     * @param index
     */
    @Override
    public void removeWordEntryAtIndex(int index)
    {
        WordEntry wordToRemove = wordlist.remove(index);
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Deleting word at index {0}, which is {1}: ", new Object[]
        {
            index, wordToRemove
        });
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Word list now has {0} entries left ", this.getCount());
    }

    public void removeAllWords()
    {
        wordlist = new ArrayList<>();
    }

    public static WordlistModel loadFromFile(File file) throws IOException, ClassNotFoundException
    {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        List wordList = (ArrayList) in.readObject();
        WordlistModel m = new WordlistModel();
        m.wordlist = wordList;
        return m;
    }

    public void saveToFile(File file) throws IOException
    {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(wordlist);
        out.close();
    }

}
