import java.util.*;
import java.sql.*;


public class PersonIdentity {
	
	
 	 String name = "";
 	 int person_id= 0;

 	 //default constructor
 	 public PersonIdentity() {
 	 }
 	
 	 //constructor with personid
 	 public PersonIdentity(int personId) {
 		 this.person_id = personId;
 	 }
 	 
 	 public PersonIdentity(int personId,String name) {
 		 this.person_id = personId;
 		 this.name = name;
 	 }
 	 



 
	/**
	 * Add Person
	 * @param name
	 * @return
	 * @throws Exception
	 */
	PersonIdentity addPerson( String name ) throws Exception {
		
		//validation
		if(name == null || name.trim().length() == 0) {
			//invalid name
			return null;
		}
		
		//add person name in DB
		PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("insert into person(name) values(?)", Statement.RETURN_GENERATED_KEYS);
	    prepStatement.setString(1, name);	
	    int response = prepStatement.executeUpdate();
	  
	    if(response == 0) {
	    	throw new Exception("Error in add person in DB");
        }
	    
	    //get the id and store it in class variable 
	    ResultSet key = prepStatement.getGeneratedKeys();
	    if(key.next()) {
	    	person_id = key.getInt(1);
	    }
	    
	 	prepStatement.close();
	    SqlConnection.closeConnection(SqlConnection.getConnection());
	    
	    //return the personIdentity object	
		return new PersonIdentity(person_id,name);		
				
	 }
	

	
	/**
	 * Find person with name
	 * @param name
	 * @return
	 * @throws Exception
	 */
	PersonIdentity findPerson( String name ) throws Exception {

		if(name == null || name.trim().length() == 0) {
			throw new Exception("Invalid data!!");
		}
		//get data from db using person name
		String query = "select * from person where name='"+name+"';";
		Statement statement = SqlConnection.getConnection().createStatement();
		ResultSet response = statement.executeQuery(query);
		
		int count = 0;
		int personId = 0;
		
		while(response.next()) {
			count = count + 1;
			personId=response.getInt("PersonId");
		}
		
		if(count > 1) {
			//multiple value has been found
			throw new Exception("multiple name has been found");
		}
		
	    SqlConnection.closeConnection(SqlConnection.getConnection());
		
		
		return new PersonIdentity(personId);
	}
	

	
	/**
	 * Find name with person object
	 * @param id
	 * @return
	 * @throws Exception 
	 */
	String findName( PersonIdentity id ) throws Exception {
		
		if(id == null ) {
			throw new Exception("Invalid parameter!!");
		}
		
		//get person name using person id
		String query = "select * from person where PersonId='"+id.person_id+"';";
		Statement statement = SqlConnection.getConnection().createStatement();
		ResultSet response = statement.executeQuery(query);
		String name = null;
		while(response.next()) {
			name = response.getString("name");
		}
		
	 	
	    SqlConnection.closeConnection(SqlConnection.getConnection());
		
	    return name;
	}
	

	
	/**
	 * Report notes and reference
	 * @param person
	 * @return
	 * @throws Exception 
	 */
	List<String> notesAndReferences( PersonIdentity person ) throws Exception{
		
		if(person == null ) {
			throw new Exception("Invalid parameter!!");
		}
		
		//get all notes and reference using person id 
		int personId = person.person_id;
		
		List<String> list = new ArrayList<String>(); 
		
		//reference of person
		String query1 = "select * from person_reference where PersonId='"+person.person_id+"';";
		Statement statement1 = SqlConnection.getConnection().createStatement();
		ResultSet response1 = statement1.executeQuery(query1);
		
		while(response1.next()) {
			list.add(response1.getString("refer"));
		}
		
		//note of person 
		String query2 = "select * from person_note where PersonId='"+person.person_id+"';";
		Statement statement2 = SqlConnection.getConnection().createStatement();
		ResultSet response2 = statement1.executeQuery(query2);
		
		while(response2 .next()) {
			list.add(response2.getString("Note"));
		}
		

	    SqlConnection.closeConnection(SqlConnection.getConnection());

	    return list;
	}
		
}
