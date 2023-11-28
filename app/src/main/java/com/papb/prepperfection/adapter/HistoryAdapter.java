package com.papb.prepperfection.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.papb.prepperfection.R;
import com.papb.prepperfection.activity.DashboardRetail;
import com.papb.prepperfection.group.Carts;
import com.papb.prepperfection.group.Products;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<Carts> list;

    public HistoryAdapter(Context context, ArrayList<Carts> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.history, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {
        Carts carts = list.get(position);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        Locale locale = new Locale("in", "ID");
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance(locale));


        holder.txtHistory.setText(carts.getCartId());
        holder.tglHistory.setText(carts.getTglItem());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtHistory, tglHistory;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            txtHistory = itemView.findViewById(R.id.tv_item_name_history);
            tglHistory = itemView.findViewById(R.id.tv_item_description_tgl);
        }

    }
}
