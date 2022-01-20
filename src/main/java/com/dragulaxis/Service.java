package com.dragulaxis;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Scanner;

public class Service {
    private static final String PATH_TO_DB = "src\\main\\resources\\db\\sqliteDB.db";

    public static void main(String[] args) throws FileNotFoundException {
        // путь скачанного файла
        // TODO: как мне получать его имя автоматически?
        File file = new File("C:\\tmp\\tempFile3736023891971900537.tmp");

        // так как в данной таблице .csv разделитель не ',', а ';',
        // то создаём парсер с ';'
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader reader = new CSVReaderBuilder(new FileReader(file)).withCSVParser(parser).build();

        // тест: читает по строке и складываем в массив строк (по 14 строк)
        try (
                Connection connection = DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
                Statement statement = connection.createStatement()
        ) {
            connection.setAutoCommit(false);
            Repository repository = new Repository();
            String[] nextLine = reader.readNext();
            int i = 1;
            while ((nextLine = reader.readNext()) != null) {
                System.out.println(i + " : " + Arrays.toString(nextLine));
                statement.execute(repository.createSQLRequest(i, nextLine));
                i++;
            }
            connection.commit();
        } catch (IOException | CsvValidationException | SQLException e) {
            e.printStackTrace();
        }
    }
}
