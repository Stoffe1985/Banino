package sthlm.sweden.christofferwiregren.banino;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by christofferwiregren on 2017-09-11.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {


    private List<User> userList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nickname, subname;
        public ImageView imageView;
        public Button btnRemove;
        private RecyclerView recyclerView;
        private UserAdapter mAdapter;

        public MyViewHolder(View view) {
            super(view);
            nickname = (TextView) view.findViewById(R.id.contactNickname);
            imageView = (ImageView) view.findViewById(R.id.hero);
            btnRemove = (Button) view.findViewById(R.id.btnRemove);
            recyclerView = (RecyclerView) view.findViewById(R.id.contactList);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);


        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        User user = userList.get(position);

        holder.nickname.setText(user.getNickname());
        holder.imageView.setImageResource(R.drawable.girl);


        holder.btnRemove.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    holder.btnRemove.setBackgroundResource(R.drawable.crossred);


                } else if (event.getAction() == MotionEvent.ACTION_UP) {


                    holder.btnRemove.setBackgroundResource(R.drawable.crossblack);
                    userList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, userList.size());

                }


                return false;
            }
        });

    }


    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
}
