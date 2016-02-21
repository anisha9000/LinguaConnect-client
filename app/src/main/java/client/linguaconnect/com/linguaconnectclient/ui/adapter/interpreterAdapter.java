package client.linguaconnect.com.linguaconnectclient.ui.adapter;

import android.content.Intent;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import client.linguaconnect.com.linguaconnectclient.AppController;
import client.linguaconnect.com.linguaconnectclient.BuildConfig;
import client.linguaconnect.com.linguaconnectclient.R;
import client.linguaconnect.com.linguaconnectclient.ui.Interpreters;
import client.linguaconnect.com.linguaconnectclient.ui.LinguaConnect;
import client.linguaconnect.com.linguaconnectclient.utils.Constants;
import client.linguaconnect.com.linguaconnectclient.utils.Utility;

/**
 * Created by anisha on 27/12/15.
 */
public class interpreterAdapter extends RecyclerView.Adapter<interpreterAdapter.ViewHolder> {

    private final ArrayList<InterpreterItem> mDataset;
    private final Interpreters activity;
    private String TAG = interpreterAdapter.class.getName();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.interpreter_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final InterpreterItem interpreterDetail = mDataset.get(position);
        Log.e(TAG, ""+interpreterDetail);

        holder.tvName.setText(interpreterDetail.getName());
        holder.tvNationality.setText(interpreterDetail.getNationality());
        holder.tvAge.setText(String.valueOf(interpreterDetail.getAge()));

        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(interpreterDetail.getPictureUrl(),
                ImageLoader.getImageListener(holder.ivInterpreterImage,R.mipmap.profile,R.mipmap.profile));

        setRating(holder, interpreterDetail.getRating());


        holder.btnBookInterpreter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookInterpreter(interpreterDetail.getEmail());
            }
        });

    }

    void setRating(ViewHolder holder,int rating){
        for(int i=1;i<=5;i++) {
            if(i<=rating) {
                holder.stars[i-1].setImageResource(R.mipmap.ic_star_black);
            } else if (i > rating) {
                holder.stars[i-1].setImageResource(R.mipmap.ic_star_border_black);
            } else {
                holder.stars[i-1].setImageResource(R.mipmap.ic_star_half_black);
            }
        }
    }

    private void bookInterpreter(String interpreterEmail) {
        JSONObject registerJSON = createJSONForBooking(interpreterEmail);

        String url = Constants.BASE_URL + Constants.CREATE_BOOKING;

        Log.e(TAG,registerJSON+","+url);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, registerJSON,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG,"response:"+response);
                        Utility.showToast(activity, response.optString("message"));
                        activity.startActivity(new Intent(activity,LinguaConnect.class));
                        activity.finish();
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
                    Utility.showToast(activity, responseString.optString("error"));
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

    private JSONObject createJSONForBooking(String interpreterEmail) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(activity.getString(R.string.client_email),
                    Utility.getLocalString(activity, Constants.USER_EMAIL));
            jsonObject.put(activity.getString(R.string.interpreter_email), interpreterEmail);
            jsonObject.put(activity.getString(R.string.language), "English");
            jsonObject.put("app-version", BuildConfig.VERSION_CODE);
            jsonObject.put("app-version-name",BuildConfig.VERSION_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView tvName, tvNationality, tvAge;
        Button btnBookInterpreter;
        ImageView ivInterpreterImage;
        ImageView[] stars;
        
        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.name);
            tvNationality = (TextView) itemView.findViewById(R.id.nationality);
            tvAge = (TextView) itemView.findViewById(R.id.age);
            btnBookInterpreter = (Button) itemView.findViewById(R.id.bookInterpreterBtn);
            ivInterpreterImage = (ImageView) itemView.findViewById(R.id.interpreter_image);
            stars=new ImageView[5];
            stars[0]=(ImageView)itemView.findViewById(R.id.rate1);
            stars[1]=(ImageView)itemView.findViewById(R.id.rate2);
            stars[2]=(ImageView)itemView.findViewById(R.id.rate3);
            stars[3]=(ImageView)itemView.findViewById(R.id.rate4);
            stars[4]=(ImageView)itemView.findViewById(R.id.rate5);
        }
    }

    public interpreterAdapter(ArrayList<InterpreterItem> myDataset, Interpreters activity) {
        this.mDataset = myDataset;
        this.activity = activity;
    }
}
