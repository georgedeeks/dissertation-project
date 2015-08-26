<%-- 
    Document   : deleteobject
    Created on : 21-Jul-2015, 16:30:25
    Author     : cut14jhu
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>delete object page</title>
    </head>
    <body>
        <h1>delete object page</h1>
        
        <p>
            
            Please delete or edit entries for ${object_type}. 
            Please note: editing names will affect previous records too, and 
            removing entries could make relevant associated records harder to retrieve.
        
        </p>
        
       <p>
                        
            <%
            
                String objectType = (String) request.getAttribute("object_type");
                
                //out.print("object_type = " + objectType) ;
                
                                 
                String[] parts = objectType.split("\\s");
                String archivedOrNot = parts[0];
                String studentOrClass = parts[1];
                
                
                if (archivedOrNot.equals("Current"))
                {
                    
                    if (studentOrClass.equals("Students"))
                    {
                        
                        
                        out.print("To archive a current student, "
                            + "use the drop down bar for class assignment next to that student's name. "
                                + "Please note that unassigning a student from any class is as effective as 'deleting' them,"
                                + " which will simply only remove them from the selected academic year's database. "
                                + "To remove them from being displayed on the mobile application - for whatever period of time "
                                + "during the academic year - then simply tick them as being inactive.");
                    }
                    else
                    {
                        out.print("Warning: archiving a current class will remove the students "
                                + "associated with that class for the academic year. Consider renaming class instead or "
                                + "ensuring that you transfer all students from that class first.");
                    }
                    
                }
                else // = Archived
                {
                   
                        out.print("Deleting " + objectType + " will permanently remove them."
                                + " Be sure this is what you want to do before proceeding.");
                 
                }
               

            %>
        
        </p>

            
          <form action="${pageContext.request.contextPath}/DeleteOrEditObjectServlet" method="GET">
              
            <p>
               
                <input type="hidden" name="object_type" value='<% out.print(objectType); %>' />
                
            </p> 
            
         
                
                <table>
                  <tr>
                    <th><b><i>Name</i></b></th>
                    <th>
                        <b>
                            <i>
                              
                                <% 
                                    if (objectType.equals("Current Students"))
                                    {
                                        out.print("In/active?");
                                    }
                                    else
                                    {
                                        out.print(objectType);
                                    }                                    
                                                                   
                                %> 
                            
                            </i>
                        </b>
                    </th>
                    <th><b><i>Edit name?</i></b></th>
                    <%
                        
                        if (objectType.equals("Current Students"))
                        {
                            out.print("<th><b><i>Change class/Remove</i></b></th>");
                        }
                        
                    %>
                    
                  </tr>
                  <%
                
                // before constructing form (as a table), 
                    //we need to see if we must construct a dorp down list of classes (ie for current students)
                        
                        // must have these outside the if statement below
                        ArrayList<String> classNamesArray = new ArrayList();  
                        ArrayList<String> whichClassForStudent = new ArrayList();
                        ArrayList<Boolean> activeList = new ArrayList();
                        
                    if (objectType.equals("Current Students"))
                    {
                                                
                    // get variables
                                 
                        classNamesArray = (ArrayList<String>) request.getAttribute("currentClassesList");
                        
                        out.print("<input type='hidden' name='classNameSize' "
                                + "value=" + classNamesArray.size() + " />");
                        
                        for (int i = 0; i < classNamesArray.size(); i++)
                        {
                            out.print("<input type='hidden' name='className" 
                                    + i + "' value='" + classNamesArray.get(i) + "' />");
                        }
  
                        whichClassForStudent = (ArrayList<String>) request.getAttribute("whichClassForStudent");
                        
                        activeList = (ArrayList<Boolean>) request.getAttribute("activeList");
                        
                    }
                    
                // construct table
                    
                    ArrayList<String> nameArray = (ArrayList<String>) request.getAttribute("newListToDisplay");
                    
                    String optionSelected = "<option selected  align='center'";
                    String optionNotSelected = "<option  align='center'";
                    String optionPlusSelectedOrNot = "";
                    String latterOptionTagPart = "";
                    String checkedOrNot = " ";
                    
                    if (nameArray.size() == 0)
                    {
                        out.print("<div style='color:red'> " + "No records to display" + " </div>");
                    }
                    
                    for (int i = 0; i < nameArray.size(); i++)
                    {
                       
                        out.print("<tr>");
                            out.print("<td>" + nameArray.get(i) + "</td>");
                            
                          //NOTE: for current students, the delet box is actually the inactive box
                            
                                if (objectType.equals("Current Students"))
                                {
                                    //e
                                    
                                    if (activeList.get(i) == true)
                                    {
                                        checkedOrNot = " checked ";
                                    }
                                    else
                                    {
                                        checkedOrNot = " ";
                                    }
                                    
                                    out.print("<td><input type='checkbox' id='cbox_delete_" + i 
                                    + "' name='cbox_delete_" + i + "'" + checkedOrNot + "></td>");
                                    
                                    String tOrF = "H";
                                    
                                    if (activeList.get(i).booleanValue() == true)
                                    {
                                        tOrF = "t";
                                    }
                                    else
                                    {
                                        tOrF = "f";
                                    }
                                    
                                    out.print("<td><input type='hidden' "
                                            + "name='active_" + i + "' value='" + tOrF + "'/></td>");
                                    
                                }
                                else
                                {
                                    //f
                                    
                                    out.print("<td><input type='checkbox' id='cbox_delete_" + i 
                                    + "' name='cbox_delete_" + i + "'></td>");
                                    
                                }                       
                            
                            out.print("<td><input type='text' maxlength='50' name='edited_name_" + i + "' /></td>");
                              
                        // add a option box if 'current students'
                            if (objectType.equals("Current Students"))
                            {

                            //0 - print out classnameforstudent for servlet

                                out.print("<input type='hidden' name='whichClassForStudent" + i + "' value='" 
                                        + whichClassForStudent.get(i) + "' />");

                            // 1 - intiial construct select box

                                String firstPart = "<td><select name=selectbox" + i + ">"; // + i e.g. selectbox0

                                String totalMiddlePart = "";

                                String lastPart = "</select></td>";

                                // now we just need an array of <option></option>

                            // 2 compile options

                                for (int j = 0; j < classNamesArray.size(); j++)
                                {

                                    //String optionSelected = "<option selected";

                                    //String optionNotSelected = "<option";

                                    if 
                                            (classNamesArray.get(j).equals(whichClassForStudent.get(i)))                                                    
                                    {
                                        optionPlusSelectedOrNot = optionSelected;
                                    }
                                    else
                                    {
                                        optionPlusSelectedOrNot = optionNotSelected;
                                    }

                                    latterOptionTagPart = " value='" + classNamesArray.get(j) + "'>" 
                                            + classNamesArray.get(j) + "</option>";

                                    String completedOptionTag = optionPlusSelectedOrNot + latterOptionTagPart;

                                    totalMiddlePart += completedOptionTag ;

                                }

                                // 3 option to not assign to a class AT THEN END
                                totalMiddlePart += "<option align='center' value='Unassigned'>Unassigned</option>";

                                //4 print out
                                out.print(firstPart + totalMiddlePart + lastPart);

                            }

                        out.print("</tr>");
                        
                        out.print("<input type=hidden name='list" + i + "' value='"+ nameArray.get(i) +"' />");
                    
                    }
                    
                    out.print("<input type=hidden name='list_size' value='"+ nameArray.size() + "' />");
                    
                    out.print("<input type=hidden name='object_type' value='"+ objectType + "' />");
                    
                  
                  %>
                                 
            
                <p>
                
            <%

                try
                {
                    String errorMessage = (String) request.getAttribute("error_message");
                    if (errorMessage == null || errorMessage.equals(""))
                    {
                        
                    }
                    else
                    {

                        out.print("<div style='color:red'> " + errorMessage + " </div>");                    
                        
                    }
                }
                catch (Exception e)
                {
                         out.print("Error messages will be printed out here. None to display.");
                }   
                
                try
                {
                    String successMessage = (String) request.getAttribute("success_message");
                    if (successMessage == null || successMessage.equals(""))
                    {
                        
                    }
                    else
                    {

                        out.print("<div style='color:blue'> " + successMessage + " </div>");                    
                        
                    }
                }
                catch (Exception e)
                {
                         out.print("Success messages will be printed out here. None to display.");
                }    

        %>
            
            </p>
            
            <p>
                
                <input type="submit" id="normal" name='normal' value='Submit' />
                
            </p>
                                    
        </form>
            
        <p>
                
            <a href="${pageContext.request.contextPath}/dbms/launch/startMainMenu.jsp">Back to main menu</a>
                
        </p>   
        
    </body>
</html>
