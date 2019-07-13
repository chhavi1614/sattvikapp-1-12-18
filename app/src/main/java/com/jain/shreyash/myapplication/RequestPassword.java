package com.jain.shreyash.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.jain.shreyash.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RequestPassword extends AppCompatActivity {
    String bodys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_password);
        Button send_mail=findViewById(R.id.password_mail);
        EditText get_email=findViewById(R.id.email_send);
        send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String[] passworded = new String[1];
                final String emailed = get_email.getText().toString();
                final String emailRefined =get_email.getText().toString().replaceAll("\\W+","");
                FirebaseDatabase LoginReference = FirebaseDatabase.getInstance();
                DatabaseReference mLoginReference = LoginReference.getReference("student_sheet");
                mLoginReference.child("students").child(emailRefined).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                PersonDetails personDetails2 = dataSnapshot.getValue(PersonDetails.class);
                                try {
                                    //checking if already registered or not
                                    if(personDetails2.email.equals(emailed)  )
                                    {
                                      //  Toast.makeText(RequestPassword.this, "correct Credentials", Toast.LENGTH_SHORT).show();

                                        BackgroundMail.newBuilder(RequestPassword.this)
                                                .withUsername("donotreply.sattvik@gmail.com")
                                                .withPassword("sattvik@iitbhu")
                                                .withMailto(emailed)
                                                .withType(BackgroundMail.TYPE_PLAIN)
                                                .withSubject("Sattvik Mess mobile application Password")
                                                .withBody("Please do remember your password for using all the digital facilities provided by sattvik mess "+"\n"+"\n"+"Your password is : " + personDetails2.pin)
                                                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                                                    @Override
                                                    public void onSuccess() {
                                                        //do some magic
                                                        Toast.makeText(getApplicationContext(), "Check your gmail", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                                                    @Override
                                                    public void onFail() {
                                                        Toast.makeText(getApplicationContext(), "send email failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .send();
                                    }
                                    else {
                                        Toast.makeText(RequestPassword.this, "Incorrect Email", Toast.LENGTH_SHORT).show();

                                    }





                    }
                                catch (Exception e){
                                    Toast.makeText(RequestPassword.this, "You are not registered with this email id", Toast.LENGTH_SHORT).show();

                                }
                }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(RequestPassword.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                            }

                            ;
            });





            }});}}
