package glostrainer.model;

import java.util.List;

/**
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public abstract class AbstractWordlistModel 
{
    protected List<WordEntry> wordList;
    
    public abstract void addWord(WordEntry word);
    
    public abstract WordEntry getWordAtIndex(int index);
    
    public abstract void replaceWordAtIndex(int index, WordEntry word);
    
    public abstract void removeWordAtIndex(int index);
    
    public int getWordCount()
    {
        return wordList.size();
    }
    
    
}
