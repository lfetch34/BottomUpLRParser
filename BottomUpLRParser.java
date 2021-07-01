
// Bottom Up LR Parser by Luke Fetchko
// Dr. Ai -- CSCI U530
// A simple bottom up parser to determine if a sentential form follows the grammar rules with the given parsing table.
// Valid lexemes are: id, +, *, (, ), and $ to end sentence

/* Six grammar rules are:
1. E -> E + T
2. E -> T
3. E -> T * F
4. T -> F
5. F -> (E)
6. F -> id
*/
import java.util.*;
public class BottomUpLRParser {
	// Queue data structure to store and handle input string
	static Queue<String> inputQ;
	// Stack data structure to store and handle parsing stack
	static Stack<String> stack;


	public static void main(String[] args) {
		// Create new Queue for input
		inputQ = new LinkedList<String>();
		// Create new Stack for parsing stack
		stack = new Stack<String>();
	// Manually entered parsing action table	
	String action[][] = {   {"s5",null,null,"s4",null,null},
							{null,"s6",null,null,null,"accept"},
							{null,"r2","s7",null,"r2","r2"},
							{null,"r4","r4",null,"r4","r4"},
							{"s5",null,null,"s4",null,null},
							{null,"r6","r6",null,"r6","r6"},
							{"s5",null,null,"s4",null,null},
							{"s5",null,null,"s4",null,null},
							{null,"s6",null,null,"s11",null},
							{null,"r1","s7",null,"r1","r1"},
							{null,"r3","r3",null,"r3","r3"},
							{null,"r5","r5",null,"r5","r5"}
						};
	// Manually entered parsing goto table
	int goTo[][] = {    {1,2,3},
						{0,0,0},
						{0,0,0},
						{0,0,0},
						{8,2,3},
						{0,0,0},
						{0,9,3},
						{0,0,10},
						{0,0,0},
						{0,0,0},
						{0,0,0},
						{0,0,0}
				   };
		// Get keyboard input of sentence to be parsed
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the sentence to be parsed wihtout any spaces (valid lexemes are: id, +, *, (, ), and $ to end sentence): ");
		String sentence = scan.nextLine();
		scan.close();
		// Convert keyboard input String to char array
		char inQArr[] = sentence.toCharArray();
		// Call addInput method to add correct chars to String Queue
		addInput(inQArr, inputQ);
		// Stack always starts with 0 in it
		stack.add("0");
		// Print statements for output table formatting
		System.out.println();
		System.out.print("Stack");
		System.out.print(String.format("%25s %20s", "Input", "Action\n"));
		System.out.println("--------------" + "      " + "---------------"+ "      " + "---------------");
		// Enter infinite loop
		while (true) {
			// Retrieve state from top of Stack
			int state = Integer.parseInt(stack.peek());
			// Retrieve lexeme from head of Queue
			String lexeme = inputQ.peek();
			// Call determineAction method and store String result in act variable
			String act = determineAction(state, lexeme, action);
			// Test for null action returned, if so it means error in sentential form entered, and breaks out of loop and causes program termination
			if (act == null) {
				System.out.print("Error, null action reached in parse table");
				break;
			}
			// Convert act String to char array
			char[] actArr = act.toCharArray();
			// Test for accept state in char array, if so, print out last Stack, Input, and Action results and break out of loop, causes program termination
			if (actArr[0] == 'a') {
				Object[] currStack = stack.toArray();
				
				
				for (int i = 0; i <currStack.length;i++) {
					System.out.print(currStack[i]);
				}
				
				System.out.print(String.format("%20s",""));
				Object[] currInput = inputQ.toArray();
				for (int i = 0; i <currInput.length;i++) {
					System.out.print(currInput[i]);
				}
				System.out.print(String.format("%20s",""));
				for (int i = 0; i <actArr.length;i++) {
					System.out.print(actArr[i]);
				}
				break;
			}
			
			// Convert current Stack to Object array
			Object[] currStack = stack.toArray();
			
			// Print each element in Stack Object array
			for (int i = 0; i <currStack.length;i++) {
				System.out.print(currStack[i]);
			}
			// Formatting for spacing
			System.out.print(String.format("%20s",""));
			// Convert input Queue to Object array
			Object[] currInput = inputQ.toArray();
			// Print each element in Queue Object array
			for (int i = 0; i <currInput.length;i++) {
				System.out.print(currInput[i]);
			}
			// String formatting
			System.out.print(String.format("%20s",""));
			// Print each element in current action char array
			for (int i = 0; i <actArr.length;i++) {
				System.out.print(actArr[i]);
			}
			
			System.out.println();
			System.out.println();
			// Test for Shift 11 and handle it
			if (actArr[0] == 's' && actArr[1] == '1') {
				stack.add(inputQ.remove());
				String toAdd = Character.toString(actArr[1]) + Character.toString(actArr[2]);
				stack.add(toAdd);
			// Test for all other Shifts and handle them	
			} else if (actArr[0] == 's') {
				stack.add(inputQ.remove());
				stack.add(Character.toString(actArr[1]));
			// Test for Reduce 2 and handle	
			} else if (actArr[0] == 'r' && actArr[1]== '2') {
				//DO REDUCE
				stack.pop();
				stack.pop();
				String intUsed = stack.peek();
				stack.add("E");
				stack.add(Integer.toString(goTo[Integer.parseInt(intUsed)][0]));
			// Test for Reduce 4 and handle	
			} else if (actArr[0] == 'r' && actArr[1]== '4') {
				stack.pop();
				stack.pop();
				String intUsed = stack.peek();
				stack.add("T");
				stack.add(Integer.toString(goTo[Integer.parseInt(intUsed)][1]));
			// Test for Reduce 6 and handle	
			} else if (actArr[0] == 'r' && actArr[1]== '6') {
				stack.pop();
				stack.pop();
				String intUsed = stack.peek();
				stack.add("F");
				stack.add(Integer.toString(goTo[Integer.parseInt(intUsed)][2]));
			// Test for Reduce 1 and handle
			} else if (actArr[0] == 'r' && actArr[1]== '1') {
				for (int i = 0; i < 6; i++) {
					stack.pop();
				}
				String intUsed = stack.peek();
				stack.add("E");
				stack.add(Integer.toString(goTo[Integer.parseInt(intUsed)][0]));
				
				
			// Test for Reduce 3 and handle	
			} else if (actArr[0] == 'r' && actArr[1]== '3') {
				for (int i = 0; i < 6; i++) {
					stack.pop();
				}
				String intUsed = stack.peek();
				stack.add("T");
				stack.add(Integer.toString(goTo[Integer.parseInt(intUsed)][1]));
				
			// Test for Reduce 5 and handle	
			} else if (actArr[0] == 'r' && actArr[1]== '5') {
				for (int i = 0; i < 6; i++) {
					stack.pop();
				}
				String intUsed = stack.peek();
				stack.add("F");
				stack.add(Integer.toString(goTo[Integer.parseInt(intUsed)][2]));
				
				
			}
		}

		
		
		
	

	}
	// addInput method takes a char array and String Queue, adds correct lexemes from input to Queue
	// Does not return any value but alters parameter Queue
	private static void addInput(char[] arr, Queue<String> q) {
		for (int i = 0; i < arr.length; i++) {
			// Test for and handle "id" lexeme by adding to Queue
			if (arr[i] == 'i' && arr[i+1]=='d') {
				String first = Character.toString(arr[i]);
				String second = Character.toString(arr[i+1]);
				q.add(first+second);
			// Test for char not being d, since d is part of id, then just add char to Queue	
			} else if (arr[i] != 'd') {
				q.add(Character.toString(arr[i]));
			}
			
		}
		
	}
	// determineAction method to determine the correct action to execute, takes an int, String, and 2D String array as parameters
	// Returns action string if successful and valid, null otherwise
	private static String determineAction(int num, String s, String[][] arr) {
		String act = "";
		// Test if lexeme is null, if so, return null
		if (s == null) {
			return null;
		}
		// Tests for each lexeme and corresponding state value being valid entries in 2D array action table from parameter, if valid concatenate action String
		if (s.equalsIgnoreCase("id")&& arr[num][0] != null) {
			act += arr[num][0];
		} else if (s.equalsIgnoreCase("+")&& arr[num][1] != null) {
			act += arr[num][1];
		} else if (s.equalsIgnoreCase("*")&& arr[num][2] != null) {
			act += arr[num][2];
		} else if (s.equalsIgnoreCase("(")&& arr[num][3] != null) {
			act += arr[num][3];
		} else if (s.equalsIgnoreCase(")")&& arr[num][4] != null) {
			act += arr[num][4];
		} else if (s.equalsIgnoreCase("$") && arr[num][5] != null) {
			act += arr[num][5];
		}
		// Tests for each lexeme value and if the corresponding state and lexeme action value in 2D array is null, if so return null
		if (s.equalsIgnoreCase("id") && arr[num][0] == null) {
			return null;
		}
		
		if (s.equalsIgnoreCase("+") && arr[num][1] == null) {
			return null;
		}
		
		if (s.equalsIgnoreCase("*") && arr[num][2] == null) {
			return null;
		}
		
		if (s.equalsIgnoreCase("(") && arr[num][3] == null) {
			return null;
		}
		
		if (s.equalsIgnoreCase(")") && arr[num][4] == null) {
			return null;
		}
		if (s.equalsIgnoreCase("$") && arr[num][5] == null) {
			return null;
		}
		// Otherwise return action String
		return act;
	}

}
