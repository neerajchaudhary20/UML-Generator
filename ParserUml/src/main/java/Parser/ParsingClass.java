package Parser;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.lang.Math;

import java.util.*;
/**
 *Neeraj
 */
public class ParsingClass {

    static Set<String> implementator;
   
    static Set<UseInterfaceItem> ballSocketFinder; //Finds ball and socket interface
    static List<ImplementInterfaceItem> implementInterfaceList;

    static List<String> classStrUML;  
    static List<String> associationStrUML; 
    static List<String> extendStrUML; 
    static List<String> interfaceStrUML; // 
    
    class AssociationItem {
        String startName;
        String endName;
        String attributeName;
        boolean ifMultiple;
    }
    static List<String> classNames;
    static List<String> interfaceNames;
    static List<AssociationItem> multiList;  
    static List<ExtendItem> extendItemList; 

  

    class ExtendItem {
        String superClassName;
        String subClassName;
    }

    class UseInterfaceItem {
        String interfaceName;
        String useName;

        @Override
        public int hashCode() {
            int hashcode = 0;
            hashcode = interfaceName.hashCode() * 20;
            hashcode += useName.hashCode();
            return hashcode;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof UseInterfaceItem) {
                UseInterfaceItem item = (UseInterfaceItem) obj;
                return (item.interfaceName.equals(this.interfaceName) && item.useName.equals(this.useName));
            } else {
                return false;
            }
        }
    }

    class ImplementInterfaceItem {
        String interfaceName;
        String implementName;
    }

   
    static String nameClassVisitor;
    static boolean isInterfaceClassVisitor;
    static int modifierClassVisitor;
    static List<ClassOrInterfaceType> extendClassVisitor;
    static List<ClassOrInterfaceType> implementClassVisitor;

 
    static List<String> methodgetter;
    static List<Integer> methodChanger;
    static List<String> typeMethodVisitor;
    static List<List<Parameter>> parameterListMethodVisitor;
    
    static List<String> getSetAttribute = new ArrayList<String>(); 

   
    static List<String> nameFieldVisitor;
    static List<Integer> modifierFieldVistor;
    static List<String> typeFieldVisitor;

 
    static List<String> nameConstructorVisitor;
    static List<Integer> modifierConstructorVisitor;
    static List<List<Parameter>> parameterListConstructorVisitor;

 
    static ArrayList<String> innerAttributeTypes = new ArrayList<String>();

    ParsingClass() {
        classNames = new ArrayList<String>();
        interfaceNames = new ArrayList<String>();

        multiList = new ArrayList<AssociationItem>();
        extendItemList = new ArrayList<ExtendItem>();
        ballSocketFinder = new LinkedHashSet<UseInterfaceItem>();
        implementInterfaceList = new ArrayList<ImplementInterfaceItem>();

        classStrUML = new ArrayList<String>();
        associationStrUML = new ArrayList<String>();
        extendStrUML = new ArrayList<String>();
        interfaceStrUML = new ArrayList<String>();

        extendClassVisitor = new ArrayList<ClassOrInterfaceType>();
        implementClassVisitor = new ArrayList<ClassOrInterfaceType>();

        methodgetter = new ArrayList<String>();
        methodChanger = new ArrayList<Integer>();
        typeMethodVisitor = new ArrayList<String>();
        parameterListMethodVisitor = new ArrayList<List<Parameter>>();

        nameFieldVisitor = new ArrayList<String>();
        modifierFieldVistor = new ArrayList<Integer>();
        typeFieldVisitor = new ArrayList<String>();

        nameConstructorVisitor = new ArrayList<String>();
        modifierConstructorVisitor = new ArrayList<Integer>();
        parameterListConstructorVisitor = new ArrayList<List<Parameter>>();

        innerAttributeTypes = new ArrayList<String>();
    }

  
    public static class ClassVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg) {

            nameClassVisitor = n.getName();
            isInterfaceClassVisitor = n.isInterface();
            extendClassVisitor = n.getExtends();
            implementClassVisitor = n.getImplements();
            modifierClassVisitor = n.getModifiers();
            
        }

    }

   
    public static class MethodVisitor extends VoidVisitorAdapter{

        @Override
        public void visit(MethodDeclaration n, Object arg) {
            
        	if(!(n.getName().startsWith("get") || n.getName().startsWith("set")))
        	{
        		methodChanger.add(n.getModifiers());
                methodgetter.add(n.getName());            
                typeMethodVisitor.add(n.getType().toString());
                parameterListMethodVisitor.add(n.getParameters());
               
        	}    
        	else
        	{
        		String methodName = n.getName();
        	    
        	    methodName = methodName.substring(3);
        	    methodName = methodName.toLowerCase();
        		getSetAttribute.add(methodName);
        		
        		
        	}
        }
    }

    
    public static class FieldVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(FieldDeclaration n, Object arg) {
        	
        	if(!(getSetAttribute.contains(n.getVariables().get(0).toString())))
        	{
        		typeFieldVisitor.add(n.getType().toString());
                nameFieldVisitor.add(n.getVariables().get(0).toString());
                modifierFieldVistor.add(n.getModifiers());
        	}
        	else
        	{
        		
        		typeFieldVisitor.add(n.getType().toString());
                nameFieldVisitor.add(n.getVariables().get(0).toString());
                modifierFieldVistor.add(ModifierSet.PUBLIC);
               
        	}
            
        }
    }

  
    public static class ConstructorVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(ConstructorDeclaration n, Object arg) {
            modifierConstructorVisitor.add(n.getModifiers());
            nameConstructorVisitor.add(n.getName());
            parameterListConstructorVisitor.add(n.getParameters());
        }
    }

   
    public static class VariableDecVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(VariableDeclarationExpr n, Object arg) {
            innerAttributeTypes.add(n.getType().toString());
        }
    }

   
    public void createClassStrUML() {
        String source = "";
        if (isInterfaceClassVisitor) {
            source += "interface " + nameClassVisitor + " {\n";
        } else {
            if (ModifierSet.isAbstract(modifierClassVisitor)) {
                source += "abstract class " + nameClassVisitor + " {\n";
            } else {
                source += "class " + nameClassVisitor + " {\n";
            }
        }

       
        for (String field : nameFieldVisitor) {
            
            int index = nameFieldVisitor.indexOf(field);
          
            String substr1 = "";
            if (typeFieldVisitor.get(index).indexOf('[') >= 0) {
                substr1 += typeFieldVisitor.get(index).substring(0, typeFieldVisitor.get(index).indexOf('['));
            } else if (typeFieldVisitor.get(index).contains("Collection") || typeFieldVisitor.get(index).contains("List") || typeFieldVisitor.get(index).contains("Map") || typeFieldVisitor.get(index).contains("Set")) {
                substr1 += typeFieldVisitor.get(index).substring(typeFieldVisitor.get(index).indexOf('<') + 1, typeFieldVisitor.get(index).indexOf('>'));
            }

            if (classNames.indexOf(typeFieldVisitor.get(index)) >= 0 || classNames.indexOf(substr1) >= 0
                    || interfaceNames.indexOf(typeFieldVisitor.get(index)) >= 0 || interfaceNames.indexOf(substr1) >= 0) {
                AssociationItem associationItem = new AssociationItem();
                associationItem.startName = nameClassVisitor;
                if (substr1 != "") {
                    associationItem.endName = substr1;
                }
                else {
                    associationItem.endName = typeFieldVisitor.get(index);
                }

                associationItem.attributeName = field;

                if (substr1 != "") {
                    associationItem.ifMultiple = true;
                } else {
                    associationItem.ifMultiple = false;
                }
                multiList.add(associationItem);
            } else {

                String typefieldstr = "";
                if (typeFieldVisitor.get(index).indexOf('[') >= 0) {
                    typefieldstr += typeFieldVisitor.get(index).substring(0, typeFieldVisitor.get(index).indexOf('['));
                    typefieldstr += "(*)";
                } else if (typeFieldVisitor.get(index).contains("Collection") || typeFieldVisitor.get(index).contains("List") || typeFieldVisitor.get(index).contains("Map") || typeFieldVisitor.get(index).contains("Set")) {
                    typefieldstr += typeFieldVisitor.get(index).substring(typeFieldVisitor.get(index).indexOf('<') + 1, typeFieldVisitor.get(index).indexOf('>'));
                    typefieldstr += "(*)";
                } else {
                    typefieldstr += typeFieldVisitor.get(index);
                }

                if (ModifierSet.isPublic(modifierFieldVistor.get(index))) {
                    source += "+" + field + ":" + typefieldstr + "\n";
                } else if (ModifierSet.isPrivate(modifierFieldVistor.get(index))) {
                    source += "-" + field + ":" + typefieldstr + "\n";
                }
            }
        }

        source += "__\n";

        
        for (String methodName : nameConstructorVisitor) {
            int index = nameConstructorVisitor.indexOf(methodName);
            if (ModifierSet.isPublic(modifierConstructorVisitor.get(index))) {
                String parameterStr = "";

                for (Parameter parameterSingle : parameterListConstructorVisitor.get(index)) {
                    String[] parts = parameterSingle.toString().split(" ");
                    parameterStr += parts[1] + ":" + parameterSingle.getType();
                    if (parameterListConstructorVisitor.get(index).indexOf(parameterSingle) + 1 != parameterListConstructorVisitor.get(index).size())
                        parameterStr += ",";
                }
                source += "+" + methodName + "(" + parameterStr + ")" + "\n";
            }

      
            for (Parameter parameterSingle : parameterListConstructorVisitor.get(index)) {
                String substr1 = "";
                String paramtertype = parameterSingle.getType().toString();

                if (paramtertype.indexOf('[') >= 0) {
                    substr1 += paramtertype.substring(0, paramtertype.indexOf('['));
                } else if (paramtertype.contains("Collection") || paramtertype.contains("List") || paramtertype.contains("Map") || paramtertype.contains("Set")) {
                    substr1 += paramtertype.substring(paramtertype.indexOf('<') + 1, paramtertype.indexOf('>'));
                } else
                    substr1 += paramtertype;

                for (String interfaceName : interfaceNames) {
                    if (interfaceName.equals(substr1)) {
                        UseInterfaceItem useInterfaceItem = new UseInterfaceItem();
                        useInterfaceItem.interfaceName = interfaceName;
                        useInterfaceItem.useName = nameClassVisitor;

                        //if use is a class, added to ballSocketFinder, ignore used by a interface
                        if (classNames.contains(nameClassVisitor))
                            ballSocketFinder.add(useInterfaceItem);
                    }
                }
            }
        }


        
        for (String methodName : methodgetter) {
            int index = methodgetter.indexOf(methodName);
            if (ModifierSet.isPublic(methodChanger.get(index)) || interfaceNames.contains(nameClassVisitor)) {
                String parameterStr = "";

                for (Parameter parameterSingle : parameterListMethodVisitor.get(index)) {
                    String[] parts = parameterSingle.toString().split(" ");
                    parameterStr += parts[1] + ":" + parameterSingle.getType();
                    if (parameterListMethodVisitor.get(index).indexOf(parameterSingle) + 1 != parameterListMethodVisitor.get(index).size())
                        parameterStr += ",";
                }

                source += "+" + methodName + "(" + parameterStr + "):" + typeMethodVisitor.get(index) + "\n";
            }


            
            for (Parameter parameterSingle : parameterListMethodVisitor.get(index)) {
                String substr1 = "";
                String paramtertype = parameterSingle.getType().toString();

                if (paramtertype.indexOf('[') >= 0) {
                    substr1 += paramtertype.substring(0, paramtertype.indexOf('['));
                } else if (paramtertype.contains("Collection") || paramtertype.contains("List") || paramtertype.contains("Map") || paramtertype.contains("Set")) {
                    substr1 += paramtertype.substring(paramtertype.indexOf('<') + 1, paramtertype.indexOf('>'));
                } else
                    substr1 += paramtertype;

                for (String interfaceName : interfaceNames) {
                    if (interfaceName.equals(substr1)) {
                        UseInterfaceItem useInterfaceItem = new UseInterfaceItem();
                        useInterfaceItem.interfaceName = interfaceName;
                        useInterfaceItem.useName = nameClassVisitor;

                       
                        if (classNames.contains(nameClassVisitor))
                            ballSocketFinder.add(useInterfaceItem);
                    }
                }
            }


            String substr1 = "";
            String returntype = typeMethodVisitor.get(index);
            if (returntype.indexOf('[') >= 0) {
                substr1 += returntype.substring(0, returntype.indexOf('['));
            } else if (returntype.contains("Collection") || returntype.contains("List") || returntype.contains("Map") || returntype.contains("Set")) {
                substr1 += returntype.substring(returntype.indexOf('<') + 1, returntype.indexOf('>'));
            } else
                substr1 += returntype;

            for (String interfaceName : interfaceNames) {
                if (interfaceName.equals(substr1)) {
                    UseInterfaceItem useInterfaceItem = new UseInterfaceItem();
                    useInterfaceItem.interfaceName = interfaceName;
                    useInterfaceItem.useName = nameClassVisitor;

                   
                    if (classNames.contains(nameClassVisitor))
                        ballSocketFinder.add(useInterfaceItem);
                }
            }

        }
        source += "}\n";

       
        for (String innervarType : innerAttributeTypes) {
            for (String interfaceName : interfaceNames) {
                if (interfaceName.equals(innervarType)) {
                    UseInterfaceItem useInterfaceItem = new UseInterfaceItem();
                    useInterfaceItem.interfaceName = interfaceName;
                    useInterfaceItem.useName = nameClassVisitor;

                   
                    if (classNames.contains(nameClassVisitor))
                        ballSocketFinder.add(useInterfaceItem);
                }
            }
        }

        classStrUML.add(source);
    }

    
    public void createAssociationStrUML() {
        String source = "";
        while (!multiList.isEmpty()) {
            String s8 = multiList.get(0).startName;
            String s9 = multiList.get(0).endName;

            int i = 0;
            for (; i < multiList.size(); i++) {
                if (multiList.get(i).startName.equals(s9) && multiList.get(i).endName.equals(s8)) {
                    break;
                }
            }
            if (i < multiList.size()) {
                if (multiList.get(0).ifMultiple && multiList.get(i).ifMultiple) {
                    source += s8 + " \"*\"" + "--" + "\"*\" " + s9 + "\n";
                } else if (multiList.get(0).ifMultiple) {
                    source += s8 + " \"1\"" + " --" + "\"*\" " + s9 + "\n";
                } else if (multiList.get(i).ifMultiple) {
                    source += s8 + " \"*\"" + "-- " + "\"1\" " + s9 + "\n";
                } else {
                    source += s8 + " \"1\"" + " -- " + "\"1\" " + s9 + "\n";
                }
                multiList.remove(i);
                multiList.remove(0);
            } else {
                if (multiList.get(0).ifMultiple) {
                    if (multiList.get(0).endName.toUpperCase().equals(multiList.get(0).attributeName.toUpperCase())) {
                        source += s8 + " --" + "\"*\" " + s9 + "\n";
                    } else {
                        
                        source += s8 + " --" + "\"*\" " + s9 + "\n";
                    }

                } else {
                    
                    source += s8 + " --" + "\"1\" " + s9 + "\n";
                }
                multiList.remove(0);
            }
        }

        associationStrUML.add(source);
    }


  
    public void createExtendStrUML() {
        String source = "";
        for (ExtendItem e : extendItemList) {
            source += e.superClassName + " <|-- " + e.subClassName + "\n";
        }
        extendStrUML.add(source);
    }

  
    public void createInterfaceStrUML() {
        String source = "";
       

        for (ImplementInterfaceItem t : implementInterfaceList) {
            source += t.interfaceName + " <|.. " + t.implementName + "\n";
        }

        for (UseInterfaceItem i : ballSocketFinder) {
            source += i.useName + " ..> " + i.interfaceName + ": use\n";
        }
        interfaceStrUML.add(source);
    }

    public void clearTempStaticClass() {

        methodgetter.clear();
        methodChanger.clear();
        typeMethodVisitor.clear();
        parameterListMethodVisitor.clear();

        nameFieldVisitor.clear();
        modifierFieldVistor.clear();
        typeFieldVisitor.clear();

        nameConstructorVisitor.clear();
        modifierConstructorVisitor.clear();
        parameterListConstructorVisitor.clear();

        innerAttributeTypes.clear();
    }
}
