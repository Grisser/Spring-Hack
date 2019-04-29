package com.example.springhack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BeforeEnemy extends AppCompatActivity {

    private ListView listView;
    private List<String> alltask;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

    private String userId;

    private List<Integer> id2;

    private String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_enemy);

        listView = findViewById(R.id.listView);
        alltask = new ArrayList<>();
        id2 = new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(BeforeEnemy.this);
        userId = preferences.getString("id", null);

        a = getIntent().getStringExtra("act");

        myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String counterStr = dataSnapshot.child("users").child(userId).child("tasks").child("counter").getValue(String.class);
                for (int i = 0; i < Integer.parseInt(counterStr) + 1; i++) {
                    if (dataSnapshot.child("users").child(userId).child("tasks").child(i + "").child("done").getValue(String.class) != null) {
                        if ((dataSnapshot.child("users").child(userId).child("tasks").child(i + "").child("done").getValue(String.class).equals("false")))
                            alltask.add(dataSnapshot.child("users").child(userId).child("tasks").child(i + "").child("taskInfo").getValue(String.class));
                            id2.add(i);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(BeforeEnemy.this, android.R.layout.simple_list_item_1, alltask);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                if(a.equals("1")) {
                    Intent intent = new Intent(BeforeEnemy.this, Enemy.class);
                    intent.putExtra("numberOfTask", id2.get(position) + "");
                    intent.putExtra("act1", "1");
                    startActivity(intent);
                } else if(a.equals("2")) {
                    Intent intent = new Intent(BeforeEnemy.this, Enemy.class);
                    intent.putExtra("numberOfTask", id2.get(position) + "");
                    intent.putExtra("act1", "2");
                    startActivity(intent);
                }

            }
        });
    }
}
