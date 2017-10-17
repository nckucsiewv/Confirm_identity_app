package csiewv.yuwen.app.app;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by mac on 18/10/2017.
 */

public final class Database {

    public ApiCallback apiCallback;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;

    public final int ID_EXIST = 1;
    public final int ID_NON_EXIST = 0;
    public final int CHECKID_ERROR = -1;

    // constructor
    public Database(Context context){
        apiCallback = (ApiCallback) context;
    }

    // check if the id exists in the firebase
    // the checking result will be displayed under the 'check' btn
    //
    public void checkExistanceByStudentId(final String studentId){
        mDatabase = database.getReference();
        Query query = mDatabase.child("users").orderByKey().equalTo(studentId);
        apiCallback.studentIdCheckProcessing(studentId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d("myLog", "ID found!");
                    apiCallback.studentIdExist(studentId);
                }else{
                    Log.d("myLog", "ID not found!");
                    apiCallback.studentIdNonExist(studentId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                apiCallback.studentIdCheckError();
            }

        });

    }
}
