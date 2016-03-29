//Compile: javac -cp .;mysql-connector-java-5.1.36-bin.jar DBConnector.java
//Run: java -cp .;mysql-connector-java-5.1.36-bin.jar DBConnector

//Always call DBConnector.setup() at the beginning of your program.
//Call DBConnector.shutdown() at the end.

import java.sql.*;
import java.util.*;

public class DBConnector {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/residentsdb";

	//Database User and Password
	static final String USER = "root";
	static final String PASS = "root";

	public static void main (String args []) throws Exception {
		DBConnector.setup();
		System.out.println("~~ All Residents");
		ArrayList<Resident> temp = DBConnector.getAllResidents();
		for (Resident r : temp)
			r.printInfo();
		System.out.println("~~ Current Residents");
		temp = DBConnector.getCurrentResidents();
		for (Resident r : temp)
			r.printInfo();
		System.out.println("~~ Residents in UDS");
		temp = DBConnector.getResidentsByHall("UDS");
		for (Resident r : temp)
			r.printInfo();
		System.out.println("~~ Residents named V");
		temp = DBConnector.getResidentsByName("v");
		for (Resident r : temp)
			r.printInfo();
		System.out.println("~~ Male Residents");
		temp = DBConnector.getResidentsByGender("Male");
		for (Resident r : temp)
			r.printInfo();
		System.out.println("~~ Residents from Ormoc");
		temp = DBConnector.getResidentsByProvince("ormoc");
		for (Resident r : temp)
			r.printInfo();
		System.out.println("~~ Residents born in 1996");
		temp = DBConnector.getResidentsByBirthday(-1, -1, 1996);
		for (Resident r : temp)
			r.printInfo();
		System.out.println("~~ Residents in dorm from 2012-2013 sem 2 to 2014-2015 sem 1");
		temp = DBConnector.getResidentsBySemester("2012-2013", 2, "2014-2015", 1);
		for (Resident r : temp)
			r.printInfo();
		System.out.println("~~ Residents in dorm from 2010-2012");
		temp = DBConnector.getResidentsByYear(2010, 2012);
		for (Resident r : temp)
			r.printInfo();
		System.out.println("~~ BSCS Residents");
		temp = DBConnector.getResidentsByCourse("BSCS");
		for (Resident r : temp)
			r.printInfo();
		System.out.println("~~ Residents living in Berlin");
		temp = DBConnector.getResidentsByAddress("berlin");
		for (Resident r : temp)
			r.printInfo();
		System.out.println("~~ Scholar Residents");
		temp = DBConnector.getResidentsByScholarship(true);
		for (Resident r : temp)
			r.printInfo();
		String [] queries = {"V", "CS", "Berlin", "Leyte", "1995-07-27", "Female", "false", "UDS", "2012-2013", "0", "2015-2016", "2"};
		System.out.println ("~~ Residents matching given queries");
		temp = DBConnector.queryResidents (queries);
		for (Resident r : temp)
			r.printInfo();
		String [] queries2 = {null, null, null, "Abra", null, "Male", "true", null, "2010-2011", "0", "2010-2011", "0"};
		System.out.println ("~~ Residents matching another set of given queries");
		temp = DBConnector.queryResidents (queries2);
		for (Resident r : temp)
			r.printInfo();
		
		
		// System.out.println("~~ Adding Stuff");
		// DBConnector.addNewResident ("000000", "Unknown", "Test", "H", "BS Studying", "2015-06-04", "Male", "Streets", "New York", "Father", "Mother", "Telephone", "Contact", "whatever@whatever.com", true);
		// DBConnector.addNewResident ("555555", "Nullfield", "Second Tester", null, "BS null", null, "Female", "Maasin City", "Southern Leyte", null, null, null, "0912-345-1234", "email@kunyare.com", false);
		// DBConnector.addNewSemester ("2012-2013", 1);
		// DBConnector.addNewAttendance ("123609", "2012-2013", 1, "EH", "304");
		// DBConnector.addNewHall ("V", "V-ann Hall");
		DBConnector.shutdown();
	}

	static Connection conn = null;

	public static void setup () {
		try {
			Class.forName(JDBC_DRIVER);
			System.out.println ("Connecting to database...");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			System.out.println ("Connected.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void shutdown () {
		try {
			conn.close();
			System.out.println ("Disconnected from the database.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Resident> getAllResidents () throws Exception {
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM residents_t";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}
	public static ArrayList<Resident> getCurrentResidents () throws Exception{
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int sem = (month>7) ? 1 : (month<6) ? 2 : 0;
		String schoolYear = (sem<2) ? year + "-" + (year+1) : (year-1) + "-" + year;
		String sql = "SELECT residents_t.idNumber, lastName, firstName, middleInitial, course, birthday, gender, address, cityProvince, fatherName, motherName, contactNum, email, telephoneNum, isScholar FROM residents_t, attendance_t WHERE schoolYear=\"" + schoolYear + "\" AND semester=" + sem + " AND attendance_t.idNumber=residents_t.idNumber"; 
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}
	//E, S, N, C
	public static ArrayList<Resident> getResidentsByHall (String hall) throws Exception{
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT DISTINCT residents_t.idNumber, lastName, firstName, middleInitial, course, birthday, gender, address, cityProvince, fatherName, motherName, contactNum, email, telephoneNum, isScholar FROM residents_t, attendance_t WHERE hallID=\"" + hall + "\" AND attendance_t.idNumber=residents_t.idNumber";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}
	//name is searched on first name, middle name, and last name
	public static ArrayList<Resident> getResidentsByName (String name) throws Exception{
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM residents_t WHERE firstName LIKE \"%" + name  + "%\" OR lastName LIKE \"%" + name  + "%\" OR middleInitial LIKE \"%" + name  + "%\"";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}
	//Female or Male
	public static ArrayList<Resident> getResidentsByGender (String gender) throws Exception{
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM residents_t WHERE gender=\"" + gender + "\"";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}
	//accepts province or city
	public static ArrayList<Resident> getResidentsByProvince (String province) throws Exception {
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM residents_t WHERE cityProvince LIKE \"%" + province + "%\"";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}
	//if any month, any date, or any year, put -1; at least one of month or year has to be set
	//month: 1-12, date: 1-21, year: normal
	public static ArrayList<Resident> getResidentsByBirthday (int month, int date, int year) throws Exception {
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM residents_t WHERE";
		if (month>0) sql += " MONTH(birthday)=" + month;
		if (year>0) {
			if (month>0) sql += " AND YEAR(birthday)=" + year;
			else sql += " YEAR(birthday)=" + year;
		}
		if (date>0) sql += " AND DAYOFMONTH(birthday)=" + date;
		sql += " AND YEAR(birthday)!=0";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}
	//from <= to
	public static ArrayList<Resident> getResidentsBySemester (String fromYear, int fromSem, String toYear, int toSem) throws Exception{
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT DISTINCT residents_t.idNumber, lastName, firstName, middleInitial, course, birthday, gender, address, cityProvince, fatherName, motherName, contactNum, email, telephoneNum, isScholar FROM residents_t, attendance_t WHERE attendance_t.idNumber=residents_t.idNumber AND ( ( schoolYear=\"" + fromYear + "\" AND semester=" + fromSem + ")"; 
		for (int i=fromSem+1; i<=2; i++) {
			sql += " OR ( schoolYear=\"" + fromYear + "\" AND semester=" + i + ")"; 
		}
		String schoolYear = fromYear.substring(fromYear.indexOf("-")+1) + "-" + (Integer.parseInt(fromYear.substring(fromYear.indexOf("-")+1))+1);
		while (!schoolYear.equals(toYear)) {
			sql += " OR (schoolYear=\"" + schoolYear + "\")";
			schoolYear = schoolYear.substring(schoolYear.indexOf("-")+1) + "-" + (Integer.parseInt(schoolYear.substring(schoolYear.indexOf("-")+1))+1);
		}
		for (int i=0; i<=toSem; i++) {
			sql += " OR (schoolYear=\"" + toYear + "\" AND semester=" + i + ")";
		}
		sql += ")";
		System.out.println(sql);
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}
	//from <= to
	public static ArrayList<Resident> getResidentsByYear (int from, int to) throws Exception{
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT DISTINCT residents_t.idNumber, lastName, firstName, middleInitial, course, birthday, gender, address, cityProvince, fatherName, motherName, contactNum, email, telephoneNum, isScholar FROM residents_t, attendance_t WHERE attendance_t.idNumber=residents_t.idNumber AND ( schoolYear LIKE \"%" + from + "%\""; 
		for (int year=from+1; year<=to; year++)
			sql += " OR schoolYear LIKE \"%" + year + "%\"";
		sql += ")";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}
	//course code only, no spaces
	public static ArrayList<Resident> getResidentsByCourse (String course) throws Exception {
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM residents_t WHERE course=\"" + course + "\"";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}
	//part of the address
	public static ArrayList<Resident> getResidentsByAddress (String address) throws Exception {
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM residents_t WHERE address LIKE \"%" + address + "%\"";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}
	public static ArrayList<Resident> getResidentsByScholarship (boolean isScholar) throws Exception {
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM residents_t WHERE isScholar=" + isScholar;
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}

	//Nullable Fields: middle, bday, father, mother, telephone
	//Input null for above fields if they do not exist
	public static void addNewResident (String idNumber, String last, String first, String middle, String course, String bday, String gender, String add, String city, String father, String mother, String telephone, String contact, String email, boolean isScholar) throws Exception {
		Statement stmt = conn.createStatement();
		String sql = "INSERT INTO residents_t (idNumber, lastName, firstName";
		if (middle!=null) sql += ", middleInitial";
		if (course!=null) sql += ", course";
		if (bday!=null) sql += ", birthday";
		sql += ", gender, address, cityProvince";
		if (father!=null) sql += ", fatherName";
		if (mother!=null) sql += ", motherName";
		if (telephone!=null) sql += ", telephoneNum";
		if (contact!=null) sql += ", contactNum";
		if (email!=null) sql += ", email";
		sql += ", isScholar)VALUES ('"+idNumber+"','"+last+"','"+first+"'";
		if (middle!=null) sql += ",'"+middle+"'";
		if (course!=null) sql += ",'"+course+"'";
		if (bday!=null) sql += ",'"+bday+"'";
		sql += ",'"+gender+"','"+add+"','"+city+"'";
		if (father!=null) sql += ",'"+father+"'";
		if (mother!=null) sql += ",'"+mother+"'";
		if (telephone!=null) sql += ",'"+telephone+"'";
		if (contact!=null) sql += ",'"+contact+"'";
		if (email!=null) sql += ",'"+email+"'";
		sql += ",'"+ ((isScholar) ? 1 : 0) +"')";
		System.out.println(sql);
		stmt.executeUpdate(sql);
		stmt.close();
		System.out.println ("Successfully added new resident.");
	}

	//roomNumber does not include hallID (i.e. use 304 instead of E304)
	public static void addNewAttendance (String idNumber, String schoolYear, int sem, String hallID, String roomNumber) throws Exception {
		Statement stmt = conn.createStatement();
		String sql = "INSERT INTO attendance_t (idNumber, schoolYear, semester, hallID, roomNumber)VALUES ('" + idNumber + "', '" + schoolYear + "', " + sem + ", '" + hallID + "', '" + roomNumber + "')";
		stmt.executeUpdate(sql);
		stmt.close();
		System.out.println ("Successfully added new attendance.");
	}

	//schoolYear should be in the form "XXXX-XXXX"
	//sem is 0 for intersession, 1 for first semester, 2 for second semester
	public static void addNewSemester (String schoolYear, int sem) throws Exception {
		Statement stmt = conn.createStatement();
		String sql = "INSERT INTO semester_t (schoolYear, semester) VALUES ('" + schoolYear + "', " + sem + ")";
		stmt.executeUpdate(sql);
		stmt.close();
		System.out.println ("Successfully added new semester.");
	}

	public static void addNewHall (String hallID, String hallName) throws Exception {
		Statement stmt = conn.createStatement();
		String sql = "INSERT INTO residenceHall_t (hallID, residenceHall) VALUES ('" + hallID + "','" + hallName + "')";
		stmt.executeUpdate(sql);
		stmt.close();
		System.out.println ("Successfully added new hall.");
	}


	//Checkers
	public static boolean hasResident (String idNumber) throws Exception {
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM residents_t WHERE idNumber='" + idNumber + "'";
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			rs.close();
			stmt.close();
			return true;
		}
		rs.close();
		stmt.close();
		return false;
	}
	public static boolean hasSemester (String schoolYear, int sem) throws Exception {
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM semester_t WHERE schoolYear='" + schoolYear + "' AND semester=" + sem;
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			rs.close();
			stmt.close();
			return true;
		}
		rs.close();
		stmt.close();
		return false;
	}
	public static boolean hasHall (String hallID) throws Exception {
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM residenceHall_t WHERE hallID='" + hallID + "'";
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			rs.close();
			stmt.close();
			return true;
		}
		rs.close();
		stmt.close();
		return false;
	}
	public static boolean hasAttendance (String idNumber, String schoolYear, int sem) throws Exception {
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM attendance_t WHERE idNumber='" + idNumber + "' AND schoolYear ='" + schoolYear + "' AND semester=" + sem;
		ResultSet rs = stmt.executeQuery(sql);
		if (rs.next()) {
			rs.close();
			stmt.close();
			return true;
		}
		rs.close();
		stmt.close();
		return false;
	}
	
	//queries should be an array of length 12 containing the following info in this order:
	//name, course, address, province, birthday, gender, isScholar, hall, schoolYearFrom, semesterFrom, schoolYearTo, semesterTo
	//birthday must be in the form XXXX-MM-DD
	//gender must be "Female" or "Male"
	//isScholar must be either "true" or "false"
	//hall must be "UDS", "UDN", "EH", or "CH"
	public static ArrayList<Resident> queryResidents (String [] queries) throws Exception{
		if (queries.length<12) throw new Exception ("Lacking queries");
		ArrayList<Resident> results = new ArrayList<Resident>();
		Statement stmt = conn.createStatement();
		String sql = "SELECT DISTINCT residents_t.idNumber, lastName, firstName, middleInitial, course, birthday, gender, address, cityProvince, fatherName, motherName, contactNum, email, telephoneNum, isScholar FROM residents_t, attendance_t WHERE attendance_t.idNumber=residents_t.idNumber"; 
		
		if (queries[0]!=null) sql += " AND ((lastName LIKE \"%" + queries[0] + "%\") OR (firstName LIKE \"%" + queries[0] + "%\") OR (middleInitial LIKE \"%" + queries[0] + "%\"))";
		if (queries[1]!=null) sql += " AND (course LIKE \"%" + queries[1] + "%\")";
		if (queries[2]!=null) sql += " AND (address LIKE \"%" + queries[2] + "%\")";
		if (queries[3]!=null) sql += " AND (cityProvince LIKE \"%" + queries[3] + "%\")";
		if (queries[4]!=null) sql += " AND (birthday=\"" + queries[4] + "\")";
		if (queries[5]!=null) sql += " AND (gender=\"" + queries[5] + "\")";
		if (queries[6]!=null) sql += " AND (isScholar=" + queries[6] + ")";
		if (queries[7]!=null) sql += " AND (hallID=\"" + queries[7] + "\")";
		if (queries[8]!=null && queries[9]!=null && queries[10]!=null && queries[11]!=null) {
			int fromSem = Integer.parseInt(queries[9]);
			sql += " AND ( ( schoolYear=\"" + queries[8] + "\" AND semester=" + fromSem + ")"; 
			for (int i=fromSem+1; i<=2; i++) {
				sql += " OR ( schoolYear=\"" + queries[8] + "\" AND semester=" + i + ")"; 
			}
			if (!queries[8].equals(queries[10])) {
				String schoolYear = queries[8].substring(queries[8].indexOf("-")+1) + "-" + (Integer.parseInt(queries[8].substring(queries[8].indexOf("-")+1))+1);
				while (!schoolYear.equals(queries[10])) {
					sql += " OR (schoolYear=\"" + schoolYear + "\")";
					schoolYear = schoolYear.substring(schoolYear.indexOf("-")+1) + "-" + (Integer.parseInt(schoolYear.substring(schoolYear.indexOf("-")+1))+1);
				}
			}
			int toSem = Integer.parseInt(queries[11]);
			for (int i=0; i<=toSem; i++) {
				sql += " OR (schoolYear=\"" + queries[10] + "\" AND semester=" + i + ")";
			}
			sql += ")";
		}
		System.out.println(sql);
		
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			results.add(new Resident(rs));
		}
		rs.close();
		stmt.close();
		return results;
	}
}
class Resident {
	String idNumber, lastName, firstName, middleInitial, course, birthday, gender, address, cityProvince, fatherName, motherName, contactNum, email, telephoneNum;
	boolean isScholar;
	public Resident (ResultSet resident) throws Exception{
		idNumber = resident.getString("idNumber");
		lastName = resident.getString("lastName");
		firstName = resident.getString("firstName");
		middleInitial = resident.getString("middleInitial");
		if (resident.wasNull()) middleInitial = "X";
		course = resident.getString("course");
		birthday = resident.getString("birthday");
		if (resident.wasNull()) birthday = "0000-01-01";
		gender = resident.getString("gender");
		address = resident.getString("address");
		cityProvince = resident.getString("cityProvince");
		fatherName = resident.getString("fatherName");
		if (resident.wasNull()) fatherName = "N/A";
		motherName = resident.getString("motherName");
		if (resident.wasNull()) motherName = "N/A";
		contactNum = resident.getString("contactNum");
		if (resident.wasNull()) contactNum = "N/A";
		email = resident.getString("email");
		if (resident.wasNull()) email = "N/A";
		telephoneNum = resident.getString("telephoneNum");
		if (resident.wasNull()) telephoneNum = "N/A";
		isScholar = resident.getBoolean("isScholar");
	}
	public void printInfo () {
		System.out.println (idNumber + ": " + lastName + ", " + firstName + " | " + course + " | " + gender + " | " + cityProvince);
	}
}