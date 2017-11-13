package sthlm.sweden.christofferwiregren.banino;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowAnswerListFragment extends android.support.v4.app.Fragment {

    private String gameId = "";
    private String adminPlayer = "";
    private String adminPlayerID = "";
    private List<GameAnswer> answearList = new ArrayList<GameAnswer>();
    private GameAnswer[] gameAnswerArray;
    private RecyclerView recyclerView;
    private GameAnswer gameAnswer, admin;
    private ShowAnswerAdapter mAdapter;
    private TextView dinRubrik, dinText;
    private ImageView imageView;
    private Button btnNext;
    private int adminPoint = 0, userPoint = 0, mypoints = 0;
    private UserPoint userPoints, adminPoints;
    private String uid = "";
    private int turn = 0;
    private int round = 0;
    private int size = 0;
    private int total = 0;
    private boolean gate;
    private boolean gate1;
    private TextView ordRubrik;


    public ShowAnswerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_answer_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());

        admin = new GameAnswer(0, "", "");
        mAdapter = new ShowAnswerAdapter(answearList);
        recyclerView = (RecyclerView) view.findViewById(R.id.answearRecler);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        dinRubrik = (TextView) view.findViewById(R.id.dittSvar);
        dinText = (TextView) view.findViewById(R.id.dittsvartext);
        imageView = (ImageView) view.findViewById(R.id.dinbildsvar);
        btnNext = (Button) view.findViewById(R.id.btnNextRound);
        userPoints = new UserPoint();
        adminPoints = new UserPoint();
        ordRubrik = (TextView) view.findViewById(R.id.ordvalet);
        gate = true;
        gate1 = true;
        btnNext.setEnabled(false);

        getGameID();
        getPoints();
        getStatus();
        getanswer();


        mAdapter.notifyDataSetChanged();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setTurn();
                setCardnbr();
                setStatus();
                removeAnswer();

            }
        });

    }


    private void removeAnswer() {

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("CurrentGame").child(gameId);

        databaseReference.removeValue();


    }

    public void getGameID() {


        try {

            uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

            Bundle bundle = getArguments();

            String currentGameID = bundle.getString("startshow");
            String currentAdmin = bundle.getString("highligthplayer");
            String currentAdminID = bundle.getString("highligthplayerID");
            String currentTurn = bundle.getString("turn");
            String currentSize = bundle.getString("size");
            String currentRound = bundle.getString("round");
            String rubrik = bundle.getString("rubrik");
            ordRubrik.setText(rubrik.toString());
            this.gameId = currentGameID.toString();
            this.adminPlayer = currentAdmin.toString();
            this.adminPlayerID = currentAdminID.toString();
            this.turn = Integer.parseInt(currentTurn.toString());
            this.round = Integer.parseInt(currentRound.toString());
            this.size = Integer.parseInt(currentSize.toString());


            if (uid.toString().equals(currentAdminID.toString())) {

                btnNext.setEnabled(true);

            }


        } catch (Exception e) {


        }


    }


    private void setTurn() {


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GameRound").child(gameId);

        Map<String, Object> hopperUpdates = new HashMap<String, Object>();

        if (turn == (size - 1) && round <= 11) {

            hopperUpdates.put("turn", "" + (0));
            hopperUpdates.put("round", "" + (round + 1));


        } else {

            hopperUpdates.put("turn", "" + (turn + 1));


        }

        databaseReference.updateChildren(hopperUpdates);


        databaseReference.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                }

            }
        });


    }


    public void checkScore() {


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();


        for (GameAnswer gameAnswer : answearList) {


            if (uid.toString().equals(gameAnswer.getUser().getId())) {


                if (gameAnswer.getChoice() == admin.getChoice()) {

                    userPoint += 1;
                    userPoints.setScore(userPoint);


                }

            }

        }

        total = (userPoint + mypoints);


    }

    public void adminScore() {


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();


        for (GameAnswer gameAnswer : answearList) {

            if (!(admin.getUser().getNickname().equals(gameAnswer.getUser().getNickname()))) {

                if (admin.getChoice() == gameAnswer.getChoice()) {

                    adminPoint += 1;
                    adminPoints.setScore(adminPoint);


                }


            }

        }

        total = (adminPoint + mypoints);


    }

    private void getStatus() {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GameStatus").child(gameId);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {


                String answear = (String) ds.child("status").getValue();

                if (answear.equals("off")) {
                    sendScoreFirebase(total);


                    startGame();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void sendScoreFirebase(int currentPoint) {


        if (gate1 == true) {

            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("GamePoint").child(gameId).child(uid);


            Map<String, Object> hopperUpdates = new HashMap<String, Object>();
            hopperUpdates.put("point", currentPoint);


            databaseReference.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {


                    }

                }
            });


        }

        gate1 = false;

    }

    public void getanswer() {

        DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference().child("CurrentGame").child(gameId);

        mMessageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                gameAnswer = new GameAnswer(0, "", "");
                answearList.clear();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String answser = (String) ds.child("val").getValue().toString();
                    int a = Integer.parseInt(answser);
                    String username = (String) ds.child("username").getValue().toString();
                    String userid = (String) ds.child("userId").getValue().toString();


                    if (!username.equals(adminPlayer.toString())) {
                        gameAnswer = new GameAnswer(a, userid, username);

                        answearList.add(gameAnswer);
                        mAdapter.notifyDataSetChanged();
                    }

                    if (username.equals(adminPlayer.toString())) {
                        gameAnswer = new GameAnswer(a, userid, username);

                        admin = gameAnswer;


                        dinRubrik.setText(gameAnswer.getUser().getNickname() + " svar:");
                        setAnswer(gameAnswer.getChoice());


                    }

                    mAdapter.notifyDataSetChanged();
                    gameAnswerArray = answearList.toArray(new GameAnswer[answearList.size()]);
                }

                if (uid.equals(admin.getUser().getId())) {


                    adminScore();


                } else {
                    checkScore();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void setAnswer(int i) {

        switch (i) {

            case 1: {

                imageView.setImageResource(R.drawable.onegreen);

                dinText.setText(R.string.val1);

                break;
            }

            case 2: {

                imageView.setImageResource(R.drawable.twogreen);
                dinText.setText(R.string.val2);


                break;
            }

            case 3: {

                imageView.setImageResource(R.drawable.threegreen);

                dinText.setText(R.string.val3);

                break;
            }

            case 4: {

                imageView.setImageResource(R.drawable.fourgreen);
                dinText.setText(R.string.val4);

                break;

            }

            default: {

                imageView.setImageResource(R.drawable.chevron);


            }
        }


    }


    private void getPoints() {

        if (gate == true) {

            uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("GamePoint").child(gameId).child(uid);


            databaseReference.
                    addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(DataSnapshot ds) {


                            mypoints = Integer.parseInt(ds.child("point").getValue().toString());
                            gate = false;

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void setCardnbr() {

        RandomCards randomCards = new RandomCards();

        int randnbr = randomCards.randomCard();


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference currentdatabase;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Cardnumber").child(gameId);


        Map<String, Object> hopperUpdates = new HashMap<String, Object>();
        hopperUpdates.put("cardTurn", randnbr);

        databaseReference.updateChildren(hopperUpdates);


        databaseReference.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                }

            }
        });


    }

    private void setStatus() {

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference currentdatabase;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GameStatus").child(gameId);


        Map<String, Object> hopperUpdates = new HashMap<String, Object>();
        hopperUpdates.put("status", "" + "off");


        databaseReference.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                }

            }
        });


    }

    private void startGame() {

        try {
            GameBoardFragment gameBoardFragment = new GameBoardFragment();
            Bundle bundle = new Bundle();
            bundle.putString("GI", gameId.toString());

            gameBoardFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.rootMain, gameBoardFragment).commit();
        } catch (Exception e) {

        }


    }

}


