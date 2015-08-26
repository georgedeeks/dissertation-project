<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%> 


<!DOCTYPE html>

<html>
    <head>
        <title>Displaying user query results</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">          
   <link rel="stylesheet" href="style.css"> 
    --> 
    </head>
    <body>
        
        <jsp:useBean id="displayInfoBean" type="dbms.DisplayInfoBean" scope="request" />
        	
        Pick perhaps at least one  
            <!-- currently active / archived --> 
                <%
                       
                    //active status from DIB's JSON
                    
                    String activeStatus = displayInfoBean.getPreviousInfo().getString("active_status");
                    
                    out.print(activeStatus);
                    
                %>
             
            <!-- Class / Student -->
                <%
                       
                    // object type from DIB's JSON
                    
                    String objectType = displayInfoBean.getPreviousInfo().getString("object_type");
                    
                    out.print(objectType);
                    
                %> 
        to include into the new academic year.        
            
            
        <form name="myform" action="${pageContext.request.contextPath}/EntitiesForNewYearServlet" method="GET">
          
            <%
                
                out.print("<input type='text' name='active_status' value='" + activeStatus + "' />");
                    
                out.print("<input type='text' name='object_type' value='" + objectType + "' />");
                  
                    
                String selecterTagFirstPart;
                String selecterTagFinalPart;
                
                if (objectType.equals("Student"))
                {
                    
                    
                        ArrayList<String> studentList = displayInfoBean.getListToDisplay();



                        Object listOfClassNamesSizeObject = displayInfoBean.getPreviousInfo().get("classname_size");  

                        String listOfClassNamesSizeString = listOfClassNamesSizeObject.toString();
                        
                        int listOfClassNamesSize = Integer.parseInt(listOfClassNamesSizeString);
                        
                        // = Integer.parseInt(listOfClassNamesSizeString);

                        ArrayList<String> classNamesArray = new ArrayList();

                        for (int i = 0; i < listOfClassNamesSize; i++)
                        {
                            String key = "classname_" + i;

                            String className = (String) displayInfoBean.getPreviousInfo().get(key);

                            classNamesArray.add(className) ;

                        }

                        // create an option box

                        String firstPart = "<select name=item"; // + i e.g. selectbox0

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

                        //String optionBoxToPrintOut = firstPart + middlePart + lastPart;

                        String middleAndLastPart = middlePart + lastPart;




                                selecterTagFirstPart = firstPart;

                                selecterTagFinalPart = middleAndLastPart ;
                }
                else //object type = "Class"
                {
                    // string to add = checkbox
                    
                    selecterTagFirstPart = "<input type=checkbox name='item" ;
                    
                    selecterTagFinalPart = "'>" ;
                    
                }
                
                // selecterTag is now initiated for both class and student
                
                for(int i=0; i<displayInfoBean.getListToDisplay().size(); i++)
                {
                   
                    // TODO (based on above)
                    // out.print(displayInfoBean.getListToDisplay().get(i) + " " + "<input type=" + stringToAdd + " name='item" + i + "'>");
                       out.print(displayInfoBean.getListToDisplay().get(i) + " " + selecterTagFirstPart + i + selecterTagFinalPart);
                    
                    out.print("<input type=text name='list" + i + "' value='" + displayInfoBean.getListToDisplay().get(i) + "'> "
                            + "(change to hidden type input)");
                }
                                     
                out.print("<input type='hidden' id='listsize' name='listsize' value='" 
                          + displayInfoBean.getListToDisplay().size() + "'>");
                
                out.print("<input type='hidden' id='json_object' name='json_object' value='" 
                          + displayInfoBean.getPreviousInfo().toString() + "'>");
                                
                
            %>
              
            <input type='submit'/>
                    
        </form> 
                        
    </body>
    
</html>

