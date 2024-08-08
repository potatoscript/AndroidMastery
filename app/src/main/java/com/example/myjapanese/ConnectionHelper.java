package com.example.myjapanese;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection con;
    String username, password, ip, port, database;

    @SuppressLint("NewApi")
    public Connection connectionclass() {
        ip = "10.0.2.2";
        database = "tomato";
        username = "tomato";
        password = "tomato";
        port = "5432";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try {
            Class.forName("org.postgresql.Driver"); // PostgreSQL JDBC driver
            ConnectionURL = "jdbc:postgresql://" + ip + ":" + port + "/" + database;
            connection = DriverManager.getConnection(ConnectionURL, username, password);

        } catch (Exception ex) {
            Log.e("Error", ex.getMessage());
        }

        return connection;
    }
}
