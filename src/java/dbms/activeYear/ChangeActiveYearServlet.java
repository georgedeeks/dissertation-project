package dbms.activeYear;

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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor...
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "ChangeActiveYearServlet", 
        urlPatterns = {"/ChangeActiveYearServlet"})

public class ChangeActiveYearServlet extends HttpServlet 
{
    
    private String oldListSizeString;
    private int oldListSize;    
    private ArrayList<String> oldList;
    private String selectedValueString;

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
            throws ServletException, IOException 
    {
        
        System.out.println("We've reached the diplaylist server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
        // Using try {...} catch {...} for error control
        try 
        { 
            
       
        // get info from webpage
            
            oldListSizeString = request.getParameter("listsize");
            oldListSize = Integer.parseInt(oldListSizeString);
            
        // find out what selected value was
            
            // System.out.println("john");
          
            oldList = new ArrayList<String>();
            
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
            
            //System.out.println("selectedValueString = " + selectedValueString);
          
        // fire off SQL to database to change active year
            
            String sql = "SELECT change_active_year("
                    + selectedValueString
                    + ");";
            
            Statement statement = DatabaseConnectionSingleton.getStatementInstance();
            
            System.out.println("sql to send = " + sql);
            
            ResultSet resultSet = statement.executeQuery(sql);
                                                
            DatabaseConnectionSingleton.closeConnection();
            
        // check results
            
            if (resultSet.getBoolean("change_active_year") == false)
            {
                //then no result has occurred
                // so send to page with error messages loaded
                                
                request.setAttribute("update_result", "Updating records failed");
                        
                request.getRequestDispatcher("dbms/updateResult.jsp").forward(request, response);
                
            }
            else
            {
                // everything is okay, so send to result page with success message
                
                //TODO
                    // could change it so that this now goes back to changeactiveyear but with updated information?
                
                request.setAttribute("update_result", "Updating records success!");
                
            }
               
        // now send off
            
            request.getRequestDispatcher("dbms/updateResult.jsp").forward(request, response);
            
        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            request.setAttribute("error_message", e); 
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        } catch (SQLException ex) {
            
            Logger.getLogger(ChangeActiveYearServlet.class.getName()).log(Level.SEVERE, null, ex);
            
            request.setAttribute("update_result", "Updating records failed");
        
            request.setAttribute("error_message", ex); 
            
            request.getRequestDispatcher("dbms/updateResult.jsp").forward(request, response);
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

            processRequest(request, response);

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

            processRequest(request, response);
  
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
