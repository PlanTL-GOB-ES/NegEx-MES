# SpanMedNeg: Spanish Medical Negation
----------------------------------------

## Introduction
---------------

This repository contains a system for negation detection in Spanish clinical texts based on Wendy Chapman's NegEx algorithm. 
It allows determining if a certain term (i.e. a word or group of words) is negated or not and, if so, the negated phrase. 

For an input text that contains a line for each term for which you want to know if it is negated or not, in the following 
format:

	identifier TAB term TAB "sentence". 

It provides the following output:

	identifier TAB term TAB "sentence" TAB modification TAB type_modification

For example:

	1 TAB cáncer TAB "El paciente no presenta cáncer ni anemia"
	1 TAB cáncer TAB "El paciente no presenta cáncer ni anemia" TAB Negated TAB negPhrases


This indicates that the term 'cáncer' appears negated in this sentence and that the negation phrase ('no') is in 
the 'negPhrases' config file.

The modification field can take only two values: Negated and Affirmed.
When the modification field is Affirmed, the value of type_modification is always NONE.
When the modification field is Negated, the type_modification field can take the following four values, ordered 
from highest to lowest degree of modification,  which allow to detect not only negation, but also uncertainty.

* Neg-phrases contains the most radical modifiers (e.g. ausencia de, rechazado, declina, etc.). That is, it contains 
modifiers that deny the term in question, and are equivalent to the logical connective of the negation in the 
propositional logic. For example, if the term were fever, these modifiers would deny this term: absence of fever, 
without fever, etc.

* Post-neg-phrases, also indicates denial, but includes some doubt, so it is a little less strict than the previous one; 
e.g. debe descartarse para, debe ser descartado para, improbable, etc.

* Pseudo-neg-phrases is very similar to the previous one, but it includes the doubt directly between its possibilities: 
I am not sure if, I doubt, I have doubts, sin aumento, sin cambios sospechosos, tengo dudas, etc.

* Conjunctions, and correspond to the adversative conjunctions; pero, sin embargo, aunque, etc.. That is, they contradict,
partially or totally, the term: however, but, if not, etc. These modifiers, therefore, allow to determine the degree of 
uncertainty that a term has with respect to the phrase in which it appears.

This can be very useful for applications that use SpanMedNeg, for example, to assign a specific weight to the terms 
according to their 'degree' of modification. For example, a weight of 0.25 could be assigned to the terms modified with 
(1), 0.50 to those modified with (2), 0.75 to those modified with (3), and 0.85 to those modified with (4).


## Directory structure
---------------------

The directory structure corresponds to package nomenclature called *smn*. 
Therefore, all packages are within that structure:

* smn/config_files/eng/with_lemma/: includes the 4 files with the modifiers explained above, in English and lemmatized.
* smn/config_files/eng/without_lemma/: includes the 4 files with the modifiers explained above, in English and without lemma.
* smn/config_files/spa/with_lemma/: includes the 4 files with the modifiers explained above, in Spanish and lemmatized.
* smn/config_files/spa/without_lemma/: includes the 4 files with the modifiers explained above, in Spanish and without lemma.
* smn/in/: includes the input file in.txt
* smn/main/: includes the main class Main.java and the execution JAR file smn.jar.
* smn/misc/: includes the modification algorithm. 
* smn/out/: includes the output file callKit.result.
* smn/util/: includes utility java classes.


## Usage
--------

To install and compile *Spanish Medical Negation* you can consult the file [Intallation.md.](https://github.com/PlanTL/SpanMedNeg/blob/master/Installation.md)
In this section we will assume that it has been installed and compiled correctly, and we only show some execution examples.

java smn.main.Main [options]

Options:
<pre>
  -help	
  	Show the line to execute *Spanish Medical Negation* and the options.
  -displayon <boolean>
  	Show the messages at the standard output. Default TRUE (show).
  -language <language>
  	SPANISH (default) or ENGLISH.
  -answerOptionYes <boolean>
  	If a pre-UMLS phrase is used as a post-UMLS phrase, for example, pain and fever denied, 
	it will print the negation scope of, in this case, 0 - 2, for an option of yes (TRUE) or 
	print -2 for an option of no (FALSE). Default TRUE.
  -isOuputFileGenerated <boolean>
  	If TRUE, it generates an output file; if FALSE, it generates a List class. Default TRUE.
  -lemmaConfigFiles <boolean>
	Configuration files with lemma (TRUE) or without lemma (FALSE). Default TRUE (with lemma).
  -routeConfigFiles <string>
	Config files folder name. Default: in ../config_files/
  -routeInTextFile <string>
	Name of the input text file. Default: in ../in/in.txt
  -routeOutTextFile <string>
	Name of the output text file. Default: in ../out/callKit.result
</pre>


## Examples
-----------

Let's assume an input file "in.txt" in the directory "in" that includes the following line: 

	1	cáncer	"El paciente no presenta cáncer ni anemia"

If we execute all the options that *Spanish Medical Negation* provides, as in (in Linux, for Windows it is the same but changing "/" to "\\"):

<pre>
java smn.main.Main -displayon true -language SPANISH -answerOptionYes true -isOuputFileGenerated true -lemmaConfigFiles false -routeConfigFiles ../config_files/ -routeInTextFile ../in/in.txt -routeOutTextFile ../out/out.txt
</pre>

This generates an output file in the directory "out" with the following line: 

	1	cáncer	"El paciente no presenta cáncer ni anemia"	Negated	negPhrases


### Execution via JAR file
--------------------------

The smn.jar file allows to execute *Spanish Medical Negation* directly from a terminal such as cmd, terminator, etc.
To do this, you have to write the following command line (from the directory where smn.jar is located):

<pre>
java -jar smn.jar [options]
</pre>

Where *options* are those shown in the 'Usage' section.
For example, if we type:

<pre>
java -jar smn.jar -help

</pre>
*Spanish Medical Negation* will show the allowed options, that is:
<pre>
Usage: java smn.main.Main [options]
Options:
   -help                   <>          : Show this message
   -displayon              <boolean>   : Show the messages at the standard output. Default TRUE (show)
   -language               <string>    : Name of the input language. Default Spanish.
   -answerOptionYes        <boolean>   : TRUE (Yes) or FALSE (No). Default: TRUE (Yes)
   -isOuputFileGenerated   <boolean>   : TRUE generate output file, FALSE generate List. Default TRUE.
   -lemmaConfigFiles       <boolean>   : Configuration files with lemma (TRUE) or without lemma (FALSE). Default TRUE (with lemma).
   -routeConfigFiles       <string>    : Config files folder name. Default: in ../config_files/
   -routeInTextFile        <string>    : Name of the input text file. Default: in ../in/in.txt
   -routeOutTextFile       <string>    : Name of the output text file. Default: in ../out/callKit.result
</pre>
The smn.jar file, as indicated above, is found at smn/main/smn.jar
So, if we move to the 'main' folder and type this:
<pre>
java -jar smn.jar
</pre>

*Spanish Medical Negation* will take the default options, and being in the directory structure 'by default' will be executed:
* In the in.txt file, that is in smn/in/in.txt
* With the configuration files that are at smn/config_files/
* It will generate an output file called callKit.result at smn/out/callKit.result

If we change smn.jar to another directory, we must specify these routes in the options, so that it works correctly.
For example, if we move smn.jar at the 'smn' parent directory, we could execute it in the following manner:
java -jar smn.jar -routeConfigFiles ./smn/config_files/ -routeInTextFile ./smn/in/in.txt -routeOutTextFile ./out.txt
What will cause it to run with the input file and the previous configuration files, but it will generate an output file at the 'smn' parent directory named 'out.txt'.

Finally, we would like to mention that in the input folder ('smn/in/in.txt') we include an example with 7 cases, in Spanish. Also in the output folder ('smn/out/callKit.result') we include the output that *Spanish Medical Negation* generates for that input text. In case you want to reproduce the results obtained, *Spanish Medical Negation* was executed with the following parameters to generate the output text (from smn/main/):

<pre>
java -jar smn.jar -lemmaConfigFiles false
</pre>


## Contact
---------
Jesús Santamaría (jsantamaria@cnio.es)


## License
----------

MIT License

Copyright (c) 2018 Secretaría de Estado para el Avance Digital (SEAD)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

