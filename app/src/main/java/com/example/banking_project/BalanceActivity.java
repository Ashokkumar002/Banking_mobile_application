package com.example.banking_project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BalanceActivity extends AppCompatActivity {
    EditText ed1;
    TextView balanceTextView;
    Button b1;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        ed1 = findViewById(R.id.phno);
        balanceTextView = findViewById(R.id.balance);
        b1 = findViewById(R.id.clickme);

        db = openOrCreateDatabase("login", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS bank(phoneno integer, balance integer)");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Long ph = Long.parseLong(ed1.getText().toString().trim());

                    String query = "SELECT balance FROM bank WHERE phoneno = ?";
                    Cursor c = db.rawQuery(query, new String[]{String.valueOf(ph)});

                    if (c != null && c.moveToFirst()) {
                        int balance = c.getInt(0);
                        balanceTextView.setText("Your Balance: " + balance);
                    } else {
                        balanceTextView.setText("No data found for the given phone number");
                    }

                    if (c != null) {
                        c.close();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(BalanceActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}