package com.example.notes.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.model.PreferenceManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;

public class SignInFragment extends Fragment {

    private static final int RC_SIGN_IN = 123;
    private GoogleSignInOptions gso;
    private GoogleApiClient mGoogleApiClient;

    private PreferenceManager preferenceManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_in, container, false);

        preferenceManager = new PreferenceManager(requireContext());


        // Configure Google Sign-In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(requireContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        SignInButton signInButton = view.findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                Toast.makeText(requireContext() , "Clicked" , Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            String username = account.getDisplayName();
            String userEmail = account.getEmail();
            String profilePic = String.valueOf(account.getPhotoUrl());

            preferenceManager.setLoggedInUserdetails(true, userEmail, username, profilePic);

            HomeFragment homeFragment = new HomeFragment();

            // Begin the fragment transaction
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, homeFragment);

            // Commit the transaction
            transaction.commit();


        } catch (ApiException e) {

            // The ApiException status code indicates the detailed failure reason.
            e.printStackTrace();
            Toast.makeText(requireContext(), "Sign In Failed", Toast.LENGTH_SHORT).show();
        }

    }
}