<%-- 
    Document   : displayRecords
    Created on : 02-Jun-2015, 20:54:27
    Author     : George
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
                
        <p>
            
            <jsp:useBean id="recordsToDisplay" type="dbms.records.RecordsToDisplay" scope="request" />
	
        <form name="myform" action="${pageContext.request.contextPath}/DeleteFromRecordsServlet" method="GET">
            
            <p>
                Target: <jsp:getProperty name="recordsToDisplay" property="specificSpt" />
                <input type='hidden' name='specificSpt' value=<jsp:getProperty name="recordsToDisplay" property="specificSpt" /> />
            </p>
            
            <p>
                Term: <jsp:getProperty name="recordsToDisplay" property="specificTerm" />
                <input type='hidden' name='specificTerm' value=<jsp:getProperty name="recordsToDisplay" property="specificTerm" /> />
            </p>
            
            <p>
                Student: <jsp:getProperty name="recordsToDisplay" property="specificStudent" />
                <input type='hidden' name='specificStudent' value=<jsp:getProperty name="recordsToDisplay" property="specificStudent" /> />
            </p>
            
            <p>
                Class: <jsp:getProperty name="recordsToDisplay" property="specificClass" />
                <input type='hidden' name='specificClass' value=<jsp:getProperty name="recordsToDisplay" property="specificClass" /> />
            </p>
                        
            <p>
                
                <%
                    
                    int dbYear = recordsToDisplay.getSpecificYear();
                    String dbYearString = Integer.toString(dbYear);
                    
                    
                    int minusOne = recordsToDisplay.getSpecificYear() - 1;

                    out.print("Academic Year: " + minusOne + "-" + recordsToDisplay.getSpecificYear());
                %>
                
                
            
                
                <input type='hidden' name='year' value='<% 
                            out.print(dbYearString); 
                       %>' />
            
            </p> 
            
            <p>
                 Displaying available records:
            </p>
           
            

                <input type="hidden" name="strategy" value="specific" />
                
                <table>

                    <tr>

                        <td  width="45%" align="center" bgcolor="yellow">

                            URL

                        </td>

                        <td  width="10%" align="center" bgcolor="yellow">

                            Level

                        </td>

                        <td  width="30%" align="center" bgcolor="yellow">

                            Observation

                        </td>

                        <td width="10%" align="center" bgcolor="yellow">

                            Date of media

                        </td>

                        <td width="5%" align="center" bgcolor="yellow">

                            Delete?

                        </td>

                    </tr>

                    <%

                        for (int i = 0; i < recordsToDisplay.getMediaIdArray().size(); i++)
                        {
                            
                            //media id first
                            out.print("<input type='hidden' name='media_id_" + i + "' "
                                           + "value='" + recordsToDisplay.getMediaIdArray().get(i) + "' />");

                            out.print("<tr>");

                                out.print("<td align='left'>");

                                   out.print(recordsToDisplay.getFileNameArray().get(i));
                                   
                                   out.print("<input type='hidden' name='file_name_" + i + "' "
                                           + "value='" + recordsToDisplay.getFileNameArray().get(i) + "' />");
                                   
                                out.print("</td>");

                                out.print("<td' align='center'>");

                                   out.print(recordsToDisplay.getLevelArray().get(i));
                                   
                                   out.print("<input type='hidden' name='level_" + i + "' "
                                           + "value='" + recordsToDisplay.getLevelArray().get(i) + "' />");

                                out.print("</td>");

                                out.print("<td align='left'>");

                                   out.print(recordsToDisplay.getObservationArray().get(i));
                                   
                                   out.print("<input type='hidden' name='observation_" + i + "' "
                                           + "value='" + recordsToDisplay.getObservationArray().get(i) + "' />");

                                out.print("</td>");

                                out.print("<td align='center'>");

                                   out.print(recordsToDisplay.getMediaDateArray().get(i));
                                   
                                   out.print("<input type='hidden' name='media_date_" + i + "' "
                                           + "value='" + recordsToDisplay.getMediaDateArray().get(i) + "' />");

                                out.print("</td>");

                                out.print("<td align='center'>");

                                   out.print("<input type='checkbox' id='cbox_delete_" + i + "' name='cbox_delete_" + i + "'>");

                                out.print("</td>");

                            out.print("</tr>");

                        }
                        
                        out.print("<input type='hidden' name='list_size' value=" + recordsToDisplay.getMediaIdArray().size() + " />");

                    %> 

                </table>
                    
                <input type="submit" value="Delete selected records" />
                
            </form>
                
       
        <p>
            
            <%
        
                /*                
                if success/error message, TRY to put it here
                */
            
                String successMessage;
                
                try
                {
                   
                    successMessage =  request.getParameter("success_message") ;
                    
                    if (successMessage == null)
                    {
                        
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
