package org.apache.jsp.dbms.launchSite;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class startMainMenu_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
      out.write("        <title>Main Menu for IT administrator (for iOS Multimedia App)</title>\n");
      out.write("    </head>\n");
      out.write("    <body>\n");
      out.write("        \n");
      out.write("        <h1>\n");
      out.write("            \n");
      out.write("            Please pick an option from listed below:\n");
      out.write("            \n");
      out.write("        </h1>\n");
      out.write("        \n");
      out.write("            <p>\n");
      out.write("                <a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/StartNewYearServlet\">1. Start New Year form</a>\n");
      out.write("            </p>\n");
      out.write("\n");
      out.write("            <p>\n");
      out.write("                <a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/dbms/displayRecords/beginSelection.jsp\">2. Display records</a>\n");
      out.write("            </p>\n");
      out.write("       \n");
      out.write("            \n");
      out.write("             <p>\n");
      out.write("                <a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/StartChangeActiveYearServlet\">3. Change active year</a>\n");
      out.write("            </p>\n");
      out.write("            \n");
      out.write("            <p>\n");
      out.write("                <a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/dbms/activeYear/wipeAllData.jsp\">4. Wipe all data</a>\n");
      out.write("            </p>\n");
      out.write("            \n");
      out.write("            <p>\n");
      out.write("  \n");
      out.write("                <a href=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/StartEditActiveTermDatesServlet\">5. Edit term dates (for active year)</a>\n");
      out.write("\n");
      out.write("            </p>\n");
      out.write("             \n");
      out.write("        <form action=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/StartDeleteOrEditObjectServlet\">\n");
      out.write("            \n");
      out.write("            <p>\n");
      out.write("               \n");
      out.write("                <input type=\"submit\" id=\"active_students\" name=\"active_students\" value=\"6a. Edit/Delete active students\" />\n");
      out.write("                <input type=\"submit\" id=\"archived_students\" name=\"archived_students\" value=\"6b. Edit/Delete archived students\" />\n");
      out.write("                <input type=\"submit\" id=\"active_classes\" name=\"active_classes\" value=\"6c. Edit/Delete active classes\" />\n");
      out.write("                <input type=\"submit\" id=\"archived_classes\" name=\"archived_classes\" value=\"6d. Edit/Delete archived classes\" />\n");
      out.write("\n");
      out.write("            </p>\n");
      out.write("        \n");
      out.write("        </form>\n");
      out.write("        \n");
      out.write("        <form action=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/StartNewActiveEntityServlet\">\n");
      out.write("            \n");
      out.write("            <p>\n");
      out.write("               \n");
      out.write("                <input type=\"submit\" id=\"Student\" name=\"Student\" value=\"7a. Add new student\" />\n");
      out.write("                <input type=\"submit\" id=\"Class\" name=\"Class\" value=\"7b. Add new class\" />\n");
      out.write("\n");
      out.write("            </p>\n");
      out.write("        \n");
      out.write("        </form>\n");
      out.write("     \n");
      out.write("        <form action=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${pageContext.request.contextPath}", java.lang.String.class, (PageContext)_jspx_page_context, null));
      out.write("/StartNewActiveSupportPlanServlet\">\n");
      out.write("            \n");
      out.write("            <p>\n");
      out.write("               \n");
      out.write("                <input type=\"submit\" id=\"target\" name=\"target\" value=\"8a. Add/Edit/Delete Support Plan Targets\" />\n");
      out.write("                <input type=\"submit\" id=\"level\" name=\"level\" value=\"8b. Add/Edit/Delete Support Plan Levels\" />\n");
      out.write("\n");
      out.write("            </p>\n");
      out.write("        \n");
      out.write("        </form>\n");
      out.write("                \n");
      out.write("    </body>\n");
      out.write("</html>\n");
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
