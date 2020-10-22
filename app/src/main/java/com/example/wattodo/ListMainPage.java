package com.example.wattodo;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListMainPage extends AppCompatActivity {

    DatabaseReference databaseReference;
    cardViewAdapter cardViewAdapter;
    List<TaskClass> taskClassList;
    RecyclerView recyclerView;
    String name;
    final public String TAG = ListMainPage.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_main_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 addTask();

            }
        });

        taskClassList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        createRecyclerView();
    }

    private void createRecyclerView() {
            name = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Toast.makeText(this, "Fetching data filling recyclerView", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Inside createRecyclerView: "+name);
            databaseReference = FirebaseDatabase.getInstance().getReference("Task").child(name);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren()) {
                            TaskClass taskData = ds.getValue(TaskClass.class);
                            String chckbox = taskData.getCheckbox();
                             if(chckbox.equals("false"))
                                 taskClassList.add(taskData);
                        }
                        setAdapterView();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    private void setAdapterView() {
        cardViewAdapter = new cardViewAdapter();
        cardViewAdapter.setTaskList(taskClassList);
        recyclerView.setAdapter(cardViewAdapter);
        cardViewAdapter.setOnItemClickListener(new cardViewAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                TaskClass tc = cardViewAdapter.getAndDeletePos(position);
                String id = String.valueOf(tc.getId());
                databaseReference.child(id).removeValue();
                cardViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCheckBoxClick(int position) {
                TaskClass tc = cardViewAdapter.getAndDeletePos(position);

                String id = String.valueOf(tc.getId());
                //Log.d(TAG, "onCheckBoxClick: "+name+" "+id+" "+tc.getCheckbox());
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                rootRef.child("Task").child(name).child(id).child("checkbox").setValue("true");
            }
        });
    }

    private void addTask() {
        startActivity(new Intent(ListMainPage.this, createTask.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
         if(id == R.id.past_reminder) {
                ////new activity
                startActivity(new Intent(ListMainPage.this,PastReminder.class));
                finish();
         }
         if(id == R.id.signOut)
          {
              FirebaseAuth.getInstance().signOut();
              startActivity(new Intent(ListMainPage.this,MainActivity.class));
          }
        return super.onOptionsItemSelected(item);
    }
}