package rs.ac.bg.etf.pp1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java_cup.runtime.Symbol;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import rs.ac.bg.etf.pp1.ast.Program;
import rs.ac.bg.etf.pp1.util.Log4JUtils;
// import rs.etf.pp1.mj.runtime.Code;
// import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Struct;

public class MJParserTest {

	static {
		DOMConfigurator.configure(Log4JUtils.instance().findLoggerConfigFile());
		Log4JUtils.instance().prepareLogFile(Logger.getRootLogger());
	}
	
	private static final String testPath = "test/test302.mj";
	
	public static void main(String[] args) throws Exception {
		
		Logger log = Logger.getLogger(MJParserTest.class);
		Reader br = null;
		try {
			File sourceCode = new File(testPath);
			log.info("Compiling source file: " + sourceCode.getAbsolutePath());
			
			br = new BufferedReader(new FileReader(sourceCode));
			Yylex lexer = new Yylex(br);
			
			MJParser parser = new MJParser(lexer);
	        Symbol symbol = parser.parse();
	        
	        Program program = (Program)(symbol.value);
	        
	        // ispis sintaksnog stabla
			log.info(program.toString(""));
			log.info("===================================");
			
			/*
			RuleVisitor ruleVisitor = new RuleVisitor();
			program.traverseBottomUp(ruleVisitor);
			log.info("Types: " + ruleVisitor.typeCnt);
			log.info("Variables: " + ruleVisitor.varCnt);
			log.info("Char constants: " + ruleVisitor.charConstCnt);
			log.info("Addition operations: " + ruleVisitor.addOpCnt);
			log.info("Methods: " + ruleVisitor.addOpCnt);
			*/
			
			if(!parser.errorDetected) {
				log.info("Parsing completed successfully!.");
			}else {
				log.error("Parser encountered error while parsing!");
				log.error(parser.errorMessage.toString());
			}
			
			semanticAnalysis(program);
			
		} 
		finally {
			if (br != null) try { br.close(); } catch (IOException e1) { log.error(e1.getMessage(), e1); }
		}

	}
	
	private static void semanticAnalysis(Program program) {
		Logger log = Logger.getLogger(MJParserTest.class);
		SemanticAnalyzer sa = new SemanticAnalyzer();
		MyTab.init();
		program.traverseBottomUp(sa);
		Tab.dump();
	}
	
	
}