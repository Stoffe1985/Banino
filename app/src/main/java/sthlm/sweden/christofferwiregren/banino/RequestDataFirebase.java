package sthlm.sweden.christofferwiregren.banino;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by christofferwiregren on 2017-10-27.
 */

public class RequestDataFirebase {
    private String gameId;
    private RequestInfo requestInfo;
    HashMap<String, String> datamap = new HashMap<String, String>();


    public RequestDataFirebase(String gameId) {
        this.gameId = gameId;
    }

    public void removeFromFirebase() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("requests").child(gameId);
        //  DatabaseReference mMessageReferenceGame = FirebaseDatabase.getInstance().getReference().child("Games").child(gameID).child("members").child(uid);
        //  mMessageReferenceGame.removeValue();
        mMessageReference.removeValue();


    }

    public void sendrequest(RequestInfo requestInfo) {

        this.requestInfo = requestInfo;


        DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference().child("User").child(requestInfo.getSenderId().toString()).child("requests").child(gameId);


        mMessageReference.setValue(requestInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


            }

        });

    }
}
