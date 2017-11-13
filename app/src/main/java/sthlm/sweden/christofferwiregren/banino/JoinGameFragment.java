package sthlm.sweden.christofferwiregren.banino;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class JoinGameFragment extends android.support.v4.app.Fragment {

    private List<RequestInfo> userList = new ArrayList<RequestInfo>();
    private RecyclerView recyclerView;
    private RequestAdapter mAdapter;
    private RequestInfo requestInfo;
    private ConstraintLayout constraintLayout;
    private ImageView imageView;
    private TextView textRub, textSub;
    private ProgressBar progressBar;


    public JoinGameFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_join_game, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        constraintLayout = (ConstraintLayout) view.findViewById(R.id.stoffishk);
        recyclerView = (RecyclerView) view.findViewById(R.id.joinGameList);
        mAdapter = new RequestAdapter(userList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        imageView = (ImageView) view.findViewById(R.id.crying);
        textRub = (TextView) view.findViewById(R.id.intebjuden);
        textSub = (TextView) view.findViewById(R.id.intebjudensubtext);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar3);

        hide();

        if (!Common.isNetworkConnected(getContext())) {

            Toast.makeText(getContext().getApplicationContext(), R.string.network, Toast.LENGTH_SHORT).show();

            progressBar.setVisibility(View.VISIBLE);
            progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
            textRub.setVisibility(View.VISIBLE);
            textRub.setText(R.string.network);

            return;

        }
        findGame();


    }


    private void findGame() {


        progressBar.setVisibility(View.VISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mMessageReference = FirebaseDatabase.getInstance().getReference().child("User").child(uid).child("requests");


        mMessageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                requestInfo = new RequestInfo();
                userList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    requestInfo = ds.getValue(RequestInfo.class);
                    userList.add(requestInfo);
                    mAdapter.notifyDataSetChanged();

                }
                progressBar.setVisibility(View.INVISIBLE);

                mAdapter.notifyDataSetChanged();


                if (userList.isEmpty()) {
                    constraintLayout.setVisibility(View.VISIBLE);

                    visibleStart();

                    imageView.setAlpha(0f);

                    imageView.setTranslationY(-200f);

                    imageView.animate().scaleX(1f).scaleX(1f).alpha(1f).translationY(0f).setDuration(2000L).setInterpolator(new BounceInterpolator());

                    imageView.setAlpha(1f);

                    imageView.setTranslationY(1f);


                } else {
                    constraintLayout.setVisibility(View.INVISIBLE);
                    hide();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });

    }


    public void hide() {


        imageView.setVisibility(View.INVISIBLE);
        textSub.setVisibility(View.INVISIBLE);
        textRub.setVisibility(View.INVISIBLE);
    }

    public void visibleStart() {
        imageView.setVisibility(View.VISIBLE);
        textSub.setVisibility(View.VISIBLE);
        textRub.setVisibility(View.VISIBLE);

    }


}
