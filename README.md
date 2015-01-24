GlosTrainer
===========

GlosTrainer is a word learning application for English-speaking learners of 
the Swedish language. Made by Robert Sebescen, written in Java SE 1.8.

<img src="http://i.imgur.com/pdYVTZ9.png" title="The main GUI GlosTrainer" alt="The main GUI of GlosTrainer"/>

<img src="http://i.imgur.com/mfhBMnw.png" title="An edit form" alt="An edit form"/>


This is a complete rewrite of an application that I wrote as a final project for a
course in C# (the source code for that is also available here on my Github). 
The Java version is platform-independent and the code follows the MVC model 
for a more robust and easy-to-overview structure.

How to run the application:
------
- Make sure you have Java 1.8 or higher installed.
- Download, extract and run GlosTrainer.zip, then double-click on GlosTrainer.jar to run
- The lib folder needs to be in the same folder as GlosTrainer.jar to make the icons visible

GlosTrainer features:
------
* a simple, user-friendly GUI for adding, editing, deleting, filtering and sorting 
words. Optional word forms for some word classes, such as definitive and plural
forms for nouns are available. Thus user can add own notes to the word entries.
* filtering of the word table. This can be made by exact or partial matches, as 
well as being limited to  only dictionary forms of words, or expanded to all forms 
of words.  
* supports importing and exporting word lists to/from files.

If you wish to download the source code of the project, NetBeans should
recognize the files as a project from this level in the file hierarchy. 
The build.xml ANT file automates creation of a runnable jar file for the application, 
as well as zipping that file and the image folder for easy distribution.





A future release of the program will include flashcard/quiz mode and other features.