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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    ArrayList<Carts> list;

    public CartAdapter(Context context, ArrayList<Carts> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cart, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyViewHolder holder, int position) {
        Carts carts = list.get(position);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        Locale locale = new Locale("in", "ID");
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance(locale));


        holder.nameProduct.setText(carts.getNameItem());
        holder.qtyProduct.setText(carts.getQtyItem()+" Qty");
        holder.priceProduct.setText(format.format(Integer.valueOf(carts.getPriceItem())));
        holder.categoryProduct.setText("    "+carts.getCategoryItem());
        Picasso.with(context).load(carts.getPhotoItem()).into(holder.photoProduct);

        if (carts.getCategoryItem().equals("Vegan") ){
            holder.categoryProductImg.setImageResource(R.drawable.ic_vegan);
        } else if (carts.getCategoryItem().equals("Fruit") ){
            holder.categoryProductImg.setImageResource(R.drawable.ic_fruit);
        } else {
            holder.categoryProductImg.setImageResource(R.drawable.ic_spice);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nameProduct, priceProduct, categoryProduct, qtyProduct;
        ImageView categoryProductImg, photoProduct;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            nameProduct = itemView.findViewById(R.id.tv_item_name);
            priceProduct = itemView.findViewById(R.id.tv_item_description_value);
            qtyProduct = itemView.findViewById(R.id.tv_item_description_qty);
            categoryProduct = itemView.findViewById(R.id.item_name_category);
            categoryProductImg = itemView.findViewById(R.id.item_img_category);
            photoProduct = itemView.findViewById(R.id.img_item_photo);
        }

    }
}
