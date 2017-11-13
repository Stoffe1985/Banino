package sthlm.sweden.christofferwiregren.banino;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HostGameFragment extends android.support.v4.app.Fragment {

    private ImageButton btnAddPlayer;
    private Button btnCreateGame;
    private List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserAdapter mAdapter;
    private EditText editNickUser;
    private String nickname;
    private User adminUser;
    private String gameKey;
    private DatabaseReference database;
    private ProgressBar progressBar;
    private ConstraintLayout constraintLayout;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public HostGameFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host_game, container, false);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        String id = bundle.getString("ID");
        String us = bundle.getString("NAME");
        this.adminUser = new User(id,us);
        btnCreateGame = (Button)view.findViewById(R.id.btnCreatGame);
        recyclerView = (RecyclerView) view.findViewById(R.id.contactList);
        btnAddPlayer = (ImageButton)view.findViewById(R.id.btnAdda);
        editNickUser = (EditText)view.findViewById(R.id.editNickname);
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar5);
        constraintLayout = (ConstraintLayout)view.findViewById(R.id.constraintsload);
        btnCreateGame.setEnabled(false);
        userList.clear();

        constraintLayout.setVisibility(View.INVISIBLE);



        btnAddPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                    constraintLayout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);


                    btnAddPlayer.setBackgroundResource(R.drawable.addyellowone);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {


                    if (Common.isNetworkConnected(getActivity())) {
                        constraintLayout.setVisibility(View.VISIBLE);


                        boolean exist = checkifuseralreadyexist(editNickUser.getText().toString());


                        String ee = editNickUser.getText().toString().replaceAll("\\s+", "");


                        if (!adminUser.getNickname().equals(ee)) {


                            if (!exist == true) {

                                checkUsername();
                                editNickUser.setText("");


                            } else {
                                Toast.makeText(getActivity(), R.string.usernametwotimes, Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getActivity(), R.string.addme, Toast.LENGTH_SHORT).show();


                        }}else {
                        Toast.makeText(getActivity(), R.string.network, Toast.LENGTH_SHORT).show();

                    }

                    constraintLayout.setVisibility(View.INVISIBLE);

                    btnAddPlayer.setBackgroundResource(R.drawable.addblanka);

                }

                return true;

            }


        });

        mAdapter = new UserAdapter(userList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);



        btnCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createGame();
                addPlayerIntoGame();

            }
        });

    }

    private void checkUsername() {

        nickname = editNickUser.getText().toString();

        nickname = nickname.replaceAll("\\s+","");

        DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference().child("User");

        mMessageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (!checkIfUsernameExists(nickname, dataSnapshot)) {

                    Toast.makeText(getActivity(), R.string.usernamedontexist, Toast.LENGTH_SHORT).show();

                } else {

                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public boolean  checkIfUsernameExists(String username, DataSnapshot datasnapshot){

        User user1 = new User();

        for (DataSnapshot ds: datasnapshot.getChildren()){
            user1.setNickname(ds.getValue(User.class).getNickname());
            user1.setId(ds.getValue(User.class).getId());

            if(StringManipulation.expandUsername(user1.getNickname()).equals(username)){
                String currentID = user1.getId();
                userList.add(user1);
                btnCreateGame.setEnabled(true);
                mAdapter.notifyDataSetChanged();
                return true;
            }
            constraintLayout.setVisibility(View.INVISIBLE);
        }
        return false;
    }

    public boolean checkifuseralreadyexist(String name){

        name = name.replaceAll("\\s+","");











        List<User> currentUserList = new ArrayList<>(userList);


        Iterator itr = currentUserList.iterator();
        while(itr.hasNext()) {


            User currentUser = (User) itr.next();

            if(StringManipulation.expandUsername(currentUser.getNickname()).equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void createGame(){

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference().child("Games").push();
        gameKey = database.getKey();
        WaitingRoomFragment waitingRoomFragment = new WaitingRoomFragment();
        HashMap<String, String> datamap = new HashMap<String, String>();
        String userID = currentFirebaseUser.getUid();
        datamap.put("admin", adminUser.getNickname());
        datamap.put("gameID", gameKey);




        database.setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), R.string.SUCCESS, Toast.LENGTH_LONG).show();
                    statusOff();
                } else {

                    Toast.makeText(getActivity(), R.string.failedconnect, Toast.LENGTH_LONG).show();

                }
            }
        });


    }
    public void addPlayerIntoGame() {

        RequestDataFirebase requestDataFirebase = new RequestDataFirebase(gameKey);

        requestDataFirebase.sendrequest(new RequestInfo("DU",""+adminUser.getId().toString(),""+gameKey.toString(),"yes"));

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentFirebaseUser.getUid();
        database = FirebaseDatabase.getInstance().getReference().child("Games").child(gameKey).child("members");

        HashMap<String, String> datamap = new HashMap<String, String>();


        if(userList.isEmpty()){
            btnCreateGame.setEnabled(false);
        }
        Iterator itr = userList.iterator();
        while(itr.hasNext()){


            User currentUser = (User) itr.next();

            sendGameRequest(currentUser.getId().toString());
            datamap.put("nickname", currentUser.getNickname());
            datamap.put("id", currentUser.getId());
            datamap.put("answear", "");


            database.child(currentUser.getId()).setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {


                    } else {

                        Toast.makeText(getActivity(), "FAILED TO CONNECT", Toast.LENGTH_LONG).show();


                    }
                }
            });

        }

    }



    public void sendGameRequest(String id){

        DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference().child("User").child(id).child("requests").child(gameKey);

        RequestInfo requestInfo = new RequestInfo(adminUser.getNickname().toString(),adminUser.getId().toString(),gameKey,"no");



        mMessageReference.setValue(requestInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    playerSize();
                    sendTurn();
                    sendCardnumber();

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
                    WaitingRoomFragment waitingRoomFragment = new WaitingRoomFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("GAMEID",gameKey);
                    bundle.putString("USERNAME", adminUser.getNickname());
                    bundle.putString("USERID", uid);

                    waitingRoomFragment.setArguments(bundle);



                    ((FragmentActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.rootMain,waitingRoomFragment).commit();

                } else {

                }
            }
        });

    }

    public void statusOff(){



        DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference().child("GameStatus").child(gameKey);
        Map<String, Object> ans = new HashMap<String, Object>();


        ans.put("status", "off");

        mMessageReference.setValue(ans).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {



                } else {

                }
            }
        });





    }

    public void playerSize(){

        database = FirebaseDatabase.getInstance().getReference().child("Games").child(gameKey);

        Map<String, Object> datamap = new HashMap<String, Object>();
        datamap.put("size", String.valueOf(userList.size()));

        database.updateChildren(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {

                }
            }
        });

    }

    public void sendTurn() {


        DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference().child("GameRound").child(gameKey);
        Map<String, Object> ans = new HashMap<String, Object>();


        ans.put("round", "1");
        ans.put("turn", "0");

        mMessageReference.setValue(ans).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {



                } else {

                }
            }
        });


    }

    private void sendCardnumber(){

        RandomCards randomCards = new RandomCards();

        int randnbr = randomCards.randomCard();



        DatabaseReference currentdatabase;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Cardnumber").child(gameKey);


        Map<String, Object> hopperUpdates = new HashMap<String, Object>();
        hopperUpdates.put("cardTurn", randnbr);

        databaseReference.updateChildren(hopperUpdates);


        databaseReference.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    
                }

            }
        });








    }




}
