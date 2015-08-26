package forFakeFrontEnd;


import database.DatabaseConnectionSingleton;
import java.io.IOException;
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

@WebServlet(name = "RequestRecordServlet", 
        urlPatterns = {"/RequestRecordServlet"})

public class RequestRecordServlet extends HttpServlet 
{
    
    
    public Boolean firstTime = true;
    
    private JSONObject fakeUploadResponseMessage;
   
    private Boolean edited = false;

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
        
        
        
        System.out.println("We've reached the request records server!");
       
        response.setContentType("text/html;charset=UTF-8");
                                  
        // Using try {...} catch {...} for error control
        try 
        {           
        
            // store webpage details
            
            String externChildName = "";
            String externYear = "";
            String externTerm = "";
                            
            
                
            externChildName = request.getParameter("child_name");
            externYear = request.getParameter("academic_year");
            externTerm = request.getParameter("selectbox");
                
               
            
                
            // check webpage details entered are valid
                
                String[] checkInputData = {externChildName, externYear, externTerm};
                
                for (int i = 0; i < 2; i++)
                {
                    //to do
                    System.out.println(checkInputData[i]);
                    
                    if // nothing has been entered or "any" has been incorrectly entered 
                            //(will be checked thoroughly in iOS app front-end anyway)
                    (
                            "".equals(checkInputData[i])
                            || //or
                            "Any".equals(checkInputData[i])
                            ||
                            "ANY".equals(checkInputData[i])
                            
                    )
                    { // then set to default "Any"
                     
                        System.out.println("changed " + checkInputData[i] + " to 'any'");                        
                    
                        checkInputData[i] = "any";

                        edited = true;
                        
                    }        
                    
                }
                
                String editedChildName = checkInputData[0];

                String editedYear = checkInputData[1];
                String editedTerm = checkInputData[2];
                
                System.out.println("edited form input? " + edited);
                
            // create a JSONObject to send off to the rest of the 'real' programme
                    
                JSONObject fakeUploadRecord = new JSONObject();
                    
                fakeUploadRecord.put("instruction_tag", "request_record");
                fakeUploadRecord.put("student_name", editedChildName);
                fakeUploadRecord.put("academic_year", editedYear);
                fakeUploadRecord.put("term", editedTerm);
                   
                System.out.println("fake record to request is: \n" + fakeUploadRecord);
            
            // send off to real back-end for processing
                
                fakeUploadResponseMessage = JsonUnparser.unparseandReturnJson(fakeUploadRecord);
                
                System.out.println("response message: \n" + fakeUploadResponseMessage);
            
                
            // set attributes for next page
                
                request.setAttribute("returnJsonToDisplayString", fakeUploadResponseMessage.toString());
                request.setAttribute("returnJsonToDisplay", fakeUploadResponseMessage);
                     
            // Move to next page

                request.getRequestDispatcher("fakeFrontEnd/afterUpload.jsp").forward(request, response);
            
        }
        catch(ServletException e) {
           
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            request.setAttribute("error_message", e.toString());
            
            // Send it on to a different View
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
            Logger.getLogger(RequestRecordServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RequestRecordServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(RequestRecordServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(RequestRecordServlet.class.getName()).log(Level.SEVERE, null, ex);
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
