package com.example.notes.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.model.PreferenceManager;
import com.example.notes.viewmodel.NoteViewModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {


    private GoogleApiClient mGoogleApiClient;

    private PreferenceManager preferenceManager;
    private HomeFragment homeFragment;

    private NoteViewModel noteViewModel;
    private SignInFragment signInFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        preferenceManager = new PreferenceManager(this);
        homeFragment = new HomeFragment();
        signInFragment = new SignInFragment();

        if (preferenceManager.isLoggedIn()) {

            //User is Logged In
            ReplaceFragmentWith(homeFragment);
        } else {

            // User is not logged in, handle login flow
            ReplaceFragmentWith(signInFragment);

        }

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);


    }
   /* private void ShowSnackBar(){

        Snackbar snackbar = Snackbar.make( , "Note Deleted" , Snackbar.LENGTH_SHORT)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                    }
                });
        snackbar.show();

    }*/

    private void ReplaceFragmentWith(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container , fragment);
        fragmentTransaction.commit();

    }

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

            case R.id.action_sign_out:
                signOut();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {

        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback( status -> {
            // Sign-out was successful

            preferenceManager.resetUserDetails();
            Toast.makeText(MainActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
            ReplaceFragmentWith(signInFragment);
        });

    }
}