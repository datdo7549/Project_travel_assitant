package com.ygaps.travelapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.squareup.picasso.Transformation;
import com.ygaps.travelapp.Adapter.CustomAdapterForTourInfo_Comment;
import com.ygaps.travelapp.Adapter.CustomAdapterForTourInfo_Member;
import com.ygaps.travelapp.Adapter.CustomAdapterForTourInfo_Rating;
import com.ygaps.travelapp.Adapter.CustomAdapterForTourInfo_StopPoint;
import com.ygaps.travelapp.Adapter.CustomAdapterUserSearch;
import com.ygaps.travelapp.Adapter.CustomAdapter_Feedback;
import com.ygaps.travelapp.Model.Add_Stop_Point_Data;
import com.ygaps.travelapp.Model.Add_Stop_Point_Result;
import com.ygaps.travelapp.Model.CommentResult_TourInfo;
import com.ygaps.travelapp.Model.CoordList;
import com.ygaps.travelapp.Model.CoordinateSet;
import com.ygaps.travelapp.Model.Feedback;
import com.ygaps.travelapp.Model.Feedback_2;
import com.ygaps.travelapp.Model.GetFeedBackList_Result;
import com.ygaps.travelapp.Model.GetReview_Result;
import com.ygaps.travelapp.Model.GetSugestStopPoint_Result;
import com.ygaps.travelapp.Model.GetSuggestStoppoint_Data;
import com.ygaps.travelapp.Model.InviteData;
import com.ygaps.travelapp.Model.InviteMember_Result;
import com.ygaps.travelapp.Model.InviteResult;
import com.ygaps.travelapp.Model.Member;
import com.ygaps.travelapp.Model.Rating_result;
import com.ygaps.travelapp.Model.RemoveStopPointResult;
import com.ygaps.travelapp.Model.Review;
import com.ygaps.travelapp.Model.SearchUserByKeyword_Result;
import com.ygaps.travelapp.Model.SendCommentData;
import com.ygaps.travelapp.Model.SendCommentResult;
import com.ygaps.travelapp.Model.SendFeedback_Data;
import com.ygaps.travelapp.Model.SendRatingData;
import com.ygaps.travelapp.Model.SendRatingResult;
import com.ygaps.travelapp.Model.SendReportComment_Data;
import com.ygaps.travelapp.Model.SendReview_Data;
import com.ygaps.travelapp.Model.StopPointResult_TourInfo;
import com.ygaps.travelapp.Model.Stop_Point;
import com.ygaps.travelapp.Model.TourInforResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.ygaps.travelapp.Model.User;
import com.ygaps.travelapp.Model.onClickRecycleView;

import java.io.File;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ygaps.travelapp.LoginActivity.URL;

import static com.ygaps.travelapp.MapActivity.arrayProvince;

import com.ygaps.travelapp.Model.onCLickRecycleView_Rating;

public class Tour_Info extends AppCompatActivity {
    private TextView name_tour_info;
    private TextView date_tour_info;
    private TextView cost_tour_info;
    private TextView adult_tour_info;
    private TextView status;
    private ImageView isPrivate;
    private ImageView back;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String id_tour;
    private String token;
    private String user_id;
    private ImageView cover_tour_imagee;
    private ListView listView_stop_point;
    private RecyclerView listView_comment;
    private RecyclerView listView_member;
    private RecyclerView listview_rating;
    private CustomAdapterForTourInfo_StopPoint customAdapterForTourInfoStopPoint;
    private CustomAdapterForTourInfo_Comment customAdapterForTourInfo_comment;
    private CustomAdapterForTourInfo_Member customAdapterForTourInfo_member;
    private CustomAdapterUserSearch customAdapterUserSearch;
    private ListView listView_user_search;
    private ArrayList<String> arrayList = new ArrayList<>();

    private Dialog stop_point_info;
    private Dialog update_stop_point;
    private Dialog send_comment_popup;
    private Dialog send_rating_popup;
    private Dialog search_user;

    private Dialog add_feedback;
    private ImageView add_comment_of_user;
    private ImageView add_rating;
    private ImageView add_member;
    private ImageView add_stop_point;

    private double mNewLat;
    private double mNewLong;
    private String mNewAddress;
    private Boolean ISPV;

    private ArrayList<CommentResult_TourInfo> arrayComment = new ArrayList<>();
    private ArrayList<Feedback> arrayList_feedback = new ArrayList<>();
    private onClickRecycleView onClickRecycleView;
    private onCLickRecycleView_Rating onCLickRecycleView_rating;


    private ArrayList<CoordList> coordLists = new ArrayList<>();

    private CustomAdapter_Feedback customAdapter_feedback;

    private ImageView follow_tour;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_tour__info);
        mapping();



       /* LatLong2 lat1=new LatLong2(48.033090,69.814943);
        LatLong2 lat2=new LatLong2(2.739697,43.903915);
        LatLong2 lat3=new LatLong2(2.739697,43.903915);
        LatLong2 lat4=new LatLong2(-6.658560,143.575005);
        LatLong2 lat5=new LatLong2(-6.658560,143.575005);
        LatLong2 lat6=new LatLong2(49.555488,143.322819);
        LatLong2 lat7=new LatLong2(49.555488,143.322819);
        LatLong2 lat8=new LatLong2(48.033090,69.814943);*/


        CoordinateSet coordinateSet_1 = new CoordinateSet(48.033090, 69.814943);
        CoordinateSet coordinateSet_2 = new CoordinateSet(2.739697, 43.903915);
        CoordinateSet coordinateSet_3 = new CoordinateSet(2.739697, 43.903915);
        CoordinateSet coordinateSet_4 = new CoordinateSet(-6.658560, 143.575005);
        CoordinateSet coordinateSet_5 = new CoordinateSet(-6.658560, 143.575005);
        CoordinateSet coordinateSet_6 = new CoordinateSet(49.555488, 143.322819);
        CoordinateSet coordinateSet_7 = new CoordinateSet(49.555488, 143.322819);
        CoordinateSet coordinateSet_8 = new CoordinateSet(48.033090, 69.814943);


        ArrayList<CoordinateSet> cs1 = new ArrayList<>();
        cs1.add(coordinateSet_1);
        cs1.add(coordinateSet_2);

        ArrayList<CoordinateSet> cs2 = new ArrayList<>();
        cs2.add(coordinateSet_3);
        cs2.add(coordinateSet_4);

        ArrayList<CoordinateSet> cs3 = new ArrayList<>();
        cs3.add(coordinateSet_5);
        cs3.add(coordinateSet_6);

        ArrayList<CoordinateSet> cs4 = new ArrayList<>();
        cs4.add(coordinateSet_7);
        cs4.add(coordinateSet_8);


        CoordList cl1 = new CoordList(cs1);
        CoordList cl2 = new CoordList(cs2);
        CoordList cl3 = new CoordList(cs3);
        CoordList cl4 = new CoordList(cs4);


        coordLists.add(cl1);
        coordLists.add(cl2);
        coordLists.add(cl3);
        coordLists.add(cl4);


        final Bundle bundle = getIntent().getExtras();
        id_tour = bundle.getString("tour_id");
        token = bundle.getString("token");
        user_id = bundle.getString("user_id");
        Toast.makeText(getApplicationContext(),"user id: "+user_id,Toast.LENGTH_SHORT).show();
        follow_tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("tour_id",id_tour);
                bundle.putString("user_id",user_id);
                bundle.putString("token_1",token);
                bundle.putInt("mode",1);
                Intent intent=new Intent(getApplicationContext(),MapTemp.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        cover_tour_imagee = findViewById(R.id.cover_tour_image);
        final Transformation transformation = new MaskTransformation(getApplicationContext(), R.drawable.rounded_convers_transformation);

        Picasso.get()
                .load("https://images.unsplash.com/photo-1494851125693-26b0ada2b614?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80")
                .transform(transformation)
                .fit()
                .into(cover_tour_imagee);





        Toast.makeText(getApplicationContext(), "ID TOUR: " + id_tour+"   " +user_id, Toast.LENGTH_SHORT).show();

        onClickRecycleView = new onClickRecycleView() {
            @Override
            public void setClick(int pos) {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", token);
                SendReportComment_Data sendReportComment_data = new SendReportComment_Data(arrayComment.get(pos).getId());

                Call<InviteMember_Result> call = jsonPlaceHolderApi.report_comment(map, sendReportComment_data);
                call.enqueue(new Callback<InviteMember_Result>() {
                    @Override
                    public void onResponse(Call<InviteMember_Result> call, Response<InviteMember_Result> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Report Fail", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Report Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<InviteMember_Result> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        onCLickRecycleView_rating = new onCLickRecycleView_Rating() {
            @Override
            public void rating(int pos) {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", token);
                SendReportComment_Data sendReportComment_data = new SendReportComment_Data(arrayList_feedback.get(pos).getId());

                Call<InviteMember_Result> call = jsonPlaceHolderApi.report_comment(map, sendReportComment_data);
                call.enqueue(new Callback<InviteMember_Result>() {
                    @Override
                    public void onResponse(Call<InviteMember_Result> call, Response<InviteMember_Result> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Report Fail", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Report Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<InviteMember_Result> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Log.d("id", id_tour);

        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        final Map<String, String> map = new HashMap<>();
        map.put("Authorization", token);

        Call<TourInforResult> call = jsonPlaceHolderApi.getTourInfo(map, Integer.parseInt(id_tour));
        call.enqueue(new Callback<TourInforResult>() {
            @Override
            public void onResponse(Call<TourInforResult> call, Response<TourInforResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(Tour_Info.this, "That bai", Toast.LENGTH_SHORT).show();
                } else {
                    //Lay duoc danh sach Stop Point cua cai tour do:
                    final ArrayList<StopPointResult_TourInfo> stopPointResult_tourInfo = response.body().getStopPoints();


                    //Lay duoc danh sach Comment cua cai tour do:
                    arrayComment = response.body().getComments();

                    //Lay duoc danh sach Member cua cai tour do:
                    ArrayList<Member> arrayMember = response.body().getMembers();
                    name_tour_info.setText(response.body().getName());

                    Boolean is_Private = response.body().getPrivate();
                    ISPV = is_Private;
                    if (is_Private) {
                        isPrivate.setBackgroundResource(R.drawable.lock);
                    } else {
                        isPrivate.setBackgroundResource(R.drawable.open);
                    }

                    switch (response.body().getStatus()) {
                        case -1: {
                            status.setText("Canceled");
                            break;
                        }
                        case 0: {
                            status.setText("Open");
                            break;
                        }
                        case 1: {
                            status.setText("Started");
                            break;
                        }
                        case 2: {
                            status.setText("Closed");
                            break;
                        }
                        default:
                            break;
                    }
                    long miliStartDate = Long.parseLong(response.body().getStartDate());
                    final Date startD = new Date(miliStartDate);
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String temp1 = dateFormat.format(startD);
                    int start = Integer.parseInt(temp1.substring(0, 2));

                    //Xu ly date
                    long miliEndDate = Long.parseLong(response.body().getEndDate());
                    Date endD = new Date(miliEndDate);
                    DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                    String temp2 = dateFormat1.format(endD);
                    int end = Integer.parseInt(temp2.substring(0, 2));
                    date_tour_info.setText((end - start) + "");

                    //Xu ly cost
                    DecimalFormat dcf = new DecimalFormat("#,###");
                    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
                    dfs.setGroupingSeparator('.');
                    dcf.setDecimalFormatSymbols(dfs);

                    double min = Double.parseDouble(response.body().getMinCost());
                    double max = Double.parseDouble(response.body().getMaxCost());

                    StringBuilder costBuild = new StringBuilder();
                    final String minCost = dcf.format(min);
                    final String maxCost = dcf.format(max);
                    costBuild.append(minCost).append("$ -> ").append(maxCost).append("$");
                    cost_tour_info.setText(costBuild);
                    //Xu ly adult, child
                    adult_tour_info.setText(response.body().getAdults() + " adult, " + response.body().getChilds() + " child");


                    //Xu li Stop Point
                    if (stopPointResult_tourInfo.size() != 0) {
                        customAdapterForTourInfoStopPoint = new CustomAdapterForTourInfo_StopPoint(Tour_Info.this, R.layout.list_stop_point_tour_info, stopPointResult_tourInfo);
                        listView_stop_point.setAdapter(customAdapterForTourInfoStopPoint);

                        listView_stop_point.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                                if (stopPointResult_tourInfo.get(position).getCheck() == 1) {
                                    LinearLayout edit_delete_stop_point = view.findViewById(R.id.edit_delete_stop_point);
                                    stopPointResult_tourInfo.get(position).setCheck(0);
                                    edit_delete_stop_point.setVisibility(View.INVISIBLE);
                                } else {

                                    //stop_point_info.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    stop_point_info.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                                    stop_point_info.setContentView(R.layout.stop_point_info_popup);
                                    ImageView image_stop_point = stop_point_info.findViewById(R.id.image_stop_point_info);
                                    TextView name_stop_point = stop_point_info.findViewById(R.id.name_stop_point_info);
                                    TextView date_stop_point = stop_point_info.findViewById(R.id.date_stop_point_info);
                                    TextView cost_stop_point = stop_point_info.findViewById(R.id.cost_stop_point_info);
                                    TextView type_service = stop_point_info.findViewById(R.id.type_service_stop_point);
                                    ImageView exit_stop_point_info_popup = stop_point_info.findViewById(R.id.exit_stop_point_info_popup);

                                    ImageView add_feedback_image=stop_point_info.findViewById(R.id.add_feedback);
                                    Toast.makeText(getApplicationContext(),"id stop point ne: "+stopPointResult_tourInfo.get(position).getId()+"---"+stopPointResult_tourInfo.get(position).getLat(),Toast.LENGTH_SHORT).show();
                                    Call<GetFeedBackList_Result> call1_5=jsonPlaceHolderApi.getFeedback(map,stopPointResult_tourInfo.get(position).getId(),1,"100");
                                    call1_5.enqueue(new Callback<GetFeedBackList_Result>() {
                                        @Override
                                        public void onResponse(Call<GetFeedBackList_Result> call, Response<GetFeedBackList_Result> response) {
                                            if (!response.isSuccessful())
                                            {
                                                Toast.makeText(getApplicationContext(),"Khong thanh cong",Toast.LENGTH_SHORT).show();
                                            }
                                            else {

                                                ArrayList<Feedback_2> arrayFeedback=response.body().getFeedbackList();
                                                ListView listView=stop_point_info.findViewById(R.id.list_feedback);
                                                customAdapter_feedback=new CustomAdapter_Feedback(getApplicationContext(),R.layout.custom_feedback_stop_point,arrayFeedback);
                                                listView.setAdapter(customAdapter_feedback);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<GetFeedBackList_Result> call, Throwable t) {
                                            Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    add_feedback_image.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            add_feedback.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                                            add_feedback.setContentView(R.layout.addfeedback_popup);

                                            final TextView feedback_textview=add_feedback.findViewById(R.id.feedback_edit);
                                            Button add_feedback_btn=add_feedback.findViewById(R.id.send_feedback);
                                            final RatingBar ratingBar = add_feedback.findViewById(R.id.rating_point_feedback);



                                            add_feedback_btn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String feedback=feedback_textview.getText().toString();
                                                    int point=(int)ratingBar.getRating();
                                                    SendFeedback_Data sendFeedback_data=new SendFeedback_Data(stopPointResult_tourInfo.get(position).getId(),feedback,point);
                                                    Call<InviteMember_Result> call1_6=jsonPlaceHolderApi.send_feed_back(map,sendFeedback_data);
                                                    call1_6.enqueue(new Callback<InviteMember_Result>() {
                                                        @Override
                                                        public void onResponse(Call<InviteMember_Result> call, Response<InviteMember_Result> response) {
                                                            if (!response.isSuccessful())
                                                            {
                                                                Toast.makeText(getApplicationContext(),"Khong thanh cong"+response.code(),Toast.LENGTH_SHORT).show();
                                                            }else {
                                                                Toast.makeText(getApplicationContext(),"Thanh cong",Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                        @Override
                                                        public void onFailure(Call<InviteMember_Result> call, Throwable t) {
                                                            Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });

                                            add_feedback.show();





                                        }
                                    });


                                    exit_stop_point_info_popup.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {



                                            stop_point_info.dismiss();
                                        }
                                    });
                                    Picasso.get()
                                            .load("https://cdn.pixabay.com/photo/2016/03/04/19/36/beach-1236581_960_720.jpg")
                                            .fit()
                                            .into(image_stop_point);
                                    name_stop_point.setText(stopPointResult_tourInfo.get(position).getName());
                                    //Xu ly date
                                    long miliStartDate = Long.parseLong(stopPointResult_tourInfo.get(position).getArrivalAt());
                                    final Date startD = new Date(miliStartDate);
                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                    String temp1 = dateFormat.format(startD);
                                    StringBuilder dateBuild = new StringBuilder();

                                    //Xu ly date
                                    long miliEndDate = Long.parseLong(stopPointResult_tourInfo.get(position).getLeaveAt());
                                    Date endD = new Date(miliEndDate);
                                    DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                                    String temp2 = dateFormat1.format(endD);
                                    dateBuild.append(temp1).append(" -> ").append(temp2);
                                    date_stop_point.setText(dateBuild);

                                    //Xu ly cost
                                    DecimalFormat dcf = new DecimalFormat("#,###");
                                    DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.getDefault());
                                    dfs.setGroupingSeparator('.');
                                    dcf.setDecimalFormatSymbols(dfs);

                                    double min = Double.parseDouble(stopPointResult_tourInfo.get(position).getMinCost());
                                    double max = Double.parseDouble(stopPointResult_tourInfo.get(position).getMaxCost());

                                    StringBuilder costBuild = new StringBuilder();
                                    String minCost = dcf.format(min);
                                    String maxCost = dcf.format(max);
                                    costBuild.append(minCost).append("$ -> ").append(maxCost).append("$");
                                    cost_stop_point.setText(costBuild);

                                    type_service.setText(stopPointResult_tourInfo.get(position).getServiceTypeId() + "");

                                    stop_point_info.show();
                                }

                            }
                        });
                        listView_stop_point.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                                final int d = position;
                                LinearLayout edit_delete_stop_point = view.findViewById(R.id.edit_delete_stop_point);
                                Button stop_point_edit = view.findViewById(R.id.stop_point_edit);
                                Button stop_point_delete = view.findViewById(R.id.stop_point_delete);
                                final Animation animation = AnimationUtils.loadAnimation(Tour_Info.this, R.anim.animation);
                                stopPointResult_tourInfo.get(position).setCheck(1);
                                edit_delete_stop_point.startAnimation(animation);
                                edit_delete_stop_point.setVisibility(View.VISIBLE);
                                stop_point_edit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Bundle bundle1 = new Bundle();
                                        bundle1.putString("id_tour", String.valueOf(id));
                                        bundle1.putInt("id_stop_point", stopPointResult_tourInfo.get(position).getId());
                                        bundle1.putInt("type", 1);
                                        update_stop_point.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        update_stop_point.setContentView(R.layout.add_stop_point);
                                        TextView title = update_stop_point.findViewById(R.id.title_add_stop_point);
                                        title.setText("Update Stop Point");
                                        final EditText newName = update_stop_point.findViewById(R.id.diem_xuat_phat);
                                        final Spinner type = update_stop_point.findViewById(R.id.restaurant);
                                        ArrayAdapter<CharSequence> adapter_byname1 = ArrayAdapter.createFromResource(Tour_Info.this, R.array.service_type, android.R.layout.simple_spinner_item);
                                        adapter_byname1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        type.setAdapter(adapter_byname1);
                                        Button btn_newAddress = update_stop_point.findViewById(R.id.btn_new_add);
                                        btn_newAddress.setVisibility(View.VISIBLE);
                                        btn_newAddress.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Bundle bundle2 = new Bundle();
                                                bundle2.putString("token",token);
                                                bundle2.putInt("type", 1);
                                                Intent intent = new Intent(Tour_Info.this, MapTemp.class);
                                                intent.putExtras(bundle2);
                                                startActivity(intent);
                                            }
                                        });
                                        final EditText newAddress = update_stop_point.findViewById(R.id.addresstext);
                                        newAddress.setVisibility(View.INVISIBLE);
                                        final EditText newProvince = update_stop_point.findViewById(R.id.provincetext);
                                        final EditText newMin = update_stop_point.findViewById(R.id.min_cost);
                                        final EditText newMax = update_stop_point.findViewById(R.id.max_cost);


                                        final EditText mtimeArrive = update_stop_point.findViewById(R.id.timeArrial);

                                        final EditText mtimeLeave = update_stop_point.findViewById(R.id.timeLeave);

                                        final EditText mDateArrive = update_stop_point.findViewById(R.id.dateArrial);

                                        final EditText mDateLeave = update_stop_point.findViewById(R.id.dateLeave);

                                        final ImageButton btnArrive = update_stop_point.findViewById(R.id.btndateArrive);

                                        final ImageButton btnLeave = update_stop_point.findViewById(R.id.btndateLeave);
                                        final Button ok_button = update_stop_point.findViewById(R.id.ok_btt);
                                        ok_button.setText("Update");


                                        btnArrive.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                final Calendar cal = Calendar.getInstance();
                                                int mDay = cal.get(Calendar.DAY_OF_MONTH);
                                                int mMonth = cal.get(Calendar.MONTH);
                                                int mYear = cal.get(Calendar.YEAR);

                                                DatePickerDialog datePickerDialog = new DatePickerDialog(Tour_Info.this, new DatePickerDialog.OnDateSetListener() {
                                                    @Override
                                                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                                        mDateArrive.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                                    }

                                                    ;
                                                }, mYear, mMonth, mDay);
                                                datePickerDialog.setTitle(R.string.choose);
                                                datePickerDialog.show();
                                            }
                                        });

                                        btnLeave.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                final Calendar cal = Calendar.getInstance();
                                                int mDay = cal.get(Calendar.DAY_OF_MONTH);
                                                int mMonth = cal.get(Calendar.MONTH);
                                                int mYear = cal.get(Calendar.YEAR);

                                                DatePickerDialog datePickerDialog = new DatePickerDialog(Tour_Info.this, new DatePickerDialog.OnDateSetListener() {
                                                    @Override
                                                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                                        mDateLeave.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                                    }

                                                    ;
                                                }, mYear, mMonth, mDay);
                                                datePickerDialog.setTitle(R.string.choose);
                                                datePickerDialog.show();
                                            }
                                        });

                                        mtimeArrive.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                final Calendar cal = Calendar.getInstance();

                                                int mHour = cal.get(Calendar.HOUR_OF_DAY);
                                                int mMinute = cal.get(Calendar.MINUTE);

                                                TimePickerDialog timePickerDialog = new TimePickerDialog(Tour_Info.this, new TimePickerDialog.OnTimeSetListener() {
                                                    @Override
                                                    public void onTimeSet(TimePicker timePicker, int hourOfday, int minute) {
                                                        mtimeArrive.setText(hourOfday + ":" + minute);
                                                    }

                                                    ;
                                                }, mHour, mMinute, false);
                                                timePickerDialog.setTitle(R.string.choose);
                                                timePickerDialog.show();
                                            }
                                        });


                                        mtimeLeave.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                final Calendar cal = Calendar.getInstance();
                                                int mHour = cal.get(Calendar.HOUR_OF_DAY);
                                                int mMinute = cal.get(Calendar.MINUTE);

                                                TimePickerDialog timePickerDialog = new TimePickerDialog(Tour_Info.this, new TimePickerDialog.OnTimeSetListener() {
                                                    @Override
                                                    public void onTimeSet(TimePicker timePicker, int hourOfday, int minute) {
                                                        mtimeLeave.setText(hourOfday + ":" + minute);
                                                    }

                                                    ;
                                                }, mHour, mMinute, false);
                                                timePickerDialog.setTitle(R.string.chooseTime);
                                                timePickerDialog.show();
                                            }
                                        });
                                        update_stop_point.show();

                                        ok_button.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String mDiemXuatPhat = newName.getText().toString();
                                                String mAddress = newAddress.getText().toString();

                                                int mType;
                                                if (type.getSelectedItem().toString().equals("Restaurant")) {
                                                    mType = 1;
                                                } else if (type.getSelectedItem().toString().equals("Hotel")) {
                                                    mType = 2;
                                                } else if (type.getSelectedItem().toString().equals("Rest Station")) {
                                                    mType = 3;
                                                } else {
                                                    mType = 4;
                                                }
                                                int mProvince = Integer.parseInt(newProvince.getText().toString());
                                                final int mMin_Cots = Integer.parseInt(newMin.getText().toString());
                                                final int mMax_Cost = Integer.parseInt(newMax.getText().toString());

                                                Date arriveDate, leaveDate;
                                                long mAr_Date = 0;
                                                long mLe_Date = 0;
                                                //xu li covert to milisecond
                                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                                                try {
                                                    arriveDate = sdf.parse(mDateArrive.getText().toString() + " " + mtimeArrive.getText().toString());
                                                    leaveDate = sdf.parse(mDateLeave.getText().toString() + " " + mtimeLeave.getText().toString());
                                                    mAr_Date = arriveDate.getTime();
                                                    mLe_Date = leaveDate.getTime();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                                Stop_Point stop_point = new Stop_Point(stopPointResult_tourInfo.get(position).getId(), mDiemXuatPhat, mNewAddress, mProvince, mNewLat, mNewLong, mAr_Date, mLe_Date, mType, mMin_Cots, mMax_Cost);
                                                ArrayList<Stop_Point> aa = new ArrayList<>();
                                                aa.add(stop_point);
                                                Add_Stop_Point_Data add_stop_point_data = new Add_Stop_Point_Data(Integer.parseInt(id_tour), aa);
                                                Map<String, String> map = new HashMap<>();
                                                map.put("Authorization", token);
                                                Call<Add_Stop_Point_Result> call = jsonPlaceHolderApi.addStopPoint(map, add_stop_point_data);
                                                call.enqueue(new Callback<Add_Stop_Point_Result>() {
                                                    @Override
                                                    public void onResponse(Call<Add_Stop_Point_Result> call, Response<Add_Stop_Point_Result> response) {
                                                        if (!response.isSuccessful()) {
                                                            Toast.makeText(Tour_Info.this, "Ko Thanh cong" + response.code() + response.body(), Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(Tour_Info.this, "Thanh cong" + mMin_Cots + " " + mMax_Cost, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Add_Stop_Point_Result> call, Throwable t) {
                                                        Toast.makeText(Tour_Info.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                Log.d("stop", stopPointResult_tourInfo.get(position).getId() + " " + id_tour);
                                            }
                                        });

                                    }
                                });
                                stop_point_delete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Map<String, String> map = new HashMap<>();
                                        map.put("Authorization", token);
                                        Call<RemoveStopPointResult> call_3 = jsonPlaceHolderApi.removeStopPoint(map, stopPointResult_tourInfo.get(position).getId() + "");
                                        call_3.enqueue(new Callback<RemoveStopPointResult>() {
                                            @Override
                                            public void onResponse(Call<RemoveStopPointResult> call, Response<RemoveStopPointResult> response) {
                                                if (!response.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Xoa diem dung khong thanh cong", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Xoa diem dung thanh cong", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<RemoveStopPointResult> call, Throwable t) {
                                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                return true;
                            }
                        });
                    } else {
                        listView_stop_point.setVisibility(View.GONE);
                        TextView stop_point_placeholder = findViewById(R.id.stop_point_placehoder);
                        stop_point_placeholder.setVisibility(View.VISIBLE);
                    }

                    //Xu ly commnt





                    Call<GetReview_Result> call1_3=jsonPlaceHolderApi.get_review(map,Integer.parseInt(id_tour),1,"100");

                    call1_3.enqueue(new Callback<GetReview_Result>() {
                        @Override
                        public void onResponse(Call<GetReview_Result> call, Response<GetReview_Result> response) {
                            if (!response.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Lay review ko thanh",Toast.LENGTH_SHORT).show();
                            }
                            else {

                               ArrayList<Review> arrayReview=response.body().getReviews();
                                if (arrayReview.size() != 0) {
                                    listView_comment.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                    listView_comment.setAdapter(new CustomAdapterForTourInfo_Comment(arrayReview, onClickRecycleView));

                                } else {
                                    TextView comment_placeholder = findViewById(R.id.comment_placehoder);
                                    comment_placeholder.setVisibility(View.VISIBLE);
                                    listView_comment.setVisibility(View.GONE);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetReview_Result> call, Throwable t) {

                        }
                    });



                    //Xu ly member
                    if (arrayMember.size() != 0) {
                        listView_member.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        listView_member.setAdapter(new CustomAdapterForTourInfo_Member(arrayMember));
                    } else {
                        listView_member.setVisibility(View.GONE);
                        TextView membeer_placeholder = findViewById(R.id.member_placeholder);
                        membeer_placeholder.setVisibility(View.VISIBLE);
                    }


                    //Xu ly Rating

                    Call<Rating_result> call1_2 = jsonPlaceHolderApi.get_rating_list(map, Integer.parseInt(id_tour), 1, "100");
                    call1_2.enqueue(new Callback<Rating_result>() {
                        @Override
                        public void onResponse(Call<Rating_result> call, Response<Rating_result> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Khong thanh cong", Toast.LENGTH_SHORT).show();
                            } else {
                                arrayList_feedback = response.body().getReviewList();
                                listview_rating.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                                listview_rating.setAdapter(new CustomAdapterForTourInfo_Rating(arrayList_feedback, onCLickRecycleView_rating));
                            }
                        }

                        @Override
                        public void onFailure(Call<Rating_result> call, Throwable t) {

                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<TourInforResult> call, Throwable t) {

            }
        });

        /*add_comment_of_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_comment_popup.setContentView(R.layout.send_comment_popup);
                final EditText comment = send_comment_popup.findViewById(R.id.comment_edit_text);
                Button send_comment = send_comment_popup.findViewById(R.id.send_comment_btn);
                ImageView exit = send_comment_popup.findViewById(R.id.exit_comment);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        send_comment_popup.dismiss();
                    }
                });
                send_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String comment_string = comment.getText().toString();
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", token);
                        SendReview_Data sendReview_data = new SendReview_Data(Integer.parseInt(id_tour), 4, comment_string);
                        Call<InviteMember_Result> call_1 = jsonPlaceHolderApi.send_review(map, sendReview_data);
                        call_1.enqueue(new Callback<InviteMember_Result>() {
                            @Override
                            public void onResponse(Call<InviteMember_Result> call, Response<InviteMember_Result> response) {
                                if (!response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Gui comment khong thanh cong", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Gui comment thanh cong", Toast.LENGTH_SHORT).show();
                                    send_comment_popup.dismiss();
                                    finish();
                                    startActivity(getIntent());
                                }
                            }

                            @Override
                            public void onFailure(Call<InviteMember_Result> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                send_comment_popup.show();
            }
        });*/

        add_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_rating_popup.setContentView(R.layout.add_rating_popup);
                final EditText rating_text = send_rating_popup.findViewById(R.id.review_edit_rating);
                Button send_rating = send_rating_popup.findViewById(R.id.send_rating);
                ImageView exit = send_rating_popup.findViewById(R.id.exit_rating);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        send_rating_popup.dismiss();
                    }
                });
                final RatingBar ratingBar = send_rating_popup.findViewById(R.id.rating_point);
                send_rating_popup.show();
                send_rating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String review_string = rating_text.getText().toString();
                        int point = (int) ratingBar.getRating();
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", token);
                        SendRatingData sendRatingData = new SendRatingData(Integer.parseInt(id_tour), point, review_string);
                        Call<SendRatingResult> call1 = jsonPlaceHolderApi.sendRating(map, sendRatingData);
                        call1.enqueue(new Callback<SendRatingResult>() {
                            @Override
                            public void onResponse(Call<SendRatingResult> call, Response<SendRatingResult> response) {
                                if (!response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Gui danh gia khong thanh cong", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Gui danh gia thanh cong", Toast.LENGTH_SHORT).show();
                                    send_rating_popup.dismiss();
                                    finish();
                                    startActivity(getIntent());
                                }
                            }

                            @Override
                            public void onFailure(Call<SendRatingResult> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                search_user.setContentView(R.layout.search_user_popup);


                final EditText edit_search = search_user.findViewById(R.id.search_user_edit_text);
                ImageView search = search_user.findViewById(R.id.search_user_button);
                final ListView listView = search_user.findViewById(R.id.list_view_search_user);
                ImageView exit = search_user.findViewById(R.id.exit_search_user);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search_user.dismiss();
                    }
                });
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Call<SearchUserByKeyword_Result> call_5 = jsonPlaceHolderApi.search_user(edit_search.getText().toString(), 1, "20");
                        call_5.enqueue(new Callback<SearchUserByKeyword_Result>() {
                            @Override
                            public void onResponse(Call<SearchUserByKeyword_Result> call, Response<SearchUserByKeyword_Result> response) {
                                if (!response.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Khong thanh cong", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
                                    final ArrayList<User> arrayList_user = response.body().getUsers();
                                    if (arrayList_user.size() != 0) {
                                        customAdapterUserSearch = new CustomAdapterUserSearch(Tour_Info.this, R.layout.custom_item_for_search_user_result, arrayList_user);
                                        listView.setAdapter(customAdapterUserSearch);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                Map<String, String> map = new HashMap<>();
                                                map.put("Authorization", token);
                                                InviteData inviteMemberData = new InviteData(id_tour, arrayList_user.get(position).getId() + "", true);
                                                Call<InviteResult> call_4 = jsonPlaceHolderApi.inviteMember(map, inviteMemberData);
                                                call_4.enqueue(new Callback<InviteResult>() {
                                                    @Override
                                                    public void onResponse(Call<InviteResult> call, final Response<InviteResult> response) {
                                                        if (!response.isSuccessful()) {
                                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast toast = Toast.makeText(getApplicationContext(), "k thanh" + response.code(), Toast.LENGTH_SHORT);
                                                                    toast.show();
                                                                }
                                                            });

                                                        } else {
                                                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast toast = Toast.makeText(getApplicationContext(), "thanh cong", Toast.LENGTH_SHORT);
                                                                    toast.show();
                                                                }
                                                            });

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<InviteResult> call, final Throwable t) {
                                                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast toast = Toast.makeText(getApplicationContext(), "Something", Toast.LENGTH_SHORT);
                                                                toast.show();
                                                            }
                                                        });

                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Chuoi rong", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<SearchUserByKeyword_Result> call, Throwable t) {

                            }
                        });
                    }
                });

                search_user.show();


            }
        });


        add_stop_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Click",Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("token", token);
                bundle.putInt("ID", Integer.parseInt(id_tour));
                Intent intent = new Intent(Tour_Info.this, MapTemp.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

       /* isPrivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", token);

                final GetSuggestStoppoint_Data getSuggestStoppoint_data=new GetSuggestStoppoint_Data(false,coordLists);
                Call<GetSugestStopPoint_Result> call1_44=jsonPlaceHolderApi.get_suggets_stop_point(map,getSuggestStoppoint_data);

                call1_44.enqueue(new Callback<GetSugestStopPoint_Result>() {
                    @Override
                    public void onResponse(Call<GetSugestStopPoint_Result> call, Response<GetSugestStopPoint_Result> response) {
                        if (!response.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Ko thanh cong"+response.code(),Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"thanh cong"+response.body().getStopPoints().get(1).getName(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetSugestStopPoint_Result> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });*/
    }


    private void mapping() {
        name_tour_info = findViewById(R.id.name_tour_info);
        date_tour_info = findViewById(R.id.date_tour_info);
        cost_tour_info = findViewById(R.id.cost_tour_info);
        adult_tour_info = findViewById(R.id.adult_tour_info);
        listView_stop_point = findViewById(R.id.list_stop_point_tour_info1);
        listView_comment = findViewById(R.id.list_comment_tour_info);
        listView_member = findViewById(R.id.list_member_tour_info);
        listview_rating = findViewById(R.id.list_rating_tour_info);
        stop_point_info = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        update_stop_point = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        add_comment_of_user = findViewById(R.id.add_comment_of_user);
        add_rating = findViewById(R.id.add_rating);
        send_comment_popup = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Objects.requireNonNull(send_comment_popup.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        send_rating_popup = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Objects.requireNonNull(send_rating_popup.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        search_user = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Objects.requireNonNull(search_user.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        add_feedback = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Objects.requireNonNull(add_feedback.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        add_member = findViewById(R.id.add_member);
        add_stop_point=findViewById(R.id.add_stop_point_of_user);
        status = findViewById(R.id.status);
        isPrivate = findViewById(R.id.is_private_image);
        back = findViewById(R.id.back_to_main);

        follow_tour=findViewById(R.id.follow_tour);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            assert uri != null;
            File file = new File(uri.getPath());
            Toast.makeText(Tour_Info.this, file + "", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        EditText address_temp = update_stop_point.findViewById(R.id.addresstext);
        Button btn_add_address = update_stop_point.findViewById(R.id.btn_new_add);
        btn_add_address.setVisibility(View.GONE);
        address_temp.setVisibility(View.VISIBLE);

        EditText province_temp = update_stop_point.findViewById(R.id.provincetext);
        Bundle bundle = intent.getExtras();
        mNewLat = bundle.getDouble("mNewLat");
        mNewLong = bundle.getDouble("mNewLong");
        mNewAddress = bundle.getString("mNewAddress");
        address_temp.setText(mNewAddress);
        for (int i = 0; i < arrayProvince.length; i++) {
            if (mNewAddress.contains(arrayProvince[i])) {
                province_temp.setText(i + 1 + "");
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
