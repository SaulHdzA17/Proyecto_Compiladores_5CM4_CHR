package parser;

public class ExprGrouping extends Expression {//Clase que hereda de Expression

    //Atributos finales de la clase
    final Expression expression;//Obtejo tipo Expression

    ExprGrouping(Expression expression) {

        //Unico constructor por parametros de la clase
        this.expression = expression;
    }
}
