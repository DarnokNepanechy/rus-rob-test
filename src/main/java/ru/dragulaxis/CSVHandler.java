package ru.dragulaxis;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static ru.dragulaxis.DBClient.createTableSQL;

public class CSVHandler {
    static void putToDb(String dbPath, String filePath, DBClient dbClient) throws FileNotFoundException, SQLException {
        File file = new File(filePath);

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        CSVReader reader = new CSVReaderBuilder(new FileReader(file))
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        statement.execute(createTableSQL);

        int i = 1;
        try {
            String[] nextLine;
            statement.execute("DELETE FROM PriceItems;");
            System.out.println("Загрузка новых данных в базу...");
            while ((nextLine = reader.readNext()) != null) {
                statement.execute(dbClient.createSQLRequest(nextLine));
                i++;
            }
        } catch (CsvValidationException | IOException e) {
            e.printStackTrace();
            System.out.println("Остались непрочитанные данные. Не удалось обработать строку: " + i);
        } finally {
            connection.commit();
            System.out.println("Добавлено " + (i - 1) + " полей");
            connection.close();
            statement.close();
        }
    }
}
