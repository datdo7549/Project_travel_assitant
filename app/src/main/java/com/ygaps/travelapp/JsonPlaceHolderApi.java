package com.ygaps.travelapp;

import com.ygaps.travelapp.Model.Add_Stop_Point_Data;
import com.ygaps.travelapp.Model.Add_Stop_Point_Result;
import com.ygaps.travelapp.Model.ChatMemberOnRoad_Data;
import com.ygaps.travelapp.Model.Create_Tour_Data;
import com.ygaps.travelapp.Model.Create_Tour_Result;
import com.ygaps.travelapp.Model.DataGoogleLogin;
import com.ygaps.travelapp.Model.FacebookLoginResult;
import com.ygaps.travelapp.Model.Fb_data_login;
import com.ygaps.travelapp.Model.GetCoordinate_Data;
import com.ygaps.travelapp.Model.GetCoordinate_Result;
import com.ygaps.travelapp.Model.GetFeedBackList_Result;
import com.ygaps.travelapp.Model.GetNotification_Result;
import com.ygaps.travelapp.Model.GetReview_Data;
import com.ygaps.travelapp.Model.GetReview_Result;
import com.ygaps.travelapp.Model.GetSugestStopPoint_Result;
import com.ygaps.travelapp.Model.GetSuggestStoppoint_Data;
import com.ygaps.travelapp.Model.GoogleLoginResult;
import com.ygaps.travelapp.Model.InviteData;
import com.ygaps.travelapp.Model.InviteMemberData;
import com.ygaps.travelapp.Model.InviteMember_Result;
import com.ygaps.travelapp.Model.InviteResult;
import com.ygaps.travelapp.Model.ListTour;
import com.ygaps.travelapp.Model.LoginResult;
import com.ygaps.travelapp.Model.My_Tour_Result;
import com.ygaps.travelapp.Model.Rating_result;
import com.ygaps.travelapp.Model.RegisterResult;
import com.ygaps.travelapp.Model.RemoveStopPointResult;
import com.ygaps.travelapp.Model.RequestOTP_Data;
import com.ygaps.travelapp.Model.RequestOTP_Result;
import com.ygaps.travelapp.Model.SearchUserByKeyword_Result;
import com.ygaps.travelapp.Model.SendCommentData;
import com.ygaps.travelapp.Model.SendCommentResult;
import com.ygaps.travelapp.Model.SendFeedback_Data;
import com.ygaps.travelapp.Model.SendRatingData;
import com.ygaps.travelapp.Model.SendRatingResult;
import com.ygaps.travelapp.Model.SendReportComment_Data;
import com.ygaps.travelapp.Model.SendReview_Data;
import com.ygaps.travelapp.Model.SendTokenFirebaseToServer_Result;
import com.ygaps.travelapp.Model.SendTokenFirebaseToSever_Data;
import com.ygaps.travelapp.Model.TourInforResult;
import com.ygaps.travelapp.Model.UpdatePasswordData;
import com.ygaps.travelapp.Model.UpdatePasswordResult;
import com.ygaps.travelapp.Model.UpdateUserInfoData;
import com.ygaps.travelapp.Model.UserResponde_Data;
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

    @GET("tour/remove-stop-point")
    Call<RemoveStopPointResult> removeStopPoint(@HeaderMap Map<String,String> headers,@Query("stopPointId") String stopPointId);

    @POST("user/update-password")
    Call<UpdatePasswordResult> updatePassword(@HeaderMap Map<String,String> headers, @Body UpdatePasswordData updatePasswordData);

    @POST("tour/current-users-coordinate")
    Call<GetCoordinate_Result> getCoordinate(@HeaderMap Map<String,String> headers, @Body GetCoordinate_Data getCoordinateData );


    @GET("user/search")
    Call<SearchUserByKeyword_Result> search_user(@Query("searchKey") String searchKey,@Query("pageIndex") int pageIndex,@Query("pageSize") String pageSize);

    @POST("tour/response/invitation")
    Call<InviteMember_Result> responde_user(@HeaderMap Map<String,String> headers,@Body UserResponde_Data userResponde_data);

    @POST("tour/report/review")
    Call<InviteMember_Result> report_comment(@HeaderMap Map<String,String> headers,@Body SendReportComment_Data sendReportComment_data);

    @GET("tour/get/review-list")
    Call<Rating_result> get_rating_list(@HeaderMap Map<String,String> headers,@Query("tourId") int tourId,@Query("pageIndex") int pageIndex,@Query("pageSize") String pageSize);

    @POST("tour/suggested-destination-list")
    Call<GetSugestStopPoint_Result> get_suggets_stop_point(@HeaderMap Map<String,String> headers,@Body GetSuggestStoppoint_Data getSuggestStoppoint_data);

    @GET("tour/get/review-list")
    Call<GetReview_Result> get_review(@HeaderMap Map<String,String> headers,@Query("tourId") int tourId,@Query("pageIndex") int pageIndex,@Query("pageSize") String pageSize);


    @GET("tour/get/feedback-service")
    Call<GetFeedBackList_Result> getFeedback(@HeaderMap Map<String,String> headers,@Query("serviceId") int serviceId,@Query("pageIndex") int pageIndex,@Query("pageSize") String pageSize);


    @POST("tour/add/feedback-service")
    Call<InviteMember_Result> send_feed_back(@HeaderMap Map<String,String> headers,@Body SendFeedback_Data sendFeedback_data);

    @POST("tour/notification")
    Call<InviteMember_Result> chat_member(@HeaderMap Map<String,String> headers, @Body ChatMemberOnRoad_Data chatMemberOnRoad_data);


    @GET("tour/notification-list")
    Call<GetNotification_Result> get_notification(@HeaderMap Map<String,String> headers,@Query("tourId") String tourId,@Query("pageIndex") int pageIndex,@Query("pageSize") String pageSize);

}
