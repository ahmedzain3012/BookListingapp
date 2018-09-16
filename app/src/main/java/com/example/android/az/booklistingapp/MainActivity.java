package com.example.android.az.booklistingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button = (Button) findViewById(R.id.search_button);

        final EditText searchBook = findViewById(R.id.search_book);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent activityChangeIntent = new Intent(MainActivity.this, BookActivity.class);
                // send intent key search with value entered by user
                activityChangeIntent.putExtra("key_search",searchBook.getText().toString());

                MainActivity.this.startActivity(activityChangeIntent);
            }
        });

    }


}
