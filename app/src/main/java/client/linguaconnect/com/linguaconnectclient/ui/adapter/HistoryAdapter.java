package client.linguaconnect.com.linguaconnectclient.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import client.linguaconnect.com.linguaconnectclient.AppController;
import client.linguaconnect.com.linguaconnectclient.R;
import client.linguaconnect.com.linguaconnectclient.ui.HistoryActivity;

/**
 * Created by anisha on 7/1/16.
 */
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private final ArrayList<HistoryItem> mDataset;
    private final HistoryActivity activity;
    private String TAG = HistoryAdapter.class.getName();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HistoryItem historyDetail = mDataset.get(position);

        holder.tvName.setText(historyDetail.getName());
        holder.tvLanguage.setText(activity.getResources().getString(R.string.displayLanguage)
                +": "+ historyDetail.getLanguage());
        holder.tvDuration.setText(String.valueOf(historyDetail.getDuration()));
        holder.tvStatus.setText(historyDetail.getStatus());
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        imageLoader.get(historyDetail.getPictureUrl(),
                ImageLoader.getImageListener(holder.ivInterpreterImage,R.mipmap.profile,R.mipmap.profile));

        setRating(holder, historyDetail.getRating());
        holder.tvBookingTime.setText(historyDetail.getBookingTime());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView tvName, tvLanguage, tvDuration, tvStatus, tvBookingTime;
        ImageView ivInterpreterImage;
        ImageView[] stars;

        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.interpreter_name);
            tvLanguage = (TextView) itemView.findViewById(R.id.language);
            tvDuration = (TextView) itemView.findViewById(R.id.duration);
            tvStatus = (TextView) itemView.findViewById(R.id.status);
            ivInterpreterImage = (ImageView) itemView.findViewById(R.id.interpreter_image);
            stars=new ImageView[5];
            stars[0]=(ImageView)itemView.findViewById(R.id.rate1);
            stars[1]=(ImageView)itemView.findViewById(R.id.rate2);
            stars[2]=(ImageView)itemView.findViewById(R.id.rate3);
            stars[3]=(ImageView)itemView.findViewById(R.id.rate4);
            stars[4]=(ImageView)itemView.findViewById(R.id.rate5);
            tvBookingTime = (TextView) itemView.findViewById(R.id.booking_time);
        }
    }

    public HistoryAdapter(ArrayList<HistoryItem> myDataset, HistoryActivity activity) {
        this.mDataset = myDataset;
        this.activity = activity;
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
}
