package com.example.loginandregister;

import android.media.session.MediaSession;

import androidx.cardview.widget.CardView;

import com.example.loginandregister.Model.Add_Stop_Point_Data;
import com.example.loginandregister.Model.Add_Stop_Point_Result;
import com.example.loginandregister.Model.Create_Tour_Data;
import com.example.loginandregister.Model.Create_Tour_Result;
import com.example.loginandregister.Model.DataGoogleLogin;
import com.example.loginandregister.Model.FacebookLoginResult;
import com.example.loginandregister.Model.Fb_data_login;
import com.example.loginandregister.Model.GoogleLoginResult;
import com.example.loginandregister.Model.ListTour;
import com.example.loginandregister.Model.LoginResult;
import com.example.loginandregister.Model.RegisterResult;
import com.example.loginandregister.Model.TourInforData;
import com.example.loginandregister.Model.TourInforResult;
import com.example.loginandregister.Model.User_login;
import com.example.loginandregister.Model.User_register;

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
}
