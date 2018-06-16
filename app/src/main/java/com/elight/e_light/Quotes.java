package com.elight.e_light;

/**
 * Created by Olije favour on 4/29/2018.
 */

public class Quotes {

    String from, message;

    public Quotes(){

    }

    public Quotes(String message,String author){
        this.from= author;
        this.message=message;
    }

    public String getFrom(){
      return  from;
    }

    public String getMessage(){
   return message;
    }

    public void setMessage(String message){
        this.message =message;

    }

    public  void setFrom(String from){
        this.from=from;

    }
}
