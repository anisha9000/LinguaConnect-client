package client.linguaconnect.com.linguaconnectclient.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import client.linguaconnect.com.linguaconnectclient.AppController;
import client.linguaconnect.com.linguaconnectclient.BuildConfig;
import client.linguaconnect.com.linguaconnectclient.R;
import client.linguaconnect.com.linguaconnectclient.utils.Constants;
import client.linguaconnect.com.linguaconnectclient.utils.Utility;

public class RegisterActivity extends AppCompatActivity {

    private String TAG = RegisterActivity.class.getName();

    EditText etEmail;
    EditText etPassword;
    EditText etFirstName;
    EditText etLastName;
    EditText etPhoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Log.e(TAG,"on create");

        initScreen();
    }

    private void initScreen() {
        etEmail = (EditText) findViewById(R.id.email);
        etPassword = (EditText) findViewById(R.id.password);
        etFirstName = (EditText) findViewById(R.id.firstName);
        etLastName = (EditText) findViewById(R.id.lastName);
        etPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
    }

    public void registerUser(View view) {

        Intent intent = new Intent(this,PersonalInfo.class);
        intent.putExtra(getString(R.string.email), etEmail.getText().toString());
        intent.putExtra(getString(R.string.password), etPassword.getText().toString());
        intent.putExtra(getString(R.string.first_name), etFirstName.getText().toString());
        intent.putExtra(getString(R.string.last_name), etLastName.getText().toString());
        intent.putExtra(getString(R.string.phone_number), etPhoneNumber.getText().toString());

        startActivity(intent);
    }

}
