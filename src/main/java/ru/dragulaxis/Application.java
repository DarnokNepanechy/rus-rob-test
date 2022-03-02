package ru.dragulaxis;

import java.io.*;
import java.sql.SQLException;
import java.util.Properties;

public class Application {
    static int column1;
    static int column2;
    static int column5;
    static int column6;
    static int column7;

    static String mailHost;
    static int mailPort;
    static String mailLogin;
    static String mailPassword;
    static String dbPath;

    static String from;

    public static void main(String[] args) {

        FileInputStream fileInputStream;
        Properties property = new Properties();

        try {
            fileInputStream = new FileInputStream("src/main/resources/config.properties");
            property.load(fileInputStream);

            mailHost = property.getProperty("mailHost");
            mailPort = Integer.parseInt(property.getProperty("mailPort"));
            mailLogin = property.getProperty("mailLogin");
            mailPassword = property.getProperty("mailPassword");
            from = property.getProperty("from");

            column1 = Integer.parseInt(property.getProperty("column1"));
            column2 = Integer.parseInt(property.getProperty("column2"));
            column5 = Integer.parseInt(property.getProperty("column5"));
            column6 = Integer.parseInt(property.getProperty("column6"));
            column7 = Integer.parseInt(property.getProperty("column7"));

            dbPath = property.getProperty("db.path");

        } catch (IOException e) {
            e.printStackTrace();
        }

//        MailClient client = new MailClient(mailHost, mailPort, mailLogin, mailPassword);
//        String filePath = client.downloadCsvFile(from);

        String filePath = "C:\\temporary\\file7883407065055785702.csv";

        DBClient dbClient = new DBClient(column1, column2, column5, column6, column7);
        try {
            CSVHandler.putToDb(dbPath, filePath, dbClient);
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
}
