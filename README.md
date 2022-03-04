# Assignment 1 README #

Glenn Hartwell  
Spring 2022

I will start by saying that if you're using vscode to view this then you can press ctrl+shift+V to change the view so this will look pretty for you.

## Definitions ##

**/directory_of_file**: this is the location where the file is saved on your computer

## Compilation ##

To compile and run the Labyrinth code at the command line in Ubuntu or Windows Powershell please do the following:  

>`> /directory_of_file javac Labyrinth.java`  
>`> /directory_of_file java Labyrinth`

To compile and run the Crystal Vase code at the command line in Ubuntu or Windows Powershell please do the following:  

>`> /directory_of_file javac CrystalVase.java`  
>`> /directory_of_file java CrystalVase`

## Problem 2 Stategy Discussion ##

After reading through the different strategies for viewing the Minotaur's crystal vase I realized that they were  
describing the exact protocols from some of the locks we learned in chapter 7.  

The first protocol follows the same idea that a TAS lock does. This allows the thread to constantly bombard the cpu with requests to acquire the lock.  
While this strategy will guarantee deadlock-free mutual exclusion it does not provide the best possible efficiency overall. However, I will note that  
I believe that the Minotaur would greatly enjoy the boost to his ego that a crowd around the door would cause.  

The second protocol follows the same idea that a TTAS lock does. The sign on the door represents the intial test to see if the shared resource is   
available that a TTAS lock makes. If that check fails the lock is theoretically free to do other things. In our case that would give guests the ability  
roam the castle. This approach is significantly faster because the cpu only has to service a request to acquire the lock if the intial test passes.  
This method allows guests to do other things if they would like to and gives them the ability to enter the showroom quickly.  

The third protocol follows the many Queue locks that the book describes in chapter 7. Using a queue adds more portability between machines,  
or in our case parties. This method will make sure that anyone that wants to see the vase eventually wiil, but at the cost of not being able  
to do other tasks in the down time. This method is the most fair to the guests, but who wants to stand in line?  

I chose to implement the second protocol with a personalized version of the TTAS lock that was provided by the textbook.  

