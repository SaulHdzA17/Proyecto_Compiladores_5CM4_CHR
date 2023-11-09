package mx.ipn.escom.k.parser;

class ExprLiteral extends Expression {//Clase que hereda de Expression

    //Atributos finales de la clase
    final Object value;//Obtejo tipo Object

    ExprLiteral(Object value) {

        //Unico constructor por parametros de la clase
        this.value = value;
    }
}
