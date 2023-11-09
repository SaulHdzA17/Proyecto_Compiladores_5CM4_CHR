import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Principal {

    static boolean existenErrores = false;//Para determinar si la cadena es valida o no

    public static void main(String[] args) throws IOException {
        ejecutarPrompt();//Llamada a la ejecucion desde consola
    }

    private static void ejecutarPrompt() throws IOException{
        //Fincion para ejecutar desde consola
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;){
            System.out.print(">>> ");
            String linea = reader.readLine();//Lectura de la linea ingresada desde consola
            if(linea == null) break; // Presionar Ctrl + D
            ejecutar(linea);//
            existenErrores = false;
        }
    }

    private static void ejecutar(String source){
        Scanner scanner = new Scanner(source);//Guarda la linea de la consolsa en scanner
        List<Token> tokens = scanner.scanTokens();//Lista tipo token llamada "tokens"; Agrega el token a la lista 

        /*for(Token token : tokens){
            System.out.println(token);
        }*/

        Parser parser = new ASDR(tokens);//Objeto parse se crea como un objeto ASDR
        parser.parse();// ".parse();" Funcion booleana
    }

    /*
    El método error se puede usar desde las distintas clases
    para reportar los errores:
    Interprete.error(....);
     */
    static void error(int linea, String mensaje){
        reportar(linea, "", mensaje);
    }

    private static void reportar(int linea, String donde, String mensaje){
        System.err.println(
                "[linea " + linea + "] Error " + donde + ": " + mensaje
        );
        existenErrores = true;
    }

}