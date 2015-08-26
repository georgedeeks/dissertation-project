package dbms.activeYear;

import database.DatabaseConnectionSingleton;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
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

@WebServlet(name = "DeleteOrEditObjectServlet", 
        urlPatterns = {"/DeleteOrEditObjectServlet"})

public class DeleteOrEditObjectServlet extends HttpServlet 
{
    private ArrayList<String> oldList;
    private ArrayList<Integer> deleteList;
    private ArrayList<String> newList;
    private String objectType;
    
    private int resultInt;
    
    private ArrayList<String> whichClassforStudent;
    private ArrayList<String> currentClassesList;
    private ArrayList<Boolean> activeList;
    private ArrayList<Boolean> previousActiveList;
    private ArrayList<String> newWhichClassforStudent;
    
    private String sql;
    private Statement statement;
        
    private String errorMessage;
    private String successMessage;
    
    private Boolean didItWork;
        
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
        
        System.out.println("We've reached the DeleteOrEditObjectServlet server!");
       
        response.setContentType("text/html;charset=UTF-8");
                  
        try 
        { 
            successMessage = new String();
            errorMessage = new String();
            deleteList = new ArrayList();
            oldList = new ArrayList();
            whichClassforStudent = new ArrayList();
            newWhichClassforStudent = new ArrayList();
                        
            // get elements
            
            String listSizeString = request.getParameter("list_size");
                        
            int listSize = Integer.parseInt(listSizeString);
            
            objectType = request.getParameter("object_type");
                        
            if (objectType.equals("Current Students"))
            {
                
                currentClassesList = new ArrayList();                
                String classNameSizeString = request.getParameter("classNameSize");            
                int classNameSize = Integer.parseInt(classNameSizeString);
                
                activeList = new ArrayList();   
                previousActiveList = new ArrayList();
                
                for (int i = 0; i < classNameSize; i++)
                {
                    
                    String key = "className" + i;
                    
                    String addThis = request.getParameter(key);
                    
                    currentClassesList.add(addThis);
                    
                }
                
                for (int i = 0; i < listSize; i++)
                {
                    
                    String cBoxKey = "cbox_delete_" + i;
                    
                    if (request.getParameter(cBoxKey) != null)
                    {
                        activeList.add(true);
                    }
                    else
                    {
                        activeList.add(false);
                    }                    
                    
                    String booleanKey = "active_" + i;
                    
                    String convertThenAddString = request.getParameter(booleanKey);
                    
                    boolean toAdd = false;
                    
                    if (convertThenAddString.equals("t"))
                    {
                        toAdd = true;
                    }
                                                            
                    previousActiveList.add(toAdd);
                    
                }
                        
            }
            
            newWhichClassforStudent = new ArrayList();
            
            statement = DatabaseConnectionSingleton.getStatementInstance();
            
            for (int i = 0; i < listSize; i++)
            {
                
            // get name
                
                String nameKey = "list" + i;

                String element = request.getParameter(nameKey);
                
                oldList.add(element);
                
            // see if it needs to be deleted
                
                String deleteKey = "cbox_delete_" + i;
                
                if (request.getParameter(deleteKey) != null && objectType.equals("Current Students") == false)
                {
                        //then delete normally

                            didItWork = deleteFromDB(element); 

                            if (didItWork == true)
                            {
                                deleteList.add(i);
                            }
                            else
                            {
                                //error handling already managed
                            }
                }   
                else
                {
                    
                                  
                // #1 see if name needs to be edited
                    
                    String renameKey = "edited_name_" + i;
                    
                    String newElementName = request.getParameter(renameKey);
                                        
                    if (newElementName.equals(""))
                    {
                        //do nothing
                    }
                    else
                    {
                        // then edit name in db
                                                
                        didItWork = renameInDB(element, newElementName);
                                                
                        if (didItWork == true)
                        {
                            //success! so replace for displaylist 
                            
                            oldList.set(i, newElementName);
                            
                        }
                        
                    }
                    
                    if (objectType.equals("Current Students"))
                    {
                
                // IF current students, 
                        
                    // #2a
                        //Go through delete box and toggle in/active
                            // nb this is NOT delete code
                            // unassigned is delete code
                        
                        
                        Boolean oldBoolean = previousActiveList.get(i);
                        
                        Boolean currentBoolean = activeList.get(i);  
                        
                        if (Objects.equals(oldBoolean, currentBoolean))
                        {
                            //do nothing
                        }
                        else
                        {
                            //change in database
                            
                            sql = "update student_in_class_year set active = "
                                    + currentBoolean
                            + " FROM academic_year WHERE s_no = ("
                            + "SELECT student_in_class_year.s_no "
                            + "FROM student_in_class_year, student, academic_year "
                            + "WHERE student_in_class_year.s_no = student.s_no "
                            + "AND student.s_name = '"
                                    + element
                            + "' "
                            + "AND student_in_class_year.academic_year = academic_year.academic_year "
                            + "AND academic_year.active = true) "
                            + "AND student_in_class_year.academic_year = ("
                            + "SELECT academic_year.academic_year "
                            + "FROM academic_year "
                            + "WHERE academic_year.active = true);";
                   
                            didItWork = executeDBStatementForBooleanResult(element);
                            
                            if (didItWork == false)
                            {
                                // if failure, then next display list must be of its default value
                                
                                activeList.set(i, oldBoolean);
                                
                            }
                            
                        }
                        
                        
                // #2B  THEN see if classroom for the year needs to be switched
                     
                    // get original option
                        
                        String key = "whichClassForStudent" + i;
                        
                        String originalClassForStudent = request.getParameter(key);
                                                
                    // get currently selected option
                        
                        key = "selectbox" + i;
                        
                        String selectedClassName = request.getParameter(key);
                        
                    // see if selected option has been changed
                        
                        if (selectedClassName.equals(originalClassForStudent))
                        {
                            //do nothing as no change
                            
                            // add original to next display list (standard)
                            whichClassforStudent.add(originalClassForStudent);
                            
                        }
                        else
                        {
                            
                        // if it has changed, then update database
                            
                            if (selectedClassName.equals("Unassigned"))
                            {
                                // delete from student in class year!
                                
                                sql = "delete from student_in_class_year "
                                        + "where s_no = ("
                                        + "SELECT student.s_no "
                                        + "from student where student.s_name = '"
                                        + element
                                        + "') "
                                        + "and academic_year = ("
                                        + "SELECT academic_year.academic_year "
                                        + "from academic_year "
                                        + "where academic_year.active = true)"
                                        + ";"
                                        ;
                                
                                Boolean theResult = executeDBStatementForBooleanResult(selectedClassName);

                            // if successful, update our display list (if not keep with default)

                                if (theResult == true)
                                {
                                    
                                    // remove class name
                                   
                                    deleteList.add(i);
                                
                                }
                                else
                                {
                                    whichClassforStudent.add(originalClassForStudent);
                                }
                                
                            }
                            else
                            {
                                
                                // change the class name in stduet-in-calss=-year
                                
                                sql = "update student_in_class_year set class_id = "
                                        + "(SELECT class_id FROM classroom WHERE class_name = '"
                                        + selectedClassName
                                        + "') "
                                        + "FROM academic_year WHERE s_no = "
                                        + "(SELECT s_no FROM student WHERE s_name = '"
                                        + oldList.get(i)
                                        + "') "
                                        + "AND student_in_class_year.academic_year = academic_year.academic_year "
                                        + "AND academic_year.active = true;";
                                
                                Boolean theResult = executeDBStatementForBooleanResult(selectedClassName);

                            // if successful, update our display list (if not keep with default)

                                if (theResult == true)
                                {
                                    whichClassforStudent.add(selectedClassName);
                                }
                                else
                                {
                                    whichClassforStudent.add(originalClassForStudent);
                                }
                                
                            }
                            
                        }
                        
                    }
                    
                }
                
            }
            
            DatabaseConnectionSingleton.closeConnection();
                        
                        
            if (deleteList.size() > 0)
            {
                
                newList = new ArrayList();
                newWhichClassforStudent = new ArrayList();
                
                for (int i = 0; i < oldList.size(); i++)
                {                    
                    
                    Boolean deleteThis = false;
                
                    for (int j = 0; j < deleteList.size(); j++)
                    {                        
                        
                        if (i == deleteList.get(j))
                        {
                            
                            deleteThis = true;
                            
                        }

                    }
                    
                    if (deleteThis == false)
                    {
                        newList.add(oldList.get(i));
                        if (objectType.equals("Current Students"))
                        {
                            newWhichClassforStudent.add(whichClassforStudent.get(i));
                        }
                    }
                    
                }
                
            }   
            else
            {
                newList = oldList;     
                if (objectType.equals("Current Students"))
                        {
                newWhichClassforStudent = whichClassforStudent;
                        }
            }
            
            if (objectType.equals("Current Students"))
            {

                request.setAttribute("currentClassesList", currentClassesList);
  
                 request.setAttribute("whichClassForStudent", newWhichClassforStudent);
                 
                 request.setAttribute("activeList", activeList);
            }
            
            if (errorMessage.equals(""))
            {
               
                if (successMessage.equals(""))
                {
                    successMessage = "No interaction with database, as no changes were requested.";
                } 
                else
                {
                    //do nothing
                }
               
            } 
            else
            {
                request.setAttribute("error_message", errorMessage);
            }
            
            request.setAttribute("success_message", successMessage);
                        
            // load attributes for next page
            
            request.setAttribute("object_type", objectType);
                        
            request.setAttribute("newListToDisplay", newList);
            
            System.out.println("hello willy man!");
            
            // send to next page
            
            request.getRequestDispatcher("dbms/ActiveYear/deleteEditObject.jsp").forward(request, response);
            
            
        } catch (SQLException | ServletException e)
        {
            
            System.out.println("error = " + e);
            
            if (errorMessage.equals(""))
            {
                errorMessage = "Unable to delete selected database entries. "
                    + "Please try again or contact your system administrator.";
            }
            
            request.setAttribute("error_message", errorMessage);
            
            request.getRequestDispatcher("dbms/errorPage.jsp").forward(request, response);
        }
    
    }
    
   
    
    public Boolean deleteFromDB(String oldName)
    {
        switch (objectType) {
                
                // case Levels = send to newsupportplan
                // case Targets = send to newsupportplan
                // case Records = send to view records
            
                //case current students- toggleinactive method, above
                   
                case "Archived Students":

                    sql = "DELETE FROM student WHERE s_name = '"
                            + oldName
                            + "';";

                    break;

                case "Current Classes":

                    sql = "DELETE FROM class_year WHERE class_id IN "
                            + "(SELECT class_year.class_id "
                            + "FROM classroom, class_year, academic_year "
                            + "WHERE class_year.class_id = classroom.class_id "
                            + "AND class_year.academic_year = academic_year.academic_year "
                            + "AND academic_year.active = TRUE "
                            + "AND classroom.class_name = '"
                            + oldName
                            + "') "
                            + "AND academic_year IN "
                            + "(SELECT class_year.academic_year "
                            + "FROM classroom, class_year, academic_year "
                            + "WHERE class_year.class_id = classroom.class_id "
                            + "AND class_year.academic_year = academic_year.academic_year "
                            + "AND academic_year.active = TRUE "
                            + "AND classroom.class_name = '"
                            + oldName
                            + "')"
                            + ";"
                            ;

                    break;

                case "Archived Classes":

                    sql = "DELETE FROM classroom WHERE class_name = '"
                            + oldName
                            + "';";

                    break;

                default:
                    
                    //THERE HAS BEEN AN ERROR
                    
                    errorMessage = "Error matching up object type string!";
                    
                    System.out.println(errorMessage);
                    
                    break;
                    
            }
        
        return executeDBStatementForBooleanResult(oldName);
                
    }
    
    public Boolean executeDBStatementForBooleanResult(String oldName)
    {
        try {
            
            System.out.println("sql to execute: " + sql);
            
            resultInt = statement.executeUpdate(sql);
            
            if (resultInt != 0)
            {
                
                String success = "Success entering " + objectType + " '" + oldName + "' to database. ";
                
                if (successMessage.equals(""))
                {
                    successMessage = success;
                }
                else
                {
                    successMessage += success;
                }
                
                return true; 
            }
            else
            {
                
                return handleDBError(objectType, oldName);
                                
            }
            
        } catch (SQLException ex) {
            
            Logger.getLogger(DeleteOrEditObjectServlet.class.getName()).log(Level.SEVERE, null, ex);
                        
            System.out.println(ex);
            
            return handleDBError(objectType, oldName);
            
        }
    }
    
    public Boolean handleDBError(String objectType, String oldName)
    {
        
        String failure = "Problem updating " + objectType + " '" + oldName + "'. Please try again. ";
         
        if (errorMessage.equals(""))
        {
            errorMessage = failure;
        }
        else
        {
            errorMessage += failure;
        }
        
        return false; 
        
    }
    
    public Boolean renameInDB(String oldName, String newName)
    {
        
        String[] parts = objectType.split(" ");
        
        String studentOrClass = parts[1];
        
        if (studentOrClass.equals("Students"))
        {
            
            if (newName.equals("Unassigned")) // i.e. for current students, wish to unassign a student
            {
                //then delete instead
                
                sql = "DELETE FROM student_in_class_year "
                            + "WHERE s_no IN "
                            + "(SELECT student.s_no "
                            + "FROM student, student_in_class_year, academic_year "
                            + "WHERE student_in_class_year.s_no = student.s_no "
                            + "AND student_in_class_year.academic_year = academic_year.academic_year "
                            + "AND academic_year.active = TRUE "
                            + "AND student.s_name = '"
                            + oldName
                            + "');"
                            ;
                
            }
            else
            {
                //genuine rename
                
                sql = "update student set s_name = '"
                        + newName
                        + "' where s_name = '"
                        + oldName
                        + "';";
                
            }
                            
        }
        else // = Class
        {
            sql = "update classroom set class_name = '"
                    + newName
                    + "' where class_name = '"
                    + oldName
                    + "';";
        }
        
        return executeDBStatementForBooleanResult(oldName);
        
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
