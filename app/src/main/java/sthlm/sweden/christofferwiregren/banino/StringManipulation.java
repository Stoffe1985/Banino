package sthlm.sweden.christofferwiregren.banino;

/**
 * Created by christofferwiregren on 2017-09-12.
 */

public class StringManipulation {

    public static String expandUsername(String username) {
        return username.replace(".", " ");
    }

    public static String condenseUsername(String username) {
        return username.replace(" ", ".");
    }

    public static String removeChar(String s, char c) {
        StringBuffer r = new StringBuffer(s.length());
        r.setLength(s.length());
        int current = 0;
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur != c) r.setCharAt(current++, cur);
        }
        return r.toString();
    }
}