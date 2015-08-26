package dbms.activeYear;

import database.DatabaseConnectionSingleton;
import java.io.IOException;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "EditActiveTermDatesServlet", 
        urlPatterns = {"/EditActiveTermDatesServlet"})

public class EditActiveTermDatesServlet extends HttpServlet
{
    
    private int dbYear;
    private Statement statement;
    private Boolean sendBack;
    private String errorMessage;
    private String successMessage;
    private String sql;
    
    private String dbWinterStart;
    private String dbWinterEnd;
    private String dbSpringStart;
    private String dbSpringEnd;
    private String dbSummerStart;
    private String dbSummerEnd;
    
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws org.json.JSONException
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException, SQLException  
    {
        
        System.out.println("We've reached the EditActiveTermDatesServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
        successMessage = new String();
        errorMessage = new String();
        
        try 
        {
            
            String dbYearString = (String) request.getParameter("activeYearString");
            
            System.out.println("dbyearstirng = " + dbYearString);
            
            dbYear = Integer.parseInt(dbYearString);
            
        // get previous dates
            
              dbWinterStart = request.getParameter("winterStart");
              dbWinterEnd = request.getParameter("winterEnd");
              dbSpringStart = request.getParameter("springStart");
              dbSpringEnd = request.getParameter("springEnd");
              dbSummerStart = request.getParameter("summerStart");
              dbSummerEnd = request.getParameter("summerEnd");
            
        // second thing is to see if datepickers have been used at all
            
            String winterStartDateUnformattedForDB = request.getParameter("datepicker0");
            String winterEndDateUnformattedForDB = request.getParameter("datepicker1");
            String springStartDateUnformattedForDB = request.getParameter("datepicker2");
            String springEndDateUnformattedForDB = request.getParameter("datepicker3");
            String summerStartDateUnformattedForDB = request.getParameter("datepicker4");
            String summerEndDateUnformattedForDB = request.getParameter("datepicker5");
                        
            if 
                (
                
                    winterStartDateUnformattedForDB.equals("")
                    &&                   
                    winterEndDateUnformattedForDB.equals("")
                    &&
                    springStartDateUnformattedForDB.equals("")
                    &&
                    springEndDateUnformattedForDB.equals("")
                    &&
                    summerStartDateUnformattedForDB.equals("")
                    &&
                    summerEndDateUnformattedForDB.equals("")                 
                    
                )
            {
                
                request.setAttribute("update_result", "No new values for term dates were added, as none were entered.");
                
                request.getRequestDispatcher("dbms/updateResult.jsp").forward(request, response);
                
            }
            else 
            {
                
            // try to enter each date that is not null into DB
                
                ArrayList<String> namesOfDates = new ArrayList();
                                
                ArrayList<String> datesToAdd = new ArrayList();
                
                datesToAdd.add(winterStartDateUnformattedForDB);
                datesToAdd.add(winterEndDateUnformattedForDB);
                datesToAdd.add(springStartDateUnformattedForDB);
                datesToAdd.add(springEndDateUnformattedForDB);
                datesToAdd.add(summerStartDateUnformattedForDB);
                datesToAdd.add(summerEndDateUnformattedForDB);
                
                ArrayList<String> termList = new ArrayList();
                
                termList.add("winter");
                termList.add("winter");
                termList.add("spring");
                termList.add("spring");
                termList.add("summer");
                termList.add("summer");
                
                ArrayList<Boolean> isStartlist = new ArrayList();
                
                isStartlist.add(true);
                isStartlist.add(false);
                isStartlist.add(true);
                isStartlist.add(false);
                isStartlist.add(true);
                isStartlist.add(false);
                
                sendBack = false;
                
                statement = DatabaseConnectionSingleton.getStatementInstance();
                
                for (int i = 0; i < datesToAdd.size(); i++)
                {
                    
                    String dateToUpdate = datesToAdd.get(i);
                    
                    String term = termList.get(i);
                    
                    Boolean isStartDate = isStartlist.get(i);
                    
                    System.out.println(
                            "to process: \n"
                            + "date to update = " + dateToUpdate + "\n" 
                            + "term = " + term + "\n"
                            + "isStartDate = " + isStartDate
                    );
                    
                    if (dateToUpdate.equals(""))
                    {
                        
                        System.out.println("not updating for int " + i + " as null");
                        
                    }
                    else
                    {
                        if (sendBack == false)
                        {
                            //try to edit element i into db
                        // if there is an error, sendBack == true and errorMessage isupdated
                        // if succcessful, then successmessage is updated!
                        tryToChangeDate(dateToUpdate, term, isStartDate, i);
                        }
                        else
                        {
                            System.out.println("not updating for int " + i + " as sendbakc is true") ;
                        }
                    }
                    // else do nothing
                }
                
                DatabaseConnectionSingleton.closeConnection();
                
                // tried to add all non-null dates to db; stopped if any issue
                
               // if issue, error message
                // if all okay, send to update_result
                
                if (sendBack == true)
                {
                    
                    request.setAttribute("success_message", successMessage);
                    
                    //load error message
                    
                    request.setAttribute("error_message", errorMessage);
                    
                    // load previous ORIGINAL unchanged values AGAIN
                             
                    String activeYearStringReceived = (String) request.getParameter("activeYearString");
                    
                    System.out.println("received active year: " + activeYearStringReceived);
                    
                    request.setAttribute("activeYearString", activeYearStringReceived); 
                    
                    request.setAttribute("winterStart", dbWinterStart);
                    request.setAttribute("winterEnd", dbWinterEnd);
                    request.setAttribute("springStart", dbSpringStart);
                    request.setAttribute("springEnd", dbSpringEnd);
                    request.setAttribute("summerStart", dbSummerStart);
                    request.setAttribute("summerEnd", dbSummerEnd);
                       
                    //move back
                    
                    request.getRequestDispatcher("dbms/activeYear/editTermDates.jsp").forward(request, response);
                            
                }
                else
                {
                    //load update result
                    request.setAttribute("update_result", successMessage);
                    
                    request.getRequestDispatcher("dbms/updateResult.jsp").forward(request, response);
                    
                }
               
            
           }   // end of if/else for if blank dates
            
        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        } catch (SQLException ex) {
            
            try {
                DatabaseConnectionSingleton.closeConnection();
            } catch (SQLException ex1) {
                Logger.getLogger(EditActiveTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
            Logger.getLogger(EditActiveTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex);
        }         
        
    
    }
    
    public Boolean tryToChangeDate
        (String unformattedForDBDate, String theTerm, Boolean istheStartDate, int iterationI) 
                throws SQLException
    {
         
        String formattedDateToEnter = formatforDB(unformattedForDBDate);
        
        if (formattedDateToEnter.equals("error"))
        {
            sendBack = true;
                
            errorMessage = "Problem inserting " + unformattedForDBDate 
                        + "into database "
                        + "due to the fact it is not a valid date format.";
                        
            return false;
            
        }
        else
        {
            


            String startOrEnd;

            if (istheStartDate == true)
            {
                startOrEnd = "start";
            }
            else
            {
                startOrEnd = "end";
            }

            String columnName = theTerm + "_term_" + startOrEnd; 

            sql = "UPDATE " + theTerm + " SET " + columnName + " = '" + formattedDateToEnter + "' WHERE academic_year = " + dbYear + ";";

            int resultInt = -1;

            try {

                resultInt = statement.executeUpdate(sql);

                if (resultInt > 0)
                {

                //success message

                    if (successMessage.equals(""))
                    {
                        successMessage = "Date '" + unformattedForDBDate + "' successfully inserted.";
                    }
                    else
                    {
                        successMessage += " Date '" + unformattedForDBDate + "' successfully inserted.";
                    }

                    System.out.println(successMessage);

                // change the date that displays for dfault

                    // unformat the date from db to user-friendly

                       String userFriendlyDate = formatforDB(formattedDateToEnter);

                    if (iterationI == 0)
                    {
                        dbWinterStart = userFriendlyDate;
                    }
                    else if (iterationI == 1)
                    {
                        dbWinterEnd = userFriendlyDate;
                    }
                    else if (iterationI == 2)
                    {
                        dbSpringStart = userFriendlyDate;
                    }
                    else if (iterationI == 3)
                    {
                        dbSpringEnd = userFriendlyDate;
                    }
                    else if (iterationI == 4)
                    {
                        dbSummerStart = userFriendlyDate;
                    }
                    else //if (iterationI == 5)
                    {
                        dbSummerEnd = userFriendlyDate;
                    } 

                // return true

                    return true;

                }
                else
                {
                    return false;
                    // dont' necessarily send back as not necessarily error
                }

            } 
            catch (SQLException ex) {

                Logger.getLogger(EditActiveTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex);

                sendBack = true;
                
                if (ex.toString().contains("violates check constraint"))
                {
                    errorMessage = "Problem inserting " + unformattedForDBDate 
                            + "into database "
                            + "due to violating a check constraint. "
                            + "Please check start and end of the next and previous (respectively) term dates, "
                            + "or contact your system administrator.";
                }
                
                else if (ex.toString().contains("cannot start"))
                {
        
                    errorMessage = "Problem inserting " + unformattedForDBDate 
                            + "into database "
                            + "due to violating a check constraint. "
                            + "ERROR: new term date selected starts before the end of "
                            + "the previous term date's end.";
                }
                
                else if (ex.toString().contains("cannot end"))
                {
        
                    errorMessage = "Problem inserting " + unformattedForDBDate 
                            + "into database "
                            + "due to violating a check constraint. "
                            + "ERROR: new term date selected starts after the start of "
                            + "the next term date's beginning.";
                }
                
                else
                {
                    errorMessage = "Problem inserting date '" + unformattedForDBDate + "'. "
                            + "Please try again, or contact your system administrator.";
                }

                return false;
            }    
        
        }
    
    }
    
    public String formatforDB(String oldDate)
    {
        try
        {
            
        
        System.out.println("olddate = " + oldDate);
        
        String[] parts = oldDate.split("-");
                
        
        System.out.println("parts = " + parts + " and partssize = " + parts.length);
        
        // dd-mm-yyyy -> yyyy-mm-dd
        
        String newDate = parts[2] + "-" + parts[1] + "-" + parts[0];
        
        return newDate;
        
        }
        catch
        (Exception e)
        {return "error";}
        
    }
       
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException | SQLException ex) {
            Logger.getLogger(EditActiveTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (JSONException | SQLException ex) {
            Logger.getLogger(EditActiveTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex);
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
