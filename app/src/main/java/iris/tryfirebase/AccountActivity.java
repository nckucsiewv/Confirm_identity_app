package iris.tryfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountActivity extends AppCompatActivity {

    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        TextView textView = (TextView) findViewById(R.id.textView_account);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(user != null){
            textView.setText("user email:\n" + user.getEmail() + "\n\nuser name:\n" + user.getDisplayName());
        }else{
            textView.setText("not logged in");
        }
    }

    // called when clicking on the 'log out' btn
    public void signOut(View view) {
        FirebaseAuth.getInstance().signOut();
        user.delete();
        startActivity(new Intent(AccountActivity.this, MainActivity.class));
    }

    // called when clicking on the 'back' btn
    public void getBack(View view) {
        startActivity(new Intent(AccountActivity.this, MainActivity.class));
    }


}