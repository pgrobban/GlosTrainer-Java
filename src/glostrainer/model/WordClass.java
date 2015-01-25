package glostrainer.model;

/**
 * Represents a class of word in the Swedish language. The definition of a word
 * calss is a bit vague and can be left for the user to interpret since we allow
 * things like expressions and other words not belonging to one class. The
 * <code>WordClass</code> class is implemented as an enum with private
 * properties, making them act like static objects (yay, Java!).
 *
 * A word class in the program has the following fields:
 * <ul>
 * <li>A string representation form, used by the toString() method to help
 * writing things like spaces and other special characters, </li>
 * <li>an array of optional forms that the user can enter, and</li>
 * <li>a tip text (HTML string), used to display a tooltip in the frame to help
 * the user understand what to write for the dictionary form.</li>
 * </ul>
 *
 * Pronouns are split into definitive (personal) pronouns that allow for more
 * optional forms, and others that usually only come in one form (and to ease
 * the burden off the user of remembering which class they belong to).
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
        "Reflexive",
        "Possessive common gender (en)",
        "Possessive neuter gender (ett)",
        "Possessive plural",
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

    private final String[] optionalForms;
    private final String dictionaryFormTip;
    private final String stringRepresentation;

    /**
     * A tooltip text to explain to the user that there are no optional forms
     * for this word class.
     */
    private static final String NO_OPTIONAL_FORMS_TIP_TEXT = "There are no known optional forms for this word class in general.<br/>"
            + "If you want to add other forms, you can do so in the <strong>My Notes</strong> text field below.";

    /**
     * Creates a word class with only a string representation. The optional
     * forms will be set to an empty array, and the tdictionary form helper text
     * will be set to the value of NO_OPIIONA_FORMS_TIP_TEXT.
     *
     * @param stringRepresentation
     */
    private WordClass(String stringRepresentation)
    {
        this(stringRepresentation, new String[0], WordClass.NO_OPTIONAL_FORMS_TIP_TEXT);
    }

    /**
     * Creates a WordClass with the given string representation, an array of
     * otpional word forms, and a string representing a helping tip for the user
     * what to write for the dictionary form of the word.
     *
     * @param stringRepresentaiton
     * @param optionalForms
     * @param dictionaryFormTip
     */
    private WordClass(String stringRepresentaiton, String[] optionalForms, String dictionaryFormTip)
    {
        this.stringRepresentation = stringRepresentaiton;
        this.optionalForms = optionalForms;
        this.dictionaryFormTip = dictionaryFormTip;
    }

    /**
     * Retrieves a string representing a helping tip for the user what to write
     * for the dictionary form of the word.
     *
     * @return the dictionary form helper tip
     */
    public String getDictionaryFormTip()
    {
        return this.dictionaryFormTip;
    }

    /**
     * Retrieves a String array of the optional forms of this WordClass. If
     * there are no optional forms defined for this word class, an empty array
     * will be returned.
     *
     * @return
     */
    public String[] getOptionalForms()
    {
        return this.optionalForms;
    }

    /**
     * Retrieves the name of this <code>WordClass</code> in a human-readable
     * format.
     *
     * @return a <code>String</code> representation of the
     * <code>WordClass</code>
     */
    @Override
    public String toString()
    {
        return this.stringRepresentation;
    }

}
