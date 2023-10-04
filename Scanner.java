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
        
        int estado = 0;
        String lexema = "";

        char c;

        for(int i = 0; i<source.length(); i++){
            
            c = source.charAt(i);//optenemos el carcater en el index 0

            switch (estado){
                
                case 0:

                    //Caso base

                    if(  Character.isLetter(c) ){
                        
                        //Cuado es una letra
                        estado = 13;
                        lexema += c;
                    
                    }else if( Character.isDigit(c) ){
                        
                        //Cuando es un digito
                        estado = 15;
                        lexema += c;


                    }else if ( Character.isWhitespace(c) ){
                        
                        //Cuadno es espacio en blanco
                        estado = 0;
                        //i++;

                    }else if( c == '/' ){
                        
                        estado = 31; //Caso para comentarios
                        lexema += c;
                    

                    }else if( c == '!'){
                        estado = 32;
                        lexema += c;    
                    
                      
                    }else if( c == '+'){
                        
                        //Checar otros casos de caracteres especiales
                        Token t = new Token(TipoToken.PLUS, lexema, null);
                        tokens.add(t);
                        
                    }else if( c== '>'){

                        //Checar otros casos de caracteres especiales
                        estado = 33;
                        lexema += c;
                     
                    
                    }else if( c== '<'){;
                    
                        //Checar otros casos de caracteres especiales
                        estado = 34;
                        lexema += c;
                     
                    
                    }else if( c== '='){
                     
                        //Para el signo =
                        estado = 35;
                        lexema += c;
                     

                      
                    }else if( c == '(' ){//Creacion de tokens de un solo caracter
                        //Parenticis izquiero
                        lexema += c;
                        Token t = new Token(TipoToken.LEFT_PAREN, lexema, null);
                        tokens.add(t);


                    }else if( c == ')' ){
                        //Parenticis derecho
                        lexema += c;
                        Token t = new Token(TipoToken.RIGHT_PAREN, lexema, null);
                        tokens.add(t);

                    }else if( c == '{' ){
                        //Llave izquiero
                        lexema += c;
                        Token t = new Token(TipoToken.LEFT_BRACE, lexema, null);
                        tokens.add(t);

                    }else if( c == '}' ){
                        //Llave derecho
                        lexema += c;
                        Token t = new Token(TipoToken.RIGHT_BRACE, lexema, null);
                        tokens.add(t);

                    }else if( c == '.' ){
                        //punto
                        lexema += c;
                        Token t = new Token(TipoToken.DOT, lexema, null);
                        tokens.add(t);

                    } else if(c == '-') {
                        //Para el signo menos
                        lexema += c;
                        Token t = new Token(TipoToken.MINUS, lexema, null);
                        tokens.add(t);
                    } else if (c == ';') {
                        //Para el punto y coma
                        lexema += c;
                        Token t = new Token(TipoToken.SEMICOLON, lexema, null);
                        tokens.add(t);
                    } else if(c == '/') {
                        //Para el slash
                        lexema += c;
                        Token t = new Token(TipoToken.SLASH, lexema, null);
                        tokens.add(t);
                    } else if(c == '*') {
                        //Para el asterisco
                        lexema += c;
                        Token t = new Token(TipoToken.STAR, lexema, null);
                        tokens.add(t);
                    } else if(c == '"') {
                        lexema += c;
                        estado = 36; //Estado para guardar las cadenas.
                    }else{

                        //error
                    }

                    //estado* = 0;
                    //lexema = "";


                    
                    break;

                case 13:
                    if (Character.isLetter(c) || Character.isDigit(c)) {
                        lexema += c;
                        estado = 13;
                    }
                    else{

                        TipoToken tt = palabrasReservadas.get(lexema);

                        if(tt == null){

                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        
                        }else{
                         
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        
                        }

                        estado = 0;
                        lexema = "";
                        i--;

                    }
                    break;

                case 15:
                    
                    //Como sabemos que es un digito aplicamos el while
                    while(Character.isDigit(c)){

                        lexema += c;
                        i++;
                        c = source.charAt(i);

                    }

                    if( c == '.' ){
                        
                        //Para float
                        lexema += c;
                        i++;
                        while(Character.isDigit(c)){

                            lexema += c;
                            i++;
                            c = source.charAt(i);

                        }
                        
                        //Creamos token cuando es flotante
                        Token t = new Token(TipoToken.NUMBER_F, lexema, Float.valueOf(lexema));
                        tokens.add(t);

                    }else{
                        
                        //Creamos token cuando es entero
                        Token t = new Token(TipoToken.NUMBER_INT, lexema, Integer.valueOf(lexema));
                        tokens.add(t);

                    }

                    //Regresamos a valores base
                    estado = 0;
                    lexema = "";
                    i--;

                break;

                case 30:
                    
                                       
                break;

//<<<<<<< Updated upstream
                case 31: 

                    c= source.charAt(i); //Para comentarios de una sola linea
                    
                    if( c== '/'){
                        
                      while( c != '\n' ){
                            c= source.charAt(i);
                            i++;
                          } //! != 

                        
                    }else if( c == '*'){ //Para comentarios multilinea

                        
                        while( i < source.length() ){
                            c= source.charAt(i);
                            i++;
                            if(c=='*') {
                                i++;
                                c= source.charAt(i);
                            }
                        }


                    }

                    estado = 0;
                    lexema = "";
                
                break;

                case 32:    //Caso para diferente de o negación
                    c = source.charAt(i); //Para comentarios de una sola linea
                
                    if(c=='='){    //Primer caso con el "Diferente de".
                        lexema+=c;
                        Token t = new Token(TipoToken.BANG_EQUAL, lexema, null);
                        tokens.add(t);
                    } else {
                        Token t = new Token(TipoToken.BANG, lexema, null);
                        tokens.add(t);
                        i--;
                    }
                    estado = 0;
                    lexema = "";


                break;
                
                case 33:
                    //Caso de >
                    c = source.charAt(i);
                    if ( c == '=' ){
                        
                        lexema += c;
                        Token t = new Token(TipoToken.GREATER_EQUAL, lexema, null);
                        tokens.add(t);

                    }else {
                        
                        Token t = new Token(TipoToken.GREATER, lexema, null);
                        tokens.add(t);
                        i--;

                    }

                    estado = 0;
                    lexema = "";

                break;

                case 34:
                    
                    //Caso de <
                    c = source.charAt(i);
                    
                    if ( c == '=' ){
                       
                        //Cunao tenemeos <=
                        lexema += c;
                        Token t = new Token(TipoToken.LESS_EQUAL, lexema, null);
                        tokens.add(t);

                    }else{
                        //Cuando unicamente es <
                        Token t = new Token(TipoToken.LESS, lexema, null);
                        tokens.add(t);
                        i--;

                    }

                    estado = 0;
                    lexema = "";

                break;

                case 35:
                   
                    //Para el signo =
                    c = source.charAt(i); //Siguiente caracter obtenido

                    if( c == '=' ){                         
                        
                        //Si el siguiente caracter es igual
                        lexema += c;
                        Token t = new Token(TipoToken.EQUAL_EQUAL, lexema, null);
                        tokens.add(t);

                    }else{

                        //Si el siguiente no es un = estinces tenemos solo un token de un solo caracter
                        Token t = new Token(TipoToken.EQUAL, lexema, null);
                        tokens.add(t);
                        i--;

                    }
                    estado = 0;
                    lexema = "";


                break;

                case 36: //Caso para las cadenas
                    c = source.charAt(i);
                    while(c != '"') {
                        lexema += c;
                        i++;
                        c = source.charAt(i);
                    }
                    lexema += c;
                    Token t = new Token(TipoToken.STRING, lexema, lexema.substring(1, lexema.length() - 1));
                    tokens.add(t);
                    estado = 0;
                    lexema = "";
                break;

            }
       
        }

        return tokens;
    
    }

}