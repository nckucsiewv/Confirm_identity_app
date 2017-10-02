package com.example.confirm_identity_app.confirm_identity_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // [START declare_database_ref]
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    // [START declare UI component reference]
    private TextView str;
    // [END declare UI component reference]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [START link UI component by id]
        str = (TextView) findViewById(R.id.tvForDisplay);
        // [END link UI component by id]

        // [START write database example]
        mDatabase = database.getReference("users/F74004088/check/20171104");
        mDatabase.setValue("0");
        // [END write database example]

        // Read from the database
        mDatabase = database.getReference("users");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                Log.d("myLog", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("myLog", "Failed to read value.", error.toException());
            }
        });
    }

}
