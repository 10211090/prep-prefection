package com.papb.prepperfection.activity;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.papb.prepperfection.MainActivity;
import com.papb.prepperfection.R;
import com.papb.prepperfection.adapter.ProductAdapter;
import com.papb.prepperfection.group.Carts;
import com.papb.prepperfection.group.Products;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class DashboardRetail extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    SharedPreferences sharedPreferences;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;
    ImageView historyActivity, notifActivity, settingActivity;
    Button btnAction, btnVegan, btnFruit, btnSpice;
    Boolean bolBtnVegan = false, bolBtnFruit = false, bolBtnSpice = false;

    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    ArrayList<Products> list;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PROFILE = "profile";
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_retail);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        setDataAdapter();

        btnVegan = findViewById(R.id.buttonVegan);
        btnFruit = findViewById(R.id.buttonFruit);
        btnSpice = findViewById(R.id.buttonSpice);

        btnVegan.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (bolBtnVegan == false){
                    bolBtnVegan = true;
                    bolBtnFruit = false;
                    bolBtnSpice = false;
                    btnVegan.setBackgroundColor(Color.GRAY);
                    btnVegan.setTextColor(Color.WHITE);
                    btnFruit.setBackgroundColor(Color.WHITE);
                    btnFruit.setTextColor(Color.GRAY);
                    btnSpice.setBackgroundColor(Color.WHITE);
                    btnSpice.setTextColor(Color.GRAY);
                    list.clear();
                    productAdapter.notifyDataSetChanged();
                    setDataVeganAdapter();

                }else {
                    btnVegan.setBackgroundColor(Color.WHITE);
                    btnVegan.setTextColor(Color.GRAY);
                    bolBtnVegan = false;
                    list.clear();
                    productAdapter.notifyDataSetChanged();
                    setDataAdapter();
                }

            }
        });
        btnFruit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (bolBtnFruit == false){
                    bolBtnFruit = true;
                    bolBtnVegan = false;
                    bolBtnSpice = false;
                    btnFruit.setBackgroundColor(Color.GRAY);
                    btnFruit.setTextColor(Color.WHITE);
                    btnVegan.setBackgroundColor(Color.WHITE);
                    btnVegan.setTextColor(Color.GRAY);
                    btnSpice.setBackgroundColor(Color.WHITE);
                    btnSpice.setTextColor(Color.GRAY);
                    list.clear();
                    productAdapter.notifyDataSetChanged();
                    setDataFruitAdapter();

                }else {
                    btnFruit.setBackgroundColor(Color.WHITE);
                    btnFruit.setTextColor(Color.GRAY);
                    bolBtnFruit = false;
                    list.clear();
                    productAdapter.notifyDataSetChanged();
                    setDataAdapter();
                }

            }
        });
        btnSpice.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (bolBtnSpice == false){
                    bolBtnSpice = true;
                    bolBtnFruit = false;
                    bolBtnVegan = false;
                    btnSpice.setBackgroundColor(Color.GRAY);
                    btnSpice.setTextColor(Color.WHITE);
                    btnFruit.setBackgroundColor(Color.WHITE);
                    btnFruit.setTextColor(Color.GRAY);
                    btnVegan.setBackgroundColor(Color.WHITE);
                    btnVegan.setTextColor(Color.GRAY);
                    list.clear();
                    productAdapter.notifyDataSetChanged();
                    setDataSpiceAdapter();

                }else {
                    btnSpice.setBackgroundColor(Color.WHITE);
                    btnSpice.setTextColor(Color.GRAY);
                    bolBtnSpice = false;
                    list.clear();
                    productAdapter.notifyDataSetChanged();
                    setDataAdapter();
                }

            }
        });


        TextView txtNameAccount;
        txtNameAccount = findViewById(R.id.txtNameAccount);

        ImageView ImgProfile;
        ImgProfile = findViewById(R.id.imgProfile);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String name = sharedPreferences.getString(KEY_NAME,null);
        txtNameAccount.setText(name);

        String profileUrl = sharedPreferences.getString(KEY_PROFILE,null);
        Picasso.with(DashboardRetail.this).load(profileUrl).into(ImgProfile);

        historyActivity = findViewById(R.id.historyIcoHome);
        notifActivity = findViewById(R.id.notifIcoHome);
        settingActivity = findViewById(R.id.settingIcoHome);
        btnAction = findViewById(R.id.btnAction);

        historyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardRetail.this, HistoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        notifActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardRetail.this, NotifActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        settingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardRetail.this, SettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }

        });



    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutCart);
        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutReceipe);
        LinearLayout liveLayout = dialog.findViewById(R.id.layoutPromo);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(DashboardRetail.this,"Keranjang Belanja is clicked",Toast.LENGTH_SHORT).show();

            }
        });

        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(DashboardRetail.this,"Rekomendasi Resep is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        liveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPromoDialog();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
    private void showPromoDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_promo);

        LinearLayout promo1 = dialog.findViewById(R.id.layoutPromo1);
        LinearLayout promo2 = dialog.findViewById(R.id.layoutPromo2);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        promo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(DashboardRetail.this,"25% Discount telah di klaim dan sedang digunakan",Toast.LENGTH_SHORT).show();

            }
        });

        promo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(DashboardRetail.this,"25% Discount telah di klaim dan sedang digunakan",Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    public void showProfile(View v){
        PopupMenu popUp = new PopupMenu(this, v);
        popUp.setOnMenuItemClickListener(this);
        popUp.inflate(R.menu.profile);
        popUp.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item){
        if (item.getItemId() == R.id.logout){

            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_NAME, null);
                    editor.putString(KEY_EMAIL, null);
                    editor.putString(KEY_PROFILE, null);
                    editor.apply();

                    firebaseAuth.signOut();

                    startActivity(new Intent(DashboardRetail.this, MainActivity.class));
                    finish();
                }
            });
        }
        return false;
    }

    public void setDataAdapter(){
        recyclerView = findViewById(R.id.productList);
        mDatabase = FirebaseDatabase.getInstance().getReference("Products");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        productAdapter = new ProductAdapter(this,list);
        recyclerView.setAdapter(productAdapter);
        productAdapter.setOnItemClickCallback(this::showSelectedProduct);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Products products = dataSnapshot.getValue(Products.class);
                    list.add(products);

                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setDataVeganAdapter(){
        recyclerView = findViewById(R.id.productList);
        mDatabase = FirebaseDatabase.getInstance().getReference("Products");
        Query query = mDatabase.orderByChild("kategoriProduk").equalTo("Vegan");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        productAdapter = new ProductAdapter(this,list);
        recyclerView.setAdapter(productAdapter);
        productAdapter.setOnItemClickCallback(this::showSelectedProduct);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Products products = dataSnapshot.getValue(Products.class);
                    list.add(products);

                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setDataFruitAdapter(){
        recyclerView = findViewById(R.id.productList);
        mDatabase = FirebaseDatabase.getInstance().getReference("Products");
        Query query = mDatabase.orderByChild("kategoriProduk").equalTo("Fruit");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        productAdapter = new ProductAdapter(this,list);
        recyclerView.setAdapter(productAdapter);
        productAdapter.setOnItemClickCallback(this::showSelectedProduct);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Products products = dataSnapshot.getValue(Products.class);
                    list.add(products);

                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setDataSpiceAdapter(){
        recyclerView = findViewById(R.id.productList);
        mDatabase = FirebaseDatabase.getInstance().getReference("Products");
        Query query = mDatabase.orderByChild("kategoriProduk").equalTo("Spice");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        productAdapter = new ProductAdapter(this,list);
        recyclerView.setAdapter(productAdapter);
        productAdapter.setOnItemClickCallback(this::showSelectedProduct);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Products products = dataSnapshot.getValue(Products.class);
                    list.add(products);

                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void showSelectedProduct(Products products) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_product_sheet);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        Locale locale = new Locale("in", "ID");
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance(locale));

        TextView namaProduk = dialog.findViewById(R.id.name_select_product);
        TextView hargaProduk = dialog.findViewById(R.id.price_select_product);
        TextView totalHarga = dialog.findViewById(R.id.price_total_select_product);
        TextView kategoriProduk = dialog.findViewById(R.id.category_name_select_product);
        TextView valueProduk = dialog.findViewById(R.id.value_select_product);
        ImageView imgKategoriProduk = dialog.findViewById(R.id.category_img_select_product);
        ImageView imgProduk = dialog.findViewById(R.id.img_select_product);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
        Button incrementButton = dialog.findViewById(R.id.increment_value_product);
        Button decrementButton = dialog.findViewById(R.id.decrement_value_product);
        Button cartButton = dialog.findViewById(R.id.btnAddCart);

        namaProduk.setText(products.getNamaProduk());
        hargaProduk.setText(format.format(Integer.valueOf(products.getHargaProduk())));
        totalHarga.setText(format.format(Integer.valueOf(products.getHargaProduk())));
        kategoriProduk.setText("    "+products.getKategoriProduk());
        Picasso.with(this).load(products.getPhotoProduk()).into(imgProduk);

        if (products.getKategoriProduk().equals("Vegan") ){
            imgKategoriProduk.setImageResource(R.drawable.ic_vegan);
        } else if (products.getKategoriProduk().equals("Fruit") ){
            imgKategoriProduk.setImageResource(R.drawable.ic_fruit);
        } else {
            imgKategoriProduk.setImageResource(R.drawable.ic_spice);
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valueString = String.valueOf(valueProduk.getText());
                Integer valueInt = Integer.valueOf(valueString);
                valueProduk.setText(String.valueOf(valueInt+1));
                totalHarga.setText(format.format(Integer.valueOf(products.getHargaProduk())*(valueInt+1)));

            }
        });
        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String valueString = String.valueOf(valueProduk.getText());
                Integer valueInt = Integer.valueOf(valueString);

                if (valueInt > 1){
                    valueProduk.setText(String.valueOf(valueInt-1));
                    totalHarga.setText(format.format(Integer.valueOf(products.getHargaProduk())*(valueInt-1)));
                }

            }
        });
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Carts carts = new Carts();
                String userId = sharedPreferences.getString(KEY_ID,null);
                mDatabase = FirebaseDatabase.getInstance().getReference("Carts");
                mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            int count = (int) snapshot.getChildrenCount();
                            if (count > 0){
                                mDatabase = FirebaseDatabase.getInstance().getReference("Carts").child(userId).child("CART00"+String.valueOf(count));
                                Query query = mDatabase.orderByChild("statusItem").equalTo("Menunggu Pembayaran");
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                            Toast.makeText(DashboardRetail.this,"Masuk yg belum bayar",Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(DashboardRetail.this,"Tidak Masuk yg belum bayar",Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }else{
                                carts.setUserId(userId);
                                carts.setCartId("CART001");
                                carts.setProductId(products.getIdProduk());
                                carts.setNameItem(products.getNamaProduk());
                                carts.setQtyItem(String.valueOf(valueProduk.getText()));
                                String qtyItem = String.valueOf(valueProduk.getText());
                                String priceItem = products.getHargaProduk();
                                int totalHargaItem = Integer.valueOf(qtyItem)*Integer.valueOf(priceItem);
                                carts.setPriceItem(String.valueOf(totalHargaItem));
                                carts.setStatusItem("Menunggu Pembayaran");
                                Date currentTime = Calendar.getInstance().getTime();
                                carts.setTglItem(String.valueOf(currentTime));

                                firebaseDatabase.getReference().child("Carts").child(userId).child("CART001").child(products.getIdProduk()).setValue(carts);
                                dialog.dismiss();
                                Toast.makeText(DashboardRetail.this,"Produk "+products.getNamaProduk()+" dengan harga "+totalHarga.getText()+"  telah ditambahkan pada keranjang.",Toast.LENGTH_SHORT).show();


                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}