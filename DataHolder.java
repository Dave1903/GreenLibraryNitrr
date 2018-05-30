package com.thedevlopershome.intranetnitrr;


public class DataHolder {
    public String fileName;
    public String fileFormate;
    public String UrlLink;
    public String subject;
    public String fileSize;
    public String submitedBy;


    public DataHolder(){


    }


    public DataHolder(String name,String format,String url,String subject,String fileSize,String submitedBy){
        fileName=name;
        fileFormate=format;
        UrlLink=url;
        this.subject=subject;
        this.fileSize=fileSize;
        this.submitedBy=submitedBy;
    }


}
