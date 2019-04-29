package com.example.springhack;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.Random;

public class Hero extends AppCompatActivity {
    private TextView heroName;
    private TextView heroInfo;
    private String name;
    private String info;

    private String imageUri;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String type;
    private int count;

    private ImageView imageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);
        heroName =  findViewById(R.id.heroName);
        heroInfo =  findViewById(R.id.heroInfo);

        imageView = findViewById(R.id.imageView2);

        user = mAuth.getInstance().getCurrentUser();

        TextView regCont = (TextView) findViewById(R.id.regCont);
        SpannableString content = new SpannableString(getString(R.string.firstly));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        regCont.setText(content);

        regCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals("teamLead")){
                    Intent intent = new Intent(Hero.this, ProfileTeamlid.class);
                    startActivity(intent);
                }
                else{
                    if (type.equals("user")){
                        Intent intent = new Intent(Hero.this, Profile.class);
                        startActivity(intent);
                    }
                }



            }
        });

        final Random random = new Random();

        myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = Integer.parseInt(dataSnapshot.child("hero").child("counter").getValue(String.class));
                final String numberOfHero = random.nextInt(count+1)+"";
                name = dataSnapshot.child("hero").child(numberOfHero).child("name").getValue(String.class);
                myRef.child("users").child(user.getUid()).child("hero").setValue("true");
                info = dataSnapshot.child("hero").child(numberOfHero).child("info").getValue(String.class);

                imageUri = dataSnapshot.child("hero").child(numberOfHero).child("id_image").getValue(String.class);

                type = dataSnapshot.child("users").child(user.getUid()).child("type").getValue(String.class);

                heroName.setText(name);
                heroInfo.setText(info);
                myRef.child("users").child(user.getUid()).child("hero_main").child("name").setValue(name);
                myRef.child("users").child(user.getUid()).child("hero_main").child("info").setValue(info);
                myRef.child("users").child(user.getUid()).child("hero_main").child("id_image").setValue(imageUri);

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReferenceFromUrl(imageUri);
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(Hero.this).load(uri).into(imageView);
                    }
                });

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
