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

/**
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class WordlistModel extends AbstractWordlistModel implements Serializable
{

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
        System.out.println("Adding word to list " + word);
        wordList.add(word);
        printListInfo();
    }

    /**
     *
     * @param index
     * @return
     */
    @Override
    public WordEntry getWordAtIndex(int index)
    {
        WordEntry w = wordList.get(index);
        System.out.println("Getting word " + w);
        return w;
    }

    /**
     *
     * @param index
     * @param word
     */
    @Override
    public void replaceWordAtIndex(int index, WordEntry word)
    {
        System.out.println("Replacing word at index " + index);
        wordList.set(index, word);
        System.out.println("New word at index " + index + " is: " + wordList.get(index));
    }

    /**
     *
     * @param index
     */
    @Override
    public void removeWordAtIndex(int index)
    {
        System.out.println("Deleting word at index " + index + ", which is" + this.wordList.get(index));
        wordList.remove(index);
    }

    public void removeAllWords()
    {
        wordList = new ArrayList<>();
    }

    public static WordlistModel loadFromFile(File file) throws IOException, ClassNotFoundException
    {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        List wordList = (ArrayList)in.readObject();
        WordlistModel m = new WordlistModel();
        m.wordList = wordList;
        return m;
    }

    public void saveToFile(File file) throws IOException
    {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
        printListInfo();
        out.writeObject(wordList);
        out.close();
    }
    
    private void printListInfo()
    {
        System.out.println("Wordlist has " + getWordCount() + " entries");
        wordList.stream().forEach((w) ->
        {
            System.out.println(w);
        });
    }


}
