<html>
    <head>
        <title>error</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- 
<link rel="stylesheet" href="style.css">
-->

</head>
<body>         
<div id="ErrorOuter">
    <div id="ErrorDetails">
        <p>We're sorry, there's been a problem connecting to the page.</p><br>
        <p>Please try again later!</p>           
    </div>
</div>
    
        ${error_message}
         
        <form action="${pageContext.request.contextPath}/StartSiteServlet">
            
            <p>
               
                <input type="submit" value="Back to main menu" />

            </p>
        
        </form>
             
</body>
</html>