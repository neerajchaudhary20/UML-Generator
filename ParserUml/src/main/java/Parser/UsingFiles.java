package Parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import Parser.ParsingClass.ExtendItem;
import Parser.ParsingClass.ImplementInterfaceItem;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;

public class UsingFiles {

	public ParsingClass Visitors(ArrayList<String> filePaths, ParsingClass useJavaParser, String folderPath) throws ParseException, IOException {
		for (String filePath : filePaths)
		
		{
        FileInputStream in = new FileInputStream(folderPath + filePath);
        CompilationUnit cu = JavaParser.parse(in);
        in.close();

        new ParsingClass.ClassVisitor().visit(cu, null);

        if (useJavaParser.isInterfaceClassVisitor) {
            useJavaParser.interfaceNames.add(useJavaParser.nameClassVisitor);
        } else {
            useJavaParser.classNames.add(useJavaParser.nameClassVisitor);
        }

        if (useJavaParser.extendClassVisitor != null) {
            for (ClassOrInterfaceType item : useJavaParser.extendClassVisitor) {
                ParsingClass.ExtendItem extendItem = useJavaParser.new ExtendItem();
                extendItem.subClassName = useJavaParser.nameClassVisitor;
                extendItem.superClassName = item.getName();
                useJavaParser.extendItemList.add(extendItem);
            }
        }

        if (useJavaParser.implementClassVisitor != null) {
            for (ClassOrInterfaceType item : useJavaParser.implementClassVisitor) {
                ParsingClass.ImplementInterfaceItem implementInterfaceItem = useJavaParser.new ImplementInterfaceItem();
                implementInterfaceItem.implementName = useJavaParser.nameClassVisitor;
                implementInterfaceItem.interfaceName = item.getName();
                useJavaParser.implementInterfaceList.add(implementInterfaceItem);
            }
        }
		}
		return useJavaParser;
	}

}
