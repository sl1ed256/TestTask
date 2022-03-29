package ru.byme.renue.parser;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;


public class Parser {

    private final int columnNumber;

    public Parser(int columnNumber) {
        this.columnNumber = columnNumber;
    }

    private final TreeMap<String, Long> indexingMap = new TreeMap<>();

    /**
     * Для того, чтобы не искать нужные строки по всему файлу, сначала читаю файл построчно
     * и помещаю в TreeMap следующие данные: ключ - строка индексируемой колонки,
     * значение - позиция строки в файле. Сразу происходит сортировка строчек (стандартно по сравнению строк).
     * Таким образом осуществляется индексация всех строк в файле.
     */
    public void parse() {
        System.out.println("Подождите, идет индексация файла");
        try (var raf = new RandomAccessFile("src/main/resources/airports.dat", "r")) {
            String currentLine;
            int indexForDuplicates = 1;
            while (raf.getFilePointer() < raf.length()) {
                var filePointer = raf.getFilePointer();
                currentLine = raf.readLine();
                String columnValue = currentLine.split(",")[columnNumber - 1].replaceAll("\"", "");
                if (indexingMap.containsKey(columnValue)) {
                    columnValue += indexForDuplicates;
                    indexForDuplicates++;
                }
                indexingMap.put(columnValue, filePointer);
            }
        } catch (IOException e) {
            System.out.println("Could not find file");
        }

        String userInput = readUserInput();

        long start = System.currentTimeMillis();
        var resultMap = searchPosition(userInput);
        printResult(resultMap);
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("Затраченное время на поиск: " + timeElapsed + " мс");
    }

    /**
     * Возвращает мапу, в которой содержится все нужные нам строки, так как данные изначально отсортированы.
     * Нижняя граница - ввод пользователя, верхняя - ввод пользователя с изменненым последним символом.
     */
    private SortedMap<String, Long> searchPosition(String userInput) {
        String endSearch = searchNextString(userInput);
        return indexingMap.subMap(userInput, endSearch);
    }

    /**
     * Данный метод нужен для того, чтобы определить верхнюю границу поиска в TreeMap.
     * Меняет последний символ пользовательского ввода на следующий.
     */
    private String searchNextString(String userInput) {
        var chars = userInput.toCharArray();
        chars[chars.length - 1] = (char) ((int) chars[chars.length - 1] + 1);
        return new String(chars);
    }

    /**
     * Принимаем отсортированную мапу и по каждому значению читаем файл.
     */
    public void printResult(SortedMap<String, Long> resultStrings) {
        resultStrings.values().forEach(this::readStringsFromFile);
        System.out.println("Количество найденных строк: " + resultStrings.size() + ";");
    }

    /**
     * Принимает адрес строки в файле.
     * RandomAccessFile позволяет обратиться сразу к нужной позиции в файле. Печатаем полученные данные.
     */
    private void readStringsFromFile(Long position) {
        try (var raf = new RandomAccessFile("src/main/resources/airports.dat", "r")) {
            raf.seek(position);
            String res = raf.readLine();
            System.out.println(res);
        } catch (IOException e) {
            System.out.println("Could not find file");
        }
    }

    /**
     * Пользовательский ввод искомой строки
     */
    private String readUserInput() {
        System.out.println("Введите строку для поиска");
        return new Scanner(System.in).nextLine();
    }
}
