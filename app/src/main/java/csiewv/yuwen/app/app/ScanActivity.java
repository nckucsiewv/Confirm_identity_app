package csiewv.yuwen.app.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanActivity extends AppCompatActivity implements ApiCallback {

    private final Database db = new Database(this);

    private TextView scanResult;
    private EditText idInput;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Bundle bundle = getIntent().getExtras();
        date = bundle.getString("date");

        scanResult = (TextView)findViewById(R.id.textView3);
        scanResult.setText("");
        idInput = (EditText)findViewById(R.id.editText);

        // Hand Input
        Button addBtn = (Button)findViewById(R.id.add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidId(idInput.getText().toString())) {
                    //Toast.makeText(ScanActivity.this, idInput.getText().toString(), Toast.LENGTH_LONG).show();
                    scanResult.append(idInput.getText().toString() + "\n");
                }
                else
                    Toast.makeText(ScanActivity.this, "Not a student ID.", Toast.LENGTH_LONG).show();
            }
        });

        // register
        Button uploadBtn = (Button)findViewById(R.id.upload);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] ids = scanResult.getText().toString().split("\n");
                for(int i=0; i<ids.length; i++)
                {
                    if(ids[i].length() == 9)
                    {
                        db.registerByStudentIdAndDate(ids[i], date);
                    }
                }
            }
        });

        Button startBtn = (Button)findViewById(R.id.scanner);
        final Activity activity = this;
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        Button clearBtn = (Button)findViewById(R.id.clear);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanResult.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null)
        {
            if(result.getContents() == null)
            {
                Toast.makeText(this, "You cancelled the scanning.", Toast.LENGTH_LONG).show();
            }
            else
            {
                String content = result.getContents();
                if(isValidId(content))
                {
                    Toast.makeText(this, content, Toast.LENGTH_LONG).show();
                    scanResult.append(content + "\n");
                }
                else
                    Toast.makeText(ScanActivity.this, "Not a student ID.", Toast.LENGTH_LONG).show();

            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    // restore textView text rotate screen
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString("textView", scanResult.getText().toString());
        outState.putString("editText", idInput.getText().toString());
    }

    @Override
    protected  void onRestoreInstanceState(Bundle savedInstanceState)
    {
        scanResult.setText(savedInstanceState.getString("textView"));
        idInput.setText(savedInstanceState.getString("editText"));
    }



    // call back functions for registering
    @Override
    public void studentIdExist(String studentId, String date) {
        String[] ids = scanResult.getText().toString().split("\n");
        for(int i=0; i<ids.length; i++)
        {

            if(ids[i].substring(0,9).equals(studentId))
            {
                Log.d("myLog", "equal!!!!");
                ids[i] = ids[i].substring(0,9) + "...found";
                UpdateScanResult(ids);
                if (db.register(studentId, date) == Database.DATABASE_SUCCESS) {
                    Log.d("myLog", "success");
                    ids[i] = ids[i].substring(0,9) + "...success!";
                } else {
                    Log.d("myLog", "fail");
                    ids[i] = ids[i].substring(0,9) + "...failed!";
                }
                UpdateScanResult(ids);
            }
        }
    }

    @Override
    public void studentIdNonExist(String studentId) {
        String[] ids = scanResult.getText().toString().split("\n");
        for(int i=0; i<ids.length; i++)
        {
            if (ids[i].equals(studentId))
            {
                ids[i] += "...not found!";
                UpdateScanResult(ids);
            }
        }
    }

    @Override
    public void studentIdCheckError() {
        Toast.makeText(this, "ID checking error", Toast.LENGTH_LONG).show();
    }

    @Override
    public void studentIdCheckProcessing(String studentId) {
        String[] ids = scanResult.getText().toString().split("\n");
        for(int i=0; i<ids.length; i++)
        {
            if (ids[i].equals(studentId))
            {
                ids[i] += "...checking";
                UpdateScanResult(ids);
            }
        }
    }

    // check ID is Valid or not
    private boolean isValidId(String id)
    {
        if(id.charAt(0) >= 'A' && id.charAt(0) <= 'Z')
        {
            for(int i=2; i<9; i++)
                if(id.charAt(i) < '0' || id.charAt(i) > '9')
                    return false;
            return true;
        }
        else {
            return false;
        }
    }

    // Update scan result textView
    private void UpdateScanResult(String[] ids)
    {
        scanResult.setText("");
        for(int i=0; i<ids.length; i++)
        {
            scanResult.append(ids[i] + "\n");
        }
    }
}
