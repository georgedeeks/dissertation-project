<%-- 
    Document   : displayRecords
    Created on : 02-Jun-2015, 20:54:27
    Author     : George


COULD DO:

confirm delete all?

    <a href="home" class="confirm">Go to home</a>

    and then:
    $(".confirm").confirm();

[http://myclabs.github.io/jquery.confirm/]

(could do for 2 other delete all pages)

SHOULD DO:

checkbox that un/ticks all checkboxes (select all)

--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Display Records</title>
    </head>
    
    <body>
        
        <h1>You have reached the display records page!</h1>
                 
        <jsp:useBean id="recordsToDisplay" type="dbms.records.RecordsToDisplay" scope="request" />
	 
        <p>
                 
            Displaying all available records:
            
        </p>
        
        <form name="myform" action="${pageContext.request.contextPath}/DeleteFromRecordsServlet" method="GET">

            <input type="hidden" name="strategy" value="all" />
            
            <table>

                <tr>

                    <td  width="40%" align="center" bgcolor="yellow">

                        URL

                    </td> 

                    <td width="5%" align="center" bgcolor="yellow">

                        Year

                    </td>

                    <td  width="5%" align="center" bgcolor="yellow">

                        Class

                    </td>

                    <td  width="5%" align="center" bgcolor="yellow">

                        Student

                    </td>

                    <td width="5%" align="center" bgcolor="yellow">

                        Term

                    </td>

                    <td width="5%" align="center" bgcolor="yellow">

                        Date of media

                    </td>

                    <td width="5%" align="center" bgcolor="yellow">

                        Support Plan Target

                    </td>

                    <td  width="5%" align="center" bgcolor="yellow">

                        Support Plan Level

                    </td>

                    <td  width="20%" align="center" bgcolor="yellow">

                        Observation

                    </td>

                    <td  width="5%" align="center" bgcolor="yellow">

                        Delete?

                    </td>

                </tr>

                <%
                    
                    int listSize = recordsToDisplay.getMediaIdArray().size() ;
                    
                    out.print("<input type='hidden' name='list_size' value='" + listSize + "' />");
                                      
                    if (listSize == 0)
                    {
                        out.print("No records to display.");
                    }
                    else
                    {
                        //print out elements
                    
                        for (int i = 0; i < listSize; i++)
                        {

                            //media id first
                            out.print("<input type='hidden' name='media_id_" + i + "' "
                                    + "value='" + recordsToDisplay.getMediaIdArray().get(i) + "' />");

                            out.print("<tr>");

                                out.print("<td align='left'>");

                                    //file

                                    out.print(recordsToDisplay.getFileNameArray().get(i));

                                    out.print("<input type='hidden' name='file_name_" + i + "' "
                                    + "value='" + recordsToDisplay.getFileNameArray().get(i) + "' />");

                                out.print("</td>");                        

                                out.print("<td align='center'>");

                                    //year

                                    out.print(recordsToDisplay.getSpecificYearArray().get(i));

                                    out.print("<input type='hidden' name='year_" + i + "' "
                                    + "value='" + recordsToDisplay.getSpecificYearArray().get(i) + "' />");

                                out.print("</td>");

                                out.print("<td align='center'>");

                                    //class

                                    out.print(recordsToDisplay.getSpecificClassArray().get(i));

                                    out.print("<input type='hidden' name='class_" + i + "' "
                                    + "value='" + recordsToDisplay.getSpecificClassArray().get(i) + "' />");

                                out.print("</td>");

                                out.print("<td align='center'>");

                                      //student

                                    out.print(recordsToDisplay.getSpecificStudentArray().get(i));

                                    out.print("<input type='hidden' name='student_" + i + "' "
                                    + "value='" + recordsToDisplay.getSpecificStudentArray().get(i) + "' />");

                                out.print("</td>");

                                out.print("<td align='center'>");

                                    //term

                                    out.print(recordsToDisplay.getSelectedTermCapitalisedArray().get(i));

                                    out.print("<input type='hidden' name='term_" + i + "' "
                                    + "value='" + recordsToDisplay.getSelectedTermCapitalisedArray().get(i) + "' />");

                                out.print("</td>");

                                out.print("<td align='center'>");

                                    //date

                                    out.print(recordsToDisplay.getMediaDateArray().get(i));

                                    out.print("<input type='hidden' name='media_date_" + i + "' "
                                    + "value='" + recordsToDisplay.getMediaDateArray().get(i) + "' />");

                                out.print("</td>");

                                out.print("<td align='center'>");

                                    //target

                                    out.print(recordsToDisplay.getSpecificSptArray().get(i));

                                    out.print("<input type='hidden' name='target_" + i + "' "
                                    + "value='" + recordsToDisplay.getSpecificSptArray().get(i) + "' />");

                                out.print("</td>");

                                out.print("<td align='center'>");

                                    //level

                                    out.print(recordsToDisplay.getLevelArray().get(i));

                                    out.print("<input type='hidden' name='level_" + i + "' "
                                    + "value='" + recordsToDisplay.getLevelArray().get(i) + "' />");

                                out.print("</td>");

                                out.print("<td align='left'>");

                                    //observation

                                    out.print(recordsToDisplay.getObservationArray().get(i));

                                    out.print("<input type='hidden' name='observation_" + i + "' "
                                    + "value='" + recordsToDisplay.getObservationArray().get(i) + "' />");

                                out.print("</td>");

                                out.print("<td align='center'>");

                                    //delete cbox

                                    out.print("<input type='checkbox' id='cbox_delete_" + i + "' name='cbox_delete_" + i + "'>");

                                out.print("</td>");                        

                            out.print("</tr>");

                        }

                    }
                    

                %> 

            </table>
                
            <p>
                
                <input type="submit" name="delete_some" value="Delete selected records" />
                        
            </p>

            <p>
                
                (Warning: deleting all records' trace from the database is permanent and cannot be undone 
                - however, you will still be able to access the files themselves from the appropriate directory.)
           
            </p>
                
        </form>  
                
        <p>
            
            <%
        
                /*                
                if success/error message, TRY to put it here
                */
            
                String successMessage;
                
                try
                {
                   
                    successMessage =  (String) request.getAttribute("success_message") ;
                    
                    if (successMessage == null || successMessage.equals(""))
                    {
                        //do nothing
                    }
                    else
                    {
                    out.print("<div style='color:green'> " + successMessage + " </div>");
                    }          
                }
                catch (Exception e)
                {
                    //do nothign
                }
                
            %>
            
        </p>       
        
        <p>
                
            <a href="${pageContext.request.contextPath}/dbms/launch/startMainMenu.jsp">Back to main menu</a>
                
        </p>
                
    </body>
    
</html>
