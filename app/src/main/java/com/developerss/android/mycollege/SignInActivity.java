package com.developerss.android.mycollege;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    EditText userIdEditText, userPasswordEditText;
    TextView registerTextView;
    Button signInButton;
    String userID, userPassword;

    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentFirebaseUser;
    private String userUid;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("USERS");

        userIdEditText = findViewById(R.id.user_id_et);
        userPasswordEditText = findViewById(R.id.user_password_et);
        registerTextView = findViewById(R.id.register_textview);
        signInButton = findViewById(R.id.sign_in_btn);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userID = userIdEditText.getText().toString();
                userPassword = userPasswordEditText.getText().toString();

                firebaseAuth.signInWithEmailAndPassword(userID, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    userDashboard();
                                } else {

                                    Toast.makeText(SignInActivity.this, "Sign In Unsuccessful", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void userDashboard() {

        userUid = currentFirebaseUser.getUid();

        databaseReference.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.child("User").getValue(String.class);
                if (user.equals("Teacher")) {
                    Intent intent = new Intent(SignInActivity.this, TeacherDashboardActivity.class);
                    startActivity(intent);
                } else if (user.equals("Student")) {
                    Intent intent = new Intent(SignInActivity.this, TeacherDashboardActivity.class);
                    startActivity(intent);
                } else if (user.equals("Admin")) {
                    Intent intent = new Intent(SignInActivity.this, TeacherDashboardActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(SignInActivity.this, "Signed In as a " + user, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
//        if (firebaseUser != null) {
//            userDashboard();
//            Toast.makeText(this, "User is Signed in already!", Toast.LENGTH_SHORT).show();
////            Intent intent = new Intent(SignInActivity.this, TeacherDashboardActivity.class);
////            startActivity(intent);
//        } else {
//            Toast.makeText(this, "Please Sign IN!", Toast.LENGTH_SHORT).show();
//        }
//
//    }
}


// Don't Change Anything
