package sthlm.sweden.christofferwiregren.banino;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by christofferwiregren on 2017-10-27.
 */

public class CheckGame {
    private String uid;
    private String gameId;


    public CheckGame(String uid, String gameId) {
        this.uid = uid;
        this.gameId = gameId;
    }

    public void switchConfirm(int a) {


        switch (a) {

            case 1: {

                dataconfirm("getRound");

                break;
            }
            case 2: {

                dataconfirm("getPoints");


                break;
            }
            case 3: {

                dataconfirm("getNumber");


                break;
            }

            case 4: {

                dataconfirm("getPlayers");


                break;
            }
            case 5: {

                dataconfirm("getCards");


                break;
            }

            case 6: {

                dataconfirm("getGameID");


                break;
            }

            default:

        }

    }

    public void dataconfirm(String parameter) {

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference currentdatabase;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GameBoardFragmentTOShow").child(gameId).child(uid);


        Map<String, Object> hopperUpdates = new HashMap<String, Object>();
        hopperUpdates.put(parameter, "Ready");

        databaseReference.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                }

            }
        });


    }

}
