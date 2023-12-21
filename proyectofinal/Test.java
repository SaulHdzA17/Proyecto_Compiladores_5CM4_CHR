/*package mx.ipn.escom.k.parser;

//import mx.ipn.escom.k.exception.ParserException;
import analizadorlexico.TipoToken;
import analizadorlexico.Token;

import java.beans.Expression;
import java.util.List;

public class Test{
    
    private final List<Token> tokens;
    private int i = 0;
    private Token preanalisis;



    //PROGRAM -> DECLARATION
    private void program(){
        declaration();

    }

    /****** Declaraciones *****

    //DECLARATION -> FUN_DECL DECLARATION | VAR_DECL DECLARATION | STATEMENT DECLARATION | Ɛ
    

    //TERM -> FACTOR TERM_2
    private void term(){
        factor();   //Llamada a funcion
        term2();    //Llamada a funcion
    }

    //FACTOR -> UNARY FACTOR_2
    private Expression factor(){
        Expression expr = unary(); //Objeto Expression se iguala a la funcion unary
        expr = factor2(expr);      //Objeto Expression se iguala a la funcion factor2 con unary 
        return expr;               //Retorna expr
    }

    //FACTOR_2 -> / UNARY FACTOR_2 | * UNARY FACTOR_2 | Ɛ
    private Expression factor2(Expression expr){ //Un FACTOR_2 puede ser terminal del P(Unary)
        switch (preanalisis.getTipo()){ //Evaluamos en que proyección cae
            case SLASH:                 //Caundo es /
                match(TipoToken.SLASH); //Comprovamos que es /
                Token operador = previous(); //Le asiganamos el token anterior al de la posicion (es /)
                Expression expr2 = unary();  //Asignamos la siguiente fuincion
                ExprBinary expb = new ExprBinary(expr, operador, expr2); //Creamos el conjunto
                return factor2(expb);   //Retornamos el conjunto
            case STAR:                  //Caundo es astedisco
                match(TipoToken.STAR);  //Comprovamos que es *
                operador = previous();  //Le asiganamos el token anterior al de la posicion
                expr2 = unary();        //Asignamos la siguiente fuincion
                expb = new ExprBinary(expr, operador, expr2); //Creamos el conjunto
                return factor2(expb);   //Retornamos el conjunto

            //Como tiene Ɛ no necesita un caso de error
        }
        return expr; //Retornamos el nodo creado
    }

    //UNARY -> ! UNARY | - UNARY | CALL
    private Expression unary(){
        switch (preanalisis.getTipo()){ //Evaluamos en que proyección cae
            case BANG:                  //Caundo es !
                match(TipoToken.BANG);  //Comprovamos que es !
                Token operador = previous();    //Le asiganamos el token anterior al de la posicion  (es !)
                Expression expr = unary();      //Asignamos la siguiente fuincion
                return new ExprUnary(operador, expr); //Asignamos la siguiente fuincion
            case MINUS:                 //Cuando es -
                match(TipoToken.MINUS); //Comprovamos que es -
                operador = previous();  //Le asiganamos el token anterior al de la posicion
                expr = unary();         //Asignamos la siguiente fuincion
                return new ExprUnary(operador, expr); //Creamos y retornamos la ExpreUnary
            default:
                return call();          //De otro modo llamamos su tercera proyección
        }
    }

    //CALL -> PRIMARY CALL_2
    private Expression call(){      
        Expression expr = primary(); //Le asignamos la primera funcion
        expr = call2(expr);          //Le asiganos la siguiente funcion con la funcion anterior expr = call( primary() );
        return expr;                 //Retornamos el nodo
    }

    //CALL_2 -> ( ARGUMENTS_OPC ) CALL_2 | Ɛ
    private Expression call2(Expression expr){
        switch (preanalisis.getTipo()){ //Evaluamos en que proyección cae
            case LEFT_PAREN:            //Cuando es (
                match(TipoToken.LEFT_PAREN);                            //Comprovamos que es (
                List<Expression> lstArguments = argumentsOptional();    //La lista guarda todas las opciones de la funcion
                match(TipoToken.RIGHT_PAREN);                           //Comprovamos que es )
                ExprCallFunction ecf = new ExprCallFunction(expr, lstArguments); //Creamos el nodo
                return call2(ecf);  //Retonamos el nodo
        }
        return expr;
    }


    //PRIMARY -> true | false | null | number | string | id | ( EXPRESSION )
    private Expression primary(){
        switch (preanalisis.getTipo()){//Evaluamos en que proyección cae
            case TRUE:  //Cuando es true
                match(TipoToken.TRUE); //Comprovamos que es true
                return new ExprLiteral(true);   //Retornamos el nodo
            case FALSE: //Cuando es false
                match(TipoToken.FALSE); //Comprovamos que es false
                return new ExprLiteral(false);  //Retornamos el nodo
            case NULL:  //Cuando es null
                match(TipoToken.NULL);  //Comprovamos que es null
                return new ExprLiteral(null);   //Retornamos el nodo
            case NUMBER://Cuando es number
                match(TipoToken.NUMBER);    //Comprovamos que es number
                Token numero = previous();  //Le asiganamos el token anterior al de la posicion  (es )
                return new ExprLiteral(numero.getLiteral());    //Retornamos el nodo
            case STRING://Cuando es string
                match(TipoToken.STRING);    //Comprovamos que es string
                Token cadena = previous();  //Le asiganamos el token anterior al de la posicion  (es )
                return new ExprLiteral(cadena.getLiteral());    //Retornamos el nodo
            case IDENTIFIER://Cuando es idenrificador
                match(TipoToken.IDENTIFIER);//Comprovamos que es un identificador
                Token id = previous();      //Le asiganamos el token anterior al de la posicion  (es )
                return new ExprVariable(id);//Retornamos el nodo
            case LEFT_PAREN://Cuando es (
                match(TipoToken.LEFT_PAREN);    //Comprovamos que es un (
                Expresion expr = expression();  //Le asiganamos la función EXPRESSION
                // Tiene que ser cachado aquello que retorna
                match(TipoToken.RIGHT_PAREN);   //Comprovamos que es un )
                return new ExprGrouping(expr);  //Retornamos el nodo
        }
        return null;
    }


    private void match(TipoToken tt) throws ParserException {
        //Funcion de comprovación
        if(preanalisis.getTipo() ==  tt){
            //Con esta parte avanzamos en la lista
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            String message = "Error en la línea " +
                    preanalisis.getPosition().getLine() +
                    ". Se esperaba " + preanalisis.getTipo() +
                    " pero se encontró " + tt;
            throw new ParserException(message);
        }
    }


    private Token previous() {
        //Retornar el token en la posicion anterior
        return this.tokens.get(i - 1);
    }

    
}
*/