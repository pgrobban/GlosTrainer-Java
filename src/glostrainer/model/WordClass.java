package glostrainer.model;

/**
 *
 * @author Robert Sebescen (pgrobban at gmail dot com)
 */
public enum WordClass
{

    NOUN("Noun", new String[]
    {
        "Singular definite form",
        "Plural indefinite form",
        "Plural definite form",
        "Singular indefinite genitive case",
        "Singular definite genitive case",
        "Plural indefinite genitive case",
        "Plural definite genitive case"
    }, "The dictionary form of a noun is the <u>singular indefinite form</u>, e.g. <i>en bil</i>, <i>ett träd</i>."),
    VERB("Verb", new String[]
    {
        "Present tense",
        "Past tense (preteritum)",
        "Perfect tense (supinum)",
        "Imperative mood",
        "Presens particip",
        "Passive infinitive",
        "Passive present tense",
        "Passive past tense (preteritum)",
        "Passive perfect tense (supinum)"
    }, "The dictionary form of a verb is the <u>infinitive of the active form</u>, e.g. <i>att springa</i>."),
    ADJECTIVE("Adjective", new String[]
    {
        "Positive common gender (en)",
        "Positive neuter gender (ett)",
        "Positive plural/definitive form",
        "Positive definitive masculine",
        "Comparative",
        "Superlative (<i>är ~</i>)",
        "Superlative <i>den/det/de ~ + noun</i>",
        "Superlative <i>den ~ + masculine noun</i>"
    }, "The dictionary form of an adjective is the <u>positive common gender (<i>en-word</i>) form</u>, e.g. <i>hög</i>."),
    ADVERB("Adverb"),
    PREPOSITION("Preposition"),
    CONJUNCTION("Conjunction"),
    INTERJECTION("Interjection"),
    PERSONAL_PRONOUN("Definite pronoun", new String[]
    {
        "Possessive common gender (en)",
        "Possessive neuter gender (ett)",
        "Reflexive",
    }, "The dictionary form of a personal pronoun is the subject form. "),
    OTHER_PRONOUN("Other pronoun"),
    NUMERAL("Numeral", new String[]
    {
        "Ordinal",
        "Ordinal genitive",
        "Ordinal masculine",
        "Ordinal masc. genitive"
    }, "The dictionary form of the numeral is the cardinal number."),
    PHRASE("Expression/phrase", new String[]
    {
        "Explanation"
    }, ""),
    OTHER("Other");

    private final String[] wordForms;
    private final String dictionaryFormTip;
    private final String stringRepresentation;

    private WordClass(String stringRepresentation)
    {
        this(stringRepresentation, new String[0], "There are no known optional forms for this word class in general.<br/>"
                + "If you want to add other forms, you can do so in the <strong>My Notes</strong> text field below.");
    }

    private WordClass(String stringRepresentaiton, String[] wordForms, String dictionaryFormTip)
    {
        this.stringRepresentation = stringRepresentaiton;
        this.wordForms = wordForms;
        this.dictionaryFormTip = dictionaryFormTip;
    }

    public String getDictionaryFormTip()
    {
        return this.dictionaryFormTip;
    }

    public String[] getWordForms()
    {
        return this.wordForms;
    }

    @Override
    public String toString()
    {
        return this.stringRepresentation;
    }

}
