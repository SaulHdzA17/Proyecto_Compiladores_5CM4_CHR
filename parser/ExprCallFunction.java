package mx.ipn.escom.k.parser;

import mx.ipn.escom.k.tools.Token;

import java.util.List;

public class ExprCallFunction extends Expression{//Clase que hereda de Expression
    
    final Expression callee;            //Objeto tipo Expression
    // final Token paren;               //Clase que hereda de Expression
    final List<Expression> arguments;   //Lista de objetos tipo Expression

    ExprCallFunction(Expression callee, /*Token paren,*/ List<Expression> arguments) {
        
        //Unico constructor por parametros de la clase
        this.callee = callee;
        // this.paren = paren;
        this.arguments = arguments;
    }
}
