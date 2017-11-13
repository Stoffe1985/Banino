package sthlm.sweden.christofferwiregren.banino;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RankingFragment extends Fragment {

    private List<UserPoint> userList = new ArrayList<UserPoint>();
    private RecyclerView recyclerView;
    private RankningAdapter mAdapter;
    private UserPoint userPoint;
    private String gameId="";
    private String uid = "";
    private Button btnAvsluta;

    public RankingFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ranking, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        recyclerView = (RecyclerView) view.findViewById(R.id.rankingRecycler);
        btnAvsluta = (Button)view.findViewById(R.id.btnAvslutaspel);

        mAdapter = new RankningAdapter(userList, getContext());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        btnAvsluta.setEnabled(false);


        getGameID();

        btnAvsluta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestDataFirebase requestDataFirebase = new RequestDataFirebase(gameId);

                requestDataFirebase.removeFromFirebase();


                MainActivityFragment mainActivityFragment = new MainActivityFragment();

                try {
                    ((FragmentActivity) view.getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.rootMain,mainActivityFragment).addToBackStack(null).commit();

                }catch (Exception e){

                }



            }
        });

    }



    public void getGameID(){
        try {

            Bundle bundle = getArguments();

            String currentGameID = bundle.getString("GameID");

            this.gameId = currentGameID.toString();
            getPoints();
            btnAvsluta.setEnabled(true);


        }catch (Exception e){


        }
    }

    private void getPoints() {


            uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("FinalPoint").child(gameId);


            databaseReference.orderByChild("score").
                    addValueEventListener(new ValueEventListener() {


                        @Override
                        public void onDataChange(DataSnapshot datasnapshot) {
                            userList.clear();


                            for (DataSnapshot ds: datasnapshot.getChildren()){


                                int point = Integer.parseInt(ds.child("score").getValue().toString());

                                String name = ds.child("name").getValue().toString();

                                userPoint = new UserPoint(uid,name, point);

                               userList.add(userPoint);


                            }

                            Collections.sort(userList);

                            mAdapter.notifyDataSetChanged();


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

    }
}
