package csiewv.yuwen.app.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = new Intent();
        i.setClass(MainActivity.this,WriteData.class);

        //new a Bundle to transfer data
        Bundle bundle = new Bundle();
        bundle.putString("data","users/F74021234/check/20171104");  //choose the person to confirm

        //transfer Bundle to intent
        i.putExtras(bundle);
        startActivity(i);
    }
}
