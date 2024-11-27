package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class login_activity extends AppCompatActivity {

    private FirebaseAuth auth;
    Button toSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        auth = FirebaseAuth.getInstance();

        EditText emailText = findViewById(R.id.email_text);
        EditText passwordText = findViewById(R.id.password_text);
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(view -> {
            String email = emailText.getText().toString().trim();
            String password = passwordText.getText().toString().trim();

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Please enter Email and Password", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(login_activity.this, activity_product.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        Button start = findViewById(R.id.toSignUp);
        start.setOnClickListener(view -> {
            Intent intent = new Intent(login_activity.this, signUp_activity.class);
            startActivity(intent);
        });

    }
}