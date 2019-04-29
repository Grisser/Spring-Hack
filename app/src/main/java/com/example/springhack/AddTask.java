package com.example.springhack;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddTask extends AppCompatActivity {
    private ImageView imageView;
    private TextView heroName;
    private TextView heroInfo;
    private TextView taskInfo;
    private TextView href;
    private ProgressBar xp;
    private ListView listView;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

    private String userId;
    private String number;
    private String team;

    private List<String> allUsersInTeam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        imageView = findViewById(R.id.logo);
        heroInfo = findViewById(R.id.heroInfo);
        heroName = findViewById(R.id.heroName);
        taskInfo = findViewById(R.id.taskInfo);
        href = findViewById(R.id.link);
        xp = findViewById(R.id.mainenprog);
        listView = findViewById(R.id.listView2);
        allUsersInTeam = new ArrayList<>();

        number = getIntent().getStringExtra("numberOfTaskForAdmin");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AddTask.this);
        userId = preferences.getString("id", null);

        myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                team = dataSnapshot.child("users").child(userId).child("team").getValue(String.class);
                heroInfo.setText("Информация о герое: " + dataSnapshot.child("team").child(team).child("case").child(number).child("infoOfPers").getValue(String.class));
                heroName.setText("Имя героя: " + dataSnapshot.child("team").child(team).child("case").child(number).child("name").getValue(String.class));
                taskInfo.setText("Задача: " + dataSnapshot.child("team").child(team).child("case").child(number).child("info").getValue(String.class));
                href.setText("Ссылка: " + dataSnapshot.child("team").child(team).child("case").child(number).child("href").getValue(String.class));

                String imageUri = dataSnapshot.child("team").child(team).child("case").child(number).child("id_image").getValue(String.class);

                int i1 = Integer.parseInt(dataSnapshot.child("team").child(team).child("case").child(number).child("stat").getValue(String.class));
                int i2 = Integer.parseInt(dataSnapshot.child("team").child(team).child("case").child("stat").getValue(String.class));
                int i = i1 * 100 / i2;

                xp.setProgress(i);

                String counter = dataSnapshot.child("team").child(team).child("users").child("counter").getValue(String.class);
                for (int g = 0; g < Integer.parseInt(counter) + 1; g++) {
                    allUsersInTeam.add(dataSnapshot.child("team").child(team).child("users").child(g + "").child("name").getValue(String.class));
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddTask.this, android.R.layout.simple_list_item_1, allUsersInTeam);
                listView.setAdapter(adapter);

                FirebaseStorage storage = FirebaseStorage.getInstance();
                if (imageUri != null) {
                    StorageReference storageRef = storage.getReferenceFromUrl(imageUri);
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(AddTask.this).load(uri).into(imageView);
                        }
                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, final int position, long id) {
                AlertDialog.Builder ad;
                ad = new AlertDialog.Builder(AddTask.this);
                ad.setTitle("Инфо");  // заголовок
                ad.setMessage("Вы уверены, что хотите ему отдать этого злодея"); // сообщение
                ad.setPositiveButton("Да, я уверен", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        myRef = database.getReference();
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                team = dataSnapshot.child("users").child(userId).child("team").getValue(String.class);

                                String idUser = dataSnapshot.child("team").child(team).child("users").child(String.valueOf(position)).child("id").getValue(String.class);
                                String counterTaskUser = dataSnapshot.child("users").child(idUser).child("tasks").child("counter").getValue(String.class);

                                String href = dataSnapshot.child("team").child(team).child("case").child(number).child("href").getValue(String.class);
                                String id_image = dataSnapshot.child("team").child(team).child("case").child(number).child("id_image").getValue(String.class);
                                String info = dataSnapshot.child("team").child(team).child("case").child(number).child("info").getValue(String.class);
                                String infoOfPers = dataSnapshot.child("team").child(team).child("case").child(number).child("infoOfPers").getValue(String.class);
                                String name = dataSnapshot.child("team").child(team).child("case").child(number).child("name").getValue(String.class);
                                String stat = dataSnapshot.child("team").child(team).child("case").child(number).child("stat").getValue(String.class);

                                int newCounterInt = Integer.parseInt(counterTaskUser) + 1;
                                String newCounter = Integer.toString(newCounterInt);
                                myRef.child("users").child(idUser).child("tasks").child(newCounter).child("done").setValue("false");
                                myRef.child("users").child(idUser).child("tasks").child(newCounter).child("href").setValue(href);
                                myRef.child("users").child(idUser).child("tasks").child(newCounter).child("id_image").setValue(id_image);
                                myRef.child("users").child(idUser).child("tasks").child(newCounter).child("info").setValue(infoOfPers);
                                myRef.child("users").child(idUser).child("tasks").child(newCounter).child("name").setValue(name);
                                myRef.child("users").child(idUser).child("tasks").child(newCounter).child("stat").setValue(stat);
                                myRef.child("users").child(idUser).child("tasks").child(newCounter).child("taskInfo").setValue(info);
                                myRef.child("users").child(idUser).child("tasks").child("counter").setValue(newCounter);
                                myRef.child("team").child(team).child("case").child(number).child("doneTeam").setValue("true");
                                Toast.makeText(AddTask.this, "Пожелаем удачи игроку", Toast.LENGTH_SHORT).show();
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                ad.setNegativeButton("Нет, вернуться назад", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.cancel();
                    }
                });
                ad.setCancelable(true);
                ad.show();
            }
        });


    }
}
