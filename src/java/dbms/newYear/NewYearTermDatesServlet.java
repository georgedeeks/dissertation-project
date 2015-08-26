package dbms.newYear;

import database.DatabaseConnectionSingleton;
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
import dbms.DisplayInfoBean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "NewYearTermDatesServlet", 
        urlPatterns = {"/NewYearTermDatesServlet"})

public class NewYearTermDatesServlet extends HttpServlet 
{
    
    private int selectedDBYear;
    private Statement statement;
    private Boolean sendBack;
    private String errorMessage;

    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws org.json.JSONException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException 
    {
        
        System.out.println("We've reached the NewYearTermDatesServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
        sendBack = false;
        errorMessage = null;
        
        try 
        { 
            
        //get elements from previous page

            String displayYear = request.getParameter("displayYear");  
            
            String[] splitDisplayYear = displayYear.split("-");
            String selectedDBYearString = splitDisplayYear[1];
            selectedDBYear = Integer.parseInt(selectedDBYearString);            
            
            String winterStartDate = request.getParameter("datepicker0");

            String winterEndDate = request.getParameter("datepicker1");

            String springStartDate = request.getParameter("datepicker2");

            String springEndDate = request.getParameter("datepicker3");

            String summerStartDate = request.getParameter("datepicker4");

            String summerEndDate = request.getParameter("datepicker5");

        // check variables for testing
            
            // non eed to null check the term picker - done in jsp page

            System.out.println("selected DB year = " + selectedDBYear);                    

            System.out.println("winterStartDate = " + winterStartDate);

            System.out.println("winterEndDate = " + winterEndDate);

            System.out.println("springStartDate = " + springStartDate);

            System.out.println("springEndDate = " + springEndDate);

            System.out.println("summerStartDate = " + summerStartDate);

            System.out.println("summerEndDate = " + summerEndDate);
                 
        // check we can insert these values
            
            try {
                
                statement = DatabaseConnectionSingleton.getStatementInstance();
                
                //
                
                
                String sqlInsertYear = "INSERT INTO academic_year (academic_year) VALUES ("
                        + selectedDBYear
                        + ");";
                      
                Boolean successInsertYear = executeAndCheckInsert(sqlInsertYear, "year");
                
                if (successInsertYear == true)
                {
                    
                    // year success, move to winter

                    //continue
                    
                    String sqlInsertWinterTermDates = "INSERT INTO winter (academic_year, "
                        + "winter_term_start, winter_term_end) VALUES ("
                        + selectedDBYear
                        + ", '"
                        + winterStartDate
                        + "', '"
                        + winterEndDate
                        + "');";
                
                    Boolean successInsertWinterTermDates = executeAndCheckInsert(sqlInsertWinterTermDates, "winter");
                
                    if (successInsertWinterTermDates == true)
                    {
                        
                        // winter success, move to spring

                        //continue
                    
                        String sqlInsertSpringTermDates = "INSERT INTO spring (academic_year, "
                            + "spring_term_start, spring_term_end) VALUES ("
                            + selectedDBYear
                            + ", '"
                            + springStartDate
                            + "', '"
                            + springEndDate
                            + "');";

                        Boolean successInsertSpringTermDates = executeAndCheckInsert(sqlInsertSpringTermDates, "spring");

                        if (successInsertSpringTermDates == true)
                        {
                            //continue
                            // spring success, move to summer

                            //continue                    
                            String sqlInsertSummerTermDates = "INSERT INTO summer (academic_year, "
                                + "summer_term_start, summer_term_end) VALUES ("
                                + selectedDBYear
                                + ", '"
                                + summerStartDate
                                + "', '"
                                + summerEndDate
                                + "');";

                            Boolean successInsertSummerTermDates = executeAndCheckInsert(sqlInsertSummerTermDates, "summer");

                            if (successInsertSummerTermDates == true)
                            {
                                //overall success!
                                // summer success!
                                
                                //just in case
                                sendBack = false;
                                
                                // if not a success, connection will be closed
                                // must explicitly close if total success, therefore
                                DatabaseConnectionSingleton.closeConnection();
                                
                            }
                            else
                            {
                            }                            
                        }
                        else
                        {
                        }                        
                    }
                    else
                    {
                    }                        
                }
                else
                {
                }
            } catch (SQLException e)
            {
                removeYearFromDB();
                
                errorMessage = e.toString();
                
            }
            
            ///////////////////////////////////
            
            
            if (sendBack == false)
                {
                    
                //proceed to the next page!
                    
                    System.out.println("success! now moving onto next page away from check term dates page!");
                    
                    
                    String objectType = "Class";
                    
                    //int activeYear = selectedDBYear;
                    
                    //attributes to stroe regardless of decision tree
                    JSONObject infoToSend = new JSONObject();
                    infoToSend.put("object_type", objectType);
                    infoToSend.put("selected_db_year", selectedDBYear);
                    
                    
                    
                    // load first choice listto display
                    ArrayList<String> listToDisplay = findActiveClasses();
                    
                    // decision tree based on shwether there were any results
                    if (listToDisplay.size() > 0)                    
                    {
                        
                         // successful listToDisplay for active classes!
                        
                        // store everything in a JSONArray
                        
                        infoToSend.put("active_status", "currently active");
                     
                        System.out.println(infoToSend.toString());                        
                        
                        DisplayInfoBean displayInfoBean = new DisplayInfoBean(infoToSend, listToDisplay);

                        request.setAttribute("strategy", "new year");
                        
                        request.setAttribute("displayInfoBean", displayInfoBean);

                        //request.setAttribute("active_status", "currently active");

                        request.getRequestDispatcher("dbms/newYear/pickArray.jsp").forward(request, response);
                        
                    } else
                    {
                        //error finding any classes
                        
                        // move straight to finding all except active classes
                        
                        listToDisplay = findArchivedClassNames();
                        
                        if (listToDisplay.size() > 0)
                            {
                            // successful listToDisplay for NONactive classes!           
                    
                            // store everything in a JSONArray

                            infoToSend.put("active_status", "archived");

                            DisplayInfoBean displayInfoBean = new DisplayInfoBean(infoToSend, listToDisplay);

                            request.setAttribute("displayInfoBean", displayInfoBean);     
                            
                            request.getRequestDispatcher("dbms/newYear/pickArray.jsp").forward(request, response);
                            
                            }
                        else
                            {
                            //gotta send to new entity list
                            
                            // after new entity list page, will need active status to be currently active
                             infoToSend.put("active_status", "currently active");
                                                          
                            DisplayInfoBean displayInfoBean = new DisplayInfoBean(infoToSend);

                            request.setAttribute("displayInfoBean", displayInfoBean);   
                            
                            request.setAttribute("success_message", "No current or archived classes found. Please enter entirely new classes.");

                             request.setAttribute("strategy", "new year");
                             
                             request.setAttribute("totally_new", "yes");
                            
                            request.getRequestDispatcher("dbms/newYear/newEntity.jsp").forward(request, response);
                            
                            } //end of SECOND listdispaly is null logic
                                                
                    } // end of INITIAL listdisplay is null logic
                                   
            }
            else // sendBack == true
            {
                // go back to term dates
                    
                // attributes set for next page
            
                    JSONObject infoForNewYear = new JSONObject();

                    infoForNewYear.put("display_year", displayYear);
                    
                    if (errorMessage == null)
                    {
                        errorMessage = "Sorry. A problem has occurred. Please try again.";
                    }
                    
                    System.out.println("error message  = " + errorMessage);

                    DisplayInfoBean displayInfoBean = new DisplayInfoBean(infoForNewYear, errorMessage);

                    request.setAttribute("displayInfoBean", displayInfoBean);                

                // Move to next page

                    request.getRequestDispatcher("dbms/newYear/chooseTermDates.jsp").forward(request, response); //temp change should be displayArray
            }
                    
        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        }         
        
    }    
    
    public ArrayList<String> findActiveClasses()
    {
        
        ArrayList<String> resultArray = new ArrayList();
        
        try {
            
            String sql = "SELECT classroom.class_name AS result "
            + "FROM classroom, class_year, academic_year "
            + "WHERE classroom.class_id = class_year.class_id "
            + "AND class_year.academic_year = academic_year.academic_year "
            + "AND academic_year.active = TRUE;";
            
            statement = DatabaseConnectionSingleton.getStatementInstance();
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            DatabaseConnectionSingleton.closeConnection();
            
            while (resultSet.next())
            {
                resultArray.add(resultSet.getString("result"));
            }
            
        } catch (ServletException | SQLException ex) {
            Logger.getLogger(NewYearTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultArray;
        
    }
    // THIS METHOD IS USED AGAIN SOMEHWERE ELSE. MENTION IN DISSERTATION HOW I HAVEN'T
    // HAD ONE METHOD IN ONE PLACE THAT BOTH PAGES MUST ACCESS (DESIGN DECISION)
    public ArrayList<String> findArchivedClassNames()
    {
        
        ArrayList<String> resultList = new ArrayList<>();
        
        try {
            
            String sql = "SELECT classroom.class_name AS result "
                    + "FROM classroom "
                    + "EXCEPT SELECT classroom.class_name "
                    + "FROM classroom, class_year, academic_year "
                    + "WHERE classroom.class_id = class_year.class_id "
                    + "AND class_year.academic_year = academic_year.academic_year "
                    + "AND academic_year.active = TRUE;";
                 
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            DatabaseConnectionSingleton.closeConnection();
        
            while (resultSet.next())
            {
                resultList.add(resultSet.getString("result"));
            }
            
        } catch (SQLException | ServletException ex) {
            Logger.getLogger(NewYearTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return resultList;
    }

    public Boolean executeAndCheckInsert(String sql, String term)
    {
        
        int resultInt;
        
        System.out.println("sql to submit = " + sql);
        
        
        try {
            resultInt = statement.executeUpdate(sql);
        } catch (SQLException ex) {
            
            // this is where the key errors happen!
            
            // wipe data FIRST
            
            removeYearFromDB();
            
            // secondly, get error

                Logger.getLogger(NewYearTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex);

               String errorToProcess = ex.toString();
               
               System.out.println("ERROR DUDE TO LOOK AT: \n" + errorToProcess);
               
               //TODO
               
            // check if error is of a particular type, and update errorMessage accordingly
               
               
               if (
                       
                       // the error contains one of the preset SQL messages for overlapping term dates
                       
                       errorToProcess.contains("violates check constraint")
                       ||
                       errorToProcess.contains("cannot start before the end of")
                       ||
                       errorToProcess.contains("cannot end before the start of the next academic")
                       ||
                       errorToProcess.contains("term cannot end after the start of")
                  )
               {
               errorMessage = "Database changed denied. Error inserting dates for term '" + term + "'. "
                       + "Please check that the start date is after the end date of the previous term, "
                       + "and that the end date is before the start of the next term's start date. "
                       + "(TODO George: make sure that here is a link to see available years/term dates)" ;
               } 
               else if (errorToProcess.contains("duplicate key value violates unique constraint"))
               {
                   
                           errorMessage = "Warning: dates entered are not unique dates for the given academic year."
                                   + " Perhaps database has had a bit of a muddle up! "
                                   + "Try checking the list of established academic years for errors, "
                                   + "then restarting the whole process; or, contact your system administrator";
                           
               }
               else //default option
               {
                    errorMessage = "something unexpected went wrong whilst determining term dates. "
                            + "please check you entry input thoroughly and try again!";
               
               }
               

                return false;
            
        }

        if (resultInt == 0)
        {
            //error
            
            removeYearFromDB();
            
            return false;
            
        }
        else
        {
            //success
            
             return true;
             
        }   
                 
    }
    
    public void removeYearFromDB()
    {
        // if at any point you need to remove info, then we must return to term dates page
        sendBack = true;
        
        try {
                    // error with inserting due to database clash!
                    // reset all values!
                  
            statement = DatabaseConnectionSingleton.getStatementInstance();
  
            String resetSql =
                            "DELETE FROM winter WHERE winter.academic_year = " + selectedDBYear + "; "
                            + "DELETE FROM spring WHERE spring.academic_year = " + selectedDBYear + "; "
                            + "DELETE FROM summer WHERE summer.academic_year = " + selectedDBYear + "; "
                            + "DELETE FROM academic_year WHERE academic_year = " + selectedDBYear + ";"
                            ;
                    
            int deletedRows = statement.executeUpdate(resetSql);
                    
            System.out.println(deletedRows + " rows were deleted");

            DatabaseConnectionSingleton.closeConnection();
                    
        } catch (SQLException | ServletException ex) {
                    
            Logger.getLogger(NewYearTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex);
       
        }
        
        System.out.println("we've had to cancel insert term dates.");
                
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
            Logger.getLogger(NewYearTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(NewYearTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex);
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
