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
        
        Q();//Funcion Q (axioma de la gramatica), no terminal con su producciones

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
    
    // Q -> select D from T
    private void Q(){
        
        //Cada palabra reservada (TipoToken;preanalisis.tipo) se debe validad con la funcion "match"
        
        match(TipoToken.SELECT); //Validamos que preanalisis.tipo sea del tipo SELECT
        D();                     //Llamada a la funcion D (No terminal con producciones)
        match(TipoToken.FROM);   //Validamos que preanalisis.tipo sea del tipo From   
        T();                   //Llamada a la funcion T (No terminal con producciones)   
    
    
    }

    // D -> distinct P | P
    private void D(){
        
        if(hayErrores) return; //Vereficamos que no haya errores

        /*Primera proyección D -> distinct P */
        if(preanalisis.tipo == TipoToken.DISTINCT){ //Comparamos que preanalisis.tipo es igual DISTINCT
        
            match(TipoToken.DISTINCT);  //Validamos que preanalisis.tipo sea del tipo DISTINCT
            P();                        //Llamada a la funcion P (No terminal con producciones)
        
        /*Segunda proyección D -> P */
        }else if (preanalisis.tipo == TipoToken.ASTERISCO           //P puede ser *
                || preanalisis.tipo == TipoToken.IDENTIFICADOR) {   //O algun identificador
            
            //Si es alguna de las dos:
            P();//Llamada a la funcion P (No terminal con producciones)
        
        }else{
        
            //De cualquier otro modo se manda error
            hayErrores = true;
            System.out.println("Se esperaba 'distinct' or '*' or 'identificador'");
        
        }
    }

    // P -> * | A
    private void P(){

        if(hayErrores) return; //Vereficamos que no haya errores 

        /*Primera proyección  P -> * */
        if(preanalisis.tipo == TipoToken.ASTERISCO){ //Comparamos que preanalisis.tipo es igual *
        
            match(TipoToken.ASTERISCO); //Validamos que preanalisis.tipo sea del tipo *
        
        /*Segunda proyección  P -> A */
        }else if(preanalisis.tipo == TipoToken.IDENTIFICADOR){ //Comparamos que preanalisis.tipo esa un identificador
            
            A(); //Llamada a la funcion A (No terminal con producciones)
        
        }
        else{
        
            //De cualquier otro modo se manda error
            hayErrores = true;
            System.out.println("Se esperaba '*' or 'identificador'");
        
        }
    }

    // A -> A2 A1
    private void A(){
        
        if(hayErrores) return; //Vereficamos que no haya errores

        A2();//Llamada a la funcion A2 (No terminal con producciones)
        A1();//Llamada a la funcion A1 (No terminal con producciones)
    
    }

    // A2 -> id A3
    private void A2(){

        if(hayErrores) return; //Vereficamos que no haya errores

        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){//Comparamos que preanalisis.tipo sea un identificador
            
            match(TipoToken.IDENTIFICADOR); //Validamos que preanalisis.tipo sea un identificador
            A3();                           //Llamada a la funcion A3 (No terminal con producciones)
        
        }else{
        
            //De cualquier otro modo se manda error
            hayErrores = true;
            System.out.println("Se esperaba un 'identificador'");
        
        }
    }

    // A1 -> ,A | Ɛ
    private void A1(){

        if(hayErrores) return; //Vereficamos que no haya errores

        /*Primera proyección  A1 -> ,A */
        if(preanalisis.tipo == TipoToken.COMA){ //Comparamos que preanalisis.tipo es igual *
            
            match(TipoToken.COMA);  //Validamos que preanalisis.tipo sea del tipo *
            A();                    //Llamada a la funcion A (No terminal con producciones)
        
        }

        /*Segunda proyección  A1 -> Ɛ */
        /*Como aparece Ɛ, no manda error al esta vacío*/
    
    }

    // A3 -> .id | Ɛ
    private void A3(){

        if(hayErrores) return; //Vereficamos que no haya errores

        /*Primera proyección  A3 -> .id */
        if(preanalisis.tipo == TipoToken.PUNTO){ //Comparamos que preanalisis.tipo es igual . 
            
            match(TipoToken.PUNTO);         //Validamos que preanalisis.tipo sea del tipo .
            match(TipoToken.IDENTIFICADOR); //Validamos que preanalisis.tipo sea un identificador
        
        }

        /*Segunda proyección  A3 -> Ɛ */
        /*Como aparece Ɛ, no manda error al esta vacío*/

    }

    //T -> T2T1

    private void T (){
        
        if(hayErrores) return; //Vereficamos que no haya errores

        T2();//Llamada a la funcion T2 (No terminal con producciones)
        T1();//Llamada a la funcion T1 (No terminal con producciones)

    }


    //9. T1 -> ,T | Ɛ
    private void T1 (){

        if(hayErrores) return; //Vereficamos que no haya errores

        //*Primera proyección  T1 -> ,T
        if ( this.preanalisis.tipo == TipoToken.COMA ) { //Comparamos que preanalisis.tipo es igual ,
            
            match( TipoToken.COMA );    //Validamos que preanalisis.tipo sea del tipo ,
            T();                        //Llamada a la funcion T (No terminal con producciones)

        }

        /*Segunda proyección  T1 -> Ɛ */
        /*Como aparece Ɛ, no manda error al esta vacío*/

    }
    
    //T2 -> idT3
    private void T2 (){
        
        if(hayErrores) return; //Vereficamos que no haya errores

        if( this.preanalisis.tipo == TipoToken.IDENTIFICADOR ) { //Comparamos que preanalisis.tipo esa un identificador

            match( TipoToken.IDENTIFICADOR );   //Validamos que preanalisis.tipo sea un identificador
            T3();                               //Llamada a la funcion T3 (No terminal con producciones)

        }else{

            //De cualquier otro modo se manda error
            hayErrores = true;
            System.out.println("Se esperaba un 'identificador'");

        }

    }

    //T3 → id | Ɛ
    private void T3(){

        if(hayErrores) return;  //Vereficamos que no haya errores

         /*Primera proyección  A3 -> .id */
        if(preanalisis.tipo == TipoToken.IDENTIFICADOR){ //Comparamos que preanalisis.tipo es igual . 
            
            match(TipoToken.IDENTIFICADOR); //Validamos que preanalisis.tipo sea un identificador
        
        }

        /*Segunda proyección  A3 -> Ɛ */
        /*Como aparece Ɛ, no manda error al esta vacío*/

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
