package palisadoes.org.onestop;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;


public class LoginActivity extends AppCompatActivity {

    private EditText mInputEmail;
    private EditText mInputPassword;
    private Button mSignInBtn;
    private TextView mSignUpLink;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;




        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Toast.makeText(mContext,"User present",Toast.LENGTH_SHORT).show();
                    //Log.d(Constants.LOGGER, "onAuthStateChanged:signed_in:" + user.getUid());

                    //go straight to main activity
                    Intent intent = new Intent(mContext,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    finish();

                } else {
                    // User is signed out
                    Toast.makeText(mContext,"User not present",Toast.LENGTH_SHORT).show();
                    //Log.d(Constants.LOGGER, "onAuthStateChanged:signed_out");
                }

            }
        };
        initViews();
        mSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processLogin();
            }
        });

    }

    private void initViews()
    {
        mInputEmail = (EditText) findViewById(R.id.input_email);
        mInputPassword = (EditText) findViewById(R.id.input_password);
        mSignInBtn = (Button) findViewById(R.id.btn_login);
        mSignUpLink = (TextView) findViewById(R.id.link_signup);
    }

    private void processLogin()
    {
        String email = mInputEmail.getText().toString();
        String password = mInputPassword.getText().toString();

        if(TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            mInputEmail.setError("Invalid email");
            return;
        }else{
            mInputEmail.setError(null);

        }

        if(TextUtils.isEmpty(password))
        {
            mInputEmail.setError("Invalid password");
            return;
        }else{
            mInputPassword.setError(null);

        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Log.d(SyncStateContract.Constants.LOGGER, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            //Log.w(Constants.LOGGER, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Successful.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(mContext,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                            finish();
                        }


                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                               // progressDialog.dismiss();
                            }
                        });
                    }
                });


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void showProgressDialog() {
      /*  progressDialog = new ProgressDialog(LoginActivity.this,
                ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();*/
    }

    private void gotoRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.setFlags(FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

}
