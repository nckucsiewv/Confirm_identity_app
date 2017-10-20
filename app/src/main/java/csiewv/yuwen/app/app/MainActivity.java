package csiewv.yuwen.app.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String date = "20171104";
    TextView textView_date;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_date = (TextView) findViewById(R.id.tx_date);
        textView_date.setText(date);

        // 選擇日期
        final String[] dateList = {"20171104", "20171105", "20171111", "20171112"};
        ImageButton ib_d = (ImageButton) findViewById(R.id.ib_d);
        ib_d.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setTitle("選擇日期").setItems(dateList,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                date = dateList[which];
                                textView_date.setText(date);
                            }
                        }).show();
            }

        });


        // 掃描檢錄
        Button bt_a = (Button) findViewById(R.id.bt_a);
        bt_a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ScanActivity.class);
                //new a Bundle to transfer data
                Bundle bundle = new Bundle();
                bundle.putString("date", date);  //choose the person to confirm

                //transfer Bundle to intent
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // 手動刪除
        Button bt_d = (Button) findViewById(R.id.bt_d);
        bt_d.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, DeleteActivity.class);

                startActivity(intent);
            }
        });

        // 秀出來
        Button bt_s = (Button) findViewById(R.id.bt_s);
        bt_s.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ShowActivity.class);

                startActivity(intent);
            }
        });
    }
}