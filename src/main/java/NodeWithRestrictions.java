import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;

import java.util.ArrayList;

public class NodeWithRestrictions {
    public com.github.javaparser.ast.Node Node;
    public ArrayList<Expression> restrictions;

    public NodeWithRestrictions(Node stmt, ArrayList<Expression> conditional) {
        Node = stmt;
        restrictions = conditional;
    }
}
