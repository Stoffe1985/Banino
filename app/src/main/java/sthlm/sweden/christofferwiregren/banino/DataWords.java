package sthlm.sweden.christofferwiregren.banino;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by christofferwiregren on 2017-10-15.
 */

public class DataWords {



    public Map<String, Word> card(){

        Word[]randCards = new Word[5];


        Map<String, Word> word = new HashMap<String, Word>();

        word.put("1", new Word("STATUSMEDVETEN", "Jag värderar attribut som visar mitt och andras sociala anseende"));
        word.put("2", new Word("ANALYTISK", "Jag förstår sammanhang och kan påvisa orsak och verkan"));
        word.put("3", new Word("LATMASK", "Jag avviker när arbetet blir obekvämt eller jobbig"));
        word.put("4", new Word("SKEPTISK", "Jag intar en avvaktande hållning i många frågor"));
        word.put("5", new Word("SKRYTMÅNS", "Jag talar gärna om min förträfflighet, mina framgångar och fina ägodelar"));
        word.put("6", new Word("SÄLLSKAPSMÄNNISKA", "Jag tycker om att umgås"));
        word.put("7", new Word("MEDKÄNNANDE", "Jag har förmåga till inlevelse i andras känslor"));
        word.put("8", new Word("BARNKÄR", "Jag blir förtjust i även andras barn"));
        return word;



    }




}
