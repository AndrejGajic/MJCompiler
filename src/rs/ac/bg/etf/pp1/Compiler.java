package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import java_cup.runtime.Symbol;
import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;

public class Compiler {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	private static String sourceFilePath = "";
	private static String objFilePath = "";
	
	private static File sourceFile = null;
	private static File objFile = null;
	
	private static Yylex lexer = null;
	private static MJParser parser = null;
	
	private static Program program = null;
	private static SemanticAnalyzer semanticAnalyzer = null;
	
	private static Logger log = Logger.getLogger(Compiler.class);
	
	public static void tsdump() {
		Tab.dump();
	}
	
	private static boolean lexAnalysis() {
		log.info("Starting lexical analysis....");
		try {
			BufferedReader br = new BufferedReader(new FileReader(sourceFile));
			lexer = new Yylex(br);
			log.info("Lexical analysis finished successfully!");
			log.info("=========================================");
			return true;
		}
		catch(FileNotFoundException e) {
			return false;
		}
	}
	
	private static boolean syntaxAnalysis() {
		log.info("Starting syntax analysis....");
		parser = new MJParser(lexer);
		try {
			Symbol symbol = parser.parse();
			if(!parser.errorDetected) {
				log.info("Syntax analysis finished successfully!");
				log.info("Syntax tree: ");
				program = (Program)(symbol.value);
				log.info(program.toString(""));
				log.info("=========================================");
				return true;
			}
			else {
				return false;
			}
			
		} catch (Exception e) {
			return false;
		}
	}
	
	private static boolean semanticAnalysis() {
		log.info("Starting semantic analysis....");
		semanticAnalyzer = new SemanticAnalyzer();
		SemanticAnalyzer.setErrorDetected(false);
		MyTab.init();
		program.traverseBottomUp(semanticAnalyzer);
		if(!SemanticAnalyzer.getErrorDetected()) {
			log.info("Semantic analysis finished successfully!");
			log.info("Symbol table: ");
			tsdump();
			log.info("=========================================");
			return true;
		}
		else {
			return false;
		}
	}
	
	private static boolean generateCode() {
		log.info("Started generating code....");
		try {
			objFile = new File(objFilePath);
			if(objFile.exists()) {
				objFile.delete();
			}
			CodeGenerator codeGenerator = new CodeGenerator();
			program.traverseBottomUp(codeGenerator);
			Code.dataSize = semanticAnalyzer.getNVars();
			Code.mainPc = codeGenerator.getMainPC();
			Code.write(new FileOutputStream(objFile));
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		log.info("Code generated successfully!");
		return true;
	}
	
	public static void main(String[] args) {
	
		if(args.length < 2) {
			log.error("Too few arguments! You must specify paths to source file and object file!");
			return;
		}
		sourceFilePath = args[0];
		objFilePath = args[1];
		
		sourceFile = new File(sourceFilePath);
		
		if(sourceFile == null || !sourceFile.exists()) {
			log.error("Source file path is not correct!");
			return;
		}
		
		log.info("Starting compilation of a file " + sourceFilePath);
		
		if(!lexAnalysis()) {
			log.error("An error occured in lexical analysis! Terminating program...");
			return;
		}
		if(!syntaxAnalysis()) {
			log.error("An error occured in syntax analysis!");
			log.error(parser.errorMessage.toString());
			log.error("Terminating program...");
			return;
		}
		if(!semanticAnalysis()) {
			log.error("An error occured in semantic analysis!");
			return;
		}
		if(!generateCode()) {
			log.error("An error occured while generating code!");
			return;
		}
		log.info("Compiling file successfully completed!");	
	}
	
}
