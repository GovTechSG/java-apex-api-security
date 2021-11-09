package com.api.util.ApiSecurity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.runner.RunWith;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.api.util.testframework.JUnitFactoryRunner;
import com.api.util.testframework.JUnitTestFactory;
import com.api.util.testframework.RuntimeTestCase;
import com.api.util.testframework.dto.TestDatum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

/**
 * @author GDS-PDD
 *
 * This class is a JUnit TC, which we need to run to test the given file,
 * which may have n number of test-cases.
 */
@RunWith(JUnitFactoryRunner.class)
public class ApiSecurityTest {
	
	private static final Logger log = LogManager.getLogger(ApiSecurityTest.class);
	
	private static final String testDataPath = getLocalPath("src/main/resources/test-suites/testData/");
	
	
    /** Here's where to set up test case inputs, and their expected result will be 
    * read from a text-file, and will be populated as an map 
    * (Map<TestInputData, ExpectedResultData>) and need to compare values with 
    * their corresponding expected result fetched from map.
    * This basically reads the text input file and prepare the List<RuntimeTestCase>.
    * and provides this List as an input to RuntimeTestCase
    * @return
     * @throws IOException 
    */
    @JUnitTestFactory
    public static Collection<?> tests() throws IOException {
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	TypeFactory typeFactory = objectMapper.getTypeFactory();
    	File testDataFolder = new File(testDataPath);
    	File[] testDataFile = testDataFolder.listFiles();
    	Map<String,TestDatum> testNameAndDatumMap = new LinkedHashMap<String,TestDatum>();
    	for (int i = 0; i < testDataFile.length; i++) {
    		List<TestDatum> testDataSingle = new ArrayList<TestDatum>();
    		if (testDataFile[i].isFile()) {
    			System.out.println("File: " + testDataFile[i].getName());
    			try{
    				String jsonString = getJSON(testDataFile[i].getCanonicalPath());
					testDataSingle = objectMapper.readValue(jsonString, typeFactory.constructCollectionType(List.class, TestDatum.class));
					for(TestDatum datum : testDataSingle){
						if(testDataFile[i].getName().contains("defaultParams") || testDataFile[i].getName().contains("httpCall") ){
							continue;
						}
						testNameAndDatumMap.put(datum.getId() + "_" + testDataFile[i].getName(), datum);
					}
    			}catch(IOException ioe){
    				ioe.printStackTrace();
    				throw ioe;
    			}
    		}
    	}
    	Set<Entry<String, TestDatum>> testNameAndDatumMapSet = testNameAndDatumMap.entrySet();
    	ArrayList<RuntimeTestCase> tests = new ArrayList<RuntimeTestCase>(testNameAndDatumMap.size());
    	for(Entry<String, TestDatum> entry : testNameAndDatumMapSet){
    		String testName = entry.getKey();
    		TestDatum testDatum = entry.getValue(); 
    		if(null != testDatum.getSkipTest() && testDatum.getSkipTest().contains("java")){
    			log.debug("Skip test: " + testName);
    			continue;
    		}
    		tests.add(new RuntimeTestCase(testName, testDatum));		
    	}
    	return tests;
    	
    }
    
    private static String getLocalPath(String relativeFileName) {
        Path currentRelativePath = Paths.get("");
        String s = combine(currentRelativePath.toAbsolutePath().toString(), relativeFileName.replaceAll("/", File.separator));
        
        return s;
    }
    private static String combine(String... paths) {
        File file = new File(File.separator);

        for (int i = 0; i < paths.length; i++) {
            file = new File(file, paths[i]);
        }

        return file.getPath();
    }
    private static String getJSON(String path) throws IOException{
    	java.io.FileInputStream fis = null;
    	String json;
    	try {
            fis = new java.io.FileInputStream(path);
            try( BufferedReader br =
                    new BufferedReader( new InputStreamReader(fis, StandardCharsets.UTF_8 )))
            {
               StringBuilder sb = new StringBuilder();
               String line;
               while(( line = br.readLine()) != null ) {
                  sb.append( line );
                  sb.append( '\n' );
               }
               json = sb.toString();
            }
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ioe) {
                    throw ioe;
                }
            }
        }
    	return json;
    }
}