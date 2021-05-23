import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;

import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        File source = new File("src/main/java/example4.java");
        TypeSolver typeSolver = new CombinedTypeSolver();

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser
                .getConfiguration()
                .setSymbolResolver(symbolSolver);
        CompilationUnit cu = StaticJavaParser.parse(source);
        System.out.println(cu.toString());

        var Node = cu.findRootNode();
        ASTHelper astHelper = new ASTHelper(Node);
        while (astHelper.queue.size() > 0) {
            astHelper.process(astHelper.queue.poll());
        }
        astHelper.print();

    }


}
