import java.util.*;
import java.io.*;

//Make sure DBConnector is setup before using this class

public class DataReader {

	public static void main (String args []) throws Exception {
		DBConnector.setup();
		//Assuming that file is of the format: <name>_XXXX-YYYY-S.txt
		//XXXX-YYYY is the school year, S is the semester, file must be tab-delimited
		readFromFile("dorm_directory_2013-2014-2.txt");
		DBConnector.shutdown();
	}

	//fileName = x.txt
	public static void readFromFile (String fileName) throws Exception {
		BufferedReader in = new BufferedReader (new FileReader (fileName));
		System.out.println(in.readLine());
		String dataRow = "";
		
		String [] yearSem = (fileName.substring(fileName.lastIndexOf("_")+1, fileName.lastIndexOf("."))).split("-");
		int sem = Integer.parseInt(yearSem[2]);
		String schoolYear = yearSem[0] + "-" + yearSem[1];
		if (!DBConnector.hasSemester(schoolYear, sem)) {
			DBConnector.addNewSemester(schoolYear, sem);
		}

		/*
		while ((dataRow = in.readLine())!=null) {
			System.out.println(dataRow);
		}
		*/

		while ((dataRow = in.readLine())!=null) {
			String [] data = dataRow.split("\t");
			
			data[3] = data[3].replaceAll("\"", "");
			//Assuming that the hall has already been added to the DB. I can't add a new hall here since I need the complete hall name in addition to the hall identifier (i.e. I need University Dorm South, not just UDS)
			String hallID = data[4];
			String room = data[5];
			String idNumber = data[2];
			String lastName = data[3].substring(0,data[3].indexOf(","));
			lastName = lastName.replaceAll("'", "BLAH");
			String firstName = data[3].substring(data[3].indexOf(",") + 2);
			firstName = firstName.replaceAll("'", "BLAH");
			int period = firstName.lastIndexOf(".");
			
			System.out.println (lastName + " " + firstName);

			String middleInitial = (period==-1) ? null : firstName.substring(firstName.lastIndexOf(" ")+1,period);
			if (period>=0) firstName = firstName.substring(0, firstName.lastIndexOf(" "));
			String course = (data.length>7) ? data[7] : null;;
			String bday = (data.length>8) ? data[8] : null;
			if (bday!=null) {
				if (bday.trim().equals("")) bday = null;
				else {
					String [] temp = bday.split("/");
					bday = temp[2] + "-" + temp[0] + "-" + temp[1];
				}
			}

			//Would be better if they had a field for gender to consider the possibility of new dorm halls
			String gender = (hallID.equals("E") || hallID.equals("S")) ? "Female" : "Male";
			String email = (data.length>9) ? data[9] : null;
			String contact = (data.length>10) ? data[10] : null;
			String telephone = (data.length>11) ? data[11] : null;
			if (telephone!=null && telephone.trim().equals("")) telephone = null;
			
			String address = (data.length>12) ? data[12] : "No Address";
			address = address.replaceAll("'", "BLAH");
			address = address.replaceAll("\"", "");
			String city = address;
			String father = (data.length>13) ? data[13] : null;
			if (father!=null && father.trim().equals("")) father = null;
			String mother = (data.length>15) ? data[15] : null;
			if (mother!=null && mother.trim().equals("")) mother = null;
			if (father!=null) father = father.replaceAll("'", "BLAH");
			if (mother!=null) mother = mother.replaceAll("'", "BLAH");
			boolean isScholar = (data.length<=17 || data[17].charAt(0)=='N') ? false : true;
			if (!DBConnector.hasResident(idNumber)) {
				DBConnector.addNewResident(idNumber, lastName, firstName, middleInitial, course, bday, gender, address, city, father, mother, telephone, contact, email, isScholar);
			}
			if (!DBConnector.hasAttendance(idNumber, schoolYear, sem)) {
				DBConnector.addNewAttendance(idNumber, schoolYear, sem, hallID, room);
			}
		}
	}
}