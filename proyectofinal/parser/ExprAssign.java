package parser; //nombre del paquete

import analizadorlexico.Token;;//importamos el analizado lexico

public class ExprAssign extends Expression{//Hereda de Expression
    
    //atributos  de la clase
    final Token name;
    final Expression value;

    ExprAssign(Token name, Expression value) {
        //Unico constructor por parametros
        this.name = name;
        this.value = value;
    }
}
