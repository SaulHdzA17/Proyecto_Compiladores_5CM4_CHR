
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import analizadorlexico.Scanner;
import analizadorlexico.Token;
import parser.ASDR;

public class Interprete {

    static boolean existenErrores = false;
    
    public static void main(String[] args) throws IOException {
        
        if(args.length > 1) {
            System.out.println("Uso correcto: interprete [archivo.txt]");

            // Convención defininida en el archivo "system.h" de UNIX
            System.exit(64);
        } else if(args.length == 1){
            ejecutarArchivo(args[0]);
        } else{
            ejecutarPrompt();
        }
    
    }

    private static void ejecutarArchivo(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        ejecutar(new String(bytes, Charset.defaultCharset()));

        // Se indica que existe un error
        if(existenErrores) System.exit(65);
    }

    private static void ejecutarPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;){
            System.out.print(">>> ");
            String linea = reader.readLine();
            if(linea == null) break; // Presionar Ctrl + D
            ejecutar(linea);
            existenErrores = false;
        }
    }

    private static void ejecutar(String source) {

        try{                        
                                    //Aqui esta source
            Scanner scanner = new Scanner(source);//Se crea un objeto llamdo Scanner
            List<Token> tokens = scanner.scan();//Se crea un arreglo dinamico de onjetos tipo Token llamado tokens

            for(Token token : tokens){
                //Imprimimos elonjeto token de la Litadinamica <tokens>
                System.out.println(token);
                //System.out.println("Hola");
       
            }

            ASDR parser = new ASDR(tokens);
            parser.parse();
       
        }
       
        catch (Exception ex){
       
            ex.printStackTrace();
       
        }

    }

    /*
    El método error se puede usar desde las distintas clases
    para reportar los errores:
    Interprete.error(....);
     */
    static void error(int linea, String mensaje){
        reportar(linea, "", mensaje);
    }

    private static void reportar(int linea, String posicion, String mensaje){
        System.err.println(
                "[linea " + linea + "] Error " + posicion + ": " + mensaje
        );
        existenErrores = true;
    }

}
