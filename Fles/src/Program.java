import com.insotheo.fles.token.Lexer;
import com.insotheo.fles.token.Token;
import com.insotheo.fles.token.TokenType;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Program {
    public static void main(String[] args){
        try{
            String filePathLine = "";
            if(args.length < 1){
                Scanner scanner = new Scanner(System.in);
                System.out.print("Please, enter the path to the *.fls file: ");
                filePathLine = scanner.nextLine();
                scanner.close();
            }
            else if(args.length == 1){
                filePathLine = args[0];
            }
            Path filePath = Paths.get(filePathLine);

            if(!Files.exists(filePath)){
                throw new FileNotFoundException(String.format("File \"%s\" doesn't exist!", filePathLine));
            }
            if(!filePath.getFileName().toString().toLowerCase().endsWith(".fls")){
                throw new Exception("Provided file is not Fles file!");
            }

            String fileContent = Files.readString(filePath);
//            System.out.println(fileContent);

            //Fles works since here!
            Lexer lexer = new Lexer(fileContent);

            Token token = lexer.next();
            while(true){
                System.out.println(token.value);
                if(token.type == TokenType.EOF){
                    break;
                }
                token = lexer.next();
            }

        }
        catch(IOException e){
            System.err.println(String.format("Error reading the file: %s", e.getMessage()));
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
