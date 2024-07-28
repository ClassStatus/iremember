package com.socialmedianetworking.iremember.activity;

import static com.socialmedianetworking.iremember.util.Constant.ASSETS_URL;
import static com.socialmedianetworking.iremember.util.Constant.BASE_URL;
import static com.socialmedianetworking.iremember.util.Constant.EDIT_PROFILE;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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
import com.balysv.materialripple.BuildConfig;
import com.bumptech.glide.Glide;
import com.socialmedianetworking.iremember.MainActivity;
import com.socialmedianetworking.iremember.R;
import com.socialmedianetworking.iremember.util.App;
import com.socialmedianetworking.iremember.util.UserSession;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private ImageView backBtn,addImage,singOutBtn;
    private CircleImageView profileImage;
    private EditText userName,dob,gender;
    private EditText userPhone;
    private TextView saveBtn;
    String image1,profileimage;
    private UserSession session;
    private HashMap<String, String> user;
    String user_id,name,email,mobile,image;
    Bitmap bit_1;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private ImageView imageView;
    private String currentPhotoPath;

    private String string1;




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
        name = user.get(UserSession.KEY_FULL_NAME);
        email = user.get(UserSession.KEY_EMAIL);
        mobile = user.get(UserSession.KEY_PHONE_NUMBER);




        backBtn = findViewById(R.id.backBtn);

        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userNameInProfile);
        dob = findViewById(R.id.dateofbirth);
        dob.setText(user.get(UserSession.KEY_DOB));
        gender = findViewById(R.id.gender);
        //Log.d("Gender----",user.get(UserSession.KEY_GENDER));
        gender.setText(user.get(UserSession.KEY_GENDER));
        userPhone = findViewById(R.id.phoneNumberIdInProfile);
        saveBtn = findViewById(R.id.saveProfileInformationButton);
        addImage = findViewById(R.id.addImage);
        singOutBtn = findViewById(R.id.signOut);
        image = user.get(UserSession.KEY_P_IMAGE);
        Glide
                .with(this)
                .load(ASSETS_URL+image)
                .placeholder(R.drawable.profile)
                .into(profileImage);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showImageSourceDialog();



            }
        });

        userName.setText(name);
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
        String phone = userPhone.getText().toString().trim();
        String doteofbirth = dob.getText().toString().trim();
        String genderstring = gender.getText().toString().trim();


//        progressDialog=  KProgressHUD.create(ProfileActivity.this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("Please wait")
//                .setCancellable(false)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//                .show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, EDIT_PROFILE,
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
                                Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();


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
                params.put("phonenumber" ,phone);
                params.put("email",email);
                params.put("fullname",name);
                params.put("gender",genderstring);
                params.put("dob",doteofbirth);
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


    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source")
                .setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        dispatchTakePictureIntent();
                                        break;
                                    case 1:
                                        dispatchChoosePictureIntent();
                                        break;
                                }
                            }
                        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void dispatchChoosePictureIntent() {
        Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(choosePictureIntent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                File file = new File(currentPhotoPath);
                Uri uri = Uri.fromFile(file);
                //startCropActivity(uri);
                profileImage.setImageURI(uri);

            } else if (requestCode == REQUEST_IMAGE_GALLERY && data != null) {
                Uri selectedImageUri = data.getData();
                profileImage.setImageURI(selectedImageUri);
            } else {
                    Toast.makeText(this, "Cropping failed", Toast.LENGTH_LONG).show();
                }
            }
        }


    private File createImageFile() throws IOException {
        // Get current date and time
        Date now = new Date();

        // Format date and time to the desired pattern
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault());
        String timeStamp = dateFormat.format(now);

        // Create the image file name using the timestamp
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Get the directory to save the image
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create a temporary image file
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save the absolute path of the image file
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
