import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JackCompiler {
    public static void main(String[] args) {
        String input = args[0];    //args[0];
        List<String> fileNames = new LinkedList<>();

        if (input.endsWith(".jack")) {
            fileNames.add(input);
        } else {
            try (Stream<Path> walk = Files.walk(Paths.get(input))) {
                List<String> result = walk.map(Path::toString)
                        .filter(f -> f.endsWith(".jack"))
                        .collect(Collectors.toList());
                fileNames.addAll(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String fileName : fileNames) {
            File file = new File(fileName);
            Tokenizer tk = new Tokenizer(file);
            new CompilationEngine(tk.getAnalyzedTokens(), fileName.substring(0, fileName.indexOf('.')) + ".vm");
        }
    }
}
