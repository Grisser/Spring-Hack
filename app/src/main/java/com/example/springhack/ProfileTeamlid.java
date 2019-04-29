package com.example.springhack;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileTeamlid extends AppCompatActivity {
    String heroName;
    String heroInfo;
    Button fight;
    TextView tv_enemy;
    TextView tv_smallBoss;
    TextView tv_bigBoss;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;

    private TextView heroNameTV;
    private ProgressBar heroXP;
    private ProgressBar bestHeroXp;
    private TextView heroInfoTV;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_teamlid);


        heroName = ((TextView) findViewById(R.id.heroName)).getText().toString();
        heroInfo = ((TextView) findViewById(R.id.heroInfo)).getText().toString();

        heroNameTV = findViewById(R.id.heroName);
        heroInfoTV = findViewById(R.id.heroInfo);
        heroXP = findViewById(R.id.progressBar);
        bestHeroXp = findViewById(R.id.mainenprog);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileTeamlid.this);
        userId = preferences.getString("id", null);


        myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mName = dataSnapshot.child("users").child(userId).child("name").getValue(String.class);
                String mHeroName = dataSnapshot.child("users").child(userId).child("hero_main").child("name").getValue(String.class);
                String mHeroInfo = dataSnapshot.child("users").child(userId).child("hero_main").child("info").getValue(String.class);
                heroNameTV.setText("Имя: " + mName);
                heroInfoTV.setText("Имя героя: " + mHeroName + "\nИнформация: " + mHeroInfo);

                String team = dataSnapshot.child("users").child(userId).child("team").getValue(String.class);
                int i1 = Integer.parseInt(dataSnapshot.child("users").child(userId).child("stat").getValue(String.class));
                int i2 = Integer.parseInt(dataSnapshot.child("team").child(team).child("case").child("stat").getValue(String.class));
                int i = i1 * 100 / i2;
                heroXP.setProgress(i);
                bestHeroXp.setProgress(Integer.parseInt(dataSnapshot.child("users").child(userId).child("topStat").getValue(String.class)));
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        CardView card = findViewById(R.id.card_view);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileTeamlid.this, Artefact.class);
                startActivity(intent);
            }
        });
        Button Case = findViewById(R.id.case1);
        Case.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileTeamlid.this, Tasks.class);
                startActivity(intent);
            }
        });

        Button dar = findViewById(R.id.button_dar);
        dar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileTeamlid.this, Exhange.class);
                startActivity(intent);
            }
        });

        fight = findViewById(R.id.button_fight);
        fight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileTeamlid.this);
                final View view = View.inflate(ProfileTeamlid.this, R.layout.dialog_select_boss, null);
                builder.setView(view);
                final AlertDialog show = builder.show();
                tv_enemy = view.findViewById(R.id.enemy);
                tv_smallBoss = view.findViewById(R.id.enemySmallBoss);
                tv_bigBoss = view.findViewById(R.id.enemyBigBoss);

                tv_bigBoss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ProfileTeamlid.this, BigBoss.class);
                        startActivity(intent);
                        show.dismiss();
                    }
                });
                tv_smallBoss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ProfileTeamlid.this, SmallBoss.class);
                        startActivity(intent);
                        show.dismiss();
                    }
                });
                tv_enemy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(ProfileTeamlid.this, BeforeEnemy.class);
                        intent.putExtra("act", "1");
                        startActivity(intent);
                        show.dismiss();
                    }
                });

            }
        });
    }

}