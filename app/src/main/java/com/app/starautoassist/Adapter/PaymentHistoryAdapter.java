package com.app.starautoassist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.starautoassist.Activity.PaymentDetailedHistoryActivity;
import com.app.starautoassist.R;
import java.util.ArrayList;
import java.util.HashMap;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<HashMap<String,String>> paymentHistoryList;

    public PaymentHistoryAdapter(Context mContext, ArrayList<HashMap<String, String>> paymentHistoryList) {
        this.mContext = mContext;
        this.paymentHistoryList = paymentHistoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_history_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final HashMap<String, String> itemMap=paymentHistoryList.get(position);

        holder.payid.setText(itemMap.get(""));
        holder.serviceid.setText(itemMap.get(""));
        holder.amount.setText(itemMap.get(""));
        holder.date.setText(itemMap.get(""));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, PaymentDetailedHistoryActivity.class);
                intent.putExtra("payid", itemMap.get(""));
                intent.putExtra("serviceid", itemMap.get(""));
                intent.putExtra("servicename", itemMap.get(""));
                intent.putExtra("amount", itemMap.get(""));
                intent.putExtra("date", itemMap.get(""));
                intent.putExtra("transactionid", itemMap.get(""));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return paymentHistoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView payid, serviceid, amount, date;
        private CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            payid = itemView.findViewById(R.id.tv_payhistory_payid);
            serviceid = itemView.findViewById(R.id.tv_payhistory_serviceid);
            amount = itemView.findViewById(R.id.tv_payhistory_amount);
            date = itemView.findViewById(R.id.tv_payhistory_date);
            cardView = itemView.findViewById(R.id.cv_payment_history);
        }
    }
}
