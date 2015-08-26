package forFakeFrontEnd;

import database.DatabaseConnectionSingleton;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static json.JsonUnparser.unparseandReturnJson;
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

@WebServlet(name = "CheckForUpdatesServlet", 
        urlPatterns = {"/CheckForUpdatesServlet"})

public class CheckForUpdatesServlet extends HttpServlet 
{
    
    
    public Boolean firstTime = true;
    
    private JSONObject fakeUploadResponseMessage;
   

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     * 
     * 
        // accessing this servlet means that we want to check for updates, via a JSON
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException, SQLException 
    {
        
        System.out.println("We've reached the checkforupdates server!");
       
        response.setContentType("text/html;charset=UTF-8");
         
        try{
        // build JSON to send for unparsing and processing
        
            JSONObject objCheckUpdates = new JSONObject();     
            
            
            
            if (request.getParameter("launching") != null)
            {
                //launching
                
                objCheckUpdates.put("instruction_tag", "launching"); 
                
            }
            else
            {
                //check
                
                objCheckUpdates.put("instruction_tag", "check_updates"); 

                Calendar today = Calendar.getInstance();
       
                Date todayDate = today.getTime();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
                String formattedDate = formatter.format(todayDate);
                
                String timestamp = formattedDate;
                    // obviously for back-end testing only
                    // returns a 2005 timestamp if error (i.e., must update!)

                System.out.println("test last updated to check against real last updated = " + timestamp);
                
                objCheckUpdates.put("timestamp", timestamp);                    

            }
            
            System.out.println("This is the JSON to unparse from checkforupdates website: " + objCheckUpdates);
            
            JSONObject returnToDoaaTemp = unparseandReturnJson(objCheckUpdates);
            
        // process return message
            
            System.out.println("returning this JSON to Doaa: " + returnToDoaaTemp);
            
            System.out.println("brown cow");
                
        // set attributes for next page
                
            request.setAttribute("returnJsonToDisplay", returnToDoaaTemp);
                     
        // Move to next page
           
            request.getRequestDispatcher("fakeFrontEnd/afterUpload.jsp").forward(request, response);

       
            
        }
        catch(ServletException e) {
  
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            request.setAttribute("error_message", e.toString());
                                   
            // Send it on to a different, error View
            request.getRequestDispatcher("fakeFrontEnd/launchFakeFrontEnd.jsp").forward(request, response);
    
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
            Logger.getLogger(CheckForUpdatesServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CheckForUpdatesServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CheckForUpdatesServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CheckForUpdatesServlet.class.getName()).log(Level.SEVERE, null, ex);
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
