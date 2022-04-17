package com.fyp.errandmanagement.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAA0cdmw8:APA91bFMTkzYlrX9cL6GatHdXi8Dh88w2oj2dLCbjkTyfTYOuby2NVL8LHPYz6s2bUvLkVeQeR3wX0SNPa5SUpXwk_Wya11yN1yyOKl0ZgW46OGj6ziUtSKkNK2ykdrk4kDVcwRll_BM"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}