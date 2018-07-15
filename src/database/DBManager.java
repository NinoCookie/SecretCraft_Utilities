package database;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.*;

public class DBManager {
    Connection connection;
    Statement statement;
    ResultSet rs;
    public DBManager() {

    }
    public void FileConnection(Plugin plugin) throws Exception{
        File file = new File(plugin.getDataFolder() + File.separator + "database.db");
        if (!file.exists()) {
            file.createNewFile();
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + file);
    }

    public void MySQLConnection(String hostname, String port, String database, String username, String password) throws SQLException{
        String url="jdbc:mysql://" + hostname + ":" + port + "/" + database;
        connection = DriverManager.getConnection(url, username, password);
    }

    public boolean createtable() throws SQLException{
        String sql = "create table if not exists taxsystemplayers(uuid varchar(36) primary key, taxamount double(20,0))";
        statement=connection.createStatement();
        statement.executeUpdate(sql);
        return true;
    }

    public void registerplayer(String uuidx) throws SQLException{
        Bukkit.getConsoleSender().sendMessage(uuidx);
        String sql="INSERT INTO taxsystemplayers(uuid,taxamount) VALUES ('"+uuidx+"',0)";
        statement=connection.createStatement();
        statement.executeUpdate(sql);
    }
    public boolean contains(String sql) throws SQLException {
        statement=connection.createStatement();
        rs=statement.executeQuery(sql);
        if(rs.next()){
            return rs.getInt(1) >= 1;
        }
        return false;
    }
    public void addmoney(String uuidx, double x) throws SQLException{
        double amount=0.0;
        statement=connection.createStatement();
        String sql = "select * from taxsystemplayers where uuid='" + uuidx + "'";
        rs=statement.executeQuery(sql);
        if(rs.next()){
            amount=rs.getDouble(2);
        }
        amount+=x;
        sql="update taxsystemplayers set taxamount ='"+amount+"'where uuid='"+uuidx+"'";
        statement=connection.createStatement();
        statement.executeUpdate(sql);
    }
    public double usertaxes(String uuidx) throws SQLException{
        double amount=0.0;
        String sql = "select * from taxsystemplayers where uuid='" + uuidx + "'";
        statement=connection.createStatement();
        rs=statement.executeQuery(sql);
        if(rs.next()){
            amount=rs.getDouble(2);
        }
        return amount;
    }
    public double alltaxes()throws SQLException{
        double amount=0.0;
        String sql="SELECT * FROM taxsystemplayers";
        statement=connection.createStatement();
        rs=statement.executeQuery(sql);
        while(rs.next()){
            amount+=rs.getDouble(2);
        }
        return amount;
    }
    public void close() throws SQLException{
        if(rs!=null){
            rs.close();
        }
        if(statement!=null){
            statement.close();
        }
        if(connection!=null){
            connection.close();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public Statement getStatement() {
        return statement;
    }
}
