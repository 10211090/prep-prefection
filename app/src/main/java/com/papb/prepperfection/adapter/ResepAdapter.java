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
import com.papb.prepperfection.activity.ResepActivity;
import com.papb.prepperfection.group.Carts;
import com.papb.prepperfection.group.Historys;
import com.papb.prepperfection.group.Products;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import com.papb.prepperfection.group.Reseps;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.MyViewHolder> {
    Context context;
    ArrayList<Reseps> list;
    private ResepAdapter.OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ResepAdapter(Context context, ArrayList<Reseps> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ResepAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.resep, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResepAdapter.MyViewHolder holder, int position) {
        Reseps reseps = list.get(position);

        Picasso.with(context).load(reseps.getFotoResep()).into(holder.fotoResep);
        holder.namaResep.setText(reseps.getNamaResep());
        holder.jenisResep.setText(reseps.getJenisResep());

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(list.get(holder.getAdapterPosition())));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView fotoResep;
        TextView namaResep, jenisResep;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            fotoResep = itemView.findViewById(R.id.img_item_photo_resep);
            namaResep = itemView.findViewById(R.id.tv_item_name_resep);
            jenisResep = itemView.findViewById(R.id.tv_item_description_value_resep);

        }

    }
    public interface OnItemClickCallback {
        void onItemClicked(Reseps data);
    }
}
