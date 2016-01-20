package com.autokrat.preworktodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
    EditText etEditText;

    private final int REQUEST_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateArrayItems();
        lvItems = (ListView) findViewById(R.id.lvitems);
        lvItems.setAdapter(aToDoAdapter);
        etEditText = (EditText) findViewById(R.id.etEditText);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);

                aToDoAdapter.notifyDataSetChanged();
                writeItems();

                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditListItemActivity.class);

                i.putExtra("text",todoItems.get(position));
                i.putExtra("position",position);

                startActivityForResult(i,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String updatedText = data.getExtras().getString("update");
            int position = data.getExtras().getInt("position", 0);
            int code = data.getExtras().getInt("code", 0);

            if(code == 200){
                todoItems.set(position, updatedText);
                aToDoAdapter.notifyDataSetChanged();

                writeItems();

                // Toast the name to display temporarily on screen
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void populateArrayItems(){

        readItems();

        //Application was crashing as todoItems was null if no items were read/file doesnt exist
        if(todoItems == null){
            todoItems = new ArrayList<String>();
        }

        aToDoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, todoItems);
    }

    public void onAddItem(View view) {

        if(etEditText.getText().toString().trim().length()==0){
            Toast.makeText(this, "Empty field!", Toast.LENGTH_SHORT).show();
            return;
        }

        aToDoAdapter.add(etEditText.getText().toString());
        etEditText.setText("");
        writeItems();

        Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show();
    }

    private void readItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");

        try{
            todoItems = new ArrayList<String>(FileUtils.readLines(file));
        }catch(IOException e) {
            System.out.println("Error Reading Items");
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }

    private void writeItems(){
        File filesDir = getFilesDir();
        File file = new File(filesDir, "todo.txt");

        try{
            FileUtils.writeLines(file,todoItems);
        }catch(IOException e) {
            System.out.println("Error Writing Items");
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
        }
    }
}
