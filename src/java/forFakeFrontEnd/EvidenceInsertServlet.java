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

@WebServlet(name = "EvidenceInsertServlet", 
        urlPatterns = {"/EvidenceInsertServlet"})

public class EvidenceInsertServlet extends HttpServlet 
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
     * @throws org.json.JSONException
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, JSONException, SQLException 
    {
        
        System.out.println("We've reached the evidenc insert server!");
       
        response.setContentType("text/html;charset=UTF-8");
                                  
        // Using try {...} catch {...} for error control
        try 
        {           
            
            fakeUploadResponseMessage = new JSONObject();
        
        // store webpage details
            
            String externFileName = request.getParameter("file_name");
            String externChildName = request.getParameter("child_name");
            String externSPT = request.getParameter("support_plan_target");
            String externTarget_Level = request.getParameter("target_level");
            String externAdditionalObservations = request.getParameter("additional_observations");
                       
        // including current date
            SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd" );
            String todayDate = formatter.format( new java.util.Date() );
                //System.out.println("todayDate = " + todayDate);
                
        // check webpage details entered are valid
                
            if 
            ( // any of these fields are blank   
                    "".equals(externChildName) 
                    || //or
                    "".equals(externSPT)
                    ||
                    "".equals(externTarget_Level)
                    ||
                    "".equals(externFileName)
                    ||                        
                    todayDate == null
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
                    
                String externAdditionalObservationsTemp = "[no observation attached]";
                    
                if ("".equals(externAdditionalObservations))
                {
                    //System.out.println("extern is empty");
                } 
                else
                {                        
            
                    //System.out.println("extern ain't empty");
                    
                    externAdditionalObservationsTemp = externAdditionalObservations;
                
                }
                    
                System.out.println("Observations = " + externAdditionalObservationsTemp);
                
            // see checkbox for final external paramtere
                
                int isImageInt;
                
                if (request.getParameter("checkbox") != null)
                {
                    isImageInt = 1;
                }
                else
                {
                    isImageInt = 0;
                }
                
            // create a JSONObject to send off to the rest of the 'real' programme
                
                JSONObject fakeUploadRecord = new JSONObject();
                    
                fakeUploadRecord.put("instruction_tag", "upload_record");
                fakeUploadRecord.put("file_name", externFileName); //e.g. 5489756-MSC0000040302.mpeg
                fakeUploadRecord.put("student_name", externChildName);
                fakeUploadRecord.put("target", externSPT);
                fakeUploadRecord.put("level", externTarget_Level);
                fakeUploadRecord.put("observation", externAdditionalObservations);
                fakeUploadRecord.put("date_stamp", todayDate);
                fakeUploadRecord.put("is_image", isImageInt);
                    
                System.out.println("fake record to upload is: \n" + fakeUploadRecord);
                   
            // send off JSONObject to be interpreted

                fakeUploadResponseMessage = JsonUnparser.unparseandReturnJson(fakeUploadRecord);

                System.out.println("response message: \n" + fakeUploadResponseMessage);
                
                
        
        request.setAttribute("returnJsonToDisplay", fakeUploadResponseMessage);
        
        request.getRequestDispatcher("fakeFrontEnd/insertURL.jsp").forward(request, response);
            
            }
                            
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
        } catch (JSONException | SQLException ex) {
            Logger.getLogger(EvidenceInsertServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (JSONException | SQLException ex) {
            Logger.getLogger(EvidenceInsertServlet.class.getName()).log(Level.SEVERE, null, ex);
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
