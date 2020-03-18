package ie.adsfinder.adsfinder;

import android.util.Log;

import java.security.KeyStore;

public class AdsfinderUtils {
    public static boolean isStringEmpty(String s) {
        if (s == null || s.isEmpty()) {
            return true;
        } else return false;
    }

    public static String capitalizeWord(String str){
        if (isStringEmpty(str)) {
            return "";
        }
        String[] arr = str.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            try {
                sb.append(Character.toUpperCase(arr[i].charAt(0)));
                sb.append(arr[i].substring(1)).append(" ");
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
        }
        return sb.toString().trim();
    }



}
