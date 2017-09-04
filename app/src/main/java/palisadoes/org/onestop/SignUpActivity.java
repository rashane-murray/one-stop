package palisadoes.org.onestop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {


    private EditText mNameInput;
    private EditText mEmailInput;
    private EditText mPhoneInput;
    private EditText mPasswordInput;
    private EditText mPassword2Input;
    private Button mSignUpButton;
    private SharedPreferences mSharedPrefs;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mContext = this;

        initViews();

        mSharedPrefs = getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE);

        mAuth = FirebaseAuth.getInstance();

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processSignUp();

            }
        });

    }

    private void processSignUp()
    {
        Toast.makeText(this,"Attempting sign up",Toast.LENGTH_SHORT).show();

        final String name = mNameInput.getText().toString();
        String email = mEmailInput.getText().toString();
        final String phone = mPhoneInput.getText().toString();
        String password = mPasswordInput.getText().toString();
        String password2 = mPassword2Input.getText().toString();

        if(TextUtils.isEmpty(name))
        {
            mNameInput.setError("Invalid name");
            return;
        }else{
            mNameInput.setError(null);
        }

        if(TextUtils.isEmpty(email))
        {
            mEmailInput.setError("Invalid email");
            return;
        }else{
            mEmailInput.setError(null);
        }

        if(TextUtils.isEmpty(phone))
        {
            mPhoneInput.setError("Invalid phone");
            return;
        }else{
            mPhoneInput.setError(null);
        }

        if(TextUtils.isEmpty(password))
        {
            mPasswordInput.setError("Invalid password");
            return;
        }else{
            mPasswordInput.setError(null);
        }

        if(TextUtils.isEmpty(password2))
        {
            mPassword2Input.setError("Re-enter password");
            return;
        }else{
            mPassword2Input.setError(null);
        }

        if(!password.contentEquals(password2)){
            mPassword2Input.setError("Passwords must match");
            return;
        }else{
            mPassword2Input.setError(null);
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(Constants.LOGGER, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(SignUpActivity.this, "user created.",
                                    Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = mSharedPrefs.edit();
                            editor.putString("name",name);
                            editor.putString("phone",phone);
                            editor.commit();

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

    private void initViews()
    {
        mNameInput = (EditText) findViewById(R.id.input_name);
        mEmailInput = (EditText) findViewById(R.id.input_email);
        mPhoneInput = (EditText) findViewById(R.id.input_phone);
        mPasswordInput = (EditText) findViewById(R.id.input_password);
        mPassword2Input = (EditText) findViewById(R.id.input_reEnterPassword);
        mSignUpButton = (Button) findViewById(R.id.btn_signup);
    }

}
