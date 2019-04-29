package com.example.springhack;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;
import java.util.List;

public class SmallBoss extends AppCompatActivity {

    private TextView heroName;
    private TextView heroInfo;
    private ImageView imageView;
    private TextView taskInfo;
    private ProgressBar xp;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

    private String team;

    private String userId;

    private ListView listView;

    private ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
    private HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_small_boss);

        heroName = findViewById(R.id.heroName);
        heroInfo = findViewById(R.id.heroInfo);
        taskInfo = findViewById(R.id.taskInfo);
        xp = findViewById(R.id.progressBar3);
        imageView = findViewById(R.id.logo);
        listView = findViewById(R.id.listView1);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SmallBoss.this);
        userId = preferences.getString("id", null);

        myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                team = dataSnapshot.child("users").child(userId).child("team").getValue(String.class);

                heroInfo.setText("Информация о персонаже: " + dataSnapshot.child("team").child(team).child("case").child("info").getValue(String.class));
                heroName.setText("Имя персонажа: " + dataSnapshot.child("team").child(team).child("case").child("name").getValue(String.class));
                taskInfo.setText("Задача: " + dataSnapshot.child("team").child(team).child("case").child("taskInfo").getValue(String.class));
                String imageUri = dataSnapshot.child("team").child("team1").child("case").child("0").child("id_image").getValue(String.class);
                FirebaseStorage storage = FirebaseStorage.getInstance();
                if (imageUri != null && imageUri != "") {
                    StorageReference storageRef = storage.getReferenceFromUrl(imageUri);
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(SmallBoss.this).load(uri).into(imageView);
                        }
                    });
                }
                int sum = 0;
                String counter = dataSnapshot.child("team").child(team).child("users").child("counter").getValue(String.class);
                for (int i = 0; i < Integer.parseInt(counter) + 1; i++) {
                    sum += Integer.parseInt(dataSnapshot.child("team").child(team).child("users").child(i + "").child("xp").getValue(String.class));
                    map = new HashMap<>();
                    map.put("Name", dataSnapshot.child("team").child(team).child("users").child(i + "").child("name").getValue(String.class));
                    map.put("Xp", dataSnapshot.child("team").child(team).child("users").child(i + "").child("xp").getValue(String.class));
                    arrayList.add(map);
                }

                SimpleAdapter adapter = new SimpleAdapter(SmallBoss.this, arrayList, android.R.layout.simple_list_item_2,
                        new String[]{"Name", "Xp"},
                        new int[]{android.R.id.text1, android.R.id.text2});
                listView.setAdapter(adapter);

                xp.setProgress(Integer.parseInt(dataSnapshot.child("team").child(team).child("case").child("stat").getValue(String.class)) - sum);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
