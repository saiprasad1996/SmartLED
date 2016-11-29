package tk.saiprasadm.smartled;

import android.net.wifi.WifiManager;

public class BaseTools {
    WifiManager wifi;

    public static int getIntOrElse(String val, int e) {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            // Log exception.
            return e;
        }
    }

    public static boolean fieldIsEmpty(String str) {
        String temp = str.trim();
        if (temp.isEmpty())
            return true;
        else
            return false;
    }


}
