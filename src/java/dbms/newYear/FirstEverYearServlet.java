package dbms.newYear;

import java.io.IOException;
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

@WebServlet(name = "FirstEverYearServlet", 
        urlPatterns = {"/FirstEverYearServlet"})

public class FirstEverYearServlet extends HttpServlet 
{
              
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
        
        System.out.println("We've reached the FirstEverYearServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
                  
        try 
        { 
            
            // get variable
                        
                Object obj = request.getParameter("startYear");
                
                String selectedYearString = obj.toString();     
                
                int selectedYear = Integer.parseInt(selectedYearString);
                
                int yearForDB = selectedYear + 1;
                         
            //  proceed           

                    JSONObject infoForNewYear = new JSONObject();

                        String selectedYearDisplay = selectedYear + "-" + yearForDB;

                    infoForNewYear.put("display_year", selectedYearDisplay);

                    infoForNewYear.put("selected_db_year", yearForDB);

                DisplayInfoBean displayInfoBean = new DisplayInfoBean(infoForNewYear);
                
                request.setAttribute("displayInfoBean", displayInfoBean);                
                                
            // Move to next page

                request.getRequestDispatcher("dbms/newYear/chooseTermDates.jsp").forward(request, response); //temp change should be displayArray
  
        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
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
     * @throws org.json.JSONException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        try {
            processRequest(request, response);
        } catch (JSONException ex) {
            Logger.getLogger(FirstEverYearServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(FirstEverYearServlet.class.getName()).log(Level.SEVERE, null, ex);
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
