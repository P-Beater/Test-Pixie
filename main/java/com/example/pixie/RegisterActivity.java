package com.example.pixie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pixie.DAO.DAOUser;
import com.example.pixie.Model.User;

public class RegisterActivity extends AppCompatActivity {
    //DECLARATIONS
    DAOUser daoUser;
    User user;
    private EditText edtUserName, reg_reg_emailET, reg_reg_passwordET, reg_reg_repasswordET;
    Button reg_reg_signupBtn;
    TextView regLogin;

    String email, password, confPassword, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //CONNECTS ALL ACTIVITY REGISTER XML TO REGISTER.JAVA
        getUtils();

        //REGISTER AND SAVE DATA IN FIREBASE
        reg_reg_signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Loading!");
                progressDialog.show();
                //DATA INPUT VALIDATIONS
                if (CheckFields() == true) {
                    //CALLING MODEL
                    User user = new User(email, password, userName);
                    //CALLING DAO
                    daoUser = new DAOUser();
                    //TELLING DAO TO ADD THE DATA INSIDE USER MODEL
                    daoUser.add(user).addOnSuccessListener(suc -> {
                        progressDialog.dismiss();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(er -> {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    });
                }
                progressDialog.dismiss();
            }
        });
        regLogin.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });


    }

    //VOID TO GET UTILS
    private void getUtils() {
        edtUserName = findViewById(R.id.reg_edtUserName);
        reg_reg_emailET = findViewById(R.id.reg_emailET);
        reg_reg_passwordET = findViewById(R.id.reg_passwordET);
        reg_reg_repasswordET = findViewById(R.id.reg_repasswordET);
        reg_reg_signupBtn = findViewById(R.id.reg_signupBtn);
        regLogin = findViewById(R.id.reg_tvLogin);
    }

    //BOOLEAN TO FIELDS CHECKER
    private Boolean CheckFields(){
        boolean bool = false;
        getValues();
        if (userName.isEmpty()) {
            edtUserName.setError("Empty Field!");
        }else if(password.isEmpty()){
            reg_reg_passwordET.setError("Empty Field!");
        }else if(confPassword.isEmpty()){
            reg_reg_repasswordET.setError("Empty Field!");
        }else if(!(password.equals(confPassword))){
            reg_reg_repasswordET.setError("Password doesn't Match!");
        }else{
            bool = true;
        }
        return bool;
    }

    //VOID GET VALUES FROM UTILS
    private void getValues() {
        userName = edtUserName.getText().toString();
        email = reg_reg_emailET.getText().toString();
        password = reg_reg_passwordET.getText().toString();
        confPassword = reg_reg_repasswordET.getText().toString();
    }
}