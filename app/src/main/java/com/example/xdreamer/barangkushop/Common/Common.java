package com.example.xdreamer.barangkushop.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.xdreamer.barangkushop.Object.User;
import com.example.xdreamer.barangkushop.Remote.APIService;
import com.example.xdreamer.barangkushop.Remote.RetrofitClient;

import java.util.Calendar;
import java.util.Locale;

public class Common {
    public static User currentUser;

    public static String PHONE_TEXT = "userPhone";

    public static final String INTENT_PRODUCT_ID = "ProductId";

    private static final String BASE_URL = "https://fcm.googleapis.com/";

    public static APIService getFCMService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }

    public static String convertCodeToStatus(String code) {
        if (code.equals("0"))
            return "Menunggu Pembayaran";
        else if (code.equals("1"))
            return "Pembayaran Berhasil";
        else if (code.equals("2"))
            return "Sedang di Proses Admin";
        else
            return "Transaksi Selesai";
    }

    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
            if (infos != null) {
                for (int i = 0; i < infos.length; i++) {
                    if (infos[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static final String DELETE = "Delete";

    public static final String USER_KEY = "User";
    public static final String PWD_KEY = "Password";

    public static String getDate(long time) {
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        StringBuilder date = new StringBuilder(
                android.text.format.DateFormat.format("dd-MM-yyyy HH:mm", calendar).toString());
        return date.toString();
    }
}
