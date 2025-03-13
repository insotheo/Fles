import com.insotheo.fles.SourceFile;

import java.util.Scanner;

public class Program {
    public static void main(String[] args){
        try{
            String filePathLine = "";
            if(args.length < 1){
                Scanner scanner = new Scanner(System.in);
                System.out.print("Please, enter the path to the *.fls file with main function: ");
                filePathLine = scanner.nextLine();
                scanner.close();
            }
            else if(args.length == 1){
                filePathLine = args[0];
            }

            SourceFile mainSource = new SourceFile(filePathLine);

        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
