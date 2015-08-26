/*
 * To change this license header, 
choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import database.DatabaseConnectionSingleton;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * sends back either un/successful uploaded record as json message
 * @author cut14jhu
 */
public class InsertURL {
    
    private static String sql;
    
    private static String errorMessage;
    
    private static JSONObject returnMessageToApp;
    
    public static JSONObject insertURL(JSONObject jsonObj) 
            throws JSONException{

        System.out.println("Now executing 'uploading a record' code...");
                
        // adds all of the record's details that were uploaded easily
        returnMessageToApp = jsonObj;
        
        errorMessage = new String();
        
        String fileName = new String();
        String URL = new String();
        
        
        
            
    // attempt to extract JSON values       
        
        try
        {
        
             fileName = jsonObj.getString("file_name"); 
                    // e.g. "5489756-MSC0000040302.mpeg"
             URL = jsonObj.getString("URL"); // e.g. "John Smith"
             
        
        } catch (JSONException e)
        {
            errorMessage = "input";
        }
        
        if 
        (
            fileName.equals("")
            || 
            URL.equals("")            
        )
        {
            errorMessage = "input";
        }
        
        if (errorMessage.equals("input") == false)
        {
            
            //then proceed
            //(selse send back reject message)
                     
            // try to SQL insert the information (assuming it's correct)

            sql = "insert into evidence_data(media_id, file_url) "
                    + "VALUES (("
                    + "SELECT evidence.media_id FROM evidence "
                    + "WHERE file_name = '"
                    + fileName
                    + "'), '"
                    + URL
                    + "')"
                    + ";"
                    ;

            // returnMessageToApp.put("sql_to_submit", sql);
            
            //System.out.println("sql for ervidence insert = \n" + sql);
            int rowsAffected = 0; 
            
            try
            {
                Statement statement = 
                        DatabaseConnectionSingleton.getStatementInstance();
                rowsAffected = statement.executeUpdate(sql);
                
                errorMessage = "";
            }
            catch (SQLException e)
            {
                errorMessage = e.toString();
               
            } catch (ServletException ex) {                
Logger.getLogger(InsertURL.class.getName()).log(Level.SEVERE, null, ex);

                errorMessage = "servlet error";
            }

            if (rowsAffected == 0)
            {
                //error

                // adds response message
                returnMessageToApp.put("response_tag", 
                        "unsuccessful_insert_URL");
                
                if (errorMessage.equals(""))
                {
                    //returnMessageToApp.put("error_message", 
                      //  "No rows were affected by insert statement.");
                
                /////////////////////////////////////////////////////////////////////////////////////////
                        // temp for testing!
                    returnMessageToApp.put("error_message", "sql problem = " + sql);
                }
                else
                {
                    returnMessageToApp.put("error_message", errorMessage);
                }
                
            }
            else
            {
                
                // adds response message
                returnMessageToApp.put("response_tag", 
                        "successful_insert_URL");
        
            }
                    
        }
        else
        {
            //error with input
            
            returnMessageToApp.put("response_tag", 
                    "insufficient_info_provided");
            
        } 
                
    // send message back to app
           
        return returnMessageToApp;
        
    }
   
    
}
