package com.example.notes.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.notes.model.Note;
import com.example.notes.viewmodel.NoteViewModel;
import com.example.notes.R;
import com.example.notes.model.PreferenceManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public class HomeFragment extends Fragment {

    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private NoteViewModel noteViewModel;
    private PreferenceManager preferenceManager;

    private AddEditNoteFragment addEditNoteFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        requireActivity().setTitle(R.string.app_name);
        preferenceManager = new PreferenceManager(requireContext());
        addEditNoteFragment = new AddEditNoteFragment();

        coordinatorLayout = view.findViewById(R.id.coordinate);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton actionButton = view.findViewById(R.id.floating_button);
        actionButton.setOnClickListener(view1 -> {

            ReplaceFragmentWith(addEditNoteFragment);
        });

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                showSnackBar();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(note -> {

            Bundle bundle = new Bundle();
            bundle.putInt("id", note.getId());
            bundle.putString("title", note.getTitle());
            bundle.putString("description", note.getDesc());
            bundle.putInt("priority", note.getPriority());
            addEditNoteFragment.setArguments(bundle);

            ReplaceFragmentWith(addEditNoteFragment);
        });

        return view;
    }

    private void ReplaceFragmentWith(Fragment fragment) {

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container , fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    
    private void showSnackBar(){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Note Deleted", Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbar.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
        snackbarView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.red)); // Assuming you have defined red color in your resources
        snackbar.show();

    }


}