package com.example.springhack;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Exhange extends AppCompatActivity {

    private ListView listOfMerch;
    private ListView listOfUsers;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

    private String userId;

    private List<String> listOfThingsL;
    private List<String> listOfUsersL;
    private List<String> idOfUsers;

    private String endOfThings = null;
    private String endOfUser = null;
    String team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhange);

        listOfMerch = findViewById(R.id.listV1);
        listOfUsers = findViewById(R.id.listV2);
        listOfUsersL = new ArrayList<>();
        listOfThingsL = new ArrayList<>();
        idOfUsers = new ArrayList<>();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Exhange.this);
        userId = preferences.getString("id", null);

        myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                team = dataSnapshot.child("users").child(userId).child("team").getValue(String.class);
                String counterStr = dataSnapshot.child("users").child(userId).child("artifact").child("counter").getValue(String.class);
                for (int i = 0; i < Integer.parseInt(counterStr) + 1; i++) {
                    if(dataSnapshot.child("users").child(userId).child("artifact").child(i + "").child("name").getValue(String.class) != null) {
                        listOfThingsL.add(dataSnapshot.child("users").child(userId).child("artifact").child(i + "").child("name").getValue(String.class));
                    }
                }
                String counterUserStr = dataSnapshot.child("team").child(team).child("users").child("counter").getValue(String.class);
                for (int it = 0; it < Integer.parseInt(counterUserStr)+1; it++){
                    if(dataSnapshot.child("team").child(team).child("users").child(it + "").child("name").getValue(String.class) != null) {
                        listOfUsersL.add(dataSnapshot.child("team").child(team).child("users").child(it + "").child("name").getValue(String.class));
                        idOfUsers.add(dataSnapshot.child("team").child(team).child("users").child(it + "").child("id").getValue(String.class));
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Exhange.this, android.R.layout.simple_list_item_1, listOfThingsL);
                listOfMerch.setAdapter(adapter);

                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Exhange.this, android.R.layout.simple_list_item_1, listOfUsersL);
                listOfUsers.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        listOfUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, final int position, long id) {
                if(endOfUser == null){
                    AlertDialog.Builder ad;
                    ad = new AlertDialog.Builder(Exhange.this);
                    ad.setTitle("Инфо");  // заголовок
                    ad.setMessage("Вы уверены, что выбираете этого человека?"); // сообщение
                    ad.setPositiveButton("Да, конечно", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            endOfUser = idOfUsers.get(position);
                        }
                    });
                    ad.setNegativeButton("Нет, вернуться назад", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.cancel();
                        }
                    });
                    ad.setCancelable(true);
                    ad.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Exhange.this);
                    builder.setTitle("Информация")
                            .setMessage("Вы уже выбрали человека")
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

        listOfMerch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, final int position, long id) {
                if(endOfThings == null){
                    AlertDialog.Builder ad;
                    ad = new AlertDialog.Builder(Exhange.this);
                    ad.setTitle("Инфо");  // заголовок
                    ad.setMessage("Вы уверены, что выбираете эту вещь?"); // сообщение
                    ad.setPositiveButton("Да, конечно", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            endOfThings = position + "";
                        }
                    });
                    ad.setNegativeButton("Нет, вернуться назад", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.cancel();
                        }
                    });
                    ad.setCancelable(true);
                    ad.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Exhange.this);
                    builder.setTitle("Информация")
                            .setMessage("Вы уже выбрали человека")
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

        Button delete = findViewById(R.id.xch2);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endOfUser = null;
                endOfThings = null;
                Toast.makeText(Exhange.this, "Сброс выбора выполнен", Toast.LENGTH_SHORT).show();
            }
        });


        Button exchange = findViewById(R.id.xch);
        exchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(endOfThings != null && endOfUser != null){
                    myRef = database.getReference();
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            team = dataSnapshot.child("users").child(userId).child("team").getValue(String.class);

                            String artIdImage = dataSnapshot.child("users").child(userId).child("artifact").child(endOfThings).child("id_image").getValue(String.class);
                            String artInfo= dataSnapshot.child("users").child(userId).child("artifact").child(endOfThings).child("info").getValue(String.class);
                            String artName = dataSnapshot.child("users").child(userId).child("artifact").child(endOfThings).child("name").getValue(String.class);

                            String lastCounter = dataSnapshot.child("users").child(endOfUser).child("artifact").child("counter").getValue(String.class);
                            int co = Integer.parseInt(lastCounter) + 1;
                            myRef.child("users").child(endOfUser).child("artifact").child(co+"").child("id_image").setValue(artIdImage);
                            myRef.child("users").child(endOfUser).child("artifact").child(co+"").child("info").setValue(artInfo);
                            myRef.child("users").child(endOfUser).child("artifact").child(co+"").child("name").setValue(artName);
                            myRef.child("users").child(endOfUser).child("artifact").child("counter").setValue(co+"");

                            myRef.child("users").child(userId).child("artifact").child(endOfThings).setValue(null);

                            endOfUser = null;
                            endOfThings = null;
                            Toast.makeText(Exhange.this, "Обмен прошел успешно", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            endOfUser =null;
                            endOfThings = null;
                        }
                    });

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Exhange.this);
                    builder.setTitle("Информация")
                            .setMessage("Вы не всех выбрали")
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
}
