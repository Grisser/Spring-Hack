package com.example.springhack;

import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class EnemyLeft extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enemy_left);

        Toast.makeText(EnemyLeft.this, "Прикоснись к великому!", Toast.LENGTH_SHORT).show();

        ImageView img = findViewById(R.id.imageView5);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EnemyLeft.this);
                builder.setTitle("Интересный факт")
                        .setMessage("Вы знали, что, Танос благодаря своим камням бесконечности может поднять молот Тора?!")
                        .setCancelable(false)
                        .setNegativeButton("ОК, спасибо",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
            }


        });
    }
}
