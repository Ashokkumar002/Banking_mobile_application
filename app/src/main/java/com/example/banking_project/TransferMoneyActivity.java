package com.example.banking_project;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TransferMoneyActivity extends AppCompatActivity {
    Button b1;
    SQLiteDatabase db, db1;
    EditText senderno, reciverno, sendamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);
        senderno = findViewById(R.id.snumber);
        reciverno = findViewById(R.id.rnumber);
        sendamount = findViewById(R.id.amount);
        b1 = findViewById(R.id.clickme); // Initialize the button
        db = openOrCreateDatabase("login", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS bank(phoneno INTEGER, balance INTEGER)");
        db1 = openOrCreateDatabase("login", MODE_PRIVATE, null);
        db1.execSQL("CREATE TABLE IF NOT EXISTS history(sno INTEGER, rno INTEGER, deposit INTEGER, withdrawal INTEGER)");

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int sendph = Integer.parseInt(senderno.getText().toString().trim());
                    int reciveph = Integer.parseInt(reciverno.getText().toString().trim());
                    int amount = Integer.parseInt(sendamount.getText().toString().trim());

                    Cursor c = db.rawQuery("SELECT phoneno FROM new_user WHERE phoneno = ?", new String[]{String.valueOf(sendph)});
                    Cursor c1 = db1.rawQuery("SELECT phoneno FROM new_user WHERE phoneno = ?", new String[]{String.valueOf(reciveph)});

                    if (c != null && c.moveToFirst() && c1 != null && c1.moveToFirst()) {
                        int currentbalance = 0, currentbalance2 = 0;

                        Cursor cursor = db.rawQuery("SELECT balance FROM bank WHERE phoneno = ?", new String[]{String.valueOf(sendph)});
                        if (cursor != null && cursor.moveToFirst()) {
                            currentbalance = cursor.getInt(0);
                        } else {
                            Toast.makeText(TransferMoneyActivity.this, "insufficient balance to make transaction!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        cursor = db.rawQuery("SELECT balance FROM bank WHERE phoneno = ?", new String[]{String.valueOf(reciveph)});
                        if (cursor != null && cursor.moveToFirst()) {
                            currentbalance2 = cursor.getInt(0);
                        }

                        int newbalance = currentbalance - amount;
                        int newbalance2 = currentbalance2 + amount;

                        String insertQuery = "UPDATE bank SET balance = ? WHERE phoneno = ?";
                        db.execSQL(insertQuery, new String[]{String.valueOf(newbalance), String.valueOf(sendph)});
                        String insertQuery3 = "UPDATE bank SET balance = ? WHERE phoneno = ?";
                        db.execSQL(insertQuery3, new String[]{String.valueOf(newbalance2), String.valueOf(reciveph)});
                        String insertQuery4 = "INSERT INTO history(sno,rno,deposit,withdrawal) VALUES (?,?,?,?)";
                        db1.execSQL(insertQuery4, new String[]{String.valueOf(sendph), String.valueOf(reciveph), String.valueOf(amount), "null"});

                        Toast.makeText(TransferMoneyActivity.this, "Transferred Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(TransferMoneyActivity.this, "Invalid phone numbers", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(TransferMoneyActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(TransferMoneyActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }
}