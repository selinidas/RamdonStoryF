package com.android.randomstory.Notification;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key= AAAA405bsLE:APA91bHgONGb9WJwpTK63mhgWtzfIe0XPF-H5tHBtqW2p-L50TP4Uuw3Eo6TLkpRYlomcSQiKFKK8yrHxNhWJYzPtoRF7qgZUUajIY2TjMvKNSdqljhS0J3G9uPQ0gHKYhRMOkEObSCg"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
