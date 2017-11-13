package sthlm.sweden.christofferwiregren.banino;

import java.util.Random;

/**
 * Created by christofferwiregren on 2017-10-22.
 */

public class RandomCards {

    public int randomCard(){

        Random random = new Random();

        int rand = random.nextInt(8)+0;


        return rand;
    }
}
