import java.util.List;

public class ASDR implements Parser{

    //Clase implementada de Parser

    private int i = 0;
    private boolean hayErrores = false; //Para determinar si hay errores
    private Token preanalisis;          //Objeto para analizar
    private final List<Token> tokens;   //Lista de tokens


    public ASDR(List<Token> tokens){
        //Costructor por parametros
        this.tokens = tokens;               //Asiganacion de la lista de tokens
        preanalisis = this.tokens.get(i);   //Le asignamos al objeto preanalisis el token del indice "i"
    }

    @Override
    public boolean parse() { //Metodo sobre escrito de la clase Parser
        
        PROGRAM();//Funcion Q (axioma de la gramatica), no terminal con su producciones

        //Si preanalisis en su atributo tipo es igual al token EOf Y !hayerrores es verdadero
        if( ( preanalisis.tipo == TipoToken.EOF ) && !hayErrores){
            System.out.println("Consulta correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }

    /****** Incio de las funciones de cada no terminal ******/
    
    /****** Estas van ha hacer el trabajo de la recursividad ******/
    
    
    //PROGRAM -> DECLARATION
    private void PROGRAM(){

        DECLARATION();

    }


    //DECLARATION -> FUN_DECL DECLARATION | VAR_DECL DECLARATION | STATEMENT DECLARATION | Ɛ
    private void DECLARATION(){

        if(hayErrores) return;

        /*Primera proyección DECLARATION -> FUN_DECL DECLARATION */
        if( this.preanalisis.tipo == TipoToken.FUN ){
            
            FUN_DECL();
            DECLARATION();

        /*Tercera proyección DECLARATION -> VAR_DECL DECLARATION */
        }else if( this.preanalisis.tipo == TipoToken.VAR ){

            VAR_DECL();
            DECLARATION();

        /*Cuarta proyección DECLARATION -> STATEMENT DECLARATION */
        }else if( this.preanalisis.tipo == TipoToken.EQUAL_EQUAL ){

            STATEMENT();
            DECLARATION();

        }
        /*Quinta proyección  DECLARATION -> Ɛ */
        /*Como aparece Ɛ, no manda error al esta vacío*/

    }


    //FUN_DECL -> fun FUNCTION
    private void FUN_DECL(){
        
    }


    private void match(TipoToken tt){
        
        if(preanalisis.tipo == tt){ //Si preanalisis.tipo es igual al TipoToken pasado por parametro (tt)
        
            i++;                         //Avanzamos uno
            preanalisis = tokens.get(i); //Asiganamos es siguiente token de la lista tokens a preanalisis   
        
        }else{ //Si no es igual
            
            hayErrores = true;                        //Ahora existen errores
            System.out.println("Error encontrado"); //Notificamos en consola
        
        }

    }

}
