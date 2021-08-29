package main.test;

import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import main.Main;

class TestData {
    File[] inputs;
    File[] outputs;
    int length;

    TestData(File[] files1, File[] files2) {
        inputs = files1;
        outputs = files2;

        assert inputs.length == outputs.length;

        length = inputs.length;
    }
}

class Test {
    public static void main(String[] args) {
        test_get_max_line_length();
        test_terrainify();
    }

    static TestData get_test_data() {
        File input_directory = new File("main/test/input");
        File output_directory = new File("main/test/output");

        File[] inputs = input_directory.listFiles();
        File[] outputs = output_directory.listFiles();

        return new TestData(inputs, outputs);
    }

    static void test_terrainify() {
        TestData test_data = get_test_data();

        for (int i = 0; i < test_data.length; i++) {
            try {
                List<String> lines = Main.read_file_lines(test_data.inputs[i].toString());
                String output = Main.terrainify(lines, false, 'g', 'd');

                assert string_is_equal_to_file(output, test_data.outputs[i]);
            } catch (IOException err) {
                System.out.println(err);
            }
        }
    }

    // Returns `true` if the given string is equal to the given file's content.
    static boolean string_is_equal_to_file(String string, File file) throws FileNotFoundException {
        Scanner string_scanner = new Scanner(string);
        Scanner file_scanner = new Scanner(file);

        boolean equal = true;

        while (string_scanner.hasNext() && file_scanner.hasNext()) {
            String string1 = string_scanner.next();
            String string2 = file_scanner.next();

            if (!string1.equals(string2)) {
                equal = false;

                break;
            }
        }

        string_scanner.close();
        file_scanner.close();

        return equal;
    }

    static void test_get_max_line_length() {
        assert Main.get_max_line_length(Arrays.asList("hello", "world")) == 5;
        assert Main.get_max_line_length(Arrays.asList("there", "are", "five", "strings", "here")) == 7;
        assert Main.get_max_line_length(Arrays.asList("", "  ")) == 2;
    }
}
