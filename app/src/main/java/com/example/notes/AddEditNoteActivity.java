package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.Objects;

public class AddEditNoteActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.example.notes.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.notes.EXTRA_TITLE";
    public static final String EXTRA_DESC = "com.example.notes.EXTRA_DESC";
    public static final String EXTRA_PRIORITY = "com.example.notes.EXTRA_PRIORITY";
    private EditText titleEditText;
    private EditText descEditText;
    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        titleEditText = findViewById(R.id.edit_title);
        descEditText = findViewById(R.id.edit_desc);
        numberPicker = findViewById(R.id.number_picker_priority);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            titleEditText.setText(intent.getStringExtra(EXTRA_TITLE));
            descEditText.setText(intent.getStringExtra(EXTRA_DESC));
            numberPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY , 1));
        }else {
        setTitle("Add Note");
        }
    }

    private void saveNote() {

        String title = titleEditText.getText().toString();
        String description = descEditText.getText().toString();
        int priority = numberPicker.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please insert Title and Description", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = getIntent().getIntExtra(EXTRA_ID , -1);

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE , title);
        data.putExtra(EXTRA_DESC , description);
        data.putExtra(EXTRA_PRIORITY , priority);

        if (id != -1){
            data.putExtra(EXTRA_ID , id);
        }

        setResult(RESULT_OK , data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note , menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}