package com.socialmedianetworking.iremember;


import static com.socialmedianetworking.iremember.util.Constant.POST_LIST;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.socialmedianetworking.iremember.activity.ProfileActivity;
import com.socialmedianetworking.iremember.adapters.PostAdapter;
import com.socialmedianetworking.iremember.model.Commentmodel;
import com.socialmedianetworking.iremember.model.Post;
import com.socialmedianetworking.iremember.util.App;
import com.socialmedianetworking.iremember.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    CircleImageView profileIcon;
    StringRequest stringRequest;
    //List<Post> posts  = new ArrayList<>();
    ArrayList<Post> posts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        profileIcon = findViewById(R.id.profileIcon);
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }
        });
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
//        }
        parseJsonResponse();
        //startActivity(new Intent(MainActivity.this, ProfileActivity.class));

    }

    private void parseJsonResponse() {
        // Implement your JSON parsing logic here
        // Example:
        posts = new ArrayList<>();
//        progressDialog = KProgressHUD.create(MyLoanList.this)
//                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
//                .setLabel("Please wait")
//                .setCancellable(true)
//                .setAnimationSpeed(2)
//                .setDimAmount(0.5f)
//                .show();
        Log.d("QQQQ",POST_LIST);
        stringRequest = new StringRequest(Request.Method.POST, POST_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("@qna111" + "", response);
                        try {
                            JSONObject jsonObject;
                            jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                JSONArray array = jsonObject.getJSONArray("Data");
                                for (int i = 0; i < array.length(); i++) {
                                    //name,id,loan_amount,tenuare, intrest_rate,laon_approvel_status,apply_date,approval_date,loan_complete
                                    JSONObject o = array.getJSONObject(i);

                                    List<Commentmodel> comments = new ArrayList<>();
                                    JSONArray commentsArray = o.getJSONArray("comments");

                                    for (int j = 0; j < commentsArray.length(); j++) {
                                        JSONObject commentObject = commentsArray.getJSONObject(j);
                                        Commentmodel comment = new Commentmodel(
                                                commentObject.getString("comment_id"),
                                                commentObject.getString("user_id"),
                                                commentObject.getString("username"),
                                                commentObject.getString("audio_file_id"),
                                                commentObject.getString("audio_file_path"),
                                                commentObject.getString("comment_created_at"),
                                                commentObject.getString("audio_uploaded_at"),
                                                commentObject.getString("pimage")
                                        );
                                        comments.add(comment);
                                    }
                                    Post newdata = new Post(
                                            o.getString("post_id"),
                                            o.getString("group_id"),
                                            o.getString("admin_id"),
                                            o.getString("content"),
                                            o.getString("content_text"),
                                            o.getInt("isImage"),
                                            o.getString("created_at"),
                                            o.getString("post_uploader_profile_image"),
                                            o.getString("post_uploaded_fullname"),
                                            comments
                                    );
                                    posts.add(newdata);
                                }
                                postAdapter = new PostAdapter(posts, MainActivity.this);
                                recyclerView.setAdapter(postAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                            } else {
                                Log.d("hhh", "Response ");
                                recyclerView.setVisibility(View.GONE);
                                //nopro.setVisibility(View.VISIBLE);
                            }
                            //progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("hhh", e.getMessage().toString());
                           // progressDialog.dismiss();
                            recyclerView.setVisibility(View.GONE);
                            //nopro.setVisibility(View.VISIBLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // progressDialog.dismiss();
                recyclerView.setVisibility(View.GONE);
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.e("VolleyError1", "Status code: " + networkResponse.statusCode);
                    Log.e("VolleyError2", "Data: " + new String(networkResponse.data));
                }
                Log.e("VolleyError3", error.toString());
               // nopro.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("group_id","1");
                Log.d("EEE", String.valueOf(params));
                return params;
            }
        };
        App.getInstance().addToRequestQueue(stringRequest);

    }

}