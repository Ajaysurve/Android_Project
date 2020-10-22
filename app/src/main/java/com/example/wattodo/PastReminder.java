package com.example.wattodo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PastReminder extends AppCompatActivity {

    DatabaseReference databaseReference;
    cardViewAdapterPastReminder cardViewAdapterPastReminder;
    List<TaskClass> taskClassListPast;
    RecyclerView recyclerView;
    String name;
    public static final String TAG = PastReminder.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_reminder);
        Toolbar toolbar = findViewById(R.id.toolbar_past);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        taskClassListPast = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view_past);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        fillRecyclerList();
    }

    private void fillRecyclerList() {
        name = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Toast.makeText(this, "Fetching data filling recyclerView", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "Inside createRecyclerView: "+name);
        databaseReference = FirebaseDatabase.getInstance().getReference("Task").child(name);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()) {
                    TaskClass taskData = ds.getValue(TaskClass.class);

                      if(taskData.checkbox.equals("true"))  {
                          Log.d(TAG, "onDataChange: Adding data to list");
                          taskClassListPast.add(taskData); }
                      else {
                          Log.d(TAG, "onDataChange: taskData.isCheckbox() == true   is failing");
                      }
                }
                setPastAdapterView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void setPastAdapterView() {
        cardViewAdapterPastReminder = new cardViewAdapterPastReminder();
        cardViewAdapterPastReminder.setTaskListPast(taskClassListPast);
        recyclerView.setAdapter(cardViewAdapterPastReminder);
        cardViewAdapterPastReminder.setOnItemClickListener(new cardViewAdapterPastReminder.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                TaskClass tc = cardViewAdapterPastReminder.getAndDeletePos(position);
                Log.d(TAG, "Inside SetAdapter for view"+position+" "+ " removing Child");
                String id = String.valueOf(tc.getId());
                //String name = FirebaseAuth.getInstance().getCurrentUser().getUid();

                Log.d(TAG, "onDeleteClick: "+name +" id: "+tc.getId());
                databaseReference.child(id).removeValue();
                cardViewAdapterPastReminder.notifyDataSetChanged();
                //createRecyclerView();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_past_reminder,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.back) {
            ////new activity
            startActivity(new Intent(PastReminder.this,ListMainPage.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}