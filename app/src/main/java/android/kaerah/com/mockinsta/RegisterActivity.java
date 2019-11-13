package android.kaerah.com.mockinsta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.kaerah.com.mockinsta.ui.login.LoginViewModel;
import android.kaerah.com.mockinsta.ui.login.LoginViewModelFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Set;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password, handle, email;
    Button registerButton;

    private static final String TAG = "Register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        handle = findViewById(R.id.handle);
        email  = findViewById(R.id.email);
        registerButton = findViewById(R.id.register);

        username.setText(getIntent().getStringExtra("USERNAME"));
        password.setText(getIntent().getStringExtra("PASSWORD"));
        registerButton.setEnabled(isFormDataValid());

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(username.getText().toString(), password.getText().toString(), email.getText().toString(), handle.getText().toString());
            }
        });

    }

    private void register(String username, String password, String email, String handle) {
        // Create the ParseUser
        ParseUser user = new ParseUser();
        // Set core properties
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        // Set custom properties
        user.put("handle", handle);
        // Invoke signUpInBackground
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Log.i(TAG, "User registered");
                    goToMainActivity();
                } else {
                    Log.e(TAG, e.toString());
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }

    private void goToMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private boolean isFormDataValid() {
        return username.getText().toString() != "" &&
                password.getText().toString() != "" &&
                email.getText().toString() != "" &&
                handle.getText().toString() != "";
    }
}
