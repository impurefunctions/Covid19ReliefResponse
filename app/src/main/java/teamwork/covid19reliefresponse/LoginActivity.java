package teamwork.covid19reliefresponse;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import teamwork.covid19reliefresponse.model.Volunteer;


public class LoginActivity extends AppCompatActivity {

    private SignInButton signInButton;
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private  Context context;
    private FirebaseAuth firebaseAuth;
    private String TAG = LoginActivity.class.getSimpleName();
    private GoogleSignInClient googleSignInClient;
    private LoginButton fb_sign_btn;
    private CallbackManager callbackManager;
    private SignInButton iv_google;
    private DatabaseReference mRootRef;

//TODO implement volunteer login and authentication ideas are welcome. Normal users will use Facebook and Gmail accounts with the code below



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
            //    new AuthUI.IdpConfig.EmailBuilder().build(),
               // new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());
              //  new AuthUI.IdpConfig.TwitterBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setLogo(R.drawable.covid_19)
                        .setTheme(R.style.AppTheme_NoActionBar)
                        .build(),
                RC_SIGN_IN);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
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
    @Override
    public void onStop() {
        super.onStop();

        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });

    }


}