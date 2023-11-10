import java.beans.Expression;
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

    /****** Gramática proyecto final ******/

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
    
    /****** Declaraciones ******/

    //PROGRAM -> DECLARATION
    private void PROGRAM(){

        DECLARATION();

    }


    //DECLARATION -> FUN_DECL DECLARATION | VAR_DECL DECLARATION | STATEMENT DECLARATION | Ɛ
    private void DECLARATION(){

        if(hayErrores) return; //Vereficamos que no haya errores

        
        
        /*switch ( this.preanalisis.tipo ) {
            
            /*Primera proyección DECLARATION -> FUN_DECL DECLARATION
            case FUN:
                
                FUN_DECL();
                DECLARATION();
            
            break;
        
            default:
            break;
        }*/
        
        
        /*Primera proyección DECLARATION -> FUN_DECL DECLARATION */
        if( this.preanalisis.tipo == TipoToken.FUN ){
            
            FUN_DECL();
            DECLARATION();

        /*Tercera proyección DECLARATION -> VAR_DECL DECLARATION */
        }else if( this.preanalisis.tipo == TipoToken.VAR ){

            VAR_DECL();
            DECLARATION();

        /*Cuarta proyección DECLARATION -> STATEMENT DECLARATION */
        }else if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
               || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
               || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
               || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) || ( this.preanalisis.tipo == TipoToken.NULL ) 
               || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN ) /*P(EXPR_STMT)*/
               || ( this.preanalisis.tipo == TipoToken.FOR )  || ( this.preanalisis.tipo == TipoToken.IF )
               || ( this.preanalisis.tipo == TipoToken.PRINT )  || ( this.preanalisis.tipo == TipoToken.RETURN )
               || ( this.preanalisis.tipo == TipoToken.WHILE )  || ( this.preanalisis.tipo == TipoToken.LEFT_BRACE ) /*P(STATEMENT)*/  ){

            STATEMENT();
            DECLARATION();

        }
        /*Quinta proyección  DECLARATION -> Ɛ */
        /*Como aparece Ɛ, no manda error al esta vacío*/

    }


    //FUN_DECL -> fun FUNCTION
    private void FUN_DECL(){

        if(hayErrores) return; //Vereficamos que no haya errores

        match(TipoToken.FUN); 
        FUNCTION();

    }

    //VAR_DECL -> var id VAR_INIT ;
    private void VAR_DECL(){

        if(hayErrores) return; //Vereficamos que no haya errores

        match(TipoToken.VAR);
        match(TipoToken.IDENTIFIER);
        VAR_INIT();
        match(TipoToken.SEMICOLON);

    }

    //VAR_INIT -> = EXPRESSION | Ɛ
    private void VAR_INIT(){

        if(hayErrores) return; //Vereficamos que no haya errores

        /*Primera proyección VAR_INIT -> = EXPRESSION */
        if(this.preanalisis.tipo == TipoToken.EQUAL){
        
            match(TipoToken.EQUAL);
            EXPRESSION();
        
        }

        /*Segunda proyección  VAR_INIT -> Ɛ */
        /*Como aparece Ɛ, no manda error al esta vacío*/
    
    }

    /****** Sentencias ******/

    //STATEMENT -> EXPR_STMT | FOR_STMT | IF_STMT | PRINT_STMT | RETURN_STMT | WHILE_STMT | BLOCK
    private void STATEMENT(){
        
        if(hayErrores) return; //Vereficamos que no haya errores


        /*Primera proyección  STATEMENT -> EXPR_STMT (-> * -> P(EXPR_STMT)  )  */
        if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
         || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
         || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
         || ( this.preanalisis.tipo == TipoToken.STRING )  || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
         || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN )  ){
            
            EXPR_STMT();

        /*Segunda proyección  STATEMENT -> FOR_STMT (-> * -> for)  */
        }else if( this.preanalisis.tipo == TipoToken.FOR ){
            
            FOR_STMT();

        /*Tercera proyección  STATEMENT -> IF_STMT (-> * -> if)  */
        }else if( this.preanalisis.tipo == TipoToken.IF ){

            IF_STMT();

        /*Cuarta proyección  STATEMENT -> PRINT_STMT (-> * -> print)  */
        }else if( this.preanalisis.tipo == TipoToken.RETURN  ){

            PRINT_STMT();

        /*Quinta proyección  STATEMENT -> RETURN_STMT (-> * -> return)  */
        }else if( this.preanalisis.tipo == TipoToken.PRINT ){
            
            RETURN_STMT();

        /*Sexta proyección  STATEMENT -> WHILE_STMT (-> * -> WHILE)  */
        }else if( this.preanalisis.tipo == TipoToken.WHILE ){

           HILE_STMT();

            
        /*Septima proyección  STATEMENT -> BLOCK (-> * -> BLOCK)  */
        }else if( this.preanalisis.tipo == TipoToken.LEFT_BRACE ){

            BLOCK();

        }else{
        
            //De cualquier otro modo se manda error
            hayErrores = true;
            System.out.println("Se esperaba el inicio de una sentencia'");
        
        }

    }

    //EXPR_STMT -> EXPRESSION ;
    private void EXPR_STMT(){

        if(hayErrores) return; //Vereficamos que no haya errores

        /*Primera proyección  STATEMENT -> EXPR_STMT (-> * -> P(EXPR_STMT)  )  */
        if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
         || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
         || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
         || ( this.preanalisis.tipo == TipoToken.STRING )  || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
         || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN ) /*P(EXPR_STMT)*/  ){
            
            EXPRESSION();
            match(TipoToken.SEMICOLON);
        
        }else{
        
            //De cualquier otro modo se manda error
            hayErrores = true;
            System.out.println("Se esperaba una Expreccion");
        
        }

    }

    //FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private void FOR_STMT(){

        if(hayErrores) return; //Vereficamos que no haya errores

        match(TipoToken.FOR);
        match(TipoToken.LEFT_PAREN);
        FOR_STMT_1();
        FOR_STMT_2();
        FOR_STMT_3();
        match(TipoToken.RIGHT_PAREN);
        STATEMENT();

    }

    //FOR_STMT_1 -> VAR_DECL | EXPR_STMT | ;
    private void FOR_STMT_1(){

        if(hayErrores) return; //Vereficamos que no haya errores
        
        /*Primera proyección  FOR_STMT_1 -> VAR_DECL (-> * -> VAR  )  */
        if( this.preanalisis.tipo == TipoToken.VAR ) {
            
            VAR_DECL();
        
        /*Segunda proyección  FOR_STMT_1 -> EXPR_STMT (-> * -> P(EXPR_STMT)  )  */
        }else if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
               || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
               || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
               || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) || ( this.preanalisis.tipo == TipoToken.NULL ) 
               || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN ) /*P(EXPR_STMT)*/  ){

            EXPR_STMT();

        /*Tercera proyección  FOR_STMT_1 -> ; */
        }else if( this.preanalisis.tipo == TipoToken.SEMICOLON ){
            
            match(TipoToken.SEMICOLON);

        }else{
        
            //De cualquier otro modo se manda error
            hayErrores = true;
            System.out.println("Error en la declaracion del FOR");
        
        }
    
    }


    //FOR_STMT_2 -> EXPRESSION; | ;
    private void FOR_STMT_2(){

        if(hayErrores) return; //Vereficamos que no haya errores

        /*Primera proyección  FOR_STMT_2 -> EXPRESSION; (-> * -> P(EXPR_STMT)  )  */
        if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
         || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
         || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
         || ( this.preanalisis.tipo == TipoToken.STRING )  || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
         || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN ) /*P(EXPR_STMT)*/ ){
            
            EXPRESSION();
            match(TipoToken.SEMICOLON);
        
        /*Segunda proyección  FOR_STMT_2 -> ; */
        }else if( this.preanalisis.tipo == TipoToken.SEMICOLON ){
             
            match(TipoToken.SEMICOLON);

        }else{
        
           //De cualquier otro modo se manda error
           hayErrores = true;
           System.out.println("Se esperaba una Expreccion");
        
        }

    }

    //FOR_STMT_3 -> EXPRESSION | Ɛ
    private void FOR_STMT_3(){

        if(hayErrores) return; //Vereficamos que no haya errores

        /*Primera proyección  FOR_STMT_3 -> EXPRESSION (-> * -> P(EXPR_STMT)  )  */
        if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
         || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
         || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
         || ( this.preanalisis.tipo == TipoToken.STRING )  || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
         || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN ) /*P(EXPR_STMT)*/ ){
            
            EXPRESSION();
        
        }

        /*Segunda proyección  FOR_STMT_3 -> Ɛ */
        /*Como aparece Ɛ, no manda error al esta vacío*/

    }
    
    //IF_STMT -> if (EXPRESSION) STATEMENT ELSE_STATEMEN
    private void IF_STMT(){

        if(hayErrores) return; //Vereficamos que no haya errores

        match(TipoToken.IF);
        match(TipoToken.LEFT_PAREN);
        EXPRESSION();
        match(TipoToken.RIGHT_PAREN);
        STATEMENT();
        ELSE_STATEMENT();

    }


    
    //ELSE_STATEMENT -> else STATEMENT | Ɛ
    private void ELSE_STATEMENT(){

        if(hayErrores) return; //Vereficamos que no haya errores

        /*Primera proyección  ELSE_STATEMENT -> else STATEMENT  */
        if( this.preanalisis.tipo == TipoToken.ELSE ){

            match(TipoToken.ELSE);
            STATEMENT();
            
        }

        /*Segunda proyección  ELSE_STATEMENT -> Ɛ */
        /*Como aparece Ɛ, no manda error al esta vacío*/

    }

    //PRINT_STMT -> print EXPRESSION ;
    private void PRINT_STMT(){

        if(hayErrores) return; //Vereficamos que no haya errores
        
        match(TipoToken.PRINT);
        EXPRESSION();
        match(TipoToken.SEMICOLON);

    }
    
    //RETURN_STMT -> return RETURN_EXP_OPC ;
    private void RETURN_STMT(){

        if(hayErrores) return; //Vereficamos que no haya errores
        
        match(TipoToken.RETURN);
        RETURN_EXP_OPC();
        match(TipoToken.SEMICOLON);

    }

    //RETURN_EXP_OPC -> EXPRESSION | Ɛ
    private void RETURN_EXP_OPC(){

        if(hayErrores) return; //Vereficamos que no haya errores

        /*Primera proyección  RETURN_EXP_OPC -> EXPRESSION (-> * -> P(EXPRESSION)  )  */
        if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
         || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
         || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
         || ( this.preanalisis.tipo == TipoToken.STRING )  || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
         || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN ) /*P(EXPRESSION)*/ ){
            
            EXPRESSION();
        
        }

        /*Segunda proyección  RETURN_EXP_OPC -> Ɛ */
        /*Como aparece Ɛ, no manda error al esta vacío*/

    }

    //HILE_STMT -> while ( EXPRESSION ) STATEMENT
    private void HILE_STMT(){

        if(hayErrores) return; //Vereficamos que no haya errores

        match(TipoToken.WHILE);
        match(TipoToken.LEFT_PAREN);
        EXPRESSION();
        match(TipoToken.RIGHT_PAREN);
        STATEMENT();

    }

    //BLOCK -> { DECLARATION }
    private void BLOCK(){

        if(hayErrores) return; //Vereficamos que no haya errores

        match(TipoToken.LEFT_BRACE);
        DECLARATION();
        match(TipoToken.RIGHT_BRACE);

    }

    /****** Expresiones ******/    

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