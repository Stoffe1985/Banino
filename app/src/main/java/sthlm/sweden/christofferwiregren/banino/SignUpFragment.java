package sthlm.sweden.christofferwiregren.banino;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class SignUpFragment extends android.support.v4.app.Fragment {


    private EditText inputEmail, inputPassword, inputNickname;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private User user;
    private Boolean gangster;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        gangster = false;

        btnSignIn = (Button) view.findViewById(R.id.sign_in_button);
        btnSignUp = (Button) view.findViewById(R.id.sign_up_button);
        inputEmail = (EditText) view.findViewById(R.id.email);
        inputPassword = (EditText) view.findViewById(R.id.password);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btnResetPassword = (Button) view.findViewById(R.id.btn_reset_password);
        inputNickname = (EditText) view.findViewById(R.id.nickname);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
                getFragmentManager().beginTransaction().replace(R.id.rootMain, resetPasswordFragment).addToBackStack(null).commit();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.rootMain, loginFragment).addToBackStack(null).commit();
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isNetworkConnected(getActivity())) {

                    setFirebaseUser();

                } else {
                    Toast.makeText(getActivity(), R.string.network, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void setFirebaseUser() {

        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        final String nickname = inputNickname.getText().toString().trim();

        user = new User("", nickname);

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), R.string.enter_email, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), R.string.enter_pass, Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getActivity(), R.string.pass_short, Toast.LENGTH_SHORT).show();
            return;
        }

        if (nickname.length() < 6) {

            inputNickname.setError("" + R.string.username_short);
            Toast.makeText(getActivity(), R.string.enter_nic, Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference().child("User");


        mMessageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (!checkIfUsernameExists(nickname.toString(), dataSnapshot)) {
                    creatDatabaseUser();
                } else {
                    Toast.makeText(getActivity(), R.string.usernameTaken, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });


        progressBar.setVisibility(View.VISIBLE);

        progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

    }


    public void creatDatabaseUser() {


        auth.createUserWithEmailAndPassword(inputEmail.getText().toString(), inputPassword.getText().toString())
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            showUserId();
                            MainActivityFragment mainActivityFragment = new MainActivityFragment();
                            getFragmentManager().beginTransaction().replace(R.id.rootMain, mainActivityFragment).commit();
                        }
                    }
                });

    }

    public void showUserId() {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference().child("User");

        user.setId(currentFirebaseUser.getUid());

        HashMap<String, String> datamap = new HashMap<String, String>();
        String userID = currentFirebaseUser.getUid();

        datamap.put("nickname", user.getNickname());
        datamap.put("id", user.getId());


        database.child(userID).setValue(datamap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                } else {


                }
            }
        });

    }

    public boolean checkIfUsernameExists(String username, DataSnapshot datasnapshot) {

        User user = new User();

        for (DataSnapshot ds : datasnapshot.getChildren()) {

            user.setNickname(ds.getValue(User.class).getNickname());

            if (StringManipulation.expandUsername(user.getNickname()).equals(username)) {
                return true;
            }
        }
        return false;
    }


}
