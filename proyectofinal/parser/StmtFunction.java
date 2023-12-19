package parser;

import analizadorlexico.Token;

import java.util.List;

public class StmtFunction extends Statement {

    /******* PLATILLA de funciones *******/

    final Token name;
    final List<Token> params;
    final StmtBlock body;

    StmtFunction(Token name, List<Token> params, StmtBlock body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

}
