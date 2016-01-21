package client.linguaconnect.com.linguaconnectclient.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import client.linguaconnect.com.linguaconnectclient.AppController;
import client.linguaconnect.com.linguaconnectclient.BuildConfig;
import client.linguaconnect.com.linguaconnectclient.R;
import client.linguaconnect.com.linguaconnectclient.ui.adapter.InterpreterItem;
import client.linguaconnect.com.linguaconnectclient.ui.adapter.interpreterAdapter;
import client.linguaconnect.com.linguaconnectclient.utils.Constants;
import client.linguaconnect.com.linguaconnectclient.utils.Utility;

public class Interpreters extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextView tvNoInterpreter;

    String eventId;
    private String TAG = Interpreters.class.getName();
    private interpreterAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interpreters);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        Intent intent = getIntent();
        eventId = intent.getStringExtra(Constants.EVENT_ID);

        initScreen();

        getInterpreterList();
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

    private void getInterpreterList() {
        String url = Constants.BASE_URL + Constants.GET_INTERPRETER_LIST + "?" + Constants.EVENT_ID
                + "="+eventId;

        Log.e(TAG, url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG,"response:"+response);

                        JSONArray interpreterArray = response.optJSONArray("interpreters");
                        ArrayList<InterpreterItem> responseList = new ArrayList<>();
                        if(interpreterArray != null) {
                            if(interpreterArray.length() > 0) {
                                for (int i = 0; i < interpreterArray.length(); i++) {
                                    JSONObject interpreterObject = interpreterArray.optJSONObject(i);
                                    if(interpreterObject != null) {

                                        responseList.add(new InterpreterItem(
                                                interpreterObject.optString(getString(R.string.first_name)),
                                                interpreterObject.optString(getString(R.string.last_name)),
                                                interpreterObject.optString(getString(R.string.email)),
                                                interpreterObject.optString(getString(R.string.nationality)),
                                                interpreterObject.optString(getString(R.string.picture_url)),
                                                interpreterObject.optInt(getString(R.string.age)),
                                                interpreterObject.optInt(getString(R.string.rating))
                                        ));

                                        mAdapter = new interpreterAdapter(responseList, Interpreters.this);
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                        tvNoInterpreter.setVisibility(View.GONE);
                                        mRecyclerView.setAdapter(mAdapter);
                                    }

                                }

                            } else {
                                tvNoInterpreter.setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                                logInterpreterRequest();
                            }
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyError error = null;
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    error = new VolleyError(new String(volleyError.networkResponse.data));
                    Log.e(TAG,"error:"+error+", volleyError:"+volleyError);
                }
                try {
                    JSONObject responseString = null;
                    if (error != null) {
                        responseString = new JSONObject(error.getMessage());
                    }
                    Utility.showToast(Interpreters.this, responseString.optString("error"));
                } catch (JSONException e) {
                    Log.e(TAG,"Exception:"+e);
                    e.printStackTrace();
                }
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

    private void logInterpreterRequest() {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(getString(R.string.email), Utility.getLocalString(this,Constants.USER_EMAIL));
                jsonObject.put("app-version", BuildConfig.VERSION_CODE);
                jsonObject.put("app-version-name",BuildConfig.VERSION_NAME);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        String url = Constants.BASE_URL + Constants.BOOKING_REQUEST;

        Log.e(TAG,url + "," + jsonObject);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG,"response:"+response);
                        tvNoInterpreter.setText("No available interpreters found. " +
                                "We have logged your request.");
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyError error = null;
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    error = new VolleyError(new String(volleyError.networkResponse.data));
                    //Log.e(TAG,"error:"+error+", volleyError:"+volleyError);
                }
                try {
                    JSONObject responseString = new JSONObject(error.getMessage());
                    Log.e(TAG,"");
                    Utility.showToast(Interpreters.this, responseString.optString("error"));
                } catch (JSONException e) {
                    Log.e(TAG,"Exception:"+e);
                    e.printStackTrace();
                }
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

    private void initScreen() {
        mRecyclerView = (RecyclerView) findViewById(R.id.interpreter_list_view);
        tvNoInterpreter = (TextView) findViewById(R.id.no_interpreters);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }
}
