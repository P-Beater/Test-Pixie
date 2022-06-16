package com.example.pixie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pixie.DAO.DAOUser;
import com.example.pixie.Model.User;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private final DatabaseReference Firebase = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference databaseReference = Firebase.child("User");

    private TextView tvUserName, tvEmail;
    private EditText edtUserName;
    private Button btnSearch;
    private String strUserName;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUtils();

        btnSearch.setOnClickListener(view -> {
            user = new User();
            strUserName = edtUserName.getText().toString();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(strUserName).exists()) {
                        user = snapshot.child(strUserName).getValue(User.class);

                        //JUST LIKE THIS vvvvv
                        //refer to line 53 on loginActivity.java
                        tvUserName.setText(user.getUserName());
                        tvEmail.setText(user.getEmail());

                    } else {
                        Snackbar.make(findViewById(android.R.id.content), "User doesn't exits!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    private void getUtils() {
        tvUserName = findViewById(R.id.main_tvUserName);
        tvEmail = findViewById(R.id.main_tvEmail);
        edtUserName = findViewById(R.id.main_edtSearch);
        btnSearch = findViewById(R.id.main_btnSearch);

    }

}