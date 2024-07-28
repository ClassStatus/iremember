package com.socialmedianetworking.iremember.activity;

import static com.socialmedianetworking.iremember.util.Constant.LOGIN;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.socialmedianetworking.iremember.MainActivity;
import com.socialmedianetworking.iremember.R;
import com.socialmedianetworking.iremember.util.App;
import com.socialmedianetworking.iremember.util.UserSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

    private EditText memberIdLogIn, passwordLogIn;
    private TextView signInNowText, forgotPasswordText, createNewText;
    private UserSession session;
    public static final String TAG = "MyTag";
    private HashMap<String, String> user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        memberIdLogIn = findViewById(R.id.memberIdLogIn);
        passwordLogIn = findViewById(R.id.passwordLogIn);
        signInNowText = findViewById(R.id.signInNowText);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        createNewText = findViewById(R.id.createNewText);

        session= new UserSession(getApplicationContext());
        //validating session
        user = session.getUserDetails();
        //user_type = user.get(UserSession.USER_TYPE);

        if (session.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
        // Set click listeners
        signInNowText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignIn();
            }
        });

        forgotPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleForgotPassword();
            }
        });

        createNewText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCreateAccount();
            }
        });
    }

    private void handleSignIn() {
        String memberId = memberIdLogIn.getText().toString().trim();
        String password = passwordLogIn.getText().toString().trim();

        if (memberId.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter Member Id and Password", Toast.LENGTH_SHORT).show();
            return;
        }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("@Slider", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    if (status.equals("1")) {
                                        JSONArray array = jsonObject.getJSONArray("Data");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject o = array.getJSONObject(i);
                                            String fullname = o.getString("fullname");
                                            String phonenumber = o.getString("phonenumber");
                                            String user_id = o.getString("user_id");
                                            String username = o.getString("username");
                                            String email = o.getString("email");
                                            String pimage = o.getString("pimage");
                                            String gender = o.getString("gender");
                                            String dob = o.getString("dob");

                                            // Create shared preference and store data
                                            session.createLoginSession(user_id, fullname, phonenumber, email, username, pimage,gender,dob);


                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                        }
                                       // progressDialog.dismiss();
                                    } else {
                                        Log.d("hhh", "Response status is not 1");
                                        //progressDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    Log.e("JSONError", "Error parsing response", e);
                                    //progressDialog.dismiss();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", "Error in request", error);
                        //progressDialog.dismiss();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters to login url
                        Map<String, String> params = new HashMap<>();
                        params.put("username", memberId);
                        params.put("password", password);
                        Log.d("@params", params.toString());
                        return params;
                    }
                };

                App.getInstance().addToRequestQueue(stringRequest);





        // TODO: Implement authentication logic here
       // startActivity(new Intent(Login.this, MainActivity.class));
        //Toast.makeText(this, "Sign In Clicked", Toast.LENGTH_SHORT).show();
    }

    private void handleForgotPassword() {
        // TODO: Implement forgot password logic here
        Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show();
    }

    private void handleCreateAccount() {
        // TODO: Implement create new account logic here
        startActivity(new Intent(Login.this, SignUp.class));
    }
}