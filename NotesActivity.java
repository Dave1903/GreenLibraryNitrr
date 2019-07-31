package com.thedevlopershome.intranetnitrr;



import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Environment;
import android.os.FileUriExposedException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.os.Environment.DIRECTORY_DOWNLOADS;


public class NotesActivity extends AppCompatActivity {

    private TextView empty;

    public static String branchName;
    public static int yearValue;
    public static AnimatedExpandableListView list;
    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private List<DataHolder> dataList;
    private Context context = this;
    private BroadcastReceiver downloadComplete;
    private FloatingActionButton downloadButton;
    JsonTask json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        //get data from branch list activity
        Bundle branchListData = getIntent().getExtras();
        if (branchListData == null) {
            return;
        }

        if(!isNetworkAvailable()){
            Snackbar.make(findViewById(R.id.notesReLayout),"No Internet Connection",Snackbar.LENGTH_LONG).show();
        }


        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        dataList = new ArrayList<>();
        branchName = branchListData.getString("branchName");
        yearValue = branchListData.getInt("yearValue");
        list = (AnimatedExpandableListView) findViewById(R.id.notesList);
        downloadButton = (FloatingActionButton) findViewById(R.id.floatingDownloadButton);
        empty=(TextView)findViewById(R.id.empty);

        list.setEmptyView(empty);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           startActivity(new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS));


            }
        });


        Toast.makeText(this, branchName, Toast.LENGTH_SHORT).show();
        json=new JsonTask();
            json.execute();
        downloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.getAction())){
                    Toast.makeText(NotesActivity.this, "DOWNLOAD COMPLETE", Toast.LENGTH_SHORT).show();

                unregisterReceiver(downloadComplete);
                }
            }
        };





         list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
             @Override
             public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                 if (list.isGroupExpanded(i)) {
                     list.collapseGroupWithAnimation(i);
                 } else {
                     list.expandGroupWithAnimation(i);
                 }
                 return true;
             }
         });




        //listner for list view
        list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (isStoragePermissionGranted()) {
                    String sub = listDataHeader.get(i);
                    String fname = listDataChild.get(listDataHeader.get(i)).get(i1);
                    int k = 0;
                    for (int j = 0; j < dataList.size(); j++) {
                        if (dataList.get(j).subject.equals(sub) && dataList.get(j).fileName.equals(fname))
                            k = j;
                    }
                    final DataHolder data = dataList.get(k);
                    int fileSize = Integer.parseInt(data.fileSize);
                    String kiloMegaDecide = " ";
                    if (fileSize > 1024) {
                        fileSize = fileSize / 1024;
                        kiloMegaDecide += "MB";
                    } else kiloMegaDecide += "KB";
                    new SweetAlertDialog(NotesActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("File Detail")
                            .setContentText("Name: " + data.fileName + "\nSubject: " + data.subject + "\n format: " + data.fileFormate + "\nFile size: " + fileSize + kiloMegaDecide + "\nSubmittedBy: " + data.submitedBy)
                            .setConfirmText("Download")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();


                                    DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                                    Uri uri = Uri.parse(data.UrlLink);
                                    DownloadManager.Request request = new DownloadManager.Request(uri);
                                    request.setAllowedNetworkTypes(
                                            DownloadManager.Request.NETWORK_WIFI
                                                    | DownloadManager.Request.NETWORK_MOBILE)
                                            .setAllowedOverRoaming(false).setTitle(data.fileName)
                                            .setDescription(data.fileFormate).setVisibleInDownloadsUi(true)
                                            .setDestinationInExternalPublicDir(DIRECTORY_DOWNLOADS,
                                                    File.separator + "green laibrary nitrr" + File.separator + data.fileName + "." + data.fileFormate);


                                    downloadManager.enqueue(request);
                                    registerReceiver(downloadComplete, new IntentFilter(
                                            DownloadManager.ACTION_DOWNLOAD_COMPLETE));


                                }
                            })
                            .show();

                }
                return true;
            }
        });


    }


    private class JsonTask extends AsyncTask<Void, Void, Integer> {


        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(NotesActivity.this);
            progressDialog.setTitle("LOADING...");
            progressDialog.setMessage("please wait");
            progressDialog.setCancelable(true);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Intent i=new Intent(NotesActivity.this,BranchListActivity.class);
                    JsonTask.this.cancel(true);
                    startActivity(i);
                }
            });
            progressDialog.show();
        }

        private String makeUrl(String para) {
            String data = "https://devlopershome.000webhostapp.com/SmartAPI.php?branchName=";
            if (para.equals("FIRST YEAR"))
                data += "firstYear";
            else if (para.equals("ELECTRONICS AND TELECOMMUNICATION"))
                data += "elex";
            else if (para.equals("MINING"))
                data += "mining";
            else if (para.equals("CIVIL"))
                data += "civil";
            else if (para.equals("COMPUTER SCIENCE"))
                data += "computerScience";
            else if (para.equals("MECHANICAL"))
                data += "mechanical";
            else if (para.equals("ELECTRICAL"))
                data += "electrical";
            else if (para.equals("BIOTECHNOLOGY"))
                data += "Biotechnology";
            else if (para.equals("METALLURGY"))
                data += "metallurgy";
            else if (para.equals("CHEMICAL"))
                data += "chemical";
            else if (para.equals("BIOMEDICAL"))
                data += "Biomedical";
            else if (para.equals("INFORMATION TECHNOLOGY"))
                data += "informationTechnology";
            else if (para.equals("ARCHITECTURE"))
                data += "architecture";
            else if (para.equals("COMMON INTEREST"))
                data += "commonInterest";

            data += "&semester=" + yearValue;

            data=data.replaceAll(" ","%20");

            return data;
        }


        private StringBuilder gaveStringData() {  //to read json data from url
            StringBuilder content = new StringBuilder();
            try {
                String getUrl = makeUrl(branchName);
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
                while ((next = bufferedReader.readLine()) != null) {
                    content.append(next);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
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
            String data = gaveStringData().toString();
            try {
                JSONObject reader = new JSONObject(data);
                JSONArray jArray = reader.getJSONArray("posts");
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject nestObject = jArray.getJSONObject(i);
                    JSONObject mainObject = nestObject.getJSONObject("post");
                    DataHolder noteData = new DataHolder(mainObject.getString("name"), mainObject.getString("fileFormat"), mainObject.getString("urlLink"), mainObject.getString("subject"), mainObject.getString("fileSize"), mainObject.getString("submittedBy"));
                    dataList.add(noteData);


                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return 1;
        }


        @Override
        protected void onPostExecute(Integer i) {
            super.onPostExecute(i);
            prepList();
            progressDialog.dismiss();
            listAdapter = new ExpandableListAdapter(NotesActivity.this, listDataHeader, listDataChild);
            list.setAdapter(listAdapter);

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
                if (listDataHeader.get(i).equals(dataList.get(j).subject))
                    dataVar.add(dataList.get(j).fileName);
            }
            listDataChild.put(listDataHeader.get(i), dataVar);
            dataVar = new ArrayList<>();
        }


    }


    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.notesFeedback:
                String s="Debug-infos:";
                s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
                s += "\n OS API Level: "+android.os.Build.VERSION.RELEASE + "("+android.os.Build.VERSION.SDK_INT+")";
                s += "\n Device: " + android.os.Build.DEVICE;
                s += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
                Intent intent1 = new Intent (Intent.ACTION_SEND);
                intent1.setType("message/rfc822");
                intent1.putExtra(Intent.EXTRA_EMAIL, new String[]{"thedevelopershome@gmail.com,gogreennitrr@gmail.com"});
                intent1.putExtra(Intent.EXTRA_SUBJECT, "feedback for greenLibraryNitrr");
                intent1.putExtra(Intent.EXTRA_TEXT,s);
                intent1.setPackage("com.google.android.gm");
                if (intent1.resolveActivity(getPackageManager())!=null)
                    startActivity(intent1);
                else
                    Toast.makeText(this,"Gmail App is not installed",Toast.LENGTH_SHORT).show();
                return true;


            case R.id.refresh:
                if(!isNetworkAvailable()){
                    Snackbar.make(findViewById(R.id.notesReLayout),"No Internet Connection",Snackbar.LENGTH_LONG).show();
                }
                else
                     new JsonTask().execute();
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected(); }


}








