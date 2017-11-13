package sthlm.sweden.christofferwiregren.banino;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by christofferwiregren on 2017-09-25.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder>{

    private final LayoutInflater inflater;
    private List<RequestInfo> requestInfoList;
    private Context context;
    private boolean zlatan;


    public class MyViewHolder extends RecyclerView.ViewHolder  {

        public TextView nickname, infotext;
        public ImageView imageView;
        public Button btnAccept, btnDecline;



        public MyViewHolder(View view)  {
            super(view);

            nickname = (TextView) view.findViewById(R.id.txtInvitorReady);
            imageView = (ImageView) view.findViewById(R.id.imageSender);
            btnAccept = (Button)view.findViewById(R.id.btnaccept);
            btnDecline = (Button)view.findViewById(R.id.btnDecline);
            infotext = (TextView)view.findViewById(R.id.txtinvitation);


        }
    }

    @Override
    public RequestAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invitation_card, parent, false);

        return new RequestAdapter.MyViewHolder(itemView);

    }


    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(final RequestAdapter.MyViewHolder holder, final int position) {
        final RequestInfo requestInfo = requestInfoList.get(position);


        if(requestInfo.getAnswear().equals("yes") && requestInfo.getSenderName().equals("DU")){
            holder.btnDecline.setVisibility(View.INVISIBLE);
            holder.btnAccept.setText(R.string.ok);
            holder.infotext.setText("Kom tillbaka till ditt eget spel");
            holder.nickname.setText("Du");
            holder.imageView.setImageResource(R.drawable.girl);


        }


        else if(requestInfo.getAnswear().equals("yes")){
            holder.btnDecline.setVisibility(View.INVISIBLE);
            holder.btnAccept.setText(R.string.ok);
            holder.infotext.setText(R.string.missU);
            holder.nickname.setText(requestInfo.getSenderName());
            holder.imageView.setImageResource(R.drawable.girl);

        }
        

        else {


            holder.nickname.setText(requestInfo.getSenderName());
            holder.imageView.setImageResource(R.drawable.girl);

        }
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!Common.isNetworkConnected(view.getContext())){

                    Toast.makeText(view.getContext().getApplicationContext(), R.string.network, Toast.LENGTH_SHORT).show();
                    return;

                }

                yesGame();

                WaitingRoomFragment waitingRoomFragment = new WaitingRoomFragment();
                GameBoardFragment gameBoardFragment = new GameBoardFragment();

                Bundle bundle = new Bundle();
                bundle.putString("GAMEID", requestInfo.getGameID());
                waitingRoomFragment.setArguments(bundle);


                ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.rootMain,waitingRoomFragment).addToBackStack(null).commit();

            }


            private void yesGame() {


                String gameID = requestInfo.getGameID();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference currentdatabase;
                    currentdatabase = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("requests").child(gameID);

                final DatabaseReference data = FirebaseDatabase.getInstance().getReference()
                        .child("Games").child(requestInfo.getGameID()).child("members").child(uid);


                Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                hopperUpdates.put("answear", "yes");

                data.updateChildren(hopperUpdates);


                currentdatabase.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){



                        }

                    }
                });

                }



        });



        holder.btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(R.string.deleteGameRequest)
                        .setTitle(R.string.meddelande).setIcon(R.drawable.trash)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                noGame();
                                removeFromFirebase();


                            }

                            private void noGame() {


                                String gameID = requestInfo.getGameID();
                                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                DatabaseReference currentdatabase;
                                currentdatabase = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("requests").child(gameID);

                                final DatabaseReference xx = FirebaseDatabase.getInstance().getReference()
                                        .child("Games").child(requestInfo.getGameID()).child("members").child(uid);


                                Map<String, Object> hopperUpdates = new HashMap<String, Object>();
                                hopperUpdates.put("answear", "no");

                                xx.updateChildren(hopperUpdates);


                                currentdatabase.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){



                                        }

                                    }
                                });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }

            private void removeFromFirebase() {

                String gameID = requestInfo.getGameID();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("requests").child(gameID);
              //  DatabaseReference mMessageReferenceGame = FirebaseDatabase.getInstance().getReference().child("Games").child(gameID).child("members").child(uid);
              //  mMessageReferenceGame.removeValue();
                mMessageReference.removeValue();

            }
        });
    }

    private void checkTest(RequestInfo requestInfo) {



        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(requestInfo.getGameID()).child("size");


        databaseReference.orderByChild("nickname").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                String answear = (String)dataSnapshot.child("answear").getValue();

                try{

                    if(answear.equals("yes")) {



                    }
                }catch (Exception e){
                    Log.e(null, e.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    public RequestAdapter(List<RequestInfo> userList, Context context ) {
        this.requestInfoList = userList;
        this.context = context;
        inflater=LayoutInflater.from(context);



    }

    @Override
    public int getItemCount() {
        return requestInfoList.size();
    }




}




