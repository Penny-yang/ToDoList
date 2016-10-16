package com.peipeiyang.todolist;

import java.util.Date;

/**
 * Created by yangp on 5/26/2016.
 */
public class Notedetail {
    public final static String STUDY = "Study";
    public final static String WORK = "Work";
    public final static String DAILYLIFE = "DailyLife";
    protected String title="";
    protected String description = "";
    protected String additionalinfo="";
    protected String duedate;
    protected String category="";
    protected long id = 0;

//    public Notedetail(){
//
//    }
//    public Notedetail(String title,String description, String duedate, String category, String additionalinfo){
//        this.title = title;
//        this.description = description;
//        this.duedate = duedate;
//        this.category = category;
//        this.additionalinfo = additionalinfo;
//    }
    public long getId() {   return(id);  }
    public void setId(long id) {   this.id=id;  }
    public String getTitle(){return (title);}
    public void setTitle(String title){this.title = title; }
    public String getDescription(){return (description);}
    public void setDescription(String description){this.description = description; }
    public String getCategory(){return (category);}
    public void setCategory(String category){this.category = category; }
    public String getAdditionalinfo(){return (additionalinfo);}
    public void setAdditionalinfo(String additionalinfo){this.additionalinfo = additionalinfo; }
    public String getDuedate(){return (duedate);}
    public void setDuedate(String duedate){this.duedate = duedate; }





}
