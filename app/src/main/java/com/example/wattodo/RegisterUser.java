package com.example.wattodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView title;
    private Button registerUser;
    private EditText emailtxt,phonetxt,fullname,passwordtxt;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private final String LOG = RegisterUser.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        title = (TextView) findViewById(R.id.Regtitle);
        title.setOnClickListener(this);
        emailtxt = (EditText) findViewById(R.id.RegEmail);
        fullname = (EditText) findViewById(R.id.fullname);
        phonetxt = (EditText) findViewById(R.id.phone);
        passwordtxt = (EditText) findViewById(R.id.RegPassword);


        registerUser = findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar2);

    }

    @Override
    public void onClick(View view) {
           switch (view.getId())
            {
                case R.id.Regtitle :
                    Intent newintent = new Intent(this,MainActivity.class);
                    startActivity(newintent);
                    break;
                case R.id.registerUser :
                    registerUser();
                    break;
            }
    }

    private void registerUser() {
        final String email = emailtxt.getText().toString().trim();
        final String name = fullname.getText().toString().trim();
        final String password = passwordtxt.getText().toString().trim();
        final String phone = phonetxt.getText().toString().trim();


        if(name.isEmpty()) {
            fullname.setError("Email is empty");
            fullname.requestFocus(); return;
        }
        if(phone.isEmpty()) {
            phonetxt.setError("Phone number is Empty");
            phonetxt.requestFocus(); return;
        }
        if(phone.length() != 10) {
            phonetxt.setError("Enter a valid number");
            phonetxt.requestFocus(); return;
        }
        if(email.isEmpty()) {
            emailtxt.setError("Email is empty");
            emailtxt.requestFocus(); return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailtxt.setError("Enter Valid Email address");
            emailtxt.requestFocus(); return;
        }
        if(password.isEmpty()) {
            passwordtxt.setError("Enter Password");
            passwordtxt.requestFocus(); return;
        }
        if(password.length() < 8) {
            passwordtxt.setError("Min 8 character required for password");
            passwordtxt.requestFocus(); return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful() ) {
                            User user = new User(name,phone,email);
                            Log.d(LOG, "creating new user Auth");
                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this,"User Registered Successfully",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else {
                                        Toast.makeText(RegisterUser.this,"Failed to Register",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(RegisterUser.this,"Failed to Register",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}