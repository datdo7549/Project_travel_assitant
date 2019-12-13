package com.ygaps.travelapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ygaps.travelapp.Model.DataGoogleLogin;
import com.ygaps.travelapp.Model.FacebookLoginResult;
import com.ygaps.travelapp.Model.Fb_data_login;
import com.ygaps.travelapp.Model.GGreesult;
import com.ygaps.travelapp.Model.GgData;
import com.ygaps.travelapp.Model.GoogleLoginResult;
import com.ygaps.travelapp.Model.LoginResult;
import com.ygaps.travelapp.Model.User_login;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String URL="http://35.197.153.192:3000/";
    public static final String URL2="https://oauth2.googleapis.com/";
    private static final int RC_GET_AUTH_CODE = 9003;
    private EditText emailPhone;
    private EditText password;
    private Button login;
    private TextView forgot_pass;
    private TextView register;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private JsonPlaceHolderAPI2 jsonPlaceHolderAPI2;
    private ProgressDialog progressDialog;
    private GoogleApiClient mGoogleApiClient;
    CallbackManager callbackManager;
    LoginButton loginButton;
    Button googleLogin;
    String email,name,first_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);
        mapping();
        keyhash();
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        googleLogin.setOnClickListener(this);
        forgot_pass.setOnClickListener(this);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        setLogin_Button();
        Button fb = (Button) findViewById(R.id.fb);
        
    }

    public void onClickFacebookButton(View view) {
        if (view == findViewById(R.id.fb)) {
            loginButton.performClick();
        }
    }
    private void setLogin_Button() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<com.facebook.login.LoginResult>() {
            @Override
            public void onSuccess(com.facebook.login.LoginResult loginResult) {
                result();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void result() {
        GraphRequest graphRequest=GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    email=object.getString("email");
                    name=object.getString("name");
                    first_name=object.getString("first_name");
                    final String token=AccessToken.getCurrentAccessToken().getToken();
                    progressDialog=new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Login Status");
                    progressDialog.setMessage("Logging...");
                    Fb_data_login login=new Fb_data_login(token);
                    Call<FacebookLoginResult> call=jsonPlaceHolderApi.facebookLogin(login);
                    call.enqueue(new Callback<FacebookLoginResult>() {
                        @Override
                        public void onResponse(Call<FacebookLoginResult> call, Response<FacebookLoginResult> response) {
                            if(!response.isSuccessful())
                            {
                                if(response.code()==400)
                                Toast.makeText(LoginActivity.this,"Login by facebook failed",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Bundle bundle=new Bundle();
                                bundle.putString("token",response.body().getToken());
                                bundle.putInt("user_id",response.body().getUserId());
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<FacebookLoginResult> call, Throwable t) {
                            Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.d("TOKEN",token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email,first_name");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }

    private void mapping() {
        emailPhone=findViewById(R.id.edit_emailPhone);
        password=findViewById(R.id.edit_password);
        login=findViewById(R.id.login_button);
        register=findViewById(R.id.register);
        Gson gson=new GsonBuilder().serializeNulls().create();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        Retrofit retrofit2=new Retrofit.Builder()
                .baseUrl(URL2)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderAPI2=retrofit2.create(JsonPlaceHolderAPI2.class);
        jsonPlaceHolderApi=retrofit.create(JsonPlaceHolderApi.class);
        loginButton=(LoginButton) findViewById(R.id.loginfb_button);
        googleLogin=findViewById(R.id.gg);
        forgot_pass=findViewById(R.id.forgot_password);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GET_AUTH_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d("ALO", "onActivityResult:GET_AUTH_CODE:success:" + result.getStatus().isSuccess());

            if (result.isSuccess()) {
                // [START get_auth_code]
                GoogleSignInAccount acct = result.getSignInAccount();
                String authCode = acct.getServerAuthCode();
                GgData temp=new GgData("authorization_code","827444390877-bsuer8sr7nu42ed3pehntbn33s3ob5rh.apps.googleusercontent.com","YTgaVwtEasppbD2y7Nsc_hUA","",acct.getIdToken(),authCode);
                Call<GGreesult> call=jsonPlaceHolderAPI2.getAccess(temp);
                call.enqueue(new Callback<GGreesult>() {
                    @Override
                    public void onResponse(Call<GGreesult> call, Response<GGreesult> response) {
                        if(!response.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this,"Login by Google failed",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Log.d("TOKEN",response.body().getAccess_token());
                            DataGoogleLogin dataGoogleLogin=new DataGoogleLogin(response.body().getAccess_token());
                            Call<GoogleLoginResult> call2=jsonPlaceHolderApi.googleLogin(dataGoogleLogin);
                            call2.enqueue(new Callback<GoogleLoginResult>() {
                                @Override
                                public void onResponse(Call<GoogleLoginResult> call, Response<GoogleLoginResult> response) {
                                    if(!response.isSuccessful())
                                    {
                                        Toast.makeText(LoginActivity.this,"Login by Google failed",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(LoginActivity.this,"Login by Google successfully",Toast.LENGTH_LONG).show();
                                        Bundle bundle=new Bundle();
                                        bundle.putString("token",response.body().getToken());
                                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                }
                                @Override
                                public void onFailure(Call<GoogleLoginResult> call, Throwable t) {
                                    Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<GGreesult> call, Throwable t) {

                    }
                });

            }
        }
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login_button:
            {
                String mEmailPhone=emailPhone.getText().toString();
                String mPassword=password.getText().toString();
                User_login user_login=new User_login(mEmailPhone,mPassword);
                Call<LoginResult> call=jsonPlaceHolderApi.login(user_login);
                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(!response.isSuccessful())
                        {
                            if(response.code()==400)
                            {
                                Toast.makeText(LoginActivity.this,"Missing email/phone or password",Toast.LENGTH_LONG).show();
                            }
                            if(response.code()==404)
                            {
                                Toast.makeText(LoginActivity.this,"Wrong email/phone or password",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this,"Login successfully",Toast.LENGTH_SHORT).show();
                            Bundle bundle=new Bundle();
                            bundle.putString("token",response.body().getToken());
                            bundle.putInt("user_id",response.body().getUserId());
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            }
            case R.id.register:
            {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.gg:
            {
                String serverClientId = getString(R.string.server_client_id);
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                        .requestServerAuthCode(serverClientId)
                        .requestIdToken(serverClientId)
                        .requestEmail()
                        .build();
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_GET_AUTH_CODE);
                break;
            }

            case R.id.forgot_password: {
                Intent intent=new Intent(LoginActivity.this,ForgotActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            emailPhone.setText(bundle.getString("email", ""));
            password.setText(bundle.getString("password", ""));
        }
    }
    public void keyhash()
    {
        try{
            PackageInfo info=getPackageManager().getPackageInfo("com.example.loginandregister", PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures)
            {
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn)
        {
            Fb_data_login login=new Fb_data_login(accessToken.getToken());
            Call<FacebookLoginResult> call=jsonPlaceHolderApi.facebookLogin(login);
            call.enqueue(new Callback<FacebookLoginResult>() {
                @Override
                public void onResponse(Call<FacebookLoginResult> call, Response<FacebookLoginResult> response) {
                        Bundle bundle=new Bundle();
                        bundle.putString("token",response.body().getToken());
                        bundle.putInt("user_id",response.body().getUserId());
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                }
                @Override
                public void onFailure(Call<FacebookLoginResult> call, Throwable t) {
                }
            });
        }
    }

}
