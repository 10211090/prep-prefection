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
import com.papb.prepperfection.group.Historys;
import com.papb.prepperfection.group.Products;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.MyViewHolder> {
    Context context;
    ArrayList<Carts> list;

    public NotifAdapter(Context context, ArrayList<Carts> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotifAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.notif, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifAdapter.MyViewHolder holder, int position) {
        Carts carts = list.get(position);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        Locale locale = new Locale("in", "ID");
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance(locale));

        holder.tglNotif.setText(carts.getTglItem());
        if (carts.getStatusItem().equals("Menunggu Pembayaran")){
            holder.imgNotif.setImageResource(R.drawable.ic_not_succes);
            holder.txtNotif.setText("Produk "+carts.getNameItem()+" pada keranjang belanja No. "+carts.getCartId()+" dengan harga "+format.format(Integer.valueOf(carts.getPriceItem()))+" belum dilakukan pembayaran.");
        }else{
            holder.imgNotif.setImageResource(R.drawable.ic_success);
            holder.txtNotif.setText("Produk "+carts.getNameItem()+" pada keranjang belanja No. "+carts.getCartId()+" dengan harga "+format.format(Integer.valueOf(carts.getPriceItem()))+" telah pembayaran.");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtNotif, tglNotif;
        ImageView imgNotif;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            txtNotif = itemView.findViewById(R.id.tv_item_name_notif);
            tglNotif = itemView.findViewById(R.id.tv_item_description_tgl_notif);
            imgNotif = itemView.findViewById(R.id.img_item_photo_notif);

        }

    }
}
