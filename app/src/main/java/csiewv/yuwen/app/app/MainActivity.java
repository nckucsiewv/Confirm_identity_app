package csiewv.yuwen.app.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import csiewv.yuwen.app.app.R;


public class MainActivity extends AppCompatActivity {

    public DatabaseReference myRef;
    static public int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    // called when users clicking on the 'leader' checkBox
    // if the leader checkBox is checked, enable the phoneNum editText,
    // so that user can input leaders' phoneNum
    public void onCheckBoxClicked(View v){
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox_leader);
        EditText editText_phone = (EditText) findViewById(R.id.editText_phone);
        if(checkBox.isChecked()){
            editText_phone.setEnabled(true);
        }else{
            editText_phone.setEnabled(false);
        }
    }

    // add a player info to firebase
    public void addPlayer(String name, String id){
        Player player = new Player(name,id);
        myRef.child("player").child(Integer.toString(count)).setValue(player);
        ++count;
    }

    // add a leader info to firebase
    public void addLeader(String name, String id, String phone){
        Player player = new Player(name,id,phone);
        myRef.child("player").child(Integer.toString(count)).setValue(player);
        ++count;
    }

    // called when users clicking the 'send' btn
    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayActivity.class);

        EditText editText = (EditText) findViewById(R.id.editText_name);
        String toSend_name = editText.getText().toString();
        EditText editText2 = (EditText) findViewById(R.id.editText_num);
        String toSend_id = editText2.getText().toString();

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox_leader);
        if(checkBox.isChecked()){
            EditText editText3 = (EditText) findViewById(R.id.editText_phone);
            String toSend_phone = editText3.getText().toString();
            addLeader(toSend_name,toSend_id,toSend_phone);
        }else{
            addPlayer(toSend_name,toSend_id);
        }

        startActivity(intent);
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
