package com.thedevlopershome.intranetnitrr;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class GreenLibraryActivity extends AppCompatActivity {

    public static AnimatedExpandableListView greenBooksList;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader ;
    HashMap<String, List<String>> listDataChild;
    List<String> listDataHeader1 ;
    HashMap<String, List<String>> listDataChild1;
    private List<BookData> dataList;
    SharedPreferences sharedPreferences;
   private GreenLibraryData greendata;
    private TextView emptylibrary;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_library);

        if(!isNetworkAvailable()){
            Snackbar.make(findViewById(R.id.relLayoutgreen),"INTERNET CONNECTION NEEDED",Snackbar.LENGTH_SHORT).show();
        }

        sharedPreferences=getSharedPreferences("userLogInData",Activity.MODE_PRIVATE);
        final String useRname=sharedPreferences.getString("name","null");


        emptylibrary=(TextView)findViewById(R.id.emptylibrary);

        dataList=new ArrayList<>();
        listDataHeader=new ArrayList<>();
        listDataChild = new HashMap<String, List<String>>();

        listDataHeader1=new ArrayList<>();
        listDataChild1 = new HashMap<String, List<String>>();

        greenBooksList=(AnimatedExpandableListView) findViewById(R.id.GreenLibraryBookList);
        greenBooksList.setEmptyView(emptylibrary);
               greendata= new GreenLibraryData();
               greendata.execute();


        greenBooksList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                if (greenBooksList.isGroupExpanded(i)) {
                    greenBooksList.collapseGroupWithAnimation(i);
                } else {
                    greenBooksList.expandGroupWithAnimation(i);
                }
                return true;
            }
        });


        greenBooksList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                final String subject= listDataHeader1.get(i);
                final String[] writer = listDataChild1.get(listDataHeader1.get(i)).get(i1).split("-");

                if(useRname.equals("guest")){

                    new SweetAlertDialog(GreenLibraryActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("ALERT")
                            .setContentText("you have to sign in first !!!")
                            .setConfirmText("signin")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent = new Intent(GreenLibraryActivity.this, LoginActivity.class);
                                    startActivity(intent);

                                }
                            })
                            .show();


                }
                else {
                    new SweetAlertDialog(GreenLibraryActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("register for book")
                            .setContentText("Do you want to register for " + writer[0] + ",subject " + subject + " ??")
                            .setConfirmText("yes")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent = new Intent(GreenLibraryActivity.this, BookRegistrationActivity.class);
                                    intent.putExtra("writer", writer[0]);
                                    intent.putExtra("subject", subject);
                                    startActivity(intent);

                                }
                            })
                            .show();
                    }


                return  true;

            }
        });



    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected(); }


    private class GreenLibraryData extends AsyncTask<Void,Void,Integer> {


        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(GreenLibraryActivity.this);
            progressDialog.setTitle("LOADING...");
            progressDialog.setMessage("please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(true);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Intent i=new Intent(GreenLibraryActivity.this,MainMenuActivity.class);
                    greendata.cancel(true);
                    startActivity(i);
                }
            });
            progressDialog.show();
        }






        private StringBuilder gaveStringData(){  //to read json data from url
            StringBuilder content = new StringBuilder();
            try {
                String getUrl="http://devlopershome.000webhostapp.com/GreenLibraryBooksAPI.php";
                URL url = new URL(getUrl);
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
                urlConnection.disconnect();
            }
            catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return content;
        }





        @Override
        protected Integer doInBackground(Void... voids) {





            String data=gaveStringData().toString();
            try {
                JSONObject reader = new JSONObject(data);
                JSONArray jArray = reader.getJSONArray("posts");
                for(int i=0;i<jArray.length();i++){
                    JSONObject nestObject=jArray.getJSONObject(i);
                    JSONObject mainObject=nestObject.getJSONObject("post");
                    BookData bookData=new BookData(mainObject.getString("writer"),mainObject.getString("subject"),mainObject.getString("quantity"));
                    dataList.add(bookData);

                }




            }
            catch(JSONException e){
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return 1;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            prepList();
            progressDialog.dismiss();
            listDataHeader1=listDataHeader;
            listDataChild1=listDataChild;
            listAdapter = new ExpandableListAdapter(GreenLibraryActivity.this, listDataHeader, listDataChild);
            greenBooksList.setAdapter(listAdapter);



        }
    }


    public void prepList() {

        ArrayList<String> dataVar = new ArrayList<>();
        int count = 0;


        try {
            for (int i = 0; i < dataList.size(); i++) {
                count = 0;
                listDataHeader.add(dataList.get(i).subject);

                for (int j = 0; j < listDataHeader.size(); j++) {
                    if (listDataHeader.get(listDataHeader.size() - 1).equals(listDataHeader.get(j))) {
                        count++;
                        if (count > 1)
                            listDataHeader.remove(j);
                    }
                }
            }

        } catch (IndexOutOfBoundsException e) {
        }

        for (int i = 0; i < listDataHeader.size(); i++) {
            for (int j = 0; j < dataList.size(); j++) {
                if (listDataHeader.get(i).equals(dataList.get(j).subject)){
                     String set;
                      if(Integer.parseInt(dataList.get(j).quantity)>1)
                          set="pcs";
                    else
                        set="pc";
                    dataVar.add(dataList.get(j).writerName+" - "+dataList.get(j).quantity+" "+ set);
                }
            }
            listDataChild.put(listDataHeader.get(i), dataVar);
            dataVar = new ArrayList<>();
        }



    }



    @Override
    public void onBackPressed() {
        Intent a = new Intent(GreenLibraryActivity.this,MainMenuActivity.class);
        startActivity(a);
    }

    private SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.green_menu,menu);
        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(GreenLibraryActivity.this, "Search by book writer name", Toast.LENGTH_SHORT).show();

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterData(query);
                searchView.clearFocus();
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                filterData(s);
                return true;
             }
        });
        return true;
    }




    public void filterData(String query){

        List<String> newList =new ArrayList<>();
        HashMap<String, List<String>> dataChild=new HashMap<String, List<String>>();

        if(query.isEmpty()){
            listDataChild1=listDataChild;
            listDataHeader1=listDataHeader;
            listAdapter=new ExpandableListAdapter(GreenLibraryActivity.this, listDataHeader, listDataChild);
            greenBooksList.setAdapter(listAdapter);

        }
        else {

            for(int i=0;i<listDataHeader.size();i++){
                ArrayList<String> temp =new ArrayList<>();
                for(int j=0;j<listDataChild.get(listDataHeader.get(i)).size();j++){
                           if(listDataChild.get(listDataHeader.get(i)).get(j).toUpperCase().contains(query.toUpperCase())){
                                temp.add((listDataChild.get(listDataHeader.get(i)).get(j)));
                            }
               }

                   if(!temp.isEmpty()){
                       newList.add(listDataHeader.get(i));
                       dataChild.put(listDataHeader.get(i),temp);
                   }

            }
                if(newList.size() > 0){
                    listDataChild1=dataChild;
                    listDataHeader1=newList;

                    listAdapter=new ExpandableListAdapter(GreenLibraryActivity.this, newList, dataChild);
                    greenBooksList.setAdapter(listAdapter);
                }
                else{
                    newList.add("No results found for \" "+ query+"\"");
                    dataChild.put("No results found for \" "+ query+"\"",new ArrayList<String>());
                    listAdapter=new ExpandableListAdapter(GreenLibraryActivity.this, newList, dataChild);
                    greenBooksList.setAdapter(listAdapter);
                }
            }
        }




    }
