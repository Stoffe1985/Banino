package sthlm.sweden.christofferwiregren.banino;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by christofferwiregren on 2017-09-28.
 */

public class ReadToPlayAdapter extends RecyclerView.Adapter<ReadToPlayAdapter.MyViewHolder> {
    private List<User> userList;


    public ReadToPlayAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.readytoplay, parent, false);


        return new ReadToPlayAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        final User user = userList.get(position);
        holder.nickname.setText(user.getNickname());
        holder.imageView.setImageResource(R.drawable.girl);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nickname;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            nickname = (TextView) view.findViewById(R.id.nicknameReady);
            imageView = (ImageView) view.findViewById(R.id.imageReady);


        }
    }


}