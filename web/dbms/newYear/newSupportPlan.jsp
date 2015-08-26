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
        <h1>Hello World!</h1>
        
        <jsp:useBean id="displayInfoBean" type="dbms.DisplayInfoBean" scope="request" />
                        
        <form action="${pageContext.request.contextPath}/NewSupportPlanServlet" method="GET">
           
            <%
          
                String objectType;
                
                try{
                    // target or level
                     objectType = displayInfoBean.getPreviousInfo().getString("object_type");
                     
                     if (objectType == null)
                     {
                         objectType = displayInfoBean.getObjectType();
                     }
                     
                } catch (Exception e)
                {
                    objectType = displayInfoBean.getObjectType();
                }
            
            // new or active year
                    // imported this line from enwentity as i know it works
            String requestedStrategy = (String) request.getAttribute("strategy") ;
            String strategy =  requestedStrategy ;
             
            // get/send on json
            out.print("<input type='text' name='json_object' value='" 
                        + displayInfoBean.getPreviousInfo().toString() + "' />");
              
            
            ArrayList<String> nameArray = displayInfoBean.getListToDisplay();
                    
            %>
                        
                <table>
                    
                 <%
            
                    int booleanListSize = (Integer) displayInfoBean.getBooleanList().size()  ;
                    
                    if (booleanListSize < 1)
                    {
                        out.print("No existing results to display.");
                    }
                    else
                    {
                        out.print("<tr>");
                            out.print("<th><b><i>Order</i></b></th>");
                            out.print("<th><b><i>" + objectType + "</i></b></th>");
                            out.print("<th><b><i>Active?</i></b></th>");
                            out.print("<th><b><i>Permanently delete?</i></b></th>");
                        out.print("</tr>");
                        
                        String isActiveCheckedAttribute ;

                        for (int i = 0; i < nameArray.size(); i++)
                        {
                            
                            Boolean isActiveSoMustBeChecked = displayInfoBean.getBooleanList().get(i);
                            
                            if (isActiveSoMustBeChecked == true)
                            {
                                isActiveCheckedAttribute = " checked ";
                            }
                            else
                            {
                                isActiveCheckedAttribute = " ";
                            }
                            
                            int displayOrderInt = i + 1;

                            out.print("<tr>");
                                out.print("<td><input type='number' min='1' max='99' step='1' "
                                        + "id='order" + i + "' name='order" + i + "' "
                                        + "value='" + displayOrderInt + "' /></td>");
                                out.print("<td>");
                                    out.print(nameArray.get(i));
                                   
                                out.print("</td>");
                                   
                                out.print("<td><input type='checkbox' id='cbox_active_" + i 
                                        + "' name='cbox_active_" + i + "' " 
                                        + isActiveCheckedAttribute + "/></td>");
                                out.print("<td><input type='checkbox' id='cbox_delete_" + i 
                                        + "' name='cbox_delete_" + i + "'/></td>");
                            out.print("</tr>");

                            out.print("<input type=hidden name='item" + i + "' value='"+ nameArray.get(i) +"' />");
                            
                            
                        }

                   
                        
                    }
                    
                    out.print("<input type=text name='list_size' value='"+ nameArray.size() + "' />");
                    
                    out.print("<input type=text name='strategy' value='"+ strategy + "' />");
                    
                %>
                  
                  
                  <tr>
                    <td><b><i>Enter new order position</i></b></td>
                    <td><b><i>Enter new 
                                <% out.print(objectType); %> 
                                            name</i></b></td>
                    <td><b><i>Add to active?</i></b></td>
                    <td><b><i>Add another?</i></b><br></td>
                  </tr>
                  <tr>
                    <%
                        //nb index is always 1 bigger than size, as index starts at 1 not 0
                        int newBiggestIndex = nameArray.size();
                        int newBiggestOrder = nameArray.size() + 1;
                    
                        out.print("<td>" + "<input type='number' id='order" + newBiggestIndex 
                                    + "' name='order" + newBiggestIndex 
                                        + "' maxlength='2' step='1' minvalue=1 maxvalue=100 "
                                    + "value='" + newBiggestOrder + "'");
                        
                        if (booleanListSize < 1)
                        {
                            //make required+
                            out.print(" required");
                        }
                        else
                        {
                            
                            // do nothing        
                        }   
                        
                        out.print(" /> </td>");
                                    
                    %>
                    <td><input type='text' id='new_entity' name='new_entity' maxlength="50" /></td>
                    <td><input type='checkbox' id='new_cbox_active' name='new_cbox_active' /></td>
                    <td><input type='checkbox' id='new_cbox_another' name='new_cbox_another' /></td>
                  </tr>
                </table>
                
                
            <%
        
                /*                
                if success/error message, put it here
                */
                    
                try
                {
                    String errorMessage = (String) request.getAttribute("error_message");
                    if (errorMessage == null || errorMessage.equals(""))
                    {
                        //do nothing
                    }
                    else
                    {

                        out.print("<div style='color:red'> " + errorMessage + " </div>");                    
                        
                    }
                }
                catch (Exception e)
                {
                         out.print("Success/error messages will be printed out here. "
                                 + "Error in determining what to display.");
                }                  

                try
                {
                    String successMessage = (String) request.getAttribute("success_message");
                    if (successMessage == null || successMessage.equals(""))
                    {                     
                       //do nothing             
                    }
                    else
                    {

                        out.print("<div style='color:blue'> " + successMessage + " </div>");                    
                        
                    }
                }
                catch (Exception e)
                {
                         out.print("Success/error messages will be printed out here. "
                                 + "Error in determining what to display.");
                }  
                
            %>
            
            <p>
                
                <input type="submit" id="normal" name='normal' value='Submit' />
                
            </p>
                                    
        </form>
        
    </body>
</html>
