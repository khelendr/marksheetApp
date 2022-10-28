package edu.marksheet.result;

import java.awt.font.NumericShaper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class MarksheetModel {

	Scanner sc = new Scanner(System.in);
	private Connection con; // Connection reference declared inside class to make it accessible to all the

	PreparedStatement ps; // PreparedStatement reference declared inside class to make it accessible to
							// all the methods
	boolean flag = true;

	String rollNo = null;
	String name = null;
	String nameregx = "^[a-zA-Z]{2,15}\\s([a-zA-Z]{2,15}\\s)?[a-zA-Z]{2,15}$"; // Regular expression to validate Name of
																				// a Student
	String rollregx = "^[A-Za-z]{2}[0-9]{5}[1-9]{1}+$"; // Regular expression to validate roll number
	String markregx = "^[0-9]$|^[1-9][0-9]$|^(100)$";	// Regular expression for marks
	int maths = 0;
	int physics = 0;
	int chemistry = 0;
	String math = null;
	String physic = null;
	String chemist = null;

	// Passed connection reference
	public MarksheetModel(Connection con) {
		super();
		this.con = con;
	}

	// ------------------------- Addition method toinsert the record inside database
	public boolean add(Marksheet ms) {

		Boolean flag = true;
		Marksheet m = new Marksheet();

		// Checking for already existing record

		ArrayList al = new ArrayList();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select rollNo from marksheet");
			while (rs.next()) {
				al.add(rs.getString(1));
			}
			rs.close();

		} catch (SQLTimeoutException e) {
			System.err.println("Session timeout please try to connect again");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Problem in executing query");
		}

		// Logic for roll number insertion

		while (flag) {
			System.out.print("Enter roll Number (eg. EC141018) : ");
			try {
				rollNo = sc.nextLine().trim().toUpperCase();
				Boolean b = rollNo.matches(rollregx);

				if (b) {
					m.setRollNo(rollNo);
					flag = false;
				} else {
					System.err.println("Please enter the roll number in provided format(eg. 'EC141018')");
					
				}
			} catch (InputMismatchException e) {
				System.err.println("Enter roll Number (eg. EC141018) :");
				
			} catch (NullPointerException e) {
				System.err.println("Enter roll Number (eg. EC141018) :");
				
			}
		}
		flag = true;

		// Logic for name insertion

		if (!al.contains(rollNo)) {

			while (flag) {
				System.out.print("Enter Name (eg. Khelendra Pratap Singh) : ");
				try {
					name = sc.nextLine().trim().toUpperCase();

					Boolean b = name.matches(nameregx);

					if (b && !name.equalsIgnoreCase("null")) {
						m.setName(name);
						flag = false;
					} else {
						System.err.println(
								"Please enter the name number in provided format(eg. 'Khelendra Pratap Singh')");
						
					}
				} catch (InputMismatchException e) {
					System.err.println("Please enter valid name e.g.(Khelendra Pratap Singh)");
					

				} catch (NullPointerException e) {
					System.err.println("Enter valid name eg(Khelendra Pratap Singh) :");
					
				}
			}

			// Logic to add marks
			boolean flag1 = true;
			System.out.print("Enter Mathmatics marks : ");
			while (flag1) {

				try {
					math = sc.nextLine().trim();
					if (math.matches(markregx)) {
						maths = Integer.parseInt(math);
						m.setMath(maths);
						flag1 = false;

					} else {
						System.err.println("Enter valid marks between 0 to 100 eg(88,25,48..)");
						
					}

				} catch (InputMismatchException imme) {
					System.err.println("Please enter valid numeric values(eg 78, 45)");
					
				} catch (NullPointerException e) {
					System.err.println("Please enter valid numeric values(eg 78, 45)");
					
				} catch (NumberFormatException e) {
					System.out.println("Please enter valid number with no spaces and between 0 to 100");
					
				}
			}

			flag = true;
			// Logic to add physics marks
			while (flag) {
				System.out.print("Enter Physics marks : ");
				try {
					physic = sc.nextLine();

					if (physic.matches(markregx)) {
						physics = Integer.parseInt(physic);
						m.setPhysics(physics);
						flag = false;
					} else {
						System.err.println("Enter valid marks between 0 to 100 eg(88,25,48..)");
						
					}
				} catch (InputMismatchException imme) {
					System.err.println("Please enter valid numeric values(eg 78, 45)");
					

				} catch (NullPointerException e) {
					System.err.println("Please enter valid numeric values(eg 78, 45)");
					
				}catch (NumberFormatException e) {
					System.out.println("Please enter valid input");
					
				}
			}

			flag = true;
			// Logic to add chemistry marks
			while (flag) {
				System.out.print("Enter Chemistry marks : ");
				try {
					chemist = sc.nextLine();

					if (chemist.matches(markregx)) {
						chemistry = Integer.parseInt(chemist);
						m.setChemistry(chemistry);
						flag = false;
					} else {
						System.err.println("Enter valid marks between 0 to 100 eg(88,25,48..)");
						
					}
				} catch (InputMismatchException imme) {
					System.err.println("Enter valid numeric  values(eg 78, 45)");
					

				} catch (NullPointerException e) {
					System.err.println("Please enter valid numeric values(eg 78, 45)");
					
				}catch (NumberFormatException e) {
					System.out.println("Please enter valid input");
					
				}
			}

			flag = false;

			try {
				PreparedStatement ps = con.prepareStatement("insert into Marksheet values(?,?,?,?,?)");
				ps.setString(1, m.getRollNo());
				ps.setString(2, m.getName());
				ps.setInt(3, m.getMath());
				ps.setInt(4, m.getPhysics());
				ps.setInt(5, m.getChemistry());
				ps.executeUpdate();
				flag = true;
			} catch (SQLException sqle) {
				System.err.println("Problem in PreparedStatement or Record already resides.");
			}

			if (flag == true) {
				return true;
			} else {
				return false;
			}
		} else {
			System.err.println("Record already exists. Please try with new roll number");
			return false;
		}
	}
	// ------------------------------------- Method to delete particular row
	
	public boolean delete(String str) {
		Long l=System.currentTimeMillis();
		System.out.println(l);
		boolean flag = true;
		rollNo = str;
		HashSet<String> al = new HashSet<String>();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("Select rollNo from marksheet");

			while (rs.next()) {
				al.add(rs.getString(1));
			}
			stmt.close();

		} catch (SQLTimeoutException sqletoe) {
			System.err.println("Running out of time. Re-execute..");
		} catch (SQLException sqle) {
			System.err.println("Trouble in statement creation or quering the data.");
		}

		if (al.contains(rollNo)) {
			try {
				PreparedStatement ps = con.prepareStatement("delete from marksheet where rollNo=?");
				ps.setString(1, rollNo);
				ps.executeUpdate();
				System.out.println("Deleted successfully");
			} catch (SQLException sqle) {
				System.err.println("Problem in establishing connection. Try again....");
			}
		} else {
			flag = false;
		}
		return flag;
	}

	// -------------------------------Implementing update method

	public boolean update(String str) {
		boolean flag = true;

		String rollNo = str;
		HashSet<String> hs = new HashSet<String>();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select rollNo from marksheet");

			while (rs.next()) {
				hs.add(rs.getString(1));
			}
			stmt.close();

		} catch (SQLTimeoutException e) {
			System.err.println("Session expire please retry...");
		} catch (SQLException e) {
			System.err.println("Problem in record access.");
		}
		if (hs.contains(rollNo)) {
			System.out.print(
					"Enter option you want to update :\n1. Press '1' to update name                      	         2. Press '2' to update Mathmatics Mark\n3. Press '3' to update Physics marks                             4. Press '4' to update Chemistry marks\n");
			System.out.println("5. Press '5' to update all field.");
			String option = sc.nextLine();
			int opt=Integer.parseInt(option);
			if (opt == 1) {
				System.out.println(rollNo);
				try {
					PreparedStatement ps = con.prepareStatement("update marksheet set name=? where rollno=?");
					
					while (flag) {
						System.out.println("Enter name : ");
						try {
							name = sc.nextLine().trim().toUpperCase();
							if (name.matches(nameregx) && !name.equalsIgnoreCase("null") && name != null) {
								this.name = name;
								flag = false;
							} else {
								System.err.println("Enter valid name eg(Khelendra Pratap Singh)");
							}
						} catch (InputMismatchException e) {
							System.err.println("Enter valid name eg(Khelendra Pratap Singh)");
						} catch (NullPointerException e) {
							System.err.println("Enter valid name eg(Khelendra Pratap Singh)");
						}catch (NumberFormatException e) {
							System.out.println("Please enter valid input");
						}
					}
					flag = true;
					ps.setString(1, name);
					ps.setString(2, rollNo);
					ps.executeUpdate();

				} catch (SQLException e) {
					System.err.println("Issue in updating name");
				}
			} else if (opt == 2) {
				try {
					System.out.println(rollNo);
					PreparedStatement ps = con.prepareStatement("update marksheet set maths=? where rollno=?");
					while (flag) {

						System.out.println("Enter mathmatics marks : ");
						try {
							math = sc.nextLine();

							if (math.matches(markregx)) {
								maths = Integer.parseInt(math);
								this.maths = maths;
								flag = false;
							} else {
								System.err.println("valid range is between 0 to 100");
								
							}
						} catch (InputMismatchException e) {
							System.err.println("Enter numeric values only between 0 to 100");
						} catch (NullPointerException e) {
							System.err.println("Enter numeric values only between 0 to 100");
						}catch (NumberFormatException e) {
							System.out.println("Please enter valid input");
						}
					}
					flag = true;
					ps.setInt(1, this.maths);
					ps.setString(2, rollNo);
					ps.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
					System.err.println("Issue in updating mathematics marks");
				}
			} else if (opt == 3) {
				try {
					System.out.println(rollNo);
					PreparedStatement ps = con.prepareStatement("update marksheet set physics=? where rollno=?");

					while (flag) {
						System.out.println("Enter physics marks : ");
						try {
							physic = sc.nextLine();
							if (physic.matches(markregx)) {
								physics = Integer.parseInt(physic);
								this.physics = physics;
								flag = false;
							} else {
								System.err.println("valid range is between 0 to 100");
							}
						} catch (InputMismatchException e) {
							System.err.println("valid range is between 0 to 100");
						} catch (NullPointerException e) {
							System.err.println("valid range is between 0 to 100");
						} catch (NumberFormatException e) {
							System.out.println("Enter valid input");
						}
					}

					flag = true;

					ps.setInt(1, physics);
					ps.setString(2, rollNo);
					ps.executeUpdate();
				} catch (SQLException e) {
					System.err.println("Issue in updating physics marks");
				}
			} else if (opt == 4) {
				try {

					PreparedStatement ps = con.prepareStatement("update marksheet set chemistry=? where rollno=?");

					while (flag) {
						System.out.println("Enter chemistry marks : ");
						try {
							chemist = sc.nextLine();
							if (chemist.matches(markregx)) {
								chemistry = Integer.parseInt(chemist);
								this.chemistry = chemistry;
								flag = false;
							} else {
								System.err.println("valid range is between 0 to 100");
							}
						} catch (InputMismatchException e) {
							System.err.println("Please enter the valid numeric value between 0 to 100");
						} catch (NullPointerException e) {
							System.err.println("Please enter the valid numeric value between 0 to 100");
						} catch (NumberFormatException e) {
							System.out.println("Enter valid input");
						}
					}
					flag = true;

					ps.setInt(1, chemistry);
					ps.setString(2, rollNo);
					ps.executeUpdate();

				} catch (SQLException e) {
					System.err.println("Issue in updating chemistry marks");
				}
			}

			// -----------------------------------------------------------------------------------

			else if (opt == 5) {
				try {

					PreparedStatement ps = con.prepareStatement(
							"update marksheet set name=?, maths=?,physics=?,chemistry=? where rollno=?");

					while (flag) {
						System.out.println("Enter name : ");
						try {
							name = sc.nextLine().trim().toUpperCase();
							if (name.matches(nameregx) && !name.equalsIgnoreCase("null") && name != null) {
								this.name = name;
								flag = false;
							} else {
								System.err.println("Enter valid name eg(Khelendra Pratap Singh)");
							}
						} catch (InputMismatchException e) {
							System.err.println("Enter valid name eg(Khelendra Pratap Singh)");
						} catch (NullPointerException e) {
							System.err.println("Enter valid name eg(Khelendra Pratap Singh)");
						} catch (NumberFormatException e) {
							System.out.println("Enter valid input");
						}
					}

					flag = true;
					while (flag) {

						System.out.println("Enter mathmatics marks : ");
						try {
							math = sc.nextLine();
							if (math.matches(markregx)) {
								maths = Integer.parseInt(math);
								this.maths = maths;
								flag = false;
							} else {
								System.err.println("valid range is between 0 to 100");
							}
						} catch (InputMismatchException e) {
							System.err.println("Enter numeric values only between 0 to 100");
						} catch (NullPointerException e) {
							System.err.println("Enter numeric values only between 0 to 100");
						} catch (NumberFormatException e) {
							System.out.println("Enter valid input");
						}
					}

					flag = true;
					while (flag) {
						System.out.println("Enter physics marks : ");
						try {
							physic = sc.nextLine();
							if (physic.matches(markregx)) {
								physics = Integer.parseInt(physic);
								this.physics = physics;
								flag = false;
							} else {
								System.err.println("valid range is between 0 to 100");
							}
						} catch (InputMismatchException e) {
							System.err.println("valid range is between 0 to 100");
						} catch (NullPointerException e) {
							System.err.println("valid range is between 0 to 100");
						} catch (NumberFormatException e) {
							System.out.println("Enter valid input");
						}
					}

					flag = true;
					while (flag) {
						System.out.println("Enter chemistry marks : ");
						try {
							chemist = sc.nextLine();
							if (chemist.matches(markregx)) {
								chemistry = Integer.parseInt(chemist);
								this.chemistry = chemistry;
								flag = false;
							} else {
								System.err.println("valid range is between 0 to 100");
							}
						} catch (InputMismatchException e) {
							System.err.println("Please enter the valid numeric value between 0 to 100");
						} catch (NullPointerException e) {
							System.err.println("Please enter the valid numeric value between 0 to 100");
						} catch (NumberFormatException e) {
							System.out.println("Enter valid input");
						}
					}
					ps.setString(1, name);
					ps.setInt(2, maths);
					ps.setInt(3, physics);
					ps.setInt(4, chemistry);
					ps.setString(5, rollNo);
					ps.executeUpdate();
					flag = true;

				} catch (SQLException e) {
					System.err.println("Issue in updating chemistry marks");
				}
			}

			// -----------------------------------------------------------------------------------
			else {
				System.err.println("Retry again");
			}
		} else {
			System.err.println("Roll number not found");
		}
		return true;
	}

	// ----------------------------------------------Implementing UpdateAll method

	public Marksheet updateAll(Marksheet ms) {

		Marksheet m = ms;
		ArrayList<String> hs = new ArrayList<String>();
		
		System.out.println("Do you really want to update all records???");
		System.out.println("Press Y/y to update all records else enter N/n to avoid");
		String in=sc.nextLine();
		
		if(in.equalsIgnoreCase("y")) {
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select rollNo from marksheet");

			while (rs.next()) {
				hs.add(rs.getString(1));
			}
			stmt.close();
			System.out.println(hs);
		} catch (SQLTimeoutException e) {
			System.err.println("Session expire please retry...");
		} catch (SQLException e) {
			System.err.println("Problem in record access.");
		}
		int size = hs.size();
		System.out.println(size);

		int i = 0;
		while (i < size) {
			if (hs.contains(hs.get(i))) {
				System.out.println(hs.get(i));
				try {
					PreparedStatement ps = con.prepareStatement(
							"update marksheet set name=?, maths=?, physics=?, chemistry=? where rollno=?");
					flag = true;
					while (flag) {
						System.out.println("Enter name : ");
						try {
							name = sc.nextLine().trim().toUpperCase();
							if (name.matches(nameregx) && name != null) {
								this.name = name;
								flag = false;
							} else {
								System.err.println("Enter alphabets only eg( Khelendra Pratap Singh)");
							}
						} catch (InputMismatchException e) {
							System.err.println("Please enter valid name eg(Khelendra Pratap Singh)");
						} catch (NullPointerException e) {
							System.err.println("Enter valid name eg(Khelendra Pratap Singh)");
						}
					}

					flag = true;
					ps.setString(1, name);

					while (flag) {
						System.out.print("Enter mathmatics marks");
						try {
							math = sc.nextLine();
							if (math.matches(markregx)) {
								maths = Integer.parseInt(math);
								this.maths = maths;
								flag = false;
							} else {
								System.err.println("valid range is between 0 to 100");
							}
						} catch (InputMismatchException e) {
							System.err.println("Enter valid numeric value eg(12,48,89)");
						} catch (NullPointerException e) {
							System.err.println("Enter valid numeric value");
						}
					}
					ps.setInt(2, maths);

					flag = true;
					while (flag) {
						System.out.print("Enter physics marks");
						try {
							physic = sc.nextLine();
							if (physic.matches(markregx)) {
								physics = Integer.parseInt(physic);
								this.physics = physics;
								flag = false;
							} else {
								System.err.println("valid range is between 0 to 100");
							}
						} catch (InputMismatchException e) {
							System.err.println("Enter valid numeric value eg(12,48,89)");
						} catch (NullPointerException e) {
							System.err.println("Enter valid numeric value eg(12,48,58)");
						}
					}
					ps.setInt(3, physics);
					flag = true;

					while (flag) {
						System.out.print("Enter chemistry marks");
						try {
							chemist = sc.nextLine();
							if (chemist.matches(markregx)) {
								chemistry = Integer.parseInt(chemist);
								this.chemistry = chemistry;
								flag = false;
							} else {
								System.err.println("valid range is between 0 to 100");
							}
						} catch (InputMismatchException e) {
							System.err.println("Enter valid numeric value eg(12,48,89)");
						} catch (NullPointerException e) {
							System.err.println("Enter valid numeric value eg(0,89)");
						}
					}
					flag = true;
					ps.setInt(4, chemistry);
					ps.setString(5, hs.get(i++));
					ps.executeUpdate();
					System.out.println("Updated Sccessfully");
				} catch (SQLException e) {
					System.err.println("Issue in updating name");
				}
			}
		}
		System.out.println("Updated Successfully");
		}else if(in.equalsIgnoreCase("n")) {
			return m;
		}
		
		return m;
	}

	// ---------------------------Implementing deleteAll method
	public boolean deleteAll() {
		int count = 0;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select count(RollNo) from Marksheet");
			rs.next();
			count = rs.getInt(1);
			stmt.close();
		} catch (SQLTimeoutException e) {
			System.err.println("Session timeout please try to connect again");
		} catch (SQLException e) {
			System.err.println("Problem in executing query");
		}

		if (count != 0) {
			try {
				PreparedStatement ps = con.prepareStatement("delete from marksheet");
				ps.executeUpdate();
			} catch (SQLTimeoutException e) {
				System.err.println("Session time out reconnect.");
			} catch (SQLException e) {
				System.err.println("Problem in fetching record, retry...");
			}

		} else {
			System.err.println("You have no records available to delete.");
		}
		return true;
	}

//--------------------------------------------------------------------Implementing get() method
	public ArrayList get(String str) {
		ArrayList al = new ArrayList();
		ArrayList all = new ArrayList();

		this.rollNo = str;
		flag = true;
		while (flag) {

			if (rollNo.matches(rollregx)) {
				try {
					Statement stmt = con.createStatement();
					ResultSet rs = stmt.executeQuery("select rollNo from marksheet");
					while (rs.next()) {
						al.add(rs.getString(1));
					}
					rs.close();

				} catch (SQLTimeoutException e) {
					System.err.println("Session timeout please try to connect again");
				} catch (SQLException e) {
					System.err.println("Problem in executing query");
				}

				if (al.contains(rollNo)) {

					try {
						PreparedStatement ps = con.prepareStatement("select* from marksheet where rollNo=?");
						ps.setString(1, rollNo);
						ResultSet rs = ps.executeQuery();
						while (rs.next()) {
							String roll = rs.getString(1);
							String na = rs.getString(2);
							String math = rs.getInt(3) + "";
							String phy = rs.getInt(4) + "";
							String chem = rs.getInt(5) + "";

							all.add("Roll number : " + roll + "\nName of candidate : " + na + "\nMathmatics marks : "
									+ math + "\nPhysics Mark : " + phy + "\nChemistry Mark : " + chem + "\n\n");

						}
					} catch (SQLTimeoutException e) {
						System.err.println("Session expire please try again");
					} catch (SQLException e) {
						System.err.println("Problem in retriving data.");
					}

				} else {
					System.err.println("Roll number does not exist. Please enter valid roll number.");
				}
				flag = false;
			} else {
				System.err.println("Roll number does not exist. Please enter valid roll number. eg(EC141018)");
				flag = true;
			}
		}
		return all;
	}

	// -------------------------------Implementing getAll() method
	public Set getAll() {
		TreeSet al = new TreeSet();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from marksheet");

			while (rs.next()) {

				String roll = rs.getString(1);
				String na = rs.getString(2);
				String math = rs.getInt(3) + "";
				String phy = rs.getInt(4) + "";
				String chem = rs.getInt(5) + "";

				al.add("Roll number : " + roll + "\nName of candidate : " + na + "\nMathmatics marks : " + math
						+ "\nPhysics Mark : " + phy + "\nChemistry Mark : " + chem + "\n\n");

			}
			rs.close();

		} catch (SQLTimeoutException e) {
			System.err.println("Session timeout please try to connect again");
		} catch (SQLException e) {
			System.err.println("Problem in executing query");
		}
		return al;
	}

	// ------------------------------------- Implementing getMeritList method

	public HashSet getMeritList() {

		HashSet h = new HashSet();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select*from marksheet where (maths>32 and physics>32 and chemistry>32) order by (maths+physics+chemistry) desc limit 10");
			int i = 0;
			while (rs.next() && i++ < 10) {
				String roll = rs.getString(1);
				String na = rs.getString(2);
				String math = rs.getInt(3) + "";
				String phy = rs.getInt(4) + "";
				String chem = rs.getInt(5) + "";

				h.add(("Roll number : " + roll + "\nName of candidate : " + na + "\nMathmatics marks : " + math
						+ "\nPhysics Mark : " + phy + "\nChemistry Mark : " + chem + "\n\n"));

			}
			rs.close();

		} catch (SQLTimeoutException e) {
			System.err.println("Session timeout please try to connect again");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Problem in executing query");
		}

		return h;
	}

	// -------------------------------------------Implementing the number of student
	public int numberOfStudents() {
		int totalStudent = 0;
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from marksheet");
			while (rs.next()) {
				totalStudent++;
			}
			rs.close();

		} catch (SQLTimeoutException e) {
			System.err.println("Session timeout please try to connect again");
		} catch (SQLException e) {
			System.err.println("Problem in executing query");
		}

		if (totalStudent != 0) {
			return totalStudent;
		} else {
			return 0;
		}
	}

	// ------------------------------------Implementing getFailedStudents() method
	public ArrayList getFailedStudents() {
		ArrayList al = new ArrayList();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select rollNo ,name,maths,physics,chemistry from marksheet where maths<33 AND physics<33 AND chemistry<33");

			while (rs.next()) {
				String roll = rs.getString(1);
				String na = rs.getString(2);
				String math = rs.getInt(3) + "";
				String phy = rs.getInt(4) + "";
				String chem = rs.getInt(5) + "";

				al.add("Roll number : " + roll + "\nName of candidate : " + na + "\nMathmatics marks : " + math
						+ "\nPhysics Mark : " + phy + "\nChemistry Mark : " + chem + "\n\n");

			}
			rs.close();
		} catch (SQLTimeoutException e) {
			System.err.println("Session timeout please try to connect again");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Problem in executing query");
		}

		return al;
	}

	// -------------------------------Implementing getAbsenties() method
	public ArrayList getAbsenties() {
		ArrayList al = new ArrayList();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select rollNo ,name from marksheet where maths=0 AND physics=0 AND chemistry=0");

			while (rs.next()) {
				String roll = rs.getString(1);
				String na = rs.getString(2);
				al.add("Roll number : " + roll + "\nName of candidate : " + na + "\n\n");

			}
			rs.close();

		} catch (SQLTimeoutException e) {
			System.err.println("Session timeout please try to connect again");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Problem in executing query");
		}

		return al;
	}

	// ----------------------------Implementing getTopper() method

	public ArrayList getTopper() {
		ArrayList al = new ArrayList();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select rollNo, name from marksheet where maths+physics+chemistry = ( select max(maths+physics+chemistry) from marksheet)");
			while (rs.next()) {

				String roll = rs.getString(1);
				String na = rs.getString(2);

				al.add("Roll number : " + roll + "\nName of candidate : " + na + "\n\n");

			}
			rs.close();

		} catch (SQLTimeoutException e) {
			System.err.println("Session timeout please try to connect again");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Problem in executing query");
		}

		return al;
	}

	// --------------------------------------Implementing getLowestMarkStudent();
	public String[] getLowestMarkStudent() {

		String[] str = {};

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select rollNo, name from marksheet where maths+physics+chemistry = ( select min(maths+physics+chemistry) from marksheet where maths>0 and physics>0 and chemistry>0)");
			rs.last();
			str = new String[rs.getRow()];
			rs.beforeFirst();
			;
			int i = 0;
			while (rs.next()) {

				String roll = rs.getString(1);
				String na = rs.getString(2);
				str[i++] = ("Roll number : " + roll + "\nName of candidate : " + na + "\n\n");
			}
			rs.close();

		} catch (SQLTimeoutException e) {
			System.err.println("Session timeout please try to connect again");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Problem in executing query");
		}

		return str;
	}

	// -----------------------------------Implementing getPassedStudents() method
	public String[] getPassedStudents() {
		ArrayList al = new ArrayList();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt
					.executeQuery("select rollNo ,name from marksheet where maths>32 AND physics>32 AND chemistry>32");
			while (rs.next()) {

				String roll = rs.getString(1);
				String na = rs.getString(2);

				al.add("Roll number : " + roll + "\nName of candidate : " + na + "\n\n");

			}
			rs.close();

		} catch (SQLTimeoutException e) {
			System.err.println("Session timeout please try to connect again");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Problem in executing query");
		}
		String[] str = new String[al.size()];
		for (int i = 0; i < al.size(); i++) {
			str[i] = (String) al.get(i);
		}
		return str;
	}

	// ----------------------------------------Implementing
	// getAverageResultOfClass()
	public double getAverageResultOfClass() {
		double avg = 0.0;
		int count = 0;
		ArrayList<Integer> al = new ArrayList<Integer>();
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select maths,physics, chemistry from marksheet");

			while (rs.next()) {
				al.add(rs.getInt("maths"));
				al.add(rs.getInt("physics"));
				al.add(rs.getInt("chemistry"));
				count++;
			}
			rs.close();

		} catch (SQLTimeoutException e) {
			System.err.println("Session timeout please try to connect again");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Problem in executing query");
		}

		for (int i = 0; i < al.size(); i++) {
			avg = avg + al.get(i);
		}

		return avg / count;
	}

	// --------------------------implementing getATKTStudents()
	public List getATKTStudents() {
		ArrayList al = new ArrayList();

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"select rollNo,name from marksheet where not ((not maths > 32 and not physics >32 and not chemistry >32) OR (not maths <33 and not physics <33 and not chemistry  <33))");
			while (rs.next()) {
				String roll = rs.getString(1);
				String na = rs.getString(2);
				al.add("Roll number : " + roll + "\nName of candidate : " + na + "\n\n");
			}
			rs.close();
		} catch (SQLTimeoutException e) {
			System.err.println("Session timeout please try to connect again");
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Problem in executing query");
		}
		return al;
	}
}
