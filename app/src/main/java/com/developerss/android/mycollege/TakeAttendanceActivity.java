package com.developerss.android.mycollege;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TakeAttendanceActivity extends AppCompatActivity {

    DatabaseReference studentDatabaseReference;
    DatabaseReference attendanceDatabaseReference;
    DatabaseReference allStudentsDatabaseReference;

    ArrayList<String> arrayList = new ArrayList<>();
    ArrayList<String> presentStudentsList = new ArrayList<>();
    ArrayList<String> absentStudentsList = new ArrayList<>();
    ListView studentAttendanceListView;
    ArrayAdapter<String> arrayAdapter;

    Spinner branchSpinner;
    Spinner classSpinner;
    Spinner periodSpinner;
    String branches[] = {"Branch", "CS", "IT", "ME", "EC"};
    String classes[] = {"Section", "A", "B", "C", "D", "E", "F"};
    String periods[] = {"Period", "1", "2", "3", "4", "5", "6", "7", "8"};
    ArrayAdapter<String> branchAdapter;
    ArrayAdapter<String> classAdapter;
    ArrayAdapter<String> periodAdapter;

    String branch;
    String section;
    String period;

    int m = 3;

    Button loadButton;
    Button submitButton;
    CheckedTextView allCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        studentAttendanceListView = findViewById(R.id.student_attendance_listview);
        studentAttendanceListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_row_layout, arrayList);
        studentAttendanceListView.setAdapter(arrayAdapter);

        studentDatabaseReference = FirebaseDatabase.getInstance().getReference("STUDENTS");
        attendanceDatabaseReference = FirebaseDatabase.getInstance().getReference("ATTENDANCE");
        allStudentsDatabaseReference = FirebaseDatabase.getInstance().getReference("AllStudents");

        classSpinner = findViewById(R.id.class_spinner);
        periodSpinner = findViewById(R.id.period_spinner);
        branchSpinner = findViewById(R.id.branch_spinner);

        loadButton = findViewById(R.id.load_students_button);
        submitButton = findViewById(R.id.submit_attendance);
        allCheckBox = findViewById(R.id.all_checkbox);

        allCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(allCheckBox.isChecked())) {
                    allCheckBox.setChecked(true);
                    for (int i = 0; i < studentAttendanceListView.getChildCount(); i++) {
                        studentAttendanceListView.setItemChecked(i, true);
                        presentStudentsList.add(arrayList.get(i));
                    }
                } else {
                    allCheckBox.setChecked(false);
                    for (int i = 0; i < studentAttendanceListView.getChildCount(); i++) {
                        studentAttendanceListView.setItemChecked(i, false);
                    }
                }
            }
        });

        adapters();

        studentAttendanceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedStud = ((TextView) view).getText().toString();
                if (presentStudentsList.contains(selectedStud)) {
                    presentStudentsList.remove(selectedStud);
                } else {
                    presentStudentsList.add(selectedStud);
                }
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadStudents();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAttendance();
                Toast.makeText(TakeAttendanceActivity.this, "Attendance Submitted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(TakeAttendanceActivity.this, TeacherDashboardActivity.class);
            }
        });

    }

    public void loadStudents() {

        studentDatabaseReference.child(branch).child(section).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
//                    Toast.makeText(TakeAttendanceActivity.this, (CharSequence) dsp, Toast.LENGTH_SHORT).show();
                    String name = dsp.getKey();
                    if (!(arrayList.contains(name))) {
//                        String val = (String) dsp.getValue();
                        arrayList.add(name);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void submitAttendance() {

        /* If Branch, Section or Period is not Selected */

        long date = System.currentTimeMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        String dateString = dateFormat.format(date);

        absentStudentsList = arrayList;

        for (final String pStud : presentStudentsList) {
            absentStudentsList.remove(pStud);

            DatabaseReference reff = allStudentsDatabaseReference.child(pStud);
            reff.child("Total Attendance").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String val = dataSnapshot.getValue().toString();
                    m = Integer.parseInt(val);
//                    Toast.makeText(TakeAttendanceActivity.this, val, Toast.LENGTH_SHORT).show();
                    m++;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(TakeAttendanceActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            reff.child("Total Attendance").setValue(m);
            attendanceDatabaseReference.child(branch + " - " + section).child(dateString)
                    .child("Period - " + period).child(pStud).setValue("P");

        }

        for (String aStud : absentStudentsList) {
            attendanceDatabaseReference.child(branch + " - " + section).child(dateString)
                    .child("Period - " + period).child(aStud).setValue("A");

        }

    }

    public void adapters() {

        branchAdapter = new ArrayAdapter<>(TakeAttendanceActivity.this, android.R.layout.simple_spinner_dropdown_item, branches);
        branchSpinner.setAdapter(branchAdapter);
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch = branches[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        classAdapter = new ArrayAdapter<>(TakeAttendanceActivity.this, android.R.layout.simple_spinner_dropdown_item, classes);
        classSpinner.setAdapter(classAdapter);
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                section = classes[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        periodAdapter = new ArrayAdapter<>(TakeAttendanceActivity.this, android.R.layout.simple_spinner_dropdown_item, periods);
        periodSpinner.setAdapter(periodAdapter);
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                period = periods[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}


// do not change

