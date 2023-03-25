package com.suuniv.afinal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PaymentMethod extends AppCompatActivity {

    private Uri imageUri=null;
    EditText bankNumber;
    EditText experationdate;
    EditText securitycode;
    String banktype = "";


    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    ImageView captureImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);


        captureImage =   findViewById(R.id.bankCard);
        bankNumber =   findViewById(R.id.bankNumber);
        experationdate =   findViewById(R.id.exp);
        securitycode =   findViewById(R.id.securityCode);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        setSupportActionBar(myToolbar);


        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        System.out.println("In new activity");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String currentUserString = currentUser.getUid();

        DatabaseReference allPostsRef = FirebaseDatabase.getInstance().getReference("BankInfo/" + currentUserString);
        allPostsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    paymentProfile paymentProfiles = snapshot.getValue(paymentProfile.class);
                    HashMap paymentProfile = (HashMap) snapshot.getValue();
                    bankNumber.setText(paymentProfiles.bankNumber);
                    experationdate.setText(paymentProfiles.expDate);
                    securitycode.setText(paymentProfiles.secCode);
                    String imageDecider = paymentProfiles.getBankType();

                    if (imageDecider.equalsIgnoreCase("mastercard")){
                        captureImage.setImageResource(R.drawable.mastercard);
                    }
                    else if (imageDecider.equalsIgnoreCase("visa")) {
                        captureImage.setImageResource(R.drawable.visa);
                    }
                    else if (imageDecider.equalsIgnoreCase("amex")) {
                        captureImage.setImageResource(R.drawable.amex);
                    }


                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed, how to handle?

            }

        });




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_drawer, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.signout:
                mAuth.signOut();
                finish();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case 1:
                if (checked)
                    captureImage.setImageResource(R.drawable.visa);
                banktype = "visa";
                    break;
            case 2:
                if (checked)
                    captureImage.setImageResource(R.drawable.mastercard);
                banktype = "mastercard";

                break;
            case 3:
                if (checked)
                    captureImage.setImageResource(R.drawable.amex);
                banktype = "amex";

                break;
        }
    }




    public void pushData2(View view) {
        //https://firebase.google.com/docs/database/admin/save-data

        if(bankNumber.getText().toString().equals("")|| securitycode.getText().toString().equals("")
                || experationdate.getText().toString().equals("")|| banktype.equals("")){
            Toast.makeText(this, "Please provide all information", Toast.LENGTH_SHORT).show();
            return;
        }

        saveUserDataToDB();
    }

    private void saveUserDataToDB(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("BankInfo");
        System.out.println("trying to push");
        usersRef.child(currentUser.getUid()).setValue(new paymentProfile(bankNumber.getText().toString(),
                banktype, experationdate.getText().toString(),
                securitycode.getText().toString()));
        Toast.makeText(this, "Payment method has been updated", Toast.LENGTH_SHORT).show();
        finish();
    }




}
