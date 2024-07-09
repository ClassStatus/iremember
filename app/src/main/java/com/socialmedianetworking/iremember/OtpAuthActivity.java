package com.socialmedianetworking.iremember;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpAuthActivity extends AppCompatActivity {

    private EditText editTextOtp;
    private Button buttonVerifyOtp;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);

        editTextOtp = findViewById(R.id.editTextOtp);
        buttonVerifyOtp = findViewById(R.id.buttonVerifyOtp);
        mAuth = FirebaseAuth.getInstance();

        verificationId = getIntent().getStringExtra("verificationId");

        buttonVerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = editTextOtp.getText().toString().trim();
                if (TextUtils.isEmpty(otp)) {
                    Toast.makeText(OtpAuthActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                verifyCode(otp);
            }
        });
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Toast.makeText(OtpAuthActivity.this, "Verification Successful", Toast.LENGTH_SHORT).show();
                    // Redirect to another activity or perform the desired action
                } else {
                    Toast.makeText(OtpAuthActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
