package com.example.banking_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText ed1, ed2;
    private Button b1;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        ed1 = findViewById(R.id.editTextUsername);
        ed2 = findViewById(R.id.editTextPassword);
        b1 = findViewById(R.id.buttonLogin);

        // Set onClickListener for the TextView to navigate to registration activity
        TextView tvRegister = findViewById(R.id.textViewRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, new_reg.class);
                startActivity(intent);
            }
        });

        // Initialize database
        db = openOrCreateDatabase("login", MODE_PRIVATE, null);

        // Set onClickListener for the login button
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered username and password
                String u1 = ed1.getText().toString().trim(); // Trim whitespace
                String p1 = ed2.getText().toString().trim(); // Trim whitespace

                // Check if username or password is empty
                if (u1.isEmpty() || p1.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Perform login check
                String query = "SELECT uname, pass FROM new_user WHERE uname=?";
                Cursor c = db.rawQuery(query, new String[]{u1});
                if (c.moveToFirst()) {
                    String username = c.getString(0);
                    String password = c.getString(1);
                    if (password.equals(p1)&&(username.equals(u1))) {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, mainmenu.class);
                        intent.putExtra("user",username);
                        startActivity(intent);
                        finish(); // Finish current activity
                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "User invalid or new user register", Toast.LENGTH_SHORT).show();
                }
                c.close();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}