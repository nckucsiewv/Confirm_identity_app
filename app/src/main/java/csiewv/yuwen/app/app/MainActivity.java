package csiewv.yuwen.app.app;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ApiCallback{

    private final Database db = new Database(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // pre check student id and date
    }

    // called when users clicking the 'register' btn
    public void onClickedRegisterBtn(View view){
        EditText editText = (EditText) findViewById(R.id.editText_num);
        db.registerByStudentIdAndDate(editText.getText().toString(), "20171104");
    }


    // call back functions for registering
    @Override
    public void studentIdExist(String studentId, String date) {
        TextView textView = (TextView) findViewById(R.id.textView_checkResult);
        textView.setText(studentId+" found!");
        if (db.register(studentId, date) == Database.DATABASE_SUCCESS) {
            Log.d("myLog", "success");
            textView.append(" (register successfully)");
        } else {
            Log.d("myLog", "fail");
            textView.append(" (register unsuccessfully)");
        }
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
