import com.insotheo.fles.SourceFile;

import java.util.Scanner;

public class Program {
    public static void main(String[] args){
        try{
            if(args.length == 0){
                System.out.println("Welcome to Fles!");
                return;
            }

            else if(args.length == 1){
                String command = args[0];
                switch (command){
                    case "--version": System.out.println("Fles version: 0.0.0.1"); break;

                    case "help":
                    case "h":
                    case "--help":
                    case "-h":
                        System.out.println("""
                                Fles get help:
                                "help" or "h" or "--help" or "-h": get help
                                
                                "run <path_to_main_source_file>": runs your code
                                "run_clear <path_to_main_source_file>": runs your code without banner
                                
                                parse <path_to_main_source_file>: check your code for lexer or parser errors
                                """);
                        break;

                    case "run": run(args, false); break;
                    case "run_clear": run(args, true); break;
                    case "parse": parse(args); break;

                    default: throw new Exception("Unknown command!");
                }
            }

            else{
                String command = args[0];

                if(command.equals("run")){
                    run(args, false);
                }
                else if(command.equals("run_clear")){
                    run(args, true);
                }

                else if(command.equals("parse")){
                    parse(args);
                }
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void run(String[] args, boolean isClear) throws Exception{
        String filePathLine = "";

        if(args.length < 2){
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please, enter the path to the *.fls file with the main function: ");
            filePathLine = scanner.nextLine();
            scanner.close();
        }

        else if(args.length == 2){
            filePathLine = args[1];
        }

        SourceFile mainSource = new SourceFile(filePathLine);

        if(mainSource.isParsingSuccess()) {
            if (!isClear) {
                System.out.println();
                System.out.println(String.format("==========FLES(%s)==========", mainSource.getFileName()));
            }

            mainSource.run();
        }
    }

    private static void parse(String[] args) throws Exception{
        String filePathLine = "";

        if(args.length < 2){
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please, enter the path to the *.fls file with the main function: ");
            filePathLine = scanner.nextLine();
            scanner.close();
        }

        else if(args.length == 2){
            filePathLine = args[1];
        }

        SourceFile mainSource = new SourceFile(filePathLine);

        if(mainSource.isParsingSuccess()){
            System.out.println(String.format("\u001B[32m" + "Fles message(about %s): Success!" + "\u001B[0m", mainSource.getFileName()));
        }
        else{
            System.err.println(String.format("Fles message(about %s): Failed!", mainSource.getFileName()));
        }
    }
}
