package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileIn {
    private LinkedList<String> fileAsStringList;

    public FileIn(String fileName) {
        try {
            BufferedReader loadedFile = readFile(fileName);
            fileAsStringList = splitFileByLine(loadedFile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<String> getText() {
        return fileAsStringList;
    }

    private BufferedReader readFile(String fileName) throws IOException {
        return new BufferedReader(new FileReader(fileName));
    }

    private LinkedList<String> splitFileByLine(BufferedReader br) throws IOException {
        LinkedList<String> convertedFile = new LinkedList<>();
        String currentLine;
        while ((currentLine = br.readLine()) != null) {
            convertedFile.add(currentLine);
        }
        return convertedFile;
    }
}
