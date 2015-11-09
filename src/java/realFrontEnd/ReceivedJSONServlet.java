package realFrontEnd;

import database.DatabaseConnectionSingleton;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import json.JsonUnparser;
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

@WebServlet(name = "ReceivedJSONServlet", 
        urlPatterns = {"/ReceivedJSONServlet"})

public class ReceivedJSONServlet extends HttpServlet 
{
    private JSONObject realRequestMessage;
    private JSONObject realResponseMessage;
  
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
    protected void processRequest
        (HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, JSONException, SQLException 
    {
        
        System.out.println("We've reached the REAL servlet!");
       
        response.setContentType("text/html;charset=UTF-8");                   
              
        // extract JSON
            
        String jsonString = request.getParameter("json_message");
           
        realRequestMessage = new JSONObject(jsonString);
                 
        // send off JSONObject to be interpreted

        realResponseMessage = 
                JsonUnparser.unparseandReturnJson(realRequestMessage);
        
        // set attributes for response
                
        String realResponseMessageString = realResponseMessage.toString();
        
        // try (with resources) to print response

        try (PrintWriter response_writer = response.getWriter()) 
        {
            response_writer.println(realResponseMessageString);
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
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException | SQLException ex) {
            Logger.getLogger(ReceivedJSONServlet.class.getName()).log(Level.SEVERE, null, ex);
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
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (JSONException | SQLException ex) {
            Logger.getLogger(ReceivedJSONServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //TODO
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
