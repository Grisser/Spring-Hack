package com.example.springhack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

public class Review extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        RadioButton radioButton = (RadioButton) findViewById(R.id.radioButton2);
        radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (compoundButton.isChecked()) {

                    findViewById(R.id.textView13).setVisibility(View.VISIBLE);
                    findViewById(R.id.seekBar).setVisibility(View.VISIBLE);

                } else {

                    findViewById(R.id.textView13).setVisibility(View.GONE);
                    findViewById(R.id.seekBar).setVisibility(View.GONE);

                }

            }
        });

    }
}
