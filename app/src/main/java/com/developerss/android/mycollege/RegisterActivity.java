package com.developerss.android.mycollege;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentFirebaseUser;

    private String userUid;

    EditText nameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    Button registerButton;

    String[] userList = {"Admin", "Teacher", "Student"};
    String email;
    String password;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseReference = FirebaseDatabase.getInstance().getReference("USERS");
        firebaseAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = firebaseAuth.getCurrentUser();

        nameEditText = findViewById(R.id.name_edittext);
        emailEditText = findViewById(R.id.email_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = emailEditText.getText().toString();
                password = passwordEditText.getText().toString();

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            addUserToDatabase();
                            Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(RegisterActivity.this, SignInActivity.class);
//                            startActivity(intent);
//                    Intent intent = new Intent(RegisterActivity.this, Dashboard.class);

                        } else {
                            Toast.makeText(RegisterActivity.this, "Registration was Unsuccessful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void addUserToDatabase() {

        userUid = currentFirebaseUser.getUid();

        String name = nameEditText.getText().toString();

        DatabaseReference mRef =  databaseReference.child(userUid);

        mRef.child("Name").setValue(name);
        mRef.child("User").setValue(user);
        mRef.child("Email").setValue(email);
    }

    @Override
    protected void onStart() {
        super.onStart();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("REGISTER AS ");
        builder.setCancelable(false);
        builder.setItems(userList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        user = userList[which];
                        getSupportActionBar().setTitle("Registering as " + user);
                        break;
                    case 1:
                        user = userList[which];
                        getSupportActionBar().setTitle("Registering as " + user);
                        break;
                    case 2:
                        user = userList[which];
                        getSupportActionBar().setTitle("Registering as " + user);
                        break;
                }
                dialog.dismiss();
            }
        });
        builder.show();
    }
}















// Don't Change Anything
