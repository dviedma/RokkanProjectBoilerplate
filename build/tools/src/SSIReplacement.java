import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.*;
import java.lang.String;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SSIReplacement
{
	
	//Compile
	//javac tools/src/SSIReplacement.java -d tools -cp tools/commons-io-2.4.jar 
	
	public static void main(String[] args) throws Exception {
		
		String dirPartials = args[0];		
		String dirPages = args[1];
		HashMap<String,String> hashMap = new HashMap<String,String>();
	
		hashMap = createMap(dirPartials, dirPages);
		traverse(new File(dirPages+"/partials"), hashMap);		//traverse only partials folder
		hashMap = createMap(dirPages+"/partials", dirPages);				//create hash again
		traverse(new File(dirPages), hashMap);					//traverse only *.html			
	} 
	
	public static HashMap<String,String> createMap(String dirPartials, String dirPages){
		HashMap<String,String> hM = new HashMap<String,String>();
		File dirPart = new File(dirPartials);
		  for (File child : dirPart.listFiles()) {
			String fN = child.getName();
			if (".".equals(fN) || "..".equals(fN) || !fN.substring( fN.lastIndexOf(".")).equals(".html") ) {
				//TODO allow files like _header.navigation.html (doble dot)
		      continue;  // Ignore the self and parent aliases.
		    }
			try {
				hM.put(fN.substring( 0, fN.lastIndexOf(".")), FileUtils.readFileToString(child));
			} catch (IOException e) {
		        e.printStackTrace();
		    }
		}		
		
		return hM;
	}
	
	public static void traverse(File directory, HashMap hashMap)
	{
	   // Get all files in directory
	   File[] files = directory.listFiles();
	   for (File file : files)
	   {
			//if (file.isDirectory() && file.getName() != "partials")
			if (file.isDirectory())
	      	{
			 	// Its a directory so (recursively) traverse it
				traverse(file, hashMap);
	      	}
	      	else
	      	{
			 	// its a file, do any processing you require here
				String fN = file.getName();
				String pageHTML = "";
				
				if (".".equals(fN) || "..".equals(fN) || fN.indexOf(".") < 0 || ((!fN.substring( fN.indexOf(".")).equals(".shtml")) && (!fN.substring( fN.lastIndexOf(".")).equals(".html"))) ) {
		      		continue;  // Ignore the self and parent aliases.
		    	}
		
				try {
					pageHTML = FileUtils.readFileToString(file);
				} catch (IOException e) {
			        e.printStackTrace();
			    }					
					
				Pattern pat = Pattern.compile("<!--#include\\s+virtual=\"[\\/]?(partials\\/)?(.*?)\\.html\"\\s+-->");
				Matcher m = pat.matcher(pageHTML);
				int count = 0;
				String ssi = "";
				String partial = "";
				while(m.find()) {
					count++;
					ssi = m.group();
					try {
						partial = Matcher.quoteReplacement( (hashMap.get(m.group(2))).toString() );
					}catch (Exception e){
						Logger.getLogger("SSIReplacement").info(e.toString()+" No SSI file for: "+m.group(1));
					}
					pageHTML = pageHTML.replaceAll(ssi, partial);
				}
				file.delete();
				
				try {
					FileUtils.writeStringToFile( new File(file.getAbsolutePath()), pageHTML);
				} catch (IOException e) {
			        e.printStackTrace();
			    }					
		  	}
	   	}
	}	
}
