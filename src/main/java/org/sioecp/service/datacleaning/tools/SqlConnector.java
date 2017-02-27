package org.sioecp.service.datacleaning.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by marin on 21/02/2017.
 */
public class SqlConnector {

    public String dburl = "jdbc:mysql://localhost:3306/db";
    public String dbuser = "root";
    public String dbpassword = "";

    public SqlConnector(){

        try {
            Class.forName( "com.mysql.jdbc.Driver" );
        } catch ( ClassNotFoundException e ) {

        }
    }

    public boolean importPropertiesFromFile(String filepath){
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(filepath);
            prop.load(input);
            dburl = prop.getProperty("dburl");
            dbuser = prop.getProperty("dbuser");
            dbpassword = prop.getProperty("dbpassword");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                input.close();
            } catch (Exception ignore) {}
        }
        return true;
    }

    private Connection connect(){
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dburl, dbuser, dbpassword);
        }
        catch ( SQLException e ) {
                return null;
        }
        return connection;
    }

    private void disconnect(Connection connection){
        try {
            connection.close();
        } catch ( Exception ignore ) {}
    }

    public int execWrite(String query) {
        Connection connection = connect();
        int status = 1;
        if (connection == null)
            return 1;

        try {
            Statement statement = connection.createStatement();
            status = statement.executeUpdate(query);
        }
        catch (SQLException e){
            return 1;
        }
        finally {
            disconnect(connection);
        }
        return status;
    }

    public List<List<String>> execRead(String query){
        List<List<String>> resList = null;
        Connection connection = connect();
        if (connection == null)
            return null;

        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(query);
            ResultSetMetaData rsmd = result.getMetaData();
            int numCols = rsmd.getColumnCount();
            resList = new ArrayList<>();

            while (result.next()) {
                List<String> row = new ArrayList<>(numCols);
                int i = 1;
                while (i <= numCols) {
                    row.add(result.getString(i++));
                }
                resList.add(row);
            }
        }
        catch (SQLException e){
            return null;
        }
        finally {
            disconnect(connection);
        }
        return resList;
    }
}
