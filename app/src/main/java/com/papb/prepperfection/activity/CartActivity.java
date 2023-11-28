package com.papb.prepperfection.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.papb.prepperfection.adapter.CartAdapter;
import com.papb.prepperfection.adapter.ProductAdapter;
import com.papb.prepperfection.group.Carts;
import com.papb.prepperfection.group.Products;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

public class CartActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    SharedPreferences sharedPreferences;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;
    Integer totalCartValue = 0;
    Boolean bolBtnDisc1 = false, bolBtnDisc2= false, valueAdapter = false, btnCartAdapter = false;
    ImageView dashboardActivity, historyActivity, notifActivity, settingActivity;
    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    ArrayList<Carts> list;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PROFILE = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        ImageView ImgProfile;
        ImgProfile = findViewById(R.id.imgProfileCart);

        String profileUrl = sharedPreferences.getString(KEY_PROFILE,null);
        Picasso.with(CartActivity.this).load(profileUrl).into(ImgProfile);

        dashboardActivity = findViewById(R.id.homeIcoCart);
        historyActivity = findViewById(R.id.historyIcoCart);
        notifActivity = findViewById(R.id.notifIcoCart);
        settingActivity = findViewById(R.id.settingIcoCart);

        dashboardActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, DashboardRetail.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        historyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, HistoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        notifActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, NotifActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        settingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, SettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        Button disc1 = findViewById(R.id.btn25disc);
        Button disc2 = findViewById(R.id.btn50disc);

        Button buyCart = findViewById(R.id.btnBuyCart);

        setCartAdapter();


        buyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnCartAdapter == false){
                    valueAdapter=true;
                    Carts carts = new Carts();
                    String userId = sharedPreferences.getString(KEY_ID,null);
                    mDatabase = FirebaseDatabase.getInstance().getReference("Carts");
                    mDatabase.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()){
                                int count = (int) snapshot.getChildrenCount();
                                mDatabase.child(userId).child("CART00"+String.valueOf(count)).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()){
                                                if (btnCartAdapter == false){
                                                    btnCartAdapter = true;
                                                    Integer cartDel = count+1;
                                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                                        Carts carts1 = dataSnapshot.getValue(Carts.class);
                                                        mDatabase.child(userId).child(String.valueOf(carts1.getCartId())).child(String.valueOf(carts1.getProductId())).child("statusItem").setValue("Sudah Dibayar");

                                                    }

                                                    Toast.makeText(CartActivity.this,"Keranjang Belanja berhasil di Belanja.",Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(CartActivity.this, DashboardRetail.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                                                }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        disc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bolBtnDisc1 == false){
                    bolBtnDisc1 = true;
                    bolBtnDisc2 = false;
                    Integer discTotalCart = totalCartValue * 75 / 100;
                    NumberFormat format = NumberFormat.getCurrencyInstance();
                    Locale locale = new Locale("in", "ID");
                    format.setMaximumFractionDigits(0);
                    format.setCurrency(Currency.getInstance(locale));

                    TextView totalHargaCart = findViewById(R.id.price_total_cart);
                    disc1.setBackgroundColor(Color.GRAY);
                    disc1.setTextColor(Color.WHITE);
                    disc2.setBackgroundColor(Color.WHITE);
                    disc2.setTextColor(Color.GRAY);
                    totalHargaCart.setText(format.format(Integer.valueOf(discTotalCart)));
                }else{
                    bolBtnDisc1 = false;
                    NumberFormat format = NumberFormat.getCurrencyInstance();
                    Locale locale = new Locale("in", "ID");
                    format.setMaximumFractionDigits(0);
                    format.setCurrency(Currency.getInstance(locale));

                    TextView totalHargaCart = findViewById(R.id.price_total_cart);
                    disc1.setBackgroundColor(Color.WHITE);
                    disc1.setTextColor(Color.GRAY);
                    totalHargaCart.setText(format.format(Integer.valueOf(totalCartValue)));
                }


            }
        });
        disc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bolBtnDisc2 == false){
                    bolBtnDisc2 = true;
                    bolBtnDisc1 = false;
                    Integer discTotalCart = totalCartValue * 50 / 100;
                    NumberFormat format = NumberFormat.getCurrencyInstance();
                    Locale locale = new Locale("in", "ID");
                    format.setMaximumFractionDigits(0);
                    format.setCurrency(Currency.getInstance(locale));

                    TextView totalHargaCart = findViewById(R.id.price_total_cart);
                    disc2.setBackgroundColor(Color.GRAY);
                    disc2.setTextColor(Color.WHITE);
                    disc1.setBackgroundColor(Color.WHITE);
                    disc1.setTextColor(Color.GRAY);
                    totalHargaCart.setText(format.format(Integer.valueOf(discTotalCart)));
                }else{
                    bolBtnDisc2 = false;
                    NumberFormat format = NumberFormat.getCurrencyInstance();
                    Locale locale = new Locale("in", "ID");
                    format.setMaximumFractionDigits(0);
                    format.setCurrency(Currency.getInstance(locale));

                    TextView totalHargaCart = findViewById(R.id.price_total_cart);
                    disc2.setBackgroundColor(Color.WHITE);
                    disc2.setTextColor(Color.GRAY);
                    totalHargaCart.setText(format.format(Integer.valueOf(totalCartValue)));
                }


            }
        });
    }
    public void setCartAdapter(){
        if (valueAdapter == false){
            String userId = sharedPreferences.getString(KEY_ID,null);
            recyclerView = findViewById(R.id.cartList);
            mDatabase = FirebaseDatabase.getInstance().getReference("Carts");
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            list = new ArrayList<>();
            cartAdapter = new CartAdapter(this,list);
            recyclerView.setAdapter(cartAdapter);

            mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        int count = (int) snapshot.getChildrenCount();
                        TextView idCart = findViewById(R.id.idCart);
                        String idCartValue = userId + "CART00"+String.valueOf(count);
                        idCart.setText("ID Pesanan : "+idCartValue.toUpperCase());
                        Query query = mDatabase.child(userId).child("CART00"+String.valueOf(count)).orderByChild("statusItem").equalTo("Menunggu Pembayaran");
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    if (valueAdapter == false){
                                        totalCartValue = totalCartValue + Integer.valueOf(dataSnapshot.child("priceItem").getValue().toString());
                                        Carts carts = dataSnapshot.getValue(Carts.class);
                                        list.add(carts);
                                    }
                                }

                                cartAdapter.notifyDataSetChanged();
                                NumberFormat format = NumberFormat.getCurrencyInstance();
                                Locale locale = new Locale("in", "ID");
                                format.setMaximumFractionDigits(0);
                                format.setCurrency(Currency.getInstance(locale));

                                TextView totalHargaCart = findViewById(R.id.price_total_cart);
                                totalHargaCart.setText(format.format(Integer.valueOf(totalCartValue)));
                                valueAdapter=true;

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

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

                    startActivity(new Intent(CartActivity.this, MainActivity.class));
                    finish();
                }
            });
        }
        return false;
    }
}