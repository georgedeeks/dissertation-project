package dbms;

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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "StartSiteServlet", 
        urlPatterns = {"/StartSiteServlet"})

public class StartSiteServlet extends HttpServlet 
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
        
        System.out.println("We've reached the StartSiteServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
          
        try 
        { 
            
        // find years, including current/active
                     
            String sql = "SELECT EXISTS (SELECT * FROM academic_year);";
            
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            ResultSet resultSet = statement.executeQuery(sql);
            
            resultSet.next();
            
            Boolean aYearExists = resultSet.getBoolean("exists");
            
            DatabaseConnectionSingleton.closeConnection();
            
            if (aYearExists == true)
            {
                request.getRequestDispatcher("dbms/launchSite/startMainMenu.jsp").forward(request, response); //temp change should be displayArray
            }
            else
            {
                request.getRequestDispatcher("dbms/launchSite/startOnlyOption.jsp").forward(request, response);
            }
            
            

                    
        }
        catch(ServletException e) {
            
            try {
                DatabaseConnectionSingleton.closeConnection();
            } catch (SQLException ex) {
                Logger.getLogger(StartSiteServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        } 
        catch (SQLException ex) {
            
            try {
                DatabaseConnectionSingleton.closeConnection();
            } catch (SQLException ex1) {
                Logger.getLogger(StartSiteServlet.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
            Logger.getLogger(StartSiteServlet.class.getName()).log(Level.SEVERE, null, ex);

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
            Logger.getLogger(StartSiteServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(StartSiteServlet.class.getName()).log(Level.SEVERE, null, ex);
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
