package dbms.records;

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
 * and open the template in the editor.
 */

/**
 *
 * @author cut14jhu
 */

@WebServlet(name = "DeleteFromRecordsServlet", 
        urlPatterns = {"/DeleteFromRecordsServlet"})

public class DeleteFromRecordsServlet extends HttpServlet 
{
    
    private ArrayList<String> oldList = new ArrayList<>();        
    private ArrayList<String> newListToSend = new ArrayList<>();
    
    private String successMessage;
    private String errorMessage;
    private Boolean noneWereTicked = true;
    
    private String sql;    
    private Statement statement;    
    private ResultSet resultSet;
        
    private ArrayList<Integer> mediaIdArray = new ArrayList<>();
    private ArrayList<String> fileNameArray = new ArrayList<>();
    private ArrayList<String> targetArray = new ArrayList<>();
    private ArrayList<String> levelArray = new ArrayList<>();
    private ArrayList<String> observationArray = new ArrayList<>();
    private ArrayList<String> mediaDateArray = new ArrayList<>();
    
      private ArrayList<Integer> arrayPositionToDelete = new ArrayList<>();
    
    private String specificSpt;
    private String specificTerm;
    private String specificStudent; 
    private String specificClass; 
    private int specificYear;
    
    private ArrayList<String> specificSptArray = new ArrayList<>();
    private ArrayList<String> selectedTermCapitalisedArray = new ArrayList<>();
    private ArrayList<String> specificStudentArray = new ArrayList<>();
    private ArrayList<String> specificClassArray = new ArrayList<>();
    private ArrayList<Integer> specificYearArray = new ArrayList<>();
    
    private int listSize;
    private String strategy;
    
    private int mediaIdToDelete;
    
    private RecordsToDisplay recordsToDisplay;
    

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
         
        // reset variables for error control
        oldList = new ArrayList<>();            
        newListToSend = new ArrayList<>();
        successMessage = new String();
        errorMessage = new String();
        noneWereTicked = true;
        sql = new String(); 
        mediaIdArray = new ArrayList<>();
        fileNameArray = new ArrayList<>();
        targetArray = new ArrayList<>();
        levelArray = new ArrayList<>();
        observationArray = new ArrayList<>();
        mediaDateArray = new ArrayList<>();
        arrayPositionToDelete = new ArrayList<>();
        specificSpt = new String();
        specificTerm = new String();
        specificStudent = new String();
        specificClass = new String();
        specificSptArray = new ArrayList<>();
        selectedTermCapitalisedArray = new ArrayList<>();
        specificStudentArray = new ArrayList<>();
        specificClassArray = new ArrayList<>();
        specificYearArray = new ArrayList<>();
        mediaIdToDelete = 0;
        
                
        System.out.println("We've reached the DeleteFromRecordsServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
        
        // Using try {...} catch {...} for error control
        try 
        { 
            
            strategy = request.getParameter("strategy");
                        
            String oldListSizeString = request.getParameter("list_size");
            int listSize = Integer.parseInt(oldListSizeString);
            
            //listSize = Integer.parseInt(listSizeString);
            
            /*
            
                Either the display specific or display all records page reaches this page, 
                so we'll see strategy string and deal with each entirely differently
            
            */
            
            // REGARDLESS of if/;else decision tree make this true
            noneWereTicked = true;
            
            String pageToSendTo = "";
            
            // used for later success message
            int toBeDeleted = 0; //test
                                
            if (strategy.equals("specific"))
            {
                
                System.out.println("specific strategy");
                
                                            
                    specificSpt = request.getParameter("specificSpt");
                    specificTerm = request.getParameter("specificTerm"); 
                    specificStudent = request.getParameter("specificStudent"); 
                    specificClass = request.getParameter("specificClass");
                    String specificYearString = (String) request.getParameter("year"); 
         //           String specificYearString = specificYearObject;
                    specificYear = Integer.parseInt(specificYearString);
                                    
                // see which checkboxes have been ticked

                   statement = DatabaseConnectionSingleton.getStatementInstance();

                   for (int i = 0; i < listSize; i++)
                   {

                       String key = "cbox_delete_" + i;

                       if (request.getParameter(key) == null)
                       {

                           System.out.println("entity " + i + " WAS selected to be SAVED");

                           // add that entity to the new display list

                           // media id

                           String keyToUse = "media_id_" + i;

                           String valueToAddString = request.getParameter(keyToUse);

                           int valueToAddInt = Integer.parseInt(valueToAddString);

                           mediaIdArray.add(valueToAddInt);

                       // file name

                           keyToUse = "file_name_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           fileNameArray.add(valueToAddString); 

                       // level name

                           keyToUse = "level_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           levelArray.add(valueToAddString);    

                       // observatione

                           keyToUse = "observation_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           observationArray.add(valueToAddString);      

                       // media date

                           keyToUse = "media_date_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           mediaDateArray.add(valueToAddString); 
                       

                       }
                       else
                       {
                           toBeDeleted++;

                           System.out.println("entity " + i + " WAS selected to be deleted...");

                           noneWereTicked = false;

                           // remove that entry from db

                           String mediaIdKey = "media_id_" + i;

                           String mediaIdToDeleteString = (String) request.getParameter(mediaIdKey);

                           mediaIdToDelete = Integer.parseInt(mediaIdToDeleteString);

                           sql = "DELETE FROM evidence WHERE media_id = "
                                   + mediaIdToDelete
                                   + ";"
                                   ;



                           int resultInt = statement.executeUpdate(sql);

                           if (resultInt != 1)
                           {
                               //error

                               errorMessage = "Error deleting entity #" + mediaIdToDelete + ". Please try again or contact your system administrator."; 
                               System.out.println(errorMessage);
                           }


                       } // end of if need to delete cbox

                   }     // end of for loop

                   DatabaseConnectionSingleton.closeConnection();

               // finisehd iether
                    //deleting entitiy
               // OR
                   // adding entities'properties to local variables to then load into recordstodisplay bean

               // now we're ready, so: create new recordsToDisplay object

                   recordsToDisplay = new RecordsToDisplay
                                            (
                                                    mediaIdArray, 
                                                    fileNameArray, 
                                                    levelArray, 
                                                    observationArray, 
                                                    mediaDateArray, 
                                                    specificSpt,
                                                    specificTerm, 
                                                    specificStudent, 
                                                    specificClass, 
                                                    specificYear
                                            );
                   
                   pageToSendTo = "dbms/displayRecords/displayRecords.jsp";
                
            }
            else // strategy = all records
            {
                
                   System.out.println("specific strategy");

                   // see which checkboxes have been ticked

                    

                   statement = DatabaseConnectionSingleton.getStatementInstance();

                   for (int i = 0; i < listSize; i++)
                   {

                       String key = "cbox_delete_" + i;

                       if (request.getParameter(key) == null)
                       {

                           System.out.println("entity " + i + " WAS selected to be SAVED");

                           // add that entity to the new display list

                           // media id

                           String keyToUse = "media_id_" + i;

                           String valueToAddString = request.getParameter(keyToUse);

                           int valueToAddInt = Integer.parseInt(valueToAddString);

                           mediaIdArray.add(valueToAddInt);

                       // file name

                           keyToUse = "file_name_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           fileNameArray.add(valueToAddString); 

                       // level name

                           keyToUse = "level_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           levelArray.add(valueToAddString);    

                       // observatione

                           keyToUse = "observation_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           observationArray.add(valueToAddString);      

                       // media date

                           keyToUse = "media_date_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           mediaDateArray.add(valueToAddString); 

                       // target

                           keyToUse = "target_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           specificSptArray.add(valueToAddString);     

                       // term

                           keyToUse = "term_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           selectedTermCapitalisedArray.add(valueToAddString); 

                       // student

                           keyToUse = "student_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           specificStudentArray.add(valueToAddString);     

                       // class

                           keyToUse = "class_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           specificClassArray.add(valueToAddString); 

                       // year

                           keyToUse = "year_" + i;

                           valueToAddString = request.getParameter(keyToUse);

                           valueToAddInt = Integer.parseInt(valueToAddString);

                           specificYearArray.add(valueToAddInt); 

                       }
                       else
                       {
                           toBeDeleted++;

                           System.out.println("entity " + i + " WAS selected to be deleted...");

                           noneWereTicked = false;

                           // remove that entry from db

                           String mediaIdKey = "media_id_" + i;

                           String mediaIdToDeleteString = (String) request.getParameter(mediaIdKey);

                           mediaIdToDelete = Integer.parseInt(mediaIdToDeleteString);

                           sql = "DELETE FROM evidence WHERE media_id = "
                                   + mediaIdToDelete
                                   + ";"
                                   ;

                           int resultInt = statement.executeUpdate(sql);

                           if (resultInt != 1)
                           {
                               //error

                               errorMessage = "Error deleting entity #" + mediaIdToDelete + ". Please try again or contact your system administrator."; 
                               System.out.println(errorMessage);
                           }


                       } // end of if need to delete cbox

                   }     // end of for loop

                   DatabaseConnectionSingleton.closeConnection();

               // finisehd iether
                    //deleting entitiy
               // OR
                   // adding entities'properties to local variables to then load into recordstodisplay bean

               // now we're ready, so: create new recordsToDisplay object

                   recordsToDisplay = new RecordsToDisplay
                   (
                           mediaIdArray, 
                           fileNameArray, 
                           levelArray, 
                           observationArray, 
                           mediaDateArray, 
                           specificSptArray,
                           selectedTermCapitalisedArray, 
                           specificStudentArray, 
                           specificClassArray, 
                           specificYearArray
                   );

                   pageToSendTo = "dbms/displayRecords/displayAllRecords.jsp";
                   
            } // end of 'specific' if/else statement       
                   
                   
                   // laod variables

                   request.setAttribute("recordsToDisplay", recordsToDisplay);

                   //what about if errrormessage equals more than ""?
                       //TODO (simple if else)

                   if (toBeDeleted == 0)
                   {
                       successMessage = "No files were selected to be deleted.";
                   }
                   else if (toBeDeleted == 1)
                   {
                       successMessage = "Congratulations! 1 record was deleted.";
                   } else
                   {
                       //TODO
                       successMessage = "Congratulations! " + toBeDeleted + " records were deleted.";
                   }

                   request.setAttribute("success_message", successMessage);

                   // send to specific records to display

                   request.getRequestDispatcher(pageToSendTo).forward(request, response);

            //thus end of method
            //move to catch
         
        }
        catch(ServletException | IOException e) {
            
            // Try and deal with any unhandled error here
            System.out.println("Error: " + e);
            
            //TODO
            request.setAttribute("error_message", "Sorry, an internal problem occurred. Please try again or contact your system administrator.");
            
            // Send it on to a different View
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        
        } catch (SQLException ex) {
            Logger.getLogger(DeleteFromRecordsServlet.class.getName()).log(Level.SEVERE, null, ex);
            
            errorMessage = "Error deleting entity #" + mediaIdToDelete + ". Please try again or contact your system administrator."; 
            
            request.setAttribute("error_message", errorMessage);
            
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
