/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import database.DatabaseConnectionSingleton;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import org.json.*;
/**
 *
 * @author George
 */
public class GenerateURL {
    
    //chagne this if necessary
    private static final String driveName = "/home/ftpuser" ;
    
    private static String folderNames;
    private static String fileName;
    private static String studentAndIDFolderName;
    private static String yearFolderName;
    private static String termFolderName;
    private static String targetFolderName;
    private static String path;
    
    public static String createPath(String studentName, int studentNumber, 
            int latterYear, String termString, String targetString, String fileString)
    {
        try 
            {
                //driveName = "/home/ftpuser/"; //////////// check this as/when deploying!

                        String editedStudentName = studentName.replaceAll("\\s","-");

                        editedStudentName += "_" + studentNumber;

                    studentAndIDFolderName = editedStudentName;

                        int earlierYear = latterYear - 1;

                        String fullYearTitleFormatted = earlierYear + "-" + latterYear;

                    yearFolderName = fullYearTitleFormatted;

                    termFolderName = termString;

                    targetFolderName = targetString;

                    // check whether student then year is better than year then student (I think so)
                folderNames =
                        studentAndIDFolderName
                        + File.separator +
                        yearFolderName
                        + File.separator +
                        termFolderName
                        + File.separator +
                        targetFolderName;

                fileName = fileString; 

                //http://stackoverflow.com/questions/6142901/how-to-create-a-file-in-a-directory-in-java
                String path = driveName
                        + File.separator 
                        + folderNames
                        + File.separator 
                        + fileName;

                // for uea
                System.out.println("path = " + path);
                
                return path;

            }
            catch (Exception e)
            {
                return "path_error";
            }
    }
    
    public static Boolean insertFile(String path)
    {
        try 
        {
            
            File f = new File(path);
            
            //(works for both Windows and Linux)
            f.getParentFile().mkdirs();

            f.createNewFile();
            
        }
        catch (IOException e)
        {
            
            return false;
            
        }        
        
        return true;
        
    }
    
    public static Boolean insertIntoDB(String externFileName, String externPath)
    {
        
        String sql = "INSERT INTO evidence_data(media_id, file_url) "
                            + "VALUES ((SELECT evidence.media_id FROM evidence "
                            + "WHERE evidence.file_name = '"
                            + externFileName
                            + "'), '"
                            + externPath
                            + "');"
                            ;
        
        try 
        {

            Statement statement = DatabaseConnectionSingleton.getStatementInstance();            

            int resultInt;

            resultInt = statement.executeUpdate(sql);

            System.out.println("rows affected number (should be 1) = " + resultInt);

            DatabaseConnectionSingleton.closeConnection();

        } 
        catch (ServletException | SQLException ex) 
        {

            Logger.getLogger(GenerateURL.class.getName()).log(Level.SEVERE, null, ex);

            try {
                DatabaseConnectionSingleton.closeConnection();
            } catch (SQLException ex1) {
                Logger.getLogger(GenerateURL.class.getName()).log(Level.SEVERE, null, ex1);
            }

            return false;

        }
        
        return true;        
        
    }
    
    
    //creates folders/file AND returns path name
    // if error returns type of error
    public static String createURL(String studentName, int studentNumber, 
            int latterYear, String termString, String targetString, String fileString)
    {
                    
            path = createPath( studentName,  studentNumber, latterYear, 
                                        termString,  targetString,  fileString);

            if (path.equals("path_error"))
            {
                return path;
            }
            
            Boolean insertFile = insertFile(path);
            
            if (insertFile == false)
            {
                String toReturn = path + "#file_error";
                
                return toReturn;
            }
        
            Boolean insertIntoDB = insertIntoDB(fileName, path);
            
            if (insertIntoDB == false)
            {
                String toReturn = path + "#sql_error";
                
                return toReturn;
            }

            return path;
            
    }
   
    public static void main(String[] args) throws IOException, JSONException {
        
        
                JSONObject realRequestMessage = new JSONObject();      
        realRequestMessage.put("instruction_tag", "upload_record");
        realRequestMessage.put("file_name", "3945-MSC000040294.txt");
        realRequestMessage.put("student_name", "Donatello");
        realRequestMessage.put("spt", "Maths");
        realRequestMessage.put("spt_level", "Initial");
        realRequestMessage.put("observations", 
                "This radical turtle got through Java to be here!");
        realRequestMessage.put("date_stamp", "19-06-2015");
        realRequestMessage.put("term_name", "Winter");
        realRequestMessage.put("student_number", 7);
        realRequestMessage.put("academic_year", 2015);
        
        String theStudentName = realRequestMessage.getString("student_name");

                int studentNumber = (int) realRequestMessage.get("student_number");

                int latterYear = (int) realRequestMessage.get("academic_year");

                String termString = realRequestMessage.getString("term_name");

                String targetString = realRequestMessage.getString("spt");

                String fileString = realRequestMessage.getString("file_name");
            
            
            String generatedURL = 
                    GenerateURL.createURL("Rafael van der Vaart", 6, latterYear, termString, targetString, fileString);
            
//            path = C:\Donatello_7\2014-2015\Winter\Maths\3945-MSC000040294.txt
        
            
    }
    
}