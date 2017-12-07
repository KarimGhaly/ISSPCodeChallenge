package com.example.admin.isspcodechallenge.View.MainActivity.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.isspcodechallenge.R;
import com.example.admin.isspcodechallenge.model.Response;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 12/6/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    List<Response> responseList;
    Context context;

    public RecyclerViewAdapter(List<Response> responseList) {
        this.responseList = responseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.pass_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Response response = responseList.get(position);
        holder.txtDuration.setText("Duration: "+response.getDuration() + " sec");
        holder.txtRiseTime.setText("Rise Time: "+getDateCurrentTimeZone(response.getRisetime()));
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }
    public  String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtDuration)
        TextView txtDuration;
        @BindView(R.id.txtRiseTime)
        TextView txtRiseTime;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
