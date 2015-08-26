<%-- 
    Document   : latestservlettest
    Created on : 25-Jun-2015, 14:59:35
    Author     : cut14jhu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Main Menu for IT administrator (for iOS Multimedia App)</title>
    </head>
    <body>
        
        <h1>
            
            Please pick an option from listed below:
            
        </h1>
        
            <p>
                <a href="${pageContext.request.contextPath}/StartNewYearServlet">1. Start New Year form</a>
            </p>

            <p>
                <a href="${pageContext.request.contextPath}/dbms/displayRecords/beginSelection.jsp">2. Display records</a>
            </p>
       
            
             <p>
                <a href="${pageContext.request.contextPath}/StartChangeActiveYearServlet">3. Change active year</a>
            </p>
            
            <p>
                <a href="${pageContext.request.contextPath}/dbms/activeYear/wipeAllData.jsp">4. Wipe all data</a>
            </p>
            
            <p>
  
                <a href="${pageContext.request.contextPath}/StartEditActiveTermDatesServlet">5. Edit term dates (for active year)</a>

            </p>
             
        <form action="${pageContext.request.contextPath}/StartDeleteOrEditObjectServlet">
            
            <p>
               
                <input type="submit" id="active_students" name="active_students" value="6a. Edit/Delete active students" />
                <input type="submit" id="archived_students" name="archived_students" value="6b. Edit/Delete archived students" />
                <input type="submit" id="active_classes" name="active_classes" value="6c. Edit/Delete active classes" />
                <input type="submit" id="archived_classes" name="archived_classes" value="6d. Edit/Delete archived classes" />

            </p>
        
        </form>
        
        <form action="${pageContext.request.contextPath}/StartNewActiveEntityServlet">
            
            <p>
               
                <input type="submit" id="Student" name="Student" value="7a. Add new student" />
                <input type="submit" id="Class" name="Class" value="7b. Add new class" />

            </p>
        
        </form>
     
        <form action="${pageContext.request.contextPath}/StartNewActiveSupportPlanServlet">
            
            <p>
               
                <input type="submit" id="target" name="target" value="8a. Add/Edit/Delete Support Plan Targets" />
                <input type="submit" id="level" name="level" value="8b. Add/Edit/Delete Support Plan Levels" />

            </p>
        
        </form>
                
    </body>
</html>
