package sthlm.sweden.christofferwiregren.banino;

import android.support.annotation.NonNull;

/**
 * Created by christofferwiregren on 2017-10-05.
 */

public class UserPoint implements Comparable<UserPoint> {
    private User user;
    private int score;

    public UserPoint(String id, String name , int score) {
        this.user = new User (id, name);
        this.score = score;
    }

    public UserPoint(){
        this("", "", 0);
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }



    @Override
    public String toString() {
        return "UserPoint{" +
                "user=" + user +
                ", Score=" + score +
                '}';
    }

    @Override
    public int compareTo(@NonNull UserPoint userPoint) {
        int compareQuantity = ((UserPoint) userPoint).getScore();

        //ascending order
        return compareQuantity - this.score;
    }
}
