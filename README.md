# Minesweeper
Table of Contents
[TOC]
***
# 1 Introduction #
### 1.1 Purpose ###
This software design document describes the architecture and system design of *Project 2 - Minesweeper*. The intended audience is someone who has taken an introductory course in computer science and object oriented programming.
### 1.2 Scope ###
This software allows a person to play a game of Minesweeper. The game is set up with a 10x10 grid and 10 mines. A timer is used to keep time, and the top 10 fastest times are recorded. Help instructions explaining how to play the game and the game has reset functionality to allow the user to restart or play a new game.
### 1.3 Overview ###
This document is organized into seven different major sections, with some subsections. A table of contents is provided at the top to allow for easy navigation. This is a comprehensive document that will familiarize one with the design and workings of the software.
***
# 2 System Overview #
The software was designed using several components. Referring to the "Head Start: Design Patterns" book that was assigned to the class, we designed the software using *Model View Controller* software architecture. The software does provides time keeping, random board generation, and also makes use of simple graphics to indicate a mine.

***
# 3 System Architecture #
### 3.1 Architectural Design ###
The high level system is best described as *Model View Controller* (MVC). Using this paradigm, the game is split up into three different components.
* Model -- There are two models -- *ScoreModel* and *MinesweeperModel*. *ScoreModel* provides all score keeping information for the top scorers. *MinesweeperModel* provides the model for a game of Minesweeper, which includes where all the mines are, what numbers belong in each tile, and the game's state.
* View -- BoardView is the view/GUI for the game. All GUI components are created here and it has minimum logic. Controllers are attached here to facilitate actions/data between the Model and View.
* Controller -- Two controllers are provided. *MenuController* handles all menu interactions, and *ButtonController* handles all button presses with either mousebutton.
* There is also a *Game* class that instantiates the *BoardView* and *MinesweeperModel*. BoardView takes care of instantiating all the controllers and *ScoreModel*.

![MVC.png](https://bitbucket.org/repo/j7y7dn/images/4182882404-MVC.png)

### 3.2 Decomposition Description ###
The systems shown in the diagram from section 3.1 are the Model, View, and Controller.

The Model system is divided into two more subsystems i.e. MinesweeperModel and ScoreModel. For MinesweeperModel, this is where the state information such as where the mines are located, what each value is in each square (the number of mines around it) and other methods that involve the board.  For ScoreModel, this is the logic for adding scores, sorting, and saving the file.

The View system has one subsystem i.e. BoardView. This system is used to display the graphical part of the game such as, grid, panels, mines, and the menus.

The Controller system is further divided into two subsystems i.e. MenuController and ButtonController. In detail, the Controller system is used to send commands to update the model. The MenuController handles all Menu operations. The ButtonController handles all button operations such as left click and right click.

Flowchart of Minesweeper game:
![minesweeper_flowchart.png](https://bitbucket.org/repo/j7y7dn/images/3712782254-minesweeper_flowchart.png)  
*(image via Google)*

### 3.3 Design Rationale ###
From reading texts on GUI programs, *MVC* is a preferred software architecture. Using this architecture, it was very easy to get the behavior of buttons to be consistent and easily replicated to any grid size desired. This also greatly simplified the way the game is Reset. Simply creating a new instance of MinesweeperModel and plugging it into the View is a huge advantage. Since the classes are decoupled from each other, the code is reduced and reusable. For example, the ScoreModel can be used in any future game that requires top scores. It also made the game easier to create since we could work on the components separately and test them as they became ready. The game was able to be divided into smaller sub systems that could be made concurrently.  

Another reason this architecture was chosen is that is provided opportunities to write unit testable code. Both model classes were able to be unit tested thoroughly to make us more confident that our code did not have issues and behaved as expected.  

We did not run into any critical issues with this design. The only trade off is development time was slightly longer than anticipated due to this being the first project we applied this architecture to.  
We considered another approach in which all the components would have been in the view class itself, however the complexity of the code would have been much more complicated and it would have had more room for error as well. It would also have had less opportunities to for unit testing parts of the code.

***
# 4 Data Design #
### 4.1 Data Description ###
**Data Structures**  
In the MinesweeperModel class, the board information is saved in a two dimensional array of an enum type called *Minefield*. This enum contains all possible values each element on a board can be. This is done to prevent invalid board by guaranteeing it contained values in the enum.

In the ScoreModel class, an ArrayList is the structure used to store the top 10 scores list. It uses an inner class that defines a UserScore which contains a player's name and their score. Using this List allows us to use the Collections class to sort it.  

The BoardView uses a two-dimensional array of buttons to create the grid. Each button is tied to its own controller than corresponds to an element in the MinesweeperModel.  

The controllers themselves do not contain any data structures. They use references provided to them by the models and view class to perform their operations.

**Data Storage**  
The only data storage item that is used in the project is *scores.bin*. This file is used to save the top 10 scores. When there is a top score, the file is created if it does not exist. If it exists, the file is loaded and updated. This is achieved by making the ScoreModel a serializable class and writing the ArrayList object to device storage.

### 4.2 Data Dictionary ###
The data dictionary is provided in the form of Javadocs.  
[Javadoc for project2](http://acolon.bitbucket.org/project2/)
***
# 5 Component Design #
As mentioned above, we used Model-View-controller to implement the user interface for our game. 

**Models and View**  
The data structures like MinesweeperModel and BoardView creates the Model-View part of Model-view-controller. A Model provides the logic and state models of the data for the game. The game uses two models: MinesweeperModel and ScoreModel. The algorithm for creating a board in MinesweeperModel is placing 10 mines randomly on the board. Once those mines are placed, then each element on the board is looked at along with its surrounding elements to determine what number should be placed in that element. There are no algorithms associated with the ScoreModel.

The BoardView is the graphical part that will allow the user to interact with the game. A View is told by the controller all the information it needs for generating an output representation to the user. It operates the grid, buttons, menu items and the mines for the graphical interface. The algorithm associated with BoardView is straight forward -- it simply runs two for loops (one nested) to generate the two dimensional grid.

**Controllers**  
The classes ButtonController and MenuController are the Controller part of our user interface. A Controller can send commands to the model to update the model's state. The ButtonController allows the user to click on the buttons that will perform several tasks. By left-clicking the buttons, the button controller will reveal whatever hides behind the button. If we right-click any button, it will put a flag on it and will disable the left-click listener. If we right-click it one more time, the flag will get changed to a question mark. Right-clicking the same button one more time will clear the button and actives the left-click listener. This is *toggling* functionality. When a user left-click on a button that is hiding a mine and it was not flagged or question marked, all the mines are revealed and the game ends. If all tiles are revealed except the mines, then the game is won and all mines are revealed with flags. This is done by keeping an internal counter to mark how many tiles have been revealed. There is also extra logic to ensure that no more than 10 flags can be placed.

The ButtonController also provides *flood-fill*, which means that if you click on a tile with 0 mines around it, it would also reveal all the tiles around those tiles without any further interaction. This achieved using a depth first search algorithm in which a recursive call the the blank fill method is made to perform the same operation if the newly revealed tile also has 0 mines around it.

The MenuController contains the code that is necessary to access the lists and menus. When a user clicks on a list or menu, the menu controller will perform the back-end task of the program and provides the necessary output. For example, if a user clicks on the top ten scores, the menu controller will fetch the top ten scores of the game for the user. 
  
***
# 6 Human Interface Design #
### 6.1 Overview of User Interface ###
The user interface meets all the criteria listed by the user. The Minesweeper model creates a 10x10 grid and allows the user to play the game by clicking on the board. In case, if the user does not know how to play the game, he/she can select the help option from the drop down menu. If the user presses a mine, all the mines are revealed and the game ends. The Minesweeper board can be reset anytime by clicking on the Reset button. When the user wins the game, a dialog box pops up showing the time it took to win the game. If the user took less amount of time to win the game as compared to the top ten scores, he/she is prompt to enter a name and it will get saved in the top ten scores list. 
### 6.2 Screen Images ###
![game.jpg](https://bitbucket.org/repo/j7y7dn/images/2091604736-game.jpg)
![game1.jpg](https://bitbucket.org/repo/j7y7dn/images/650027841-game1.jpg)
![won.jpg](https://bitbucket.org/repo/j7y7dn/images/3235687283-won.jpg)
![topten.jpg](https://bitbucket.org/repo/j7y7dn/images/2799692455-topten.jpg)
### 6.3 Screen Objects and Actions ###
When a user Right-clicks a button once or twice, a flag and question mark gets placed on it. In this case, errors are handled when the user tries to Left-click on a button that has already been marked with a flag or question mark.

![flag.jpg](https://bitbucket.org/repo/j7y7dn/images/1182241875-flag.jpg) 
***
# 7 Requirements Matrix #
Requirement  | Where it is met
------------- | -------------
For the game, you will need to create a playing grid of 10x10 buttons. All 100 of the buttons will have no text displayed on the button. The game begins with the program randomly placing 10 mines "under" 10 of the 100 buttons. The game is over when either the player left clicks on a button containing a mine (the player loses) or when the 90 buttons that do not have mines have been cleared (the player wins). The description of the program below assumes the program is going to be done using JButtons with text. Using images instead of text earns extra credit (see below) and looks much better. | BoardView.java, ButtonController.java
If the user clicks on a button with the right mouse button, the program is to mark that button as containing a mine. The program is to now change the text of the button from having no text to the character of M (in upper case). This indicates the user believes a mine is hidden under the button. If the user clicks again on this button, the character is changed to a question mark, "?". This indicates the user thinks there may be a mine hidden under the button. If the user clicks on this button a third time, the no text is displayed. Repeated right clicks is to repeat the sequence of characters displayed from no text to "M" to "?" to no text. While a button is marked with an "M" or a "?", the button cannot be left clicked (see next paragraph). | ButtonController.java
If the user clicks on the button with the left mouse button, the program must first check what the text on the button is. If the text on the button is "M" or "?", nothing occurs. If the button has no text, the program must check if that button "hides" a mine. If it does, the mine explodes. Then all mines are shown and the game is over. If it does not hide a mine, the button is disabled and the text of the button is to be changed to give the number of mines that are hidden under buttons adjacent to the current button. We will call this "clearing a button". A button can have a maximum of 8 adjacent buttons (those on the edge of the grid will have less). If no adjacent button hides a mine, display the value 0 (See additional comments #1 below for reason on striking out this instruction.) and your program is to automatically clear all adjacent buttons. If one of the buttons that is automatically cleared also has no adjacent hidden mines, this button should also be automatically cleared. | ButtonController.java, MinesweeperModel.java
Apart from the 10x10 grid, your program must have another area of the program to display some game information. This information will contain three GUI elements: an output text field that shows the number of mines left to be found, a reset button and an output text field that shows the amount of elapsed time for the game. These three elements should be separate from the 10x10 grid of buttons (i.e. contained in their own JPanel).  | BoardView.java
The output field that shows the number of mines left should be 10 at the start of the game and decrement by 1 every time a button in the grid is marked with an "M". If the button's text is changed from an "M" to something else, the number of mines left should be incremented. Note simply marking 10 places with "M" does not win the game. The game is won when 90 buttons have been successfully cleared. As soon as the game is won, any buttons that contain mines but are not marked should be automatically marked with an "M" and the number of mines left should be zero.  | BoardView.java, ButtonController.java, MinesweeperModel.java
The reset button should allow the user to start a new game at anytime.  | MenuController.java
The output field that shows the amount of elapsed time for the game is to display the number of seconds since the user first left clicks on a button in the 10x10 grid. The must be updated as the game is played until either the user wins or loses the game.  | BoardView.java, ButtonController.java
Your program must have two drop down menus called Game and Help. In the Game drop down menu, you are to have two menu items: Reset, Top ten and eXit. Under the Help drop down menu, you are to have two menu items: Help and About. All menu items are to have the mnemonic of the capital letter in the word. The Reset menu items is to perform the same function as the reset button. The eXit menu item should end the program. The Help menu items should display some information about how to play the game (don't go overboard on this, keep it simple!). The About menu item should give some information about the programmer (name, user- id and class).  | BoardView.java, MenuController.java
The top ten scorers is a list of the top ten fastest users to solve a game. There must also be a way to reset/clear the top ten scorers (perhaps another button on the Game drop down menu). When a user makes the top ten scorers list, you must prompt for the user's name and record both the user's name and elapsed time for the game. This information must be kept from each time your program is run; therefore, you will need to keep this information in a file (you may assume it is in the java runtime default directory). This file will be need to be read in when the program begins and saved out this information as the program ends. If the file doesn’t exist at start time, assume the program is running for the first time and the file does not yet exist. | ScoreModel.java, BoardView.java, MenuController.java, ButtonController.java
For 10 points extra credit instead of using text to indicate a mine, use images. You may need to create your own images. However, I am sure you can fine such images on the internet. | ButtonController.java