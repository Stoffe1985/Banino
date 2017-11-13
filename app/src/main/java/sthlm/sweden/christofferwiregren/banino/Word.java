package sthlm.sweden.christofferwiregren.banino;

/**
 * Created by christofferwiregren on 2017-10-12.
 */

public class Word {
    private String word;
    private String explanation;


    public Word() {
        this("","");

    }

    public Word(String word, String explanation) {

        this.word = word;
        this.explanation = explanation;
    }

    public String getWord() {

        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
