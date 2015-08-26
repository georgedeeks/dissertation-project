<%-- 
    Document   : newentity
    Created on : 11-Jul-2015, 15:07:56
    Author     : cut14jhu
--%>

<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        
        
        <jsp:useBean id="displayInfoBean" type="dbms.DisplayInfoBean" scope="request" />
        
        <form action="${pageContext.request.contextPath}/NewEntityServlet" method="GET">
        
            <%

                String requestedStrategy = (String) request.getAttribute("strategy") ;

                String objectType = displayInfoBean.getPreviousInfo().getString("object_type");

                //print out strategy string for next servlet's processing if required            

                out.print("<input type=hidden id='strategy' name='strategy' value='" + requestedStrategy + "' />");

                out.print("<input type=hidden id='object_type' name='object_type' value='" + objectType + "' />");
                
                out.print("<input type=hidden id='json_object' name='json_object' value='" + displayInfoBean.getPreviousInfo().toString() + "' />");

            %>
                
            
            <h1>Enter <% out.print(objectType + " for " + requestedStrategy + "!"); %> </h1>
            
            <p>
                
                Name: <input type="text" id="new_entity" name="new_entity" maxlength="50" > 
                
                <!-- allow some spacing here -->      
                
                Include in ${strategy}? 
                
                <%
                    
                    if (objectType.equals("Student") )
                    {
                        

                        //special select option box
                        
                        
                        // 1 create an option box
                        
                            // 1a - get class names
                                                    
                                int listOfClassNamesSize = (Integer) displayInfoBean.getPreviousInfo().get("classname_size");

                                ArrayList<String> classNamesArray = new ArrayList();

                                for (int i = 0; i < listOfClassNamesSize; i++)
                                {
                                    String key = "classname_" + i;

                                    String className = (String) displayInfoBean.getPreviousInfo().get(key);

                                    classNamesArray.add(className) ;

                                }
                                
                            // 1b - f
                    
                    String firstPart = "<select name=selectbox"; // + i e.g. selectbox0
                    
                    String middlePart = ">";
                    
                    String lastPart = "</select>";
                    
                    // option to not assign to a class
                    middlePart += "<option value='Unassigned'>Unassigned</option>";
                    
                    for (int i = 0; i < classNamesArray.size(); i++)
                    {
                        
                        String className = classNamesArray.get(i);
                        middlePart += "<option value='" + className + "'>" + className + "</option>";
                        
                        //print out class names for easy retrieval in servlet
                        // no need for this anymore
                        // out.print("<input type=hidden name='class_name_" + i + "' value='"+ className +"' />");
                        
                    }
                        
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
                <input type="submit" id="normal" name="normal" value="Submit entry only" />
                <input type="submit" id="none" name="none" value="Submit no new entries" />
                
            </p>
            
            <%
        
                /*
                
                if success/error message, put it here
                */
                    
                    

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
                         out.print("Success/error messages will be printed out here. None to display.");
                }                  

        
                

                try
                {
                    String successMessage = (String) request.getAttribute("success_message");
                    if (successMessage == null || successMessage.equals(""))
                    {
                        
                                    

        
                // PRINT OUT EXISTING ENTITIES
                
                /*
                
                if new year strategy, add object type's current names below dynamically PLUS an option to delete an entitity  if required
                
                if not new year strategy, load current active objecttypes into Bean.
                
                */
                
                try{
                
                String totallyNew = request.getParameter("totally_new");
                        
                if (totallyNew.equals("yes"))
                {
                    out.print("<p>(No previous entities to display here, as it's a new list. "
                            + "But they would normally be here.)</p>");
                }
                else
                {
                    
                    // probably entitties to print out
                
                    try
                    {
                        out.print("<p>");

                            out.print("List of currently selected " + objectType + " entities:");

                        out.print("</p>");

                        out.print("<p>");

                            // get attributes for for loop

                            // arraysizestring 
                            // keywithoutkindex

                            String arraySizeString = new String();
                            String keyMinusIndex = new String();




                            if (objectType.equals("Class"))
                            {


                                 arraySizeString = (String) displayInfoBean.getPreviousInfo().get("classname_size");

                                 keyMinusIndex = "classname_";
                                 
                            }
                            else if (objectType.equals("Student") )
                            {
                                // hi
                                    arraySizeString = (String) displayInfoBean.getPreviousInfo().get("student_in_class_size");

                                    keyMinusIndex = "student_in_class_";

                            }
                            else //error
                            {
                                //TODO

                                out.print("<div style='color:red'> " + "Issue finding entities for '" + objectType + "'."
                                        + " Please try again, or contact your system administrator." + " </div>");
                            }

                            int arraySize = Integer.parseInt(arraySizeString);

                            // for loop

                            for (int i = 0; i < arraySize; i++)
                            {

                                out.print("<p>");

                                    String key = keyMinusIndex + i;

                                    String toDisplayStudentAndClassFormatted = "";

                                    if (objectType.equals("Student") )
                                    {
                                        // we must break up student value to user friendly string

                                        String notToDisplay = (String) displayInfoBean.getPreviousInfo().get(key);

                                        String[] part = notToDisplay.split("#");

                                        toDisplayStudentAndClassFormatted = part[0] + " (" + part[1] + ")";

                                        out.print(toDisplayStudentAndClassFormatted);

                                    }
                                    else
                                    {
                                        out.print(displayInfoBean.getPreviousInfo().get(key));
                                    }

                                    if (requestedStrategy.equals("new year"))
                                    {

                                        out.print("   ("); // bit of space between name and checkbox

                                        out.print("<input type=checkbox name='item" + i + "'> "
                                                + "Check to remove this name from " + requestedStrategy + " entry)");

                                    }
                                    else
                                    {
                                        out.print("Please remove these active entries for " + objectType 
                                                + " by clicking on the appropriate link on the header menu.");
                                    }

                                out.print("</p>");

                            }                        

                        out.print("</p>");



                
                    }
                    catch (Exception e)
                    {
                    
                        out.print("<p>(No previous entities to display here.)</p>");
                            
                    }
                        
                }
                } catch (Exception e)
                {
                    

                    //out.print("error G");
                } 
                
                
                
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
