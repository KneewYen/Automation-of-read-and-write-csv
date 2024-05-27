// -----------------------------------------------------
// Assignment 3
// Question: Driver class
// Written by: Vincent de Serres-40272920 and Nguyen Le-40272922
// For COMP 249 Section S (Vincent) Section U (Nguyen) Winter 2024
// -----------------------------------------------------
/* General Explanation:
The WordLinkedList class manages a singly linked list of words. It provides methods to add, remove, display, and manipulate words within the list.
Each node in the linked list represents a word, containing the word itself and a reference to the next node.
Key functionalities include adding words at the head or end of the list, displaying all words in sorted order, displaying words starting with a given letter,
removing words, modifying words, and searching for words within the list.
Additionally, the class facilitates saving words to a file and provides methods to retrieve the size of the linked list and access the head node.
Overall, the WordLinkedList class serves as a fundamental data structure for managing words in the Vocabulary Control Center program. */



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

// Define a class named WordLinkedList
class WordLinkedList {

	// Private member variables
	private Word head; // Reference to the first node in the linked list
	private int size = 0; // Number of nodes in the linked list
	private static int countDisplayWordStartingWithLetter = 1; // Counter for displaying words starting with a given letter
	static boolean letterPrintedFlag = true; // Flag for printing

	/**
	 * Constructs an empty WordLinkedList with the head initialized to null.
	 */
	public WordLinkedList() {
		this.head = null; // Initialize the head of the linked list to null
	}

	/**
	 * Adds a word at the head of the linked list.
	 *
	 * @param word The word to be added at the head of the linked list.
	 */
	public void addAtHead(String word) {
		head = new Word(word, head); // Create a new node with the given word and add it at the head
		size++; // Increment the size of the linked list
	}

	/**
	 * Adds a word at the end of the linked list.
	 *
	 * @param word The word to be added at the end of the linked list.
	 */
	public void addAtEnd(String word) {
		if (head == null) {
			addAtHead(word); // If the list is empty, add the word at the head
		} else {
			Word current = head;
			while (current.next != null) {
				current = current.next; // Traverse to the end of the linked list
			}
			current.next = new Word(word, null); // Add the word at the end of the linked list
			size++; // Increment the size of the linked list
		}
	}

	/**
	 * Displays all words in the linked list in sorted order. Words are displayed
	 * with a numbering scheme and are sorted alphabetically. A new line is printed
	 * after every 4 words.
	 */
	public void display() {
		int countDisplayWord = 1; // Counter for displaying words
		Word current = head;
		ArrayList<String> wordArray = new ArrayList<>(); // ArrayList to store words for sorting
		while (current != null) {
			wordArray.add(current.getWord()); // Add words to the ArrayList
			current = current.next; // Move to the next node
		}
		// Sorting the ArrayList of words
		for (int i = 0; i < wordArray.size(); i++) {
			for (int j = i + 1; j < wordArray.size(); j++) {
				if (wordArray.get(i).compareTo(wordArray.get(j)) > 0) {
					String temp = wordArray.get(j);
					wordArray.set(j, wordArray.get(i));
					wordArray.set(i, temp);
				}
			}
		}

		// Displaying the sorted words
		current = head;
		for (int i = 0; i < wordArray.toArray().length; i++) {
			System.out.printf("%-30s", countDisplayWord + ": " + wordArray.get(i));
			if (countDisplayWord % 4 == 0)
				System.out.println(); // Print a new line after every 4 words
			current = current.next; // Move to the next node
			countDisplayWord++; // Increment the word counter
		}
		System.out.println(); // Print a new line after displaying all words
	}

	/**
	 * Displays words starting with a given letter. Words are displayed with a
	 * numbering scheme and are sorted alphabetically. A new line is printed after
	 * every 4 words.
	 *
	 * @param letter The letter to filter words by.
	 */
	public void displayWordsStartingWithLetter(String letter) {
		Word current = head;
		ArrayList<String> wordArrayStartingWithGivenLetter  = new ArrayList<>(); // ArrayList to store words starting with the given
																// letter
		while (current != null) {
			if (letter.equals(current.getWord().substring(0, 1)))
				wordArrayStartingWithGivenLetter .add(current.getWord()); // Add words starting with the given letter to the ArrayList
			current = current.next; // Move to the next node
		}
		current = head;
		for (int i = 0; i < wordArrayStartingWithGivenLetter .toArray().length; i++) {
			System.out.printf("%-30s", countDisplayWordStartingWithLetter + ": " + wordArrayStartingWithGivenLetter .get(i));
			if (countDisplayWordStartingWithLetter % 4 == 0)
				System.out.println(); // Print a new line after every 4 words
			current = current.next; // Move to the next node
			countDisplayWordStartingWithLetter++; // Increment the word counter for words starting with the given letter
		}
		if (wordArrayStartingWithGivenLetter .size() > 0)
			letterPrintedFlag = false; // Set a flag if words starting with the given letter are found
	}

	/**
	 * Removes a word from the linked list.
	 *
	 * @param word The word to be removed from the linked list.
	 * @return A message indicating whether the word was successfully removed or
	 *         not.
	 */
	public String removeWord(String word) {
		if (head == null) {
			return null; // Return null if the list is empty
		} else if (head.word.equals(word)) {
			Word temp = head;
			head = head.next;
			size--; // Remove the word from the head of the linked list
			return temp.word + " was removed";
		} else {
			Word current = head;
			while (current.next != null && !current.next.word.equals(word)) {
				current = current.next; // Traverse the linked list until the word to be removed is found
			}

			Word temp = current.next;
			if (current.next != null) {
				current.next = current.next.next;
				size--; // Remove the word from the middle of the linked list
				return temp.word + " was removed";
			} else
				return "there is no word: " + word; // Return a message if the word to be removed is not found
		}
	}

	/**
	 * Adds a word to the linked list.
	 *
	 * @param word The word to be added to the linked list.
	 * @return A message indicating whether the word was successfully added or not.
	 */
	public String addWord(String word) {
		Word current = head;
		while (current.next != null && !current.next.word.equals(word)) {
			current = current.next; // Traverse the linked list until the end or the word is found
		}
		if (current.next != null) {
			return "the word: '" + word + "' is already listed"; // Return a message if the word is already
																		// listed
		} else {
			addAtHead(word); // Add the word at the head if it's not already listed
			return word + " has been added";
		}
	}

	/**
	 * Modifies a word in the linked list.
	 *
	 * @param word         The word to be modified in the linked list.
	 * @param modifiedWord The new value of the word to be modified.
	 * @return A message indicating whether the word was successfully modified or
	 *         not.
	 */
	public String modifyWord(String word, String modifiedWord) {
		Word current = head;
		while (current.next != null && !current.next.word.equals(word)) {
			current = current.next; // Traverse the linked list until the word to be modified is found
		}
		if (current.next != null) {
			current.next.setWord(modifiedWord); // Modify the word if found
			return word + "' has been modified to '" + modifiedWord ;
		} else {
			return "The word: '" + word + "' is not in the file"; // Return a message if the word is not found
		}
	}

	/**
	 * Searches for a word in the linked list.
	 *
	 * @param word The word to be searched in the linked list.
	 * @return True if the word is found in the linked list, false otherwise.
	 */
	public boolean searchWordinWords(String word) {
		Word current = head;
		while (current.next != null && !current.next.word.equals(word)) {
			current = current.next; // Traverse the linked list until the word to be searched is found
		}
		if (current.next != null) { // If found, set a flag and return true
			DLinkedList.wordFoundInAnyTopic  = false;
			return true;
		}
		return false; // If not found, return false
	}

	/**
	 * Saves words from the linked list to a file. Words are saved to a file named
	 * "A3_input_file_modified.txt".
	 */
	public void saveWordToFile() {
		Word current;
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new FileOutputStream("A3_input_file_modified.txt", true));
			current = head;
			while (current.next != null) {
				printWriter.println(current.word); // Write words to a file
				current = current.next; // Traverse the linked list
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found"); // Handle file not found exception
		}
	}

	/**
	 * Retrieves the size of the linked list.
	 *
	 * @return The number of nodes in the linked list.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size of the linked list.
	 *
	 * @param size The new size of the linked list.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Retrieves the head of the linked list.
	 *
	 * @return The reference to the first node in the linked list.
	 */
	public Word getHead() {
		return head;
	}

	/**
	 * Sets the head of the linked list.
	 *
	 * @param head The new head of the linked list.
	 */
	public void setHead(Word head) {
		this.head = head;
	}

	/**
	 * Represents a node in the linked list.
	 */
	private class Word {
		/**
		 * Word stored in the node.
		 */
		private String word;

		/**
		 * Reference to the next node.
		 */
		private Word next;

		/**
		 * Constructs a Word node with the given word and next node reference.
		 *
		 * @param word The word to be stored in the node.
		 * @param next The reference to the next node.
		 */
		public Word(String word, Word next) {
			this.word = word;
			this.next = next;
		}

		/**
		 * Retrieves the word stored in the node.
		 *
		 * @return The word stored in the node.
		 */
		public String getWord() {
			return word;
		}

		/**
		 * Sets the word stored in the node.
		 *
		 * @param word The new value of the word to be stored in the node.
		 */
		public void setWord(String word) {
			this.word = word;
		}
	}

}