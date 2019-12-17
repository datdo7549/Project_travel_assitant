package com.ygaps.travelapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import com.ygaps.travelapp.Adapter.CustomAdapterForTourInfo_Comment;
import com.ygaps.travelapp.Adapter.CustomAdapterForTourInfo_Member;
import com.ygaps.travelapp.Adapter.CustomAdapterForTourInfo_StopPoint;
import com.ygaps.travelapp.Model.Add_Stop_Point_Data;
import com.ygaps.travelapp.Model.Add_Stop_Point_Result;
import com.ygaps.travelapp.Model.CommentResult_TourInfo;
import com.ygaps.travelapp.Model.InviteData;
import com.ygaps.travelapp.Model.InviteMemberData;
import com.ygaps.travelapp.Model.InviteMember_Result;
import com.ygaps.travelapp.Model.InviteResult;
import com.ygaps.travelapp.Model.Member;
import com.ygaps.travelapp.Model.RemoveStopPointResult;
import com.ygaps.travelapp.Model.SendCommentData;
import com.ygaps.travelapp.Model.SendCommentResult;
import com.ygaps.travelapp.Model.SendRatingData;
import com.ygaps.travelapp.Model.SendRatingResult;
import com.ygaps.travelapp.Model.StopPointResult_TourInfo;
import com.ygaps.travelapp.Model.Stop_Point;
import com.ygaps.travelapp.Model.TourInforResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ygaps.travelapp.LoginActivity.URL;
import static com.ygaps.travelapp.MapActivity.arrayProvince;

public class Tour_Info extends AppCompatActivity {
    private ImageView upload_image_tour_info;
    private TextView name_tour_info;
    private TextView date_tour_info;
    private TextView cost_tour_info;
    private TextView adult_tour_info;
    private TextView baby_tour_info;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private String id_tour;
    private String token;
    private String user_id;
    private ImageView imagee_tour_info;
    private ListView listView_stop_point;
    private ListView listView_comment;
    private ListView listView_member;
    private CustomAdapterForTourInfo_StopPoint customAdapterForTourInfoStopPoint;
    private CustomAdapterForTourInfo_Comment customAdapterForTourInfo_comment;
    private CustomAdapterForTourInfo_Member customAdapterForTourInfo_member;
    private ArrayList<String> arrayList = new ArrayList<>();
    private Dialog stop_point_info;
    private Dialog update_stop_point;
    private Dialog send_comment_popup;
    private Dialog send_rating_popup;
    private ImageView add_comment_of_user;
    private ImageView add_rating;
    private ImageView add_member;

    private double mNewLat;
    private double mNewLong;
    private String mNewAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window w = getWindow();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_tour__info);
        mapping();


        upload_image_tour_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 2);
            }
        });
        imagee_tour_info = findViewById(R.id.image_tour_info);
        Picasso.get()
                .load("https://cdn.pixabay.com/photo/2016/03/04/19/36/beach-1236581_960_720.jpg")
                .fit()
                .into(imagee_tour_info);
        final Bundle bundle = getIntent().getExtras();
        id_tour = bundle.getString("tour_id");
        token = bundle.getString("token");
        user_id = bundle.getString("user_id_string");


        Log.d("id", id_tour);

        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Map<String, String> map = new HashMap<>();
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
                    ArrayList<CommentResult_TourInfo> arrayComment = response.body().getComments();

                    //Lay duoc danh sach Member cua cai tour do:
                    ArrayList<Member> arrayMember = response.body().getMembers();
                    name_tour_info.setText(response.body().getName());

                    long miliStartDate = Long.parseLong(response.body().getStartDate());
                    final Date startD = new Date(miliStartDate);
                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String temp1 = dateFormat.format(startD);
                    StringBuilder dateBuild = new StringBuilder();

                    //Xu ly date
                    long miliEndDate = Long.parseLong(response.body().getEndDate());
                    Date endD = new Date(miliEndDate);
                    DateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
                    String temp2 = dateFormat1.format(endD);
                    dateBuild.append(temp1).append(" -> ").append(temp2);
                    date_tour_info.setText(dateBuild);

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
                    adult_tour_info.setText(response.body().getAdults() + "");
                    baby_tour_info.setText(response.body().getChilds() + "");


                    //Xu li Stop Point
                    if (stopPointResult_tourInfo.size() != 0) {
                        customAdapterForTourInfoStopPoint = new CustomAdapterForTourInfo_StopPoint(Tour_Info.this, R.layout.list_stop_point_tour_info, stopPointResult_tourInfo);
                        listView_stop_point.setAdapter(customAdapterForTourInfoStopPoint);

                        listView_stop_point.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                                        bundle.putInt("type", 1);
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
                                                bundle2.putInt("type", 1);
                                                Intent intent = new Intent(Tour_Info.this, MapActivity.class);
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
                                        Call<RemoveStopPointResult> call_3=jsonPlaceHolderApi.removeStopPoint(map,stopPointResult_tourInfo.get(position).getId()+"");
                                        call_3.enqueue(new Callback<RemoveStopPointResult>() {
                                            @Override
                                            public void onResponse(Call<RemoveStopPointResult> call, Response<RemoveStopPointResult> response) {
                                                if (!response.isSuccessful())
                                                {
                                                    Toast.makeText(getApplicationContext(),"Xoa diem dung khong thanh cong",Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(),"Xoa diem dung thanh cong",Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<RemoveStopPointResult> call, Throwable t) {
                                                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                                return true;
                            }
                        });


                    } else {
                        listView_stop_point.setVisibility(View.GONE);
                        TextView stop_point_placeholder = findViewById(R.id.stop_point_placeholder);
                        stop_point_placeholder.setVisibility(View.VISIBLE);
                    }

                    //Xu ly commnt
                    if (arrayComment.size() != 0) {
                        customAdapterForTourInfo_comment = new CustomAdapterForTourInfo_Comment(Tour_Info.this, R.layout.list_comment_tour_info, arrayComment);
                        listView_comment.setAdapter(customAdapterForTourInfo_comment);
                    } else {
                        listView_comment.setVisibility(View.GONE);
                        TextView comment_placeholder = findViewById(R.id.comment_placeholder);
                        comment_placeholder.setVisibility(View.VISIBLE);
                    }

                    //Xu ly member
                    if (arrayMember.size() != 0) {
                        customAdapterForTourInfo_member = new CustomAdapterForTourInfo_Member(Tour_Info.this, R.layout.list_member_tour_info, arrayMember);
                        listView_member.setClipToOutline(true);
                        listView_member.setAdapter(customAdapterForTourInfo_member);
                    } else {
                        listView_member.setVisibility(View.GONE);
                        TextView membeer_placeholder = findViewById(R.id.member_placeholder);
                        membeer_placeholder.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onFailure(Call<TourInforResult> call, Throwable t) {

            }
        });

        add_comment_of_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_comment_popup.setContentView(R.layout.send_comment_popup);
                final EditText comment = send_comment_popup.findViewById(R.id.comment_edit_text);
                Button send_comment = send_comment_popup.findViewById(R.id.send_comment_btn);
                ImageView exit=send_comment_popup.findViewById(R.id.exit_comment);
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
                        SendCommentData sendCommentData = new SendCommentData(id_tour, user_id, comment_string);
                        Call<SendCommentResult> call_1 = jsonPlaceHolderApi.sendComment(map, sendCommentData);
                        call_1.enqueue(new Callback<SendCommentResult>() {
                            @Override
                            public void onResponse(Call<SendCommentResult> call, Response<SendCommentResult> response) {
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
                            public void onFailure(Call<SendCommentResult> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                send_comment_popup.show();
            }
        });

        add_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_rating_popup.setContentView(R.layout.add_rating_popup);
                final EditText rating_text=send_rating_popup.findViewById(R.id.review_edit_rating);
                Button send_rating=send_rating_popup.findViewById(R.id.send_rating);
                ImageView exit=send_rating_popup.findViewById(R.id.exit_rating);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        send_rating_popup.dismiss();
                    }
                });
                final RatingBar ratingBar=send_rating_popup.findViewById(R.id.rating_point);
                send_rating_popup.show();
                send_rating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String review_string=rating_text.getText().toString();
                        int point=(int) ratingBar.getRating();
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", token);
                        SendRatingData sendRatingData=new SendRatingData(Integer.parseInt(id_tour),point,review_string);
                        Call<SendRatingResult> call1=jsonPlaceHolderApi.sendRating(map,sendRatingData);
                        call1.enqueue(new Callback<SendRatingResult>() {
                            @Override
                            public void onResponse(Call<SendRatingResult> call, Response<SendRatingResult> response) {
                                if (!response.isSuccessful())
                                {
                                    Toast.makeText(getApplicationContext(),"Gui danh gia khong thanh cong",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Gui danh gia thanh cong",Toast.LENGTH_SHORT).show();
                                    send_rating_popup.dismiss();
                                    finish();
                                    startActivity(getIntent());
                                }
                            }

                            @Override
                            public void onFailure(Call<SendRatingResult> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });
        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", token);
                InviteData inviteMemberData=new InviteData(id_tour,"641",true);
                Call<InviteResult> call_4=jsonPlaceHolderApi.inviteMember(map,inviteMemberData);
                call_4.enqueue(new Callback<InviteResult>() {
                    @Override
                    public void onResponse(Call<InviteResult> call, Response<InviteResult> response) {
                        if (!response.isSuccessful())
                        {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(getApplicationContext(), "k thanh", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });

                        }
                        else {
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
    }


    private void mapping() {
        name_tour_info = findViewById(R.id.name_tour_info);
        date_tour_info = findViewById(R.id.date_tour_info);
        cost_tour_info = findViewById(R.id.cost_tour_info);
        adult_tour_info = findViewById(R.id.adult_tour_info);
        baby_tour_info = findViewById(R.id.baby_tour_info);
        upload_image_tour_info = findViewById(R.id.upload_image_tour_info);
        listView_stop_point = findViewById(R.id.list_stop_point_tour_info1);
        listView_comment = findViewById(R.id.list_comment_tour_info);
        listView_member = findViewById(R.id.list_member_tour_info);
        stop_point_info = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        update_stop_point = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        add_comment_of_user = findViewById(R.id.add_comment_of_user);
        add_rating=findViewById(R.id.add_rating);
        send_comment_popup = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Objects.requireNonNull(send_comment_popup.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        send_rating_popup = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Objects.requireNonNull(send_rating_popup.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        add_member=findViewById(R.id.add_member);
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


}
