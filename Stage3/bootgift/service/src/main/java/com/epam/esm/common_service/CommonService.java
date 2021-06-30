package com.epam.esm.common_service;

import com.epam.esm.errors.LocalAppException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public interface CommonService<T> {

    Long createFromJson(String jsonString) throws Exception;

    Long create (String... params) throws LocalAppException;

    T readById(String id) throws LocalAppException;

    List<T> readByCriteria(String... params);

    boolean updateFromJson(String id, String jsonString) throws LocalAppException;

    boolean updateField(String id, Map<String, String> params) throws LocalAppException;

    void deleteById(String id) throws LocalAppException;

    boolean fillTable();

    default int getWordsAmount(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter("\\n");
        Random r = new Random();
        int size = 0;
        while (scanner.hasNext()) {
            scanner.next();
            size++;
        }
        scanner.close();
        return size;
    }


    default String getRandomWord(File file, int size) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        String result = "";
        scanner.useDelimiter("\\n");
        Random r = new Random();
        int pos = r.nextInt(size);
        for (int j = 1; j < pos; j++) {
            if (scanner.hasNext()) {
                result = scanner.next();
            } else {
                break;
            }
        }
        scanner.close();
        return result;
    }
}
