// -----------------------------------------------------
// Assignment 3
// Question: Driver class
// Written by: Vincent de Serres-40272920 and Nguyen Le-40272922
// For COMP 249 Section S (Vincent) Section U (Nguyen) Winter 2024
// -----------------------------------------------------

/* General Explanation:

The DLinkedList class manages a doubly linked list of topics and their associated words. It provides methods to add, remove, display, and manipulate topics and words within the list.
Each node in the doubly linked list represents a topic, containing a title and a reference to a WordLinkedList containing associated words.
Key functionalities include adding topics at the head, end, or specified index, removing topics by title, displaying topic titles, displaying words for a specific topic,
and searching for topics based on words.Additionally, the class facilitates saving topics and words to a file and provides methods to retrieve the size of the doubly linked list.
Nested within DLinkedList is the Vocab class, representing a node in the doubly linked list. It contains references to the next and previous nodes,
along with the topic title and associated words. 
Overall, the DLinkedList class serves as the backbone for organizing and managing topics and words in the Vocabulary Control Center program. */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

// Define a class named DLinkedList
class DLinkedList {
	private Vocab head; // Reference to the first node in the doubly linked list
	private int size = 0; // Number of nodes in the doubly linked list
	static boolean wordFoundInAnyTopic  = true; // Flag to indicate if a word is found in any topic
	static int countWrite = 0; // Counter for writing to file

	// Constructor for DLinkedList
	public DLinkedList() {
		this.head = null; // Initialize the head of the doubly linked list to null
	}

	/**
	 * Adds a new topic at the head of the doubly linked list.
	 *
	 * @param title The title of the new topic.
	 * @param words The WordLinkedList containing the words associated with the
	 *              topic.
	 */
	public void addAtHead(String title, WordLinkedList words) {
		head = new Vocab(title, null, head, words); // Create a new node with the given title and words, and add it at the head
		size++; // Increment the size of the doubly linked list
	}

	/**
	 * Adds a new topic at the end of the doubly linked list.
	 *
	 * @param title The title of the new topic.
	 * @param words The WordLinkedList containing the words associated with the
	 *              topic.
	 */
	public void addAtEnd(String title, WordLinkedList words) {
		if (head == null) {
			addAtHead(title, words); // If the list is empty, add the topic at the head
		} else {
			Vocab current = head;
			while (current.next != null) {
				current = current.next; // Traverse to the end of the doubly linked list
			}
			current.next = new Vocab(title, null, current, words); // Add the topic at the end of the doubly linked list
			size++; // Increment the size of the doubly linked list
		}
	}

	/**
	 * Displays only the titles of all topics in the doubly linked list.
	 */
	public void displayTopicOnly() {
		Vocab current = head;
		int currentNumber = 1;
		while (current != null) {
			System.out.println(currentNumber + " " + current.getTitle()); // Print the title of each topic
			current = current.next; // Move to the next node
			currentNumber++;
		}
	}

	/**
	 * Displays the words of a specific topic based on the user's selection.
	 *
	 * @param select The index of the topic to display.
	 */
	public void display(int select) {
		Vocab current = head;
		int index = 1;
		if (select == -1)
			select = index;
		while (current != null) {
			if (index == select) {
				System.out.println("\n\nTopic: " + current.getTitle()); // Print the title of the selected topic
				if (current.getWords() == null)
					System.out.println("This vocab category is empty");
				else
					current.getWords().display(); // Display the words associated with the selected topic
				break;
			}
			current = current.next; // Move to the next node
			index++;
		}
		if (index >= size + 1)
			System.out.println("You did not choose one of the Vocabs, try again"); // Inform the user if the selection
																					// is invalid
	}

	/**
	 * Displays words starting with a given letter from all topics.
	 *
	 * @param letter The letter to filter words by.
	 */
	public void displayWordsStartingWithLetter(String letter) {
		Vocab current = head;
		if (letter.length() > 1) {
			System.out.println("Please enter only one letter");
			Driver.choose(8); // Prompt the user to enter only one letter
		}
		while (current != null) {
			current.getWords().displayWordsStartingWithLetter(letter); // Display words starting with the given letter for each topic
			current = current.next; // Move to the next node
		}
		if (WordLinkedList.letterPrintedFlag)
			System.out.println("There are no words starting with the letter '" + letter + "'"); // Inform the user if
																									// no words are
																									// found
		System.out.println(); // Print an additional space
	}

	/**
	 * Adds a new topic at a specific index in the doubly linked list.
	 *
	 * @param addIndex The index at which to add the new topic.
	 * @param newTitle The title of the new topic.
	 * @param words    The WordLinkedList containing the words associated with the
	 *                 topic.
	 * @param after    A boolean value indicating whether to add the topic after the
	 *                 specified index (true) or before (false).
	 */
	public void addVocab(int addIndex, String newTitle, WordLinkedList words, boolean after) {
		if (addIndex != 0) {
			Vocab current = head;
			if (current == null) {
				head = new Vocab(newTitle, null, null, words); // Add the topic at the head if the list is empty
			} else {
				int index = 1;
				while (current != null && addIndex != index) {
					current = current.next; // Traverse the list until the specified index is reached
					index++;
				}

				if (after) {
					if (index == size) {
						addAtEnd(newTitle, words); // Add the topic at the end if specified to add after the last index
						size--;
					} else {
						current.next = new Vocab(newTitle, current.next, current, words); // Add the topic after the
																								// specified index
					}

				} else {
					if (index != 1)
						current.previous.next = new Vocab(newTitle, current, current.previous, words); // Add the
																											// topic
																											// before
																											// the
																											// specified
																											// index
					else {
						head = new Vocab(newTitle, current, head, words); // Add the topic at the head if specified to
																			// add before the first index
					}

				}

			}
			size++;
		} else
			System.out.println("No Vocab was added"); // Inform the user if no topic is added
	}

	/**
	 * Removes a topic from the doubly linked list based on its title.
	 *
	 * @param title The title of the topic to be removed.
	 * @return The title of the removed topic, or null if the specified topic is not
	 *         found.
	 */
	public String removeVocab(String title) {
		if (title == null) {
			return "No vocab"; // Return a message if no title is provided
		}
		if (head == null) {
			return null; // Return null if the list is empty
		} else if (head.title.equals(title)) {
			Vocab temp = head;
			head = head.next;
			size--; // Remove the topic from the head of the list
			return temp.title; // Return the title of the removed topic
		} else {
			Vocab current = head;
			while (current.next != null && !current.next.title.equals(title)) {
				current = current.next; // Traverse the list until the specified topic is found
			}

			if (current.next != null) {
				Vocab temp = current.next;
				current.next = current.next.next;
				size--; // Remove the specified topic
				return temp.title; // Return the title of the removed topic
			} else
				return null; // Return null if the specified topic is not found
		}
	}

	/**
	 * Converts a topic index to its corresponding title.
	 *
	 * @param indexUser The index of the topic.
	 * @return The title of the topic corresponding to the specified index, or null
	 *         if the index is invalid.
	 */
	public String convertNumberToTitle(int indexUser) {
		Vocab current = head;
		int index = 1;
		while (current != null) {
			if (index == indexUser)
				return current.title; // Return the title of the topic corresponding to the specified index
			current = current.next;
			index++;
		}
		return null; // Return null if the specified index is invalid
	}

	/**
	 * Converts a topic index to its associated words.
	 *
	 * @param indexUser The index of the topic.
	 * @return The WordLinkedList containing the words associated with the topic
	 *         corresponding to the specified index, or null if the index is
	 *         invalid.
	 */
	public WordLinkedList convertNumberToWords(int indexUser) {
		Vocab current = head;
		int index = 1;
		while (current != null) {
			if (index == indexUser)
				return current.getWords(); // Return the words associated with the topic corresponding to the specified
											// index
			current = current.next;
			index++;
		}
		return null; // Return null if the specified index is invalid
	}

	/**
	 * Searches for a word in all topics and displays the topics where the word is
	 * found.
	 *
	 * @param word The word to search for.
	 */
	public void searchWord(String word) {
		Vocab current = head;
		while (current.next != null) {
			if (current.getWords().searchWordinWords(word))
				System.out.print(current.title + "  "); // Print the title of topics where the word is found
			current = current.next;
		}
		if (wordFoundInAnyTopic )
			System.out.println("There is no topic associated with this word"); // Inform the user if the word is not
																				// found in any topic
		else {
			System.out.println("is/are the topic/s where this word is found");
			System.out.println();
		}
	}

	/**
	 * Saves all topics and associated words to a file.
	 */
	public void saveToFile() {
		Vocab current;
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new FileOutputStream("A3_input_file_modified.txt"));
			current = head;
			while (current != null) {
				printWriter.println("\n#" + current.title); // Write the title of each topic to the file
				printWriter.close();
				printWriter = new PrintWriter(new FileOutputStream("A3_input_file_modified.txt", true));
				current.getWords().saveWordToFile(); // Write the associated words of each topic to the file
				current = current.next;
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found"); // Handle file not found exception
		}
	}

	/**
	 * Retrieves the size of the doubly linked list.
	 *
	 * @return The number of nodes in the doubly linked list.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Sets the size of the doubly linked list.
	 *
	 * @param size The size to set for the doubly linked list.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Represents a node in the doubly linked list.
	 */
	private class Vocab {
		private Vocab next; // Reference to the next node
		private Vocab previous; // Reference to the previous node
		private String title; // Title of the topic
		private WordLinkedList words; // Reference to the words associated with the topic

		/**
		 * Initializes a Vocab node.
		 *
		 * @param title        The title of the topic.
		 * @param nextLink     The reference to the next node.
		 * @param previousLink The reference to the previous node.
		 * @param words        The words associated with the topic.
		 */
		public Vocab(String title, Vocab nextLink, Vocab previousLink, WordLinkedList words) {
			this.title = title;
			this.next = nextLink;
			this.previous = previousLink;
			this.words = words;
		}

		/**
		 * Retrieves the words associated with the topic.
		 *
		 * @return The WordLinkedList containing the words associated with the topic.
		 */
		private WordLinkedList getWords() {
			return words;
		}

		/**
		 * Retrieves the title of the topic.
		 *
		 * @return The title of the topic.
		 */
		public String getTitle() {
			return title;
		}

	}
}
