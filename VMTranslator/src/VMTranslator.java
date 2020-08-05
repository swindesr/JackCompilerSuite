import parser.CodeWriter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class VMTranslator {
    public static void main(String[] args) {
        String input = args[0];
        String code = "";
        Path filePath;

        if (isDirectory(input)) {
            CodeWriter cw = new CodeWriter();
            code = code.concat(cw.writeStartupCode());
            String outputName = input.substring(input.indexOf("/") + 1);

            List<String> fileNames = new LinkedList<>();
            try (Stream<Path> walk = Files.walk(Paths.get(input))) {
                List<String> result = walk.map(Path::toString)
                        .filter(f -> f.endsWith(".vm")).collect(Collectors.toList());
                fileNames.addAll(result);
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String fileName : fileNames) {
                code = code.concat(new CodeWriter(fileName).translate());
            }

            filePath = Paths.get(input + "/" + outputName + ".asm");
        } else {
            CodeWriter cw = new CodeWriter(input);
            code = code.concat(cw.translate());
            input = input.substring(0, input.indexOf("."));

            filePath = Paths.get(input + ".asm");
        }

        try {
            Files.write(filePath, Collections.singleton(code), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isDirectory(String input) {
        return !input.contains(".vm");
    }
}
