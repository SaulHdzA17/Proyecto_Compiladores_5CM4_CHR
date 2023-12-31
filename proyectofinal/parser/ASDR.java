package parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import analizadorlexico.TipoToken;
import analizadorlexico.Token;


public class ASDR implements Parser{

    //Clase implementada de Parser

    private int i = 0;
    private boolean hayErrores = false; //Para determinar si hay errores
    private Token preanalisis;          //Objeto para analizar
    private final List<Token> tokens;   //Lista de tokens

    public ASDR(List<Token> tokens){
        //Costructor por parametros
        this.tokens = tokens;               //Asiganacion de la lista de tokens
        preanalisis = this.tokens.get(i);   //Le asignamos al objeto preanalisis el token del indice "i"
    }

    /****** Gramática proyecto final ******/

    @Override
    public boolean parse() throws Exception{ //Metodo sobre escrito de la clase Parser
        
        PROGRAM();//Funcion Q (axioma de la gramatica), no terminal con su producciones

        //Si preanalisis en su atributo tipo es igual al token EOf Y !hayerrores es verdadero
        if( ( preanalisis.tipo == TipoToken.EOF ) && !hayErrores){
            System.out.println("Consulta correcta");
            return  true;
        }else {
            System.out.println("Se encontraron errores");
        }
        return false;
    }

    /****** Incio de las funciones de cada no terminal ******/
    
    /****** Estas van ha hacer el trabajo de la recursividad ******/
    
    /****** Declaraciones ******/

    //PROGRAM -> DECLARATION
    private List<Statement> PROGRAM() throws Exception{

        List<Statement> pro = new ArrayList<>();
        DECLARATION(pro);

        return pro;
    }


    //DECLARATION -> FUN_DECL DECLARATION | VAR_DECL DECLARATION | STATEMENT DECLARATION | Ɛ
    private void DECLARATION(List<Statement> pro)throws Exception{/*Puede retornar una lista*/

        //if(hayErrores) return; //Vereficamos que no haya errores

        /*Primera proyección DECLARATION -> FUN_DECL DECLARATION */
        if( this.preanalisis.tipo == TipoToken.FUN ){
            
            Statement fun = FUN_DECL();
            pro.add(fun);
            DECLARATION(pro);

        /*Segunda proyección DECLARATION -> VAR_DECL DECLARATION */
        }else if( this.preanalisis.tipo == TipoToken.VAR ){

            pro.add(VAR_DECL());
            DECLARATION(pro);

        /*Tercera proyección DECLARATION -> STATEMENT DECLARATION */
        }else if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
               || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
               || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
               || ( this.preanalisis.tipo == TipoToken.STRING ) || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
               || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN ) /*P(EXPR_STMT)*/
               || ( this.preanalisis.tipo == TipoToken.FOR )  || ( this.preanalisis.tipo == TipoToken.IF )
               || ( this.preanalisis.tipo == TipoToken.PRINT )  || ( this.preanalisis.tipo == TipoToken.RETURN )
               || ( this.preanalisis.tipo == TipoToken.WHILE )  || ( this.preanalisis.tipo == TipoToken.LEFT_BRACE ) /*P(STATEMENT)*/  ){

            Statement stmt = STATEMENT();
            pro.add(stmt);
            DECLARATION(pro);

        }
        /*Cuarta proyección  DECLARATION -> Ɛ */
        /*Como aparece Ɛ, no manda error al esta vacío*/

    }


    //FUN_DECL -> fun FUNCTION
    private Statement FUN_DECL() throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion FUN_DECL"); //Vereficamos que no haya errores

        match(TipoToken.FUN); 
        return FUNCTION();

    }

    //VAR_DECL -> var id VAR_INIT ;
    private Statement VAR_DECL() throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion VAR_DECL"); //Vereficamos que no haya errores

        match(TipoToken.VAR);
        match(TipoToken.IDENTIFIER);
        Token id = this.previous();
        Expression expr = VAR_INIT();
        match(TipoToken.SEMICOLON);
        return new StmtVar(id, expr);

    }

    //VAR_INIT -> = EXPRESSION | Ɛ
    private Expression VAR_INIT() throws Exception{/*Puede ser una exprecion unaria*/

        //if(hayErrores) throw new Exception("Error en la funcion VAR_INIT"); //Vereficamos que no haya errores

        Expression assig;

        /*Primera proyección VAR_INIT -> = EXPRESSION */
        if(this.preanalisis.tipo == TipoToken.EQUAL){
        
            match(TipoToken.EQUAL);
            assig = EXPRESSION();
            return assig;
        
        }

        return null;

        /*Segunda proyección  VAR_INIT -> Ɛ */
        /*Como aparece Ɛ, no manda error al esta vacío*/
    
    }

    /****** Sentencias ******/

    //STATEMENT -> EXPR_STMT | FOR_STMT | IF_STMT | PRINT_STMT | RETURN_STMT | WHILE_STMT | BLOCK
    private Statement STATEMENT() throws Exception{
        
        //if(hayErrores) return; //Vereficamos que no haya errores

        /*Primera proyección  STATEMENT -> EXPR_STMT (-> * -> P(EXPR_STMT)  )  */
        if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
         || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
         || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
         || ( this.preanalisis.tipo == TipoToken.STRING )  || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
         || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN )  ){
            
            return EXPR_STMT();

        /*Segunda proyección  STATEMENT -> FOR_STMT (-> * -> for)  */
        }else if( this.preanalisis.tipo == TipoToken.FOR ){
            
            return FOR_STMT();

        /*Tercera proyección  STATEMENT -> IF_STMT (-> * -> if)  */
        }else if( this.preanalisis.tipo == TipoToken.IF ){

            return IF_STMT();

        /*Cuarta proyección  STATEMENT -> PRINT_STMT (-> * -> print)  */
        }else if( this.preanalisis.tipo == TipoToken.PRINT  ){

            return PRINT_STMT();

        /*Quinta proyección  STATEMENT -> RETURN_STMT (-> * -> return)  */
        }else if( this.preanalisis.tipo == TipoToken.RETURN ){
            
            return RETURN_STMT();

        /*Sexta proyección  STATEMENT -> WHILE_STMT (-> * -> WHILE)  */
        }else if( this.preanalisis.tipo == TipoToken.WHILE ){

           return WHILE_STMT();

        /*Septima proyección  STATEMENT -> BLOCK (-> * -> BLOCK)  */
        }else if( this.preanalisis.tipo == TipoToken.LEFT_BRACE ){
            
            List<Statement> block = new ArrayList<>();
            return BLOCK(block);

        }else{
        
            //De cualquier otro modo se manda error
            hayErrores = true;
            throw new Exception("Error en la funcion STATEMENT");
        
        }

    }

    //EXPR_STMT -> EXPRESSION ;
    private Statement EXPR_STMT() throws Exception{

        //if(hayErrores) return; //Vereficamos que no haya errores

        /*Primera proyección  STATEMENT -> EXPR_STMT (-> * -> P(EXPR_STMT)  )  */
        if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
         || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
         || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
         || ( this.preanalisis.tipo == TipoToken.STRING )  || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
         || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN ) /*P(EXPR_STMT)*/  ){
            
            Expression expr = EXPRESSION();
            match(TipoToken.SEMICOLON);
            return new StmtExpression(expr);
        
        }else{
        
            //De cualquier otro modo se manda error
            hayErrores = true;
            throw new Exception("Error en la funcion EXPR_STMT");
        
        }

    } 

    //FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private Statement FOR_STMT() throws Exception{

        //if(hayErrores) return; //Vereficamos que no haya errores

        match(TipoToken.FOR);
        match(TipoToken.LEFT_PAREN);
        Statement decl = FOR_STMT_1();//Declaración
        Expression cond = FOR_STMT_2();//Una binaria logica
        Expression inc = FOR_STMT_3();//Un incremento
        match(TipoToken.RIGHT_PAREN);
        Statement body = STATEMENT();


        if(inc != null){
            body = new StmtBlock(Arrays.asList(body, new StmtExpression(inc)));
        }

        if(cond == null){
            cond = new ExprLiteral(true);
        }
        body = new StmtLoop(cond, body);

        if(decl != null){
            body = new StmtBlock(Arrays.asList(decl, body));
        }

        return body;

    }

    //FOR_STMT_1 -> VAR_DECL | EXPR_STMT | ;
    private Statement FOR_STMT_1() throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion FOR_STMT_2"); //Vereficamos que no haya errores
        
        /*Primera proyección  FOR_STMT_1 -> VAR_DECL (-> * -> VAR  )  */
        if( this.preanalisis.tipo == TipoToken.VAR ) {
            
            return VAR_DECL();
        
        /*Segunda proyección  FOR_STMT_1 -> EXPR_STMT (-> * -> P(EXPR_STMT)  )  */
        }else if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
               || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
               || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
               || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) || ( this.preanalisis.tipo == TipoToken.NULL ) 
               || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN ) /*P(EXPR_STMT)*/  ){

            return EXPR_STMT();

        /*Tercera proyección  FOR_STMT_1 -> ; */
        }else if( this.preanalisis.tipo == TipoToken.SEMICOLON ){
            
            match(TipoToken.SEMICOLON);
            return null;

        }else{
        
            //De cualquier otro modo se manda error
            hayErrores = true;
            throw new Exception("Error en la funcion FOR_STMT_1"); 
        
        }
    
    }


    //FOR_STMT_2 -> EXPRESSION; | ;
    private Expression FOR_STMT_2() throws Exception{

       // if(hayErrores) throw new Exception("Error en la funcion FOR_STMT_2"); //Vereficamos que no haya errores

        /*Primera proyección  FOR_STMT_2 -> EXPRESSION; (-> * -> P(EXPR_STMT)  )  */
        if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
         || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
         || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
         || ( this.preanalisis.tipo == TipoToken.STRING )  || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
         || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN ) /*P(EXPR_STMT)*/ ){
            
            Expression expr = EXPRESSION();
            match(TipoToken.SEMICOLON);
            return expr;
        
        /*Segunda proyección  FOR_STMT_2 -> ; */
        }else if( this.preanalisis.tipo == TipoToken.SEMICOLON ){
             
            match(TipoToken.SEMICOLON);
            return null;

        }else{
           //De cualquier otro modo se manda error
           hayErrores = true;
           throw new Exception("Error en la funcion FOR_STMT_2"); 
        
        }

    }

    //FOR_STMT_3 -> EXPRESSION | Ɛ
    private Expression FOR_STMT_3() throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion FOR_STMT_3"); //Vereficamos que no haya errores

        /*Primera proyección  FOR_STMT_3 -> EXPRESSION (-> * -> P(EXPR_STMT)  )  */
        if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
         || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
         || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
         || ( this.preanalisis.tipo == TipoToken.STRING )  || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
         || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN ) /*P(EXPR_STMT)*/ ){
            
            return EXPRESSION();
        
        }

        return null;

        /*Segunda proyección  FOR_STMT_3 -> Ɛ */
        /*Como aparece Ɛ, nos manda error al estar vacío*/

    }
    
    //IF_STMT -> if (EXPRESSION) STATEMENT ELSE_STATEMEN
    private Statement IF_STMT()throws Exception{

        /*CHECAR*/
        //if(hayErrores) throw new Exception("Error en la funcion IF_STMT"); //Vereficamos que no haya errores

        match(TipoToken.IF);
        match(TipoToken.LEFT_PAREN);
        Expression cond = EXPRESSION();
        match(TipoToken.RIGHT_PAREN);
        Statement thenIf = STATEMENT();
        Statement thenElse = ELSE_STATEMENT();
        

        return new StmtIf(cond, thenIf, thenElse);

    }
    
    //ELSE_STATEMENT -> else STATEMENT | Ɛ
    private Statement ELSE_STATEMENT() throws Exception{
        /*CHECAR*/

        //if(hayErrores) throw new Exception("Error en la funcion ELSE_STMT"); //Vereficamos que no haya errores

        /*Primera proyección  ELSE_STATEMENT -> else STATEMENT  */
        if( this.preanalisis.tipo == TipoToken.ELSE ){

            match(TipoToken.ELSE);
            return STATEMENT();
        }
        return null;

        /*Segunda proyección  ELSE_STATEMENT -> Ɛ */
        /*Como aparece Ɛ, nos manda error al estar vacío*/

    }

    //PRINT_STMT -> print EXPRESSION ;
    private Statement PRINT_STMT() throws Exception{
        /*CHECAR*/

        //if(hayErrores) throw new Exception("Error en la funcion PRINT_STMT"); //Vereficamos que no haya errores
        
        match(TipoToken.PRINT);
        Expression expr = EXPRESSION();
        match(TipoToken.SEMICOLON);
        return new StmtPrint(expr);

    }
    
    //RETURN_STMT -> return RETURN_EXP_OPC ;
    private Statement RETURN_STMT() throws Exception{

        /*CHECAR*/

        //if(hayErrores) throw new Exception("Error en la funcion RETURN_STMT"); //Vereficamos que no haya errores
        
        match(TipoToken.RETURN);
        Expression re = RETURN_EXP_OPC();
        match(TipoToken.SEMICOLON);
        return new StmtReturn(re);

    }

    //RETURN_EXP_OPC -> EXPRESSION | Ɛ
    private Expression RETURN_EXP_OPC() throws Exception{
        /*CHECAR*/

        ///f(hayErrores) throw new Exception("Error en la funcion RETURN_EXP_OPC"); //Vereficamos que no haya errores

        /*Primera proyección  RETURN_EXP_OPC -> EXPRESSION (-> * -> P(EXPRESSION)  )  */

        if( ( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
        || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
        || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
        || ( this.preanalisis.tipo == TipoToken.STRING )  || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
        || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN ) /*P(EXPRESSION)*/ ){
           
           return EXPRESSION();
       
       }
        
        return null;
        
        /*Segunda proyección  RETURN_EXP_OPC -> Ɛ */
        /*Como aparece Ɛ, nos manda error al estar vacío*/

    }

    //WHILE_STMT -> while ( EXPRESSION ) STATEMENT
    private Statement WHILE_STMT() throws Exception{

        /*CHECAR*/

        //if(hayErrores) throw new Exception("Error en la funcion WHILE_STMT"); //Vereficamos que no haya errores

        match(TipoToken.WHILE);
        match(TipoToken.LEFT_PAREN);
        Expression expr = EXPRESSION();
        match(TipoToken.RIGHT_PAREN);
        Statement body = STATEMENT();

        return new StmtLoop(expr, body);

    }

    //BLOCK -> { DECLARATION }
    private StmtBlock BLOCK( List<Statement> state ) throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion BLOCK"); //Vereficamos que no haya errores

        match(TipoToken.LEFT_BRACE);
        state = new ArrayList<>();
        DECLARATION(state);
        match(TipoToken.RIGHT_BRACE);

        return new StmtBlock(state);

    }

    /****** Expresiones ******/    

    //EXPRESSION -> ASSIGNMENT
    private Expression EXPRESSION() throws Exception {

        //Retorna una Asiganción
        //if(hayErrores) throw new Exception("Error en la funcion Expression"); //Vereficamos que no haya errores

        return ASSIGNMENT();
    }

    //ASSIGNMENT -> LOGIC_OR ASSIGNMENT_OPC
    private Expression ASSIGNMENT() throws Exception {

        /*CHECAR */

        //if(hayErrores) throw new Exception("Error en la funcion ASSIGNMENT"); //Vereficamos que no haya errores

        Expression expr = LOGIC_OR();
        return ASSIGNMENT_OP(expr);

    
    }

    //ASSIGNMENT_OPC -> = EXPRESSION | Ɛ
    private Expression ASSIGNMENT_OP( Expression assiOP )throws Exception{
        
        //if(hayErrores) throw new Exception("Error en la funcion FASSIGNMENT_OP"); //Vereficamos que no haya errores

        //Primera producción: ASSIGNMENT_OPC -> = EXPRESSION
        if((this.preanalisis.tipo == TipoToken.EQUAL)) {

            if(assiOP instanceof ExprVariable){
                Token t = ((ExprVariable) assiOP).name;

                match(TipoToken.EQUAL);
                Expression exprAssign = EXPRESSION();

                return new ExprAssign(t, exprAssign);

            }
            else{
                throw new Exception("Error en la funcion ASSIGNMENT_OP");
            }            
        }

        return assiOP;
        //Segunda producción: ASSIGNMENT_OPC -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/
    }

    //LOGIC_OR -> LOGIC_AND LOGIC_OR_2
    
    private Expression LOGIC_OR()throws Exception{

        /*CHECAR JUNTO CON PARAMETERS_OPC PARAMETERS ARGUMENTS_OPC*/

        //if(hayErrores) throw new Exception("Error en la funcion LOGIC_OR"); //Vereficamos que no haya errores

        Expression logAnd = LOGIC_AND();
        return LOGIC_OR_2(logAnd);
        
        
    }

    //LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2 | Ɛ //LISTA DE LOGIC_AND (LOGIC_AND or LOGIC_AND)
    private Expression LOGIC_OR_2(Expression left )throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion LOGIC_OR_2"); //Vereficamos que no haya errores
        
        Token or;
        Expression right;

        //Primera producción: LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2
        if ((this.preanalisis.tipo == TipoToken.OR)) {
            match(TipoToken.OR);
            or = this.previous();
            right = LOGIC_AND();
            Expression newLeft = new ExprLogical(left, or, right);
            return LOGIC_OR_2(newLeft);
        }
        //Segunda producción: LOGIC_OR_2 -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/

        return left;

    }

    //LOGIC_AND -> EQUALITY LOGIC_AND_2
    private Expression LOGIC_AND()throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion LOGIC_AND"); //Vereficamos que no haya errores

        Expression equ = EQUALITY(); 
        return LOGIC_AND_2(equ);

    }

    //LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2 | Ɛ
    private Expression LOGIC_AND_2(Expression left)throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion LOGIC_AND_2"); //Vereficamos que no haya errores

        Token opl;
        Expression right;

        //Primera producción: LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2
        if((this.preanalisis.tipo == TipoToken.AND)) {
            match(TipoToken.AND);
            opl = this.previous();
            right = EQUALITY(); 
            Expression newLeft = new ExprLogical(left, opl, right);
            return LOGIC_AND_2(newLeft);
        }
        //Segunda producción: LOGIC_AND_2 -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/
        return left;
    }

    //EQUALITY -> COMPARISON EQUALITY_2
    private Expression EQUALITY()throws Exception{

        /*CHECAR*/
        //if(hayErrores) throw new Exception("Error en la funcion EQUALITY"); //Vereficamos que no haya errores

        Expression comp = COMPARISON();
        return EQUALITY_2(comp);
        
    }

    //EQUALITY_2 -> != COMPARISON EQUALITY_2 | == COMPARISON EQUALITY_2 | Ɛ
    private Expression EQUALITY_2(Expression left)throws Exception{

        /*CHECAR*/

        //if(hayErrores) throw new Exception("Error en la funcion EQUALITY_2"); //Vereficamos que no haya errores

        Token op;
        Expression right;
        
        //Primera producción: EQUALITY_2 -> != COMPARISON EQUALITY_2
        if ((this.preanalisis.tipo == TipoToken.BANG_EQUAL)) {
            match(TipoToken.BANG_EQUAL);
            op = this.previous();
            right = COMPARISON();
            Expression newLeft = new ExprBinary(left, op, right);
            return EQUALITY_2(newLeft);
        }
        //Segunda producción: EQUALITY_2 -> == COMPARISON EQUALITY_2
        else if ((this.preanalisis.tipo == TipoToken.EQUAL_EQUAL)) {
            match(TipoToken.EQUAL_EQUAL);
            op = this.previous();
            right = COMPARISON();
            Expression newLeft = new ExprBinary(left, op, right);
            return EQUALITY_2(newLeft);
        }
        //Tercera producción: EQUALITY_2 -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/
        return left;

    }

    //COMPARISON -> TERM COMPARISON_2
    private Expression COMPARISON()throws Exception{
        /*CHECAR*/
        //if(hayErrores) throw new Exception("Error en la funcion COMPARISON"); //Vereficamos que no haya errores
    
        Expression expr = TERM();
        return COMPARISON_2(expr);
    
    }

    //COMPARISON_2 -> > TERM COMPARISON_2 | >= TERM COMPARISON_2 | < TERM COMPARISON_2 | <= TERM COMPARISON_2 | Ɛ
    private Expression COMPARISON_2( Expression left )throws Exception{

        /*CHECAR*/

        //if(hayErrores) throw new Exception("Error en la funcion COMPARISON_2"); //Vereficamos que no haya errores

        Token op;
        Expression right; 

        //Primera producción: COMPARISON_2 -> > TERM COMPARISON_2
        if ((this.preanalisis.tipo == TipoToken.GREATER)) {
            match(TipoToken.GREATER);
            op = this.previous();
            right = TERM();
            Expression newLeft = new ExprBinary(left, op, right);
            return COMPARISON_2(newLeft);
        }
        //Segunda producción: COMPARISON_2 -> >= TERM COMPARISON_2
        else if ((this.preanalisis.tipo == TipoToken.GREATER_EQUAL)) {
            match(TipoToken.GREATER_EQUAL);
            op = this.previous();
            right = TERM();
            Expression newLeft = new ExprBinary(left, op, right);
            return COMPARISON_2(newLeft);
        }
        //Tercera producción: COMPARISON_2 -> < TERM COMPARISON_2
        else if((this.preanalisis.tipo == TipoToken.LESS)) {
            match(TipoToken.LESS);
            op = this.previous();
            right = TERM();
            Expression newLeft = new ExprBinary(left, op, right);
            return COMPARISON_2(newLeft);
        }
        //Cuarta producción: COMPARISON_2 -> <= TERM COMPARISON_2
        else if ((this.preanalisis.tipo == TipoToken.LESS_EQUAL)) {
            match(TipoToken.LESS_EQUAL);
            op = this.previous();
            right = TERM();
            Expression newLeft = new ExprBinary(left, op, right);
            return COMPARISON_2(newLeft);
        }
        //Quinta producción: COMPARISON_2 -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/

        return left;

    }

    //TERM -> FACTOR TERM_2
    private Expression TERM() throws Exception{
        /*CHECAR */

        //if(hayErrores) throw new Exception("Error en la funcion TERM"); //Vereficamos que no haya errores

        Expression fact = FACTOR();
        return TERM_2(fact);

    }

    //TERM_2 -> - FACTOR TERM_2 | + FACTOR TERM_2 | Ɛ
    private Expression TERM_2( Expression izq ) throws Exception{

        /*CHECAR*/

        //if(hayErrores) throw new Exception("Error en la funcion TERM_2"); //Vereficamos que no haya errores

        Token simbolo;
        Expression dere;

        //Primera producción: TERM_2 -> - FACTOR TERM_2
        if(( this.preanalisis.tipo == TipoToken.MINUS )) {
            match(TipoToken.MINUS);
            simbolo = this.previous();
            dere = FACTOR();//Exprecion binaria
            Expression newIzq = new ExprBinary(izq, simbolo, dere);
            return TERM_2(newIzq);
        }
        //Segunda producción: TERM_2 -> + FACTOR TERM_2
        else if(( this.preanalisis.tipo == TipoToken.PLUS )) {
            match(TipoToken.PLUS);
            simbolo = this.previous();
            dere = FACTOR();//Exprecion binaria
            Expression newIzq = new ExprBinary(izq, simbolo, dere);
            return TERM_2(newIzq);
        }
        //Tercera producción: TERM_2 -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/

        return izq;

    }

    //FACTOR -> UNARY FACTOR_2
    private Expression FACTOR() throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion FACTOR"); //Vereficamos que no haya errores

        Expression fact = UNARY();
        return FACTOR_2(fact);
            
    }

    //FACTOR_2 -> / UNARY FACTOR_2 | * UNARY FACTOR_2 | Ɛ
    private Expression FACTOR_2(Expression izq) throws Exception {

        //if(hayErrores) throw new Exception("Error en la funcion FACTOR_2"); //Vereficamos que no haya errores

        //Primera producción: FACTOR_2 -> / UNARY FACTOR_2
        if (( this.preanalisis.tipo == TipoToken.SLASH )) {
            match(TipoToken.SLASH);
            Token operador = previous();
            Expression der = UNARY();
            ExprBinary expB = new ExprBinary(izq, operador, der);
            return FACTOR_2(expB);
        }
        //Segunda producción: FACTOR_2 -> * UNARY FACTOR_2
        else if (( this.preanalisis.tipo == TipoToken.STAR )) {
            match(TipoToken.STAR);
            Token operador = previous();
            Expression der = UNARY();
            ExprBinary expB = new ExprBinary(izq, operador, der);
            return FACTOR_2(expB);
        }
        //Tercera producción: FACTOR_2 -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/
        return izq;
    }

    //UNARY -> ! UNARY | - UNARY | CALL
    private Expression UNARY() throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion UNARY"); //Vereficamos que no haya errores

        Token operador = null;

        //Primera producción: UNARY -> ! UNARY
        if(( this.preanalisis.tipo == TipoToken.BANG )) {
            match(TipoToken.BANG);
            operador = this.previous();
            Expression expr = UNARY();
            return new ExprUnary(operador, expr);
        }
        //Segunda producción: UNARY -> - UNARY
        else if (( this.preanalisis.tipo == TipoToken.MINUS )) {
            match(TipoToken.MINUS);
            Expression expr = UNARY();
            return new ExprUnary(operador, expr);
        }
        //Tercera producción:  UNARY -> CALL
        else if (( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
         || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
         || ( this.preanalisis.tipo == TipoToken.STRING )  || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
         || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN )) {
            
            return CALL();

        }

        throw new Exception("Error en la funcion UNARY");
        //return new ExprUnary(operador, llamada);
    
    }

    //CALL -> PRIMARY CALL_2
    private Expression CALL()throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion CALL"); //Vereficamos que no haya errores

        Expression expr = PRIMARY();
        return CALL_2(expr);
    }

    //CALL_2 -> ( ARGUMENTS_OPC ) CALL_2 | Ɛ
    private Expression CALL_2(Expression expr)throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion CALL_2"); //Vereficamos que no haya errores

        //Primera producción: CALL_2 -> ( ARGUMENTS_OPC ) CALL_2
        if (( this.preanalisis.tipo == TipoToken.LEFT_PAREN )) {
            match((TipoToken.LEFT_PAREN));
            List<Expression> arguments = ARGUMENTS_OPC();
            match((TipoToken.RIGHT_PAREN));

            return new ExprCallFunction(expr, arguments);
        }

        return expr;
        //Segunda producción: CALL_2 -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/
    }

    //PRIMARY -> true | false | null | number | string | id | ( EXPRESSION )
    private Expression PRIMARY() throws Exception {

        //if(hayErrores) throw new Exception("Error en la funcion PRIMARY"); //Vereficamos que no haya errores

        //Primera producción: PRIMARY -> true
        if (( this.preanalisis.tipo == TipoToken.TRUE )) {
            match(TipoToken.TRUE);
            return new ExprLiteral(true);
        }
        //Segunda producción: PRIMARY -> false
        else if (( this.preanalisis.tipo == TipoToken.FALSE )) {
            match(TipoToken.FALSE);
            return new ExprLiteral(false);
        }
        //Tercera producción: PRIMARY -> null
        else if (( this.preanalisis.tipo == TipoToken.NULL )) {
            match(TipoToken.NULL);
            return new ExprLiteral(null);
        }
        //Cuarta producción: PRIMARY -> number
        else if (( this.preanalisis.tipo == TipoToken.NUMBER )) {
            match(TipoToken.NUMBER);
            Token numero = previous();                      //Le asiganamos el token anterior al de la posicion  (es )
            return new ExprLiteral(numero.getLiteral());    //Retornamos el nodo
        }
        //Quinta producción: PRIMARY -> string
        else if (( this.preanalisis.tipo == TipoToken.STRING )) {
            match(TipoToken.STRING);    //Comprovamos que es string
            Token cadena = previous();  //Le asiganamos el token anterior al de la posicion  (es )
            return new ExprLiteral(cadena.getLiteral());    //Retornamos el nodo
        }
        //Sexta producción: PRIMARY -> id
        else if (( this.preanalisis.tipo == TipoToken.IDENTIFIER )) {
            match(TipoToken.IDENTIFIER);//Comprovamos que es un identificador
            Token id = previous();      //Le asiganamos el token anterior al de la posicion  (es )
            return new ExprVariable(id);//Retornamos el nodo

        }
        //Septima producción: PRIMARY -> ( EXPRESSION )
        else if (( this.preanalisis.tipo == TipoToken.LEFT_PAREN )) {
            
            match(TipoToken.LEFT_PAREN);
            Expression expr = EXPRESSION();
            match(TipoToken.RIGHT_PAREN);
            return new ExprGrouping(expr);
        
        }

        throw new Exception("Error en PRIMARY");
    }

    //*********** OTRAS ********************

    //FUNCTION -> id ( PARAMETERS_OPC ) BLOCK
    private Statement FUNCTION() throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion FUNCTION"); //Vereficamos que no haya errores

        match(TipoToken.IDENTIFIER);
        //que guardar el id
        Token name = previous();
        match(TipoToken.LEFT_PAREN);
        //Avance de PARAMETERS_OPC
        List<Token> parame = PARAMETERS_OPC();
        match(TipoToken.RIGHT_PAREN);
        List<Statement> state = new ArrayList<>();
        StmtBlock block = BLOCK(state);

        return new StmtFunction( name, parame, block);

    }

    //FUNCTIONS -> FUN_DECL FUNCTIONS | Ɛ
    private void FUNCTIONS() throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion FUNCTIONS"); //Vereficamos que no haya errores

        //Primera producción: FUNCTIONS -> FUN_DECL FUNCTIONS
        if (( this.preanalisis.tipo == TipoToken.FUN )) {
            FUN_DECL();
            FUNCTIONS();
        }

        
        //Segunda producción: FUNCTIONS -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/
    }

    //PARAMETERS_OPC -> PARAMETERS | Ɛ
    private List<Token> PARAMETERS_OPC(){

        //RETORNAR UNA LISTA DE PARAMAETROS
        //if(hayErrores) throw new Exception("Error en la funcion PARAMETRS_OPC"); //Vereficamos que no haya errores

        List<Token> nameList = new ArrayList<>();

        //Primera producción: PARAMETERS_OPC -> PARAMETERS
        if (( this.preanalisis.tipo == TipoToken.IDENTIFIER )) {
             
            
            PARAMETERS(nameList);
        }

        return nameList;
        //Segunda producción: PARAMETERS_OPC -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/
    }

    //PARAMETERS -> id PARAMETERS_2
    private void PARAMETERS(List<Token> identificadores){

        //if(hayErrores) throw new Exception("Error en la funcion PARAMETRS"); //Vereficamos que no haya errores

        match(TipoToken.IDENTIFIER);
        Token antes = previous();
        identificadores.add(antes);
        PARAMETERS_2(identificadores);

    }

    //PARAMETERS_2 -> , id PARAMETERS_2 | Ɛ
    private void PARAMETERS_2(List<Token> identificadores){

        //if(hayErrores) throw new Exception("Error en la funcion PARAMETRS_2"); //Vereficamos que no haya errores

        //Primera producción: PARAMETERS_2 -> , id PARAMETERS_2
        if (( this.preanalisis.tipo == TipoToken.COMMA )) {
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            Token antes = previous();
            identificadores.add(antes);
            PARAMETERS_2(identificadores);
        }

        //Segunda producción: PARAMETERS_2 -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/
    }

    //ARGUMENTS_OPC -> EXPRESSION ARGUMENTS | Ɛ
    private List<Expression> ARGUMENTS_OPC() throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion ARGUMENTS_OPC"); //Vereficamos que no haya errores

        List<Expression> nameList = new ArrayList<>();
        //Primera producción: ARGUMENTS_OPC -> EXPRESSION ARGUMENTS
        if(( this.preanalisis.tipo == TipoToken.BANG )  || ( this.preanalisis.tipo == TipoToken.MINUS ) 
         || ( this.preanalisis.tipo == TipoToken.TRUE )  || ( this.preanalisis.tipo == TipoToken.FALSE )
         || ( this.preanalisis.tipo == TipoToken.NULL )  || ( this.preanalisis.tipo == TipoToken.NUMBER )
         || ( this.preanalisis.tipo == TipoToken.STRING )  || ( this.preanalisis.tipo == TipoToken.IDENTIFIER ) 
         || ( this.preanalisis.tipo == TipoToken.LEFT_PAREN )) {

            Expression expr = EXPRESSION();
            nameList.add(expr);
            ARGUMENTS(nameList);
        }

        return nameList;
        //Segunda producción: ARGUMENTS_OPC -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/
    }

    //ARGUMENTS -> , EXPRESSION ARGUMENTS | Ɛ
    private void ARGUMENTS(List<Expression> expresiones) throws Exception{

        //if(hayErrores) throw new Exception("Error en la funcion ARGUMENTS"); //Vereficamos que no haya errores

        if(( this.preanalisis.tipo == TipoToken.COMMA )) {
            match(TipoToken.COMMA);
            Expression expr = EXPRESSION();
            expresiones.add(expr);
            ARGUMENTS(expresiones);
        }
        //Segunda producción: ARGUMENTS -> Ɛ
        /*Como aparece Ɛ, nos manda error al estar vacío*/
    }

    private void match(TipoToken tt){
        
        if(preanalisis.tipo == tt){ //Si preanalisis.tipo es igual al TipoToken pasado por parametro (tt)
        
            i++;                         //Avanzamos uno
            preanalisis = tokens.get(i); //Asiganamos es siguiente token de la lista tokens a preanalisis   
        
        }else{ //Si no es igual
            
            hayErrores = true;                        //Ahora existen errores
            System.out.println("Error encontrado"); //Notificamos en consola
            // Lanzar la excepcion
        }

    }

    private Token previous() {
        //Retornar el token en la posicion anterior
        return this.tokens.get(i - 1);
    }

}