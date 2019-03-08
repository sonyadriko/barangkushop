package com.example.xdreamer.barangkushop.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.xdreamer.barangkushop.Object.User;

public class Common {
    public static User currentUser;

    public static String convertCodeToStatus(String code){
        if (code.equals("0"))
            return "Placed";
        else if (code.equals("1"))
            return "On Procced";
        else
            return "Shipped";
    }

    public static boolean isConnectedToInternet(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null){
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if (infos != null){
                for (int i=0;i<infos.length;i++){
                    if (infos[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
    public static final String DELETE="Delete";

    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";
}
