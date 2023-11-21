package com.papb.prepperfection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SettingActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    ImageView DashboardActivity, historyActivity, notifActivity;
    Button btnAction;
    Button btnLogout;
    SharedPreferences sharedPreferences;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PROFILE = "profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        TextView NameAccount, EmailAccount;
        NameAccount = findViewById(R.id.nameProfileSetting);
        NameAccount.setText(sharedPreferences.getString(KEY_NAME,null));

        EmailAccount = findViewById(R.id.emailProfileSetting);
        EmailAccount.setText(sharedPreferences.getString(KEY_EMAIL,null));

        ImageView ImgProfile,ImgProfileMenu;
        ImgProfile = findViewById(R.id.imgProfileSetting);
        ImgProfileMenu = findViewById(R.id.imgProfileSettingMenu);


        String profileUrl = sharedPreferences.getString(KEY_PROFILE,null);
        Picasso.with(SettingActivity.this).load(profileUrl).into(ImgProfile);
        Picasso.with(SettingActivity.this).load(profileUrl).into(ImgProfileMenu);

        DashboardActivity = findViewById(R.id.homeIcoSetting);
        historyActivity = findViewById(R.id.historyIcoSetting);
        notifActivity = findViewById(R.id.notifIcoSetting);
        btnLogout = findViewById(R.id.btnLogout);
        btnAction = findViewById(R.id.btnAction);

        DashboardActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, DashboardRetail.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        historyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, HistoryActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        notifActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, NotifActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_NAME, null);
                        editor.putString(KEY_EMAIL, null);
                        editor.putString(KEY_PROFILE, null);
                        editor.apply();

                        firebaseAuth.signOut();

                        startActivity(new Intent(SettingActivity.this,MainActivity.class));
                        finish();
                    }
                });
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
                Toast.makeText(SettingActivity.this,"Keranjang Belanja is clicked",Toast.LENGTH_SHORT).show();

            }
        });

        shortsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(SettingActivity.this,"Rekomendasi Resep is Clicked",Toast.LENGTH_SHORT).show();

            }
        });

        liveLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(SettingActivity.this,"Voucher Promo is Clicked",Toast.LENGTH_SHORT).show();

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

                    startActivity(new Intent(SettingActivity.this,MainActivity.class));
                    finish();
                }
            });
        }
        return false;
    }

}