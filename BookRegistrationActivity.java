package com.thedevlopershome.intranetnitrr;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class BookRegistrationActivity extends AppCompatActivity {

    Spinner branchSpinner;
    Spinner semSpinner;
    EditText nameText,contactText,rollNo;
    TextView requestButton;
    SharedPreferences sharedPreferences;
    String oneTimeRegistration;
    private String subject;
    private String writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_registration);

        Bundle greenData = getIntent().getExtras();
        if (greenData == null) {
            Toast.makeText(this, "no data passed", Toast.LENGTH_SHORT).show();
            return;
        }

        subject=greenData.getString("subject");
        writer=greenData.getString("writer");


        if(!isNetworkAvailable()){
            Snackbar.make(findViewById(R.id.bookRegLnear),"INTERNET CONNECTION NEEDED",Snackbar.LENGTH_SHORT).show();
        }




        branchSpinner=(Spinner)findViewById(R.id.branchSpiner1);
        semSpinner=(Spinner)findViewById(R.id.semSpiner1);
        nameText=(EditText)findViewById(R.id.nameText);
        contactText=(EditText)findViewById(R.id.contactText);
        rollNo=(EditText)findViewById(R.id.rollNoText);
        requestButton=(TextView)findViewById(R.id.requestButton);

        sharedPreferences=getSharedPreferences("userLogInData", Activity.MODE_PRIVATE);
        String name=sharedPreferences.getString("name","null");
        String mobileNo=sharedPreferences.getString("userphoneno","0000000000");
        oneTimeRegistration=sharedPreferences.getString("oneTimeRegistration","null");


        if(oneTimeRegistration.equals("true")){
            new SweetAlertDialog(BookRegistrationActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Already Registered")
                    .setContentText("you have Already registered for one book")
                    .setConfirmText("okay")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Intent intent = new Intent(BookRegistrationActivity.this, MainMenuActivity.class);
                            startActivity(intent);

                        }
                    })
                    .show();
        }


        nameText.setText(name );
        contactText.setText(mobileNo);
        contactText.setEnabled(false);

        String[] setSem={"1","2","3","4","5","6","7","8"};
        ArrayAdapter<String> semAdapter=new ArrayAdapter<String>(BookRegistrationActivity.this,R.layout.support_simple_spinner_dropdown_item,setSem);
        semSpinner.setAdapter(semAdapter);

        final ArrayList<String> branchArrasyList = new ArrayList <String>();
        branchArrasyList.add(0,"ARCHITECTURE");
        branchArrasyList.add(0,"INFORMATION TECHNOLOGY");
        branchArrasyList.add(0,"COMPUTER SCIENCE");
        branchArrasyList.add(0,"ELECTRONICS AND TELECOMMUNICATION");
        branchArrasyList.add(0,"ELECTRICAL");
        branchArrasyList.add(0,"MECHANICAL");
        branchArrasyList.add(0,"MINING");
        branchArrasyList.add(0,"METALLURGY");
        branchArrasyList.add(0,"CHEMICAL");
        branchArrasyList.add(0,"BIOTECHNOLOGY");
        branchArrasyList.add(0,"BIOMEDICAL");
        branchArrasyList.add(0,"CIVIL");
        branchArrasyList.add(0,"MCA");



        ArrayAdapter<String>  bindBranchList= new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,branchArrasyList);
        branchSpinner.setAdapter(bindBranchList);


    }



    private String  createUrl(){
        String requestUrl="http://devlopershome.000webhostapp.com/BookRequestAPI.php?";
        requestUrl+="name="+nameText.getText()+"&semester="+semSpinner.getSelectedItem()+"&branch="+branchSpinner.getSelectedItem();
        requestUrl+="&rollNo="+rollNo.getText()+"&contactNo="+contactText.getText();
        requestUrl+="&bookName="+writer+"&subject="+subject;
        requestUrl=requestUrl.replaceAll(" ","%20");
        return requestUrl;
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected(); }



    public void onSendRequest(View view){
             if(!isNetworkAvailable())
                 Snackbar.make(findViewById(R.id.bookRegLnear),"No Internet Connection",Snackbar.LENGTH_LONG).show();
        else {    if(checkData())
                   new SendRequest().execute();
             }
    }



    private boolean checkData(){
        if(contactText.getText().length()<10){
            Toast.makeText(BookRegistrationActivity.this, "invalid phone Number enter 10 digit number please", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        if(rollNo.getText().length()==0){
            Toast.makeText(BookRegistrationActivity.this, "please enter valid roll no", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        if(nameText.getText().length()==0){
            Toast.makeText(BookRegistrationActivity.this, "name cannot be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return true;

    }


    private class SendRequest extends AsyncTask<Void,Void,String>{

        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(Void... voids) {
            return uploadToServer();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(BookRegistrationActivity.this, "sending request...", "Please wait", true);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals("sucess")){
                new SweetAlertDialog(BookRegistrationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Thank YOU")
                        .setContentText("Your request has been successfully added,we will contact you if you are eligible after data processing" +
                                ".")
                        .setConfirmText("CONTINUE")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {


                                sDialog.dismissWithAnimation();
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("oneTimeRegistration","true");
                                editor.apply();
                                Intent intent=new Intent(BookRegistrationActivity.this,GreenLibraryActivity.class);
                                startActivity(intent);



                            }
                        })
                        .show();
            }
            else
            {
                new SweetAlertDialog(BookRegistrationActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Error processing your request")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {


                                sDialog.dismissWithAnimation();




                            }
                        })
                        .show();
            }
        }
    }



    String uploadToServer(){
        StringBuilder content = new StringBuilder();
        try {

            URL url = new URL(createUrl());
            HttpURLConnection urlConnection =
                    (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // gets the server json data
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
            String next;
            while ((next = bufferedReader.readLine()) != null)
            {
                content.append(next);
            }
            bufferedReader.close();



        }
        catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return content.toString();

    }






}
