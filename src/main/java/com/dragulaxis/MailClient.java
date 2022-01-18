package com.dragulaxis;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.search.FlagTerm;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static com.dragulaxis.AppSettings.*;

public class MailClient {
    // на всякий с параметром, так как несколько поставщиков
    static void download(String from) {
        try {
            // создаём директорию и файл, куда сложим загруженное с почты
            File folder = new File("C:/files-from-email");
            if (!folder.exists()) {
                folder.mkdir();
            }
            File file = File.createTempFile("file", null, folder);

            // доавляем настройки
            Properties emailProperties = new Properties();
            emailProperties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            // создаём сессию
            Session session = Session.getDefaultInstance(emailProperties);
            Store store = session.getStore("imap");

            // коннектимся к почте
            store.connect(MAIL_HOST, MAIL_PORT, MAIL_LOGIN, MAIL_PASSWORD);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            // ищем все непрочитанные сообщения
            Flags seen = new Flags(Flags.Flag.SEEN);
            FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
            Message[] messages = inbox.search(unseenFlagTerm);

            // сначала последние сообщения смотрим, там как там же актуальная информация
            // TODO: не правильно, надо все просмотреть и отметить прочитанными, а затем взять самыое последнее письмо из них
            // потому что второй раз может скачать неактуальную информацию
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
                            // TODO: разобраться, почему именно так
                            FileOutputStream outputStream = new FileOutputStream(file);
                            InputStream inputStream = content.getInputStream();
                            byte[] buffer = new byte[4096];
                            int byteRead;
                            while ((byteRead = inputStream.read(buffer)) != -1) {
                                outputStream.write(buffer, 0, byteRead);
                            }
                            outputStream.close();
                            return;
                        }
                    }
                }
            }

            System.out.println("Новой почты от " + from + " нет.");

        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}