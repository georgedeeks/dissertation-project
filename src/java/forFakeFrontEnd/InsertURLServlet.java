package forFakeFrontEnd;


import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

/*import org.json.JSONObject;*/

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "InsertURLServlet", 
        urlPatterns = {"/InsertURLServlet"})

public class InsertURLServlet extends HttpServlet 
{
    
    private String errorMessage;    
    private JSONObject fakeUploadResponseMessage;
   

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
            throws ServletException, IOException, JSONException, SQLException 
    {
        
        System.out.println("We've reached the InsertURLServlet!");
       
        response.setContentType("text/html;charset=UTF-8");
                                  
        // Using try {...} catch {...} for error control
        try{         
            
            fakeUploadResponseMessage = new JSONObject();
        
        // store webpage details
            
            String externFileName = request.getParameter("file_name");
            String externUrl = request.getParameter("url");
                                       
        // check webpage details entered are valid
                
            if 
            ( // any of these fields are blank   
                    "".equals(externFileName) 
                    || //or
                    "".equals(externUrl)                    
            )
            { // then there's been an error
                    
                errorMessage = "Web form failure! Not enough info provided!";
                    
                    //to do 
                        // add info about error and then dynamically display this in front-end web page
                            // this is one of the last things you need to do
                
                fakeUploadResponseMessage.put("error_message", errorMessage);
                    
                        
                    
            } 
            else
            {
                   
                System.out.println("Form input via web user success!");

                // (optional) check if 'observvations' are empry 
                    
                               
            // create a JSONObject to send off to the rest of the 'real' programme
                
                JSONObject fakeUploadRecord = new JSONObject();
                    
                fakeUploadRecord.put("instruction_tag", "insert_URL");
                fakeUploadRecord.put("file_name", externFileName); //e.g. 5489756-MSC0000040302.mpeg
                fakeUploadRecord.put("URL", externUrl);
                                    
               // System.out.println("fake record to upload is: \n" + fakeUploadRecord);
                   
            // send off JSONObject to be interpreted

                fakeUploadResponseMessage = JsonUnparser.unparseandReturnJson(fakeUploadRecord);

                System.out.println("response message: \n" + fakeUploadResponseMessage);
            
            }         
        
        request.setAttribute("returnJsonToDisplay", fakeUploadResponseMessage);
        
        request.getRequestDispatcher("fakeFrontEnd/afterUpload.jsp").forward(request, response);
        
        }
        catch(ServletException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
                                    
            request.setAttribute("error_message", e.toString());
            
            // Send it on to a different View
            request.getRequestDispatcher("fakeFrontEnd/launchFakeFrontEnd").forward(request, response);
            
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
            Logger.getLogger(InsertURLServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(InsertURLServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(InsertURLServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(InsertURLServlet.class.getName()).log(Level.SEVERE, null, ex);
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
