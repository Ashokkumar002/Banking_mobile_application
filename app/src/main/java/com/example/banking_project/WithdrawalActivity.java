package com.example.banking_project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WithdrawalActivity extends AppCompatActivity {
    EditText ed1, ed2;
    Button b1;
    SQLiteDatabase db, db1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);

        ed1 = findViewById(R.id.phno);
        ed2 = findViewById(R.id.amount);
        b1 = findViewById(R.id.clickme);

        db = openOrCreateDatabase("login", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS bank(phoneno integer, balance integer)");
        db1 = openOrCreateDatabase("login", MODE_PRIVATE, null);
        db1.execSQL("CREATE TABLE IF NOT EXISTS history(sno integer,rno integer, deposit integer, withdrawal integer)");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    long ph = Long.parseLong(ed1.getText().toString().trim());
                    int withdrawalAmount = Integer.parseInt(ed2.getText().toString().trim());

                    String query = "SELECT phoneno FROM bank WHERE phoneno = ?";
                    Cursor c = db.rawQuery(query, new String[]{String.valueOf(ph)});

                    if (c != null && c.moveToFirst()) {
                        int currentBalance = c.getInt(0);

                        if (currentBalance >= withdrawalAmount) {
                            int newBalance = currentBalance - withdrawalAmount;

                            String updateBalanceQuery = "UPDATE bank SET balance = ? WHERE phoneno = ?";
                            db.execSQL(updateBalanceQuery, new Object[]{newBalance, ph});

                            String insertQuery1 = "INSERT INTO history(sno,rno, deposit, withdrawal) VALUES (?, ?, ?, ?)";
                            db1.execSQL(insertQuery1, new Object[]{ph, "null", "null", withdrawalAmount});

                            Toast.makeText(WithdrawalActivity.this, "Withdrawal successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(WithdrawalActivity.this, "Insufficient balance", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WithdrawalActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                    }

                    if (c != null) {
                        c.close();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(WithdrawalActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
