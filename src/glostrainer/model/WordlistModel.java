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
public class WordlistModel extends AbstractWordlistModel implements Serializable
{

    private static final long serialVersionUID = 42L;

    /**
     *
     */
    public WordlistModel()
    {
        wordList = new ArrayList<>();
    }

    /**
     *
     * @param word
     */
    @Override
    public void addWord(WordEntry word)
    {
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Adding word to list: {0}", word);
        wordList.add(word);
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Word list now has {0} entries remaining", getWordCount());

    }

    /**
     *
     * @param index
     * @return
     */
    @Override
    public WordEntry getWordAtIndex(int index)
    {
        WordEntry word = wordList.get(index);
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Getting word from index {0}: {1}", new Object[]
        {
            index, word
        });
        return word;
    }

    public Stream<WordEntry> getAllWordsAsStream()
    {
        return wordList.stream();
    }

    /**
     *
     * @param index
     * @param word
     */
    @Override
    public void replaceWordAtIndex(int index, WordEntry word)
    {
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Replacing word at index {0}", index);
        wordList.set(index, word);
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
    public void removeWordAtIndex(int index)
    {
        WordEntry wordToRemove = wordList.remove(index);
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Deleting word at index {0}, which is {1}: ", new Object[]
        {
            index, wordToRemove
        });
        Logger.getLogger(WordlistModel.class.getName()).log(Level.INFO, "Word list now has {0} entries left ", this.getWordCount());
    }

    public void removeAllWords()
    {
        wordList = new ArrayList<>();
    }

    public static WordlistModel loadFromFile(File file) throws IOException, ClassNotFoundException
    {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        List wordList = (ArrayList) in.readObject();
        WordlistModel m = new WordlistModel();
        m.wordList = wordList;
        return m;
    }

    public void saveToFile(File file) throws IOException
    {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        out.writeObject(wordList);
        out.close();
    }

}
