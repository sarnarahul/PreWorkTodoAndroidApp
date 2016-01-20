package com.autokrat.preworktodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditListItemActivity extends AppCompatActivity {

    EditText multiLineText;
    int itemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list_item);

        multiLineText = (EditText) findViewById(R.id.etUpdateText);

        String itemText = getIntent().getStringExtra("text");
        itemPosition = getIntent().getIntExtra("position", 0);

        multiLineText.setText(itemText);
        multiLineText.setSelection(multiLineText.getText().length());
    }

    public void onSaveItem(View view) {

        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("update", multiLineText.getText().toString());
        data.putExtra("position", itemPosition);

        data.putExtra("code", 200);
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // setting result code and bundle data for response
        finish();
    }
}
