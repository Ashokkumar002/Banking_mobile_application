package com.example.banking_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryActivity extends AppCompatActivity {
    Button bb1;
    EditText e1;
    SQLiteDatabase db;
    int ph;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        e1 = findViewById(R.id.editTextPhoneNumber);
        bb1 = findViewById(R.id.buttonSearch);
        tv = findViewById(R.id.Transactions);
        db = openOrCreateDatabase("login", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS history(sno integer,rno integer, deposit integer, withdrawal integer)");

        bb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumberString = e1.getText().toString().trim();
                if (phoneNumberString.isEmpty()) {
                    Toast.makeText(TransactionHistoryActivity.this, "Please enter a phone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    // No need for try-catch since phoneNumberString is a string
                } catch (NumberFormatException e) {
                    Toast.makeText(TransactionHistoryActivity.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                int phone = Integer.parseInt(phoneNumberString);
                String query = "SELECT * FROM history WHERE sno = ?";
                Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(phone)});

                if (cursor != null && cursor.moveToFirst()) {
                    StringBuilder historyBuilder = new StringBuilder();
                    do {
                        int phoneNumber = cursor.getInt(0);
                        int receiverNumber = cursor.getInt(1);
                        int depositAmount = cursor.getInt(2);
                        int withdrawalAmount = cursor.getInt(3);

                        historyBuilder.append("Phone Number: ").append(phoneNumber)
                                .append(", Receiver Number: ").append(receiverNumber)
                                .append(", Deposit Amount: ").append(depositAmount)
                                .append(", Withdrawal Amount: ").append(withdrawalAmount)
                                .append("\n");
                    } while (cursor.moveToNext());

                    tv.setText(historyBuilder.toString());
                } else {
                    Toast.makeText(TransactionHistoryActivity.this, "No transaction history found", Toast.LENGTH_SHORT).show();
                }

                if (cursor != null) {
                    cursor.close();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}