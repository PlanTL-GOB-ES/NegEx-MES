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
 




/************************************************************
 * Utility class to calculate the accuracy of negation
 * detection on the testkit.
 *************************************************************/


 package smn.misc;
 
 
 
import java.io.*;
import java.util.*;

public class Accuracy {

    public static void main(String[] args) throws IOException {
	if (args.length != 1) {
	    System.out.println("Usage: please, specify a text file.");
	    return;
	}
	List<String> data = new LinkedList<String>();
	process(data, args[0]);
	printResult(data);
    }
     
    // post: process data
    public static void process(List<String> data, String textfile) throws IOException {
	BufferedReader file = new BufferedReader(new FileReader(textfile));
        String line;
        while ((line = file.readLine()) != null) {
	    String[] s = line.trim().split("\\t");
	    String person = s[3].toLowerCase();
	    String computer = s[4].toLowerCase();
	    if (person.equals("negated"))
		if (computer.equals("negated"))
		    data.add("1|0|0|0");
		else  
		    data.add("0|0|0|1");
	    else 
		if (computer.equals("negated"))
		    data.add("0|1|0|0");
		else 
		    data.add("0|0|1|0");
	}
	file.close();
    }
    
    // post: prints the outcome 
    public static void printResult(List<String> data) {
	int tp = 0;
	int fp = 0;
	int tn = 0;
	int fn = 0;
	for (String token : data) {
	    String[] s = token.split("\\|");
	    if (s[0].equals("1")) 
		tp++;
	    else if (s[1].equals("1"))
		fp++;
	    else if (s[2].equals("1"))
		tn++;
	    else 
		fn++;
	}
	double recall = (double) tp / (tp + fn);
	double precision = (double) tp / (tp + fp);
	double f = (2 * recall * precision) / (recall + precision);
	double accuracy = (double) 100 * (tp + tn) / (tp + tn + fp + fn);
	     
	System.out.println("# Using the test kit, printout should look like:");
	System.out.println("GS Negated: " + (tp + fn));
	System.out.println("GS Affirmed: " + (fp + tn));
	System.out.println();
	  
	System.out.println("System Negated: " + (tp + fp));
	System.out.println("System Affirmed: " + (tn + fn));
	System.out.println();
	  
	System.out.println("TP or Correct Negated: " + tp);
	System.out.println("TN or Correct Affirmed: " + tn);
	System.out.println("FP or False Negated: " + fp);
	System.out.println("FN or False Affirmed: " + fn);
	System.out.println();
	  
	System.out.println("Recall: " + recall);
	System.out.println("Precision: " + precision);
	System.out.println("F-measure: " + f);
	System.out.println("Correct Negated + Correct Affirmed/Total * 100 = " + accuracy);
	System.out.println("Total: " + (tp + fp + tn + fn));
    }
}
