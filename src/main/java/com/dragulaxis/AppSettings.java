package com.dragulaxis;

public class AppSettings {
    // для доступа приложения к почте нужно
    // 0. загуглить и вести хост и порт почтового клиента
    // 1. разрешить использование IMAP для сторонних клиентов
    // 2. сгенерировать пароль приложения (именно он используется в программе в качестве пароля)

    // ниже приведены настройки по умолчанию для почты test17012022@yandex.ru
    static String MAIL_HOST = "imap.yandex.ru";
    static int MAIL_PORT = 993;
    static String MAIL_LOGIN = "test17012022@yandex.ru";
    static String MAIL_PASSWORD = "qyuyjrnkurpzrhrx";
}
