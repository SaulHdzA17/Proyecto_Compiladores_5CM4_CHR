import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private static final Map<String, TipoToken> palabrasReservadas;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and",    TipoToken.AND);
        palabrasReservadas.put("else",   TipoToken.ELSE);
        palabrasReservadas.put("false",  TipoToken.FALSE);
        palabrasReservadas.put("for",    TipoToken.FOR);
        palabrasReservadas.put("fun",    TipoToken.FUN);
        palabrasReservadas.put("if",     TipoToken.IF);
        palabrasReservadas.put("null",   TipoToken.NULL);
        palabrasReservadas.put("or",     TipoToken.OR);
        palabrasReservadas.put("print",  TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true",   TipoToken.TRUE);
        palabrasReservadas.put("var",    TipoToken.VAR);
        palabrasReservadas.put("while",  TipoToken.WHILE);
    }

    private final String source;

    private final List<Token> tokens = new ArrayList<>();
    
    public Scanner(String source){
        this.source = source + " ";
    }

    public List<Token> scan() throws Exception {
        
        int estado = 0;//Para moverse dentro de los estados
        String lexema = "";//Para almacenar las cadenas
        char c;//Reconocer caracter por caracter

        for(int i = 0; i<source.length(); i++){
            
            c = source.charAt(i);//optenemos el carcater en el index 0

            switch (estado){

                case 0:
                
                    //Caso base
                    if( c == '>'){
                        
                        //Mayor
                        estado = 1;
                        lexema += c;    
                    
                    }else if( c == '<'){
                        
                        //Menor
                        estado = 4;
                        lexema += c;                        
                        
                    }else if( c== '='){

                        //Igual
                        estado = 7;
                        lexema += c;
                        
                    }else if( c== '!'){;
                    
                        //Diferente
                        estado = 10;
                        lexema += c;
                     
                    }else if( c == '(' ){//Creacion de tokens de un solo caracter
                        
                        //Parenticis izquiero
                        lexema += c;
                        Token t = new Token(TipoToken.LEFT_PAREN, lexema, null);
                        tokens.add(t);
                        //Devolvemos los valores a cero
                        estado = 0;
                        lexema = "";

                    }else if( c == ')' ){
                        
                        //Parenticis derecho
                        lexema += c;
                        Token t = new Token(TipoToken.RIGHT_PAREN, lexema, null);
                        tokens.add(t);
                        //Devolvemos los valores a cero
                        estado = 0;
                        lexema = "";

                    }else if( c == '{' ){
                        
                        //Llave izquiero
                        lexema += c;
                        Token t = new Token(TipoToken.LEFT_BRACE, lexema, null);
                        tokens.add(t);
                        //Devolvemos los valores a cero
                        estado = 0;
                        lexema = "";

                    }else if( c == '}' ){
                        
                        //Llave derecho
                        lexema += c;
                        Token t = new Token(TipoToken.RIGHT_BRACE, lexema, null);
                        tokens.add(t);
                        //Devolvemos los valores a cero
                        estado = 0;
                        lexema = "";

                    }else if( c == '.' ){
                        
                        //punto
                        lexema += c;
                        Token t = new Token(TipoToken.DOT, lexema, null);
                        tokens.add(t);//Devolvemos los valores a cero
                        estado = 0;
                        lexema = "";

                    } else if(c == '-') {
                        
                        //Para el signo menos
                        lexema += c;
                        Token t = new Token(TipoToken.MINUS, lexema, null);
                        tokens.add(t);
                        //Devolvemos los valores a cero
                        estado = 0;
                        lexema = "";
                    
                    }else if( c == '+' ) {

                        //Para el +
                        lexema += c;
                        Token t = new Token(TipoToken.PLUS, lexema, null);
                        tokens.add(t);
                        //Devolvemos los valores a cero
                        estado = 0;
                        lexema = "";


                    }else if (c == ';') {
                        
                        //Para el punto y coma
                        lexema += c;
                        Token t = new Token(TipoToken.SEMICOLON, lexema, null);
                        tokens.add(t);
                        //Devolvemos los valores a cero
                        estado = 0;
                        lexema = "";
                        
                    } else if(c == '/') {
                       
                        //Para el slash
                        //System.out.println( "Estado: " + estado + "\nCaracter: "+ c + "\nLexema: " + lexema );
                        lexema += c;
                        estado = 26;
                                               
                    } else if(c == '*') {
                        
                        //Para el asterisco
                        lexema += c;
                        Token t = new Token(TipoToken.STAR, lexema, null);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";

                    } else if(c == '"') {
                        
                        //Estado para guardar las cadenas.
                        lexema += c;
                        estado = 24; 
                    
                    }else if ( Character.isLetter(c) ){

                        //Para indentifar identificadores y palabras reservadas
                        estado = 13;
                        lexema += c;
                       
                    }else if( Character.isDigit(c) ){
                        
                        //Cuando es un difgito
                        estado = 15;
                        lexema +=c;

                    }else if( Character.isWhitespace(c) ){

                        //Para espacios en blanco
                        estado = 0;
                        lexema += c;

                    }

                break;

                case 1:

                    //Estado de para determinar >=
                    if( c == '=' ){
                        //Cuando es >=
                        lexema += c;
                        estado = 2; 
                    }else {
                        //Cuando es >
                        estado = 3;
                        i--;//Retrocedemos para guardar unicamente el anterior
                    }

                break;

                case 2:

                    //Aqui es cuando es >=
                    Token GE = new Token(TipoToken.GREATER_EQUAL, lexema);
                    tokens.add(GE);
                    //Regresamos a valores base
                    estado = 0;
                    lexema = "";
                    i--;

                break;

                case 3:
                    
                    //Cuadno es >
                    Token OG = new Token(TipoToken.GREATER, lexema);
                    tokens.add(OG);
                    //Regresamos a valores base
                    estado = 0;
                    lexema = "";
                    i--;

                break;

                case 4:

                    //Estado de para determinar >=
                    if( c == '=' ){
                        //Cuando es >=
                        lexema += c;
                        estado = 5; 
                    }else {
                        //Cuando es <
                        estado = 6;
                        i--;//Retrocedemos para guardar unicamente el anterior
                    }

                break;

                case 5:

                    //Aqui es cuando es <=
                    Token LE = new Token(TipoToken.LESS_EQUAL, lexema);
                    tokens.add(LE);
                    //Regresamos a valores base
                    estado = 0;
                    lexema = "";
                    i--;

                break;

                case 6:
                    
                    //Cuadno es <
                    Token OL = new Token(TipoToken.LESS, lexema);
                    tokens.add(OL);
                    //Regresamos a valores base
                    estado = 0;
                    lexema = "";
                    i--;

                break;

                case 7:
                    
                    //Estado de para determinar ==
                    if( c == '=' ){
                        //Cuando es ==
                        lexema += c;
                        estado = 8; 
                    }else {
                        //Cuando es <
                        estado = 9;
                        i--;//Retrocedemos para guardar unicamente el anterior
                    }

                break;

                case 8:

                    //Aqui es cuando es ==
                    Token EE = new Token(TipoToken.EQUAL_EQUAL, lexema);
                    tokens.add(EE);
                    //Regresamos a valores base
                    estado = 0;
                    lexema = "";
                    i--;

                break;

                case 9:
                    
                    //Cuadno es =
                    Token OE = new Token(TipoToken.EQUAL, lexema);
                    tokens.add(OE);
                    //Regresamos a valores base
                    estado = 0;
                    lexema = "";
                    i--;

                break;

                case 10:
                    
                    //Estado de para determinar !=
                    if( c == '=' ){
                        //Cuando es !=
                        lexema += c;
                        estado = 11; 
                    }else {
                        //Cuando es !
                        estado = 12;
                        i--;//Retrocedemos para guardar unicamente el anterior
                    }

                break;

                case 11:

                    //Aqui es cuando es !=
                    Token BE = new Token(TipoToken.BANG_EQUAL, lexema);
                    tokens.add(BE);
                    //Regresamos a valores base
                    estado = 0;
                    lexema = "";
                    i--;

                break;

                case 12:
                    
                    //Cuadno es !
                    Token OB = new Token(TipoToken.BANG, lexema);
                    tokens.add(OB);
                    //Regresamos a valores base
                    estado = 0;
                    lexema = "";
                    i--;

                break;

                case 13:
                    
                    //Palabras reservadas e indentificadores
                    if(Character.isLetterOrDigit(c)){
                        
                        //Miestras sela letra o digito, permanece aquí
                        estado = 13;
                        lexema += c;
                    
                    }else {
                        
                        //Cuando ya no lo sea determinamos que es
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if(tt == null){
                            /*  
                                tt es un objeto TipoToken
                                si el lexema no concuerda con alguna palabra reservada
                                entones es un identificadro
                            */
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        
                        } else {
                            
                            //Si concuerda es una palabra reservada reservada
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        
                        }

                        //Devolvemos valores a cero
                        estado = 0;
                        lexema = "";
                        i--;

                    }

                break;

                case 15:
 
                    //Como sabemos que es un digito aplicamos el while
                    if(Character.isDigit(c)){

                        estado = 15;
                        lexema += c;

                    }else if( c == '.' ){
                        
                        //Para float
                        estado = 16;
                        lexema += c;
                                              
                    }else if( c == 'E' ){

                        //Cuando tinene exponente
                        estado = 18;
                        lexema += c;

                    }else{
                        
                       //Creamos token cuando es entero (Estado 22)
                       Token NI = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                       tokens.add(NI);
                       //Regresamos a valores base
                       estado = 0;
                       lexema = "";
                       i--;

                    }

                break;

                case 16:

                //Estado de números
                
                if(Character.isDigit(c)){

                    //Miestras sea un digito se mantiene aquí
                    estado = 17;
                    lexema += c;

                }else{

                    //Error
                    estado = 0;
                    lexema = "";
                
                }

                break;

                case 17:

                    //Parte flotante de numero
                    
                    if( Character.isDigit(c) ){
                      
                        //Mientras sea un digito permanece aquí
                        estado = 17;
                        lexema += c;

                    }else if( c == 'E' ){

                        //Cuando tinene exponente
                        estado = 18;
                        lexema += c;

                    }else{

                        //En otro caso
                        estado = 23;
                        i--;

                    }

                break;

                case 18:
                //Estado de números
                
                if( ( c == '+' ) || ( c == '-' ) || ( c == ' ' ) ){
                    
                    //Contemplamos simbolos
                    estado = 19;
                    lexema += c;
                    
                }else if( Character.isDigit(c) ){
                    
                    //Cuando nuevamente es digito
                    estado = 20;
                    lexema+=c;
                    
                }else{

                    //Otro es error
                    estado = 0;
                    lexema = "";
                    
                }

                break;

                case 19:
                
                    //Unicamente estado de transicion al 20
                    if( Character.isDigit(c) ){
                        estado = 20;
                        lexema += c ;
                    }else{

                        //Otro es error
                        estado = 0;
                        lexema = "";
                    
                    }
                
                    break;

                case 20:
                    //Unicamente digitos
                    
                    if(Character.isDigit(c)){
                
                        //Miestras sea un digito se mantiene aquí
                        estado = 20;
                        lexema += c;
                    
                    }else {
                                               
                        //Cuando es otro
                        estado = 21;
                        i--;

                    }
                
                
                break;

                case 21:

                    //Generación de token para lotante con exponente
                    Token NFE = new Token(TipoToken.NUMBER, lexema, lexema);
                    tokens.add(NFE);
                    //Regresamos a valores base
                    estado = 0;
                    lexema = "";
                    i--;

                break;
                
                case 23:

                    //Generación de token de nuero con parte decimal
                    Token NF = new Token(TipoToken.NUMBER, lexema, Float.valueOf(lexema));
                    tokens.add(NF);                                            
                    //Regresamos a valores base
                    estado = 0;
                    lexema = "";
                    i--;

                break;

                case 24:
                    
                    //Para cadenas de caractes
                    while(c != '"') {
                        lexema += c;
                        i++;
                        c = source.charAt(i);
                        if( c == '\n' ){

                            System.out.println("Error para leer cadena -24");
                            break;

                        }
                    }

                    if( c == '\n' ) break;
                    lexema += c;
                    Token t = new Token(TipoToken.STRING, lexema, lexema.substring(1, lexema.length() - 1));
                    tokens.add(t);
                    //Devolvemos los valores a cero
                    estado = 0;
                    lexema = "";
                    
                break;

                case 26:

                    //Para el caracter /
                    if( c == '*'){
                        
                        //Comentarios multilinea
                        estado = 27;

                    }else if( c == '/'){ 
                        
                        //Para comentarios simples
                        estado = 30;
                    
                    }else{
                        
                        //Divición (estado 32)
                        //System.out.println(lexema);
                        estado = 32;
                        i--;
                        
                    }
                    
                break;

                case 27:

                    //Cometarios multilinea
                    if( c != '*' ){

                        //Mietras sea difente se queda aquí
                        estado = 27;

                    }else{

                        //Cuando es igual, nos movemos al estado 28
                        estado = 28;

                    }

                break;

                case 28:
                    
                    //Estado para los *
                    if( c == '*' ){

                        //Mientras sea igual a *, seguimas aquí
                        estado = 28;

                    }else if( c != '*' ){

                        /*Cuando es difenrete suponemos que es algun otro caracte, 
                        por ende el comentario o a terminado*/
                        estado = 27;

                    }else if( c == '/' ){

                        //Cuando es igual a / es el fin del comentario
                        //Como no genera token devolvemos tosdos lo valores a cero 
                        //Estado 29
                        estado = 0;
                        lexema = "";

                    }

                break;

                case 30:

                    //Estado de cometarios simples
                    if( c != '\n' ){

                        //Mientras sea diferete de salto de linea, permanece aquí
                        estado = 30;
                        
                    }else if( c == '\n' ){

                        //Cuando es igual a '\n' es el fin del comentario
                        //Como no genera token devolvemos tosdos lo valores a cero 
                        //Estado 31
                        estado = 0;
                        lexema = "";

                    }else{

                        System.out.println("Error -30");

                    }
                    
                break;

                case 32:
                    
                    //Para unicamente /
                    Token OS = new Token(TipoToken.SLASH, lexema);
                    tokens.add(OS);
                    estado = 0;
                    lexema = "";

                break;


            }

        }

        return tokens;
    
    }

}