// -----------------------------------------------------
// Assignment 3
// Question: Driver class
// Written by: Vincent de Serres-40272920 and Nguyen Le-40272922
// For COMP 249 Section S (Vincent) Section U (Nguyen) Winter 2024
// -----------------------------------------------------

/* General Explanation:
The Driver class serves as the central component of the Vocabulary Control Center program.
It manages user interaction through a menu-based interface, handling functions such as browsing, adding, removing, and modifying topics and words.
Interacting with other classes like DLinkedList and WordLinkedList, it orchestrates the program's functionality,
enabling users to perform various actions like searching for topics based on words, loading/saving data from/to files, and displaying words by letter.
With methods for menu display, user choice handling, file loading, data transfer, topic management, and word display,
the Driver class acts as the control hub of the program, ensuring smooth operation and efficient management of vocabulary data. */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

// Class definition for the Driver class
public class Driver {
	// Static variables for managing topic numbers, user input, and linked lists
	private static int titleNumber = 1;
	private static String titleUser;
	private static DLinkedList doubleLL;
	private static WordLinkedList worldList;

	/**
	 * Main method to start the Vocabulary Control Center program. Displays the menu
	 * and handles user's choices until the user chooses to exit.
	 *
	 * @param args The command-line arguments.
	 */
	public static void main(String[] args) {
		int choice; // Variable for storing user's choice
		// Loop to display menu and handle user's choice
		do {
			display(); // Display menu
			choice = key.nextInt(); // Get user's choice
			choose(choice); // Handle user's choice
		} while (choice != 0); // Continue until user chooses to exit
	}

	// Scanner object for user input
	static Scanner key = new Scanner(System.in);

	/**
	 * Displays the menu for the Vocabulary Control Center program.
	 */
	public static void display() {
		System.out.print("""
				===========================
				Vocabulary Control Center
				===========================
				1 browse a topic
				2 insert a new topic before another one
				3 insert a new topic after another one
				4 remove a topic
				5 modify a topic
				6 search topics for a word
				7 load from a file
				8 show all words starting with a given letter
				9 save to file
				0 exit
				===========================
				Enter Your Choice:\s""");
	}

	/**
	 * Handles the user's choice based on the menu options.
	 *
	 * @param choice The user's choice.
	 */
	public static void choose(int choice) {
		// Check if the doubly linked list is empty and the choice is not related to creating a new topic
		if (doubleLL == null && (choice != 7 && choice != 2 && choice != 3)) {
			System.out.println("Linked list is empty"); // Inform the user if the list is empty
		} else {
			// Switch case to handle different choices
			switch (choice) {
			case 1:
				int choiceDisplay = -1;
				while (choiceDisplay != 0) {
					insideDisplay(); // Display topics
					choiceDisplay = key.nextInt(); // Get user's choice of topic
					doubleLL.display(choiceDisplay); // Display words of the selected topic
				}
				break;
			case 2:
				addTopic(); // Add a new topic before another one
				if (doubleLL != null)
					doubleLL.addVocab(titleNumber, titleUser, worldList, false);
				else {
					doubleLL = new DLinkedList();
					doubleLL.addVocab(titleNumber, titleUser, worldList, true);
				}
				break;
			case 3:
				addTopic(); // Add a new topic after another one
				if (doubleLL != null)
					doubleLL.addVocab(titleNumber, titleUser, worldList, true);
				else {
					doubleLL = new DLinkedList();
					doubleLL.addVocab(titleNumber, titleUser, worldList, true);
				}
				break;
			case 4:
				removeTopic(); // Remove a topic
				System.out.println(doubleLL.removeVocab(doubleLL.convertNumberToTitle(titleNumber))
						+ " was removed"); // Inform the user about the removed topic
				break;
			case 5:
				try {
					modify(); // Modify a topic
				} catch (InputMismatchException e) {
					System.out.println("You should enter an integer"); // Handle input mismatch exception
				}
				break;
			case 6:
				search(); // Search for topics based on a word
				break;
			case 7:
				loadFile(); // Load topics and words from a file
				break;
			case 8:
				wordLetter(); // Display words starting with a given letter
				break;
			case 9:
				doubleLL.saveToFile(); // Save topics and words to a file
				System.out.println("Done loading");
				break;
			}
		}
	}

	/**
	 * Loads topics and words from a file specified by the user. Prompts the user to
	 * enter the name of the input file. Reads each line from the file, processes
	 * it, and adds topics and words to the doubly linked list. Displays a message
	 * indicating successful loading.
	 */
	public static void loadFile() {
		System.out.println("Enter the name of the input file you want to read");
		String fileName = key.next();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			doubleLL = new DLinkedList(); // Initialize the doubly linked list
			String line;
			while ((line = reader.readLine()) != null) {
				transfer(line); // Process each line from the file
			}
			System.out.println("Done loading");

		} catch (FileNotFoundException e) {
			System.out.println("File not found"); // Handle file not found exception
		} catch (IOException e) {
			System.out.println("Cannot read the file"); // Handle IO exception
		}

	}

	/**
	 * Processes each line from the input file. If the line starts with '#', it
	 * indicates a new topic, so a new WordLinkedList is created and added to the
	 * doubly linked list. Otherwise, it adds the word to the current topic.
	 *
	 * @param line The line read from the input file.
	 */
	public static void transfer(String line) {
		if (line.indexOf("#") == 0) {
			worldList = new WordLinkedList(); // Create a new WordLinkedList for a new topic
			doubleLL.addAtEnd(line.substring(1), worldList); // Add a new topic to the doubly linked list
		} else {
			if (!line.isEmpty())
				worldList.addAtEnd(line); // Add a word to the current topic
		}
	}

	/**
	 * Prompts the user to add a new topic. Displays the existing topics and prompts
	 * the user to choose the index for the new topic. Asks the user for the topic
	 * name and words to add to the new topic. Adds the new topic to the doubly
	 * linked list.
	 */
	public static void addTopic() {
		do {
			insideDisplay(); // Display topics
			if (doubleLL == null)
				break;

			titleNumber = key.nextInt(); // Get user's choice of topic
			if ((titleNumber < 0 || titleNumber > doubleLL.getSize())) // Check if the choice is valid
				System.out.println("Wrong choice, please try again");
		} while ((titleNumber < 0 || titleNumber > doubleLL.getSize()));

		if (doubleLL == null) {
			System.out.println("Empty list");
			titleNumber = -1;
		}

		if (titleNumber != 0) {
			System.out.print("Enter a topic name: ");
			titleUser = key.next(); // Get user's input for topic name
			System.out.println("Enter a word (to quit, write the word Enter): ");
			worldList = new WordLinkedList(); // Create a new WordLinkedList object

			String line;
			while (key.hasNext()) {
				line = key.next();
				if (line.equalsIgnoreCase("enter"))
					break;
				worldList.addAtEnd(line); // Add words to the current topic
			}
		}
	}

	/**
	 * Removes a topic based on the user's selection. Displays topics for the user
	 * to choose from. Retrieves the user's choice of topic and assigns it to
	 * {@code titleNumber}.
	 */
	public static void removeTopic() {
		insideDisplay(); // Display topics
		titleNumber = key.nextInt(); // Get user's choice of topic
	}

	/**
	 * Displays topics for modification. If the doubly linked list is not empty, it
	 * displays the titles of all topics. Provides an option to exit.
	 */
	public static void insideDisplay() {
		System.out.print("""
				------------------------------
				Pick a topic
				------------------------------
				""");
		if (doubleLL != null) {
			doubleLL.displayTopicOnly(); // Display topics
		}
		System.out.print("""
				0 Exit
				------------------------------
				Enter Your Choice:\s""");
	}

	/**
	 * Displays modification options for topics. Provides options to add a word,
	 * remove a word, change a word, or exit.
	 */
	public static void modifyDisplay() {
		System.out.print("""
				-----------------------------
				Modify Topics Menu
				-----------------------------
				a add a word
				r remove a word
				c change a word
				0 Exit
				-----------------------------
				Enter Your Choice:\s""");
	}

	/**
	 * Modifies a topic based on the user's selection. Allows the user to add,
	 * remove, or change words in the selected topic. Displays modification options
	 * and prompts the user for input accordingly.
	 *
	 * @throws InputMismatchException If the input is not an integer when expected.
	 */
	public static void modify() throws InputMismatchException {
		String modifyChoice;
		do {
			insideDisplay(); // Display topics
			titleNumber = key.nextInt(); // Get user's choice of topic
			if ((titleNumber < 0 || titleNumber > doubleLL.getSize())) {
				System.out.println("This is not one of the choices, try again");
				break;
			}

			modifyDisplay(); // Display modification options
			modifyChoice = key.next(); // Get user's choice
			WordLinkedList wordsOut = doubleLL.convertNumberToWords(titleNumber); // Get words for the selected
																							// topic

			switch (modifyChoice) {
			case "a":
				System.out.println("Type a word and press Enter, or press Enter to end input");
				String addedWord = key.next();
				System.out.println(wordsOut.addWord(addedWord)); // Add a word to the topic
				break;
			case "r":
				System.out.println("Enter a word:");
				String removedWord = key.next();
				System.out.println(wordsOut.removeWord(removedWord)); // Remove a word from the topic
				break;
			case "c":
				System.out.print("Enter the word you want to modify: ");
				String originalWord = key.next();
				System.out.print("Enter the new word: ");
				String modifiedWord = key.next();
				System.out.println(wordsOut.modifyWord(originalWord, modifiedWord)); // Modify a word in the topic
				break;
			case "0":
				break;
			default:
				System.out.println("This is not one of the choices");
			}
		} while (!modifyChoice.equals("a") && !modifyChoice.equals("r") && !modifyChoice.equals("c")
				&& !modifyChoice.equals("0"));
	}

	/**
	 * Searches for topics based on a word entered by the user. Prompts the user to
	 * enter a word to search for. Displays topics where the word is found.
	 */
	public static void search() {
		System.out.print("Enter the word you want to get the topics from: ");
		String word = key.next();
		doubleLL.searchWord(word); // Search for topics based on a word
	}

	/**
	 * Displays words starting with a given letter entered by the user. Prompts the
	 * user to enter a letter. Displays words starting with the given letter from
	 * all topics.
	 */
	public static void wordLetter() {
		System.out.print("Enter the letter: ");
		String letter = key.next();
		doubleLL.displayWordsStartingWithLetter(letter); // Display words starting with the given letter
	}

}