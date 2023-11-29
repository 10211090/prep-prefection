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
import com.papb.prepperfection.adapter.HistoryAdapter;
import com.papb.prepperfection.group.Carts;
import com.papb.prepperfection.group.Historys;
import com.papb.prepperfection.group.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class HistoryActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    ImageView DashboardActivity, notifActivity, settingActivity;
    Button btnAction;
    SharedPreferences sharedPreferences;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mDatabase;
    Integer intCartValue = 0;
    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;
    ArrayList<Historys> list;
    String userId;
    int valCart;
    String cartID = null;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PROFILE = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        ImageView ImgProfile;
        ImgProfile = findViewById(R.id.imgProfileHistory);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String profileUrl = sharedPreferences.getString(KEY_PROFILE,null);
        Picasso.with(HistoryActivity.this).load(profileUrl).into(ImgProfile);

        TextView intCart = findViewById(R.id.int_cart);

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

        setDataHistoryAdapter();

        DashboardActivity = findViewById(R.id.homeIcoHistory);
        notifActivity = findViewById(R.id.notifIcoHistory);
        settingActivity = findViewById(R.id.settingIcoHistory);
        btnAction = findViewById(R.id.btnAction);

        DashboardActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, DashboardRetail.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        notifActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, NotifActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        settingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, SettingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
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

        TextView intCartSheet = dialog.findViewById(R.id.int_cart_sheet);
        if (intCartValue > 0){
            intCartSheet.setText(String.valueOf(intCartValue));
        }

        videoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intCartValue > 0){
                    dialog.dismiss();
                    startActivity(new Intent(HistoryActivity.this, CartActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                }else{
                    dialog.dismiss();
                    Toast.makeText(HistoryActivity.this,"Silahkan memasukkan Produk ke Keranjang Belanja terlebih dahulu",Toast.LENGTH_SHORT).show();
                }
            }
        });

        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                startActivity(new Intent(HistoryActivity.this, ResepActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));

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
                Toast.makeText(HistoryActivity.this,"25% Discount telah di klaim dan sedang digunakan",Toast.LENGTH_SHORT).show();

            }
        });

        promo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(HistoryActivity.this,"25% Discount telah di klaim dan sedang digunakan",Toast.LENGTH_SHORT).show();

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

                    startActivity(new Intent(HistoryActivity.this, MainActivity.class));
                    finish();
                }
            });
        }
        return false;
    }

    public void setDataHistoryAdapter(){
        String userId = sharedPreferences.getString(KEY_ID,null);
        recyclerView = findViewById(R.id.historyList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Orders");
        list = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this,list);
        recyclerView.setAdapter(historyAdapter);

        mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = (int) snapshot.getChildrenCount();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (count > 0){
                        Historys historys = dataSnapshot.getValue(Historys.class);
                        list.add(historys);

                        }else{
                            TextView noHistory = findViewById(R.id.txtNoHistory);
                            noHistory.setText("Tidak ada Data History");
                        }

                historyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}