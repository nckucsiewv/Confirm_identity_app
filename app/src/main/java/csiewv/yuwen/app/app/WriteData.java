package csiewv.yuwen.app.app;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WriteData extends Activity{

    // [START declare_database_ref]
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the bundle
        Bundle bundle = getIntent().getExtras();
        String transferdata = bundle.getString("data");

        // [START write database yuwen]
        mDatabase = database.getReference(transferdata);
        mDatabase.setValue("1");    //0:not yet. 1:has already confirmed.
        // [END write database yuwen]
    }
}
