import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.io.IOException;

public class Day1 {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(
            Path.of("../inputs/2020/1.txt")
            );
        for(String numA : lines) {
            for(String numB : lines) {
                int a = Integer.parseInt(numA);
                int b = Integer.parseInt(numB);
                // every pair (numA, numB)
                // is right here
                if(a + b == 2020) {
                    // here
                    System.out.println("a:" + a + " b:" + b);
                    System.out.println(a * b);
                    return;
                }
            }
        }
    }
}