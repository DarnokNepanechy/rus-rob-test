package ru.dragulaxis;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class MailClient {
    private final String mailHost;
    private final int mailPort;
    private final String mailLogin;
    private final String mailPassword;

    MailClient(String mailHost, int mailPort, String mailLogin, String mailPassword) {
        this.mailHost = mailHost;
        this.mailPort = mailPort;
        this.mailLogin = mailLogin;
        this.mailPassword = mailPassword;
    }

    String downloadCsvFile(String from) {

        System.out.println("Подготовка...");

        Properties emailProperties = new Properties();
        emailProperties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Session session = Session.getDefaultInstance(emailProperties);

        try (Store store = session.getStore("imap")) {

            // создаём директорию и файл, куда сохраним загруженное с почты
            File folder = new File("C:/temporary");
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = File.createTempFile("file", ".csv", folder);
            System.out.println("Временный файл находится здесь: " + file.getAbsolutePath());

            // коннектимся к почте и берём оттуда сообщения
            store.connect(mailHost, mailPort, mailLogin, mailPassword);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);
            Message[] messages = inbox.getMessages();

            // рассматривем самые последние сообщения, присланные от поставщика
            // потому что в них, предположительно, актуальный прайс-лист
            for (int i = messages.length - 1; i >= 0 ; i--) {

                // смотрим первый адрес, убираем <почта>, пробел в конце и сравниваем с тем, что указано в параметрах
                String addressLine = messages[i].getFrom()[0].toString();
                if (addressLine.substring(0, addressLine.indexOf("<")).trim().equals(from)) {
                    Multipart multipart = (Multipart) messages[i].getContent();

                    // просматриваем все части сообщения
                    for (int j = 0; j < multipart.getCount(); j++) {
                        MimeBodyPart content = (MimeBodyPart) multipart.getBodyPart(j);

                        // и если есть файл .csv
                        if (content.getFileName() != null && content.getFileName().endsWith(".csv")) {

                            // то записываем его в файл
                            FileOutputStream outputStream = new FileOutputStream(file);
                            InputStream inputStream = content.getInputStream();
                            byte[] buffer = new byte[4096];
                            int byteRead;
                            System.out.println("Скачивается...");
                            while ((byteRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, byteRead);
                            }
                            outputStream.close();
                            inputStream.close();
                            inbox.close();

                            return file.getAbsolutePath();
                        }
                    }
                }
            }

            System.out.println("Почты от \"" + from + "\" нет.");

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}