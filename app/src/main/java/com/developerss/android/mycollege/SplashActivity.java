package com.developerss.android.mycollege;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    String userUid;

    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("USERS");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (firebaseUser != null){
                    userUid = firebaseUser.getUid();
                    databaseReference.child(userUid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            user = dataSnapshot.child("User").getValue(String.class);

                            if (user != null) {
                                if (user.equals("Teacher")) {
                                    Intent intent = new Intent(SplashActivity.this, TeacherDashboardActivity.class);
                                    startActivity(intent);
                                } else if (user.equals("Student")) {
                                    Intent intent = new Intent(SplashActivity.this, TeacherDashboardActivity.class);
                                    startActivity(intent);
                                } else if (user.equals("Admin")) {
                                    Intent intent = new Intent(SplashActivity.this, TeacherDashboardActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Intent intent = new Intent(SplashActivity.this, SignInActivity.class);
                    startActivity(intent);
                }

            }
        },2000);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getSupportActionBar().hide();

    }
}
















// Don't Change Anything
