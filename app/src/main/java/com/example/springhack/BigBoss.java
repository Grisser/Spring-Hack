package com.example.springhack;

import android.content.DialogInterface;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class BigBoss extends AppCompatActivity {
    DataAdapterForRecyclerViewTopComands adapter;
    DatabaseReference myRef;
    String flag = "true";
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    int counter;
    ArrayList<TopComandForRecyclerView> comands = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_boss);
        
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                counter = Integer.valueOf(dataSnapshot.child("team").child("counter").getValue(String.class));
                flag = dataSnapshot.child("team").child("team1").child("activeBigBoss").getValue(String.class);
                Toast.makeText(BigBoss.this, flag + "", Toast.LENGTH_SHORT).show();

                for (int i = 1; i < counter+2; i++) {
                    String s = "team"+i;
                    String name = s;
                    String xp;
                    String nomber = i+"";
                    xp = dataSnapshot.child("team").child(s).child("stat").getValue(String.class);
                    comands.add(new TopComandForRecyclerView(name,xp, nomber,BigBoss.this));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        layoutManager = new LinearLayoutManager(BigBoss.this, LinearLayoutManager.VERTICAL, false);
        recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(layoutManager);

        updateUI();





      /*  if(flag.compareTo("false") == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder((BigBoss.this));
            builder.setTitle("Время не пришло")
                    .setMessage("Враг наподет через 2 недели")
                    .setCancelable(false)
                    .setNegativeButton("Ок, закрыть",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }*/

    }

    public void updateUI() {


        adapter = new DataAdapterForRecyclerViewTopComands(BigBoss.this, comands);
        // устанавливаем для списка адаптер
        recyclerView.setAdapter(adapter);
    }
}
