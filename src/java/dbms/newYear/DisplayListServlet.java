package dbms.newYear;

import database.DatabaseConnectionSingleton;
import dbms.DisplayInfoBean;
import dbms.records.RecordsToDisplay;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "DisplayListServlet", 
        urlPatterns = {"/DisplayListServlet"})

public class DisplayListServlet extends HttpServlet 
{
    
    private String selectedValueString;
    private String newObjectType;
    
    private String oldObjectType;
    private String oldListSizeString;
    private int oldListSize;
    private String oldPreviousInfoString;
    
    private ArrayList<String> oldList;
    
    private JSONObject newInfoToSend;
    
    private ArrayList<String> newListToSend;
    
    private String sql;
    
    private Statement statement;
    
    private ResultSet resultSet;
        
    private final String[] termNames = new String[]{"winter", "spring", "summer"};
    
    private ArrayList<Integer> mediaIdArray;
    private ArrayList<String> fileNameArray;
    private ArrayList<String> levelArray;
    private ArrayList<String> targetArray;
    private ArrayList<String> observationArray;
    private ArrayList<String> mediaDateArray;
    

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException 
    {
        
        System.out.println("We've reached the diplaylist server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
        // Using try {...} catch {...} for error control
        try 
        { 
            
            
            newListToSend = new ArrayList();
       
        // get info from webpage
            
            
            oldObjectType = request.getParameter("objecttype");
            
            oldListSizeString = request.getParameter("listsize");
            
            oldPreviousInfoString = request.getParameter("previousinfo");
            
            // we'll convert it to the 'new' JSON later
            // oldPreviousInfo = new JSONObject(oldPreviousInfoString);
            
            oldListSize = Integer.parseInt(oldListSizeString);
            
            oldList = new ArrayList<>();
            
            
        // find out what selected value was
            System.out.println("john");
          
        
            for (int i = 0; i < oldListSize; i++)
            {
                
                //System.out.println("going round the well for the #" + i + " time!");
                
                
            // get each array list attributes from jsp page
                
                String listParameterName = "list" + i;
                
                String listParameterValue = request.getParameter(listParameterName);
                                
                oldList.add(listParameterValue);
                
            // check each corresponding button to see if it has been fired
                
                String buttonName = "btn" + i;
                
                //System.out.println("buttonName = " + buttonName);
                
                if (request.getParameter(buttonName) != null)
                {
                    // ie this button was fired, so current parameter value corresponds to that
                    selectedValueString = listParameterValue;
                    
                }                
                
            }
            
            System.out.println("selectedValueString = " + selectedValueString);
          
            
            //System.out.println("jim");
                        
            
        // find out what the previous/next object TYPE is & process SQL etc
            
            newInfoToSend = new JSONObject(oldPreviousInfoString);
            

         //   System.out.println("old type to be used to discover nd process new type = " + oldObjectType);

            if (null != oldObjectType)                
            switch (oldObjectType) {

                case "Year":

                    newObjectType = "Class";
                    newInfoToSend.put("selected_year", selectedValueString);
                    
                    // unpack any required variables for findnextlist method here
                        //TODO
                    
                    newListToSend = findClasses(selectedValueString);
                        // what data do we need to store here?   
                    
                    break;

                  case "Class":

                    newObjectType = "Student";
                    newInfoToSend.put("selected_class", selectedValueString);
                    
                    // unpack any required variables for findnextlist method here
                        
                    String theSelectedYear = (String) newInfoToSend.get("selected_year");
                    String theSelectedClassName = newInfoToSend.getString("selected_class");                  
                                        
                    newListToSend = findStudents(selectedValueString, theSelectedYear, theSelectedClassName);
                  
                    
                    break;

                case "Student":

                    newObjectType = "Term";
                    newInfoToSend.put("selected_student", selectedValueString);
                    
                    theSelectedYear = (String) newInfoToSend.get("selected_year");
                    
                    newListToSend = findTerms(selectedValueString, theSelectedYear);
                    
                    if //there aren't any records 
                    (
                            
                            newListToSend.get(0).equals("false")
                            &&
                            newListToSend.get(1).equals("false")
                            &&
                            newListToSend.get(2).equals("false")
                            
                    )
                    {
                       
                        //then make the result list blank
                        newListToSend = new ArrayList();
                    }
                    
                    
                         
                    break;

                case "Term":

                    newObjectType = "Target";
                                        
                    newInfoToSend.put("selected_term", selectedValueString);
                    
                    String selectedAcYear = (String) newInfoToSend.get("selected_year");
                    
                    String selectedStudent = newInfoToSend.getString("selected_student");
                                                            
                    newListToSend = findTargets(selectedValueString, selectedAcYear, selectedStudent);
                                
                    
                    break;
                    
                case "Target":

                    newObjectType = "Records";
                                        
                    newInfoToSend.put("selected_target", selectedValueString);
                    
                    selectedAcYear = (String) newInfoToSend.get("selected_year");
                    
                    selectedStudent = newInfoToSend.getString("selected_student");
                    
                    String selectedTerm = newInfoToSend.getString("selected_term");
                                                          
                    findRecords(selectedTerm, selectedValueString, selectedAcYear, selectedStudent);
                    
                    // check to see if records are null here???
          
                    
                    break;
                    
                default:
                    
                    //THERE HAS BEEN AN ERROR
                    
                    System.out.println("error discovering new object type");
                    
                    newListToSend = new ArrayList();
                    
                    break;
            }
            else {
                
                System.out.println("Error unparsing *null* old object type");
                
                newListToSend = new ArrayList();
                      
            }
            
            if (newListToSend.size() < 1) // then there are no records to display
            {
                
                String resultMessage;
                
                resultMessage = "Sorry, no records exist for " 
                        + oldObjectType + " '" 
                        + selectedValueString + "'. Please search again for different records"
                        + " or select the 'Display All Records' option."  ;
                                
                request.setAttribute("update_result", resultMessage);
                        
                // Move to next page
                request.getRequestDispatcher("dbms/updateResult.jsp").forward(request, response); //temp change should be displayArray
                       
            }        
            else if (newObjectType.equals("Records") == true) // then move onto a different page
            {
                
                // unpack required data 
                
                    
                
                    String specificSpt = selectedValueString; 
                    String selectedTermTemp = newInfoToSend.getString("selected_term"); 
                    
                     String selectedTermCapitalised = Character.toUpperCase(selectedTermTemp.charAt(0)) 
                            + selectedTermTemp.substring(1);
                    
                    //System.out.println("selected term capitalised = " + selectedTermCapitalised);
                    
                    String specificStudent = newInfoToSend.getString("selected_student");       
                    int specificYear = Integer.parseInt(newInfoToSend.getString("selected_year"));   
                    String specificClass = newInfoToSend.getString("selected_class");

                // search for next set of elements to display

                    RecordsToDisplay recordsToDisplay = 
                           new RecordsToDisplay(
                                   mediaIdArray,
                                   fileNameArray,
                                   // URLNameArray //TODO                                    
                                   // targetArray, //removed
                                   levelArray,
                                   observationArray,
                           
                                   mediaDateArray,
                                   specificSpt, 
                                   selectedTermCapitalised, 
                                   specificStudent, 
                                   specificClass,
                                   specificYear
                           );
                    

                    request.setAttribute("recordsToDisplay", recordsToDisplay); 

                // Move to next page

                    request.getRequestDispatcher("dbms/displayRecords/displayRecords.jsp").forward(request, response); //temp change should be displayArray
                    
            }
            else // proceed to generate a new list using displayArray.jsp
            {               

                    // check that the process has worked
                    System.out.println("displayInfoBean = " + "\n" 
                                   + "newObjectType: " + newObjectType + "\n" 
                                    + "newListtoSend: " + newListToSend + "\n" 
                                   + "newInfoTOsend" +  newInfoToSend);

                    // put results in a bean
                    DisplayInfoBean displayInfoBean = new DisplayInfoBean(newObjectType, newListToSend, newInfoToSend);

                    // load bean into next apge   
                    request.setAttribute("displayInfoBean", displayInfoBean); 

                // Move to next page
                    request.getRequestDispatcher("dbms/displayRecords/displayArray.jsp").forward(request, response); //temp change should be displayArray

            
            }     
        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        }        

    }
    
    
    public ResultSet executeSQL(String sql)
    {
                
        try {
            
            statement = DatabaseConnectionSingleton.getStatementInstance();
            
            resultSet = statement.executeQuery(sql);
            
            DatabaseConnectionSingleton.closeConnection();
                        
        } catch (ServletException ex) {
            Logger.getLogger(DisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultSet;
    }
    
    public ArrayList<String> processResultSet(ResultSet rs)
    {
        
        ArrayList<String> resultArray = new ArrayList<>();
        
        try {
            while (rs.next())
            {
                resultArray.add(resultSet.getString("result"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultArray;
    }
    
    public ArrayList<String> findClasses(String selectedYear)
    {
        
        sql = "SELECT DISTINCT classroom.class_name AS "
                + "result"
                + " "
                + "FROM classroom, class_year, academic_year "
                + "WHERE classroom.class_id = class_year.class_id "
                + "AND class_year.academic_year = "
                + selectedYear
                + ";";
        
        //System.out.println("sql = " + sql);
        
        resultSet = executeSQL(sql);
        
        newListToSend = processResultSet(resultSet);
        
        return newListToSend;
                
    }
    
    public ArrayList<String> findStudents (String selectedClass, String selectedYear, String selectedClassName)
    {
        
        sql = "SELECT DISTINCT student.s_name AS "
                + "result"
                + " "
                + "FROM student, student_in_class_year, classroom, class_year, academic_year "
                + "WHERE student.s_no = student_in_class_year.s_no "
                + "AND student_in_class_year.active = TRUE "
                + "AND student_in_class_year.class_id = classroom.class_id "
                + "AND student_in_class_year.academic_year = "
                + selectedYear
                + " "
                + "AND classroom.class_name = '"
                + selectedClassName
                + "';";
                        
        //System.out.println("sql = " + sql);
        
        resultSet = executeSQL(sql);
        
        newListToSend = processResultSet(resultSet);
        
        return newListToSend;
        
    }
    
    // gets a list of terms TO DISPLAY
    public ArrayList<String> findTerms(String theStudent, String selectedYear)
    {
        
        newListToSend = new ArrayList<>();
        
        try {
            
            
            // open database
            statement = DatabaseConnectionSingleton.getStatementInstance();
            
            for (int i = 0; i < termNames.length; i++)
            {
                
                // get the term name
                
                String termToCheck = termNames[i];
                
                // check database if that term has a record
                
                sql = "SELECT EXISTS "
                        + "(SELECT evidence.media_id, student.s_name, academic_year.academic_year "
                        + "FROM evidence, student, academic_year "
                        + "WHERE evidence.s_no = student.s_no "
                        + "AND academic_year.academic_year = "
                        + selectedYear
                        + " AND whichTerm("
                        + selectedYear
                        + ",evidence.media_date) = '"
                        + termToCheck
                        + "' AND student.s_name = '"
                        + theStudent
                        + "') AS result;";
                
                System.out.println("sql = " + sql);
                
                resultSet = statement.executeQuery(sql);
                
                resultSet.next();          
                
                // process 'true' terms as list to send back
                // i.e., three strings are added, corresponding to whether respective terms have results in them or not
                // e.g., true-false-false would mean only winter has a result
                
                if (resultSet.getBoolean("result") == true)
                {
                    
                    newListToSend.add("true");
                    
                }
                else
                {
                    newListToSend.add("false");
                }
                
            }
            
            // close database
            DatabaseConnectionSingleton.closeConnection();
            
           
        } catch (SQLException ex) {
            Logger.getLogger(DisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            Logger.getLogger(DisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return newListToSend;
        
    }
    
    public ArrayList<String> findTargets(String selectedTerm, String selectedAcYear, String selectedStudent)
    {
        
        sql = "SELECT DISTINCT evidence.support_plan_target AS result "
                + "FROM evidence, student, "
                + selectedTerm
                + " WHERE evidence.s_no = student.s_no "
                + "AND student.s_name = '"
                + selectedStudent
                + "' AND "
                + selectedTerm
                + ".academic_year = "
                + selectedAcYear
                + ";";
        
        System.out.println("sql = " + sql);
        
        resultSet = executeSQL(sql);
        
        newListToSend = processResultSet(resultSet);
        
        return newListToSend;    
    
    }
    
    public void findRecords(
            String selectedTerm, String selectedTarget, String selectedAcYear, String selectedStudent)
    {
        
        sql = "SELECT DISTINCT "
                + "evidence.media_id, evidence_data.file_url AS file_name, "
                + "evidence.support_plan_target, evidence.support_plan_level, "
                + "evidence.observation, evidence.media_date "
                + "FROM evidence, evidence_data, student, student_in_class_year, classroom, "
                + selectedTerm
                + " "
                + "WHERE student.s_no = evidence.s_no "
                + "AND student_in_class_year.s_no = evidence.s_no "
                + "AND student_in_class_year.academic_year = whichYear(evidence.media_date) "
                + "AND student_in_class_year.class_id = classroom.class_id "
                + "AND student.s_name = '"
                + selectedStudent
                + "' AND "
                + selectedTerm
                + ".academic_year = "
                + selectedAcYear
                + " AND evidence.support_plan_target = '"
                + selectedTarget
                + "' AND evidence.media_id = evidence_data.media_id"
                + ";";
                
        System.out.println("sql = " + sql);
        
        resultSet = executeSQL(sql);
        
        // process resultSet
        
        mediaIdArray = new ArrayList<>();
        fileNameArray = new ArrayList<>();
        targetArray = new ArrayList<>();
        levelArray = new ArrayList<>();
        observationArray = new ArrayList<>();
        mediaDateArray = new ArrayList<>();
        
        try {
            while (resultSet.next())
            {
                mediaIdArray.add(resultSet.getInt("media_id"));
                fileNameArray.add(resultSet.getString("file_name"));
                // targetArray.add(resultSet.getString("file_name"));
                levelArray.add(resultSet.getString("support_plan_level"));
                observationArray.add(resultSet.getString("observation"));
                    // explicit casting, just to be sure
                    Date dateToConvert = (Date) resultSet.getObject("media_date");   
                    String dateString = dateToConvert.toString();
                    
                mediaDateArray.add(dateString);
                newListToSend.add("Record");
                                
            }
        } catch (SQLException ex) {
            Logger.getLogger(DisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(DisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(DisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
}
