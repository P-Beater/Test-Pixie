package com.example.pixie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pixie.Model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class Mall extends AppCompatActivity {

    private final DatabaseReference Firebase = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference databaseReference = Firebase.child("User");
    private Button a_mall_profileBtn;
    private long backPressedTime;
    private Dialog mDialog;
    private TextView a_mall_popu_emailadd, a_mall_popu_username;
    private User user;
    private String strUserName;
    String store_userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);
        getUtils();

        Intent intent = getIntent();

        store_userName = intent.getStringExtra("pass_userName");

        mDialog = new Dialog(this);
        a_mall_profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new User();
                strUserName = store_userName;
                Toast.makeText(Mall.this, "" + strUserName, Toast.LENGTH_SHORT).show();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if (snapshot.child(strUserName).exists()) {
                            user = snapshot.child(strUserName).getValue(User.class);

                            //JUST LIKE THIS vvvvv
                            //refer to line 53 on loginActivity.java
                            a_mall_popu_username.setText(user.getUserName());
                            a_mall_popu_emailadd.setText(user.getEmail());

                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "User doesn't exits!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                mDialog.setContentView(R.layout.popup);
                mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mDialog.show();
            }
        });
    }

    private void getUtils(){
        a_mall_profileBtn = findViewById(R.id.mall_profileBtn);
        a_mall_popu_emailadd = findViewById(R.id.popu_username);
        a_mall_popu_username = findViewById(R.id.popu_emailadd);
    }

    //.. this will prevent user from exiting when pressing back button the first time
    @Override
    public void onBackPressed(){

        if(backPressedTime + 2000 > System.currentTimeMillis()){
            //FirebaseAuth.getInstance().signOut();
            super.onBackPressed();
            finish();

        }else{
            Toast.makeText(getBaseContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}