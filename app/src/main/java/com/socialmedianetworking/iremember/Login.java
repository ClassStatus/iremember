package com.socialmedianetworking.iremember;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {

    private EditText memberIdLogIn, passwordLogIn;
    private TextView signInNowText, forgotPasswordText, createNewText;

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

        // TODO: Implement authentication logic here
        startActivity(new Intent(Login.this,MainActivity.class));
        Toast.makeText(this, "Sign In Clicked", Toast.LENGTH_SHORT).show();
    }

    private void handleForgotPassword() {
        // TODO: Implement forgot password logic here
        Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show();
    }

    private void handleCreateAccount() {
        // TODO: Implement create new account logic here
        Toast.makeText(this, "Create New Account Clicked", Toast.LENGTH_SHORT).show();
    }
}