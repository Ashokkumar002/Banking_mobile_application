package com.example.banking_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.app.AlertDialog;

import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;

public class new_reg extends AppCompatActivity {
    AlertDialog.Builder builder;
    Button b1;
    EditText una,pas,cpas,ema,phno;
    SQLiteDatabase db;
    String s1,s2,un,em;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reg);

        b1 = findViewById(R.id.buttonRegister);
        una = findViewById(R.id.editTextUsername);
        pas = findViewById(R.id.editTextPassword);
        cpas = findViewById(R.id.editTextConfirmPassword);
        ema = findViewById(R.id.editTextEmail);
        phno = findViewById(R.id.editTextPhoneNumber);
        builder = new AlertDialog.Builder(this);

        db = openOrCreateDatabase("login", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS new_user(uname VARCHAR(30),pass VARCHAR(30),conpass VARCHAR(30),email VARCHAR(30),phoneno INTEGER Primary key)");

        b1.setOnClickListener(v -> {
            s1 = pas.getText().toString().trim();
            s2 = cpas.getText().toString().trim();
            un = una.getText().toString().trim();
            em = ema.getText().toString().trim();
            String phone = phno.getText().toString().trim();

            if (un.isEmpty() || s1.isEmpty() || s2.isEmpty() || em.isEmpty() || phone.isEmpty()) {
                builder.setMessage("Please fill all fields");
                builder.show();
                return;
            }

            if (!s1.equals(s2)) {
                builder.setMessage("Passwords don't match");
                builder.show();
                return;
            }


            if (phone.length() != 10) {
                builder.setMessage("Phone number should be exactly 10 digits long");
                builder.show();
                return;
            }

            try {
                long p = Long.parseLong(phone);

                // Check if the phone number already exists in the database
                Cursor cursor = db.rawQuery("SELECT * FROM new_user WHERE phoneno = ?", new String[]{phone});
                if (cursor.getCount() > 0) {
                    builder.setMessage("Phone number already exists");
                    builder.show();
                    cursor.close();
                    return;
                }
                cursor.close();
                db.execSQL("INSERT INTO new_user VALUES('"+un+"','"+s1+"','"+s2+"','"+em+"',"+p+")");
                builder.setMessage("Record inserted to database");
                builder.show();
                Intent in = new Intent(new_reg.this, MainActivity.class);
                startActivity(in);
            } catch (NumberFormatException e) {
                builder.setMessage("Invalid phone number");
                builder.show();
            }
        });

    }
}
