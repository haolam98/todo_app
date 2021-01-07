package com.example.todo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {
    EditText editText;
    Button bttn_save;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        editText =findViewById(R.id.editText_editName);
        bttn_save =findViewById(R.id.button_save);
        getSupportActionBar().setTitle("Edit Item");

        //retrieve data from main activity
        editText.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        position = getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION);
    }

    public void handle_saveItem(View view) {
        //when user is done editing
        //create intent containing the modified result that user typed in
        Intent intent = new Intent();
        // pass result
        intent.putExtra(MainActivity.KEY_ITEM_TEXT,editText.getText().toString());
        intent.putExtra(MainActivity.KEY_ITEM_POSITION, position);
        //set result
        setResult(RESULT_OK,intent);
        // finish activity
        finish();

    }
}