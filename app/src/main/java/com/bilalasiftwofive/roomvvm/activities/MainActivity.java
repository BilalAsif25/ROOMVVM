package com.bilalasiftwofive.roomvvm.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bilalasiftwofive.roomvvm.model.Note;
import com.bilalasiftwofive.roomvvm.adapters.NoteAdapter;
import com.bilalasiftwofive.roomvvm.viewmodel.NoteViewModel;
import com.bilalasiftwofive.roomvvm.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private NoteViewModel noteViewModel;
    private RecyclerView mRecyclerviewMain;
    private FloatingActionButton mAddNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAddNoteBtn = findViewById(R.id.addNoteBtn);
        mAddNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });
        mRecyclerviewMain = findViewById(R.id.recyclerview_main);
        mRecyclerviewMain.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerviewMain.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter();
        mRecyclerviewMain.setAdapter(noteAdapter);

        noteViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //update reyclerview later
                noteAdapter.submitList(notes);
                //  Toast.makeText(MainActivity.this, "onChanged", Toast.LENGTH_SHORT).show();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mRecyclerviewMain);

        noteAdapter.setonItemclicklistener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title;
            String description;
            int priority;

            if (data != null) {
                title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
                priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 0);
                Note note = new Note(title, description, priority);
                noteViewModel.insert(note);
                Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title;
            String description;
            int priority;
            int id;

            if (data != null) {
                id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);
                if (id == -1) {
                    Toast.makeText(this, "Cant update Note", Toast.LENGTH_SHORT).show();
                    return;
                }

                title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
                priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 0);
                Note note = new Note(title, description, priority);
                note.setId(id);
                noteViewModel.update(note);
                Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}