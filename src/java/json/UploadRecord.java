/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package json;

import database.DatabaseConnectionSingleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * sends back either un/successful uploaded record as json message
 * @author cut14jhu
 */
public class UploadRecord {
    
    private static String sql;
    
    private static String errorMessage;
    
    private static JSONObject returnMessageToApp;
    
    public static JSONObject uploadRecord(JSONObject jsonObj) throws JSONException{

        System.out.println("Now executing 'uploading a record' code...");
                
        // adds all of the record's details that were uploaded easily
        returnMessageToApp = jsonObj;
        
        errorMessage = new String();
        
         String fileName = new String();
            String studentName = new String();
            String target = new String();
            String level = new String();
            String observation;
            String dateStamp = new String();
            int isImage = 2;
            
    // attempt to extract JSON values       
        
        try
        {
            observation = jsonObj.getString("observation"); // e.g. "Tying up his shoelaces" >> could be blank though!!            
        }
        catch (JSONException e)
        {
            observation = "";
        }
            
        try
        {
        
             fileName = jsonObj.getString("file_name"); // e.g. "5489756-MSC0000040302.mpeg"
             studentName = jsonObj.getString("student_name"); // e.g. "John Smith"
             target = jsonObj.getString("target"); // e.g "Communication"
             level = jsonObj.getString("level"); // e.g. ""
             
             dateStamp = jsonObj.getString("date_stamp"); // e.g. "19-07-2015"
             isImage = jsonObj.getInt("is_image");
        
        } catch (JSONException e)
        {
            errorMessage = "input";
        }
        
        if 
        (
            fileName.equals("")
            || 
            studentName.equals("")
            ||
            target.equals("")
            ||
            level.equals("")
            ||
            checkDateStamp(dateStamp) == false // see method, below
            // OBSERVATION CAN BE BLANK || observation.equals("")
            ||
            isImage == 2   //IE NOT 0 OR 1
        )
        {
            errorMessage = "input";
        }
        
        if (errorMessage.equals("input") == false)
        {
            
            //then proceed
            //(selse send back reject message)
                     
            // try to SQL insert the information (assuming it's correct)

            if // additional comments have been added
            (
                    observation != null
                    &&
                    observation.equals("") == false
            )
            {

                // remove ' for database integrity
                String observationTrimmed = observation.replace("'", "");

                System.out.println("Extra observations to add");

                sql = "INSERT INTO evidence (file_name, s_no, support_plan_target, "
                        + "support_plan_level, observation, media_date) "
                        + "SELECT '"
                        + fileName
                        + "', student.s_no, '"
                        + target
                        + "', '"
                        + level
                        + "', '"
                        + observation
                        + "', '"
                        + dateStamp
                        + "' FROM student WHERE student.s_name = '"
                        + studentName
                        + "';"
                        ;

                // System.out.println("check this extra observations: " + sql);

            }       
            else // no additioanl comments have been added
            {

                System.out.println("No extra observations to add");

                sql = "INSERT INTO evidence (file_name, s_no, support_plan_target, "
                    + "support_plan_level, media_date, is_image) "
                    + "SELECT '"
                    + fileName
                    + "', student.s_no, '"
                    + target
                    + "', '"
                    + level
                    + "', '"
                    + dateStamp
                    + "', "
                    + isImage
                    + " FROM student WHERE student.s_name = '"
                    + studentName
                    + "';"
                    ;

                //System.out.println("cehck this NO extra observations: " + sql);

            }      

            // returnMessageToApp.put("sql_to_submit", sql);
            
            //System.out.println("sql for ervidence insert = \n" + sql);
            int rowsAffected = 0; 

            try
            {
                Statement statement = DatabaseConnectionSingleton.getStatementInstance();
                rowsAffected = statement.executeUpdate(sql);
            }
            catch (SQLException e)
            {
                errorMessage = e.toString();
               
            } catch (ServletException ex) {                
                Logger.getLogger(UploadRecord.class.getName()).log(Level.SEVERE, null, ex);                
            }

            if (rowsAffected == 0)
            {
                //error

                // adds response message
                returnMessageToApp.put("response_tag", "uploaded_record_failure");
                returnMessageToApp.put("error_message", 
                        "No rows were affected by insert statement.");

            }
            else
            {

                boolean proceed = true;

                //System.out.println("rows affected asfter executing update = " + rowsAffected);

                String otherSql = 
                        "SELECT "
                        + "whichTerm(EXTRACT(year FROM evidence.media_date)::int,evidence.media_date) AS term, "
                        + "s_no AS number "
                        + ", whichYear(evidence.media_date) AS year "
                        + "FROM evidence WHERE evidence.file_name = '"
                        + fileName
                        + "';";

                //System.out.println("sql to test: " + otherSql);

                int studentNo = 0;
                int academicYear = 0;
                String termName = "";
                
                try
                {
                    
                    Statement statement = DatabaseConnectionSingleton.getStatementInstance();

                    ResultSet resultSet = statement.executeQuery(otherSql);

                    resultSet.next();

                     studentNo = resultSet.getInt("number");
                     termName = resultSet.getString("term");
                     academicYear = resultSet.getInt("year");

                    returnMessageToApp.put("student_number", studentNo);
                    returnMessageToApp.put("term_name", termName);
                    returnMessageToApp.put("academic_year", academicYear);

                    proceed = true;

                }
                catch (SQLException | JSONException e)
                {
                    proceed = false;
                    errorMessage = e.toString();
                    
                } catch (ServletException ex) {
                    Logger.getLogger(UploadRecord.class.getName()).log(Level.SEVERE, null, ex);
                    
                    proceed = false;
                }
                
                if (studentNo < 1 || academicYear < 1 || termName.equals(""))
                {               
                    proceed = false;
                }

                if (proceed)
                {
                //try to enter file url
                    //create path
                    // add to db
                    
                    String pathResult = GenerateURL.createPath(studentName, studentNo, academicYear, termName, target, fileName);
                    
                    if (pathResult.equals("path_error"))
                    {
                        
                        returnMessageToApp.put("response_tag", "uploaded_record_failure");
                        returnMessageToApp.put("error_message", 
                                    "Error compiling the path. "
                                        + "Database was not updated with the url path. "
                                            + "Evidence record still exists in database, however.");
                        
                
                    }
                    else
                    {
                        //success!
                            
                            // send success message back to app

                            returnMessageToApp.put("URL", pathResult);

                            //objUploadRecord.put("file_name", "U/2013/1/34/1/3/3945-MSC000040294.mpeg");

                            // adds response message
                            returnMessageToApp.put("response_tag", "uploaded_record_success");
                        
                                            
                    }
                    
                }
                else // procceed = flase
                {
                    //error with getting all info

                    returnMessageToApp.put("response_tag", "uploaded_record_failure");
                    
                    String manyErrorDetails = "Data inserted into database, but not all information could be retrieved. Please try again, "
                                + "or contact your system administrator.";
                    
                    if (errorMessage.equals(""))
                    {}
                    else
                    {
                        manyErrorDetails = " " + errorMessage;
                    }
                    
                    returnMessageToApp.put("error_message", manyErrorDetails);
                    
                }
        
            }
                    
        }
        else
        {
            //error with input
            
            returnMessageToApp.put("response_tag", "insufficient_info_provided");
            
        } 
                
    // send message back to app
           
        return returnMessageToApp;
        
    }
    
     public static void main(String[] args)
    {
        String john = "19-06-2015";
        
        boolean peter = checkDateStamp(john);
        
        System.out.println("peter = " + peter);
    }

    private static boolean checkDateStamp(String dateStamp) {
        
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
   
       // formatter.setLenient(false);

        try 
        {

            Date date = formatter.parse(dateStamp);

        } 
        catch (ParseException e) 
        {
        
            return false;

        }

        return true;
        
    }
    
}
