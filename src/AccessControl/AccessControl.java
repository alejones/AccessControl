package AccessControl;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


/*
 * Load an access control matrix from a file
 * First line: subjects 
 * Second line: objects
 * Subsequent lines: access control matrix (comma separated)
 * e.g 
 * 
 *    alice, bob, carol, eve
 *    top-secret, secret, public
 *    rw,                
 *    r,          rw,    rw
 *    ,           r,     rw
 *    ,           ,      r   
 */
public class AccessControl {
	
	private String[] people, accessLevel;
	private String [][] accessGrid;
	
	
	// Define any fields or methods as necessary.
	
	// load access control matrix from a file by the name of "fileName"
	public void load(String fileName) throws IOException {		
		try {
			Scanner in = new Scanner(new FileReader(fileName));
			
			people = in.nextLine().split(",\\s+");
			accessLevel = in.nextLine().split(",\\s+");
			accessGrid = new String[people.length][accessLevel.length];
			
			for(int i = 0; i < people.length && in.hasNextLine(); i++)
				accessGrid[i] = in.nextLine().split(",\\s+");
			
			in.close();
		}
		catch(IOException IO) {
			System.out.println("Can't open file: " + fileName);
			throw(IO);
		}catch(Exception e) {
			System.out.println("Something went wrong");
			e.printStackTrace();
		}
	}
	
	//returns a string representation of the access control matrix
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("\t");
		for(String level: accessLevel)
			output.append(level + "\t");
		
		for(int i = 0; i < people.length; i++) {
			output.append("\n" + people[i]);
			for(int x = 0; x < accessGrid[i].length; x++) 
				output.append("\t" + accessGrid[i][x]);
		}
		
		return(output.toString());
	}
	
	//returns true if and only if subj can perform operation op on obj according to the access control matrix.
	public boolean check(String subj, String op, String obj) {
		
		//Find if obj is one of the accesLevel options
		for(int x = 0; x < accessLevel.length; x++) {
			if(accessLevel[x].equals(obj)) {
				//Find if the person is in the people array, and if they have the correct accessLevel
				for(int i = 0; i < people.length; i++) {
					if(people[i].toLowerCase().equals(subj.toLowerCase()))
						//Check if they have the access level for the operation
						if(accessGrid[i].length >= x && accessGrid[i][x].contains(op.toLowerCase()))
							return true;
				}	
			}
		}		
		return false;
	}

	// Driver method
	public static void main(String[] args) {
		AccessControl ac = new AccessControl();
		try {
			ac.load("ac_matrix");
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		} 
		System.out.println(ac);

		Scanner in = new Scanner(System.in);

		System.out.println("Enter your command (e.g. alice r secret):");
		String cmd = in.nextLine().trim(); 

		while(!cmd.equals("")) {
			String[] triple = cmd.split(" ");
			if(triple.length != 3) {
				System.out.println("Illegal command. Try again");
			}
			else {
				String subj = triple[0], op = triple[1], obj = triple[2];
				try {
					if(ac.check(subj, op, obj)) {
						System.out.printf("%s is allowed to perform %s operation on %s\n", subj, op, obj);
					}
					else {
						System.out.printf("%s is NOT allowed to perform %s operation on %s\n", subj, op, obj);
					}
				} catch(Exception e) { System.out.println(e); }
			}
			System.out.println("\nEnter your command");
			cmd = in.nextLine().trim();
		}

		in.close();
	}

}
