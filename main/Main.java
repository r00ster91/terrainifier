package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class Main {
    enum Type {
        Grass, Dirt
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Please specify the input file name");
        } else {
            try {
                List<String> lines = read_file_lines(args[0]);

                char grass = get_grass_character(args);
                char dirt = get_dirt_character(args);

                String output = terrainify(lines, enable_colors(args), grass, dirt);

                System.out.print(output);
            } catch (IOException err) {
                System.err.println(err.getMessage());
            }
        }
    }

    // Determines whether colors should be enabled.
    static boolean enable_colors(String[] args) {
        return args.length > 1 && args[1].equals("--colors");
    }

    static char get_grass_character(String[] args) {
        if (args.length > 2) {
            return args[2].charAt(0);
        } else {
            return 'g';
        }
    }

    static char get_dirt_character(String[] args) {
        if (args.length > 3) {
            return args[3].charAt(0);
        } else {
            return 'd';
        }
    }

    public static List<String> read_file_lines(String filename) throws IOException {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));

            return lines;
        } catch (IOException err) {
            throw new IOException("Error reading input file");
        }
    }

    // Builds a terrainified string.
    public static String terrainify(List<String> lines, boolean colors, char grass, char dirt) {
        int max_line_length = get_max_line_length(lines);

        // We know how big the outcome will be so we can preallocate with a capacity
        // to avoid reallocations later on
        int capacity = lines.stream().mapToInt(line -> line.length() + 1 /* Add 1 for the newline */).sum();

        StringBuilder string_builder = new StringBuilder(capacity);

        // This array defines what the objects will be in the next iteration
        Type[] types = new Type[max_line_length];

        lines.forEach(line -> {
            int i = 0;
            for (; i < line.length(); i++) {
                char character = line.charAt(i);
                if (character == ' ') {
                    if (colors) {
                        string_builder.append("  ");
                    } else {
                        string_builder.append(' ');
                    }
                    types[i] = Type.Grass;
                } else {
                    if (types[i] == Type.Grass) {
                        if (colors) {
                            string_builder.append("\033[38;5;34m██");
                        } else {
                            string_builder.append(grass);
                        }
                    } else {
                        if (colors) {
                            string_builder.append("\033[38;5;94m██");
                        } else {
                            string_builder.append(dirt);
                        }
                    }
                    types[i] = Type.Dirt;
                }
            }

            // Fill the rest with grass
            for (; i < max_line_length; i++) {
                types[i] = Type.Grass;
            }

            string_builder.append('\n');
        });

        // Make sure our assumption holds true
        assert string_builder.capacity() == string_builder.length();

        return string_builder.toString();
    }

    public static int get_max_line_length(List<String> lines) {
        String longest_line = Collections.max(lines, Comparator.comparing(String::length));

        return longest_line.length();
    }
}
