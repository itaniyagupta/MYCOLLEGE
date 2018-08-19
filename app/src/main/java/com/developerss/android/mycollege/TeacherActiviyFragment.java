package com.developerss.android.mycollege;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeacherActiviyFragment extends Fragment {

    Button takeAttendanceButton;
    Button uploadAssignmentButton;
    Button feedbackButton;

    public TeacherActiviyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_activiy, container, false);

        takeAttendanceButton = view.findViewById(R.id.take_attendance_button);
        uploadAssignmentButton = view.findViewById(R.id.upload_assignment_button);
        feedbackButton = view.findViewById(R.id.feedback_button);

        takeAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TakeAttendanceActivity.class);
                startActivity(intent);
            }
        });

        uploadAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UploadAssignmentActivity.class);
                startActivity(intent);
            }
        });

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}

















// Don't Change Anything