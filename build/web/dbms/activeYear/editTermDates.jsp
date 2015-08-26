<%-- 
    Document   : choosetermdates
    Created on : 10-Jul-2015, 10:41:28
    Author     : cut14jhu

    Non-Plagiarism: code adapted from https://jqueryui.com/datepicker/

   COULD DO
        - change the first (and default value for all other ones) timepicker so that it starts in september of the earlier of two years
        - change the latter datepickers so that they are looking to start the day after the previously selected date
        GO TO: http://jqueryui.com/datepicker/
        if time deffo look at http://api.jqueryui.com/datepicker/#option-defaultDate


--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>jQuery UI Datepicker - Default functionality</title>

        <link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">        
        <script src="//code.jquery.com/jquery-1.10.2.js"></script>
        <script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
        <link rel="stylesheet" href="/resources/demos/style.css">

        <script>

        $(function() {
            
          $( "#datepicker0" ).datepicker({ dateFormat: 'dd-mm-yy', changeMonth: true, changeYear: true });
          
          $( "#datepicker1" ).datepicker({ dateFormat: 'dd-mm-yy', changeMonth: true, changeYear: true });
          
          $( "#datepicker2" ).datepicker({ dateFormat: 'dd-mm-yy', changeMonth: true, changeYear: true });
          
          $( "#datepicker3" ).datepicker({ dateFormat: 'dd-mm-yy', changeMonth: true, changeYear: true });
          
          $( "#datepicker4" ).datepicker({ dateFormat: 'dd-mm-yy', changeMonth: true, changeYear: true });
          
          $( "#datepicker5" ).datepicker({ dateFormat: 'dd-mm-yy', changeMonth: true, changeYear: true });
          
        });

        </script>

    </head>

    <body> 
        
        <form name="myform" action="${pageContext.request.contextPath}/EditActiveTermDatesServlet" method="GET">
                    
            <p>  
                
                Selected/active year: ${activeYearString}
            
                <%            
                    
                    String dbYearString = (String) request.getAttribute("activeYearString");
                    //out.print("debyearstring = " + dbYearString);
                    
                    int dbYear = Integer.parseInt(dbYearString);
                    
                    out.print("dbYear = " + dbYear);
                    
                    String winterStart = (String) request.getAttribute("winterStart");
                    
                    //out.print("winter start = " + winterStart);
                    
                    String winterEnd = (String) request.getAttribute("winterEnd");
                    String springStart = (String) request.getAttribute("springStart");
                    String springEnd = (String) request.getAttribute("springEnd");
                    String summerStart = (String) request.getAttribute("summerStart");
                    String summerEnd = (String) request.getAttribute("summerEnd");
                    
                    out.print("<input type='text' id='winterStart' name='winterStart' value='" + winterStart + "'>");
                    out.print("<input type='text' id='winterEnd' name='winterEnd' value='" + winterEnd + "'>");
                    out.print("<input type='text' id='springStart' name='springStart' value='" + springStart + "'>");
                    out.print("<input type='text' id='springEnd' name='springEnd' value='" + springEnd + "'>");
                    out.print("<input type='text' id='summerStart' name='summerStart' value='" + summerStart + "'>");
                    out.print("<input type='text' id='summerEnd' name='summerEnd' value='" + summerEnd + "'>");

                    out.print("<input type='text' id='activeYearString' name='activeYearString' value='" + dbYear + "'>");

                %>
            
            </p>
            
            <p>
                Pick term dates for upcoming year HOPE YOU'VE ENTERED 2015! (format: 'dd-mm-yyyy'):
            </p>
            
            <p>
                Winter start date (current is <% out.print(winterStart); %> ): 
                <input type="text" id="datepicker0" name='datepicker0'>
            </p>

            <p>Winter end date (current is <% out.print(winterEnd); %> ): 
                <input type="text" id="datepicker1" name='datepicker1'></p>

            <p>Spring start date (current is <% out.print(springStart); %> ): 
                <input type="text" id="datepicker2" name='datepicker2'></p>

            <p>Spring end date (current is <% out.print(springEnd); %> ): 
                <input type="text" id="datepicker3" name='datepicker3'></p>

            <p>Summer start date (current is <% out.print(summerStart); %> ): 
                <input type="text" id="datepicker4" name='datepicker4'></p>

            <p>Summer end date (current is <% out.print(summerEnd); %> ): 
                <input type="text" id="datepicker5" name='datepicker5'></p>
            
            <p><input type="submit"/> this button to actually confirm the changes
                        
        </form>
                
        <form action="${pageContext.request.contextPath}/dbms/launch/startservers.jsp">
                
            <input type="submit" value="cancel the changes!"/>
                        
        </form>
                
            
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
               
    
        <p>
    
            <a href="${pageContext.request.contextPath}/dbms/launch/startMainMenu.jsp">Back to main menu</a>
    
        </p>
                
   </body>

</html>