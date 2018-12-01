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
 



/*********************************************************************************************************************************
 * Wendy Chapman's NegEx algorithm in Java.
 *
 * Sentence boundaries  serve as WINDOW for negation (suggested by Wendy Chapman)
 *
 *
 * NOTES: 
 * If the negation scope exists in a sentence, it will print the negation scope such as  1 - 1, 0 - 24.
 * If the negation does not exist in a sentence, it will print -1
 * If a pre-UMLS phrase is used as a post-UMLS phrase, for example, pain and fever denied, 
 * 		it will print the negation scope of, in this case, 0 - 2, for an option of yes or print -2 for an option of no
 *
 ***********************************************************************************************************************************/

 

package smn.misc;
 

import smn.util.Statics; 
import smn.util.LoadData;
 
import java.util.*;

public class GenNegEx
{
    private List<String> pseNegPhrases;     // list of pseudo-negation phrases
    private List<String> negPhrases;        // list of negation phrases
    private List<String> postNegPhrases;    // list of post-negation pharses
    private List<String> conjunctions;      // list of conjunctions
    private boolean value;                  // boolean for an option of yes or no
	private String routeConfigFiles;		// config files path
	
	private String nameNegConfigFile = Statics.NONE;
    
    //post: constructs a GenNegEx object
    //creates a list of negation phrases, pseudo-negation phrases, post-negation phrases, and conjunction
    public GenNegEx(boolean value, String routeConfigFiles)
	{
		this.routeConfigFiles = routeConfigFiles;
		pseNegPhrases = new LinkedList<String>();
		negPhrases = new LinkedList<String>();
		postNegPhrases = new LinkedList<String>();
		conjunctions = new LinkedList<String>();
		setPhrases(pseNegPhrases, negPhrases, postNegPhrases, conjunctions);
		sorts(pseNegPhrases);
		sorts(negPhrases);
		sorts(postNegPhrases);
		sorts(conjunctions);
		this.value = value;
    }
     
    //post: sorts a list in descending order
    private void sorts(List<String> list)
	{
		Collections.sort(list);
		Collections.reverse(list);
    }
     
    //post: returns a negation scope of an input sentence
    public String negScope(String line)
	{
		String[] s = line.split("\\s+");
		return helper(s, 0);
    }
    
    //post: processes data and returns negation scope
    //returns -1 if no negation phrase is found
    private String helper(String[] s, int index)
	{
		if (index < s.length)
		{
			for (int i = index; i < s.length; i++)
			{
				int indexII = contains(s, pseNegPhrases, i, 0);
				if (indexII != -1)
				{
					//System.out.println("NEG pseNegPhrases");
					this.nameNegConfigFile = Statics.PSE;
					return helper(s, indexII);
				}
				else
				{
					int indexIII = contains(s, negPhrases, i, 0);
					if (indexIII != -1)
					{
						//System.out.println("NEG negPhrases");
						this.nameNegConfigFile = Statics.NEGPH;
						int indexIV = -1;
						for (int j = indexIII; j < s.length; j++)
						{
							indexIV = contains(s, conjunctions, j, 1);
							if (indexIV != -1)
							{
								//System.out.println("NEG conjunctions");
								this.nameNegConfigFile = Statics.CON;
								break;
							}
						}
						if (indexIV != -1)
							return indexIII + " - " + indexIV;
						else
						{
							if (indexIII > s.length - 1)
							{
								if(value)
									return "0 - " + (indexIII - 2);
								else
									return "-2";
							}
							else
								return indexIII + " - " + (s.length - 1);
						}
					}
					else
					{
						int indexV = contains(s, postNegPhrases, i , 1);
						if (indexV != -1)
						{
							//System.out.println("NEG postNegPhrases");
							this.nameNegConfigFile = Statics.POS;
							return "0 - " + indexV;
						}
					}  
				} 
			}
		}
		return "-1";
    }
    
    //post: returns index of negation phrase if any negation phrase is found in a sentence
    //returns -1 if no negation phrase is found
    private int contains(String[] s, List<String> list, int index, int type)
	{
		int counts = 0;
		for (String token : list)
		{
			String[] element = token.split("\\s+");
			if(element.length == 1)
			{
				if(s[index].equals(element[0]))
					return index + 1;
			}
			else
			{
				if(s.length - index >= element.length)
				{
					String firstWord = s[index];
					if(firstWord.equals(element[0]))
					{
						counts++;
						for(int i = 1; i < element.length; i++)
						{
							if (s[index + i].equals(element[i])) 
								counts++;
							else
							{
								counts = 0;
								break;
							}
							if(counts == element.length)
							{
								if (type == 0) 
									return index + i + 1;
								else
									return index;
							}
						}
					}
				}
			}
		}
		return -1;
    }     

	//post: saves pseudo negation phrases, negation phrases, conjunctions into the database
	private void setPhrases(List<String> pseNegPhrases, List<String> negPhrases, List<String> postNegPhrases, List<String> conjunctions)
	{
		//load parameters from config files
		LoadData dat = new LoadData();
		dat.setData(this.routeConfigFiles);

		this.pseNegPhrases = Statics.pseNegPhrases;
		this.negPhrases = Statics.negPhrases;
		this.postNegPhrases = Statics.postNegPhrases;
		this.conjunctions = Statics.conjunctions;
	}
	
	public String getNameNegConfigFile()
	{
		return this.nameNegConfigFile;
	}
	
	public void setNameNegConfigFile(String nameNegConfigFile)
	{
		this.nameNegConfigFile = nameNegConfigFile;
	}



}






















