<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>  
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%> 


<!DOCTYPE html>

<html>
    <head>
        <title>Enter new year</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
   <!--    
   <link rel="stylesheet" href="style.css"> 
    --> 
    </head>
    <body>
        
        <jsp:useBean id="displayInfoBean" type="dbms.DisplayInfoBean" scope="request" />
        
        <div>
            
            Enter the year (i.e. 4 digits) that you would like the new academic year to start in (e.g., for 2015-2016, please type in "2015"): 
            
            <form name='myform' action="${pageContext.request.contextPath}/NewYearServlet" method="GET"> 
                
                <p>
                    Note:
                    <input type="text" id="year_list" name="year_list" value=<jsp:getProperty name="displayInfoBean" property="activeYear" /> />
                </p>

                <p>
                    The current active year is: 
                    
                    <% 
                
                        int minusOne = displayInfoBean.getActiveYear() - 1;
                        
                        out.print(minusOne + "-" + displayInfoBean.getActiveYear() );
 
                    %> 
                    
                    <input type="text" id="active_year" name="active_year" value=<jsp:getProperty name="displayInfoBean" property="activeYear" /> />
                </p>

                <p>
                    The most modern year in the database is: 
                    
                    <% 
                
                        int subtractOne = displayInfoBean.getNewestYear() - 1;
                        
                        out.print(subtractOne + "-" + displayInfoBean.getNewestYear() );
 
                    %> 
                    
                    <input type="text" id="newest_year" name="newest_year" value=<jsp:getProperty name="displayInfoBean" property="newestYear" /> />
                </p>
                <p>
                    (Please ensure you pick a new year that is no more than one greater than the newest established academic year in the system.)
                    
                </p>
                
                <%
                
                    out.print("<input type='hidden' id='listsize' name='listsize' value=" 
                          + displayInfoBean.getYearList().size() + ">");
                    
                    for(int i=0; i<displayInfoBean.getYearList().size(); i++)
                            {

                                out.print("<input "
                                        + "type='hidden' "
                                        + "id='list" 
                                        + i + "' "
                                        + "name='list" 
                                        + i + "' "
                                        + "value='" 
                                        + displayInfoBean.getYearList().get(i)
                                        +  "'>");

                            }
                %>
                
                <input type="text" id="startYear" name="startYear"  maxlength="4" value="<% out.print(displayInfoBean.getNewestYear()); %>"/> 
                <input type="submit"/>
                
            </form>     
            
            <%

                if (displayInfoBean.getErrorMessage() == null || displayInfoBean.getErrorMessage().equals(""))

                {
                    
                    // nothing!
                }
                else
                {
                    // print out error message in red
                    
                    
                    out.print("<div style='color:red'> " + displayInfoBean.getErrorMessage() + " </div>");

             
                }

            %>
            
        </div>
        
    </body>
</html>

