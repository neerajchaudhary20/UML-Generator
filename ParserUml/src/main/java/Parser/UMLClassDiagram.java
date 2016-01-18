package Parser;

import net.sourceforge.plantuml.SourceStringReader;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;

public class UMLClassDiagram {
    public static void creator(Collection<String> classStrUML, Collection<String> associationStrUML, Collection<String> extendStrUML, Collection<String> ballsocketStrUML,String path2) throws Exception {
        OutputStream png = new FileOutputStream(path2+"/diagram.png");
        String startSource = "@startuml\n";
        for (String s : classStrUML) {
        	startSource += s;
        }

        for (String s : associationStrUML) {
        	startSource += s;
        }

        for (String s: extendStrUML) {
        	startSource += s;
        }

        for (String s : ballsocketStrUML) {
        	startSource += s;
        }

        startSource += "@enduml\n";
        SourceStringReader reader = new SourceStringReader(startSource);
        String s2 = reader.generateImage(png); 
    }
}
