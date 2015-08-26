package org.apache.jsp.dbms.newYear;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.util.ArrayList;

public final class chooseNewYear_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("  \n");
      out.write("\n");
      out.write(" \n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <title>Enter new year</title>\n");
      out.write("        <meta charset=\"UTF-8\">\n");
      out.write("        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
      out.write("   <!--    \n");
      out.write("   <link rel=\"stylesheet\" href=\"style.css\"> \n");
      out.write("    --> \n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        \n");
      out.write("        ");
      dbms.DisplayInfoBean displayInfoBean = null;
      displayInfoBean = (dbms.DisplayInfoBean) _jspx_page_context.getAttribute("displayInfoBean", PageContext.REQUEST_SCOPE);
      if (displayInfoBean == null){
        throw new java.lang.InstantiationException("bean displayInfoBean not found within scope");
      }
      out.write("\n");
      out.write("        \n");
      out.write("        <div>\n");
      out.write("            \n");
      out.write("            Enter the year (i.e. 4 digits) that you would like the new academic year to start in (e.g., for 2015-2016, please type in \"2015\"): \n");
      out.write("            \n");
      out.write("            <form name='myform' action=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/NewYearServlet\" method=\"GET\"> \n");
      out.write("                \n");
      out.write("                <p>\n");
      out.write("                    Note:\n");
      out.write("                    <input type=\"text\" id=\"year_list\" name=\"year_list\" value=");
      out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((dbms.DisplayInfoBean)_jspx_page_context.findAttribute("displayInfoBean")).getActiveYear())));
      out.write(" />\n");
      out.write("                </p>\n");
      out.write("\n");
      out.write("                <p>\n");
      out.write("                    The current active year is: \n");
      out.write("                    \n");
      out.write("                    ");
 
                
                        int minusOne = displayInfoBean.getActiveYear() - 1;
                        
                        out.print(minusOne + "-" + displayInfoBean.getActiveYear() );
 
                    
      out.write(" \n");
      out.write("                    \n");
      out.write("                    <input type=\"text\" id=\"active_year\" name=\"active_year\" value=");
      out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((dbms.DisplayInfoBean)_jspx_page_context.findAttribute("displayInfoBean")).getActiveYear())));
      out.write(" />\n");
      out.write("                </p>\n");
      out.write("\n");
      out.write("                <p>\n");
      out.write("                    The most modern year in the database is: \n");
      out.write("                    \n");
      out.write("                    ");
 
                
                        int subtractOne = displayInfoBean.getNewestYear() - 1;
                        
                        out.print(subtractOne + "-" + displayInfoBean.getNewestYear() );
 
                    
      out.write(" \n");
      out.write("                    \n");
      out.write("                    <input type=\"text\" id=\"newest_year\" name=\"newest_year\" value=");
      out.write(org.apache.jasper.runtime.JspRuntimeLibrary.toString((((dbms.DisplayInfoBean)_jspx_page_context.findAttribute("displayInfoBean")).getNewestYear())));
      out.write(" />\n");
      out.write("                </p>\n");
      out.write("                <p>\n");
      out.write("                    (Please ensure you pick a new year that is no more than one greater than the newest established academic year in the system.)\n");
      out.write("                    \n");
      out.write("                </p>\n");
      out.write("                \n");
      out.write("                ");

                
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
                
      out.write("\n");
      out.write("                \n");
      out.write("                <input type=\"text\" id=\"startYear\" name=\"startYear\"  maxlength=\"4\" value=\"");
 out.print(displayInfoBean.getNewestYear()); 
      out.write("\"/> \n");
      out.write("                <input type=\"submit\"/>\n");
      out.write("                \n");
      out.write("            </form>     \n");
      out.write("            \n");
      out.write("            ");


                if (displayInfoBean.getErrorMessage() == null || displayInfoBean.getErrorMessage().equals(""))

                {
                    
                    // nothing!
                }
                else
                {
                    // print out error message in red
                    
                    
                    out.print("<div style='color:red'> " + displayInfoBean.getErrorMessage() + " </div>");

             
                }

            
      out.write("\n");
      out.write("            \n");
      out.write("        </div>\n");
      out.write("        \n");
      out.write("    </body>\n");
      out.write("</html>\n");
      out.write("\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
