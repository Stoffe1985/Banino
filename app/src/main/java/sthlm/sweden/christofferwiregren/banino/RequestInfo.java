package sthlm.sweden.christofferwiregren.banino;

/**
 * Created by christofferwiregren on 2017-09-22.
 */

public class RequestInfo {

    private String senderName;
    private String senderId;
    private String gameID;
    private String answear;

    public RequestInfo() {
        this("", "", "", "");

    }

    public String getAnswear() {
        return answear;
    }

    public void setAnswear(String answear) {
        this.answear = answear;
    }

    public RequestInfo(String senderName, String senderId, String gameID, String answear) {

        this.senderId = senderId;
        this.senderName = senderName;
        this.gameID = gameID;
        this.answear = answear;

    }


    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }


}
