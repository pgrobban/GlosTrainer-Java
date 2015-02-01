package glostrainer.model;

/**
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public class QuizWordlistModel implements IModel
{
    private WordlistModel wordlist;
    
    public QuizWordlistModel(WordlistModel wordlist)
    {
        this.wordlist = wordlist;
    }
    
    public WordlistModel getWordlist()
    {
        return this.wordlist;
    }
}
