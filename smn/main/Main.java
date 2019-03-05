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
 


package smn.main;


import smn.util.Statics;
import smn.misc.GenNegEx;


import java.io.*;
import java.util.*;



public class Main implements Serializable
{

	private static final long serialVersionUID = -7149755349268484907L;


	private static final String HELP = "-help"; //show the usage method
	private static final String DISON = "-displayon"; //show the messages at the standard output
	//
	private static final String LAT = "-language"; //Name of the input language. Default Spanish.
	//
	private static final String ANOP = "-answerOptionYes"; //TRUE (Yes) or FALSE (No). Default: TRUE (Yes)
	private static final String IOFG = "-isOuputFileGenerated"; //TRUE generate output file, FALSE generate List. Default TRUE.
    private static final String LEMCONF = "-lemmaConfigFiles"; //Configuration files with lemma (TRUE) or without lemma (FALSE). Default TRUE.
	//
	private static final String RCF = "-routeConfigFiles"; //config files folder name. Default: in ../config_files/
	//
	private static final String RITF = "-routeInTextFile"; //name of the input text file. Default: in ../in/in.txt
	private static final String ROTF = "-routeOutTextFile"; //name of the output text file. Default: in ../out/callKit.result

	
	String routeConfigFiles = "../config_files/";
	String routeInTextFile = "../in/in.txt";
	String routeOutTextFile = "../out/callKit.result";
					
	String language = "SPANISH";
	boolean answerOptionYes = true; //true = yes; false == no
    boolean lemmaConfigFiles = true;
	boolean dison = true;
	//
	boolean isOuputFileGenerated = true;
	List<String> answerList = new ArrayList<String>();
	//


	
	public Main(String args[])
	{
		this.routeConfigFiles = this.routeConfigFiles.replace("/", Statics.FILE_SEP);
		this.routeInTextFile = this.routeInTextFile.replace("/", Statics.FILE_SEP);
		this.routeOutTextFile = this.routeOutTextFile.replace("/", Statics.FILE_SEP);
		
		startMain(args);
	}


	public static void main(String args[]) throws IOException
	{
		Main m = new Main(args);
	}
	
	
	/**
	* Starts the text mode interface.
	*  
	* @param args the commandline arguments.
	*/
	public void startMain(String[] args)
	{
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals(HELP))
			{
				usage();
			}
			//
			else if(args[i].equals(DISON))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.dison = Boolean.parseBoolean(args[i].toLowerCase());
			}
			//
			else if(args[i].equals(LAT))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.language = args[i].toUpperCase();
			}
			else if(args[i].equals(ANOP))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.answerOptionYes = Boolean.parseBoolean(args[i].toLowerCase());
			}
            else if(args[i].equals(LEMCONF))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.lemmaConfigFiles = Boolean.parseBoolean(args[i].toLowerCase());
			}
			//
			else if(args[i].equals(IOFG))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.isOuputFileGenerated = Boolean.parseBoolean(args[i].toLowerCase());
			}
			//
			else if(args[i].equals(RCF))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.routeConfigFiles = args[i];
			}
			else if(args[i].equals(RITF))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.routeInTextFile = args[i];
			}
			else if(args[i].equals(ROTF))
			{
				i++;
				if(i >= args.length)
				{
					usage();
				}
				this.routeOutTextFile = args[i];
			}
			//
			else //other parameter
			{
				System.err.println("The parameter " + args[i] + " is not among the possibilities offered");
				System.exit(0);
			}
		}
		
		//set route config_files by language
		if(this.language.equals("SPANISH"))
			this.routeConfigFiles += "spa" + Statics.FILE_SEP;
		else
			this.routeConfigFiles += "eng" + Statics.FILE_SEP;
        //set route config_files by lemma
		if(this.lemmaConfigFiles)
			this.routeConfigFiles += "with_lemma" + Statics.FILE_SEP;
		else
			this.routeConfigFiles += "without_lemma" + Statics.FILE_SEP;
		
		
		
		if(this.isOuputFileGenerated)
		{
			if(this.dison)
				System.out.println("executing modifier: result to a file");
			execFileGenerated();
		}
		else
		{
			if(this.dison)
				System.out.println("executing modifier: result to a List<String>");
			execListGenerated();
			if(this.dison)
			{
				System.out.println("\n\tRESULT");
				System.out.println(this.getAnswerList().toString());
			}
				
		}
	}
	
	public void setAnswerList(List<String> answerList)
	{
		this.answerList = answerList;
	}
	
	public List<String> getAnswerList()
	{
		return this.answerList;
	}
	
	
	
	
	
	
	public void execFileGenerated()
	{
		File existed = new File(this.routeOutTextFile);
		if(existed.exists())
			existed.delete();
		try
		{
			FileOutputStream fos = new FileOutputStream(this.routeOutTextFile, true); //true := append
			OutputStreamWriter osw = new OutputStreamWriter(fos, java.nio.charset.StandardCharsets.ISO_8859_1);
			BufferedWriter bw = new BufferedWriter(osw);
			GenNegEx g = new GenNegEx(this.answerOptionYes, this.routeConfigFiles);
			Statics.process(bw, g, this.routeInTextFile, this.dison);
			bw.close();
			/*
			FileWriter fw = new FileWriter(this.routeOutTextFile, true);
			GenNegEx g = new GenNegEx(this.answerOptionYes, this.routeConfigFiles);
			Statics.process(fw, g, this.routeInTextFile, this.dison);
			fw.close();
			*/
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//generate list
	public void execListGenerated()
	{
		try
		{
			FileWriter fw = new FileWriter(this.routeOutTextFile, true);
			GenNegEx g = new GenNegEx(this.answerOptionYes, this.routeConfigFiles);
			this.answerList = Statics.process(g, this.routeInTextFile, this.dison);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	
	//get modifier's name
	public String modifierName(String term)
	{
		//load parameters from config files
		//LoadData dat = new LoadData();
		//dat.setData(this.routeConfigFiles);

		
		for(String element : Statics.pseNegPhrases)
		{
			if(element.equals(term))
				return Statics.PSE;
		}
		for(String element : Statics.negPhrases)
		{
			if(element.equals(term))
				return Statics.NEG;
		}
		for(String element : Statics.postNegPhrases)
		{
			if(element.equals(term))
				return Statics.POS;
		}
		for(String element : Statics.conjunctions)
		{
			if(element.equals(term))
				return Statics.CON;
		}
		return null;
	}
	
	


	
	
	//This will print the usage requirements and exit.
	private static void usage()
	{
		String message = "Usage: java smn.main.Main [options]\n"
				+ "\nOptions:\n"
				+ "\t-help\t\t\t<>\t\t\t: Show this message\n"
				//
				+ "\t-displayon\t\t\t<boolean>\t\t\t: Show the messages at the standard output. Default TRUE (show)\n"
				//
                + "\t-language\t\t\t<string>\t\t\t: Name of the input language. Default Spanish.\n"
                + "\t-answerOptionYes\t\t\t<boolean>\t\t\t: TRUE (Yes) or FALSE (No). Default: TRUE (Yes)\n"
				+ "\t-isOuputFileGenerated\t\t\t<boolean>\t\t\t: TRUE generate output file, FALSE generate List. Default TRUE.\n"
                + "\t-lemmaConfigFiles\t\t\t<boolean>\t\t\t: Configuration files with lemma (TRUE) or without lemma (FALSE). Default TRUE (with lemma).\n"
				//
				+ "\t-routeConfigFiles\t\t\t<string>\t\t\t: Config files folder name. Default: in ../config_files/\n"
				//
				+ "\t-routeInTextFile\t\t\t<string>\t\t\t: Name of the input text file. Default: in ../in/in.txt\n"
				+ "\t-routeOutTextFile\t\t\t<string>\t\t\t: Name of the output text file. Default: in ../out/callKit.result";
		System.err.println(message);
		System.exit(1);
	}
	

}








































