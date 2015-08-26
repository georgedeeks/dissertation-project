package dbms.activeYear;

import database.DatabaseConnectionSingleton;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet; 
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "WipeAllDataServlet", 
        urlPatterns = {"/WipeAllDataServlet"})

public class WipeAllDataServlet extends HttpServlet 
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
            throws ServletException, IOException, SQLException             
    {
        
        System.out.println("We've reached the WipeAllDataServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
                  
        try 
        { 
            
            String sqlToReset = "TRUNCATE summer cascade; "
                    + "TRUNCATE spring cascade; "
                    + "TRUNCATE winter cascade; "
                    + "TRUNCATE evidence cascade; "
                    + "TRUNCATE evidence_data cascade; "
                    + "TRUNCATE support_plan_level cascade; "
                    + "TRUNCATE support_plan_target cascade; "
                    + "TRUNCATE student_in_class_year cascade; "
                    + "TRUNCATE student cascade; "
                    + "TRUNCATE class_year cascade; "
                    + "TRUNCATE academic_year cascade; "
                    + "TRUNCATE classroom cascade; "
                    ;
            
            System.out.println("sqlToReset = " + sqlToReset);
            
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            statement.executeUpdate(sqlToReset);
            
            String sqlUpdateLastUpdated = "UPDATE last_updated SET date_last_updated = now() WHERE only_id = 1;";
            
            statement.executeUpdate(sqlUpdateLastUpdated);
            
            ResultSet resultSet = statement.executeQuery("SELECT date_last_updated AS result FROM last_updated WHERE only_id = 1;");
            
            String timestampToString = "not initialised";
            
            while (resultSet.next())
            {            
                
                java.sql.Timestamp ts = resultSet.getTimestamp("result");
                
                timestampToString = ts.toString();
                System.out.println(timestampToString);
                
                
            }
            
            DatabaseConnectionSingleton.closeConnection();
            
            request.setAttribute("update_result", "All information has been reset at " + timestampToString + ".");
            
            request.getRequestDispatcher("dbms/updateResult.jsp").forward(request, response);
            
            
        } catch (SQLException e)
        {
            
            System.out.println(e);
            
            request.setAttribute("error_message", "Unable to delete all information. "
                    + "Please try again or contact your system administrator.");
            
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        }
        catch (ServletException e)
        {
            request.setAttribute("error_message", "Server error.");
            
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
        } catch (SQLException ex) {
            Logger.getLogger(WipeAllDataServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (SQLException ex) {
            Logger.getLogger(WipeAllDataServlet.class.getName()).log(Level.SEVERE, null, ex);
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
