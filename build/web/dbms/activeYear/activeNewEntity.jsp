<%-- 
    Document   : newentity
    Created on : 11-Jul-2015, 15:07:56
    Author     : cut14jhu
--%>


<%@page import="java.lang.Integer"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
                        
        <form action="${pageContext.request.contextPath}/ActiveNewEntityServlet" method="GET">
        
            <%

                String objectType = (String) request.getAttribute("object_type");

                out.print("<input type=hidden id='object_type' name='object_type' value='" + objectType + "' />");
                   
                ArrayList<String> classList = new ArrayList();
                
                if (objectType.equals("Student"))
                {

                    classList = (ArrayList<String>) request.getAttribute("class_list");

                    int classListSize = classList.size();
                    
                    String classListSizeString = "" + classListSize;
                                        
                    out.print("<input type=hidden id='classListSize' name='classListSize' value='" + classListSizeString + "' />");
                    
                    out.print("Select a class from the drop down list to assign the new student into a current class.");
                    
                }
                else
                {
                    out.print("Tick box to include entry into active year");
                }
                
            %>
                
            
            <h1>Enter new <% out.print(objectType); %>: </h1>
            
            <p>
                
                Name: <input type="text" id="new_entity" name="new_entity" maxlength="50" > 
                
                <!-- allow some spacing here -->      
                                
                <%
                    
                    if (objectType.equals("Student") )
                    {
                        
                    //special select option box
                                       
                        String firstPart = "<select name=selectbox>"; // + i e.g. selectbox0

                        // option to not assign to a class
                        String middlePart = "<option value='Unassigned' selected >Unassigned</option>";

                        for (int i = 0; i < classList.size(); i++)
                        {

                            String className = classList.get(i);
                            middlePart += "<option value='" + className + "'>" + className + "</option>";

                            //print out class names for easy retrieval in servlet
                            out.print("<input type=hidden name='class_name_" + i + "' value='"+ className +"' />");

                        }

                        String lastPart = "</select>";

                        out.print(firstPart + middlePart + lastPart);
                        
                    }
                    else //normal checkbox for Class
                    {
                        out.print("<input type='checkbox' id='new_entity_cbox' name='new_entity_cbox' checked>");
                    }
                    
                %>
                    
            </p>           
            
            <p>
                
                <input type="submit" id="another" name="another" value="Submit entry and add another" />
                <input type="submit" id="normal" name="normal" value="Submit entry only and exit" />
                
            </p>
            
            <%
        
                /*                
                if success/error message, put it here
                */
                   
                    String errorMessage = "";
                    String successMessage = "";
                    
                try
                {
                     errorMessage = (String) request.getAttribute("error_message");
                    if (errorMessage.equals(""))
                    {
                        
                    }
                    else
                    {

                        out.print("<div style='color:red'> " + errorMessage + " </div>");                    
                        
                    }
                }
                catch (Exception e)
                {
                         out.print("Success/error messages will be printed out here. None to display.");
                }                  

        
                try
                {
                   successMessage = (String) request.getAttribute("success_message");
                    if (successMessage.equals(""))
                    {
                        
                    }
                    else
                    {

                        out.print("<div style='color:blue'> " + successMessage + " </div>");                    
                        
                    }
                }
                catch (Exception e)
                {
                         out.print("Success/error messages will be printed out here. None to display.");
                }    
                                
            %>
                        
        </form>
        
    </body>
</html>
