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
import com.papb.prepperfection.group.Products;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    Context context;
    ArrayList<Products> list;
    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ProductAdapter(Context context, ArrayList<Products> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyViewHolder holder, int position) {
        Products products = list.get(position);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        Locale locale = new Locale("in", "ID");
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance(locale));


        holder.nameProduct.setText(products.getNamaProduk());
        holder.priceProduct.setText(format.format(Integer.valueOf(products.getHargaProduk())));
        holder.categoryProduct.setText("    "+products.getKategoriProduk());
        Picasso.with(context).load(products.getPhotoProduk()).into(holder.photoProduct);

        if (products.getKategoriProduk().equals("Vegan") ){
               holder.categoryProductImg.setImageResource(R.drawable.ic_vegan);
        } else if (products.getKategoriProduk().equals("Instant") ){
            holder.categoryProductImg.setImageResource(R.drawable.ic_instant);
        } else {
            holder.categoryProductImg.setImageResource(R.drawable.ic_spice);
        }

        holder.itemView.setOnClickListener(v -> onItemClickCallback.onItemClicked(list.get(holder.getAdapterPosition())));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nameProduct, priceProduct, categoryProduct;
        ImageView categoryProductImg, photoProduct;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            nameProduct = itemView.findViewById(R.id.tv_item_name);
            priceProduct = itemView.findViewById(R.id.tv_item_description_value);
            categoryProduct = itemView.findViewById(R.id.item_name_category);
            categoryProductImg = itemView.findViewById(R.id.item_img_category);
            photoProduct = itemView.findViewById(R.id.img_item_photo);
        }

    }
    public interface OnItemClickCallback {
        void onItemClicked(Products data);
    }
}
