package com.bilalasiftwofive.roomvvm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bilalasiftwofive.roomvvm.R;

public class AddNoteActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.bilalasiftwofive.roomvvm.EXTRA_TITLE";

    public static final String EXTRA_DESCRIPTION = "com.bilalasiftwofive.roomvvm.EXTRA_DESCRIPTION";

    public static final String EXTRA_PRIORITY = "com.bilalasiftwofive.roomvvm.EXTRA_PRIORITY";

    private EditText mEditTextTitle;
    private EditText mEditTextDescription;
    private NumberPicker mNumberPickerPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        mEditTextTitle = findViewById(R.id.edit_text_title);
        mEditTextDescription = findViewById(R.id.edit_text_description);
        mNumberPickerPriority = findViewById(R.id.number_picker_priority);

        mNumberPickerPriority.setMinValue(1);
        mNumberPickerPriority.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Note");

    }

    private void saveNote() {
        String title = mEditTextTitle.getText().toString();
        String description = mEditTextDescription.getText().toString();
        int priority = mNumberPickerPriority.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intentData = new Intent();
        intentData.putExtra(EXTRA_TITLE, title);
        intentData.putExtra(EXTRA_DESCRIPTION, description);
        intentData.putExtra(EXTRA_PRIORITY, priority);
        setResult(RESULT_OK, intentData);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}