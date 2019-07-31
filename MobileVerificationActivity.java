package com.thedevlopershome.intranetnitrr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.thefinestartist.Base;

public class MobileVerificationActivity extends AppCompatActivity {

    private String phoneNumberString;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_mobile_verification);

        Base.initialize(this);
        sharedPreferences=getSharedPreferences("userLogInData", Activity.MODE_PRIVATE);

        String mobileVar=sharedPreferences.getString("mobileVerification","null");

        if(mobileVar.equals("true")){
            Intent intent=new Intent(MobileVerificationActivity.this,MainMenuActivity.class);
            startActivity(intent);
        }

            AccessToken accessToken = AccountKit.getCurrentAccessToken();



            if (accessToken != null) {
                if(!new OftenFunctions(MobileVerificationActivity.this).isNetworkAvailable()) {
                    Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
                }


            } else {
                phoneLogin(getCurrentFocus());
            }

            }


    public static int APP_REQUEST_CODE = 99;

    public void phoneLogin(final View view) {
        final Intent intent = new Intent(MobileVerificationActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration
        // ..
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(
            final int requestCode,
            final int resultCode,
            final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = "error in verification";
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            String accountKitId = account.getId();
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            phoneNumberString = phoneNumber.toString();
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("userphoneno",phoneNumberString);
                            editor.putString("mobileVerification","true");
                            editor.apply();
                            Toast.makeText(MobileVerificationActivity.this, phoneNumberString, Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onError(final AccountKitError error) {
                            Toast.makeText(MobileVerificationActivity.this, "error:"+error.getErrorType(), Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0,10));

                }

                // If you have an authorization code, retrieve it from
                // loginResult.getAuthorizationCode()
                // and pass it to your server and exchange it for an access token.

                // Success! Start your next activity...
                Intent intent= new Intent(MobileVerificationActivity.this,LoginActivity.class);
                Toast.makeText(this, "successfully verified", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("mobileVerification","true");
                editor.apply();
                startActivity(intent);
            }

            // Surface the result to your user in an appropriate way.

        }
    }


    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }




    }

