package com.example.susan.scanner;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {

    private TextView scanResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanResult = (TextView)findViewById(R.id.textView3);
        scanResult.setText("");

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
                Toast.makeText(this, content, Toast.LENGTH_LONG).show();
                scanResult.append(content + "\n");
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
    }

    @Override
    protected  void onRestoreInstanceState(Bundle savedInstanceState)
    {
        scanResult.setText(savedInstanceState.getString("textView"));
    }

}
