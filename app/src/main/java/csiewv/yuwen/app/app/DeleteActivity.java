package csiewv.yuwen.app.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DeleteActivity extends AppCompatActivity implements ApiCallback{

    EditText id;
    DatabaseReference database;
    private final Database db = new Database(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        //
        id = (EditText) findViewById(R.id.et_id);
        final Button bt_ok = (Button) findViewById(R.id.bt_ok);
        bt_ok.setVisibility(View.INVISIBLE);

        // listview
        final ListView list = (ListView) findViewById(R.id.list);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice) ;
        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // firebase
        database = FirebaseDatabase.getInstance().getReference("uid").child("users");
        final String[] date = {"20171104","20171105","20171111","20171112"};


        // 搜尋學號
        final Button bt_check = (Button) findViewById(R.id.bt_s);
        bt_check.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter.clear();
                        bt_ok.setVisibility(View.INVISIBLE);
                        for (DataSnapshot ds :dataSnapshot.getChildren()) {
                            if (id.getText().toString().equals(ds.getKey().toString())){
                                for(DataSnapshot d_c : ds.child("check").getChildren()) {
                                    adapter.add(d_c.getKey().toString() + " : " + d_c.getValue().toString());
                                }
                                bt_ok.setVisibility(View.VISIBLE);
                                break;
                            }
                        }
                    }
                    public  void onCancelled(DatabaseError error){

                    }
                });
            }
        });

        // 修改
        bt_ok.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new AlertDialog.Builder(DeleteActivity.this).setTitle("選擇日期").setMessage("你確定要修改ㄇ").
                        setNegativeButton("不要",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("好",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String s_id = id.getText().toString();
                        SparseBooleanArray pos = list.getCheckedItemPositions();
                        for(int i = 0; i< 4 ;i++){
                            if ( pos.get(i) ){
                                db.delete(s_id,date[i]);
                                list.setItemChecked(i,false);
                            }
                        }
                    }
                }).show();

            }
        });

    }
    public void studentIdExist(String studentId, String date){}
    public void studentIdNonExist(String studentId){}
    public void studentIdCheckError(){}
    public void studentIdCheckProcessing(String studentId){}
}
