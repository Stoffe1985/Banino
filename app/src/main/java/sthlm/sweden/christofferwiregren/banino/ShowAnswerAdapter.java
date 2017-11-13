package sthlm.sweden.christofferwiregren.banino;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by christofferwiregren on 2017-10-19.
 */

public class ShowAnswerAdapter extends RecyclerView.Adapter<ShowAnswerAdapter.MyViewHolder> {
    private List<GameAnswer> userList;

    public ShowAnswerAdapter(List<GameAnswer> answearList) {
        this.userList = answearList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.answer_player, parent, false);

        return new ShowAnswerAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final GameAnswer user = userList.get(position);


        String val = playerchooice(user.getChoice());

        switch (user.getChoice()) {

            case 1: {

                holder.imageChoice.setImageResource(R.drawable.onegreen);
                holder.nickname.setText(user.getUser().getNickname() + " tycker:");
                holder.answear.setText(val.toString());

                break;
            }

            case 2: {

                holder.imageChoice.setImageResource(R.drawable.twogreen);
                holder.nickname.setText(user.getUser().getNickname() + " tycker:");
                holder.answear.setText(val.toString());

                break;
            }

            case 3: {

                holder.imageChoice.setImageResource(R.drawable.threegreen);
                holder.nickname.setText(user.getUser().getNickname() + " tycker:");
                holder.answear.setText(val.toString());


                break;
            }

            case 4: {

                holder.imageChoice.setImageResource(R.drawable.fourgreen);
                holder.nickname.setText(user.getUser().getNickname() + " tycker:");
                holder.answear.setText(val.toString());


                break;

            }

            default: {

                holder.imageChoice.setImageResource(R.drawable.chevron);
                holder.nickname.setText(user.getUser().getNickname() + " tycker:");
                holder.answear.setText(val.toString());

            }
        }


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nickname, answear;
        public ImageView imageChoice;

        public MyViewHolder(View view) {
            super(view);

            nickname = (TextView) view.findViewById(R.id.txtplayertycker);
            answear = (TextView) view.findViewById(R.id.txttycker);
            imageChoice = (ImageView) view.findViewById(R.id.imageNumber);

        }
    }

    public String playerchooice(int choice) {

        String val = "";

        switch (choice) {
            case 1: {
                val = "Stämmer inte alls";

                break;

            }
            case 2: {
                val = "Stämmer till någon del";
                break;
            }

            case 3: {
                val = "Stämmer i huvudsak";
                break;
            }

            case 4: {
                val = "Stämmer mycket bra";
                break;
            }
            default: {

            }
        }

        return val;

    }

}
