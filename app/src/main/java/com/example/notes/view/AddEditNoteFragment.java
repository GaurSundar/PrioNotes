package com.example.notes.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.notes.model.Note;
import com.example.notes.viewmodel.NoteViewModel;
import com.example.notes.R;

public class AddEditNoteFragment extends Fragment {

    private NoteViewModel noteViewModel;
    private EditText titleEditText;
    private EditText descEditText;
    private NumberPicker numberPicker;

    private int NoteId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_note, container, false);

        titleEditText = view.findViewById(R.id.edit_title);
        descEditText = view.findViewById(R.id.edit_desc);
        Button saveButton = view.findViewById(R.id.btn_save);
        numberPicker = view.findViewById(R.id.number_picker_priority);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10);

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        Bundle bundle = this.getArguments();
        if(bundle != null){

            requireActivity().setTitle("Edit Note");
            NoteId = bundle.getInt("id" , -1);
            titleEditText.setText(bundle.getString("title"));
            descEditText.setText(bundle.getString("description"));
            numberPicker.setValue(bundle.getInt("priority"));
        } else {
            requireActivity().setTitle("Add Note");
        }

        saveButton.setOnClickListener(view1 -> {
            saveNote();
        });

        return view;
    }

    private void saveNote() {
        String title = titleEditText.getText().toString();
        String description = descEditText.getText().toString();
        int priority = numberPicker.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(requireContext(), "Please insert Title and Description", Toast.LENGTH_SHORT).show();
            return;
        }

        Note note = new Note(title, description, priority);

        if (NoteId != -1) {
            note.setId(NoteId);
            try {
                noteViewModel.update(note);
                Toast.makeText(requireContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Failed to update note", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            try {
                noteViewModel.insert(note);
                Toast.makeText(requireContext(), "Note Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Failed to save note", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.fragment_container , homeFragment);
        fragmentTransaction.commit();
    }


}
