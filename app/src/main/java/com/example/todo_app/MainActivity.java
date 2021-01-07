package com.example.todo_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public  static final String KEY_ITEM_TEXT = "item_text";
    public  static final String KEY_ITEM_POSITION = "item_position";
    public  static final int EDIT_TEXT_CODE = 20;

    List<String> items;
    Button bttn_add;
    EditText editText_itemName;
    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttn_add = findViewById(R.id.button_add);
        editText_itemName = findViewById(R.id.editTextText_itemName);
        recyclerView = findViewById(R.id.list_item);

        loadItems();
//        items = new ArrayList<>();
//        items.add("Buy Milk");
//        items.add("Go to gym");

        ItemAdapter.OnClickListener onClickListener = new ItemAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity","Single Tap At Position: "+ position);
                //Create new activity
                Intent i = new Intent(MainActivity.this,EditActivity.class);
                //Pass data to activity
                i.putExtra(KEY_ITEM_TEXT,items.get(position));
                i.putExtra(KEY_ITEM_POSITION,position);
                //Display activity
                startActivityForResult(i,EDIT_TEXT_CODE);
            }
        };
        ItemAdapter.OnLongClickListener onLongClickListener = new ItemAdapter.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //delete item from the list
                items.remove(position);
                //notify adapter that item was deleted
                itemAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        itemAdapter = new ItemAdapter(items,onLongClickListener, onClickListener);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void handle_addItem(View view) {
        String new_item = editText_itemName.getText().toString();
        items.add(new_item);
        itemAdapter.notifyItemInserted(items.size()-1);
        editText_itemName.setText("");
        Toast.makeText(getApplicationContext(),"Item was added", Toast.LENGTH_SHORT).show();
        saveItems();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //handle result of the activity
        super.onActivityResult(requestCode, resultCode, data);
        //check the request code is matched
        if (resultCode == RESULT_OK && requestCode== EDIT_TEXT_CODE)
        {
            //retrieve updated text value
            String updated_text = data.getStringExtra(KEY_ITEM_TEXT);
            //update the value to the list
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update model
            items.set(position,updated_text);
            //notify adapter
            itemAdapter.notifyItemChanged(position);
            //save change to persist data file
            saveItems();
            Toast.makeText(getApplicationContext(),"Item was updated successfully!", Toast.LENGTH_SHORT).show();

        }
        else {
            Log.d("MainActivity","Unknown call to onActivityResult");
        }
    }

    private File getDataFile()
    {
        return new File(getFilesDir(),"data.txt");

    }

    private void loadItems()
    {//Function: read data file and load items to list
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(),Charset.defaultCharset()));
        } catch (IOException e)
        {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems()
    {//Function: save items to file by write them into the data file
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writting items", e);

        }
    }
}