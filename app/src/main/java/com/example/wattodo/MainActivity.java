package com.example.wattodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView register,forgetPassword;
    TextView editTextMail,editTextPassword;
    Button signUp;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        register = findViewById(R.id.register);
        forgetPassword = findViewById(R.id.forgetPassword);
        signUp = (Button)findViewById(R.id.signIn);
        signUp.setOnClickListener(this);

        editTextMail = (TextView) findViewById(R.id.email);
        editTextPassword = (TextView) findViewById(R.id.password);
        progressbar3 = (ProgressBar)findViewById(R.id.progressBar);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegisterUser.class));
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForgetPassword();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setForgetPassword() {

        if(!isNetworkAvailable()) {
            Toast.makeText(this, "Please Check your Internet Connectivity", Toast.LENGTH_LONG).show();
            return;
        }
        String email = editTextMail.getText().toString().trim();
        if(email.isEmpty()) {
            editTextMail.setError("Email is empty,Enter Your mailId to send Reset Password Link");
            editTextMail.requestFocus(); return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextMail.setError("Enter Valid Email address");
            editTextMail.requestFocus(); return;
        }
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Check Ur Email for Password Reset", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Email-Id for User doesn't Exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
         switch(view.getId()) {
             case R.id.signIn :
                 userLogin();
                 break;
         }
    }

    private void userLogin() {
        if(!isNetworkAvailable()) {
            Toast.makeText(this, "Please Check your Internet Connectivity", Toast.LENGTH_LONG).show();
            return;
        }
        String email = editTextMail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()) {
            editTextMail.setError("Email is empty");
            editTextMail.requestFocus(); return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextMail.setError("Enter Valid Email address");
            editTextMail.requestFocus(); return;
        }
        if(password.isEmpty()) {
            editTextPassword.setError("Password is empty");
            editTextMail.requestFocus(); return;
        }
        progressbar3.setVisibility(View.VISIBLE);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if(user.isEmailVerified()) {
                                progressbar3.setVisibility(View.GONE);
                                startActivity(new Intent(MainActivity.this, ListMainPage.class));
                            }
                            else {
                                user.sendEmailVerification();
                                Toast.makeText(MainActivity.this,"Verification Mail has been sent to your mail",Toast.LENGTH_LONG).show();
                                progressbar3.setVisibility(View.GONE);
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this,"Login failed Recheck ur credential",Toast.LENGTH_LONG).show();
                            progressbar3.setVisibility(View.GONE);
                        }
                    }
                });
    }
}