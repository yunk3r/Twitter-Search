package com.mycompany.mavenproject1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;
/**
 *
 * @author michael.yunker
 */
 
public class twitter_stream  extends Thread {

    private static final String STREAM_URI = "https://stream.twitter.com/1.1/statuses/filter.json";
   
    public boolean run=true;
     private String latestTweet;
     private String searchText;
     private String searchEmoji;
     private int tweetCount=0;
    
     public void setSearchText(String t)
     {
         searchText=t;
     }
     public void setSearchEmoji(String e)
     {
         searchEmoji=e;
     }
     public String getTweet()
     {
         return latestTweet;
     }
     
     public int getTweetCount()
     {
         return tweetCount;
     }
     public boolean getRun()
     {
         return run;
     }
     
    public void run(){
        try{
            run=true;
            System.out.println("Starting Twitter public stream consumer thread.");

            // Enter your consumer key and secret below
            OAuthService service = new ServiceBuilder()
                    .provider(TwitterApi.class)
                    .apiKey("KEY")
                    .apiSecret("SECRET")
                    .build();

            // Set your access token
            Token accessToken = new Token("Token", "Token");

            // Let's generate the request
            System.out.println("Connecting to Twitter Public Stream");
            OAuthRequest request = new OAuthRequest(Verb.POST, STREAM_URI);
            request.addHeader("version", "HTTP/1.1");
            request.addHeader("host", "stream.twitter.com");
            request.setConnectionKeepAlive(true);
            request.addHeader("user-agent", "Twitter Stream Reader");
           // request.addBodyParameter("track", "Spring");
            request.addBodyParameter("locations","-86.0,37.5,-84.8,38.7");// Set keywords you'd like to track here
            service.signRequest(accessToken, request);
            Response response = request.send();

            // Create a reader to read Twitter's stream
            JSONParser parser=new JSONParser();
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getStream()));
           
               
            String str="";
            JSONObject o ,user;
            JSONObject u;
            String tweet;
            
            while (((str = reader.readLine()) != null)&&run) {
                try {
                    System.out.println("1");
                     o=(JSONObject) parser.parse(str);
                      
                     u=(JSONObject)o.get("user");
                     
                     if(((o.get("text")+"").toLowerCase()).contains(searchText.toLowerCase())&&(o.get("text")+"").contains(searchEmoji))
                     {
                     tweet=u.get("screen_name").toString().toUpperCase()+" - "+o.get("text")+"\n\n";
                     latestTweet=tweet;
                     tweetCount++;
 
                
                     }
                    
                } catch (ParseException ex) {
                    Logger.getLogger(twitter_stream.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            
        
            }
      response.getStream().close();
    }   catch (IOException ex) {
            Logger.getLogger(twitter_stream.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
   public void stop_stream()
    {
        run=false;
    }
    
}
