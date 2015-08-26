package dbms.newYear;

import database.DatabaseConnectionSingleton;
import dbms.DisplayInfoBean;
import dbms.records.RecordsToDisplay;
import java.io.IOException;
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

@WebServlet(name = "StartDisplayListServlet", 
        urlPatterns = {"/StartDisplayListServlet"})

public class StartDisplayListServlet extends HttpServlet 
{
        
    private ArrayList<String> yearList;
    private ArrayList<String> listToSend;    
    private String objectTypeToSend;
    private JSONObject newInfoToSend;
    
    
    // for display all records
    private ArrayList<Integer> mediaIdArray;
    private ArrayList<String> fileNameArray;
    private ArrayList<String> levelArray;
    private ArrayList<String> observationArray;
    private ArrayList<String> mediaDateArray;
    private ArrayList<String> specificSptArray;
    private ArrayList<String> selectedTermCapitalisedArray;
    private ArrayList<String> specificStudentArray;
    private ArrayList<String> specificClassArray;
    private ArrayList<Integer> specificYearArray;

    
    
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
        
        System.out.println("We've reached the StartDisplayList server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
         // Using try {...} catch {...} for error control
        try 
        { 
                    
            if (request.getParameter("btn3") != null) 
            {
               
                // get all records from db and load local variables with them
                getAllRecords();
                
                // get display records from local variables into a bean
                
                RecordsToDisplay recordsToDisplay = 
                           new RecordsToDisplay(
                                   mediaIdArray,
                                   fileNameArray,
                                   // URLNameArray //TODO                                    
                                   // targetArray, //removed
                                   levelArray,
                                   observationArray,
                           
                                   mediaDateArray,
                                   specificSptArray, 
                                   selectedTermCapitalisedArray, 
                                   specificStudentArray, 
                                   specificClassArray,
                                   specificYearArray
                           );
                                
                // send to dispalyrecords page
                                
                request.setAttribute("recordsToDisplay", recordsToDisplay); 

                // Move to next page

                request.getRequestDispatcher("dbms/displayRecords/displayAllRecords.jsp").forward(request, response);
                
            }
            else
            {
                
            // find years, including current/active

                String sql = "SELECT academic_year, active FROM academic_year ORDER BY academic_year DESC;"; 
                
                Statement statement = DatabaseConnectionSingleton.getStatementInstance();

                ResultSet resultSet = statement.executeQuery(sql);

                // we will convert int results into strings to help uniform data input into displayList.jsp
                yearList = new ArrayList();

                int activeYear = -1;

                while (resultSet.next())
                {
                        int yearInt = resultSet.getInt("academic_year");

                        String year = Integer.toString(yearInt);

                        yearList.add(year);

                        Boolean isActive = resultSet.getBoolean("active");

                        if (isActive == true)
                                {
                                        activeYear = yearInt;
                                }
                }

                System.out.println("the active year is: " + activeYear 
                        + " (this cannot be -1)");

                // close the db connection

                DatabaseConnectionSingleton.closeConnection();


                ///////// sectioon break ///////

                newInfoToSend = new JSONObject();

                newInfoToSend.put("active_academic_year", activeYear);


                // establish what the user wants to do based on button clicking

                if (request.getParameter("btn1") != null) 
                {
                    System.out.println("serach button 1 fired - choosing just active year...");
                    
                    newInfoToSend.put("selected_year", Integer.toString(activeYear));
                    
                // load variables
                    objectTypeToSend = "Class";
                    // laready done JSONObject
                    // listtosend is below
                    
                // get list to display (ie classes with the active year)
                    
                    listToSend = new ArrayList();
                    
                    listToSend = findClasses(activeYear);
                    
                    if (listToSend.isEmpty())
                    {
                    
                        request.setAttribute("error_message", "Could not find any classes!");

                        // Send it on to a different View
                        request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);              
                        
                    }
                    else
                    {
                        //proceed
                        
                    // put results in a bean
                        DisplayInfoBean displayInfoBean = new DisplayInfoBean(objectTypeToSend, listToSend, newInfoToSend);

                    // load bean into next apge   
                        request.setAttribute("displayInfoBean", displayInfoBean); 

                    // Move to next page
                        request.getRequestDispatcher("dbms/displayRecords/displayArray.jsp").forward(request, response);
                        
                    }
                    
                }
                else if (request.getParameter("btn2") != null) 
                {
            
                    System.out.println("serach button 2 fired - displaying all years...");
                                             
                    // put results into local variables 
           
                    objectTypeToSend = "Year";           
                    listToSend = yearList;           
           
                    DisplayInfoBean displayInfoBean = new DisplayInfoBean(objectTypeToSend, listToSend, newInfoToSend);

                    request.setAttribute("displayInfoBean", displayInfoBean); 

            
                // Move to next page
           
                    request.getRequestDispatcher("/dbms/displayRecords/displayArray.jsp").forward(request, response); //temp change should be displayArray
                 
                }
                else
                {
                    request.setAttribute("error_message", "Failure to select an appropriate button."
                            + " Please contact your system administrator.");
                    
                    // Send it on to a different View
                    request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
                
                }
    
           }

        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        } 
        catch (SQLException ex) {
            
            Logger.getLogger(StartDisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);

            request.setAttribute("error_message", ex);
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);            
        }
    
    }
    
    public void getAllRecords()
    {
        
    // reset variables
        
        mediaIdArray = new ArrayList<>();               
        fileNameArray  = new ArrayList<>();                           
        levelArray = new ArrayList<>();          
        observationArray = new ArrayList<>(); 
        mediaDateArray      = new ArrayList<>();                               
        specificSptArray   = new ArrayList<>();                          
        selectedTermCapitalisedArray  = new ArrayList<>();                           
        specificStudentArray      = new ArrayList<>();     
        specificClassArray      = new ArrayList<>();                       
        specificYearArray = new ArrayList<>();
        
        String sql = "SELECT DISTINCT evidence.media_id, evidence_data.file_url AS file_name, "
                + "student.s_name, evidence.support_plan_target, evidence.support_plan_level, "
                + "evidence.observation, evidence.media_date, whichYear(evidence.media_date), "
                + "classroom.class_name, "
                + "whichTerm(EXTRACT(year FROM evidence.media_date)::int,evidence.media_date) "
                + "FROM evidence, student, class_year, classroom, academic_year, "
                + ""
                + "student_in_class_year, evidence_data WHERE student.s_no = evidence.s_no "
                + "AND classroom.class_id = class_year.class_id "
                + "AND student_in_class_year.class_id = class_year.class_id "
                + "AND academic_year.academic_year = whichYear(evidence.media_date) "
                + "AND student.s_no = student_in_class_year.s_no "
                + "AND academic_year.academic_year = student_in_class_year.academic_year "
                + "AND evidence.media_id = evidence_data.media_id"
                + ";"
                ;
        
        try {
            
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            DatabaseConnectionSingleton.closeConnection();
        
            while (resultSet.next())
            {
                
            // get iteration of results  
                
                int media_id = resultSet.getInt("media_id");
                String s_name = resultSet.getString("s_name");
                String file_name = resultSet.getString("file_name");
                String support_plan_target = resultSet.getString("support_plan_target");
                String support_plan_level = resultSet.getString("support_plan_level");
                String observation = resultSet.getString("observation");
                String media_date = resultSet.getString("media_date"); //convert
                int whichyear = resultSet.getInt("whichyear"); //convert
                String class_name = resultSet.getString("class_name");
                String whichterm = resultSet.getString("whichterm"); //capitalise?
                
            // format result
                
                int mediaId = media_id;                
                
                String fileName = file_name;
                
                String level = support_plan_level;
                
                //String observation = observation;
                
                    String[] dateParts = media_date.split("-");                                
                String mediaDate = dateParts[2] + "-" + dateParts[1] + "-" + dateParts[0];
                
                String specificSpt = support_plan_target;                
                
                String selectedTermCapitalised = whichterm.substring(0, 1).toUpperCase() + whichterm.substring(1);
                
                String specificStudent = s_name;
                
                String specificClass = class_name;
                
                int specificYear = whichyear;
                
                
            // add iteration to array
        
                mediaIdArray.add(mediaId);
                fileNameArray.add(fileName);                           
                levelArray.add(level);         
                observationArray.add(observation);
                mediaDateArray.add(mediaDate);                                   
                specificSptArray.add(specificSpt);                           
                selectedTermCapitalisedArray.add(selectedTermCapitalised);                           
                specificStudentArray.add(specificStudent);        
                specificClassArray.add(specificClass);                           
                specificYearArray.add(specificYear);                         
                // URLNameArray //TODO
                // targetArray, //removed
            }
            
        } catch (SQLException | ServletException ex) {
            Logger.getLogger(DisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // no need to return anything as we've loaded object varaibles
                                
    }
    
    public ArrayList<String> findClasses(int selectedYear)
    {
        
        ArrayList<String> resultArray = new ArrayList<String>();
        
        String sql = "SELECT DISTINCT classroom.class_name AS "
                + "result"
                + " "
                + "FROM classroom, class_year, academic_year "
                + "WHERE classroom.class_id = class_year.class_id "
                + "AND class_year.academic_year = "
                + selectedYear
                + ";";
        
        //System.out.println("sql = " + sql);
        
        try {
            
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            DatabaseConnectionSingleton.closeConnection();
        
            while (resultSet.next())
            {
                resultArray.add(resultSet.getString("result"));
            }
            
        } catch (SQLException | ServletException ex) {
            Logger.getLogger(DisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultArray;
                
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
            Logger.getLogger(StartDisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StartDisplayListServlet.class.getName()).log(Level.SEVERE, null, ex);
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
