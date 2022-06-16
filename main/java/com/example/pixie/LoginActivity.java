package com.example.pixie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {
    private final DatabaseReference Firebase = FirebaseDatabase.getInstance().getReference();
    private final DatabaseReference databaseReference = Firebase.child("User");
    private User user;

    private EditText edtUserName, edtPassword;
    Button btnLogin;
    TextView tvCreateAcc;
    String password, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //GET ACTIVITY UTILS
        getUtils();
        //INTIALIZE USER MODEL
        user = new User();
        btnLogin.setOnClickListener(view -> {
            //SAME AS CHECKFIELDS() == TRUE
            if (CheckFields()) {
                //CALLING DATABASE
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //IF DATABASE > USER > USERNAME exists
                        if (snapshot.child(userName).exists()) {
                            //USERNAME exists, GET ALL DATA and SAVE TO USER MODEL
                            //you can use it like displaying email
                            //exe.
                            //tvCreateAcc.setText(user.getEmail()); aswell
                            user = snapshot.child(userName).getValue(User.class);
                            //USERNAME exists, CHECK PASSWORD
                            if (password.equals(user.getPassword())) {
                                Intent intent = new Intent(LoginActivity.this, Mall.class);
                                intent.putExtra("pass_userName",userName);
                                startActivity(intent);
                                /*
                                startActivity(intent);
                                startActivity(new Intent(LoginActivity.this, Mall.class));
                                finish();
                                 */
                            } else {
                                Snackbar.make(findViewById(android.R.id.content), "Wrong Password!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(findViewById(android.R.id.content), "User doesn't exits!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        tvCreateAcc.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void getUtils() {
        edtUserName = findViewById(R.id.login_edtUserName);
        edtPassword = findViewById(R.id.login_edtPassword);
        btnLogin = findViewById(R.id.login_btnLogin);
        tvCreateAcc = findViewById(R.id.login_tvCreateAcc);
    }

    private Boolean CheckFields() {
        boolean bool = false;
        getValues();
        if (userName.isEmpty()) {
            edtUserName.setError("Empty Field!");
        } else if (password.isEmpty()) {
            edtPassword.setError("Empty Field!");
        } else {
            bool = true;
        }
        return bool;
    }

    private void getValues() {
        userName = edtUserName.getText().toString();
        password = edtPassword.getText().toString();
    }
}