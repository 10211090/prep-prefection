package com.papb.prepperfection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

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

        ImageView ImgProfile;
        ImgProfile = findViewById(R.id.imgProfileSetting);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String profileUrl = sharedPreferences.getString(KEY_PROFILE,null);
        Picasso.with(SettingActivity.this).load(profileUrl).into(ImgProfile);

        DashboardActivity = findViewById(R.id.homeIcoSetting);
        historyActivity = findViewById(R.id.historyIcoSetting);
        notifActivity = findViewById(R.id.notifIcoSetting);

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