package com.example.springhack;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Tasks extends AppCompatActivity {

    private ListView listView;
    private List<String> allTasks;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

    private String userId;

    private Random random = new Random();

    private List<Integer> id2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        allTasks = new ArrayList<>();
        listView = findViewById(R.id.list);
        id2 = new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Tasks.this);
        userId = preferences.getString("id", null);

        myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                final String team = dataSnapshot.child("users").child(userId).child("team").getValue(String.class);
                String counterStr = dataSnapshot.child("team").child(team).child("case").child("counter").getValue(String.class);
                for (int i = 0; i < Integer.parseInt(counterStr) + 1; i++) {
                    if(dataSnapshot.child("team").child(team).child("case").child(i + "").child("doneTeam").getValue(String.class) !=null) {
                        if (dataSnapshot.child("team").child(team).child("case").child(i + "").child("doneTeam").getValue(String.class).equals("false")) {
                            allTasks.add(dataSnapshot.child("team").child(team).child("case").child(i + "").child("info").getValue(String.class));
                            id2.add(i);
                        }
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Tasks.this, android.R.layout.simple_list_item_1, allTasks);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                        if ((dataSnapshot.child("team").child(team).child("case").child("isHero").getValue(String.class)).equals("true")) {
                            Intent intent = new Intent(Tasks.this, AddTask.class);
                            intent.putExtra("numberOfTaskForAdmin", id2.get(position) + "");
                            startActivity(intent);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Tasks.this);
                            builder.setTitle("Информация")
                                    .setMessage("Призовите персонажей")
                                    .setCancelable(false)
                                    .setNegativeButton("Ок, закрыть",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button addTask = findViewById(R.id.addTask);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef = database.getReference();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                        String team = dataSnapshot.child("users").child(userId).child("team").getValue(String.class);
                        if ((dataSnapshot.child("team").child(team).child("case").child("isHero").getValue(String.class)).equals("false")) {
                            String counterEvil = dataSnapshot.child("evil").child("counter").getValue(String.class);
                            String counterStr = dataSnapshot.child("team").child(team).child("case").child("counter").getValue(String.class);
                            String numberOfBoss = random.nextInt(Integer.parseInt(counterEvil) + 1) + "";

                            String idOFBoss = dataSnapshot.child("evil").child(numberOfBoss).child("id_image").getValue(String.class);
                            String nameOFBoss = dataSnapshot.child("evil").child(numberOfBoss).child("name").getValue(String.class);
                            String infoOFBoss = dataSnapshot.child("evil").child(numberOfBoss).child("info").getValue(String.class);
                            myRef.child("team").child(team).child("case").child("name").setValue(nameOFBoss);
                            myRef.child("team").child(team).child("case").child("info").setValue(infoOFBoss);
                            myRef.child("team").child(team).child("case").child("id_image").setValue(idOFBoss);

                            for (int i = 0; i < Integer.parseInt(counterStr) + 1; i++) {
                                String numberOfEvil = random.nextInt(Integer.parseInt(counterEvil) + 1) + "";

                                String idOFEvil = dataSnapshot.child("evil").child(numberOfEvil).child("id_image").getValue(String.class);
                                String nameOFEvil = dataSnapshot.child("evil").child(numberOfEvil).child("name").getValue(String.class);
                                String infoOFEvil = dataSnapshot.child("evil").child(numberOfEvil).child("info").getValue(String.class);

                                myRef.child("team").child(team).child("case").child(i + "").child("name").setValue(nameOFEvil);
                                myRef.child("team").child(team).child("case").child(i + "").child("infoOfPers").setValue(infoOFEvil);
                                myRef.child("team").child(team).child("case").child(i + "").child("id_image").setValue(idOFEvil);
                                myRef.child("team").child(team).child("case").child(i + "").child("doneTeam").setValue("false");

                            }

                            myRef.child("team").child(team).child("case").child("isHero").setValue("true");
                            Toast.makeText(Tasks.this, "Персонажи призваны", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Tasks.this);
                            builder.setTitle("Информация")
                                    .setMessage("Персонажи уже призваны")
                                    .setCancelable(false)
                                    .setNegativeButton("Ок, закрыть",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

}
