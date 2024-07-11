/*package com.example.journalapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button loginBtn,createAccountBtn;
    private EditText emailEt,passEt;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAccountBtn=findViewById(R.id.create_account);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        loginBtn=findViewById(R.id.email_signin);
        emailEt=findViewById(R.id.email);
        passEt=findViewById(R.id.password);

        firebaseAuth=FirebaseAuth.getInstance();
        loginBtn.setOnClickListener(v->{
            logEmailUser(
                    emailEt.getText().toString().trim(),
                    passEt.getText().toString().trim()
            );
        });

    }

    private void logEmailUser(String email,String pwd){
        if(!TextUtils.isEmpty(null)&&!TextUtils.isEmpty(pwd)){
            firebaseAuth.signInWithEmailAndPassword(email,pwd).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user=firebaseAuth.getCurrentUser();
                    Intent i=new Intent(MainActivity.this,JournalListActivity.class);
                    startActivity(i);
                }
            });

        }
    }
}*/

package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    Button loginBtn, createAccountBtn;
    private EditText emailEt, passEt;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAccountBtn = findViewById(R.id.create_account);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        loginBtn = findViewById(R.id.email_signin);
        emailEt = findViewById(R.id.email);
        passEt = findViewById(R.id.password);

        firebaseAuth = FirebaseAuth.getInstance();
        loginBtn.setOnClickListener(v -> {
            logEmailUser(
                    emailEt.getText().toString().trim(),
                    passEt.getText().toString().trim()
            );
        });
    }

    private void logEmailUser(String email, String pwd) {
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd)) {
            firebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Intent i = new Intent(MainActivity.this, JournalListActivity.class);
                        startActivity(i);
                    } else {
                        // Handle failure
                        Toast.makeText(MainActivity.this, "Authentication failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            // Notify the user to enter email and password
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
        }
    }
}
