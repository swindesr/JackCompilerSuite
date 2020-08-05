package io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileOut {
    public FileOut(List<String> parsedInstructions, String fileName) {
        Path file = Paths.get(fileName);
        try {
            Files.write(file, parsedInstructions, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
