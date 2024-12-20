package com.news.backend;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/news_db";
        String username = "root";
        String password = "rkgus1004!";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("MySQL 연결 성공!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}