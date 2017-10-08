package iris.tryfirebase;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.*;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.*;

import org.apache.http.*;
import org.apache.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observer;


public class MainActivity extends AppCompatActivity {

    public DatabaseReference myRef;
    static public int count=0;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "RandomRandom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                if(firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(MainActivity.this, AccountActivity.class));
                }
            }
        };

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener(){
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult){
                        Toast.makeText(MainActivity.this, "got an error", Toast.LENGTH_LONG).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    // called when users clicking the 'SIGN IN' btn
    public void signIn(View v) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    // called when users clicking on the 'leader' checkBox
    // if the leader checkBox is checked, enable the phoneNum editText,
    // so that user can input leaders' phoneNum
    public void onCheckBoxClicked(View v){
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox_leader);
        EditText editText_phone = (EditText) findViewById(R.id.editText_phone);
        if(checkBox.isChecked()){
            editText_phone.setEnabled(true);
        }else{
            editText_phone.setEnabled(false);
        }
    }

    // add a player info to firebase
    public void addPlayer(String name, String id){
        if(mAuth.getCurrentUser() != null){
            Player player = new Player(name,id);
            myRef.child(mAuth.getCurrentUser().getUid()).child(Integer.toString(count)).setValue(player);
            ++count;
        }
    }

    // add a leader info to firebase
    public void addLeader(String name, String id, String phone){
        if(mAuth.getCurrentUser() != null){
            Player player = new Player(name,id,phone);
            myRef.child(mAuth.getCurrentUser().getUid()).child(Integer.toString(count)).setValue(player);
            ++count;
        }
    }

    // called when users clicking the 'send' btn
    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayActivity.class);

        EditText editText = (EditText) findViewById(R.id.editText_name);
        String toSend_name = editText.getText().toString();
        EditText editText2 = (EditText) findViewById(R.id.editText_num);
        String toSend_id = editText2.getText().toString();

        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox_leader);
        if(checkBox.isChecked()){
            EditText editText3 = (EditText) findViewById(R.id.editText_phone);
            String toSend_phone = editText3.getText().toString();
            addLeader(toSend_name,toSend_id,toSend_phone);
        }else{
            addPlayer(toSend_name,toSend_id);
        }

        startActivity(intent);
    }

    // called when users clicking the 'check id' btn
    public void sendCheckingID(View view){
        EditText editText = (EditText) findViewById(R.id.editText_num);
        checkID(editText.getText().toString());
    }

    // check if the id exists in the firebase
    // the checking result will be displayed under the 'check' btn
    public void checkID(String id){
        if(mAuth.getCurrentUser() != null){
            Query query = myRef.child(mAuth.getCurrentUser().getUid()).orderByChild("id").equalTo(id);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    TextView textView = (TextView) findViewById(R.id.textView_checkResult);
                    if(dataSnapshot.exists()){
                        textView.setText("ID found!");
                    }else{
                        textView.setText("ID not found!");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    // called when clicking on 'check profile' btn
    // jump to AccountActivity
    public void checkProfile(View v){
        startActivity(new Intent(MainActivity.this, AccountActivity.class));
    }

}
