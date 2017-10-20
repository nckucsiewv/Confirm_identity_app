package csiewv.yuwen.app.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class ShowActivity extends AppCompatActivity {

    private String chosenDepartment;
    private String chosenDate;
    final static String CURRENT_MONTH = "201711";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner spinner_dep = (Spinner)findViewById(R.id.spinner_department);
        final ArrayAdapter<CharSequence> departmentList = ArrayAdapter.createFromResource(ShowActivity.this,
                R.array.department,
                android.R.layout.simple_spinner_dropdown_item);
        spinner_dep.setAdapter(departmentList);
        spinner_dep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ShowActivity.this, "你選的是" + spinner_dep.getSelectedItem(), Toast.LENGTH_SHORT).show();
                chosenDepartment = spinner_dep.getSelectedItem().toString();
                Toast.makeText(ShowActivity.this, chosenDepartment, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        final Spinner spinner_dat = (Spinner)findViewById(R.id.spinner_date);
        ArrayAdapter<CharSequence> dateList = ArrayAdapter.createFromResource(ShowActivity.this,
                R.array.date,
                android.R.layout.simple_spinner_dropdown_item);
        spinner_dat.setAdapter(dateList);
        spinner_dat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ShowActivity.this, "你選的是" + parent.getSelectedItem(), Toast.LENGTH_SHORT).show();
                chosenDate = spinner_dat.getSelectedItem().toString();
                Toast.makeText(ShowActivity.this, chosenDate, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void toDataActivity(View view){
        Intent intent = new Intent();
        intent.setClass(ShowActivity.this,DataActivity.class);

        String getDepartmentEng[] = chosenDepartment.split(" ");
        chosenDepartment = getDepartmentEng[0];

        chosenDate = CURRENT_MONTH+chosenDate;

        intent.putExtra("department",chosenDepartment);
        intent.putExtra("date",chosenDate);

        startActivity(intent);
    }


}
