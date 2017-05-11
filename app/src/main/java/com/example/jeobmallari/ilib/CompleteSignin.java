package com.example.jeobmallari.ilib;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.jeobmallari.ilib.DBHelper.userTableName;

public class CompleteSignin extends AppCompatActivity implements Button.OnClickListener{

    EditText studentNo_et;
    EditText homeAdd_et;
    EditText collAdd_et;
    EditText bday_et;
    Spinner college_spinner;
    Button button;

    SignedInGoogleClient client;
    GoogleApiClient mGoogleClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_signin);
        button = (Button) findViewById(R.id.submitButton);
        button.setOnClickListener(this);
        client = SignedInGoogleClient.getOurInstance();
        studentNo_et = (EditText) findViewById(R.id.studnum);
        homeAdd_et = (EditText) findViewById(R.id.homeadd);
        collAdd_et = (EditText) findViewById(R.id.collegeadd);
        bday_et = (EditText) findViewById(R.id.birthday);
        college_spinner = (Spinner) findViewById(R.id.college);
        mGoogleClient = client.getmGoogleClient();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.submitButton){
            completeSignIn();
        }
    }

    public void completeSignIn(){
        String studentNo = studentNo_et.getText().toString();
        String homeAdd = homeAdd_et.getText().toString();
        String collAdd = collAdd_et.getText().toString();
        String bday = bday_et.getText().toString();
        String college = college_spinner.getSelectedItem().toString();

        client.setStudentNo(studentNo);
        client.setHomeadd(homeAdd);
        client.setCollegeadd(collAdd);
        client.setBday(bday);
        client.setCollege(college);

        User user = new User();
        user.setId(client.getId());
        user.setFamilyName(client.getFamilyName());
        user.setDisplayName(client.getDisplayName());
        user.setEmail(client.getEmail());
        user.setGivenName(client.getGivenName());
        user.setName(client.getName());
        if(client.getDisplayPic() != null)
            user.setDisplayPic(client.getDisplayPic().toString());
        else user.setDisplayPic("");

        user.setStudentNo(client.getStudentNo());
        user.setHomeadd(client.getHomeadd());
        user.setCollegeadd(client.getCollegeadd());
        user.setBday(client.getBday());
        user.setCollege(client.getCollege());

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child(userTableName);
        dbRef.child(user.getId()).setValue(user);
        Log.e("Complete Sign In: ", client.getStudentNo() + " log-in complete");
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
        finish();
    }
}
