package Parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.type.ClassOrInterfaceType;




import java.io.File;
import java.io.FileInputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class ParserCaller {


        @SuppressWarnings("unchecked")
		public static void main(String[] args) throws Exception {
        	if (args.length < 2) {
                throw new InvalidParameterException("Too few parameters (" + args.length + ").");
            }
        	String path1 = args[0].toString();
        	String path2 = args[1].toString();
            
            String folderPath = path1;
            ArrayList<String> filePaths = new ArrayList<String>();
            String fileString;
            ParsingClass p= new ParsingClass();
            File folder = new File(folderPath);
            File[] listofFiles = folder.listFiles();
            for (File file : listofFiles) {
                if (file.isFile()) {
                    fileString = file.getName();
                    if (fileString.endsWith(".java") || fileString.endsWith(".JAVA"))
                        filePaths.add(fileString);
                }
            }

            ParsingClass useJavaParser = new ParsingClass();
            
            UsingFiles u = new UsingFiles();
            ParsingClass p2= u.Visitors(filePaths,useJavaParser,folderPath);
            useJavaParser=p2;            
            for (String filePath : filePaths) {
                FileInputStream in = new FileInputStream(folderPath + filePath);
                CompilationUnit cu = JavaParser.parse(in);
                in.close();

                new ParsingClass.ClassVisitor().visit(cu, null);
                new ParsingClass.MethodVisitor().visit(cu, null);
                new ParsingClass.FieldVisitor().visit(cu, null);
                new ParsingClass.ConstructorVisitor().visit(cu, null);
                new ParsingClass.VariableDecVisitor().visit(cu, null);

                useJavaParser.createClassStrUML(); 
                useJavaParser.clearTempStaticClass();
            }
               AssociationClass c = new AssociationClass();
               c.associations(useJavaParser,path2);
        }
    }

