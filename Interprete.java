/* 
Alumnos   
Castro Rendon Gibrham
    Saúl Hernández Alonso 
    Rendon Cardona Ever
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Interprete {

    static boolean existenErrores = false;
    
    public static void main(String[] args) throws IOException {
        
        FileReader archivo;     //Archivo a leer    
        BufferedReader lector;  //Receptor de cadenas

        try {//Intentamos
            //Buscamos y abrimos el archivos
            archivo = new FileReader("archivo\\Prueba3.txt");
           
            if(archivo.ready()) {
              
                //Se cehca si el archivo esta listo
                lector = new BufferedReader( archivo ); //Si es así le passamos el archivo a lector
                String cadena;//variable para leer cade por cadena
                        
                       //Asignamos cadena del archivo
                while( ( cadena = lector.readLine() ) != null) {//Mientras la cadena no llegue al fin de larchivo llamamos a ejecutar
                    
                    ejecutar(cadena);//funcion que manda a llamar a scan de la Clase Scanner
                           //Le pasamos como caracter cadena ya que esta es la que almacena la lina del archivo

                }

            } else {

                //Si no se puede leer el archivo, mandamo error
                System.out.println("El archivo no está listo para ser leído...");
            
            }

        } catch(Exception e) {

            //Exception para ver errores al intar ejecutar el codigo de arriba
            System.out.println("Error: " + e.getMessage());
        
        }
        
        /*if(args.length > 1) {
            System.out.println("Uso correcto: interprete [archivo.txt]");

            // Convención defininida en el archivo "system.h" de UNIX
            System.exit(64);
        } else if(args.length == 1){
            ejecutarArchivo(args[0]);
        } else{
            ejecutarPrompt();
        }*/
    
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
       
            }
       
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
