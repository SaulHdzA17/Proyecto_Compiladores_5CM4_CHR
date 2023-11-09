package mx.ipn.escom.k.parser;

import mx.ipn.escom.k.tools.Token;

public class ExprSuper extends Expression {
    
    //Clasee heredada de Expression
    // final Token keyword;
    final Token method;

    ExprSuper(Token method) {
        //Unico costructor por parametros
        // this.keyword = keyword;
        this.method = method;
    }
}