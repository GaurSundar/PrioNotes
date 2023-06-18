package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    public static final int ADD_REQUEST = 1;
    public static final int EDIT_REQUEST = 2;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coordinatorLayout = findViewById(R.id.coordinate);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton actionButton = findViewById(R.id.floating_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , AddEditNoteActivity.class);
                startActivityForResult(intent , ADD_REQUEST);

            }
        });
        actionButton.setBackgroundColor(getResources().getColor(R.color.pinkish));

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                 adapter.submitList(notes);
            }
        });



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0 ,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
//                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
//                ShowSnackBar();
            }

        }).attachToRecyclerView(recyclerView);


        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {

            @Override
            public void OnItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this , AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESC, note.getDesc());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, EDIT_REQUEST);
            }


        });
    }
//    private void ShowSnackBar(){
//
//        Snackbar snackbar = Snackbar.make(coordinatorLayout , "Note Deleted" , Snackbar.LENGTH_SHORT)
//                .setAction("UNDO", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//
//                    }
//                });
//        snackbar.show();
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main , menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.main :
                noteViewModel.deleteAllNotes();
                Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_REQUEST && resultCode == RESULT_OK) {
        String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
        String desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESC);
        int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY , 1);

        Note note = new Note(title , desc , priority);

        noteViewModel.insert(note);

        Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show();
        }else if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {

            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESC);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY , 1);
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID , -1);

            if (id == -1)
                Toast.makeText(this, "Note Can't be Updated", Toast.LENGTH_SHORT).show();

            Note note = new Note(title , desc , priority);
            note.setId(id);
            noteViewModel.update(note);
        }
        else
            Toast.makeText(this, "Note not Saved", Toast.LENGTH_SHORT).show();
    }
}