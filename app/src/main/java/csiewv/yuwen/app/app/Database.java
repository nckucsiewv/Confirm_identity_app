package csiewv.yuwen.app.app;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database{

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;

    // constructor
    public Database(){
    }



    // write registry status to database, return 1 means fail, 0 means success
    public int registerByStudentIdAndDate(String studentId, String date) {
        // [START write database yuwen]
        try {
            mDatabase = database.getReference("users/" + studentId + "/check/" + date);
            mDatabase.setValue("1");    //0:not yet. 1:has already confirmed.
            // [END write database yuwen]
        }catch (Exception e){
            Log.d("MyLog",e.toString());
            return 1;
        }
        return 0;
    }
}
