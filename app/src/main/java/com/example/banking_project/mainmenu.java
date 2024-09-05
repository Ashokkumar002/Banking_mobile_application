package com.example.banking_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class mainmenu extends AppCompatActivity {
    Button balance, TransferMoney, Deposit, Withdrawal, History;
    SQLiteDatabase db;
    long phonenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        balance = findViewById(R.id.buttonAccountBalance);
        TransferMoney = findViewById(R.id.buttonTransferMoney);
        Deposit = findViewById(R.id.buttonDeposit);
        Withdrawal = findViewById(R.id.buttonWithdrawal);
        History = findViewById(R.id.buttonHistory);

        // Get the username from the intent
        String username = getIntent().getStringExtra("user");

        // Initialize database
        db = openOrCreateDatabase("login", MODE_PRIVATE, null);

        // Query the database to get the phone number
        Cursor c = db.rawQuery("SELECT phoneno FROM new_user WHERE uname = ?", new String[]{username});
        if (c.moveToFirst()) {
            phonenum = c.getLong(0); // Retrieve the phone number from the first column
        }
        c.close();

        // Set click listeners for buttons
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this, BalanceActivity.class);
                startActivity(intent);
            }
        });

        TransferMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this, TransferMoneyActivity.class);
                intent.putExtra("phone",phonenum);
                startActivity(intent);
            }
        });

        Deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this, DepositActivity.class);
                
                startActivity(intent);
            }
        });

        Withdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this, WithdrawalActivity.class);
                intent.putExtra("phone",phonenum);
                startActivity(intent);
            }
        });

        History.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this, TransactionHistoryActivity.class);
                intent.putExtra("phone",phonenum);
                startActivity(intent);
            }
        });
    }
}
