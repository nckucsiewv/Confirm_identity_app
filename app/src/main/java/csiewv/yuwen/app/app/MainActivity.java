package csiewv.yuwen.app.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    public DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    // called when users clicking the 'check id' btn
    public void sendCheckingID(View view){
        EditText editText = (EditText) findViewById(R.id.editText_num);
        checkID(editText.getText().toString());
    }

    // check if the id exists in the firebase
    // the checking result will be displayed under the 'check' btn
    public void checkID(String id){
        Query query = myRef.child("player").orderByChild("id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TextView textView = (TextView) findViewById(R.id.textView_checkResult);
                if(dataSnapshot.exists()){
                    textView.setText("ID found!");
                }else{
                    textView.setText("ID not found!");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
