package com.ygaps.travelapp;

import com.ygaps.travelapp.Model.Add_Stop_Point_Data;
import com.ygaps.travelapp.Model.Add_Stop_Point_Result;
import com.ygaps.travelapp.Model.Create_Tour_Data;
import com.ygaps.travelapp.Model.Create_Tour_Result;
import com.ygaps.travelapp.Model.DataGoogleLogin;
import com.ygaps.travelapp.Model.FacebookLoginResult;
import com.ygaps.travelapp.Model.Fb_data_login;
import com.ygaps.travelapp.Model.GoogleLoginResult;
import com.ygaps.travelapp.Model.InviteData;
import com.ygaps.travelapp.Model.InviteMemberData;
import com.ygaps.travelapp.Model.InviteMember_Result;
import com.ygaps.travelapp.Model.InviteResult;
import com.ygaps.travelapp.Model.ListTour;
import com.ygaps.travelapp.Model.LoginResult;
import com.ygaps.travelapp.Model.My_Tour_Result;
import com.ygaps.travelapp.Model.RegisterResult;
import com.ygaps.travelapp.Model.RequestOTP_Data;
import com.ygaps.travelapp.Model.RequestOTP_Result;
import com.ygaps.travelapp.Model.SendCommentData;
import com.ygaps.travelapp.Model.SendCommentResult;
import com.ygaps.travelapp.Model.SendRatingData;
import com.ygaps.travelapp.Model.SendRatingResult;
import com.ygaps.travelapp.Model.SendTokenFirebaseToServer_Result;
import com.ygaps.travelapp.Model.SendTokenFirebaseToSever_Data;
import com.ygaps.travelapp.Model.TourInforResult;
import com.ygaps.travelapp.Model.UpdateUserInfoData;
import com.ygaps.travelapp.Model.User_Info_Result;
import com.ygaps.travelapp.Model.User_login;
import com.ygaps.travelapp.Model.User_register;
import com.ygaps.travelapp.Model.VerifyOTP_Data;
import com.ygaps.travelapp.Model.VerifyOTP_Result;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface JsonPlaceHolderApi {
    @POST("user/login")
    Call<LoginResult> login(@Body User_login user_login);

    @POST("/user/login/by-facebook")
    Call<FacebookLoginResult> facebookLogin(@Body Fb_data_login accessToken);
    @POST("/user/register")
    Call<RegisterResult> register(@Body User_register user_register);
    @GET("tour/list")
    Call<ListTour> getTour(
            @Query("rowPerPage") int row,
            @Query("pageNum") int numb,
            @Query("orderBy") String otherBy,
            @Query("isDesc") boolean isDesc,
            @HeaderMap Map<String,String> headers);

    @POST("user/login/by-google")
    Call<GoogleLoginResult> googleLogin(@Body DataGoogleLogin dataGoogleLogin);

    @POST("tour/create")
    Call<Create_Tour_Result> createTour(@HeaderMap Map<String,String> headers, @Body Create_Tour_Data create_tour_data);

    @POST("tour/set-stop-points")
    Call<Add_Stop_Point_Result> addStopPoint(@HeaderMap Map<String,String> headers, @Body Add_Stop_Point_Data add_stop_point_data);

    @GET("tour/info")
    Call<TourInforResult> getTourInfo(@HeaderMap Map<String,String> headers,@Query("tourId") int id);


    @GET("tour/history-user")
    Call<My_Tour_Result> getMyTour(@HeaderMap Map<String,String> headers,@Query("pageIndex") int pageIndex,@Query("pageSize") int pageSize);

    @POST("tour/update-tour")
    Call<Create_Tour_Result> updateTour(@HeaderMap Map<String,String> headers,@Body Create_Tour_Data create_tour_data);

    @POST("user/request-otp-recovery")
    Call<RequestOTP_Result> requestOTP(@Body RequestOTP_Data requestOTP_data);

    @POST("user/verify-otp-recovery")
    Call<VerifyOTP_Result> verifyOTP(@Body VerifyOTP_Data verifyOTP_data);

    @GET("user/info")
    Call<User_Info_Result> getUserInfo(@HeaderMap Map<String,String> headers);

    @POST("user/edit-info")
    Call<VerifyOTP_Result> updateUserInfo(@HeaderMap Map<String,String> headers, @Body UpdateUserInfoData updateUserInfoData);

    @POST("user/notification/put-token")
    Call<SendTokenFirebaseToServer_Result> sendTokenToServer(@HeaderMap Map<String,String> headers, @Body SendTokenFirebaseToSever_Data sendTokenFirebaseToSever_data);

    @POST("tour/add/member")
    Call<InviteResult> inviteMember(@HeaderMap Map<String,String> headers, @Body InviteData inviteData);

    @POST("tour/comment")
    Call<SendCommentResult> sendComment(@HeaderMap Map<String,String> headers, @Body SendCommentData sendCommentData);

    @POST("tour/add/review")
    Call<SendRatingResult> sendRating(@HeaderMap Map<String,String> headers, @Body SendRatingData sendRatingData);

}
