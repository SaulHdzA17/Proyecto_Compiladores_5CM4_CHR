package parser;

import analizadorlexico.Token;

public class ExprSet extends Expression{
    //atributos de la clase ExprSet
    final Expression object;
    final Token name;
    final Expression value;

    ExprSet(Expression object, Token name, Expression value) {
        //Unico constructor por parametros
        this.object = object;
        this.name = name;
        this.value = value;
    }
}
