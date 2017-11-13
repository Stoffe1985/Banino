package sthlm.sweden.christofferwiregren.banino;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends android.support.v4.app.Fragment {
    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();

        inputEmail = (EditText) view.findViewById(R.id.email);
        inputPassword = (EditText) view.findViewById(R.id.password);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btnSignup = (Button) view.findViewById(R.id.btn_signup);
        btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnReset = (Button) view.findViewById(R.id.btn_reset_password);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignUpFragment signUpFragment = new SignUpFragment();
                getFragmentManager().beginTransaction().replace(R.id.rootMain, signUpFragment).addToBackStack(null).commit();


            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
                getFragmentManager().beginTransaction().replace(R.id.rootMain, resetPasswordFragment).addToBackStack(null).commit();


            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Common.isNetworkConnected(getContext())) {

                    Toast.makeText(getContext().getApplicationContext(), R.string.network, Toast.LENGTH_SHORT).show();
                    return;

                }


                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getContext().getApplicationContext(), R.string.enter_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext().getApplicationContext(), R.string.enter_pass, Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error


                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    }
                                    if (!email.contains("@")) {
                                        inputEmail.setError(getString(R.string.minimun_email));

                                    } else {
                                        Toast.makeText(getContext().getApplicationContext(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {

                                    MainActivityFragment mainActivityFragment = new MainActivityFragment();
                                    getFragmentManager().beginTransaction().replace(R.id.rootMain, mainActivityFragment).commit();

                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

}


