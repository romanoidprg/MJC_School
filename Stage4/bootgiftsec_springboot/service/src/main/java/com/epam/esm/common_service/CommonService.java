package com.epam.esm.common_service;

import com.epam.esm.errors.LocalAppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public interface CommonService<T> {

    long createFromJson(String jsonString) throws Exception;

    Long create (String... params) throws LocalAppException;

    T readById(String id) throws LocalAppException;

    Page<T> readByCriteria(Pageable pageable, String... params) throws LocalAppException;

    boolean updateFromJson(String id, String jsonString) throws LocalAppException;

    boolean updateField(String id, Map<String, String> params) throws LocalAppException;

    void deleteById(String id) throws LocalAppException;

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
