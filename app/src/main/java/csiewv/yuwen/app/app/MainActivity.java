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

public class MainActivity extends AppCompatActivity implements ApiCallback{

    private final Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    // called when users clicking the 'check id' btn
    public void sendCheckingID(View view){
        EditText editText = (EditText) findViewById(R.id.editText_num);
        db.checkExistanceByStudentId(editText.getText().toString());
    }


    @Override
    public void studentIdExist(String studentId) {
        TextView textView = (TextView) findViewById(R.id.textView_checkResult);
        textView.setText(studentId+" found!");
    }

    @Override
    public void studentIdNonExist(String studentId) {
        TextView textView = (TextView) findViewById(R.id.textView_checkResult);
        textView.setText(studentId+" not found!");
    }

    @Override
    public void studentIdCheckError() {
        TextView textView = (TextView) findViewById(R.id.textView_checkResult);
        textView.setText("ID checking error");
    }

    @Override
    public void studentIdCheckProcessing(String studentId) {
        TextView textView = (TextView) findViewById(R.id.textView_checkResult);
        textView.setText(studentId+" checking");
    }
}
