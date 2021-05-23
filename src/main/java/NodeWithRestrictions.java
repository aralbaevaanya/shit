import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;

import java.util.List;

public class NodeWithRestrictions {
    public com.github.javaparser.ast.Node Node;
    public List<Expression> restrictions;

    public NodeWithRestrictions(Node stmt, List<Expression> conditional) {
        Node = stmt;
        restrictions = conditional;
    }
}
