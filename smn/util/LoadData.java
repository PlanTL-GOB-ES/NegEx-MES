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
 



/**************************************************************************************************
* Load config files into List<String>
***************************************************************************************************/




 package smn.util;
 
 





import java.util.*;
import java.io.*;


import java.nio.charset.*;
import java.nio.file.*;



public class LoadData
{


	//pseudo neg phrases
	public static String relativeFilepnp = "." + Statics.FILE_SEP + "config_files" + Statics.FILE_SEP + "pseudo-neg-phrases.txt";
	public static File pnp = new File(relativeFilepnp);
	public static String filepnp = pnp.getAbsolutePath();

	//neg phrases
	private static String relativeFilenp = "." + Statics.FILE_SEP + "config_files" + Statics.FILE_SEP + "neg-phrases.txt";
	public static File np = new File(relativeFilenp);
	public static String filenp = np.getAbsolutePath();

	//post neg phrases
	private static String relativeFileponp = "." + Statics.FILE_SEP + "config_files" + Statics.FILE_SEP + "post-neg-phrases.txt";
	public static File ponp = new File(relativeFileponp);
	public static String fileponp = ponp.getAbsolutePath();


	//conjunctions
	public static String relativeFilec = "." + Statics.FILE_SEP + "config_files" + Statics.FILE_SEP + "conjunctions.txt";
	public static File c = new File(relativeFilec);
	public static String filec = c.getAbsolutePath();

	public LoadData()
	{
	}



	/****************************************
		LOAD
	****************************************/




	public void setData()
	{
		//pseudo neg phrases
		List<String> list = FileToString.asStringList(LoadData.filepnp, StandardCharsets.UTF_8);
		Statics.pseNegPhrases = preproConfigFiles(list);

		//neg phrases
		list = FileToString.asStringList(LoadData.filenp, StandardCharsets.UTF_8);
		Statics.negPhrases = preproConfigFiles(list);

		//post neg phrases
		list = FileToString.asStringList(LoadData.fileponp, StandardCharsets.UTF_8);
		Statics.postNegPhrases = preproConfigFiles(list);

		//conjunctions
		list = FileToString.asStringList(LoadData.filec, StandardCharsets.UTF_8);
		Statics.conjunctions = preproConfigFiles(list);
	}
	
	public void setData(String routeConfigFiles)
	{
		//pseudo neg phrases
		LoadData.filepnp = routeConfigFiles + "pseudo-neg-phrases.txt";
		List<String> list = FileToString.asStringList(LoadData.filepnp, StandardCharsets.UTF_8);
		Statics.pseNegPhrases = preproConfigFiles(list);

		//neg phrases
		LoadData.filenp = routeConfigFiles + "neg-phrases.txt";
		list = FileToString.asStringList(LoadData.filenp, StandardCharsets.UTF_8);
		Statics.negPhrases = preproConfigFiles(list);

		//post neg phrases
		LoadData.fileponp = routeConfigFiles + "post-neg-phrases.txt";
		list = FileToString.asStringList(LoadData.fileponp, StandardCharsets.UTF_8);
		Statics.postNegPhrases = preproConfigFiles(list);

		//conjunctions
		LoadData.filec = routeConfigFiles + "conjunctions.txt";
		list = FileToString.asStringList(LoadData.filec, StandardCharsets.UTF_8);
		Statics.conjunctions = preproConfigFiles(list);
	}

	//skip comments ":="
	public List<String> preproConfigFiles(List<String> liststr)
	{
		List<String> newListstr = new ArrayList<String>();
		for(int i = 0; i < liststr.size(); i++)
		{
			String str = liststr.get(i);
			if(!Statics.isComment(str))
			{
				newListstr.add(str);
			}
		}
		return newListstr;
	}



}






















