package Parser;

import java.util.List;

import Parser.ParsingClass.AssociationItem;

public class AssociationClass {
	
	
	
	public void associations(ParsingClass useJavaParser,String path2)
	{
         useJavaParser.createAssociationStrUML();
         useJavaParser.createExtendStrUML();
         useJavaParser.createInterfaceStrUML();
         try {
			UMLClassDiagram.creator(useJavaParser.classStrUML, useJavaParser.associationStrUML, useJavaParser.extendStrUML, useJavaParser.interfaceStrUML,path2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

         //test if interfaceNames isEmpty?
         System.out.println(useJavaParser.interfaceNames.isEmpty());
		
	}


}
