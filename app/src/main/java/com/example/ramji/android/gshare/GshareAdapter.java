package com.example.ramji.android.gshare;


import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ramji.android.gshare.provider.GshareContract;

import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.N)
public class GshareAdapter extends RecyclerView.Adapter<GshareAdapter.GshareViewHolder>{

    private static final String TAG = "GshareAdapter";

    private Cursor mData;
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("dd MMM");

    private static final long SECOND_MILLIS = 1000;
    private static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    @Override
    public GshareAdapter.GshareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gshare_list,parent,false);

        GshareViewHolder gshareViewHolder = new GshareViewHolder(view);
        return gshareViewHolder;
    }

    @Override
    public void onBindViewHolder(GshareAdapter.GshareViewHolder holder, int position) {

        mData.moveToPosition(position);

        String author = mData.getString(MainActivity.COL_NUM_AUTHOR);
        String message = mData.getString(MainActivity.COL_NUM_MESSAGE);
        String authorKey = mData.getString(MainActivity.COL_NUM_AUTHOR_KEY);

        //Get the date for displaying
        long dateMillis = mData.getLong(MainActivity.COL_NUM_DATE);
        String date = "";
        long now = System.currentTimeMillis();

        // Change how the date is displayed depending on whether it was written in the last minute,
        // the hour, etc.
        if (now - dateMillis < (DAY_MILLIS)) {
            if (now - dateMillis < (HOUR_MILLIS)) {
                if (now - dateMillis < (MINUTE_MILLIS)){
                    long seconds = Math.round((now - dateMillis) / SECOND_MILLIS);
                    date = String.valueOf(seconds) + "s";
                }else {
                    long minutes = Math.round((now - dateMillis) / MINUTE_MILLIS);
                    date = String.valueOf(minutes) + "m";
                }
            } else {
                long minutes = Math.round((now - dateMillis) / HOUR_MILLIS);
                date = String.valueOf(minutes) + "h";
            }
        } else {
            Date dateDate = new Date(dateMillis);
            date = sDateFormat.format(dateDate);
        }
        // Add a dot to the date string
        date = "\u2022 " + date;

        holder.messageTextView.setText(message);
        holder.authorTextView.setText(author);
        holder.dateTextView.setText(date);

        // Choose the correct, and in this case, locally stored asset for the instructor. If there
        // were more users, you'd probably download this as part of the message.
        switch (authorKey) {
            case GshareContract.RAMJI_KEY:
                holder.authorImageView.setImageResource(R.drawable.ramji);
                break;
            case GshareContract.YESHWENTH_KEY:
                holder.authorImageView.setImageResource(R.drawable.yeshwenth);
                break;
            case GshareContract.PAVITHRAN_KEY:
                holder.authorImageView.setImageResource(R.drawable.pavithran);
                break;
            case GshareContract.SINDHU_KEY:
                holder.authorImageView.setImageResource(R.drawable.sindhu);
                break;
            case GshareContract.VIJI_KEY:
                holder.authorImageView.setImageResource(R.drawable.viji);
                break;
            default:
                holder.authorImageView.setImageResource(R.drawable.test);
        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.getCount();
    }

    public void swapCursor(Cursor newCursor){
        mData = newCursor;
        notifyDataSetChanged();
    }

    public class GshareViewHolder extends RecyclerView.ViewHolder{

        final TextView authorTextView;
        final TextView messageTextView;
        final TextView dateTextView;
        final ImageView authorImageView;

        public GshareViewHolder(View itemView) {
            super(itemView);
            authorTextView = (TextView) itemView.findViewById(R.id.author_text_view);
            messageTextView = (TextView) itemView.findViewById(R.id.message_text_view);
            dateTextView = (TextView) itemView.findViewById(R.id.date_text_view);
            authorImageView = (ImageView) itemView.findViewById(R.id.author_image_view);
        }
    }
}
