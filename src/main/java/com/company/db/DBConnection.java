package com.company.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection =
                    DriverManager.getConnection("jdbc:postgresql://localhost:5432/java_db_database",
                            "java_db_user", "java_db_pswd");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static void createTable() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();

            String sql = "create table if not exists profile(\n" +
                    "\tid serial primary key,\n" +
                    "\tname varchar(20) not null,\n" +
                    "\tsurname varchar(20) not null,\n" +
                    "\tphone varchar(13) not null unique,\n" +
                    "\tpassword varchar not null,\n" +
                    "\tcreated_date timestamp default now(),\n" +
                    "\tstatus varchar(10) not null,\n" +
                    "\trole varchar(10) not null\n" +
                    ");\n" +
                    "create table if not exists card(\n" +
                    "\tid serial primary key,\n" +
                    "\tcard_number varchar(16) unique,\n" +
                    "\texpiration_date date not null,\n" +
                    "\tbalance bigint default 0,\n" +
                    "\tstatus varchar(10) not null,\n" +
                    "\tcreated_date timestamp default now()\n" +
                    ");\n" +
                    "create table if not exists profile_card(\n" +
                    "\tid serial primary key,\n" +
                    "\tcard_number varchar(16),\n" +
                    "\tphone varchar(13),\n" +
                    "\tadded_date timestamp default now(),\n" +
                    "\tvisible_user boolean default true,\n" +
                    "\tunique(card_number,phone)\n" +
                    ");\n" +
                    "create table if not exists terminal(\n" +
                    "\tid serial primary key,\n" +
                    "\tcode varchar(8) unique,\n" +
                    "\taddress varchar not null,\n" +
                    "\tstatus varchar(10) not null,\n" +
                    "\tcreated_date timestamp default now()\n" +
                    ");\n" +
                    "create table if not exists transaction_history(\n" +
                    "\tid serial primary key,\n" +
                    "\tcard_number varchar(16),\n" +
                    "\tamount bigint not null,\n" +
                    "\tterminal_code varchar(8),\n" +
                    "\ttype varchar(10) not null,\n" +
                    "\ttransaction_date timestamp default now()\n" +
                    ");\n" +
                    "insert into profile(name,surname,phone,password,status,role) " +
                    "values('Company','Card','+998777','777','ACTIVE','ADMIN');" +
                    "insert into card(card_number,expiration_date,status) " +
                    "values('8600123477777777','2024-01-01','ACTIVE');" +
                    "insert into profile_card(card_number,phone) " +
                    "values('8600123477777777','998777');" +
                    "insert into terminal(code,address,status) " +
                    "values('12345678','qwerty','ACTIVE');";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.executeUpdate();

        } catch (SQLException | RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException | RuntimeException e) {
                e.printStackTrace();
            }
        }
    }
}
