package ru.dragulaxis;

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

public class Application {
    static int column1 = 1;
    static int column2 = 10;
    static int column5 = 3;
    static int column6 = 6;
    static int column7 = 8;

    static String mailHost = "imap.yandex.ru";
    static int mailPort = 993;
    static String mailLogin = "test17012022@yandex.ru";
    static String mailPassword = "qyuyjrnkurpzrhrx";

    static String from = "Hello World";

    public static void main(String[] args) throws IOException, SQLException {

        String choice = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (!choice.equals("4")){
            info();
            choice = br.readLine();
            switch (choice) {
                case ("1") -> tableSetting(br);
                case ("2") -> mailSetting(br);
                case ("3") -> {
                    System.out.print("Присылатель: ");
                    from = br.readLine();
                }
                case ("4") -> br.close();
                case ("5") -> {
                    br.close();
                    return;
                }
            }
        }

        MailClient client = new MailClient(mailHost, mailPort, mailLogin, mailPassword);
        String filePath = client.downloadCsvFile(from);

        assert filePath != null;
        File file = new File(filePath);

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        CSVReader reader = new CSVReaderBuilder(new FileReader(file))
                .withSkipLines(1)
                .withCSVParser(parser)
                .build();

        String PATH_TO_DB = "src\\main\\resources\\db\\sqliteDB.db";
        Repository repository = new Repository(column1, column2, column5, column6, column7);
        repository.createTable();

        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + PATH_TO_DB);
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();

        int i = 1;
        try {
            String[] nextLine;
            statement.execute("DELETE FROM PriceItems;");
            System.out.println("Загрузка данных в базу...");
            while ((nextLine = reader.readNext()) != null) {
                statement.execute(repository.createSQLRequest(nextLine));
                i++;
            }
        } catch (CsvValidationException e) {
            System.out.println("Не удалось обработать строку: " + i);
            e.printStackTrace();
        } finally {
            connection.commit();
            connection.close();
            statement.close();
            System.out.println("Добавлено " + (i - 1) + " полей");
        }
        connection.commit();
    }

    static void info() {
        System.out.println("Текущие настройки прайс-листа (нумерация с 0):");
        System.out.println("Номер колонки \"Бренд\": " + column1);
        System.out.println("Номер колонки \"Каталожный номер\": " + column2);
        System.out.println("Номер колонки \"Описание\": " + column5);
        System.out.println("Номер колонки \"Цена, руб.\": " + column6);
        System.out.println("Номер колонки \"Наличие\": " + column7);
        System.out.println();
        System.out.println("Текущие настройки почты:");
        System.out.println("Хост: " + mailHost);
        System.out.println("Порт: " + mailPort);
        System.out.println("Логин: " + mailLogin);
        System.out.println();
        System.out.println("Присылает почту: " + from);
        System.out.println();
        System.out.println("Введите соответствующую цифру для команды:");
        System.out.println("1. Настройка прайс-листа поставщика. ");
        System.out.println("2. Настройка почты. ");
        System.out.println("3. Сменить присылателя. ");
        System.out.println("4. Начать выполнение с текущими настройками! ");
        System.out.println("5. Выйти из программы. ");
        System.out.println();
    }

    static void tableSetting(BufferedReader br) throws IOException {
        System.out.println();
        System.out.println("Настройка прайс-листа поставщика: ");
        System.out.print("Номер колонки \"Бренд\": ");
        column1 = Integer.parseInt(br.readLine());
        System.out.print("Номер колонки \"Каталожный номер\": ");
        column2 = Integer.parseInt(br.readLine());
        System.out.print("Номер колонки \"Описание\": ");
        column5 = Integer.parseInt(br.readLine());
        System.out.print("Номер колонки \"Цена, руб.\": ");
        column6 = Integer.parseInt(br.readLine());
        System.out.print("Номер колонки \"Наличие\": ");
        column7 = Integer.parseInt(br.readLine());
        System.out.println();
    }

    static void mailSetting(BufferedReader br) throws IOException {
        System.out.println();
        System.out.println("Настройка почты: ");
        System.out.print("Хост: ");
        mailHost = br.readLine();
        System.out.print("Порт: ");
        mailPort = Integer.parseInt(br.readLine());
        System.out.print("Логин: ");
        mailLogin = br.readLine();
        System.out.print("Пароль: ");
        mailPassword = br.readLine();
        System.out.println();
    }
}
