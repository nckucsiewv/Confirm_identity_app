package com.example.susan.scanner;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {

    private TextView scanResult;
    private EditText idInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanResult = (TextView)findViewById(R.id.textView3);
        scanResult.setText("");
        idInput = (EditText)findViewById(R.id.editText);
        Button addBtn = (Button)findViewById(R.id.add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidId(idInput.getText().toString())) {
                    Toast.makeText(MainActivity.this, idInput.getText().toString(), Toast.LENGTH_LONG).show();
                    scanResult.append(idInput.getText().toString() + "\n");
                }
                else
                    Toast.makeText(MainActivity.this, "Not a student ID.", Toast.LENGTH_LONG).show();
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
    }

    @Override
    protected void onStart()
    {
        super.onStart();

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
                    Toast.makeText(MainActivity.this, "Not a student ID.", Toast.LENGTH_LONG).show();

            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

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

    private boolean isValidId(String id)
    {
        if(id.charAt(0) >= 'A' && id.charAt(0) <= 'Z')
        {
            for(int i=1; i<9; i++)
                if(id.charAt(i) < '0' || id.charAt(i) > '9')
                    return false;
            return true;
        }
        else {
            return false;
        }
    }

}
