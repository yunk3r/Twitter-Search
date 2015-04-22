package com.mycompany.mavenproject1;



import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import np.com.ngopal.control.AutoFillTextBox;
import com.mycompany.mavenproject1.twitter_stream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;



public class FXMLController implements Initializable {
    
private Connection connect=null;
	private Statement statement=null;
	private PreparedStatement ps=null;
	private ResultSet result=null;
        twitter_stream stream;
        
     @FXML
    //private ComboBox comboBox1;
     private AutoFillTextBox fill;
     @FXML
    private TextField text;
     @FXML
     private TextArea textArea;
     
      @FXML
    private void handleButtonAction(ActionEvent event) throws InterruptedException {
      String emoji="";
      twitter_stream streamConsumer = new twitter_stream();
      stream=streamConsumer;
        try
               {
                   //emoji  = ((String) comboBox1.getValue()).substring(0, 2).trim();
                   if(fill.getText().equals("NONE"))
                   {
                       emoji=" ";
                   }
                   else
                   {
                   emoji  = ((String) fill.getText()).substring(fill.getText().length()-2, fill.getText().length()).trim();
                   }
               }
        catch(Exception e)
        {
            
        }
       String textSearch=text.getText();
       streamConsumer.setSearchEmoji(emoji);
       streamConsumer.setSearchText(textSearch);
         String tweet;
         textArea.setText("");
         streamConsumer.start();
         thread();
         
          
      
    }
    @FXML
     private void handleButtonActionStop(ActionEvent event) {
        
         stream.stop_stream();
     
     }
     
    public void DropDown()
    {
   
         ObservableList<String> data = FXCollections.observableArrayList();
        //comboBox1.getItems().add("    ");
         data.add("NONE");
        for(Emoji emoji: EmojiManager.getAll())
        {
           
            data.add(emoji.getDescription()+" "+emoji.getUnicode());
        }
        fill.setData(data);
       fill.setFilterMode(true);
        fill.setListLimit(20);
       
       
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DropDown();
     
    }    
    
 
    public void thread()
    {
    Thread t= new Thread(new Runnable() {
              int j=0;
            public void run() {
                while (stream.getRun()) {
               
                     try { Thread.sleep(100); } catch (Exception e) {}
                    if(j<stream.getTweetCount())
                     {
          
                     
                    Platform.runLater(new Runnable() {
                        public void run() {
                            
                             textArea.insertText(0,stream.getTweet());
                        }
                    });
                    j++;
                     }
                   
                            
                }
            }
        });
    
    t.start();
    }
    
}
