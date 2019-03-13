package com.example.xdreamer.barangkushop.Remote;

import com.example.xdreamer.barangkushop.Object.MyResponse;
import com.example.xdreamer.barangkushop.Object.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAIJloUJk:APA91bEuuuF-swvJgq3Fid2FvbOOHQNG4fCT2b1WNWHnMC6ueG3AbDtoPLSuQI794aOcGDvn-gmmXfCsuFAEzIM6r2cU6Nf1ZBBc6_SlbESfDWXn3oDNoiSBT0WHEWWbkDwI8lWnadOi"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
