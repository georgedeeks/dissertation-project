package database;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author George
 */
public class DatabaseConnectionSingleton {
    
    // singleton pattern variables	
	private static DatabaseConnectionSingleton instance = null;
        
        private static Statement statement;
        
        private static  DatabaseAccess databaseAccess;
        
        private static Connection connection;
        
        
        
    public static Statement getStatementInstance() throws ServletException, SQLException{
        
        if (statement == null)
        {
            
            DatabaseConnectionSingleton dbconnectionsingleton = 
            DatabaseConnectionSingleton.getInstance();
         
            // now there's definitely a statement
            // System.out.println("Had to create a new connection to DB for purposes of having a statement object");
        }
        
        return statement;
    }
        
    /**
     * constructor (of an empty object)
     */
    private DatabaseConnectionSingleton() throws ServletException, SQLException {
    
        initialConnect();
        
    }
	
    /**
     * gets instance, as part of the singleton design pattern
     * @return 
     */
    public static DatabaseConnectionSingleton getInstance() throws ServletException, SQLException {

	if (instance == null) 
        {
            instance = new DatabaseConnectionSingleton();
	}

        return instance;

	}
    
    
    
    
    public static void initialConnect() throws ServletException, SQLException{
        //todo
        
        
            // Create a new instance of Database 
            databaseAccess = new DatabaseAccess();
            
            // Step 1 of 7 Load Driver
            databaseAccess.LoadJDBCDriver();
            // Step 2 and 3 of 7 Connect
            connection = databaseAccess.getConnection();
           
            
            System.out.println("Opened connection to the database!");
        
            statement = connection.createStatement();

            
        
    }
    
    
    public static void closeConnection() throws SQLException{
    
        if 
                (
                    instance != null                
                )
        {
    
            databaseAccess.Close(connection);
            
            System.out.println("Closed connection to the database");
            
            instance = null;
            // are the following three necessary?
            statement = null;
            databaseAccess = null;
            connection = null;
    
        }
        else
        {
            System.out.println("tried to close db connection but instance was null");
        }
        
    }
    
}
