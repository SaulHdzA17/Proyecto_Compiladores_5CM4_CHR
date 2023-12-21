package parser;

import java.util.List;

public class ExprGrupFor extends Expression {//Clase creada para el FOR_STMT

    /****** Para un grupo de expreciones o parametros *****/
    //Atributos finales de la clase
    final Expression f1;//Obtejo tipo Expression
    final Expression f2;
    final Expression f3;

    ExprGrupFor(List<Expression> pramatros) {

        this.f1 = pramatros.get(0);
        this.f2 = pramatros.get(1);
        this.f3 = pramatros.get(2);

        //Unico constructor por parametros de la clase
        
    }
}
