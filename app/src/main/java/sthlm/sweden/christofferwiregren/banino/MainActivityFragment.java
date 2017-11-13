package sthlm.sweden.christofferwiregren.banino;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainActivityFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private ImageButton signOut;
    private Button btnCreate,btnjoinafunnygame, btnRules;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private User user;
    private ImageView imageView;

    private ConstraintLayout constraintLayout;
    private ProgressBar progressBar;
    public MainActivityFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_activity, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = (ProgressBar)view.findViewById(R.id.progressBar2) ;
        constraintLayout = (ConstraintLayout)view.findViewById(R.id.constraintmain);
        initButtons(view);
        setonClic();

        auth = FirebaseAuth.getInstance();
        signOut = (ImageButton) view.findViewById(R.id.btn_signOuty);
        btnLock();



        auth = FirebaseAuth.getInstance();


        if (auth.getCurrentUser() == null) {
            LoginFragment loginFragment = new LoginFragment();
            getFragmentManager().beginTransaction().replace(R.id.rootMain,loginFragment).commit();

        }

try {
    database = FirebaseDatabase.getInstance().getReference().child("User").child(currentFirebaseUser.getUid());

}catch (Exception e){
            Log.v(null,""+ e.toString());
}





        signOut.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    signOut.setBackgroundResource(R.drawable.logoutorange);


                } else if (event.getAction()== MotionEvent.ACTION_UP){
                    signOut.setBackgroundResource(R.drawable.logaut);
                    signOut();


                }

                return true;
            }


        });


    }



 //sign out method
    public void signOut() {

        if(!Common.isNetworkConnected(getContext())){

            Toast.makeText(getContext().getApplicationContext(), "Kolla nätet", Toast.LENGTH_SHORT).show();
            return;

        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.logout)
                .setTitle(R.string.meddelande).setIcon(R.drawable.exit)
                .setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                auth.signOut();
                                LoginFragment loginFragment = new LoginFragment();
                                getFragmentManager().beginTransaction().replace(R.id.rootMain,loginFragment).commit();

                            }
                        })

                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();






    }

    public void initButtons(View view){

        btnCreate = (Button)view.findViewById(R.id.funnyGame);
        btnjoinafunnygame = (Button)view.findViewById(R.id.btnYoda);
        btnRules = (Button)view.findViewById(R.id.btnRules);
        imageView = (ImageView)view.findViewById(R.id.animationpeople);


    }

    public void setonClic(){
        btnCreate.setOnClickListener(this);
        btnjoinafunnygame.setOnClickListener(this);
        btnRules.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        switch(v.getId()) {
            case R.id.funnyGame: {

                try {

                    Bundle bundle = new Bundle();
                    bundle.putString("NAME", user.getNickname());
                    bundle.putString("ID", user.getId());
                    HostGameFragment hostGameFragment = new HostGameFragment();
                    hostGameFragment.setArguments(bundle);
                    getFragmentManager().beginTransaction().replace(R.id.rootMain,hostGameFragment).addToBackStack(null).commit();



                }catch (Exception e){

                }



                break;
            }

            case R.id.btnYoda: {
                JoinGameFragment joinGameFragment = new JoinGameFragment();
                getFragmentManager().beginTransaction().replace(R.id.rootMain,joinGameFragment).addToBackStack(null).commit();
                break;
            }
            case R.id.btnRules:{
                RulesFragment rulesFragment = new RulesFragment();
                getFragmentManager().beginTransaction().replace(R.id.rootMain,rulesFragment).addToBackStack(null).commit();
                break;

            }
            default: Log.v("", "");
                break;
        }

    }
    @Override
    public void onStart() {

        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

        super.onStart();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                user = new User();
                user = dataSnapshot.getValue(User.class);
                progressBar.setVisibility(View.INVISIBLE);
                constraintLayout.setVisibility(View.INVISIBLE);
                btnUnlock();

            }



            @Override
            public void onCancelled(DatabaseError databaseError) {



            }
        });



    }

    public void btnLock(){

        btnCreate.setEnabled(false);
        btnRules.setEnabled(false);
        btnjoinafunnygame.setEnabled(false);

    }

    public void btnUnlock(){
        btnCreate.setEnabled(true);
        btnRules.setEnabled(true);
        btnjoinafunnygame.setEnabled(true);
    }


}


