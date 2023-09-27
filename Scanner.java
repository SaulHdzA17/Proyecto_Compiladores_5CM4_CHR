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

        for(int i=0; i<source.length(); i++){
            
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

                        /*while(Character.isDigit(c)){

                        lexema += c;
                        i++;
                        c = source.charAt(i);

                        } */

                        /*Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        lexema = "";
                        estado = 0;
                        tokens.add(t);*/
                        

                    }else if ( Character.isWhitespace(c) ){
                        
                        //Cuadno es espacio en blanco
                        i++;

                    }else if( !Character.isLetterOrDigit(c) ){
                        
                        //Cuado no es letra, digito ni espacio en blanco
                        //Suponemos que es un caracter especial
                        
                        estado = 20; //20 -> caracteres especiales
                        lexema += c;

                    }
                    
                    break;

                case 13:

                    //Estado de letras
                    if( !Character.isLetter(c) ){ //Si es diferente de letra

                        //Comprobamos el caracter
                        estado = 0;
                        //lexema += c; ??
                    
                    }else{

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

                    if( c = '.' ){
                        
                        //Para float
                        lexema += C;
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

                case 20:
                    //Caracter especial

                break;
            }


        }


        return tokens;
    }
}
 