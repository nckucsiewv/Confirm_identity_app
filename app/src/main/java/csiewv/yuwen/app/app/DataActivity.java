package csiewv.yuwen.app.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends AppCompatActivity {

    private String chosenDepartment;
    private String chosenDate;

    public DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        chosenDate = "";

        Intent intent = this.getIntent();
        chosenDepartment = intent.getStringExtra("department");
        chosenDate = intent.getStringExtra("date");

        TextView textView = (TextView) findViewById(R.id.textView_first);
        textView.setText("finding : "+chosenDepartment+", date : "+chosenDate);

        myRef = FirebaseDatabase.getInstance().getReference();

        Log.d("myLog", chosenDate);
        getData(chosenDepartment, chosenDate);


    }

    private void getData(final String department, final String date){
        Query query = myRef.child("department").child(department).orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> competitorList = new ArrayList<String>();

                for(DataSnapshot dataElement : dataSnapshot.getChildren()){
                    competitorList.add(dataElement.getValue().toString());
                }

                for(final String id : competitorList){
                    Query checkQuery = myRef.child("users").child(id).orderByValue();
                    checkQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                LinearLayout layout = (LinearLayout) findViewById(R.id.layoutInScrollView);
                                TextView textView = new TextView(DataActivity.this);
                                String getID = id;
                                String getName = dataSnapshot.child("name").getValue().toString();
                                String getCheck = dataSnapshot.child("check").child(date).getValue().toString();
                                textView.setText(getID+" "+getName+" : "+getCheck);
                                textView.setTextSize(20);
                                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                layout.addView(textView);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
