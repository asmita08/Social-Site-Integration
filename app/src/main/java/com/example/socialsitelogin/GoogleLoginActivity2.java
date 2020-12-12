package com.example.socialsitelogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.squareup.picasso.Picasso;

public class GoogleLoginActivity2 extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener{

    private ImageView googledp;
    private TextView googlename, googlemailid, googleid;
    private Button SignoutBtn;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login2);

      googledp= findViewById(R.id.googledp);
      googlename = findViewById(R.id.googlename);
      googlemailid=findViewById(R.id.googlemailid);
      googleid=findViewById(R.id.googleid);
      SignoutBtn=findViewById(R.id.signoutbtn);

      googleSignInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
      googleApiClient= new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

      SignoutBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                  @Override
                  public void onResult(@NonNull Status status) {
                      if (status.isSuccess())
                          gotoMainActivity();
                      else
                          Toast.makeText(GoogleLoginActivity2.this,"Log Out Failed",Toast.LENGTH_SHORT).show();
                  }
              });
          }
      });

    }

    private void gotoMainActivity() {

        startActivity(new Intent(GoogleLoginActivity2.this, MainActivity.class));
        finish();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private  void  handleSignInResult(GoogleSignInResult result)
    {
        if (result.isSuccess())
        {
            GoogleSignInAccount account=result.getSignInAccount();
            googlename.setText(account.getDisplayName());
            googlemailid.setText((account.getEmail()));
            googleid.setText(account.getId());
           Picasso.get().load(account.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(googledp);
        }
        else {
            gotoMainActivity();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> optionalPendingResult= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (optionalPendingResult.isDone())
        {
            GoogleSignInResult result=optionalPendingResult.get();
            handleSignInResult(result);

        }

        else
        {
            optionalPendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(result);
                }
            });
        }
     }
}