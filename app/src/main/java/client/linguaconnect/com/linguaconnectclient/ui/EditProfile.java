package client.linguaconnect.com.linguaconnectclient.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import client.linguaconnect.com.linguaconnectclient.AppController;
import client.linguaconnect.com.linguaconnectclient.BuildConfig;
import client.linguaconnect.com.linguaconnectclient.R;
import client.linguaconnect.com.linguaconnectclient.utils.Constants;
import client.linguaconnect.com.linguaconnectclient.utils.FileUtils;
import client.linguaconnect.com.linguaconnectclient.utils.HandleProgressBar;
import client.linguaconnect.com.linguaconnectclient.utils.MultipartUtility;
import client.linguaconnect.com.linguaconnectclient.utils.Utility;

public class EditProfile extends AppCompatActivity {

    EditText etFirstName, etLastName, etPhone, etEmail;
    EditText etAge, etCompany, etDesignation, etLocation;
    RadioGroup rgGender;
    RadioButton rbMale, rbFemale;
    String genderSelected;
    ImageView accountImage;
    TextView updatePassword;
    TextView uploadImageText;
    private String TAG = EditProfile.class.getName();

    private static final int REQUEST_CHOOSE_PROFILE_IMAGE = 0xac23;
    private File imageFile;
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        initScreen();

        setProfileElements();
    }

    private void setProfileElements() {
        etFirstName.setText(Utility.getLocalString(this,Constants.USER_FIRST_NAME));
        etLastName.setText(Utility.getLocalString(this,Constants.USER_LAST_NAME));
        etPhone.setText(Utility.getLocalString(this,Constants.USER_PHONE_NUMBER));
        etEmail.setText(Utility.getLocalString(this,Constants.USER_EMAIL));
        etAge.setText(String.valueOf(Utility.getLocalInt(this,Constants.USER_AGE)));
        if(Utility.getLocalString(this,Constants.USER_GENDER).equalsIgnoreCase("male")) {
            Log.e(TAG,"rbMale selected");
            rgGender.clearCheck();
            rbMale.setSelected(true);
        } else if(Utility.getLocalString(this,Constants.USER_GENDER).equalsIgnoreCase("female")) {
            Log.e(TAG,"rbfemale selected");
            rgGender.clearCheck();
            rbFemale.setSelected(true);
        }
        etCompany.setText(Utility.getLocalString(this,Constants.USER_COMPANY));
        etDesignation.setText(Utility.getLocalString(this,Constants.USER_DESIGNATION));
        etLocation.setText(Utility.getLocalString(this,Constants.USER_LOCATION));

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(Utility.getLocalString(this, Constants.USER_PICTURE_URL),
                ImageLoader.getImageListener(accountImage,R.mipmap.profile,R.mipmap.profile), 200, 200);

    }

    private void initScreen() {
        etFirstName = (EditText) findViewById(R.id.first_name);
        etLastName = (EditText) findViewById(R.id.last_name);
        etPhone = (EditText) findViewById(R.id.avatar_contact);
        etEmail = (EditText) findViewById(R.id.avatar_email);
        etAge = (EditText) findViewById(R.id.avatar_age);
        rgGender = (RadioGroup) findViewById(R.id.avatar_gender);
        etCompany = (EditText) findViewById(R.id.avatar_company);
        etDesignation = (EditText) findViewById(R.id.avatar_designation);
        etLocation = (EditText) findViewById(R.id.avatar_location);
        rbFemale = (RadioButton) findViewById(R.id.radio_female);
        rbMale = (RadioButton) findViewById(R.id.radio_male);
        accountImage = (ImageView) findViewById(R.id.account_image);
        updatePassword = (TextView) findViewById(R.id.update_password);
        uploadImageText = (TextView) findViewById(R.id.upload_image_text);

        genderSelected = "male";

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_male:
                        genderSelected = "male";
                        Log.e(TAG, "gender selected"+genderSelected);
                        break;
                    case R.id.radio_female:
                        genderSelected = "female";
                        Log.e(TAG, "gender selected"+genderSelected);
                        break;
                }
            }
        });

        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                try {
                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(EditProfile.this);
                    startActivityForResult(intent, Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        accountImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectionDialog();
            }
        });

        uploadImageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectionDialog();
            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfile.this, UpdatePassword.class));
            }
        });
    }

    public void saveProfile(View view) {
        final JSONObject registerJSON = createJSONForEdit();

        String url = Constants.BASE_URL + Constants.UPDATE_CLIENT;

        Log.e(TAG,registerJSON+","+url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, registerJSON,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG,"response:"+response);
                        updateLocalData(registerJSON);
                        Utility.showToast(EditProfile.this,response.optString("message"));
                        finish();
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
                    Utility.showToast(EditProfile.this, responseString.optString("error"));
                    if(volleyError.networkResponse.statusCode == 500) {

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

    private JSONObject createJSONForEdit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(getString(R.string.email), etEmail.getText().toString());
            jsonObject.put(getString(R.string.first_name), etFirstName.getText().toString());
            jsonObject.put(getString(R.string.last_name), etLastName.getText().toString());
            jsonObject.put(getString(R.string.age), etAge.getText().toString());
            jsonObject.put(getString(R.string.gender), genderSelected);
            jsonObject.put(getString(R.string.company), etCompany.getText().toString());
            jsonObject.put(getString(R.string.designation), etDesignation.getText().toString());
            jsonObject.put(getString(R.string.phone_number), etPhone.getText().toString());
            jsonObject.put(getString(R.string.location), etLocation.getText().toString());

            jsonObject.put("app-version", BuildConfig.VERSION_CODE);
            jsonObject.put("app-version-name",BuildConfig.VERSION_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private void updateLocalData (JSONObject jsonObject) {
        Utility.saveLocalString(this,Constants.USER_FIRST_NAME, jsonObject.optString(getString(R.string.first_name)));
        Utility.saveLocalString(this,Constants.USER_LAST_NAME, jsonObject.optString(getString(R.string.last_name)));
        Utility.saveLocalString(this,Constants.USER_PHONE_NUMBER, jsonObject.optString(getString(R.string.phone_number)));
        Utility.saveLocalString(this,Constants.USER_EMAIL, jsonObject.optString(getString(R.string.email)));
        Utility.saveLocalInt(this,Constants.USER_AGE, Integer.parseInt(jsonObject.optString(getString(R.string.age))));
        Utility.saveLocalString(this,Constants.USER_COMPANY, jsonObject.optString(getString(R.string.company)));
        Utility.saveLocalString(this,Constants.USER_DESIGNATION, jsonObject.optString(getString(R.string.designation)));
        Utility.saveLocalString(this,Constants.USER_LOCATION, jsonObject.optString(getString(R.string.location)));
        Utility.saveLocalString(this,Constants.USER_GENDER, jsonObject.optString(getString(R.string.gender)));

        Log.e(TAG,"gender saved:"+Utility.getLocalString(this,Constants.USER_GENDER));
    }

    void openSelectionDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choose_media_source);
        TextView openCamera = (TextView) dialog.findViewById(R.id.openCamera);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
                dialog.dismiss();
            }
        });

        TextView openGallery = (TextView) dialog.findViewById(R.id.openGallery);
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    void openCamera() {
        imageFile = FileUtils.getOutputMediaFile();
        if (imageFile != null) {
            Log.e(TAG, "Calling camera");
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
            currentPhotoPath = imageFile.getAbsolutePath();

            startActivityForResult(cameraIntent, Constants.REQUEST_CODE_NATIVE_CAMERA);
        }
    }

    void openGallery() {
        Intent attachFileIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        attachFileIntent.setType("image/*");
        Intent chooser = Intent.createChooser(attachFileIntent,
                getString(R.string.select_file));
        startActivityForResult(chooser, REQUEST_CHOOSE_PROFILE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "request code:" + requestCode + ",result code:" + resultCode);
        if (resultCode == RESULT_OK) {
            Log.e(TAG, "RESULT_OK");
            switch (requestCode) {
                case REQUEST_CHOOSE_PROFILE_IMAGE:
                    Uri avatarUri = data.getData();
                    currentPhotoPath = FileUtils.getPath(this, avatarUri);
                    uploadImage();
                    Log.e(TAG, "Uri:" + avatarUri.toString());
                    break;
                case Constants.REQUEST_CODE_NATIVE_CAMERA:
                    Log.e(TAG, "SENT image url:" + imageFile.getAbsolutePath());
                    uploadImage();
                    break;
                case Constants.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    etLocation.setText(place.getName());
                    Log.e(TAG, "Place: " + place.getName());
                    break;

            }
        } else if (resultCode == RESULT_CANCELED) {

        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);
            Log.e(TAG, status.getStatusMessage());

        }
    }

    private void uploadImage() {
        new UploadFileToServer().execute();
    }

    private class UploadFileToServer extends AsyncTask<String, String, String> {

        HandleProgressBar progress;

        @Override
        protected String doInBackground(String... params) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress = new HandleProgressBar(EditProfile.this);
                        progress.showProgressBar();
                    }
                });

                MultipartUtility multipart = new MultipartUtility(Constants.BASE_URL + Constants.UPLOAD_PIC, "UTF-8");
                multipart.addFormField(getString(R.string.email), Utility.getLocalString(EditProfile.this,Constants.USER_EMAIL));
                    Log.e(TAG,"Adding profile image");
                    File uploadingFile = new File(currentPhotoPath);

                /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Bitmap bmp = Utility.decodeFile(uploadingFile);
                if(bmp != null) {
                    bmp.compress(Bitmap.CompressFormat.JPEG, 90 , bos);
                    ContentBody foto = new ByteArrayBody(bos.toByteArray(), "fileselfie.jpg");
                    reqEntity.addPart(Constants.KEY_SELFIE_FILE, foto);
                    bmp.recycle();
                }*/

                Log.e(TAG,"size:"+uploadingFile.length());
                    multipart.addFilePart("picture", uploadingFile);
                List<String> response = multipart.finish();

                Log.e(TAG,"SERVER REPLIED:");

                int status = 0;
                Log.e(TAG, "Response size : " + response.size());
                String imageUrl = null;
                String statusDisplaying = null;
                for (String line : response) {

                    Log.e(TAG, line);
                    JSONObject uploadPhoto = new JSONObject(line);
                    statusDisplaying = uploadPhoto.getString("message");
                    imageUrl = uploadPhoto.optString("url");
                    Log.e(TAG,statusDisplaying+","+imageUrl);
                }

                Utility.saveLocalString(EditProfile.this,Constants.USER_PICTURE_URL, imageUrl);
                Log.e(TAG,"imageUrl:"+imageUrl);
                final String finalStatusDisplaying = statusDisplaying;
                final String finalImageUrl = imageUrl;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progress != null)
                            progress.hideProgressBar();
                        Utility.showToast(EditProfile.this, finalStatusDisplaying);
                        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
                        imageLoader.get(finalImageUrl,
                                ImageLoader.getImageListener(accountImage,R.mipmap.profile,R.mipmap.profile), 200, 200);
                    }
                });
            } catch (IOException ex) {
                Log.e(TAG, "IOException in uploading : " + ex.toString());
                ex.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(progress != null)
                            progress.hideProgressBar();
                    }
                });
                Utility.showToast(EditProfile.this,"Error uploading image");
                System.err.println(ex);
            } catch (JSONException e) {
                Log.e(TAG, "JSONExp in uploading : " + e.toString());
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
