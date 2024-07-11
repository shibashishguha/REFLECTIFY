package com.example.journalapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.PopupMenu;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class JournalListActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private String currentUserId;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Journal");

    private List<Journal> journalList;

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        journalList = new ArrayList<>();
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            Intent i = new Intent(JournalListActivity.this, AddJournalActivity.class);
            startActivity(i);
        });

        authStateListener = firebaseAuth -> {
            user = firebaseAuth.getCurrentUser();
            if (user == null) {
                // User is not signed in
                Intent intent = new Intent(JournalListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                currentUserId = user.getUid();
                loadUserJournals();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Log.d("JournalListActivity", "Menu item clicked: " + itemId); // Debugging log
        if (itemId == R.id.action_settings) {
            View menuItemView = findViewById(R.id.action_settings);
            showSettingsMenu(menuItemView);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSettingsMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.mymenu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_signout) {
                logout();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void logout() {
        if (user != null && firebaseAuth != null) {
            firebaseAuth.signOut();
            Intent i = new Intent(JournalListActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    private void loadUserJournals() {
        db.collection("users").document(currentUserId).collection("journals").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    journalList.clear();
                    for (QueryDocumentSnapshot journals : queryDocumentSnapshots) {
                        Journal journal = journals.toObject(Journal.class);
                        journalList.add(journal);
                    }
                    myAdapter = new MyAdapter(JournalListActivity.this, journalList);
                    recyclerView.setAdapter(myAdapter);
                    myAdapter.notifyDataSetChanged();
                }).addOnFailureListener(e ->
                        Toast.makeText(JournalListActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}