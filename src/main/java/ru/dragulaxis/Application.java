package ru.dragulaxis;

import java.io.*;
import java.sql.SQLException;

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

    public static void main(String[] args) {

        String choice = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        MailClient client = new MailClient(mailHost, mailPort, mailLogin, mailPassword);
        String filePath = client.downloadCsvFile(from);

        Repository repository = new Repository(column1, column2, column5, column6, column7);
        try {
            repository.putToDb("sqliteDB.db", filePath);
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
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
        System.out.println("Поставщик: " + from);
        System.out.println();
        System.out.println("Введите соответствующую цифру для команды:");
        System.out.println("1. Поменять номера колонок прайс-листа поставщика. ");
        System.out.println("2. Поменять настройку почты. ");
        System.out.println("3. Сменить поставщика. ");
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
        System.out.print("Пароль (обычно используются пароли приложений): ");
        mailPassword = br.readLine();
        System.out.println();
    }
}
