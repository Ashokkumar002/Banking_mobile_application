package com.example.banking_project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DepositActivity extends AppCompatActivity {
    EditText ed1, ed2;
    Button b1;
    SQLiteDatabase db, db1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        ed1 = findViewById(R.id.phno);
        ed2 = findViewById(R.id.amount);
        b1 = findViewById(R.id.clickme);

        // Open or create necessary databases
        db = openOrCreateDatabase("login", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS bank(phoneno INTEGER PRIMARY KEY, balance INTEGER)");
        db1 = openOrCreateDatabase("login", MODE_PRIVATE, null);
        db1.execSQL("CREATE TABLE IF NOT EXISTS history(sno INTEGER PRIMARY KEY, rno INTEGER, deposit INTEGER, withdrawal INTEGER)");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Check if EditText fields are empty
                    String phoneStr = ed1.getText().toString().trim();
                    String amountStr = ed2.getText().toString().trim();
                    if (phoneStr.isEmpty() || amountStr.isEmpty()) {
                        Toast.makeText(DepositActivity.this, "Please enter phone number and deposit amount", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Parse the phone number and deposit amount from EditText fields
                    long p = Long.parseLong(phoneStr);
                    int depositAmount = Integer.parseInt(amountStr);

                    // Query to check if phone number exists in the database
                    Cursor c = db.rawQuery("SELECT balance FROM bank WHERE phoneno = ?", new String[]{String.valueOf(p)});
                    if (c != null && c.moveToFirst()) {
                        // Phone number exists, update the balance
                        int currentBalance = c.getInt(0);
                        int newBalance = currentBalance + depositAmount;

                        // Update balance in the bank table
                        db.execSQL("UPDATE bank SET balance = ? WHERE phoneno = ?", new Object[]{newBalance, p});

                        // Insert deposit transaction into history table
                        db1.execSQL("INSERT INTO history(sno, rno, deposit, withdrawal) VALUES (?, ?, ?, ?)", new Object[]{p, "null", depositAmount, "null"});

                        Toast.makeText(DepositActivity.this, "Deposit successful", Toast.LENGTH_SHORT).show();
                    } else {
                        // Phone number doesn't exist, insert a new record
                        db.execSQL("INSERT INTO bank (phoneno, balance) VALUES (?, ?)", new Object[]{p, depositAmount});
                        db1.execSQL("INSERT INTO history(sno, rno, deposit, withdrawal) VALUES (?, ?, ?, ?)", new Object[]{p, "null", depositAmount, "null"});

                        Toast.makeText(DepositActivity.this, "New record inserted successfully", Toast.LENGTH_SHORT).show();
                    }

                    // Close the cursor
                    if (c != null) {
                        c.close();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(DepositActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
