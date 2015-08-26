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

        - defaultDate: "13-01-1991" });

--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

        <title>Pick term dates</title>

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
        
        <form name="myform" action="${pageContext.request.contextPath}/NewYearTermDatesServlet" method="GET">
            
            <jsp:useBean id="displayInfoBean" type="dbms.DisplayInfoBean" scope="request" />
        
            <p>  
            Selected year: 
            <%            
                String displayYear = displayInfoBean.getPreviousInfo().get("display_year").toString();

                out.print(displayYear); 

                out.print("<input type='hidden' id='displayYear' name='displayYear' value='" 
                              + displayYear + "'>");
            %>
            </p>
            
            <p>
                Pick term dates for upcoming year (format: 'mm-dd-yyyy'): 
            </p>
            
            <p>Winter start date: <input type="text" id="datepicker0" name='datepicker0' value='09-09-2015' required></p>

            <p>Winter end date: <input type="text" id="datepicker1" name='datepicker1' value='11-11-2015' required></p>

            <p>Spring start date: <input type="text" id="datepicker2" name='datepicker2' value='01-01-2016' required></p>

            <p>Spring end date: <input type="text" id="datepicker3" name='datepicker3' value='02-02-2016' required></p>

            <p>Summer start date: <input type="text" id="datepicker4" name='datepicker4' value='03-03-2016' required></p>

            <p>Summer end date: <input type="text" id="datepicker5" name='datepicker5' value='04-05-2016' required></p>
            
            <p><input type="submit"/></p>
            
        </form>
            
        <%

                if (displayInfoBean.getErrorMessage() == null)

                {
                    
                    // nothing!
                }
                else
                {
                    // print out error message in red
                    
                    
                    out.print("<div style='color:red'> " + displayInfoBean.getErrorMessage() + " </div>");

             
                }

            %>
               
    </body>

</html>