package sthlm.sweden.christofferwiregren.banino;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by christofferwiregren on 2017-10-05.
 */

public class RankningAdapter extends RecyclerView.Adapter<RankningAdapter.MyViewHolder> {


    private List<UserPoint> userList;


    public RankningAdapter(List<UserPoint> userList, Context context) {

        this.userList = userList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ranking, parent, false);


        return new RankningAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        UserPoint user = userList.get(position);

        String point = String.valueOf(user.getScore());


        holder.nickname.setText(user.getUser().getNickname().toString());

        holder.points.setText(point.toString());


        switch (position) {

            case 0: {

                holder.imageView.setImageResource(R.drawable.goldmedal);

                break;
            }

            case 1: {

                holder.imageView.setImageResource(R.drawable.silvermedal);

                break;
            }

            case 2: {

                holder.imageView.setImageResource(R.drawable.bronzemedal);


                break;
            }

            case 3: {

                holder.imageView.setImageResource(R.drawable.zinkmedal);


                break;

            }

            default: {

                holder.imageView.setImageResource(R.drawable.chevron);

            }


        }


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView nickname, points;
        public ImageView imageView;
        public RecyclerView recyclerView;


        public MyViewHolder(View view) {
            super(view);

            points = (TextView) view.findViewById(R.id.rankningPoints);
            nickname = (TextView) view.findViewById(R.id.rankingname);
            imageView = (ImageView) view.findViewById(R.id.rankingIcon);
            recyclerView = (RecyclerView) view.findViewById(R.id.rankingRecycler);


        }
    }


}


