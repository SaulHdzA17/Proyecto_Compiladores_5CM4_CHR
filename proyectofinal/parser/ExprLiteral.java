package parser;

class ExprLiteral extends Expression {//Clase que hereda de Expression

    /****** Expresiones de valores concretos ******/
    //Atributos finales de la clase
    final Object value;//Obtejo tipo Object

    ExprLiteral(Object value) {

        //Unico constructor por parametros de la clase
        this.value = value;
    }
}
