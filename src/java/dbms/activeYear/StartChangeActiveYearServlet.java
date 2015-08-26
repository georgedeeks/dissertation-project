package dbms.activeYear;

import dbms.DisplayInfoBean;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "StartChangeActiveYearServlet", 
        urlPatterns = {"/StartChangeActiveYearServlet"})

public class StartChangeActiveYearServlet extends HttpServlet 
{
    
    
    private ArrayList<String> yearList;

    
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
        
          
        try 
        { 
            
        // find years, including current/active
                     
            String sql = "SELECT academic_year, active FROM academic_year ORDER BY academic_year ASC;"; //ORDER ASC 
                // asc order ensures that if multiple actives accdiently turned on, then most recent year will
                // be considered active year
            
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
            
            
                // put results in a bean
                    DisplayInfoBean displayInfoBean = new DisplayInfoBean(yearList, activeYear);

                // load bean into next apge   
                    request.setAttribute("displayInfoBean", displayInfoBean);
                    
                // Move to next page
                    request.getRequestDispatcher("dbms/activeYear/changeActiveYear.jsp").forward(request, response); //temp change should be displayArray

                    
        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        } 
        catch (SQLException ex) {
            
            Logger.getLogger(StartChangeActiveYearServlet.class.getName()).log(Level.SEVERE, null, ex);

            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);            
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
            Logger.getLogger(StartChangeActiveYearServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StartChangeActiveYearServlet.class.getName()).log(Level.SEVERE, null, ex);
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
