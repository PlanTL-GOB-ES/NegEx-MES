/*********************************************************************************************
This resource was developed by the Centro Nacional de Investigaciones Oncológicas (CNIO) 
in the framework of the "Plan de Impulso de las Tecnologías del Lenguaje” driven by the 
Secretaría de Estado para la Sociedad de la Información y Agenda Digital.

Copyright (C) 2017 Secretaría de Estado para la Sociedad de la Información y la Agenda Digital (SESIAD)
 
This program is free software; you can redistribute it and/or
modify it under the terms of the MIT License see LICENSE.txt file.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*********************************************************************************************/


/**
 *
 * @author Jesús Santamaría
 */
 



/**************************
* Statics attributes
***************************/



 package smn.util;
 
 
 import smn.misc.GenNegEx;
 

import java.util.*;
import java.io.*;


public class Statics
{

	public Statics()
	{
	}


	public static final String COMMENT = ":="; //comments at config_files


	public static final String FILE_SEP = System.getProperty("file.separator"); //the file separator depends on OS: Linux="/"; Windows="\"



	public static List<String> pseNegPhrases = new LinkedList<String>();
	public static List<String> negPhrases = new LinkedList<String>();
	public static List<String> postNegPhrases = new LinkedList<String>();
	public static List<String> conjunctions = new LinkedList<String>();



	public static final String AFF = "Affirmed";
	public static final String NEG = "Negated";
	
	
	public static final String PSE = "pseNegPhrases";
	public static final String NEGPH = "negPhrases";
	public static final String POS = "postNegPhrases";
	public static final String CON = "conjunctions";
	public static final String NONE = "NONE";



	static public boolean isComment(String str)
	{
		return str.startsWith(Statics.COMMENT);
	}




	
	
	//old methods of callkit class 
    //post: process data and prints a result to a text file
    //public static void process(FileWriter fw, GenNegEx g, String textfile, boolean dison) throws IOException
	public static void process(BufferedWriter fw, GenNegEx g, String textfile, boolean dison) throws IOException
	{
		BufferedReader file = new BufferedReader(new FileReader(textfile));
        String line;
        while((line = file.readLine()) != null)
		{
//System.out.println(line);
			String[] parts = line.split("\\t");
			String sentence = cleans(parts[2]);
			String scope = g.negScope(sentence);	    
			if(scope.equals("-1")) 
				fw.write(line + "\t" + AFF + "\t" + Statics.NONE + "\n");
			else if(scope.equals("-2"))
				fw.write(line + "\t" + NEG + "\t" + g.getNameNegConfigFile() + "\n");
			else
			{
				String keyWords = cleans(parts[1]); 
				if(contains(scope, sentence, keyWords))
					fw.write(line + "\t" + NEG + "\t" + g.getNameNegConfigFile() + "\n");
				else
					fw.write(line + "\t" + AFF + "\t" + Statics.NONE + "\n");
			}
			// Prints out the scope on the screen for demonstration purposes.
			// CHANGE as you like.
			if(dison)
				System.out.println(scope);
		}
		file.close();
    }
	


	//post: process data and prints a result to a List<String>
    public static List<String> process(GenNegEx g, String textfile, boolean dison) throws IOException
	{
		List<String> listAffNeg = new ArrayList<String>();
		BufferedReader file = new BufferedReader(new FileReader(textfile));
        String line;
        while((line = file.readLine()) != null)
		{
			String[] parts = line.split("\\t");
			String sentence = cleans(parts[2]);
			String scope = g.negScope(sentence);	    
			if(scope.equals("-1")) 
				listAffNeg.add(line + "\t" + AFF + "\t" + Statics.NONE);
			else if(scope.equals("-2"))
				listAffNeg.add(line + "\t" + NEG + "\t" + g.getNameNegConfigFile());
			else
			{
				String keyWords = cleans(parts[1]); 
				if(contains(scope, sentence, keyWords))
					listAffNeg.add(line + "\t" + NEG + "\t" + g.getNameNegConfigFile());
				else
					listAffNeg.add(line + "\t" + AFF + "\t" + Statics.NONE);
			}
			// Prints out the scope on the screen for demonstration purposes.
			// CHANGE as you like.
			if(dison)
				System.out.println(scope);
		}
		file.close();
		
		return listAffNeg;
    }
	
	
	
    
    //post: returns true if a keyword is in the negation scope. otherwise, returns false 
    private static boolean contains(String scope, String line, String keyWords)
	{
		String[] token = line.split("\\s+");  
		String[] s = keyWords.trim().split("\\s+");  
		String[] number = scope.split("\\s+");
//System.out.println("scope=" + scope + "\nline=" + line + "\nkeyWords=" + keyWords);
//System.out.println("token.length=" + token.length + "\ns.length=" + s.length + "\nnumber.length=" + number.length);
		int counts = 0;  
		int iend = Integer.valueOf(number[2]);
		if(iend == token.length)
			iend = iend - 1;
		//for (int i = Integer.valueOf(number[0]); i <= Integer.valueOf(number[2]); i++)
		for (int i = Integer.valueOf(number[0]); i <= iend; i++)
		{
			if(s.length == 1)
			{
				if (token[i].equals(s[0]))
					return true;
			}
			else 
			{
				if ((token.length - i) >= s.length)
				{
					String firstWord = token[i];
					if(firstWord.equals(s[0]))
					{
						counts++;
						for(int j = 1; j < s.length; j++)
						{ 
							if(token[i + j].equals(s[j]))
								counts++;
							else
							{
								counts = 0;
								break;
							}
							if(counts == s.length)
								return true;
						}
					}
				}
			}
		}
		return false;
    }

    //post: removes punctuations
    private static String cleans(String line)
	{
		line = line.toLowerCase();
		if (line.contains("\""))
			line = line.replaceAll("\"", "");
		if (line.contains(","))
			line = line.replaceAll(",", "");  
		if (line.contains("."))
			line = line.replaceAll("\\.", "");
		if (line.contains(";"))
			line = line.replaceAll(";", "");
		if (line.contains(":"))
			line = line.replaceAll(":", "");
		return line;
    }





}







































