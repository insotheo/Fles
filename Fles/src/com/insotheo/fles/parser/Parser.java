package com.insotheo.fles.parser;

import com.insotheo.fles.ast.*;
import com.insotheo.fles.lexer.Lexer;
import com.insotheo.fles.lexer.Token;
import com.insotheo.fles.lexer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Lexer lexer;
    private final List<ASTNode> nodes;
    private Token currentToken;

    public Parser(Lexer fileLexer) throws Exception {
        lexer = fileLexer;
        currentToken = lexer.next();
        nodes = new ArrayList<ASTNode>();
    }

    public List<ASTNode> parse() throws Exception {
        while (currentToken != null && currentToken.type != TokenType.EOF) {
            nodes.add(parseStatement());
        }
        return nodes;
    }

    private ASTNode parseStatement() throws Exception {
        String currentTokenValue = currentToken.value; //because identifier is everything so for handling exception correctly

        if (eat(TokenType.LBrace)) { //block
            return parseBlock();
        } else if (eat(TokenType.Module)) {
            fatalCheck(TokenType.Identifier);
            String moduleName = currentToken.value;
            eatFatal(TokenType.Identifier);

            fatalCheck(TokenType.LBrace);
            eatFatal(TokenType.LBrace);
            BlockNode moduleBody = parseBlock();
            return new ModuleNode(moduleName, moduleBody);
        } else if (eat(TokenType.Let)) { //variable definition
            //let <identifier> : <data_type> ...
            fatalCheck(TokenType.Identifier);
            String identifier = currentToken.value;
            eatFatal(TokenType.Identifier);

            eatFatal(TokenType.Colon);
            fatalCheck(TokenType.Identifier);
            String type = currentToken.value;
            eatFatal(TokenType.Identifier);

            VariableNode variable = new VariableNode(type, identifier);
            if (eat(TokenType.EqualSign)) { //assignment: let <identifier> : <type> = <expression>;
                ASTNode expression = parseExpression();
                eatFatal(TokenType.Semicolon);
                return new AssignmentNode(variable, expression, true);
            } else if (eat(TokenType.Semicolon)) { //if no assignment: let <identifier> : <type>;
                return variable;
            }
        } else if (eat(TokenType.Fn)) { //function definition
            //fn <identifier>(<args>) : <return_type>{... if return type is void possible way is: fn <identifier>(<args>){...
            fatalCheck(TokenType.Identifier);
            String funcName = currentToken.value;
            eatFatal(TokenType.Identifier);

            eatFatal(TokenType.LParen); //parsing parameters
            List<ParameterNode> funcParameters = new ArrayList<ParameterNode>();
            while (currentToken != null && currentToken.type != TokenType.RParen) {
                if (currentToken.type == TokenType.Identifier) { //parameter: <identifier> : <data_type>
                    String parameterIdentifier = currentToken.value;
                    eatFatal(TokenType.Identifier);

                    eatFatal(TokenType.Colon);
                    fatalCheck(TokenType.Identifier);
                    String parameterType = currentToken.value;
                    eatFatal(TokenType.Identifier);

                    funcParameters.add(new ParameterNode(parameterType, parameterIdentifier));
                    if (!eat(TokenType.Comma)) {
                        break;
                    }
                }
            }
            eatFatal(TokenType.RParen);

            String funcReturnType = "void";
            if (eat(TokenType.Colon)) {
                fatalCheck(TokenType.Identifier); //return type
                funcReturnType = currentToken.value;
                eatFatal(TokenType.Identifier);
            }

            eatFatal(TokenType.LBrace);
            BlockNode funcBody = parseBlock();

            return new FunctionNode(funcName, funcReturnType, funcParameters, funcBody);
        } else if (eat(TokenType.Return)) {
            ASTNode value = parseExpression();
            eatFatal(TokenType.Semicolon);
            return new ReturnNode(value);
        } else if (eat(TokenType.Delete)) {
            fatalCheck(TokenType.Identifier);
            String identifier = currentToken.value;
            eatFatal(TokenType.Identifier);
            eatFatal(TokenType.Semicolon);

            DeleteTypeValue type = DeleteTypeValue.Variable;

            return new DeleteNode(identifier, type);
        } else if (currentToken.type == TokenType.Identifier) {
            String identifier = currentToken.value;
            eat(TokenType.Identifier);

            List<String> identifiers = new ArrayList<>();
            identifiers.add(identifier);
            while (eat(TokenType.Dot)) {
                fatalCheck(TokenType.Identifier);
                identifiers.add(currentToken.value);
                eatFatal(TokenType.Identifier);
            }

            if (eat(TokenType.EqualSign)) { //assignment: <identifier> = <expression>;
                VariableNode variable = new VariableNode("auto", String.join(".", identifiers));
                ASTNode expression = parseExpression();
                eatFatal(TokenType.Semicolon);
                return new AssignmentNode(variable, expression, false);
            } else if (currentToken.type == TokenType.LParen) { //function call
                FunctionCallNode functionCall = parseFunctionCall(String.join(".", identifiers));
                eatFatal(TokenType.Semicolon);
                return functionCall;
            } else {
                eatFatal(TokenType.Semicolon);
                return new QualifiedIdentifierNode(String.join(".", identifiers));
            }
        } else if (eat(TokenType.If)) {
            List<ConditionBlock> blocks = new ArrayList<>();
            BlockNode elseBlock = null;

            eatFatal(TokenType.LParen);
            ASTNode ifExpression = parseExpression();
            eatFatal(TokenType.RParen);
            eatFatal(TokenType.LBrace);
            BlockNode ifBody = parseBlock();

            blocks.add(new ConditionBlock(ifExpression, ifBody)); //if

            while (currentToken != null) {
                if (eat(TokenType.Else)) {
                    if (eat(TokenType.If)) { //else if
                        eatFatal(TokenType.LParen);
                        ASTNode elifExpression = parseExpression();
                        eatFatal(TokenType.RParen);
                        eatFatal(TokenType.LBrace);
                        BlockNode elifBody = parseBlock();
                        blocks.add(new ConditionBlock(elifExpression, elifBody));
                        continue;
                    }
                    eatFatal(TokenType.LBrace);
                    elseBlock = parseBlock();
                    break;
                }
                break;
            }

            return new IfBranch(blocks, elseBlock);
        } else if(eat(TokenType.While)){
            eatFatal(TokenType.LParen);
            ASTNode expr = parseExpression();
            eatFatal(TokenType.RParen);
            eatFatal(TokenType.LBrace);
            BlockNode loopBody = parseBlock();
            return new WhileLoop(expr, loopBody);
        }

        ParserExceptions.throwUnknownCommandException(currentTokenValue);
        return null;
    }

    private BlockNode parseBlock() throws Exception {
        List<ASTNode> blockStatements = new ArrayList<ASTNode>();
        while (currentToken != null && currentToken.type != TokenType.RBrace) {
            blockStatements.add(parseStatement());
        }
        eatFatal(TokenType.RBrace);
        return new BlockNode(blockStatements);
    }

    private ASTNode parseFactor() throws Exception {//for identifiers or ()
        if (currentToken.type == TokenType.Plus || currentToken.type == TokenType.Minus) {
            NumberNode multiplier = currentToken.type == TokenType.Plus ? new NumberNode(1) : new NumberNode(-1); //plus or minus
            eatFatal(currentToken.type);
            ASTNode value = parseFactor();
            return new BinaryOperationNode(multiplier, OperationValue.Multiplication, value);
        } else if (currentToken.type == TokenType.LogicNot) {
            eatFatal(TokenType.LogicNot);
            ASTNode expression = parseFactor();
            return new NotNode(expression);
        } else if (currentToken.type == TokenType.Number) {
            NumberNode number = new NumberNode(Double.parseDouble(currentToken.value));
            eatFatal(TokenType.Number);
            return number;
        } else if (currentToken.type == TokenType.True || currentToken.type == TokenType.False) {
            BooleanNode bool = new BooleanNode(currentToken.type == TokenType.True);
            eatFatal(currentToken.type);
            return bool;
        } else if (currentToken.type == TokenType.StringLiteral) {
            StringLiteralNode string = new StringLiteralNode(currentToken.value);
            eatFatal(TokenType.StringLiteral);
            return string;
        } else if (currentToken.type == TokenType.CharLiteral) {
            CharLiteral character = new CharLiteral(currentToken.value.charAt(0));
            eatFatal(TokenType.CharLiteral);
            return character;
        } else if (currentToken.type == TokenType.Identifier) {
            String identifier = currentToken.value;
            String type = "auto"; //auto is the same for unknown
            eatFatal(TokenType.Identifier);

            if (currentToken.type == TokenType.LParen) { //if calls for a function
                return parseFunctionCall(identifier);
            }
            return new VariableNode(type, identifier);
        } else if (currentToken.type == TokenType.LParen) {
            eatFatal(TokenType.LParen);
            ASTNode result = parseExpression();
            eatFatal(TokenType.RParen);
            return result;
        } else if (currentToken.type == TokenType.LBrace) {
            eatFatal(TokenType.LBrace);
            BlockNode result = parseBlock();
            return result;
        }

        ParserExceptions.throwUnexpectedTokenInFactorException(currentToken);
        return null;
    }

    private ASTNode parseTerm() throws Exception { //for * or /
        ASTNode left = parseFactor();

        while (currentToken != null &&
                (currentToken.type == TokenType.Asterisk
                        || currentToken.type == TokenType.Slash
                        || currentToken.type == TokenType.Bucks
                )) {
            OperationValue operator = switch (currentToken.type) {
                case Asterisk -> OperationValue.Multiplication;
                case Slash -> OperationValue.Division;
                case Bucks -> OperationValue.Cast;
                default -> OperationValue.Unknown;
            };
            eatFatal(currentToken.type);
            ASTNode right = parseFactor();
            left = new BinaryOperationNode(left, operator, right);
        }

        return left;
    }

    private ASTNode parseExpression() throws Exception { //for + or -, or logic
        ASTNode left = parseTerm();

        while (currentToken != null &&
                (currentToken.type == TokenType.Plus ||
                        currentToken.type == TokenType.Minus ||
                        currentToken.type == TokenType.MoreThan ||
                        currentToken.type == TokenType.LessThan ||
                        currentToken.type == TokenType.MoreOrEqual ||
                        currentToken.type == TokenType.LessOrEqual ||
                        currentToken.type == TokenType.Equality ||
                        currentToken.type == TokenType.LogicAnd ||
                        currentToken.type == TokenType.LogicOr ||
                        currentToken.type == TokenType.NotEqual
                )) {
            OperationValue operator = switch (currentToken.type) {
                case TokenType.Plus -> OperationValue.Addition;
                case TokenType.Minus -> OperationValue.Subtraction;
                case TokenType.MoreThan -> OperationValue.GreaterThan;
                case TokenType.LessThan -> OperationValue.LessThan;
                case TokenType.MoreOrEqual -> OperationValue.GreaterOrEqualThan;
                case TokenType.LessOrEqual -> OperationValue.LessOrEqualThan;
                case TokenType.Equality -> OperationValue.Equal;
                case TokenType.NotEqual -> OperationValue.NotEqual;
                case TokenType.LogicAnd -> OperationValue.LogicalAnd;
                case TokenType.LogicOr -> OperationValue.LogicalOr;
                default -> OperationValue.Unknown;
            };
            eatFatal(currentToken.type);
            ASTNode right = parseTerm();
            left = new BinaryOperationNode(left, operator, right);
        }

        return left;
    }

    private FunctionCallNode parseFunctionCall(String funcName) throws Exception {
        eatFatal(TokenType.LParen);

        List<ASTNode> args = new ArrayList<ASTNode>();
        if (currentToken != null && currentToken.type != TokenType.RParen) {
            do {
                args.add(parseExpression());

                if (currentToken.type != null && currentToken.type == TokenType.Comma) {
                    eatFatal(TokenType.Comma);
                } else {
                    break;
                }
            } while (true);
        }

        eatFatal(TokenType.RParen);
        return new FunctionCallNode(funcName, args);
    }

    private boolean eat(TokenType type) throws Exception {
        if (currentToken.type == type) {
            currentToken = lexer.next();
            return true;
        }
        return false;
    }

    private void eatFatal(TokenType expected) throws Exception {
        if (currentToken.type != expected) {
            if (currentToken.type == TokenType.Unknown) {
                ParserExceptions.throwUnexpectedTokenException(currentToken.value, expected);
            }
            ParserExceptions.throwUnexpectedTokenException(currentToken.type, expected);
        } else {
            currentToken = lexer.next();
        }
    }

    private void fatalCheck(TokenType expected) throws Exception {
        if (currentToken.type != expected) {
            if (currentToken.type == TokenType.Unknown) {
                ParserExceptions.throwUnexpectedTokenException(currentToken.value, expected);
            }
            ParserExceptions.throwUnexpectedTokenException(currentToken.type, expected);
        }
    }

}
