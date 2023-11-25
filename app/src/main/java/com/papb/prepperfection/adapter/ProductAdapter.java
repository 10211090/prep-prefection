package com.papb.prepperfection.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context context;
    ArrayList<Products> list;

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

        if (products.getKategoriProduk().equals("Vegan") ){
               holder.categoryProductImg.setImageResource(R.drawable.ic_vegan);
        } else if (products.getKategoriProduk().equals("Fruit") ){
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

        TextView nameProduct, priceProduct, categoryProduct;
        ImageView categoryProductImg;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            nameProduct = itemView.findViewById(R.id.tv_item_name);
            priceProduct = itemView.findViewById(R.id.tv_item_description_value);
            categoryProduct = itemView.findViewById(R.id.item_name_category);
            categoryProductImg = itemView.findViewById(R.id.item_img_category);
        }

    }
}
