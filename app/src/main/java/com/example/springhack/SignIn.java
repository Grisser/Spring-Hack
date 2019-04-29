package com.example.springhack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    String password;
    String login;
    Button signIn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TextView regLink = (TextView) findViewById(R.id.regLink);
        SpannableString content = new SpannableString(getString(R.string.firstly));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        regLink.setText(content);

        mAuth = FirebaseAuth.getInstance();

        login = ((EditText) findViewById(R.id.Email)).getText().toString();
        password = ((EditText) findViewById(R.id.Password)).getText().toString();

        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, Profile.class);
                startActivity(intent);

            }
        });

        regLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);

            }
        });

    }
}
