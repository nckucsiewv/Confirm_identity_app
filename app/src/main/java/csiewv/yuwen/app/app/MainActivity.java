package csiewv.yuwen.app.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private final Database db = new Database();
    private final int DATABASE_SUCCESS = 0;
    private final int DATABASE_FAIL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // pre check student id and date

        if(db.registerByStudentIdAndDate("F74004089", "20171104")==DATABASE_SUCCESS){
            Log.d("myLog", "success");
        }else{
            Log.d("firebase", "fail");
        }
    }
}
