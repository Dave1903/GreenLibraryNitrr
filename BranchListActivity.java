package com.thedevlopershome.intranetnitrr;


import android.*;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.app.Notification;

import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.ListView;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;


import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class BranchListActivity extends AppCompatActivity  {

     private static TabHost tabHost;
     private static ListView branchList ;
     private static  PopupMenu yearListMenu;
    private static  PopupMenu archiListMenu;
    private static  PopupMenu firstListMenu;
     public int yearValue ;
     public String branchName ;
     public Intent intent;
     private int currentTab;


   //for file upload tab
    Spinner branchSpinner;
    Spinner semSpinner;
    Spinner subSpinner;
    EditText filePath;
    SharedPreferences sharedPreferences;

    ImageButton selectFilePath;
    TextView uploadFile;
    File uploadFilePath;
    String uploadFileName;




    //array adapters
    ArrayAdapter<String> semAdapter;
    ArrayAdapter<String> subAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch_list);

        uploadFilePath=new File("nothing");



        sharedPreferences=getSharedPreferences("userLogInData", Activity.MODE_PRIVATE);
        //resource of upload file
        filePath=(EditText)findViewById(R.id.filePath);
        selectFilePath=(ImageButton)findViewById(R.id.selectFilePathButton);
        uploadFile=(TextView) findViewById(R.id.uploadButton);
        subSpinner=(Spinner) findViewById(R.id.subjectSpiner);
        branchSpinner=(Spinner)findViewById(R.id.branchSpiner);
        semSpinner=(Spinner)findViewById(R.id.semSpiner);
        String[] semlist={"SELECT BRANCH FIRST"};
         semAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,semlist);
         subAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,semlist);
        semSpinner.setAdapter(semAdapter);
        subSpinner.setAdapter(subAdapter);

        tabHost=(TabHost)findViewById(R.id.mainTabHost);
        branchList=(ListView)findViewById(R.id.branchList);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("BranchList");
        spec.setContent(R.id.tab1);
        spec.setIndicator("BranchList");
        tabHost.addTab(spec);

         //Tab 2
        spec = tabHost.newTabSpec("DONATE NOTES");
        spec.setContent(R.id.tab2);
        spec.setIndicator("DONATE NOTES");
        tabHost.addTab(spec);




         currentTab=tabHost.getCurrentTab();
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                View currentView=tabHost.getCurrentView();
                if(Build.VERSION.SDK_INT >= 21){
                if(tabHost.getCurrentTab()>currentTab){
                    currentView.setAnimation(new MyAnimations().inFromRightAnimation());
                }
                else
                {
                    currentView.setAnimation(new MyAnimations().outToRightAnimation());
                } }
                currentTab=tabHost.getCurrentTab();
            }
        });


        //NAME OF BRANCHES
        final ArrayList <String> branchArrasyList = new ArrayList <String>();
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
        branchArrasyList.add(0,"FIRST YEAR");
        branchArrasyList.add(0,"COMMON INTEREST");


         intent =new Intent(BranchListActivity.this,NotesActivity.class);



         ArrayAdapter<String> bindBranchList ;
        bindBranchList= new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,branchArrasyList);
        branchList.setAdapter(bindBranchList);
        branchSpinner.setAdapter(bindBranchList);

      branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              if(branchSpinner.getSelectedItem().toString().equals("FIRST YEAR")){
                  String[] setSem={"1","2"};
                  semAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,setSem);
                   semSpinner.setAdapter(semAdapter);
              }
              else
              if(branchSpinner.getSelectedItem().toString().equals("ARCHITECTURE")){
                  String[] setSem={"1","2","3","4","5","6","7","8"};
                  semAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,setSem);
                  semSpinner.setAdapter(semAdapter);

              }
              else
              if(branchSpinner.getSelectedItem().toString().equals("COMMON INTEREST")){
                  String[] setSem={"-"};
                  semAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,setSem);
                  semSpinner.setAdapter(semAdapter);

              }
              else
              {
                  String[] setSem={"3","4","5","6","7","8"};
                  semAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,setSem);
                    semSpinner.setAdapter(semAdapter);

              }
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }
      });



        semSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(branchSpinner.getSelectedItem().toString().equals("COMMON INTEREST")){

                    if(semSpinner.getSelectedItem().toString().equals("-")) {
                        String[] subject = {"NOVELS","MAGAZINES","MUSIC THEORY,TABS,..,.","SOCIAL","TECHNOLOGY","APTITUDE","NITRR STUFF","EDUCATIONAL","PSYCHOLOGY","OTHER"};
                        subAdapter = new ArrayAdapter<String>(BranchListActivity.this, R.layout.support_simple_spinner_dropdown_item, subject);
                        subSpinner.setAdapter(subAdapter);
                    }

                }
                if(branchSpinner.getSelectedItem().toString().equals("FIRST YEAR")){

                    if(semSpinner.getSelectedItem().toString().equals("1")) {
                        String[] subject = {"basic civil", "basic electronics", "besic mechanical engg.", "CFIT", "Engg graphics", "maths I","Env. & Ecology","lab manual"};
                        subAdapter = new ArrayAdapter<String>(BranchListActivity.this, R.layout.support_simple_spinner_dropdown_item, subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("2")) {
                        String[] subject = {"maths II","Applied physics", "Applied chemistry", "basic electrical", "engg. mechanics", "Workshop"};
                        subAdapter = new ArrayAdapter<String>(BranchListActivity.this, R.layout.support_simple_spinner_dropdown_item, subject);
                        subSpinner.setAdapter(subAdapter);
                    }

                }

                else

                if(branchSpinner.getSelectedItem().toString().equals("CIVIL")){
                 if(semSpinner.getSelectedItem().toString().equals("3")){
                     String[] subject={"Math III","FLUID MECHANICS I","SURVEYING I","BUILDING METERIALS","ENGG. GEOLOGY","MECH. OF SOLIDS","LAB RECORD"};
                     subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                     subSpinner.setAdapter(subAdapter);
                 }
                    else
                 if(semSpinner.getSelectedItem().toString().equals("4")){
                     String[] subject={"STRUCTURAL ANALYSIS I","FLUID MECHANICS II","SURVEYING II","CIVIL ENGG DRAWING","BUILDING CONSTRUCTION","TRANSPORTATION ENGG I","LAB RECORD"};
                     subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                     subSpinner.setAdapter(subAdapter);
                 }
                 else
                 if(semSpinner.getSelectedItem().toString().equals("5")){
                     String[] subject={"STRUCTURAL ANALYSIS II","STRUCTURAL ENGG DESIGN","GEOTECH ENGG I","ENGG. HYDROLOGY","ENVIRONMENTAL ENGG I","TRANSPORTATION ENGG II","LAB RECORD"};
                     subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                     subSpinner.setAdapter(subAdapter);
                 }
                 else
                 if(semSpinner.getSelectedItem().toString().equals("6")){
                     String[] subject={"STRUCTURAL ENGG DESIGN II","ENVIRONMENTAL ENGG. II","QUALITY SURVEYING AND COST EVALUTION","GEOTECH ENGG II","CONSTRUCTION PLANING & MANAGEMENT","PROFESSIONAL ELECTIVE I","LAB RECORD"};
                     subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                     subSpinner.setAdapter(subAdapter);
                 }
                 else
                 if(semSpinner.getSelectedItem().toString().equals("7")){
                     String[] subject={"STRUCTURAL ENGG DESIGN III","WATER RESOURCES ENGG I","STRUCTURAL ANALYSIS III","PROFFESIONAL ELECTIVE II","LAB RECORD"};
                     subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                     subSpinner.setAdapter(subAdapter);
                 }
                 else
                 if(semSpinner.getSelectedItem().toString().equals("8")){
                     String[] subject={"STRUCTURAL ENGG DESIGN IV","WATER RESOURCES ENGG II","NUMERICAL METHOD IN CIVIL ENGG","PROFESSIONAL ELECTIVE III","LAB RECORD"};
                     subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                     subSpinner.setAdapter(subAdapter);
                 }
                }


                else
                if(branchSpinner.getSelectedItem().toString().equals("BIOMEDICAL")){


                    if(semSpinner.getSelectedItem().toString().equals("3")){
                        String[] subject={"MATH III","BIOCHEMISTRY","NETWORK ANALYSIS","BIOMEDICAL TRANSDUCERS AND MEASURMENT","ANATOMY AND PHYSIOLOGY","BIOMECHANICS","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("4")){
                        String[] subject={"MICHROBIOLOGY","NUMERICAL ANALYSIS","BIOMEDICAL SIGNAL PROCESSING","DIGITAL ELECTRONICS","BIOMEDICAL INSTRUMENTATION","ANALOG CIRCUITS","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("5")){
                        String[] subject={"BIOMEDICAL EQUIPMENTS","MICROPROCESSOR","BIOLOGICAL CONTROL SYSTEM","BASIC CLENICAL SCIENCE I","PRINCIPAL OF COMMUNICATION SYSTEM","MICROELECTRONICS AND I.C.","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("6")){
                        String[] subject={"MEDICAL IMAGE PROCESSING","MANAGMENT SCIENCE","TELEMEDICINE","BASIC CLINICAL SCIENCE II","BIOELECTRICITY","BIOINFORMATICS","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("7")){
                        String[] subject={"BIOMATERIALS","MICROCONTROLLER","DATABASE MANAGMENT SYSTEM","ELECTIVE I","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("8")){
                        String[] subject={"OPTICAL FIBER AND LASER IN MEDICINE","NUCLEAR MEDICINE","TISSUE ENGG.","ELECTIVE II","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                }
                else
                if(branchSpinner.getSelectedItem().toString().equals("BIOTECHNOLOGY")){
                    if(semSpinner.getSelectedItem().toString().equals("3")){
                        String[] subject={"Math III","BIOCHEMISTRY","CELL & MOLECULAR BIOLOGY","OBJECT ORIENTED PROGRAMMING","BIOPHYSICS","STOICHIOMETRIC CALC.","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("4")){
                        String[] subject={"MICROBIOLOGY","PLANT BIOTECHNOLOGY","FOOD BIOTECHNOLOGY","CRECOMBINANT DNA TECHNO.","NUMERICAL ANALYSIS","THERMODYNAMICS AND REACTION ENGG","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("5")){
                        String[] subject={"ANIMAL BIOTECHNOLOGY","ENZYME TECHNOLOGY","DRUG DESIGN AND PHARMA TECHNOLOGY","BIOETHICS & BIOSAFETY","IMMUNOLOGY","FLUID FLOW OPERATIONS","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("6")){
                        String[] subject={"BIOINFORMATICS","ANALYTICAL TECHNIQUES","MANAGEMENT SCIENCE","DATABASE MANAGMENT SYSTEM","BIOSTATICS AND PROBABLITIES","HEAT & MASS TRANSFER","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("7")){
                        String[] subject={"GENOMICS","MICROBIAL TECHNOLOGY","UNIT OPERATION","ELECTIVE I","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("8")){
                        String[] subject={"METOBOLIC ENGG","ENV. BIOTECHNOLOGY","ELECTIVE 1","ELECTIVE II","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                }
                else
                if(branchSpinner.getSelectedItem().toString().equals("CHEMICAL")){
                    if(semSpinner.getSelectedItem().toString().equals("3")){
                        String[] subject={"Math III","STRENGTH OF MATERIALS","PROG. IN C++","INDUSTRIAL PROCESS CALC.","INORGANIC PROCESS TECH.","MATERIAL TECH.","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("4")){
                        String[] subject={"MECHANICAL OPERATION","CHEM ENGG. THERMODYNAMICS","PROCESS ECNOMICS & MANAGEMENT","FUEL TECHNOLOGY","INSTRUMENTS & MEASURMENT","APPLIED PHYSICS AND  ORGANIC CHEMISTRY","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("5")){
                        String[] subject={"FLUID MECHANICS","MASS TRANSFER OPERATION I","HEAT TRANSFER OPERATIONS","NUMERICAL METHODS","PLANT DESIGN & COSTING","PROCESS SAFETY AND HAZARD MANAGEMENT","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("6")){
                        String[] subject={"MASS TRANSFER OPERATION II","CHEMICAL REACTION I","TRANSPORT PHENOMENA","PROCESS EQUIP. DESIGN I","ORGANIC PROCESS & TECHNOLOGY","NON-CONVENTIONAL ENERGY","PULP & PAPER TECHNOLOGY","MEMBRANE SEPARATION","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                     else
                    if(semSpinner.getSelectedItem().toString().equals("7")){
                        String[] subject={"PROCESS EQUIP. DESIGN II","CHEMICAL REACTION ENGG. II","PROCESS DYNAMICS AND CONTROL","ELECTIVE II","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("8")){
                        String[] subject={"PROCESS MODELLING & SIMULATION","FLUIDIZATION ENGG.","PROCESS EQUIP DESIGN III","ENVIRNMENT POLLUTION & CONTROL","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                }
                else
                     if(branchSpinner.getSelectedItem().toString().equals("METALLURGY")){
                         if(semSpinner.getSelectedItem().toString().equals("3")){
                             String[] subject={"NACP","FUNDAMENTALS OF TECH. & ELECTRONICS","INTRO TO MATERIAL SCIENCE","METALLURGICAL THERMODYNAMICS","FUEL,FURNACES & REFRACTORY","MINERALS & ORE BENEFICIATION","LAB RECORD"};
                             subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                             subSpinner.setAdapter(subAdapter);
                         }
                         else
                         if(semSpinner.getSelectedItem().toString().equals("4")){
                             String[] subject={"TESTING OF MATERIALS","PHYSICAL MEATLLURGY","FERROUS EXTRACTIVE METALLURGY I","ENGG. OF NON-METALLIC MATERIALS","PRINCIPLES OF NON-FERROUS EXTRACTIVE METALLURGY","TRANSPORT PHENOMENA IN METALLURGICAL PROCESSES","LAB RECORD"};
                             subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                             subSpinner.setAdapter(subAdapter);
                         }
                         else
                         if(semSpinner.getSelectedItem().toString().equals("5")){
                             String[] subject={"HEAT TREATMENT TECH.","MATERIAL JOINING PROCESSES","FERROUS EXTRACTIVE METALLURGY II","DEFORMATION THEORIES OF METALS AND ALLOYS","FOUNDRY TECHNOLOGY","OPTIONAL I","LAB RECORD"};
                             subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                             subSpinner.setAdapter(subAdapter);
                         }
                         else
                         if(semSpinner.getSelectedItem().toString().equals("6")){
                             String[] subject={"ENGG. ECONOMICS AND INDUSTRIAL MANAGEMENT","METAL FORMING PROCESSES","SECONDARY & SPECIAL STEEL MAKING","MATERIALS MODELLING & SIMULATION","MATERIAL CHARACTERIZATION TECHNIQUE","OPTIONAL II","LAB RECORD"};
                             subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                             subSpinner.setAdapter(subAdapter);
                         }
                         else
                         if(semSpinner.getSelectedItem().toString().equals("7")){
                             String[] subject={"CORROSION ENGG.","PHASE TRANSFORMATION & PHASE EQUILIBRIUM","OPTIONAL III","OPTIONAL IV","LAB RECORD"};
                             subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                             subSpinner.setAdapter(subAdapter);
                         }
                         else
                         if(semSpinner.getSelectedItem().toString().equals("8")){
                             String[] subject={"FRACTURE & FAILURE ANALYSIS","ALLOY DESIGN & APPLICATION","OPTIONAL V","OPTIONAL VI","LAB RECORD"};
                             subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                             subSpinner.setAdapter(subAdapter);
                         }
                     }
                else
                if(branchSpinner.getSelectedItem().toString().equals("MINING")){
                    if(semSpinner.getSelectedItem().toString().equals("3")){
                        String[] subject={"Math III","MECH. OF SOLIDS & FLUIDS","MINE SURVEY I","INTRO TO MINING","GEOLOGY I","PROG. WITH C","LAB RECORD","Eesh Jain Notes"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("4")){
                        String[] subject={"MINE ENV I","ENGG. MATERIALS","GEOLOGY II","UG COAL MINING","SURFACE MINING I","BASIC ELECTRICAL & ELECTRONICS","Eesh Jain Notes","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("5")){
                        String[] subject={"NACP","MINING SURVEY II","MINE LEGISLATION SAFETY I","POLLUTION CONTROL ENGG.","MINING MACHINERY I","UG METAL MINING","Eesh Jain Notes","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("6")){
                        String[] subject={"MINE ENV. II","MINERAL PROCESSING","MINE MACHINERY II","MINE LEGISLATION & SAFETY II","MINE ECONOMICS","MINE MANAGEMENT","Eesh Jain Notes","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("7")){
                        String[] subject={"ROCK MECHANICS","STARTA CONTROL","BLASTING TECH","PROFFESIONAL ELECTIVE I","Eesh Jain Notes","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("8")){
                        String[] subject={"MINING SURVEY III","MINE MACHINERY III","MINE ENV. III","PROFESSIONAL ELECTIVE II","Eesh Jain Notes","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                }
                else
                if(branchSpinner.getSelectedItem().toString().equals("MECHANICAL")){
                    if(semSpinner.getSelectedItem().toString().equals("3")){
                        String[] subject={"Math II","NUMERICAL TECHNIQUES","MATERIAL SCIENCE","MECHANICS OF SOLIDS I","APPLIED THERMODYNAMICS","MACHINE DRAWING","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("4")){
                        String[] subject={"INTERNAL COMBUSTION ENGINE","FLUID MECHANICS","MECHANICS OF SOLIDS II","MEASUREMENT & CONTROL","MENUFACTURING SCIENCE I","KINAMATICS OF MACHINES","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("5")){
                        String[] subject={"COMPUTER GRAPHICS","FINITE ELEMENT METHOD","FLUID MACHINERY","MANUFACTURING SCIENCE II","INDUSTRIAL ENGG.","MACHINE DESIGN I","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("6")){
                        String[] subject={"DYNAMICS OF MACHINES","TURBO MACHINERY","ENERGY CONVERSION SYSTEM","INDUSTRIAL MANAGEMENT","AUTOMOBILE ENGG.","ELECTIVE I","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("7")){
                        String[] subject={"HEAT & MASS TRANSFER","OPERATION RESEARCH","REFRIGERATION & AIR CONDITIONING","ELECTIVE II","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("8")){
                        String[] subject={"COMPUTER AIDED DESIGN & MANUFACTURING","MACHINE DESIGN II","PRODUCTION MANAGEMENT","ELECTIVE III","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                }
                else
                if(branchSpinner.getSelectedItem().toString().equals("ELECTRICAL")){
                    if(semSpinner.getSelectedItem().toString().equals("3")){
                        String[] subject={"NACP","SOLID STATE DEVICES","ELECTRIC CIRCUITS","MATHS III","GENRATION OF ELECTRICAL ENERGY","ELECTRICAL MACHINES I","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("4")){
                        String[] subject={"ELECTRICAL POWER SYSTEM","ELECTRICAL ENGG. MATERIALS & SEMICUNDUCTOR DEVICE","ELECTROMAGNETIC FIELDS","DIGITAL ELECTRONICS AND LOGIC DESIGN","ELECTRICAL MEASUREMENT & INSTRUMENTATION","ELECTRICAL NETWORK ANALYSIS AND SYNTHESIS","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("5")){
                        String[] subject={"DIGITAL SIGNAL PROCESSING","COMPUTER SYSTEM ARCHITECTURE","ANALOG AND DIGITAL COMMUNICATION","ANALOG ELECTRONICS","CONTROL SYSTEM ENGG.","POWER ELECTRONICS","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("6")){
                        String[] subject={"UTILIZATION OF ELECTRICAL ENERGY","ELECTRICAL MACHINE II","POWER SYSTEM ANALYSIS","POWER SYSTEM PROTECTION & SWITCHGEAR","MICROPROCESSORS","HVDC POWER TRANSMITTION","ARTIFICIAL NEURAL NETWORKS & FUZZY SYSTEMS","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("7")){
                        String[] subject={"ELECTRICAL MACHINES III","MANAGEMENT CONCEPT & TECHNIQUES","HIGH VOLTAGE ENGG.","ELECTIVE II","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("8")){
                        String[] subject={"FLEXIBLE AC TRANSMISSION SYSTEM","ELECTRICAL DRIVES","POWER APPARATUS SYSTEM","MODERN CONTROL SYSTEM ENGG.","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                }
                else
                if(branchSpinner.getSelectedItem().toString().equals("ELECTRONICS AND TELECOMMUNICATION")){
                    if(semSpinner.getSelectedItem().toString().equals("3")){
                        String[] subject={"Math III","SIGNALS AND SYSTEMS","DIGITAL LOGIC DESIGN","ELECTRONIC MEASURMENT AND INSTRUMENTATION","NETWORK ANALYSIS & SYNTHESIS","DEVICES AND CIRCUITS I","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("4")){
                        String[] subject={"PROBABLITIES & STOCHASTIC PROCESS","ANALOG COMMUNICATION","COMPUTER ORGANISATION & ARCHITECTURE","EM WAVES & ANTENNAS","DEVICES AND CIRCUITS II","MICROPROCESSORS(8085/86)","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("5")){
                        String[] subject={"ELECTIVE I","ANALOG IC & APPLICATIONS","AUTOMATIC CONTROL SYSTEMS","DIGITAL SIGNAL PROCESSING","MICROCONTROLLER & EMBADDED SYSTEMS","DIGITAL COMMUNICATION","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("6")){
                        String[] subject={"ELECTIVE II","DATA COMMUNICATION & NETWORKING","DIGITAL SYSTEM DESIGN","WIRELESS COMMUNICATION","VLSI & MICROeLECTRONICS","MICROWAVE & RADAR ENGG.","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("7")){
                        String[] subject={"ELECTIVE III","ELECTIVE IV","TELECOM SWITCHING & CELLULAR SYSTEMS","INFORMATION THEORY AND CODING","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("8")){
                        String[] subject={"OPTICAL FIBER COMMUNICATION","ELECTIVE V","ELECTIVE VI","COMMUNICATION SYSTEMS","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                }
                else
                if(branchSpinner.getSelectedItem().toString().equals("COMPUTER SCIENCE")){
                    if(semSpinner.getSelectedItem().toString().equals("3")){
                        String[] subject={"Math III","COMPUTATIONAL MATHAMATICS","DIGITAL LOGIC & DESIGN","PRINCIPLES OF MANAGEMENT","PROBLEM SOLVING & LOGIC BUILDING USING C","COMPUTER FUNDAMENTALS","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("4")){
                        String[] subject={"MICROPROCESSOR & INTERFACE","DATA STRUCTURE","OBJECT ORIENTED CONCEPTS & PROG. & C++","DISCRETE STRUCTURE","COMPUTER SYSTEM ARCHITECTURE","PRINCIPLE OF COMM. SYSTEM","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("5")){
                        String[] subject={"OPERATING SYSTEMS","DATABASE MANAGEMENT SYSTEM","COMPUTER GRAPHICS MULTIMEDIA","DATA COMMUNICATION","THEORY COMPUTATION","OPERATIONS RESEARCH","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("6")){
                        String[] subject={"ANALYSIS & DESIGN ALGORITHMS","COMPUTER NETWORK","UNIX & SHELL PROG.","SOFTWARE ENGG.","COMPILER DESIGN","PARALLEL PROCESSOR & COMPUTING","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("7")){
                        String[] subject={"ARTIFICIAL INTELLIGENCE & EXPERT SYSTEM","NETWORK PROG.","ELECTIVE I","ELECTIVE II","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("8")){
                        String[] subject={"SOFTWARE PROJECT MANAGEMENT","DATA MINING & WARE HOUSING","ELECTIVE III","ELECTIVE VI","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                }
                else
                if(branchSpinner.getSelectedItem().toString().equals("INFORMATION TECHNOLOGY")){
                    if(semSpinner.getSelectedItem().toString().equals("3")){
                        String[] subject={"Math III","COMPUTATIONAL MATHEMATICS","BASIC ELECTRONICS","DATA STRUCTURE","OBJECT ORIENTED PROG. METHADOLOGIES","DIGITAL ELECTRONIC & LOGIC DESIGN","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("4")){
                        String[] subject={"STATISTICAL METHOD & PROBABILITY","DISCRETE STRUCTURES","COMPUTER ORGANIZATION","PRINCIPLES OF COMM. SYSTEM","THEORY OF COMPUTATION","OPERATING SYSTEMS","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("5")){
                        String[] subject={"COMPUTER NETWORKS","DIGITAL ANALYSIS OF ALGORITHMS","MICROPROCESSORS","DATABASE MANAGEMENT SYSTEM","COMPUTER GRAPHICS","ELECTIVE I","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("6")){
                        String[] subject={"COMPILER DESIGN","INFORMATION THEORY & CODING","INTERNET & WEB TECHNOLOGIES","CELLULAR & MOBILE COMPUTING","SOFTWARE ENGG","ELECTIVE II","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("7")){
                        String[] subject={"MANAGEMENT INFORMATION SYSTEM","ARTIFICIAL INTELIGENCE & EXPERT SYSTEMS","ELECTIVE III","ELECTIVE VI","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("8")){
                        String[] subject={"DATA MINING & WARE HOUSING","CRYPTOGRAPHY","ELECTIVE V","ELECTIVE IV","LAB RECORD"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                }
                else
                if(branchSpinner.getSelectedItem().toString().equals("ARCHITECTURE")){
                    if(semSpinner.getSelectedItem().toString().equals("1")){
                        String[] subject={"ARCHITECHTURAL DESIGN I","BUILDING CONTRUCTION & TECHNOLOGY I","STRACTURAL DESIGN & SYSTEM I","ARCHITECTURAL GRAPHICS SKILL I","BUILDING MATERIAL & SCIENCE I"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("2")){
                        String[] subject={"ARCHITECTURAL DESIGN II","BUILDING CONSTRUCTION & TECHNOLOGY II","STRUCTURAL DESIGN & SYSTEMS II","ARCHITECTURAL GRAPHICS SKILL II","ENVIRONMENTAL STUDIES"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("3")){
                        String[] subject={"ARCHITECHTURAL DESIGN III","BUILDING CONTRUCTION & TECHNOLOGY III","STRACTURAL DESIGN & SYSTEM III","CLIMATOLOGY","BUILDING MATERIAL & SCIENCE III","HISTORY OF ARCHITECHTURE ART & CULTURE","COMPUTER APPLICATION IN ARCHITECHTURE","DISASTER MANAGEMENT"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("4")){
                        String[] subject={"ARCHITECTURAL DESIGN IV","BUILDING CONSTRUCTION & TECHNOLOGY IV","SURVEYING & LEVELLING","HUMAN SATTLEMENT & VERNACULAR ARCHITECHTURE","HISTORY OF ARCHITECHTURE ART AND CULTURE II","ENERGY EFFICIENCY ARCHITECTURE"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("5")){
                        String[] subject={"ARCHITECTURAL DESIGN V","BUILDING CONSTRUCTION & TECHNOLOGY V","STRUCTURAL DESIGN & SYSTEMS V","BUILDING SERVICES & EQUIP. I","BUILDING BYE-LAWS AND CODES OF PRACTICES","HISTORY OF ARCHITECTURE ART & CULTURE III"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("6")){
                        String[] subject={"ARCHITECTURAL DESIGN VI","BUILDING CONSTRUCTION & TECHNOLOGY VI","STRUCTURAL DESIGN & SYSTEM VI","BUILDING SERVICES & EQUIP. II","ESTIMATION,COSTING & SPECIFICATINS","MODERN ARCHITECTURE"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("7")){
                        String[] subject={"ARCHITECTURAL DESIGN VII","BUILDING CONSTRUCTION & TECHNOLOGY VII","ADVANCED STRUCTURAL DESIGN & SYSTEM","TOWN PLANNING & LANDSCAPE","ELECTIVE I","ELECTIVE II"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                    else
                    if(semSpinner.getSelectedItem().toString().equals("8")){
                        String[] subject={"ELECTIVE III","ELECTIVE IV","THESIS PROJECT"};
                        subAdapter=new ArrayAdapter<String>(BranchListActivity.this,R.layout.support_simple_spinner_dropdown_item,subject);
                        subSpinner.setAdapter(subAdapter);
                    }
                }













            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        branchList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String branch=String.valueOf(adapterView.getItemAtPosition(i));
                        branchName=branch;
                        if(branch=="COMMON INTEREST"){
                            intent.putExtra("yearValue", "-");
                            intent.putExtra("branchName", branchName);
                            startActivity(intent);
                        }
                        else
                         if(branch=="FIRST YEAR"){
                            firstListMenu=new PopupMenu(BranchListActivity.this,view);
                            firstListMenu.inflate(R.menu.first_year_menu);
                            firstListMenu.show();
                            firstListMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch(item.getItemId()) {

                                        case (R.id.fir): {
                                            yearValue = 1;
                                            intent.putExtra("yearValue", yearValue);
                                            intent.putExtra("branchName", branchName);
                                            startActivity(intent);
                                            return true;
                                        }

                                        case (R.id.sec): {
                                            yearValue = 2;
                                            intent.putExtra("yearValue", yearValue);
                                            intent.putExtra("branchName", branchName);
                                             startActivity(intent);
                                            return true;
                                        }

                                        default: return false;
                                    }
                                }
                            });
                        }
                            else if(branch=="ARCHITECTURE"){
                            archiListMenu=new PopupMenu(BranchListActivity.this,view);
                            archiListMenu.inflate(R.menu.archi_menu);
                            archiListMenu.show();
                            archiListMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    switch(item.getItemId()) {

                                        case(R.id.one):{yearValue=1;
                                            intent.putExtra("yearValue",yearValue);
                                            intent.putExtra("branchName",branchName);
                                            startActivity(intent);
                                            return true;}

                                        case (R.id.two):{ yearValue=2;
                                            intent.putExtra("yearValue",yearValue);
                                            intent.putExtra("branchName",branchName);
                                            startActivity(intent);
                                            return true;}


                                        case(R.id.three):{yearValue=3;
                                            intent.putExtra("yearValue",yearValue);
                                            intent.putExtra("branchName",branchName);
                                            startActivity(intent);
                                            return true;}

                                        case (R.id.four):{ yearValue=4;
                                            intent.putExtra("yearValue",yearValue);
                                            intent.putExtra("branchName",branchName);
                                            startActivity(intent);
                                            return true;}

                                        case (R.id.five):{ yearValue=5;
                                            intent.putExtra("yearValue",yearValue);
                                            intent.putExtra("branchName",branchName);
                                            startActivity(intent);
                                            return true;}

                                        case (R.id.six):{ yearValue=6;
                                            intent.putExtra("yearValue",yearValue);
                                            intent.putExtra("branchName",branchName);
                                            startActivity(intent);
                                            return true;}

                                        case (R.id.seven):{ yearValue=7;
                                            intent.putExtra("yearValue",yearValue);
                                            intent.putExtra("branchName",branchName);
                                            startActivity(intent);
                                            return true;}

                                        case (R.id.eight):{ yearValue=4;
                                            intent.putExtra("yearValue",yearValue);
                                            intent.putExtra("branchName",branchName);
                                            startActivity(intent);
                                            return true;}

                                        default:return false;

                                    }
                                }
                            });



                        }
                        else{
                             yearListMenu=new PopupMenu(BranchListActivity.this,view);
                             yearListMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch(item.getItemId()) {

                                    case(R.id.third):{yearValue=3;
                                        intent.putExtra("yearValue",yearValue);
                                        intent.putExtra("branchName",branchName);
                                        startActivity(intent);
                                        return true;}

                                    case (R.id.forth):{ yearValue=4;
                                        intent.putExtra("yearValue",yearValue);
                                        intent.putExtra("branchName",branchName);
                                        startActivity(intent);
                                        return true;}

                                    case (R.id.fifth):{ yearValue=5;
                                        intent.putExtra("yearValue",yearValue);
                                        intent.putExtra("branchName",branchName);
                                        startActivity(intent);
                                        return true;}

                                    case (R.id.sixth):{ yearValue=6;
                                        intent.putExtra("yearValue",yearValue);
                                        intent.putExtra("branchName",branchName);
                                        startActivity(intent);
                                        return true;}

                                    case (R.id.seventh):{ yearValue=7;
                                        intent.putExtra("yearValue",yearValue);
                                        intent.putExtra("branchName",branchName);
                                        startActivity(intent);
                                        return true;}

                                    case (R.id.eighth):{ yearValue=4;
                                        intent.putExtra("yearValue",yearValue);
                                        intent.putExtra("branchName",branchName);
                                        startActivity(intent);
                                        return true;}

                                    default:return false;

                                }
                            }
                        });

                        yearListMenu.inflate(R.menu.branch_yearlist_menu);
                        yearListMenu.show();




                        }
                    }
                }


        );



        uploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OnuploadFile(view);
            }
        });
           selectFilePath.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   selectFilePath(view);
               }
           });


    }









    void OnuploadFile(View  v){
        if(sharedPreferences.getString("name","null").equals("guest")){
            new SweetAlertDialog(BranchListActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("ALERT")
                    .setContentText("you have to sign in first to use this feature")
                    .setConfirmText("SignIn")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Intent intent=new Intent(BranchListActivity.this,LoginActivity.class);
                            startActivity(intent);



                        }
                    })
                    .show();
        }
        else {  if(!isNetworkAvailable()){
            Snackbar.make(findViewById(R.id.branchLayout),"No Internet Connection",Snackbar.LENGTH_LONG).show();
        }
          else {
            new SweetAlertDialog(BranchListActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("ALERT")
                    .setContentText("Are you sure all information filled is correct !!???")
                    .setConfirmText("Yes")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            new UploadFileAsync().execute();


                        }
                    })
                    .show();
        }
        }
    }


    void selectFilePath(View v){
           if(isStoragePermissionGranted()) {
               new ChooserDialog().with(this)
                       .withFilterRegex(false, false, ".*\\.(doc?x|pdf|ppt?x)")
                       .withStartFile(Environment.getExternalStorageDirectory().getPath())
                       .withResources(R.string.title_choose_file, R.string.title_choose, R.string.dialog_cancel)
                       .withChosenListener(new ChooserDialog.Result() {
                           @Override
                           public void onChoosePath(String path, File pathFile) {
                               filePath.setText(path);
                               uploadFilePath = pathFile;
                               uploadFileName = pathFile.getName();
                               Toast.makeText(BranchListActivity.this, "FILE: " + path + "Selected", Toast.LENGTH_SHORT).show();
                           }
                       })
                       .build()
                       .show();
           }
    }




    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }




       private class UploadFileAsync extends AsyncTask<String,Void,String>{


           @Override
           protected String doInBackground(String... strings) {
               String serverRespone="error";
               if(uploadFilePath.exists()){

               OkHttpClient client = new OkHttpClient.Builder()
                       .connectTimeout(5,TimeUnit.MINUTES)
                       .writeTimeout(5,TimeUnit.MINUTES)
                       .readTimeout(5,TimeUnit.MINUTES)
                       .build();


               String extension = uploadFileName.toString().substring(uploadFileName.toString().lastIndexOf("."));
               RequestBody file_body = RequestBody.create(MediaType.parse(extension),uploadFilePath);

               RequestBody request_body = new MultipartBody.Builder()
                       .setType(MultipartBody.FORM)
                       .addFormDataPart("type",extension)

                       .addFormDataPart("uploaded_file",uploadFilePath.toString().substring(uploadFilePath.toString().lastIndexOf("/")+1), file_body)
                       .build();

               Request request = new Request.Builder()
                       .url("http://gogreennitrr.org/green_lib_file_uploas_api.php")
                       .post(request_body)
                       .build();

               try {
                   Response response = client.newCall(request).execute();
                   if(response.isSuccessful()){

                       serverRespone=uploadToServer();

                   }
                   else
                   {    serverRespone="error";
                       throw new IOException("Error : "+response);
                   }



               } catch (IOException e) {
                   e.printStackTrace();
               }                  }
               else{
                   serverRespone="fILE NOT EXIST";
               }




               return serverRespone;
           }


           NotificationManager mNotifyManager;
           android.support.v4.app.NotificationCompat.Builder mBuilder;

           @Override
           protected void onPreExecute() {
               super.onPreExecute();
               mNotifyManager =
                       (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
               mBuilder = new NotificationCompat.Builder(getBaseContext());
               mBuilder.setContentTitle(uploadFileName)
                       .setContentText("upload in progress")
                       .setSmallIcon(R.drawable.ic_file);
               mBuilder.setProgress(0, 0, true);// Issues the notification
               mNotifyManager.notify(1, mBuilder.build());
           }




           @Override
           protected void onPostExecute(String s) {
               super.onPostExecute(s);

               if(s.equals("fILE NOT EXIST")){
                   Toast.makeText(BranchListActivity.this, "File not Exist-wrong File Path.", Toast.LENGTH_SHORT).show();
                   mBuilder.setContentText("file not exist")
                           .setProgress(0,0,false);
                   mNotifyManager.notify(1, mBuilder.build());

               }
               else

                    if(s.equals("sucess")){

                        mBuilder.setContentText("upload complete")
                                .setProgress(0,0,false);

                        mNotifyManager.notify(1, mBuilder.build());
                   Toast.makeText(BranchListActivity.this, "File Upload complete.", Toast.LENGTH_SHORT).show();}
                    else {

                        mBuilder.setContentText("error uploading")
                                .setProgress(0,0,false);

                        mNotifyManager.notify(1, mBuilder.build());
                   Toast.makeText(BranchListActivity.this, "File upload Failed", Toast.LENGTH_SHORT).show();
               }



           }
       }

       String createUrl(){
           String fileFormat=uploadFilePath.toString().substring(uploadFilePath.toString().lastIndexOf(".")+1);
           int size=Integer.parseInt(String.valueOf(uploadFilePath.length()/1024));
           String createdUrl="https://devlopershome.000webhostapp.com/InsertDataAPI.php?";
           String urlLink="http://gogreennitrr.org/green_library/"+uploadFileName.replace(" ","_");
           createdUrl+="fileName="+uploadFileName+"&urlLink="+urlLink+"&subject="+subSpinner.getSelectedItem().toString()+"&";
           createdUrl+="semester="+semSpinner.getSelectedItem().toString()+"&branch="+getBranchTableName()+"&";
           createdUrl+="submittedBy="+sharedPreferences.getString("name","null")+"&fileSize="+size+"&fileFormat="+fileFormat;
           createdUrl=createdUrl.replaceAll(" ","%20");
           return createdUrl;

       }


        String getBranchTableName()
        {    String para=branchSpinner.getSelectedItem().toString();
            String data="null";
            if(para.equals("FIRST YEAR"))
                data="firstYear";
            else
            if(para.equals("ELECTRONICS AND TELECOMMUNICATION"))
                data="elex";
            else
            if(para.equals("MINING"))
                data="mining";
            else
            if(para.equals("CIVIL"))
                data="civil";
            else
            if(para.equals("COMPUTER SCIENCE"))
                data="computerScience";
            else
            if(para.equals("MECHANICAL"))
                data="mechanical";
            else
            if(para.equals("ELECTRICAL"))
                data="electrical";
            else
            if(para.equals("BIOTECHNOLOGY"))
                data="Biotechnology";
            else
            if(para.equals("METALLURGY"))
                data="metallurgy";
            else
            if(para.equals("CHEMICAL"))
                data="chemical";
            else
            if(para.equals("BIOMEDICAL"))
                data="Biomedical";
            else
            if(para.equals("INFORMATION TECHNOLOGY"))
                data="informationTechnology";
            else
            if(para.equals("ARCHITECTURE"))
                data="architecture";
            else if (para.equals("COMMON INTEREST"))
                data = "commonInterest";



            return data;


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



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected(); }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
          startActivity(new Intent(BranchListActivity.this,MainMenuActivity.class));

    }
}




