package teamwork.covid19reliefresponse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

        import android.os.Bundle;
        import android.text.TextUtils;
        import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.facebook.AccessToken;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.login.LoginResult;
        import com.facebook.login.widget.LoginButton;

        import com.google.android.gms.auth.api.Auth;
        import com.google.android.gms.auth.api.signin.GoogleSignIn;
        import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
        import com.google.android.gms.auth.api.signin.GoogleSignInClient;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.auth.api.signin.GoogleSignInResult;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.SignInButton;
        import com.google.android.gms.common.api.ApiException;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthCredential;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FacebookAuthProvider;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.auth.FirebaseUserMetadata;
        import com.google.firebase.auth.GoogleAuthProvider;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.Map;

import teamwork.covid19reliefresponse.model.Volunteer;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private  Context context;
    private DatabaseReference mRootRef;
    private FirebaseAuth firebaseAuth;
    private String TAG = LoginActivity.class.getSimpleName();
    private GoogleSignInClient googleSignInClient;
    private LoginButton fb_sign_btn;
    private CallbackManager callbackManager;
    private SignInButton iv_google;

//TODO implement volunteer login and authentication ideas are welcome. Normal users will use Facebook and Gmail accounts with the code below



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        getSupportActionBar().hide();


        init();
        setClickListeners();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        configureFacebookSignIn();
        configureGoogleSignIn();

        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        //Button gButton=
    }


    private void configureFacebookSignIn() {
        callbackManager = CallbackManager.Factory.create();
        fb_sign_btn.setReadPermissions("email", "public_profile");
        fb_sign_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e(TAG, "facebook:onError" + error.getLocalizedMessage());
                Log.i(TAG, "facebook:onError YOOOOOOOP" + error.getMessage());
                System.out.print("facebook:onError YOOOOOOOP " + error.getMessage());
                String message=error.getMessage();
                Toast.makeText(context, "FacebookException : " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.e(TAG, "handleFacebookAccessToken :" + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, "signInWithCredential:success");
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        updateUI(currentUser);
                    }
                } else {
                    Log.e(TAG, "signInWithCredential:failure " + task.getException());
                    Toast.makeText(context, "signInWithCredential:failure :" + task.getException(), Toast.LENGTH_SHORT).show();
//                    updateUI(null);
                }

            }
        });
    }

    private void setClickListeners() {
//        google_sign_in_btn.setOnClickListener(this);
//        login_btn.setOnClickListener(this);
        //tv_register.setOnClickListener(this);
        //tv_forgotPassword.setOnClickListener(this);
        // iv_google.setOnClickListener(this);
        //  iv_facebook.setOnClickListener(this);
    }

    private void configureGoogleSignIn() {

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))        // for firebase web server authentication
                .requestEmail()
                .build();


        // for custom developed API i.e own server
//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();

        googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "on resume Called");
        // if (progress_bar.getVisibility() == View.VISIBLE)
        //    progress_bar.setVisibility(View.GONE);

    }


    private void updateUI(FirebaseUser firebaseUser) {
        //  String email = firebaseUser.getEmail();

        String displayName = firebaseUser.getDisplayName();
        Intent intent = new Intent(context, SignUp.class);    // launch MainActivity  if exist  // hide google sign in button if not
        //intent.putExtra(Constants.KEY_USER_EMAIL, email);
        intent.putExtra(Constants.KEY_USER_DISPLAY_NAME, displayName);
        startActivity(intent);
        finish();

    }

    private void checkUser(){
        //TODO LIST
        //Volunteer lists will be updated by us
        //put up list with each volunteer email,name, and type of volunteer Housing or food hamper,organization
        //when volunteer logs in with Gmail check them against volunteer list if they exist ask how they would like to login as just a user or a volunteer
        //if volunteer is picked remember it with and display option to switch to user
        //first time login as a volunteer get userid and put it into volunteer object and save it
        //every other time check if the userid exists in the object if not get it and save it (use it to sign every decision the volunteer makes)
        //okay that looks like that's  it

        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userEmail = currentUser.getEmail();
        String uid = currentUser.getUid();
        DatabaseReference volunteerRef = mRootRef.child("Volunteers");
        Query volunteerQuery = mRootRef.child("Volunteers").orderByChild("email").equalTo(userEmail);
        volunteerQuery.keepSynced(true);



        volunteerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot ds: dataSnapshot.getChildren())
                    {
                        Volunteer volunteer= ds.getValue(Volunteer.class);
                        if(volunteer.getId().equals(uid)){
                            //Welcome user back
                            //send them to mainActivity

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("key",volunteer.getType());// launch MainActivity  if exist  // hide google sign in button if not
                            startActivity(intent);
                        }
                        else if(!(volunteer.getId().equals(uid))){
                            //possible clerical error
                            //really i dont know i just dont need a misdiagnosis


                        }else if(volunteer.getId()==null){

                            //Volunteer is new give them a tour or something
                            //put their userid in the object and put it back

                            String key = volunteerQuery.getRef().getKey();

                            volunteer.setId(uid);
                            volunteerRef.child(key).setValue(volunteer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    //toast to success

                                    LayoutInflater inflater = getLayoutInflater();
                                    View layout = inflater.inflate(R.layout.custom_toast,
                                            (ViewGroup) findViewById(R.id.custom_toast_container));

                                    TextView text = (TextView) layout.findViewById(R.id.text);
                                    text.setText("Welcome"+ currentUser.getDisplayName());

                                    Toast toast = new Toast(context);
                                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                                    toast.setDuration(Toast.LENGTH_LONG);
                                    toast.setView(layout);
                                    toast.show();


                                    Intent intent = new Intent(context, MainActivity.class);    // launch MainActivity  if exist  // hide google sign in button if not
                                    intent.putExtra("key",volunteer.getType());
                                    startActivity(intent);
                                }
                            });


                        }
                    }
                }else{
//this just a normal user that logged in with Gmail
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("key","user");// launch MainActivity  if exist  // hide google sign in button if not
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }
    private void init() {

        context = this;
       // progress_bar = findViewById(R.id.progress_bar);

        fb_sign_btn = findViewById(R.id.login_button);
        iv_google = findViewById(R.id.sign_in_button);



    }

    private void googleSignIn() {

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(signInAccountTask);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);    // for facebook integration
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount googleSignInAccount = completedTask.getResult(ApiException.class);
            if (googleSignInAccount != null) {
                fireBaseAuthWithGoogle(googleSignInAccount);   // signed in successfully, authenticate with firebase
            }
        } catch (ApiException e) {
            Log.e(TAG, "signInResult:failed code= " + e.getStatusCode());
//            updateUI(null);
        }

    }

    private void fireBaseAuthWithGoogle(GoogleSignInAccount googleSignInAccount) {
        Log.e(TAG, "fireBaseAuthWithGoogle : " + googleSignInAccount.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {
                    Log.e(TAG, "signInWithCredential success");
                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        //  updateUI(currentUser);
                     checkUser();
                    }
                } else {
                    Log.e(TAG, "signInWithCredential : failure", task.getException());
                    Toast.makeText(context, "signInWithCredential:failure :" + task.getException(), Toast.LENGTH_SHORT).show();
                    // Util.showSnackBar(findViewById(R.id.main_layout), getString(R.string.authentication_failed));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.sign_in_button:
//                google_sign_in_btn.performClick();
                googleSignIn();
                break;
            case R.id.login_button:
                fb_sign_btn.performClick();
                break;


        }
    }



    @Override
    public void onStop() {
        super.onStop();

    }


}