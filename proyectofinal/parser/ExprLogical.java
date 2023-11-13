package parser;

import analizadorlexico.Token;

public class ExprLogical extends Expression{//Clase que hereda de Expression

    //Atributos finales de la clase
    final Expression left;
    final Token operator;
    final Expression right;

    ExprLogical(Expression left, Token operator, Expression right) {
        //Unico costructor por parametros
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}

