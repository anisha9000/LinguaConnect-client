package client.linguaconnect.com.linguaconnectclient.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;

import org.json.JSONException;
import org.json.JSONObject;

import client.linguaconnect.com.linguaconnectclient.AppController;
import client.linguaconnect.com.linguaconnectclient.BuildConfig;
import client.linguaconnect.com.linguaconnectclient.R;
import client.linguaconnect.com.linguaconnectclient.utils.Constants;
import client.linguaconnect.com.linguaconnectclient.utils.MpGooglePlayServices;
import client.linguaconnect.com.linguaconnectclient.utils.Utility;

import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

public class PersonalInfo extends AppCompatActivity {

    String email;
    String firstName;
    String lastName;
    String phone;
    String password;
    String genderSelected;

    EditText etAge;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    EditText etCompany;
    EditText etDesignation;
    EditText etLocation;
    private String TAG = PersonalInfo.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        Intent intent = getIntent();
        if(intent != null) {
            email = intent.getStringExtra(getString(R.string.email));
            password = intent.getStringExtra(getString(R.string.password));
            firstName = intent.getStringExtra(getString(R.string.first_name));
            lastName = intent.getStringExtra(getString(R.string.last_name));
            phone = intent.getStringExtra(getString(R.string.phone_number));
        }

        initScreen();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    private void initScreen() {
        etAge = (EditText) findViewById(R.id.age);
        rgGender = (RadioGroup) findViewById(R.id.gender);
        etCompany = (EditText) findViewById(R.id.company);
        etDesignation = (EditText) findViewById(R.id.designation);
        etLocation = (EditText) findViewById(R.id.location);
        genderSelected = "male";

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_male:
                        genderSelected = "male";
                        break;
                    case R.id.radio_female:
                        genderSelected = "female";
                        break;
                }
            }
        });

        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =
                        null;
                try {
                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(PersonalInfo.this);
                    startActivityForResult(intent, Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                etLocation.setText(place.getName());
                Log.e(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    public void registerUser(View view) {

        JSONObject registerJSON = createJSONForRegistration();

        String url = Constants.BASE_URL + Constants.REGISTER_URL;

        Log.e(TAG,registerJSON+","+url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, registerJSON,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG,"response:"+response);

                        Utility.showToast(PersonalInfo.this,response.optString("message"));
                        startActivity(new Intent(PersonalInfo.this, LoginActivity.class));
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyError error = null;
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    error = new VolleyError(new String(volleyError.networkResponse.data));
                }
                try {
                    JSONObject responseString = new JSONObject(error.getMessage());
                    Utility.showToast(PersonalInfo.this, responseString.optString("error"));
                    if(volleyError.networkResponse.statusCode == 500) {
                        startActivity(new Intent(PersonalInfo.this, LoginActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, ""+volleyError.getMessage());
            }
        }) {

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {

                return volleyError;
            }
        };

        DefaultRetryPolicy policy = new DefaultRetryPolicy(2 * 60 * 1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjReq.setRetryPolicy(policy);

        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    private JSONObject createJSONForRegistration() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(getString(R.string.email), email);
            jsonObject.put(getString(R.string.password), password);
            jsonObject.put(getString(R.string.first_name), firstName);
            jsonObject.put(getString(R.string.last_name), lastName);
            jsonObject.put(getString(R.string.age), etAge.getText().toString());
            jsonObject.put(getString(R.string.gender), genderSelected);
            jsonObject.put(getString(R.string.company), etCompany.getText().toString());
            jsonObject.put(getString(R.string.designation), etDesignation.getText().toString());
            jsonObject.put(getString(R.string.phone_number), phone);
            jsonObject.put(getString(R.string.location), etLocation.getText().toString());

            jsonObject.put("app-version", BuildConfig.VERSION_CODE);
            jsonObject.put("app-version-name",BuildConfig.VERSION_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
