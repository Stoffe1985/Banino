package sthlm.sweden.christofferwiregren.banino;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaitingRoomFragment extends android.support.v4.app.Fragment {
    private Button btnStartGame;
    private String gameId = "";
    private String adminName = "";
    private List<User> userList = new ArrayList<User>();
    private RecyclerView recyclerView;
    private ReadToPlayAdapter mAdapter;
    private ConstraintLayout constraintLayout;
    private int totalmembers;
    private int notAnswear;
    private int answearNo;
    private ImageView imageViewUfo, cOne, cTwo, cThree, starFall, sun;
    private TextView rubrik;
    private String uid;


    public WaitingRoomFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_waiting_room, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cOne = (ImageView) view.findViewById(R.id.cloudone);
        cTwo = (ImageView) view.findViewById(R.id.cloudtwo);
        cThree = (ImageView) view.findViewById(R.id.cloudtree);
        sun = (ImageView) view.findViewById(R.id.thesun);

        starFall = (ImageView) view.findViewById(R.id.starfall);


        imageViewUfo = (ImageView) view.findViewById(R.id.imageUfo);
        btnStartGame = (Button) view.findViewById(R.id.btnStartGame);
        recyclerView = (RecyclerView) view.findViewById(R.id.readtoplaylist);
        mAdapter = new ReadToPlayAdapter(userList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        rubrik = (TextView) view.findViewById(R.id.rubrik);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        btnStartGame.setEnabled(false);
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.xxxx);
        constraintLayout.setVisibility(View.INVISIBLE);


        startInfo();
        checkGameStatus();


        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Common.isNetworkConnected(getContext())) {

                    Toast.makeText(getContext().getApplicationContext(), R.string.network, Toast.LENGTH_SHORT).show();
                    return;

                }

                starGame();


            }
        });


    }

    private void checkGameStatus() {

        if (!Common.isNetworkConnected(getContext())) {

            Toast.makeText(getContext().getApplicationContext(), R.string.network, Toast.LENGTH_SHORT).show();
            return;

        }

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(gameId);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {


                    String answear = (String) dataSnapshot.child("gamestatus").getValue();


                    if (answear.equals("ready")) {


                        setTimer();


                    }
                } catch (Exception e) {
                    Log.e(null, e.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void startInfo() {

        try {
            Bundle bundle = getArguments();

            String currentGameID = bundle.getString("GAMEID");

            adminName = bundle.getString("USERNAME");

            this.gameId = currentGameID;


            if (!(adminName.isEmpty())) {
                checkAdmin();
                gameSize();
                checkPlayerStatus();
                putCardsToFirebade();

            }
        } catch (Exception e) {

            checkPlayerStatus();

        }


    }

    private void checkPlayerStatus() {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(gameId).child("members");


        databaseReference.orderByChild("nickname").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userList.clear();
                notAnswear = 0;
                answearNo = 0;

                RequestInfo requestInfo = new RequestInfo();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String answear = (String) ds.child("answear").getValue();

                    if (answear.equals("yes")) {

                        String name = (String) ds.child("nickname").getValue();
                        String id = (String) ds.child("id").getValue();
                        User user = new User(id, name);
                        userList.add(user);

                    }

                    if (answear.equals("no")) {

                        answearNo -= 1;

                    }

                    if (answear.equals("")) {

                        notAnswear += 1;

                    }


                    mAdapter.notifyDataSetChanged();


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void gameSize() {

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Games").child(gameId);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                totalmembers = Integer.parseInt(dataSnapshot.child("size").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void checkAdmin() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(gameId);


        databaseReference.orderByChild("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    String namn = String.valueOf(ds.getValue());

                    if (namn.equals(adminName.toString())) {
                        btnStartGame.setEnabled(true);
                        btnStartGame.setVisibility(View.VISIBLE);

                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void starGame() {

        if ((totalmembers - answearNo) == userList.size()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.ready).setTitle(R.string.meddelande).setIcon(R.drawable.spaceship)
                    .setCancelable(false)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {


                            statusGame();
                            addAdminIntoGame();

                            mAdapter.notifyDataSetChanged();

                            dialog.cancel();

                        }
                    })
                    .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else {


            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


            builder.setMessage(R.string.meddelande_wait).setTitle(R.string.meddelande)
                    .setCancelable(false).
                    setIcon(R.drawable.notall)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            mAdapter.notifyDataSetChanged();
                            dialog.cancel();


                        }

                    });
            AlertDialog alert = builder.create();
            alert.show();

        }


    }

    private void statusGame() {


        DatabaseReference currentdatabase;
        currentdatabase = FirebaseDatabase.getInstance().getReference().child("Games").child(gameId);

        Map<String, Object> hopperUpdates = new HashMap<String, Object>();
        hopperUpdates.put("gamestatus", "ready");


        currentdatabase.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                }

            }
        });


    }

    public void setTimer() {

        imageViewUfo.setTranslationY(1f);
        imageViewUfo.animate().translationY(-2800f).setDuration(4000L);
        cOne.setTranslationX(1f);
        cOne.animate().translationX(1000f).setDuration(24000L);
        cTwo.setTranslationX(1f);
        cTwo.animate().translationX(1000f).setDuration(24000L);
        cThree.setTranslationX(1f);
        cThree.animate().translationX(1000f).setDuration(24000L);
        starFall.setTranslationX(1f);
        starFall.setTranslationY(1f);
        starFall.setAlpha(0.2f);
        starFall.animate().translationX(-1800f).translationY(1000).alpha(0.8f).setDuration(5000L);
        starFall.setAlpha(1f);

        btnStartGame.setEnabled(false);

        constraintLayout.setVisibility(View.VISIBLE);

        CountDownTimer timer = new CountDownTimer(4000, 1000) {


            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                try {
                    GameBoardFragment gameBoardFragment = new GameBoardFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("GI", gameId.toString());

                    gameBoardFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.rootMain, gameBoardFragment).commit();
                } catch (Exception e) {
                    Log.e("WaitingRoom", "" + e);
                }
            }


        }.start();
    }

    public void addAdminIntoGame() {


        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentFirebaseUser.getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Games").child(gameId).child("members");
        HashMap<String, String> datamap = new HashMap<String, String>();

        datamap.put("nickname", adminName.toString());
        datamap.put("id", userID.toString());
        datamap.put("answear", "yes");
        datamap.put("points", "0");


        database.child(userID).setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                } else {


                }
            }
        });

    }

    public void putCardsToFirebade() {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("Cards").child(gameId);

        DataWords dataWords = new DataWords();

        Map<String, Word> word = dataWords.card();
        database.setValue("CardTurn", "0");
        database.setValue(word);

    }


}
