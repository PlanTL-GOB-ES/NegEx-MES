# NegEx-MES: NegEx para textos Médicos en ESpañol (NegEx algorithm for Spanish Medical texts)
--------------------------------------

## Introduction
---------------

This repository contains *NegEx-MES*, a system for negation detection in Spanish clinical texts based on Wendy Chapman's 
NegEx algorithm. It allows determining if a certain term (i.e. a word or group of words) is negated or not, and, if so, 
the negation word(s). Thus, for an input text that contains a line for each term for which you want to know if it is 
negated or not in the following format:

	identifier TAB term TAB "sentence". 

It provides the following output:

	identifier TAB term TAB "sentence" TAB modification TAB type_modification

Example:

	1 TAB cáncer TAB "El paciente no presenta cáncer ni anemia"
	1 TAB cáncer TAB "El paciente no presenta cáncer ni anemia" TAB Negated TAB negPhrases


This example indicates that the term 'cáncer' appears negated in this sentence and that the negation adverb *no* belongs 
to type 'negPhrases'.

The modification field can take only two values: Negated and Affirmed.
When the modification field is Affirmed, the value of type_modification is always NONE.
When the modification field is Negated, the type_modification field can take the following four values, ordered 
from highest to lowest degree of negation, which allow to detect not only negation, but also uncertainty.

* `negPhrases`: for words --adverbs (e.g. *no* (no), *tampoco* (neither)), negative predicates (e.g. *declina* 
(declines), *ausencia de* (absemce of), *rechazado* (rejected)) and prepositions (*sin* (without)-- that deny 
the term in question and are equivalent to the logical connective of the negation in the propositional logic. 
For example, if the term was fever, these words would deny this term: absence of fever, without fever, etc.

* `postNegPhrases`: for words that also indicate denial, but include some doubt, so they are a little less strict 
than the previous ones; e.g. *debe descartarse para/debe ser descartado para* (it should be discarded for), 
*improbable* (unlikely), etc.

* `pseNegPhrases`: for words very similar to the previous ones, but they include the doubt directly between their  
possibilities; e.g. *no estoy seguro si* (I am not sure if), *dudo* (I doubt), *tengo dudas* (I have doubts), etc.

* `conjunctions`: for adversative conjunctions: pero, sin embargo, aunque, etc. (however, but, if not...).  
That is, they contradict, partially or totally, the term. Therefore, they allow to determine the degree of 
uncertainty that a term has with respect to the phrase in which it appears.

Distinguishing these different degrees of negation can be very useful, for example, to assign a specific weight to 
the terms according to the negation words that co-occur with them. For example, a weight of 0.25 could be assigned to the 
terms co-ocurring with (1), 0.50 to those co-ocurring with (2), 0.75 to those co-ocurring with (3), and 0.85 to those 
co-ocurring with (4).


## Directory structure
---------------------

The directory structure corresponds to package nomenclature called *smn*. 
Therefore, all packages are within that structure:

<pre>
smn/config_files/eng/with_lemma/
Includes the 4 files with the modifiers explained above, in English and lemmatized.

smn/config_files/eng/without_lemma/
Includes the 4 files with the modifiers explained above, in English and without lemma.

smn/config_files/spa/with_lemma/
Includes the 4 files with the modifiers explained above, in Spanish and lemmatized.

smn/config_files/spa/without_lemma/
Includes the 4 files with the modifiers explained above, in Spanish and without lemma.

smn/in/
Includes the input file in.txt

smn/main/
Includes the main class Main.java and the execution JAR file smn.jar.

smn/misc/
Includes the modification algorithm. 

smn/out/
Includes the output file callKit.result.

smn/util/
includes utility java classes.
</pre>


## Usage
--------

To install and compile *NegEx-MES* you can consult the file [Intallation.md.] (https://github.com/PlanTL/SpanMedNeg/blob/master/Installation.md). 
In this section we will assume that it has been installed and compiled correctly and we only show some execution examples.

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

If we execute all the options that *NegEx-MES* provides, as in (in Linux, for Windows it is the same but changing 
"/" to "\\"):

<pre>
java smn.main.Main -displayon true -language SPANISH -answerOptionYes true -isOuputFileGenerated true -lemmaConfigFiles false -routeConfigFiles ../config_files/ -routeInTextFile ../in/in.txt -routeOutTextFile ../out/out.txt
</pre>

This generates an output file in the directory "out" with the following line: 

	1	cáncer	"El paciente no presenta cáncer ni anemia"	Negated	negPhrases


### Execution via JAR file
--------------------------

The smn.jar file allows to execute *NegEx-MES* directly from a terminal such as cmd, terminator, etc.
To do this, write the following command line (from the directory where smn.jar is located):

<pre>
java -jar smn.jar [options]
</pre>

Where *options* are those shown in the 'Usage' section.


The smn.jar file, as indicated above, is found at smn/main/smn.jar. 

So, if we move to the 'main' folder and type this:
<pre>
java -jar smn.jar
</pre>

*NegEx-MES* will take the default options, and being in the directory structure 'by default':
* It will be executed in the `in.txt file`, that is in `smn/in/in.txt`.
* It will be executed with the configuration files that are at `smn/config_files/`
* It will generate an output file called `callKit.result` at `smn/out/callKit.result`

If we change `smn.jar` to another directory, we must specify these routes in the options, so that it works correctly.
For example, if we move `smn.jar` at the `smn` parent directory, we could execute it in the following manner:

<pre>
java -jar smn.jar -routeConfigFiles ./smn/config_files/ -routeInTextFile ./smn/in/in.txt -routeOutTextFile ./out.txt
</pre>

What will cause it to run with the input file and the previous configuration files, but it will generate an output file 
at the `smn` parent directory named `out.txt`.

Finally, the input folder `smn/in/in.txt` includes an example with 7 cases, in Spanish, and the output folder 
`smn/out/callKit.result` the output that *NegEx-MES* generates for that input text. To reproduce the results obtained 
from `smn/main/`, execute the following command :

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

