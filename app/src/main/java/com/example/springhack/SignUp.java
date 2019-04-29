package com.example.springhack;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignUp extends AppCompatActivity {
    String name;
    String firstName;
    Button signUp;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private FirebaseAuth mAuth;

    FirebaseUser user = mAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name = ((EditText) findViewById(R.id.name)).getText().toString();
        firstName = ((EditText) findViewById(R.id.firstname)).getText().toString();

        signUp = findViewById(R.id.confirm2);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = ((EditText) findViewById(R.id.name)).getText().toString();
                firstName = ((EditText) findViewById(R.id.firstname)).getText().toString();
                myRef = database.getReference();
                myRef.child("users").child(user.getUid()).child("name").setValue(name);
                myRef.child("users").child(user.getUid()).child("surname").setValue(firstName);
                myRef.child("users").child(user.getUid()).child("artifact").child("counter").setValue("-1");
                myRef.child("users").child(user.getUid()).child("hero").setValue("false");
                myRef.child("users").child(user.getUid()).child("stat").setValue("0");
                myRef.child("users").child(user.getUid()).child("tasks").child("counter").setValue("-1");
                myRef.child("users").child(user.getUid()).child("topStat").setValue("0");
                // team

                myRef = database.getReference();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String team = dataSnapshot.child("users").child(user.getUid()).child("team").getValue(String.class);
                        String counter = dataSnapshot.child("team").child(team).child("users").child("counter").getValue(String.class);
                        int co = Integer.parseInt(counter) + 1;

                        myRef.child("team").child(team).child("users").child(co+"").child("id").setValue(user.getUid());
                        myRef.child("team").child(team).child("users").child(co+"").child("name").setValue(name);
                        myRef.child("team").child(team).child("users").child(co+"").child("id").setValue("0");
                        myRef.child("team").child(team).child("users").child("counter").setValue(co+"");
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(SignUp.this, Hero.class);
                startActivity(intent);
            }
        });
    }
}
