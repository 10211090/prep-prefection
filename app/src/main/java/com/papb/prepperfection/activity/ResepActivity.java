package com.papb.prepperfection.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.papb.prepperfection.adapter.ResepAdapter;
import com.papb.prepperfection.group.Carts;
import com.papb.prepperfection.group.Products;
import com.papb.prepperfection.group.Reseps;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

public class ResepActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    SharedPreferences sharedPreferences;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase, mDatabaseOrder;
    Integer intCartValue = 0;
    ImageView dashboardActivity, historyActivity, notifActivity, settingActivity;
    Button btnAction;
    RecyclerView recyclerView;
    ResepAdapter resepAdapter;
    ArrayList<Reseps> list;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PROFILE = "profile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resep);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        ImageView ImgProfile;
        ImgProfile = findViewById(R.id.imgProfileResep);

        String profileUrl = sharedPreferences.getString(KEY_PROFILE,null);
        Picasso.with(ResepActivity.this).load(profileUrl).into(ImgProfile);
        TextView intCart = findViewById(R.id.int_cart);

        dashboardActivity = findViewById(R.id.homeIcoResep);
        historyActivity = findViewById(R.id.historyIcoResep);
        notifActivity = findViewById(R.id.notifIcoResep);
        settingActivity = findViewById(R.id.settingIcoResep);
        btnAction = findViewById(R.id.btnAction);

        dashboardActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResepActivity.this, DashboardRetail.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        historyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResepActivity.this, HistoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        notifActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResepActivity.this, NotifActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        settingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResepActivity.this, SettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

        mDatabase = firebaseDatabase.getReference("Carts").child(String.valueOf(sharedPreferences.getString(KEY_ID,null)));
        Query query = mDatabase;

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    int count = (int) snapshot.getChildrenCount();
                    mDatabase = firebaseDatabase.getReference("Carts").child(String.valueOf(sharedPreferences.getString(KEY_ID,null))).child("CART00"+String.valueOf(count));
                    Query query = mDatabase.orderByChild("statusItem").equalTo("Menunggu Pembayaran");

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                int count = (int) snapshot.getChildrenCount();
                                intCartValue = count;
                                intCart.setText(String.valueOf(count));
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showResepDialog();

    }
    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet);

        LinearLayout videoLayout = dialog.findViewById(R.id.layoutCart);
        LinearLayout shortsLayout = dialog.findViewById(R.id.layoutReceipe);
        LinearLayout liveLayout = dialog.findViewById(R.id.layoutPromo);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        TextView intCartSheet = dialog.findViewById(R.id.int_cart_sheet);
        if (intCartValue > 0){
            intCartSheet.setText(String.valueOf(intCartValue));
        }

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intCartValue > 0){
                    dialog.dismiss();
                    startActivity(new Intent(ResepActivity.this, CartActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                }else{
                    dialog.dismiss();
                    Toast.makeText(ResepActivity.this,"Silahkan memasukkan Produk ke Keranjang Belanja terlebih dahulu",Toast.LENGTH_SHORT).show();
                }
            }
        });

        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                startActivity(new Intent(ResepActivity.this, ResepActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

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
                Toast.makeText(ResepActivity.this,"25% Discount telah di klaim dan sedang digunakan",Toast.LENGTH_SHORT).show();

            }
        });

        promo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(ResepActivity.this,"25% Discount telah di klaim dan sedang digunakan",Toast.LENGTH_SHORT).show();

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

                    startActivity(new Intent(ResepActivity.this, MainActivity.class));
                    finish();
                }
            });
        }
        return false;
    }
    public void showResepDialog() {
        recyclerView = findViewById(R.id.resepList);
        mDatabase = FirebaseDatabase.getInstance().getReference("Resep");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        resepAdapter = new ResepAdapter(this,list);
        recyclerView.setAdapter(resepAdapter);
        resepAdapter.setOnItemClickCallback(this::showSelectedResep);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Reseps reseps = dataSnapshot.getValue(Reseps.class);
                    list.add(reseps);

                }
                resepAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void showSelectedResep(Reseps reseps) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_resep_sheet);

        TextView namaResep = dialog.findViewById(R.id.name_select_resep);
        TextView kategoriResep = dialog.findViewById(R.id.category_select_resep);
        TextView bahanResep = dialog.findViewById(R.id.material_select_resep);
        TextView buatResep = dialog.findViewById(R.id.how_select_resep);
        ImageView imgResep = dialog.findViewById(R.id.img_select_resep);

        namaResep.setText(reseps.getNamaResep());
        kategoriResep.setText(reseps.getJenisResep());
        bahanResep.setText(reseps.getBahanResep());
        buatResep.setText(reseps.getBuatResep());
        Picasso.with(this).load(reseps.getFotoResep()).into(imgResep);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}