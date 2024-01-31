package com.hafidikhsana.formulaonedrivers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button registButton;
    Button toLogineButton;
    EditText emailInputRegist;
    EditText passwordInputRegist;
    EditText repeatPasswordInputRegist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        registButton = findViewById(R.id.regist_button);
        toLogineButton = findViewById(R.id.to_login_button);
        emailInputRegist = findViewById(R.id.regist_email_input);
        passwordInputRegist = findViewById(R.id.regist_password_input);
        repeatPasswordInputRegist = findViewById(R.id.regist_password_confirm_input);
        mAuth = FirebaseAuth.getInstance();

        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInputRegist.getText().toString();
                String password = passwordInputRegist.getText().toString();
                String repeatPassword = repeatPasswordInputRegist.getText().toString();

                if (password.equals(repeatPassword)) {

                    if (email.equals("") || password.equals("")) {
                        Toast.makeText(RegistActivity.this, "The form is empty",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    startActivity(new Intent(RegistActivity.this,LoginActivity.class));
                                } else {
                                    Toast.makeText(RegistActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(RegistActivity.this, "Password tidak sesuai", Toast.LENGTH_LONG).show();
                }
            }
        });

        toLogineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistActivity.this,LoginActivity.class));
            }
        });
    }
}