
package database;

/* 
PLAGIARISM NOTE:
Derived by NRR from code exampled provided as examples by Vilius Audinis
*  Date: November 26th 2014
* 
*/ 

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;

// Class definition
public class DatabaseAccess 
{
    
    /**
     * Method to load JDBC Driver
     */
    public void LoadJDBCDriver()
    {
    
        /** Step 1: Load the JDBC driver. 
             * In order to load a driver, the Class.forName must be used 
             * with a specified classname of the database driver. 
             * Consider a postgres database for example;
             */
        try
        {
            // Make sure the required sql driver exists
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e)
        {
        
            try 
            {
                 // Throw an error if it doesn't
                 throw new ServletException(String.format
                 ("Error: Cannot find JDBC driver... "), e);
            } 
            catch (ServletException ex) 
            {
                 Logger.getLogger(DatabaseAccess.class.getName()).
                                            log(Level.SEVERE, null, ex);
            }
        }
    }
    
       
  
    /** Step 2 & 3: Connection URL specifies the server host, 
             * port and the database name with which to establish a 
             * connection. Note that host and port can be missed out 
             * in some cases where the system defaults are enough. 
             * Once again, consider postgres as the example;
             * "jdbc:postgresql:host:port:databaseName"
     * @return 
     * @throws javax.servlet.ServletException 
             */
     // A method for creating a database connection following Steps 1 to 3
        public Connection getConnection() throws ServletException
    {
        // Using try {...} catch {...} for error control - Step 1
        try
        {
            // Make sure the required sql driver exists
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e)
        {
            // Throw an error if it doesn't
            throw new ServletException(String.format
                        ("Error: Cannot find JDBC driver... "), e);
        }
        
        // Using try {...} catch {...} for error control. Step 2 and 3 
        try
        {
            //System.out.println("we're trying G-man now to connect to database!");
            
            
 ////////////////////////////////////////////////////////////////////////////////////////////////           
           
            /*           
            
            Connection connection = DriverManager.getConnection(
                   "jdbc:postgresql://cmp-14jboss01.cmp.uea.ac.uk:5432/"
                           + "group02","group02","group02");
            */
            
            /* Try and make a connection to the given database (database,
             user, password */
            Connection connection = DriverManager.getConnection(
                   "jdbc:postgresql://cmpmscdbsvr.cmp.uea.ac.uk:5432/gdeeksdb"
                    ,
                    "gdeeks"
                    ,
                    "9dkcbSJX"
            );
   
 //////////////////////////////////////////////////////////////////////////////////////////////// 
             //System.out.println("we've tried G-man!");
            
            /*Development site connections  that may be discarded if not 
             *longer needed for development 
              Connection connection = DriverManager.getConnection(
                  "jdbc:postgresql:"
                + "studentdb","student","dbpassword");
              Connection connection = DriverManager.getConnection(
                  "jdbc:postgresql:"
                 + "postgres","postgres","Vilpostgres"); */
            
             
            // Return the connection once made            
            return (connection);
        }
        catch (SQLException e)
        {
            // Throw an error message if any connection problems occur
            throw new ServletException(String.format(
                    "Error: Database connection failed... "), e);
        }
    }
    
    /** Step 4: Creating statement objects in SQL  - 
     *  This is performed within the Java Beans
     */    
        
   
    /** Step 5a: Run the SQL statement using the database connection
      * Creating a Statement object in Step 4 in the java Bean  object 
      * allows commands and 
      * queries to be sent to the database.     
      * The following  method is for running a given query through a 
      * given connection for retrieving data from DB
     * @param sql
     * @param connection
     * @return 
     * @throws javax.servlet.ServletException */
    public ResultSet runQuery(String sql, Connection connection)
                                                throws ServletException
    {
        // Using try {...} catch {...} for error control
        try
        {
            // Create a statement variable to be used for the sql query
            Statement statement = connection.createStatement();
            
            // Run the query and return the ResultSet
            return(statement.executeQuery(sql));
        }
        catch (SQLException e)
        {
            // Deal with the error if it happens
            throw new ServletException(String.format(
                    "Error: Problem running query... "), e);
        }
    }
    
   /** Step 5b: Run the SQL statement using the database connection
    *  
      * Creating a Statement object in Step 4 in the java Bean  object 
      * allows commands and queries to be sent to the database.     
      * The following  method is for running an insert, update or delete 
      * using the given connection to the DB */
       public boolean runUpdateQuery(String sql, Connection connection)
                                    throws ServletException
    {
        // Using try {...} catch {...} for error control
        try 
        {
            Statement statement;
            statement = connection.createStatement();
           
            // Perform the update
            statement.executeUpdate(sql);
            
            // Completed successfuly
            return true;
        }
        catch (SQLException e) 
        {
            // Problem encountered
            return false;
           
        }
    }
   
     /** Step 6: Results analysis conducted in the controller 
     *  This is performed within the servlets and supported by java beans 
     *  for the model and JSP on the view pages.
     */ 
       
    /**
     * Step 6: Results analysis conducted in the controller 
  This is performed within the servlets and supported by java beans 
  for the model and JSP on the view pages.
     * @param connection
     * @throws java.sql.SQLException
     */
    public void Close(Connection connection) throws SQLException
    {        
           /** Step 7 Close the connection.
             * Whenever no more queries need to be performed and all
             * the results have been processed, the connection to the 
             * database needs to be closed. 
             * Close the database connection once finished 
             */
            connection.close();
        
        try
        {
                   
        }
        catch(Exception e) 
        {
        // Try and deal with any unhandled error here
            System.err.println("Error: " + e);
        }    
    
    }
      
    
}
