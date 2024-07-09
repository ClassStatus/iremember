package com.socialmedianetworking.iremember.activity;

import static com.socialmedianetworking.iremember.util.Constant.ASSETS_URL;
import static com.socialmedianetworking.iremember.util.Constant.BASE_URL;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.socialmedianetworking.iremember.MainActivity;
import com.socialmedianetworking.iremember.R;
import com.socialmedianetworking.iremember.util.App;
import com.socialmedianetworking.iremember.util.UserSession;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private ImageView backBtn,addImage,singOutBtn;
    private CircleImageView profileImage;
    private EditText userName,userEmail;
    private EditText userPhone;
    private TextView saveBtn;
    String image1,profileimage;
    private UserSession session;
    private HashMap<String, String> user;
    String user_id,name,email,mobile,image;
    Bitmap bit_1;


    private Uri saveUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        initUI();

    }

    private void initUI() {

        session = new UserSession(getApplicationContext());
        session.isLoggedIn();
        user = session.getUserDetails();
        user_id = user.get(UserSession.KEY_USER_ID);
        name = user.get(UserSession.KEY_NAME);
        email = user.get(UserSession.KEY_EMAIL);
        mobile = user.get(UserSession.KEY_MOBiLE);




        backBtn = findViewById(R.id.backBtn);

        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userNameInProfile);
        userEmail = findViewById(R.id.emailIdInProfile);
        userPhone = findViewById(R.id.phoneNumberIdInProfile);
        saveBtn = findViewById(R.id.saveProfileInformationButton);
        addImage = findViewById(R.id.addImage);
        singOutBtn = findViewById(R.id.signOut);
        image = user.get(UserSession.KEY_P_IMAGE);
//        Glide
//                .with(this)
//                .load(ASSETS_URL+image)
//                .placeholder(R.drawable.profile)
//                .into(profileImage);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CropImage.activity(null)
//                        .setGuidelines(CropImageView.Guidelines.ON)
//                        .setCropShape(CropImageView.CropShape.OVAL)
//                        .setAspectRatio(1, 1)
//                        .start(ProfileActivity.this);



            }
        });

        userName.setText(name);
        userEmail.setText(email);
        userPhone.setText(mobile);
        //profileImage.





        backBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });



        singOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //userAuth.signOut();

                session.logoutUser();

            }
        });

        //set the user value


    }

    public void updateUserData(){
       String name = userName.getText().toString().trim();
        String email = userEmail.getText().toString().trim();
        String phone = userPhone.getText().toString().trim();


//        progressDialog=  KProgressHUD.create(ProfileActivity.this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("Please wait")
//                .setCancellable(false)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//                .show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + "save_profile",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("@UPLOAD_DOBULED" + "", response);
                        try {
                            JSONObject jsonObject;
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (status.equals("1"))
                            {
                                Toast.makeText(getApplicationContext(),"Sucess",Toast.LENGTH_LONG).show();


                            }else{
                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                            }


                            //progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("hhh", e.getMessage().toString());
                            //progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hhh", String.valueOf(error));
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(),
                            "Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    Log.d("hhh", String.valueOf(error));
                } else if (error instanceof ServerError) {
                    //TODO
                    Log.d("hhh", String.valueOf(error));
                } else if (error instanceof NetworkError) {
                    //TODO
                    Log.d("hhh", String.valueOf(error));
                } else if (error instanceof ParseError) {
                    //TODO
                    Log.d("hhh", String.valueOf(error));
                }
                //progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("id",user_id);
                params.put("mobile" , phone  );
                params.put("email",email);
                params.put("name",name);
                Log.d("looooo", String.valueOf(params));
                return params;
            }

        };

        App.getInstance().addToRequestQueue(stringRequest);



    }


    private void saveimage(String pancard, String user_id, String image1) {
//        progressDialog=  KProgressHUD.create(ProfileActivity.this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("Please wait")
//                .setCancellable(false)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//                .show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + "upload_image",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("@UPLOAD_DOBULED" + "", response);
                        try {
                            JSONObject jsonObject;
                            jsonObject = new JSONObject(response);
                            if(jsonObject.getBoolean("success"))
                            {
                                Toast.makeText(getApplicationContext(),"Sucess",Toast.LENGTH_LONG).show();


                            }else{
                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                            }


                            //progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("hhh", e.getMessage().toString());
                            //progressDialog.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("hhh", String.valueOf(error));
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(),
                            "Error",
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    //TODO
                    Log.d("hhh", String.valueOf(error));
                } else if (error instanceof ServerError) {
                    //TODO
                    Log.d("hhh", String.valueOf(error));
                } else if (error instanceof NetworkError) {
                    //TODO
                    Log.d("hhh", String.valueOf(error));
                } else if (error instanceof ParseError) {
                    //TODO
                    Log.d("hhh", String.valueOf(error));
                }
                //progressDialog.dismiss();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id);
                params.put("type" , pancard  );
                params.put("image",image1);
                Log.d("looooo", String.valueOf(params));
                return params;
            }

        };

        App.getInstance().addToRequestQueue(stringRequest);
    }

}
