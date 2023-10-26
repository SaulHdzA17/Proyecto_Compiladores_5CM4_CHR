package mx.ipn.escom.k.parser;

import mx.ipn.escom.k.tools.Token;

public class ExprGet extends Expression{//Clase que hereda de Expression

    //Atributos finales de la clase
    final Expression object;//Obtejo tipo Expression
    final Token name;//Objeto tipo Token (analizador lexico)

    ExprGet(Expression object, Token name) {

        //Unico constructor por parametros de la clase
        this.object = object;
        this.name = name;
    }
}
