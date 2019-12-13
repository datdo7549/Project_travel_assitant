package com.ygaps.travelapp;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ygaps.travelapp.Model.UpdatePasswordData;
import com.ygaps.travelapp.Model.UpdatePasswordResult;
import com.ygaps.travelapp.Model.UpdateUserInfoData;
import com.ygaps.travelapp.Model.User_Info_Result;
import com.ygaps.travelapp.Model.VerifyOTP_Result;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ygaps.travelapp.LoginActivity.URL;


public class Setting_Fragment extends Fragment {

    private Button log_out;
    private ImageView imageView;
    private TextView nameUser, emailUser, phoneUser, addressUser, dobUser, genderUser, emailVer, phoneVer;
    private String token;
    private int user_id;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private View view;
    private Dialog upadte_user_info_dialog;
    private Dialog upadte_user_password_dialog;
    private String temp_date;
    private Date date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        final MainActivity activity = (MainActivity) getActivity();
        token = activity.getMyData();
        user_id=Integer.parseInt(activity.getUser_id());
        mapping();
        getUserInfo();

        /*log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                Intent intent=new Intent(getContext(),LoginActivity.class);
                startActivity(intent);
            }
        });*/
        Picasso.get()
                .load("https://images.unsplash.com/photo-1564859227770-c3ffd1fb1deb?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=634&q=80")
                .transform(new CropCircleTransformation())
                .into(imageView);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_setting, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.log_out: {
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);


                break;
            }
            case R.id.update_info: {
                upadte_user_info_dialog.setContentView(R.layout.upate_user_info_popup);


                final EditText fullName = upadte_user_info_dialog.findViewById(R.id.full_name_update);
                final EditText email = upadte_user_info_dialog.findViewById(R.id.email_update);
                final EditText phone = upadte_user_info_dialog.findViewById(R.id.phone_upđate);
                final RadioGroup gender = upadte_user_info_dialog.findViewById(R.id.gender_update);
                final RadioButton genderMale = upadte_user_info_dialog.findViewById(R.id.radioButton_male_update);
                final RadioButton genderFemale = upadte_user_info_dialog.findViewById(R.id.radioButton_female_update);
                ImageButton dob = upadte_user_info_dialog.findViewById(R.id.dob_update);
                Button btn_upadte = upadte_user_info_dialog.findViewById(R.id.btn_update_info);
                ImageView exit = upadte_user_info_dialog.findViewById(R.id.exit_update_info);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upadte_user_info_dialog.dismiss();
                    }
                });

                dob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar cal = Calendar.getInstance();
                        int mDay = cal.get(Calendar.DAY_OF_MONTH);
                        int mMonth = cal.get(Calendar.MONTH);
                        int mYear = cal.get(Calendar.YEAR);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                temp_date = (dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }


                        }, mYear, mMonth, mDay);
                        datePickerDialog.setTitle(R.string.choose);
                        datePickerDialog.show();
                    }
                });

                btn_upadte.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String mFullName = fullName.getText().toString();
                        final String mEmail = email.getText().toString();
                        final String mPhone = phone.getText().toString();
                        int mGender;
                        if (genderMale.isChecked()) {
                            mGender = 1;
                        } else {
                            mGender = 0;
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            date = sdf.parse(temp_date);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", token);
                        UpdateUserInfoData updateUserInfoData = new UpdateUserInfoData(mFullName, mEmail, mPhone, mGender, date);
                        Call<VerifyOTP_Result> call = jsonPlaceHolderApi.updateUserInfo(map, updateUserInfoData);
                        call.enqueue(new Callback<VerifyOTP_Result>() {
                            @Override
                            public void onResponse(Call<VerifyOTP_Result> call, Response<VerifyOTP_Result> response) {
                                if (!response.isSuccessful()) {
                                    Toast.makeText(getContext(), "Cap nhat thong tin khong thanh cong", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Cap nhat thong tin thanh cong", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<VerifyOTP_Result> call, Throwable t) {

                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                upadte_user_info_dialog.show();


                break;
            }
            case R.id.update_password: {
                upadte_user_password_dialog.setContentView(R.layout.update_password_popup);
                Button update_password=upadte_user_password_dialog.findViewById(R.id.btn_update_password);
                ImageView exit=upadte_user_password_dialog.findViewById(R.id.exit_update_password);
                final EditText curr_password=upadte_user_password_dialog.findViewById(R.id.current_password);
                final EditText new_password=upadte_user_password_dialog.findViewById(R.id.new_password);
                exit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upadte_user_password_dialog.dismiss();
                    }
                });
                update_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> map = new HashMap<>();
                        map.put("Authorization", token);
                        String cur_pass=curr_password.getText().toString();
                        String new_pass=new_password.getText().toString();
                        UpdatePasswordData updatePasswordData=new UpdatePasswordData(user_id,cur_pass,new_pass);

                        Call<UpdatePasswordResult> call=jsonPlaceHolderApi.updatePassword(map,updatePasswordData);
                        call.enqueue(new Callback<UpdatePasswordResult>() {
                            @Override
                            public void onResponse(Call<UpdatePasswordResult> call, Response<UpdatePasswordResult> response) {
                                if (!response.isSuccessful())
                                {
                                    if (response.code()==400)
                                    {
                                        Toast.makeText(getContext(),"Current password is wrong",Toast.LENGTH_SHORT).show();
                                    }
                                    else if (response.code()==404)
                                    {
                                        Toast.makeText(getContext(),"EMAIL/PHONE doesn't exist",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(),"Loi",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"Cap nhap mat khau thanh cong",Toast.LENGTH_SHORT).show();

                                }
                            }
                            @Override
                            public void onFailure(Call<UpdatePasswordResult> call, Throwable t) {
                                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                upadte_user_password_dialog.show();
                break;
            }
        }
        return true;
    }

    private void getUserInfo() {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", token);
        Call<User_Info_Result> call = jsonPlaceHolderApi.getUserInfo(map);
        call.enqueue(new Callback<User_Info_Result>() {
            @Override
            public void onResponse(Call<User_Info_Result> call, Response<User_Info_Result> response) {
                if (!response.isSuccessful()) {

                } else {
                    User_Info_Result user_info_result = response.body();
                    nameUser.setText(user_info_result.getFullName());
                    emailUser.setText((user_info_result.getEmail().equals("")) ? "Chưa cập nhật" : user_info_result.getEmail());

                    phoneUser.setText((user_info_result.getPhone().equals("")) ? "Chưa cập nhật" : user_info_result.getPhone());


                    addressUser.setText((user_info_result.getAddress() == null) ? "Chưa cập nhật" : user_info_result.getAddress());
                    dobUser.setText((user_info_result.getDob() == null) ? "Chưa cập nhật" : user_info_result.getDob().substring(0, 10));

                    if (user_info_result.getGender() == 0) {
                        genderUser.setText("Male");
                    } else {
                        genderUser.setText("Female");
                    }

                    /*if (user_info_result.getEmail_verified() == true) {
                        emailVer.setText("Yes");
                    } else {
                        emailVer.setText("No");
                    }

                    if (user_info_result.getPhone_verified() == true) {
                        phoneVer.setText("Yes");
                    } else {
                        phoneVer.setText("No");
                    }*/
                }
            }

            @Override
            public void onFailure(Call<User_Info_Result> call, Throwable t) {

            }
        });
    }

    private void mapping() {
        //log_out=view.findViewById(R.id.btn_log_out);
        imageView = view.findViewById(R.id.avatar_user);
        nameUser = view.findViewById(R.id.name_user);
        emailUser = view.findViewById(R.id.email_user);
        phoneUser = view.findViewById(R.id.phone_user);
        addressUser = view.findViewById(R.id.address_user);
        dobUser = view.findViewById(R.id.dob_user);
        genderUser = view.findViewById(R.id.gender_user);
        emailVer = view.findViewById(R.id.email_verified);
        phoneVer = view.findViewById(R.id.phone_verified);
        Gson gson = new GsonBuilder().serializeNulls().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        upadte_user_info_dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        upadte_user_info_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        upadte_user_password_dialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        upadte_user_password_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }


}
