package dbms.activeYear;

import database.DatabaseConnectionSingleton;
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

@WebServlet(name = "StartEditActiveTermDatesServlet", 
        urlPatterns = {"/StartEditActiveTermDatesServlet"})

public class StartEditActiveTermDatesServlet extends HttpServlet 
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
        
        System.out.println("We've reached the StartEditActiveTermDatesServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
                
        try 
        { 
            System.out.println("john has cried upon entering Sebastain");
            
        // get active term dates
                    
            String sql = "SELECT winter.winter_term_start, winter.winter_term_end, "
                    + "spring.spring_term_start, spring.spring_term_end, "
                    + "summer.summer_term_start, summer.summer_term_end, "
                    + "academic_year.academic_year "
                    + "FROM winter, spring, summer, academic_year "
                    + "WHERE winter.academic_year = academic_year.academic_year "
                    + "AND spring.academic_year = academic_year.academic_year "
                    + "AND summer.academic_year = academic_year.academic_year "
                    + "AND academic_year.active = TRUE;";
            
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            resultSet.next();
            
            String winterStartUnformatted = resultSet.getString("winter_term_start");
            String winterEndUnformatted = resultSet.getString("winter_term_end");
            String springStartUnformatted = resultSet.getString("spring_term_start");
            String springEndUnformatted = resultSet.getString("spring_term_end");
            String summerStartUnformatted = resultSet.getString("summer_term_start");
            String summerEndUnformatted = resultSet.getString("summer_term_end");
            
            int activeYear = resultSet.getInt("academic_year");
            String activeYearString = Integer.toString(activeYear);
            
            System.out.println("we have interacted with db");
            
            if // ANY RESULTS ARE NULL
                    (
                        winterStartUnformatted == null 
                        || 
                        winterEndUnformatted == null 
                        ||
                        springStartUnformatted == null 
                        ||
                        springEndUnformatted == null 
                        ||
                        summerStartUnformatted == null 
                        ||
                        summerEndUnformatted == null 
 
                    )
            {
                // then send to error page
                
                System.out.println("attribute is null");

                request.setAttribute("error_message", "Not all term dates are valid."
                        + " Please check there is an active year present in the database, or"
                        + " contact your system administrator.");

                // Send it on to a different View
                request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
                
            }
            else
            {
                
                System.out.println("no attributes are null, loading variables");
                
                //send to edit term dates page
                
                request.setAttribute("activeYearString", activeYearString);
                            
                System.out.println("activeYearString = " + activeYearString);
                activeYear = Integer.parseInt(activeYearString);
                System.out.println("activeYear = Integer.parseInt(activeYearString); = " + "activeYear = " + activeYear);
                
                String winterStart = convertDate(winterStartUnformatted);
                String winterEnd = convertDate(winterEndUnformatted);
                String springStart = convertDate(springStartUnformatted);
                String springEnd = convertDate(springEndUnformatted);
                String summerStart = convertDate(summerStartUnformatted);
                String summerEnd = convertDate(summerEndUnformatted);
                
                request.setAttribute("winterStart", winterStart);
                request.setAttribute("winterEnd", winterEnd);
                request.setAttribute("springStart", springStart);
                request.setAttribute("springEnd", springEnd);
                request.setAttribute("summerStart", summerStart);
                request.setAttribute("summerEnd", summerEnd);
                
                request.getRequestDispatcher("dbms/activeYear/editTermDates.jsp").forward(request, response);
                
            }
        }
        catch(ServletException | SQLException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            errorMessage = e.toString();
            
            request.setAttribute("error_message", errorMessage);
                        
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        }         
        
    }    
    
    public String convertDate(String oldDate)
    {
        String[] parts = oldDate.split("-");
        
        String newDate = parts[2] + "-" + parts[1] + "-" +  parts[0] ;
        
        return newDate;
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
            Logger.getLogger(StartEditActiveTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StartEditActiveTermDatesServlet.class.getName()).log(Level.SEVERE, null, ex);
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
