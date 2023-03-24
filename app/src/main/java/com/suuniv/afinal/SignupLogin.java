package com.suuniv.afinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class SignupLogin extends AppCompatActivity {

    private EditText email, password, displayname, phonenumber;
    private CheckBox userType;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    Button signupBtn;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);
        email=findViewById(R.id.emailText);
        password=findViewById(R.id.passwordText);
        phonenumber=findViewById(R.id.phoneNumberText);
        displayname=findViewById(R.id.displayNameText);
        signupBtn=findViewById(R.id.singupBtn);
        userType = findViewById(R.id.dogwalker);
        img= findViewById(R.id.logo);
        img.setImageResource(R.mipmap.dog_and_owner_round);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        updateUI();
    }

    public void Signup(View view) {
        //https://firebase.google.com/docs/database/admin/save-data

        if(email.getText().toString().equals("")|| password.getText().toString().equals("")
                || phonenumber.getText().toString().equals("")|| displayname.getText().toString().equals("")){
            Toast.makeText(this, "Please provide all information", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        currentUser=authResult.getUser();
                        saveUserDataToDB();
                        updateUI();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void Login(View view) {
        if(email.getText().toString().equals("")|| password.getText().toString().equals("")){
            Toast.makeText(this, "Please provide all information", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        currentUser=authResult.getUser();
                        Toast.makeText(SignupLogin.this, "Login Succesful.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignupLogin.this, MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void saveUserDataToDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("Users");
        boolean iswalker = userType.isChecked();
        String type ="";
        if(iswalker){
            type = "DOGWALKER";
        }else{
            type ="DOGOWNER";
        }
        usersRef.child(currentUser.getUid()).setValue(new User(displayname.getText().toString(),
                email.getText().toString(), phonenumber.getText().toString(),
                type));

    }
    private void updateUI(){
        if(currentUser!=null){
            findViewById(R.id.displayNameLayout).setVisibility(View.GONE);
            findViewById(R.id.phoneNumberLayout).setVisibility(View.GONE);
            signupBtn.setVisibility(View.GONE);
        }
    }

    public void restoreUI(View view){
        findViewById(R.id.displayNameLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.phoneNumberLayout).setVisibility(View.VISIBLE);
        signupBtn.setVisibility(View.VISIBLE);
    }
    public void ResetPassword(View view) {
        Toast.makeText(this, "You clicked ResetPassword", Toast.LENGTH_SHORT).show();

    }
    public void sendEmailVerification(View view) {
        Toast.makeText(this, "You clicked sendEmailVerification", Toast.LENGTH_SHORT).show();

    }

    public void dogwalkerClick(View view) {

        if(userType.isChecked()){
            img.setImageResource(R.mipmap.dog_walker_round);
        }
        else{
            img.setImageResource(R.mipmap.dog_and_owner_round);
        }

    }



}