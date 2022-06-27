import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * @author Parth Gondaliya
 *
 */
public class Genealogy {
	
	
	/**
	 * Add person attributes
	 * @param person
	 * @param attributes
	 * @return
	 * @throws Exception
	 */
	Boolean recordAttributes( PersonIdentity person, Map<String, String> attributes ) throws Exception {
		
		//person id 
		int personId = person.person_id;
		
		//validation 
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			//empty value of key is not allowed
			if(entry.getKey().trim().length()==0) {
				return false;
			}
		}
		
		//store the attributes in db
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			
			
			//update if possible 
			PreparedStatement prepStatement1 = SqlConnection.getConnection().prepareStatement("select * from person_attributes where PersonId=? and keyOfAttribute=?");
			prepStatement1.setInt(1,personId);
		    prepStatement1.setString(2,key);
		    
		    ResultSet response1 = prepStatement1.executeQuery();
		    
			if(response1.next()) {
				//update the data 
				PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("UPDATE person_attributes SET valueOfAttribute =? WHERE PersonId =? and keyOfAttribute=?");
				prepStatement.setString(1,value);
				prepStatement.setInt(2,personId);
				prepStatement.setString(3,key);
				prepStatement.execute();
				
			 	prepStatement.close();
			    SqlConnection.closeConnection(SqlConnection.getConnection());
			}else {
				//add new data
				PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("INSERT INTO person_attributes VALUES (?, ?, ?)");
				prepStatement.setInt(1,personId);
				prepStatement.setString(2,key);
				prepStatement.setString(3,value);
				int res  = prepStatement.executeUpdate();
				
			 	prepStatement.close();
			    SqlConnection.closeConnection(SqlConnection.getConnection());
				
				if(res==0) {
					return false;
				}
			}
		}
		return true;
	}
	

	
	/**
	 * @ Add person references
	 * @param person
	 * @param reference
	 * @return
	 * @throws Exception 
	 */
	Boolean recordReference( PersonIdentity person, String reference ) throws Exception {
		//could have multiple references
		
		if(person == null || reference == null || reference.trim().length() == 0) {
			throw new Exception("Invalid parameter!!");
		}
		
		//get the id of person identity
		int person_id = person.person_id;
		
		//add reference in db with person id using person name
		PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("insert into person_reference(PersonId,refer) values(?,?)");
	    prepStatement.setInt(1, person_id);
	    prepStatement.setString(2, reference);
	    int response = prepStatement.executeUpdate();
	    
	 	prepStatement.close();
	    SqlConnection.closeConnection(SqlConnection.getConnection());
	    
	    if(response==0) {
	    	System.out.println("Error in DB operation !!");
	    	return false;
	    }else {
	    	return true;
	    }
	}
	

	
	/**
	 * Add person Notes
	 * @param person
	 * @param note
	 * @return
	 * @throws Exception 
	 */
	Boolean recordNote( PersonIdentity person, String note ) throws Exception {
		//could have multiple notes
		
		if(person == null || note == null || note.trim().length() == 0) {
			throw new Exception("Invalid parameter!!");
		}
		
		//get the person id
		int person_id = person.person_id;
		
		//add notes in db with person id usinf person name
		PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("insert into person_note(PersonId,Note) values(?,?)");
	    prepStatement.setInt(1, person_id);
	    prepStatement.setString(2, note);
	    int response = prepStatement.executeUpdate();
	    
	 	prepStatement.close();
	    SqlConnection.closeConnection(SqlConnection.getConnection());
	    
	    if(response==0) {
	    	System.out.println("Error in DB operation");
	    	return false;
	    }else {
	    	return true;
	    }
	}
	
	
	
	/**
	 * Add media attributes
	 * @param fileIdentifier
	 * @param attributes
	 * @return
	 * @throws Exception
	 */
	Boolean recordMediaAttributes( FileIdentifier fileIdentifier, Map<String, String> attributes ) throws Exception {
		
		if(fileIdentifier == null ) {
			throw new Exception("Invalid parameter!!");
		}
		
		//file id 
		int fileId = fileIdentifier.fileId;
		
		
		//validation empty string
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			//empty value of key is not allowed
			if(entry.getKey().trim().length()==0) {
				return false;
			}
		}
		
		//store the attributes in db
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			
			
			//date attributes 
			if(key.equalsIgnoreCase("date")) {
				
				//validate the date 
				if(!DateValidation.inputDateValidation(value)) {
					//invalid format 
					throw new Exception("Invalid date format!!");
				}
				
				PreparedStatement prepStatement_date = SqlConnection.getConnection().prepareStatement("select * from files where fileId=?");
				prepStatement_date.setInt(1,fileId);
				
				ResultSet response_date = prepStatement_date.executeQuery();
				if(response_date.next()) {
					//date is exist for file id
					PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("UPDATE files SET date=? WHERE fileId=?");
					prepStatement.setString(1,value);
					prepStatement.setInt(2,fileId);
					prepStatement.execute();
				}
			} else if(key.equalsIgnoreCase("location")) {				
				
				PreparedStatement prepStatement_location = SqlConnection.getConnection().prepareStatement("select * from files where fileId=?");
				prepStatement_location.setInt(1,fileId);
				
				ResultSet response_date = prepStatement_location.executeQuery();
				
				if(response_date.next()) {
				//location is exist for file id
				PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("UPDATE files SET location=? WHERE fileId=?");
				prepStatement.setString(1,value);
				prepStatement.setInt(2,fileId);
				prepStatement.execute();
			}
		} else {			
			//update if possible
			PreparedStatement prepStatement1 = SqlConnection.getConnection().prepareStatement("select * from file_attributes where keyOfAttribute=? and valueOfAttribute=? and  fileId =? ");
			prepStatement1.setString(1,key);
			prepStatement1.setString(2,value);
			prepStatement1.setInt(3,fileId);
			
			ResultSet response1 = prepStatement1.executeQuery();
			
			if(response1.next()) {
				//update the data 
				PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("UPDATE file_attributes SET valueOfAttribute =? WHERE fileId =? and keyOfAttribute=?");
				prepStatement.setString(1,value);
				prepStatement.setInt(2,fileId);
				prepStatement.setString(3,key);
				prepStatement.execute();
			}else {
				//add new data
				PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("INSERT INTO file_attributes VALUES (?, ?, ?)");
				prepStatement.setInt(1,fileId);
				prepStatement.setString(2,key);
				prepStatement.setString(3,value);
				int res  = prepStatement.executeUpdate();
				
				if(res==0) {
					return false;
				}
			}
		}
			
			
		}
		
		
		
		return true;
	}
	

	
	/**
	 * Add people in media
	 * @param fileIdentifier
	 * @param people
	 * @return
	 * @throws Exception
	 */
	Boolean peopleInMedia( FileIdentifier fileIdentifier, List<PersonIdentity> people ) throws Exception {
		//add people in media using file_id
		
		if(fileIdentifier == null ) {
			throw new Exception("Invalid parameter!!");
		}
		
		//file id
		int fileId  = fileIdentifier.fileId;
				
		//loop for people list and link each person to file
		for (int i = 0; i < people.size(); i++) {
			
			//check duplicate data
			PreparedStatement prepStatement1 = SqlConnection.getConnection().prepareStatement("SELECT * FROM filewithperson where fileId=? and PersonId=?;");
			prepStatement1.setInt(1,fileId);
			prepStatement1.setInt(2,people.get(i).person_id);
		    ResultSet response = prepStatement1.executeQuery();
		    
		    if(response.next()) {
		    	
		    }else {
		    	//add data 
	    	PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("insert into filewithperson(fileId,PersonId) values(?,?)");
	    	prepStatement.setInt(1,fileId);
	    	prepStatement.setInt(2,people.get(i).person_id);
	    	int response2 = prepStatement.executeUpdate();
	    	
	    	if(response2 == 0) {
	    		return false;
	    	}		    	
		  }			
		}
			
		return true;
	}
	
	
	
	/**
	 * Add tag in media
	 * @param fileIdentifier
	 * @param tag
	 * @return
	 * @throws Exception 
	 */
	Boolean tagMedia( FileIdentifier fileIdentifier, String tag ) throws Exception {
		//add tags in files using file_id
		
		if(fileIdentifier == null || tag.trim().length() == 0) {
			throw new Exception("Invalid parameter!!");
		}
		
		//get the id of file identity
	    int fileId = fileIdentifier.fileId;
	    
 	    
     	//check duplicate data
		PreparedStatement prepStatement1 = SqlConnection.getConnection().prepareStatement("SELECT * FROM tags where fileId=? and tag=? ;");
		prepStatement1.setInt(1,fileId);
		prepStatement1.setString(2,tag);
	    ResultSet response1 = prepStatement1.executeQuery();
	    if(response1.next()) {
	    	//tag is already exist in DB
	    	return false;
	    }else {
	    	//add tag in db with file id 
	    	PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("insert into tags(fileId,tag) values(?,?)");
	    	prepStatement.setInt(1, fileId);
	    	prepStatement.setString(2, tag);
	    	int response = prepStatement.executeUpdate();
	    	
		 	prepStatement.close();
		    SqlConnection.closeConnection(SqlConnection.getConnection());
	    	
		    if(response==0) {
	    		System.out.println("Error in DB operation of adding tag for file");
	    		return false;
	    	}else {
	    		return true;
	    	}	    	
	    }
	    
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		try {
		//Genealogy 
			//1. recordAttributes
			//2. recordReference
			//3. recordNote
			//4. recordMediaAttributes
			//5. peopleInMedia
			//6. tagMedia
		
		//PersonIdentity
			//1. addPerson
			//2. findPerson
			//3. findName
			//4. notesAndReferences
		
		//FileIdentify
			//1. addMediaFile
			//2. findMediaFile
			//3. findMediaFile
			//4. findMediaByTag
			//5. findMediaByLocation
			//6. findIndividualsMedia
			//7. findBiologicalFamilyMedia
		
		//BiologicalRelation
			//1. findRelation
			//2. descendents
			//3. ancestores
			//4. recordChild
			//5. recordPartnering
			//6. recordDissolution
			
		
		
		
			
		} catch (Exception e) {
			System.out.println("Error has been occured"+e);
		}	


	}

}
