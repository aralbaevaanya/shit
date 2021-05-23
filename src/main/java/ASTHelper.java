import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static com.github.javaparser.ast.expr.BinaryExpr.Operator.*;
import static com.github.javaparser.ast.expr.BinaryExpr.Operator.LESS;

public class ASTHelper {
    public Queue<NodeWithRestrictions> queue = new ArrayDeque<>();
    public ArrayList<NodeWithRestrictions> res;

    public ASTHelper(Node node) {
        res = new ArrayList<>();
        queue.add(new NodeWithRestrictions(node, new ArrayList<>()));
    }

    public void process(NodeWithRestrictions node) {
        Node cloneNode = node.Node.clone();
        IfStmt stmt = cloneNode.findFirst(IfStmt.class).get();

        var conditional = stmt.getCondition();

        var leftCondition = new ArrayList<Expression>();
        leftCondition.add(node.restrictions.isEmpty()
                ? conditional
                : new BinaryExpr(node.restrictions.get(0), conditional, AND));//to do optimize
        var thenStmt = stmt.getThenStmt();

        if (thenStmt.findFirst(ReturnStmt.class).isPresent()) {
            replace(cloneNode, stmt.getParentNode().get(), thenStmt, leftCondition);
        } else {
            replace(cloneNode, stmt, thenStmt, leftCondition);
        }


        Node cloneNode1 = node.Node.clone();
        IfStmt stmt1 = cloneNode1.findFirst(IfStmt.class).get();

        if (stmt.getElseStmt().isPresent()) {
            var elseStmt = stmt1.getElseStmt();
            ArrayList<Expression> rightCondition = new ArrayList<>();
            //to do optimize
            rightCondition.add(node.restrictions.isEmpty()
                    ? revert(conditional)
                    : new BinaryExpr(node.restrictions.get(0), revert(conditional), AND));

            replace(cloneNode1, stmt1, elseStmt.get(), rightCondition);
        } else {
            stmt1.getParentNode().get().remove(stmt1);
            ArrayList<Expression> rightCondition = new ArrayList<>();
            rightCondition.add(node.restrictions.isEmpty()
                    ? revert(conditional)
                    : new BinaryExpr(node.restrictions.get(0), revert(conditional), AND));//to do optimize
            var newNode = new NodeWithRestrictions(cloneNode1, rightCondition);

            if (newNode.Node.findFirst(IfStmt.class).isPresent()) {
                queue.add(newNode);
            } else {
                res.add(newNode);
            }
        }


    }

    public void replace(Node parent, Node blockOld, Node blockNew, List<Expression> restrictions) {

        blockOld.getParentNode().get().replace(blockOld, blockNew);

        var newNode = new NodeWithRestrictions(parent.clone(), restrictions);

        if (parent.findFirst(IfStmt.class).isPresent()) {
            queue.add(newNode);
        } else {
            res.add(newNode);
        }
    }

    public Expression revert(Expression expression) {
        return revert(expression.asBinaryExpr());
    }

    public BinaryExpr revert(BinaryExpr exception) {
        if (exception.getLeft().isNameExpr() || exception.getRight().isNameExpr()) {
            return new BinaryExpr(exception.getLeft(), exception.getRight(), revert(exception.getOperator()));
        } else {
            return new BinaryExpr(revert(exception.getLeft()), revert(exception.getRight()), revert(exception.getOperator()));
        }
    }

    public BinaryExpr.Operator revert(BinaryExpr.Operator op) {
        switch (op) {
            case OR -> {
                return AND;
            }
            case AND -> {
                return OR;
            }
            case EQUALS -> {
                return NOT_EQUALS;
            }
            case NOT_EQUALS -> {
                return EQUALS;
            }
            case LESS -> {
                return GREATER_EQUALS;
            }
            case GREATER -> {
                return LESS_EQUALS;
            }
            case LESS_EQUALS -> {
                return GREATER;
            }
            case GREATER_EQUALS -> {
                return LESS;
            }
            default -> throw new IllegalStateException("Unexpected value: " + op);
        }
    }


    public void print() {
        res.forEach(p -> {
            System.out.println(p.restrictions.toString());
            System.out.println(p.Node);
        });
    }

}
