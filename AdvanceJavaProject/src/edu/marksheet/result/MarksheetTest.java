package edu.marksheet.result;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TreeSet;

public class MarksheetTest {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		BufferedReader br = null; // To read the driver and connection requirement
		Boolean flag = true;
		Connection con = null;

		try {
			br = new BufferedReader(new FileReader("E:/AdvanceJavaFile/project.txt"));

			Class.forName(br.readLine());
			con = DriverManager.getConnection(br.readLine(), br.readLine(), br.readLine());
			
			// ---------------------------------------Once the connection get
			// establishedOptions/
			String input = null;
			while (flag) {
				System.out.println("Enter the option Y/y to enter the program else N/n to exit.");
				try {
					input = sc.nextLine().trim();
					if (input.equalsIgnoreCase("y")) {
						flag = false;
					} else if (input.equalsIgnoreCase("n")) {
						System.err.println("Thanks for visiting us.");
						System.exit(0);
					} else {
						System.out.println("Enter valid input either Y/y Or N/n");
					}
				} catch (InputMismatchException e) {
					System.err.println("Enter the valid value either Y/y or N/n.");
				} catch (NullPointerException e) {
					System.err.println("Enter the valid value either Y/y or N/n.");
				}
			}

			System.out.println(
					"============================= Welcome to the Marksheet Management program ====================================");
			
			
			int opt = 0;
			Marksheet ms = new Marksheet();

			MarksheetModel mt = new MarksheetModel(con);
			System.out.println("Choose one among the below options to perform your required operations.");

			flag = true;
			while (flag) {
				
				System.out.println();
				System.out.println("1. To add data: ");
				System.out.println("2. To delete particular parameter.");
				System.out.println("3. To update record.");
				System.out.println("4. To update all record.");
				System.out.println("5. To delete all the records.");
				System.out.println("6. To get detail of particular roll number. ");
				System.out.println("7. To get detail of all candidate.");
				System.out.println("8. To get merit list.");
				System.out.println("9. To get the number of students in class.");
				System.out.println("10. To get failed students.");
				System.out.println("11. To get the absenties.");
				System.out.println("12. To get the topper.");
				System.out.println("13. To get the lowest mark of student.");
				System.out.println("14. To get Passed students.");
				System.out.println("15. To get the average result of students.");
				System.out.println("16. To get ATKT students.");
				System.out.println("17. To EXIT");
				System.out.println("Choose one option");
				System.out.println();
				flag = true;
				while (flag) {
					try {
						String option = sc.nextLine().trim();
						opt = Integer.parseInt(option);
						if (opt > 0 && opt < 18) {
							flag = false;
						} else {
							System.err.println("Choose option within the range of 1 to 17");
						}
					} catch (InputMismatchException e) {
						System.err.println("Enter the valid option on the list");
					} catch (NullPointerException e) {
						System.err.println("Enter some value");
					} catch (NumberFormatException e) {
						System.out.println("Please enter valid input");
					}
				}

				flag = true;

				// -------------------------------------------- Calling add() method
				if (opt == 1) {
					mt.add(ms);
				}

				// --------------------Calling delete() method to delete single record
				else if (opt == 2) {
					Boolean test = true;
					String roll = null;
					boolean result = false;
					boolean b = true;
					System.out.print("Enter the roll number of candidate to delete: ");
					while (test) {
						try {
							roll = sc.nextLine().trim().toUpperCase();
							b = roll.matches("^[A-Za-z]{2}[0-9]{6}+$");
							Long l=System.currentTimeMillis();
							System.out.println(l);
							if (b == true) {
								result = mt.delete(roll);
								test = false;
								Long ll=System.currentTimeMillis();
								System.out.println(ll);
								System.out.println(ll-l);
							} else {
								System.err.println("Please enter valid roll number eg(EC141018)");
							}
						} catch (InputMismatchException e) {
							System.err.println("Please enter the valid roll number eg(EC141018)");
						} catch (NullPointerException e) {
							System.err.println("Please enter some value");
						}
						
					}
					if (result == true) {
						System.out.println("Record with roll number " + roll + " deleted Successfully");
					} else {
						System.err.println("Record does not exist");

					}
				}

				// ------------------Calling update() method to update single record
				else if (opt == 3) {

					boolean b = true;

					String roll = null;

					System.out.println("Enter roll number you want to update");
					while (b) {
						try {
							roll = sc.nextLine().trim().toUpperCase();
							if (roll.matches("^[A-Za-z]{2}[0-9]{6}$")) {
								mt.update(roll);
								b = false;
							} else {
								System.err.println("Please enter the valid roll Number eg(EC141018)");
							}
						} catch (InputMismatchException e) {
							System.err.println("Please enter the valid roll Number eg(EC141018)");
						} catch (NullPointerException e) {
							System.err.println("Please enter some value");
						}
					}
				}

				// ------------------Calling updateAll() method to update all records
				else if (opt == 4) {
					mt.updateAll(ms);
					System.out.println("Your records has been updated successfully.");
				}

				// -----------------------------Calling deleteAll() method to delete all records

				else if (opt == 5) {

					System.err.println("Are you sure want to delete all records?");
					System.out.println("Press 'Y/y' to delete all record or press 'N/n' to exit");
					String in = sc.nextLine();
					if (in.equalsIgnoreCase("y")) {
						mt.deleteAll();
						System.out.println("Your all records has been deleted.");

					} else if (in.equalsIgnoreCase("n")) {
						System.out.println("Your record is saved as it is.");
					} else {
						System.out.println("Please enter valid options among Y/y or N/n .");
					}
				}

				// -----------------------------Calling

				else if (opt == 6) {
					ArrayList<String> al = new ArrayList<String>();
					boolean b = true;

					String roll = null;

					System.out.print("Enter roll number you want to see record : ");
					while (b) {
						try {
							roll = sc.nextLine().trim().toUpperCase();
							if (roll.matches("^[A-Za-z]{2}[0-9]{6}$")) {
								System.out.println(mt.get(roll));
								b = false;
							} else {
								System.err.println("Please enter the valid roll Number eg(EC141018)");
							}
						} catch (InputMismatchException e) {
							System.err.println("Please enter the valid roll Number eg(EC141018)");
						} catch (NullPointerException e) {
							System.out.println("Please enter the valid roll number eg(EC141018)");
						}

					}
				}

				// ---------------------Calling getAll() method to get record of all candidate
				else if (opt == 7) {
					Long l=System.nanoTime();
					System.out.println(l);
					TreeSet<String> ts = new TreeSet<String>();
					ts = (TreeSet<String>) mt.getAll();
					Long ll=System.nanoTime();
					System.out.println(ll);
					System.out.println(ll-l);
					for (String string : ts) {
						System.out.println(string);
					}
					System.out.println("The above candidate are the overall candidate of the class.");
				}

				// ----------------------------Calling getMeritList() method to get merit
				// Student

				else if (opt == 8) {
					HashSet<String> hs= new HashSet<String>();
					hs=mt.getMeritList();
					for (String string : hs) {
						System.out.println(string);
					}
					System.out.println("The above candidate are the meritorious students of the class.");
				}

				// ----------------------------Calling numberOfStudents() method to get total
				// students.

				else if (opt == 9) {
					int student = mt.numberOfStudents();
					System.out.println("The number of candidate(s) in the batch is "+student);
				}

				// ----------------------------Calling getFailedStudents() method to get the
				// failed Student
				else if (opt == 10) {
					ArrayList<String> al= new ArrayList<String>();
					al=mt.getFailedStudents();
					
					for (String string : al) {
						System.out.println(string);
					}
					System.out.println("The above candidate(s) are the failed candidate in the class.");
				}

				// ----------------------------Calling getAbsenties() method to get absent
				// student in exam

				else if (opt == 11) {
					System.out.println(mt.getAbsenties());
					System.out.println("The number above candidate(s) where the absenties of the class.");
				}

				// ---------------------------Calling getTopper() method to get the toppers

				else if (opt == 12) {
					ArrayList<String> al = new ArrayList<String>();
					al = mt.getTopper();
					for (String string : al) {
						System.out.println(string);
					}
					System.out.println("The above candidate(s) is the topper of class");
				}

				// --------------------------Calling getLowestMarkStudent() method to get Lowest
				// scorer

				else if (opt == 13) {
					String low[] = mt.getLowestMarkStudent();
					for (String string : low) {
						System.out.println(string);
					}
					System.out.println("The above candidate(s) are those candidate who secured lowest marks.");
				}

				// --------------------------Calling getPassedStudents() to get the passed
				// Students

				else if (opt == 14) {
					String s[] = mt.getPassedStudents();
					for (String string : s) {
						System.out.println(string);
					}
					System.out.println("The above candidate list is student who secured passing marks.");
				}

				// -------------------Calling getAverageResultOfClass() method to get the
				// average score

				else if (opt == 15) {
					double avg = mt.getAverageResultOfClass();
					System.out.println("The average result of a class is "+avg+" .");
				}

				// ------------------Calling getATKTStudents() method to get ATKTstudents

				else if (opt == 16) {
					ArrayList<String> al = new ArrayList<>();
					al = (ArrayList<String>) mt.getATKTStudents();
					for (String str : al) {
						System.out.println(str);
					}
					System.out.println("Above candidate are ATKT students");
					// ---------------------Getting out of program

				} else if (opt == 17) {
					System.out.println("Thanks for visiting us.Visit again.......");
					System.exit(0);
				}
			}

			System.out.println("<<<=====================================================================>>>");
			System.out.println();
		} catch (SQLException sqle) {
			System.out.println("Getting trouble in establishing connection.");
		} catch (ClassNotFoundException cnfe) {
			System.out.println("Driver is unable to find the path of driver");
		}

		catch (FileNotFoundException cnfe) {
			System.out.println("Please enter the valid path");
		}

		catch (IOException ioe) {
			System.out.println("There may be difficulty in reading the driver path, please fix it.");
		} finally {
			try {
				br.close();
				con.close();
				sc.close();

			} catch (IOException ioe) {
				System.err.println("Problem in closing connection.");
			} catch (SQLException e) {
				System.out.println("Problem in closing connection.");
			}
		}
	}
}
