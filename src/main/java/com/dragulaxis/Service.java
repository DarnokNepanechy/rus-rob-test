package com.dragulaxis;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Service {
    public static void main(String[] args) {
        Reader in;
        Iterable<CSVRecord> records = null;
        String[] HEADERS = {"Номенклатура", "Описание"};

        try {
            in = new FileReader("C:/files-from-email/file10340224668235556898.tmp");
            records = CSVFormat.DEFAULT
                    .withHeader(HEADERS)
                    .withFirstRecordAsHeader()
                    .parse(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (CSVRecord record : records) {
            String nom = record.get("Номенклатура");
            String dis = record.get("Описание");
            System.out.println(nom + " :::: " + dis);
            System.out.println();
        }
    }
}
