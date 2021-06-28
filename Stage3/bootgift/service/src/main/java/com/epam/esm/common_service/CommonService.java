package com.epam.esm.common_service;

import com.epam.esm.errors.NoSuchIdException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public interface CommonService<T> {

    Long createFromJson(String jsonString) throws Exception;

    T readById(String id) throws NoSuchIdException;

    List<T> readByCriteria(String... params);

    boolean updateFromJson(String jsonString);

    boolean deleteById(String id);

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
