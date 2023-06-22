package gnu.ecmascript;

import gnu.expr.ApplyExp;
import gnu.expr.BeginExp;
import gnu.expr.ErrorExp;
import gnu.expr.Expression;
import gnu.expr.IfExp;
import gnu.expr.QuoteExp;
import gnu.expr.ReferenceExp;
import gnu.expr.SetExp;
import gnu.lists.Sequence;
import gnu.mapping.Environment;
import gnu.mapping.InPort;
import gnu.mapping.OutPort;
import gnu.mapping.TtyInPort;
import gnu.mapping.Values;
import gnu.text.SyntaxException;
import java.io.IOException;
import java.util.Vector;
import kawa.standard.Scheme;

public class Parser {
    public static final Expression[] emptyArgs = new Expression[0];
    static Expression emptyStatement = new QuoteExp(Values.empty);
    public static Expression eofExpr = new QuoteExp(Sequence.eofValue);
    public int errors;
    Lexer lexer;
    InPort port;
    Object previous_token;
    Object token;

    public Parser(InPort port2) {
        this.port = port2;
        this.lexer = new Lexer(port2);
    }

    public Expression parseConditionalExpression() throws IOException, SyntaxException {
        Expression exp1 = parseBinaryExpression(1);
        if (peekToken() != Lexer.condToken) {
            return exp1;
        }
        skipToken();
        Expression exp2 = parseAssignmentExpression();
        if (getToken() != Lexer.colonToken) {
            return syntaxError("expected ':' in conditional expression");
        }
        return new IfExp(exp1, exp2, parseAssignmentExpression());
    }

    public Expression parseAssignmentExpression() throws IOException, SyntaxException {
        Expression exp1 = parseConditionalExpression();
        Object token2 = peekToken();
        if (token2 == Lexer.equalToken) {
            skipToken();
            Expression exp2 = parseAssignmentExpression();
            if (!(exp1 instanceof ReferenceExp)) {
                return syntaxError("unmplemented non-symbol ihs in assignment");
            }
            SetExp sex = new SetExp((Object) ((ReferenceExp) exp1).getName(), exp2);
            sex.setDefining(true);
            return sex;
        } else if (!(token2 instanceof Reserved)) {
            return exp1;
        } else {
            Reserved op = (Reserved) token2;
            if (!op.isAssignmentOp()) {
                return exp1;
            }
            skipToken();
            return new ApplyExp((Expression) new QuoteExp(op.proc), exp1, parseAssignmentExpression());
        }
    }

    public Expression parseExpression() throws IOException, SyntaxException {
        boolean last;
        Expression[] exps = null;
        int nExps = 0;
        while (true) {
            Expression exp1 = parseAssignmentExpression();
            if (peekToken() != Lexer.commaToken) {
                last = true;
            } else {
                last = false;
            }
            if (exps == null) {
                if (last) {
                    return exp1;
                }
                exps = new Expression[2];
            } else if (!last ? exps.length <= nExps : exps.length != nExps + 1) {
                Expression[] new_exps = new Expression[(last ? nExps + 1 : exps.length * 2)];
                System.arraycopy(exps, 0, new_exps, 0, nExps);
                exps = new_exps;
            }
            int nExps2 = nExps + 1;
            exps[nExps] = exp1;
            if (last) {
                int i = nExps2;
                return new BeginExp(exps);
            }
            skipToken();
            nExps = nExps2;
        }
    }

    public Object peekTokenOrLine() throws IOException, SyntaxException {
        if (this.token == null) {
            this.token = this.lexer.getToken();
        }
        return this.token;
    }

    public Object peekToken() throws IOException, SyntaxException {
        if (this.token == null) {
            this.token = this.lexer.getToken();
        }
        while (this.token == Lexer.eolToken) {
            skipToken();
            this.token = this.lexer.getToken();
        }
        return this.token;
    }

    public Object getToken() throws IOException, SyntaxException {
        Object result = peekToken();
        skipToken();
        return result;
    }

    public final void skipToken() {
        if (this.token != Lexer.eofToken) {
            this.previous_token = this.token;
            this.token = null;
        }
    }

    public void getSemicolon() throws IOException, SyntaxException {
        this.token = peekToken();
        if (this.token == Lexer.semicolonToken) {
            skipToken();
        } else if (this.token != Lexer.rbraceToken && this.token != Lexer.eofToken && this.previous_token != Lexer.eolToken) {
            syntaxError("missing ';' after expression");
        }
    }

    public Expression parsePrimaryExpression() throws IOException, SyntaxException {
        Object result = getToken();
        if (result instanceof QuoteExp) {
            return (QuoteExp) result;
        }
        if (result instanceof String) {
            return new ReferenceExp((String) result);
        }
        if (result != Lexer.lparenToken) {
            return syntaxError("unexpected token: " + result);
        }
        Expression expr = parseExpression();
        Object token2 = getToken();
        return token2 != Lexer.rparenToken ? syntaxError("expected ')' - got:" + token2) : expr;
    }

    public Expression makePropertyAccessor(Expression exp, Expression prop) {
        return null;
    }

    public Expression[] parseArguments() throws IOException, SyntaxException {
        skipToken();
        if (peekToken() == Lexer.rparenToken) {
            skipToken();
            return emptyArgs;
        }
        Vector args = new Vector(10);
        while (true) {
            args.addElement(parseAssignmentExpression());
            Object token2 = getToken();
            if (token2 == Lexer.rparenToken) {
                Expression[] exps = new Expression[args.size()];
                args.copyInto(exps);
                return exps;
            } else if (token2 != Lexer.commaToken) {
                syntaxError("invalid token '" + token2 + "' in argument list");
            }
        }
    }

    public Expression makeNewExpression(Expression exp, Expression[] args) {
        if (args == null) {
            args = emptyArgs;
        }
        return new ApplyExp((Expression) null, args);
    }

    public Expression makeCallExpression(Expression exp, Expression[] args) {
        return new ApplyExp(exp, args);
    }

    public String getIdentifier() throws IOException, SyntaxException {
        Object token2 = getToken();
        if (token2 instanceof String) {
            return (String) token2;
        }
        syntaxError("missing identifier");
        return "??";
    }

    public Expression parseLeftHandSideExpression() throws IOException, SyntaxException {
        int newCount = 0;
        while (peekToken() == Lexer.newToken) {
            newCount++;
            skipToken();
        }
        Expression exp = parsePrimaryExpression();
        while (true) {
            Object token2 = peekToken();
            if (token2 == Lexer.dotToken) {
                skipToken();
                exp = makePropertyAccessor(exp, new QuoteExp(getIdentifier()));
            } else if (token2 == Lexer.lbracketToken) {
                skipToken();
                Expression prop = parseExpression();
                Object token3 = getToken();
                if (token3 != Lexer.rbracketToken) {
                    return syntaxError("expected ']' - got:" + token3);
                }
                exp = makePropertyAccessor(exp, prop);
            } else if (token2 == Lexer.lparenToken) {
                Expression[] args = parseArguments();
                System.err.println("after parseArgs:" + peekToken());
                if (newCount > 0) {
                    exp = makeNewExpression(exp, args);
                    newCount--;
                } else {
                    exp = makeCallExpression(exp, args);
                }
            } else {
                while (newCount > 0) {
                    exp = makeNewExpression(exp, (Expression[]) null);
                    newCount--;
                }
                return exp;
            }
        }
    }

    public Expression parsePostfixExpression() throws IOException, SyntaxException {
        Expression exp = parseLeftHandSideExpression();
        Object op = peekTokenOrLine();
        if (op != Reserved.opPlusPlus && op != Reserved.opMinusMinus) {
            return exp;
        }
        skipToken();
        return new ApplyExp((Expression) new QuoteExp(((Reserved) op).proc), exp);
    }

    public Expression parseUnaryExpression() throws IOException, SyntaxException {
        return parsePostfixExpression();
    }

    public Expression syntaxError(String message) {
        this.errors++;
        OutPort err = OutPort.errDefault();
        String current_filename = this.port.getName();
        int current_line = this.port.getLineNumber() + 1;
        int current_column = this.port.getColumnNumber() + 1;
        if (current_line > 0) {
            if (current_filename != null) {
                err.print(current_filename);
            }
            err.print(':');
            err.print(current_line);
            if (current_column > 1) {
                err.print(':');
                err.print(current_column);
            }
            err.print(": ");
        }
        err.println(message);
        return new ErrorExp(message);
    }

    public Expression parseBinaryExpression(int prio) throws IOException, SyntaxException {
        Expression exp1 = parseUnaryExpression();
        while (true) {
            this.token = peekToken();
            if (this.token instanceof Reserved) {
                Reserved op = (Reserved) this.token;
                if (op.prio < prio) {
                    break;
                }
                getToken();
                exp1 = new ApplyExp((Expression) new QuoteExp(op.proc), exp1, parseBinaryExpression(op.prio + 1));
            } else {
                break;
            }
        }
        return exp1;
    }

    public Expression parseIfStatement() throws IOException, SyntaxException {
        Expression else_part;
        skipToken();
        Object token2 = getToken();
        if (token2 != Lexer.lparenToken) {
            return syntaxError("expected '(' - got:" + token2);
        }
        Expression test_part = parseExpression();
        Object token3 = getToken();
        if (token3 != Lexer.rparenToken) {
            return syntaxError("expected ')' - got:" + token3);
        }
        Expression then_part = parseStatement();
        if (peekToken() == Lexer.elseToken) {
            skipToken();
            else_part = parseStatement();
        } else {
            else_part = null;
        }
        return new IfExp(test_part, then_part, else_part);
    }

    public Expression buildLoop(Expression init, Expression test, Expression incr, Expression body) {
        if (init != null) {
            return new BeginExp(new Expression[]{init, buildLoop((Expression) null, test, incr, body)});
        }
        throw new Error("not implemented - buildLoop");
    }

    public Expression parseWhileStatement() throws IOException, SyntaxException {
        skipToken();
        Object token2 = getToken();
        if (token2 != Lexer.lparenToken) {
            return syntaxError("expected '(' - got:" + token2);
        }
        Expression test_part = parseExpression();
        Object token3 = getToken();
        if (token3 != Lexer.rparenToken) {
            return syntaxError("expected ')' - got:" + token3);
        }
        return buildLoop((Expression) null, test_part, (Expression) null, parseStatement());
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 116 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public gnu.expr.Expression parseFunctionDefinition() throws java.io.IOException, gnu.text.SyntaxException {
        /*
            r9 = this;
            r9.skipToken()
            java.lang.String r4 = r9.getIdentifier()
            java.lang.Object r6 = r9.getToken()
            gnu.text.Char r7 = gnu.ecmascript.Lexer.lparenToken
            if (r6 == r7) goto L_0x0027
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "expected '(' - got:"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r6)
            java.lang.String r7 = r7.toString()
            gnu.expr.Expression r5 = r9.syntaxError(r7)
        L_0x0026:
            return r5
        L_0x0027:
            java.util.Vector r1 = new java.util.Vector
            r7 = 10
            r1.<init>(r7)
            java.lang.Object r7 = r9.peekToken()
            gnu.text.Char r8 = gnu.ecmascript.Lexer.rparenToken
            if (r7 != r8) goto L_0x006f
            r9.skipToken()
        L_0x0039:
            gnu.expr.Expression r2 = r9.parseBlock()
            gnu.expr.LambdaExp r3 = new gnu.expr.LambdaExp
            r3.<init>((gnu.expr.Expression) r2)
            r3.setName(r4)
            gnu.expr.SetExp r5 = new gnu.expr.SetExp
            r5.<init>((java.lang.Object) r4, (gnu.expr.Expression) r3)
            r7 = 1
            r5.setDefining(r7)
            goto L_0x0026
        L_0x004f:
            gnu.text.Char r7 = gnu.ecmascript.Lexer.commaToken
            if (r6 == r7) goto L_0x006f
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "invalid token '"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r6)
            java.lang.String r8 = "' in argument list"
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.String r7 = r7.toString()
            r9.syntaxError(r7)
        L_0x006f:
            java.lang.String r0 = r9.getIdentifier()
            r1.addElement(r0)
            java.lang.Object r6 = r9.getToken()
            gnu.text.Char r7 = gnu.ecmascript.Lexer.rparenToken
            if (r6 != r7) goto L_0x004f
            goto L_0x0039
        */
        throw new UnsupportedOperationException("Method not decompiled: gnu.ecmascript.Parser.parseFunctionDefinition():gnu.expr.Expression");
    }

    public Expression parseBlock() throws IOException, SyntaxException {
        boolean last;
        Expression[] exps = null;
        if (getToken() != Lexer.lbraceToken) {
            return syntaxError("extened '{'");
        }
        int nExps = 0;
        while (true) {
            this.token = peekToken();
            if (this.token == Lexer.rbraceToken) {
                skipToken();
                if (exps == null) {
                    return emptyStatement;
                }
                last = true;
            } else {
                last = false;
            }
            if (exps == null) {
                exps = new Expression[2];
            } else if (!last ? exps.length <= nExps : exps.length != nExps) {
                Expression[] new_exps = new Expression[(last ? nExps : exps.length * 2)];
                System.arraycopy(exps, 0, new_exps, 0, nExps);
                exps = new_exps;
            }
            if (last) {
                return new BeginExp(exps);
            }
            exps[nExps] = parseStatement();
            nExps++;
        }
    }

    public Expression parseStatement() throws IOException, SyntaxException {
        Object token2 = peekToken();
        if (token2 instanceof Reserved) {
            switch (((Reserved) token2).prio) {
                case 31:
                    return parseIfStatement();
                case 32:
                    return parseWhileStatement();
                case 41:
                    return parseFunctionDefinition();
            }
        }
        if (token2 == Lexer.eofToken) {
            return eofExpr;
        }
        if (token2 == Lexer.semicolonToken) {
            skipToken();
            return emptyStatement;
        } else if (token2 == Lexer.lbraceToken) {
            return parseBlock();
        } else {
            Expression parseExpression = parseExpression();
            getSemicolon();
            return parseExpression;
        }
    }

    public static void main(String[] args) {
        new Scheme();
        InPort inp = InPort.inDefault();
        if (inp instanceof TtyInPort) {
            ((TtyInPort) inp).setPrompter(new Prompter());
        }
        Parser parser = new Parser(inp);
        OutPort out = OutPort.outDefault();
        while (true) {
            try {
                Expression expr = parser.parseStatement();
                if (expr != eofExpr) {
                    out.print("[Expression: ");
                    expr.print(out);
                    out.println("]");
                    Object result = expr.eval(Environment.user());
                    out.print("result: ");
                    out.print(result);
                    out.println();
                } else {
                    return;
                }
            } catch (Throwable ex) {
                System.err.println("caught exception:" + ex);
                ex.printStackTrace(System.err);
                return;
            }
        }
    }
}
