package com.socialmedianetworking.iremember.activity;


import static com.socialmedianetworking.iremember.util.Constant.REGISTER;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.socialmedianetworking.iremember.R;
import com.socialmedianetworking.iremember.util.App;
import com.socialmedianetworking.iremember.util.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText userName, emailId, password, phoneNumber;
    private TextView signUpBtn, signInText;
    private CardView googleCard, facebookCard;

    private UserSession session;
    private HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        initUI();
    }




    private void initUI() {

        userName = findViewById(R.id.userName);
        emailId = findViewById(R.id.emailId);
        password = findViewById(R.id.password);
        phoneNumber = findViewById(R.id.phoneNumber);
        signUpBtn = findViewById(R.id.signUpForNow);
        signInText = findViewById(R.id.singInText);
        //googleCard = findViewById(R.id.googleLogInCard);
        //facebookCard = findViewById(R.id.facebookLogInCard);

        signUpBtn.setOnClickListener(v -> {

            String name = userName.getText().toString();
            String mobile = phoneNumber.getText().toString();
            String password1 = password.getText().toString();
            String email = emailId.getText().toString();


            if(checkValidation())
            {
//                progressDialog = KProgressHUD.create(SignUp.this)
//                        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                        .setLabel("Please wait")
//                        .setCancellable(false)
//                        .setAnimationSpeed(2)
//                        .setDimAmount(0.5f)
//                        .show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("@FFFFF", response);
                                try {
                                    JSONObject jsonObject;
                                    jsonObject = new JSONObject(response);
                                   // Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();

                                    String status = jsonObject.getString("status");
                                    if(status.equals("1")) {

                                        JSONArray array = jsonObject.getJSONArray("Data");
                                        Log.d("QQQQQ",status);
                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignUp.this, Login.class));
                                        //progressDialog.dismiss();


                                    }else{
                                        Log.d("hhh", "Response ");
                                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SignUp.this, Login.class));
                                        //progressDialog.dismiss();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                   // progressDialog.dismiss();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // progressDialog.dismiss();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters to login url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("name",name);
                        params.put("password",password1);
                        params.put("mobile",mobile);
                        params.put("email",email);
                        Log.d("@params", String.valueOf(params));
                        return params;
                    }
                };
                App.getInstance().addToRequestQueue(stringRequest);

            }
        });

        signInText.setOnClickListener(v -> {
            startActivity(new Intent(SignUp.this, Login.class));
        });
    }

    private boolean checkValidation() {
        if (TextUtils.isEmpty(userName.getText().toString())) {
            userName.setError("Please enter valid Name");
            userName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(emailId.getText().toString())) {
            emailId.setError("Please enter valid Email");
            emailId.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
            phoneNumber.setError("Please enter valid Phone Number");
            phoneNumber.requestFocus();
            return false;
        }

        if (phoneNumber.getText().toString().length() > 14 ||
                phoneNumber.getText().toString().length() < 13) {
            phoneNumber.setError("Phone number should be valid");
            phoneNumber.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password.getText().toString())) {
            password.setError("Please enter valid Password");
            password.requestFocus();
            return false;
        }

        if (password.getText().toString().length() < 6) {
            password.setError("Password length should be at least 6!");
            password.requestFocus();
            return false;
        }
        return true;
    }


}