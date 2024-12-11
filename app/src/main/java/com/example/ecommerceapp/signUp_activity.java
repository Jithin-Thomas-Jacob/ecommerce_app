package com.example.ecommerceapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class signUp_activity extends AppCompatActivity {

    Button signUp_button;
    TextView toLogin;
    private FirebaseAuth signUpAuth;
    private DatabaseReference databaseReference;
    private EditText firstName_text, lastName_text, email_text, password_text, repeat_password_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        Toast.makeText(this, "Sign Up Activity Launched", Toast.LENGTH_SHORT).show();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUpAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        firstName_text = findViewById(R.id.firstName_text);
        lastName_text = findViewById(R.id.lastName_text);
        email_text = findViewById(R.id.email_text);
        password_text = findViewById(R.id.password_text);
        repeat_password_text = findViewById(R.id.repeat_password_text);

        Button signUp = findViewById(R.id.signUp_button);
        signUp.setOnClickListener(view -> register());

        TextView start = findViewById(R.id.toLogin);
        start.setOnClickListener(view -> {
            Intent intent = new Intent(signUp_activity.this, login_activity.class);
            startActivity(intent);
        });
    }
    private void register(){
        String firstName = firstName_text.getText().toString().trim();
        String lastName = lastName_text.getText().toString().trim();
        String email = email_text.getText().toString().trim();
        String password = password_text.getText().toString().trim();
        String repeatPassword = repeat_password_text.getText().toString().trim();


        if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()){
            Toast.makeText(this, "Please Fill all the details", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.i(password, repeatPassword);
        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }


        if(!password.trim().equals(repeatPassword.trim())){
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }

        signUpAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if(task.isSuccessful()){
                FirebaseUser user = signUpAuth.getCurrentUser();
                if(user != null){
                    saveUserToDatabase(user.getUid(), firstName, lastName, email);
                    Toast.makeText(signUp_activity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(signUp_activity.this, login_activity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(signUp_activity.this, "Registration Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveUserToDatabase(String userId, String firstName, String lastName, String email) {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("firstName", firstName);
        userDetails.put("lastName", lastName);
        userDetails.put("email", email);

        databaseReference.child(userId).setValue(userDetails).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(this, "User Details saved Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save User Details", Toast.LENGTH_SHORT).show();
            }
        });

    }
}