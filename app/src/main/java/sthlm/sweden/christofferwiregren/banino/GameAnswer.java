package sthlm.sweden.christofferwiregren.banino;

/**
 * Created by christofferwiregren on 2017-10-09.
 */

public class GameAnswer {

    private int choice;
    private  User user;


    public GameAnswer(int choice, String id, String name) {

        this.choice = choice;
        this.user = new User(id, name);
    }


    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }



    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
