package sthlm.sweden.christofferwiregren.banino;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
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


public class GameBoardFragment extends android.support.v4.app.Fragment {
    private Button btnbad, btnalright, btnverygood, btnbest;
    private ImageView starImage, logoImage;
    private TextView txtRubrik, txtSubRubrik, countdown, nextPlayer, turnsGame, playerTurntextfield;
    private ConstraintLayout constraintLayout, gameConstraintlayout;
    private TextView points;
    private UserPoint userPoint;
    private List<User> userList = new ArrayList<>();
    private List<GameAnswer> answearList = new ArrayList<>();
    private List<Word> wordList = new ArrayList<>();
    private User[] gameTurn;
    private Word[] wordsarray;
    private String gameId = "";
    private int turn = 0;
    private GameAnswer gameAnswer;
    private GameAnswer[] gameAnswerArray;
    private int count = 0;
    private String uid = "";
    private User userMe;
    private String turnstart, round;
    private int mypoints;
    private ShowAnswerListFragment showAnswerListFragment;
    private int randomNumber = 0;
    private CheckGame checkGame;
    private ImageView clock;



    public GameBoardFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_board, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnbad = (Button) view.findViewById(R.id.btnBad);
        btnalright = (Button) view.findViewById(R.id.btnAlright);
        btnverygood = (Button) view.findViewById(R.id.btnVerygood);
        btnbest = (Button) view.findViewById(R.id.btnBest);
        starImage = (ImageView) view.findViewById(R.id.starsymbol);
        logoImage = (ImageView) view.findViewById(R.id.logoImage);
        txtRubrik = (TextView) view.findViewById(R.id.txtRubrik);
        txtSubRubrik = (TextView) view.findViewById(R.id.subrubrik);
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.playerTurnAlert);
        countdown = (TextView) view.findViewById(R.id.countdowngame);
        gameConstraintlayout = (ConstraintLayout) view.findViewById(R.id.gamelayout);
        constraintLayout.setVisibility(View.INVISIBLE);
        points = (TextView) view.findViewById(R.id.nbrPoints);
        nextPlayer = (TextView) view.findViewById(R.id.txtnextPlayer);
        turnsGame = (TextView) view.findViewById(R.id.turnsGame);
        userPoint = new UserPoint();
        showAnswerListFragment = new ShowAnswerListFragment();
        playerTurntextfield = (TextView)view.findViewById(R.id.nameTurn);
        clock = (ImageView)view.findViewById(R.id.clock);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        gameAnswer = new GameAnswer(0, "", "");


        btnSetonTuch();
        lockBtn();
        getGameID(); //
        getPoints(); // Check
        getNumber(); //Check
        getPlayers(); // Samlar alla Spelare som sagt ja
        getCards();
        getRound();
        getAllDataCheck();
        getAllDataForAccess();


    }

    private void getAllDataCheck() {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GameBoardFragment").child(gameId).child(uid);


        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                int answearr = (int) dataSnapshot.getChildrenCount();

                if (answearr == 6) {

                    setTimer();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getAllDataForAccess() {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GameBoardFragmentTOShow").child(gameId).child(uid);


        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                int answearr = (int) dataSnapshot.getChildrenCount();

                if (answearr == 6) {

                    getStatus();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void getStatus() {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GameStatus").child(gameId);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot ds) {


                String answear = (String) ds.child("status").getValue();

                if (answear.equals("on")) {


                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                            .child("GameBoardFragmentTOShow").child(gameId).child(uid);

                    databaseReference.removeValue();

                    startShow();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void btnSetonTuch() {

        btnbad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    gameAnswer.setChoice(1);
                    lockBtn();
                    btnbad.setAlpha(0f);
                    btnbad.setScaleY(0f);
                    btnbad.setScaleX(0f);
                    btnbad.animate().scaleX(1f).scaleX(1f).alpha(1f).setDuration(2000L);
                    btnbad.setAlpha(1f);
                    btnbad.setScaleY(1f);
                    btnbad.setScaleX(1f);
                    btnbad.setTextColor(Color.YELLOW);
                    waitingInfo();
                    sendanswer();

                }

                return false;
            }
        });

        btnalright.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    gameAnswer.setChoice(2);
                    lockBtn();
                    btnalright.setAlpha(0f);
                    btnalright.setScaleY(0f);
                    btnalright.setScaleX(0f);
                    btnalright.animate().scaleX(1f).scaleX(1f).alpha(1f).setDuration(2000L);
                    btnalright.setAlpha(1f);
                    btnalright.setScaleY(1f);
                    btnalright.setScaleX(1f);
                    btnalright.setTextColor(Color.YELLOW);
                    waitingInfo();
                    sendanswer();

                }

                return false;
            }
        });

        btnverygood.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    gameAnswer.setChoice(3);
                    lockBtn();
                    btnverygood.setAlpha(0f);
                    btnverygood.setScaleY(0f);
                    btnverygood.setScaleX(0f);
                    btnverygood.animate().scaleX(1f).scaleX(1f).alpha(1f).setDuration(2000L);
                    btnverygood.setAlpha(1f);
                    btnverygood.setScaleY(1f);
                    btnverygood.setScaleX(1f);
                    btnverygood.setTextColor(Color.YELLOW);
                    waitingInfo();
                    sendanswer();
                }


                return false;
            }
        });

        btnbest.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {


                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    gameAnswer.setChoice(4);
                    lockBtn();
                    btnbest.setAlpha(0f);
                    btnbest.setScaleY(0f);
                    btnbest.setScaleX(0f);
                    btnbest.animate().scaleX(1f).scaleX(1f).alpha(1f).setDuration(2000L);
                    btnbest.setAlpha(1f);
                    btnbest.setScaleY(1f);
                    btnbest.setScaleX(1f);
                    btnbest.setTextColor(Color.YELLOW);


                    waitingInfo();
                    sendanswer();

                }
                return false;
            }
        });

    }

    public void animatePoint() {

        points.setAlpha(0f);
        points.setScaleY(0f);
        points.setScaleX(0f);
        points.setTranslationY(-200f);
        points.setRotation(360f);

        points.animate().scaleX(1f).scaleX(1f).alpha(1f).translationY(0f).rotation(0f).setDuration(2000L).setInterpolator(new BounceInterpolator());
        points.setText(String.valueOf(userPoint.getScore()));

        points.setAlpha(1f);
        points.setScaleY(1f);
        points.setScaleX(1f);
        points.setTranslationY(1f);
        points.setRotation(1f);


    }

    public void setTimer() {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GameBoardFragment").child(gameId).child(uid);

        databaseReference.removeValue();

        animatePoint();
        nextPlayer();
        txtSubRubrik.setVisibility(View.INVISIBLE);
        txtRubrik.setVisibility(View.INVISIBLE);
        logoImage.setVisibility(View.INVISIBLE);
        constraintLayout.setVisibility(View.VISIBLE);

        CountDownTimer timer = new CountDownTimer(4000, 1000) {

            public void onTick(long millisUntilFinished) {

                countdown.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {

                try {

                    constraintLayout.setVisibility(View.INVISIBLE);
                    txtSubRubrik.setVisibility(View.VISIBLE);
                    txtRubrik.setVisibility(View.VISIBLE);
                    logoImage.setVisibility(View.VISIBLE);
                    txtRubrik.setText(wordsarray[randomNumber].getWord().toString());
                    txtSubRubrik.setText(wordsarray[randomNumber].getExplanation().toString());
                    playerTurntextfield.setText("- "+gameTurn[count].getNickname().toString());

                    unlockBtn();


                } catch (Exception e) {
                }
            }


        }.start();
    }

    public void lockBtn() {

        btnbad.setEnabled(false);
        btnalright.setEnabled(false);
        btnverygood.setEnabled(false);
        btnbest.setEnabled(false);

    }

    public void unlockBtn() {

        btnbad.setEnabled(true);
        btnalright.setEnabled(true);
        btnverygood.setEnabled(true);
        btnbest.setEnabled(true);

    }

    public void nextPlayer() {

        int a = Integer.parseInt(turnstart.toString());

        turnsGame.setText(round + "/10");
        nextPlayer.setText("" + gameTurn[count].getNickname().toString());

        if (userMe.getNickname().toString().equals(gameTurn[count].getNickname().toString())) {

            getAnswer();

        }


    }

    public void sendanswer() {


        DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference().child("CurrentGame").child(gameId).child(uid);
        Map<String, Object> ans = new HashMap<String, Object>();


        ans.put("val", gameAnswer.getChoice());
        ans.put("username", userMe.getNickname());
        ans.put("userId", userMe.getId());

        mMessageReference.setValue(ans).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                } else {

                }
            }
        });


    }

    public void getAnswer() {


        DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference().child("CurrentGame").child(gameId);

        mMessageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                answearList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String answser = (String) ds.child("val").getValue().toString();
                    int a = Integer.parseInt(answser);
                    String username = (String) ds.child("username").getValue().toString();
                    String userid = (String) ds.child("userId").getValue().toString();

                    try {

                        GameAnswer gameAnswer = new GameAnswer(a, userid, username);

                        answearList.add(gameAnswer);


                    } catch (Exception e) {

                    }

                }

                try {

                    gameAnswerArray = answearList.toArray(new GameAnswer[answearList.size()]);


                    if (gameAnswerArray.length == gameTurn.length) {

                        if (!turnstart.isEmpty() && userMe.getNickname().equals(gameTurn[count].getNickname())) {


                            setStatus();


                        }


                    }

                } catch (Exception e) {

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void setStatus() {

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference currentdatabase;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GameStatus").child(gameId);


        Map<String, Object> hopperUpdates = new HashMap<String, Object>();
        hopperUpdates.put("status", "" + "on");

        databaseReference.updateChildren(hopperUpdates);


        databaseReference.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                }

            }
        });


    }

    private void startShow() {

        try {

            Bundle bundle = new Bundle();
            bundle.putString("startshow", gameId.toString());
            bundle.putString("highligthplayer", gameTurn[count].getNickname().toString());
            bundle.putString("highligthplayerID", gameTurn[count].getId().toString());
            bundle.putString("turn", turnstart.toString());
            bundle.putString("round", round.toString());
            bundle.putString("size", "" + gameTurn.length);
            bundle.putString("rubrik", wordsarray[randomNumber].getWord().toString());


            showAnswerListFragment.setArguments(bundle);


            getFragmentManager().beginTransaction().replace(R.id.rootMain, showAnswerListFragment).commit();


        } catch (Exception e) {

        }


    }

    private void getPlayers() {


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Games").child(gameId).child("members");


        databaseReference.orderByChild("nickname").addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String answear = (String) ds.child("answear").getValue();

                    if (answear.equals("yes")) {

                        String name = (String) ds.child("nickname").getValue();
                        String id = (String) ds.child("id").getValue();
                        User user = new User(id, name);

                        userList.add(user);

                        if (uid.equals(id)) {

                            userMe = new User(id.toString(), name.toString());

                        }


                    }

                    gameTurn = userList.toArray(new User[userList.size()]);


                }

                switchConfirm(4);
                checkGame.switchConfirm(4);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getGameID() {


        try {

            Bundle bundle = getArguments();

            String currentGameID = bundle.getString("GI");

            this.gameId = currentGameID.toString();

            checkGame = new CheckGame(uid, gameId);

            switchConfirm(6);
            checkGame.switchConfirm(6);


        } catch (Exception e) {


        }


    }

    private void getCards() {

        wordList.clear();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Cards").child(gameId);


        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {


                    String text = (String) ds.child("explanation").getValue();
                    String word = (String) ds.child("word").getValue();


                    Word words = new Word(word, text);
                    wordList.add(words);

                }

                wordsarray = wordList.toArray(new Word[wordList.size()]);
                switchConfirm(5);
                checkGame.switchConfirm(5);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void waitingInfo() {

        txtRubrik.setText(R.string.waiting);
        txtSubRubrik.setText(R.string.waitingInfo);

        logoImage.setVisibility(View.INVISIBLE);
        playerTurntextfield.setVisibility(View.INVISIBLE);
        clock.setVisibility(View.VISIBLE);


    }

    private void getRound() {

        round = "";
        turnstart = "";

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GameRound").child(gameId);


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                round = (String) dataSnapshot.child("round").getValue();
                turnstart = (String) dataSnapshot.child("turn").getValue();


                try {

                    count = Integer.parseInt(turnstart.toString());

                    if (turnstart.equals("0") && round.equals("1")) {

                        sendScoreFirebase();

                    }
                    if (round.equals("10")) {

                        createFinalpointTable();

                        try {

                            RankingFragment rankingFragment = new RankingFragment();

                            Bundle bundle = new Bundle();
                            bundle.putString("GameID", gameId);
                            rankingFragment.setArguments(bundle);


                            getFragmentManager().beginTransaction().replace(R.id.rootMain, rankingFragment).commit();


                        } catch (Exception e) {

                        }

                    }

                } catch (Exception e) {
                    Log.e("TURN", e.toString());
                }

                switchConfirm(1);
                checkGame.switchConfirm(1);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void createFinalpointTable() {


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("FinalPoint").child(gameId).child(uid);


        Map<String, Object> hopperUpdates = new HashMap<String, Object>();


        hopperUpdates.put("score", mypoints);
        hopperUpdates.put("name", "" + userMe.getNickname().toString());


        databaseReference.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                }

            }
        });


    }

    private void getPoints() {


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GamePoint").child(gameId).child(uid);


        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot ds) {


                try {

                    mypoints = Integer.parseInt(ds.child("point").getValue().toString());
                    userPoint.setScore(mypoints);
                    Log.e("GamboardFragment", "" + mypoints);

                    switchConfirm(2);
                    checkGame.switchConfirm(2);

                } catch (Exception e) {

                }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void switchConfirm(int a) {


        switch (a) {

            case 1: {

                dataconfirm("getRound");

                Log.e("DATACONFIRN", "getround");

                break;
            }
            case 2: {

                dataconfirm("getPoints");

                Log.e("DATACONFIRN", "getPOINT");


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
                .child("GameBoardFragment").child(gameId).child(uid);


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

    public void sendScoreFirebase() {


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference currentdatabase;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("GamePoint").child(gameId).child(uid);


        Map<String, Object> hopperUpdates = new HashMap<String, Object>();
        hopperUpdates.put("point", 0);


        databaseReference.updateChildren(hopperUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {


                }

            }
        });


    }

    private void getNumber() {


        uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Cardnumber").child(gameId);


        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot ds) {


                try {

                    randomNumber = Integer.parseInt(ds.child("cardTurn").getValue().toString());

                    switchConfirm(3);
                    checkGame.switchConfirm(3);


                } catch (Exception e) {

                }

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }


    @Override
    public void onStop() {
        super.onStop();

        Log.e("ONSTOP", "ONSTOP");


    }
}


