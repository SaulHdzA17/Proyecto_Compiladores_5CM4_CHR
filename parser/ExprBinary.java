package mx.ipn.escom.k.parser;

import mx.ipn.escom.k.tools.Token;

public class ExprBinary extends Expression{//Clase que hereda de Expression
    
    //Atributos finales de la clase
    final Expression left;  //Obtejo tipo Expression
    final Token operator;   //Objeto tipo Token (analizador lexico)
    final Expression right; //Obtejo tipo Expression

    ExprBinary(Expression left, Token operator, Expression right) {

        //Unico constructor por parametros de la clase
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

}
