package gxj.study.util;

import gxj.study.demo.datastruct.hash.ZipBloomFilter;
import gxj.study.demo.datastruct.hash.ZipHash;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author xinjie_guo
 * @date 2022/5/13 18:17
 */
public class FileUtils {

    public static void readLine(String fileName, Consumer<String> lineHandler) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                lineHandler.accept(line);
            }
        }
    }

    public static void readLine(String fileName, BiConsumer<String, Integer> lineHandler) throws FileNotFoundException {
        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            int i = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                lineHandler.accept(line, i);
                i++;
            }
        }
    }

    public static String readFileToString(String fileName) throws FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                sb.append(line);
            }
        }
        return sb.toString();
    }

}
