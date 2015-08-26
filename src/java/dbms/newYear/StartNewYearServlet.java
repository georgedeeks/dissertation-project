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

@WebServlet(name = "StartNewYearServlet", 
        urlPatterns = {"/StartNewYearServlet"})

public class StartNewYearServlet extends HttpServlet 
{
    
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
        
        System.out.println("We've reached the StartNewYearServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
                  
        try 
        { 
            
        // see whether to send to 'new year entirely' page or enter a year name
                
            // open db
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            String sql = "select "
                    + "exists (SELECT academic_year AS result FROM academic_year WHERE active = TRUE), "
                    + "academic_year, active FROM academic_year ORDER BY academic_year DESC;"; 
            
            System.out.println("sql to send to get yearlist = " + sql);
            
            ResultSet resultSet = statement.executeQuery(sql);
                        
            // close the db connection    
            DatabaseConnectionSingleton.closeConnection();
            
            System.out.println("collecting results");
            
        // now collect the results          
            
            ArrayList<Integer> yearList = new ArrayList();
            int activeYear = -1;
            
            Boolean exists = false;
            
            while (resultSet.next())                
            {
                  
                //stays the same
                 exists = resultSet.getBoolean("exists"); 
                   
                int yearInt = resultSet.getInt("academic_year");
                        
                yearList.add(yearInt);
      
                Boolean isActive = resultSet.getBoolean("active");
     
                if (isActive == true)
                       
                {   
                    activeYear = yearInt;
                }                
                
            }
            
            
            if (exists == true)
            {
                
                int newestYear = yearList.get(0);            

            // attributes set for next page
                System.out.println("year list to send to choose new year page = " + yearList);
                DisplayInfoBean displayInfoBean = new DisplayInfoBean(yearList, activeYear, newestYear);

                request.setAttribute("displayInfoBean", displayInfoBean);

            // Move to next page

                request.getRequestDispatcher("dbms/newYear/chooseNewYear.jsp").forward(request, response); //temp change should be displayArray
               
            }
            else
            {
                request.getRequestDispatcher("dbms/newYear/chooseFirstEverYear.jsp").forward(request, response);
            }
         
            
        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        } 
        catch (SQLException ex) {
            
            Logger.getLogger(StartNewYearServlet.class.getName()).log(Level.SEVERE, null, ex);
            
            request.setAttribute("error_message", "Problem interacting with database. "
                    + "Please try again or contact your system administrator. "
                    + "(You may have to wipe entire contents of database.)");

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
            Logger.getLogger(StartNewYearServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StartNewYearServlet.class.getName()).log(Level.SEVERE, null, ex);
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
