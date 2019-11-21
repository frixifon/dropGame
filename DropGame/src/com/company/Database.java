package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    public String host;
    public String user;
    public String password;
    public Connection connection;

    public Database(String host, String user, String password) {
        this.host = host;
        this.user = user;
        this.password = password;
    }

    public void addRecord(String name, int score) {
        try {
            String sql = String.format("INSERT INTO kaplja(Name,Score) VALUES('%s','%d')", name, score);
            Statement st = this.connection.createStatement();
            st.executeUpdate(sql);
            st.close();
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    public ArrayList<String> getRecords() {
        ArrayList result = new ArrayList();

        try {
            Statement st = this.connection.createStatement();
            ResultSet res = st.executeQuery("SELECT * FROM kaplja");

            while(res.next()) {
                int score = res.getInt(3);
                String name = res.getString(2);
                String date = res.getString(4);
                result.add(name + ": " + score);
            }

            res.close();
            st.close();
        } catch (Exception var7) {
        }

        return result;
    }

    public void init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            this.connection = DriverManager.getConnection(this.host, this.user, this.password);
            System.out.println("Connection to Store DB succesfull!");
        } catch (Exception var2) {
            System.out.println("Connection failed...");
            System.out.println(var2);
            var2.printStackTrace();
        }

    }
}
