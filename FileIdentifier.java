import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * @author Parth gondaliya
 *
 */
public class FileIdentifier {
	int fileId = 0;
	String name="";
	
	public FileIdentifier(int fileId) {
		this.fileId= fileId;
	}


	public FileIdentifier() {
	}


	
	/**
	 * Add media file 
	 * @param fileLocation
	 * @return
	 * @throws Exception
	 */
	
	FileIdentifier addMediaFile( String fileLocation ) throws Exception {
		
		if(fileLocation == null || fileLocation.trim().length() == 0 ) {
			throw new Exception("Invalid parameter!!");
		}
		
		//validatin of duplicate file name 
		PreparedStatement prepStatement1 = SqlConnection.getConnection().prepareStatement("SELECT * FROM files where fileLocation=? ;");
		prepStatement1.setString(1,fileLocation);

	    ResultSet response1 = prepStatement1.executeQuery();

	    if(response1.next()) {
	    	//record is present
	    	throw new Exception("File is already added in DB!");
	    }
		
		//add new file data into db 
		PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("insert into files(fileLocation) values(?)", Statement.RETURN_GENERATED_KEYS);
	    prepStatement.setString(1, fileLocation);	
	    int response = prepStatement.executeUpdate();
	    
	    if(response == 0) {
	    	throw new Exception("Error in add file location in DB!!");
        }
	    
	    //get the id and store it in class variable 
	    ResultSet key = prepStatement.getGeneratedKeys();
	    if(key.next()) {
	    	fileId = key.getInt(1);
	    }
	    
	    
	    prepStatement.close();
	    SqlConnection.closeConnection(SqlConnection.getConnection());
	    
	    //return the File object	
		return new FileIdentifier(fileId);		
	}
	
	
	
	/**
	 * find media file with name
	 * @param name
	 * @return
	 * @throws Exception
	 */
	FileIdentifier findMediaFile( String name ) throws Exception {
		
		
		if(name == null || name.trim().length() == 0) {
			throw new Exception("Invalid file name !!");
		}
		
		String query = "select * from files where fileLocation='"+name+"';";
		Statement statement = SqlConnection.getConnection().createStatement();
		ResultSet response = statement.executeQuery(query);
		
		int count =0;
		int fileId = 0;
	
		while(response.next()) {
			count = count +1;
			fileId= response.getInt("fileId");
		}
		
		
		if(count>1) {
			throw new Exception("More than one file exist !!");
		}
		
		FileIdentifier f = new FileIdentifier();
		f.fileId=fileId;
		
	    SqlConnection.closeConnection(SqlConnection.getConnection());
		
		return f;
		
	}
	
	
	/**
	 * find file name with file object 
	 * @param fileId
	 * @return
	 * @throws Exception
	 */
	String findMediaFile( FileIdentifier fileId ) throws Exception {
		
		if(fileId == null) {
			throw new Exception("Invalid data !!");
		}
		
		String query = "select * from files where fileId='"+fileId.fileId+"';";
		Statement statement = SqlConnection.getConnection().createStatement();
		ResultSet response = statement.executeQuery(query);
		String name = null;
		
		while(response.next()) {
			name = response.getString("fileLocation");
		}
		

	    SqlConnection.closeConnection(SqlConnection.getConnection());

	    return name;
	}
	

	/**
	 * Report media by tag
	 * @param tag
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	Set<FileIdentifier> findMediaByTag( String tag , String startDate, String endDate) throws Exception{
		
		if(tag == null || tag.trim().length() == 0) {
			throw new Exception("Invalid data!!");
		}
		
		if(startDate == null && endDate == null ) {
			
			//list for object of files
			HashSet<FileIdentifier> list = new HashSet<FileIdentifier>();
			
			//no limit on records
			String query = "SELECT f.fileId,tag FROM tags\r\n"
					+ "INNER JOIN files as f\r\n"
					+ "on tags.fileId=f.fileId\r\n"
					+ "group by f.fileId,tag\r\n"
					+ "having tag='"+ tag +"'";
			
			
			
			Statement statement = SqlConnection.getConnection().createStatement();
			ResultSet response = statement.executeQuery(query);
			while(response.next()) {
				list.add(new FileIdentifier(response.getInt("fileId")));
			}
			
			return list;
			
		}else if(startDate == null) {
		
			//verify the end date format 
			if(!DateValidation.dateValidate(endDate)) {
				//invalid format 
				throw new Exception("Invalid format!!");
			}
			

			//list for object of files
			HashSet<FileIdentifier> list = new HashSet<FileIdentifier>();
			
			
			String query = "SELECT f.fileId,tag,date as d FROM tags\r\n"
					+ "right JOIN files as f\r\n"
					+ "on tags.fileId=f.fileId and tag is not null and f.date is not null \r\n"
					+ "group by f.fileId,tag\r\n"
					+ "having tag='"+tag+"' and ((DATE(STR_TO_DATE(date,'%Y-%m-%d')) <= DATE(STR_TO_DATE('"+endDate+"','%Y-%m-%d'))));\r\n"
					+ "";
			
			
			Statement statement = SqlConnection.getConnection().createStatement();
			ResultSet response = statement.executeQuery(query);
			
			while(response.next()) {
				list.add(new FileIdentifier(response.getInt("fileId")));
			}
			
			return list;
					
		}else if(endDate == null) {
			//verify the format of start date
			if(!DateValidation.dateValidate(startDate)) {
				//invalid format 
				throw new Exception("Invalid format!!");
			}
			
			//list for object of files
			HashSet<FileIdentifier> list = new HashSet<FileIdentifier>();
			
			String query = "SELECT f.fileId,tag,date as d FROM tags\r\n"
					+ "right JOIN files as f\r\n"
					+ "on tags.fileId=f.fileId and tag is not null and f.date is not null\r\n"
					+ "group by f.fileId,tag\r\n"
					+ "having tag='"+tag+"' and ((DATE(STR_TO_DATE(date,'%Y-%m-%d')) >= DATE(STR_TO_DATE('"+ startDate+"','%Y-%m-%d'))));\r\n";
			
			Statement statement = SqlConnection.getConnection().createStatement();
			ResultSet response = statement.executeQuery(query);
			
			
			while(response.next()) {
				list.add(new FileIdentifier(response.getInt("fileId")));
			}
			
			return list;
			
		}else if(startDate != null && endDate != null) {
			//verify the start date 
			if(!DateValidation.dateValidate(startDate) || !DateValidation.dateValidate(endDate)) {
				//invalid format 
				throw new Exception("Invalid format!!");
			}
			
			//list for object of files
			HashSet<FileIdentifier> list = new HashSet<FileIdentifier>();
	
			//verify the end date
			String query = "SELECT f.fileId,tag,date as d FROM tags\r\n"
					+ "INNER JOIN files as f\r\n"
					+ "on tags.fileId=f.fileId and f.date is not null \r\n"
					+ "group by f.fileId,tag\r\n"
					+ "having tag='"+tag+"' and ((DATE(STR_TO_DATE(date,'%Y-%m-%d')) between DATE(STR_TO_DATE('"+startDate+"','%Y-%m-%d')) and  DATE(STR_TO_DATE('"+endDate+"','%Y-%m-%d'))));\r\n"
					+ "";
			
			Statement statement = SqlConnection.getConnection().createStatement();
			ResultSet response = statement.executeQuery(query);
			
			
			while(response.next()) {
				list.add(new FileIdentifier(response.getInt("fileId")));
			}
			
			return list;
		}
		
		
		
		
		return null;
		
	}
	

	/**
	 * Report media by location
	 * @param location
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	Set<FileIdentifier> findMediaByLocation( String location, String startDate, String endDate) throws Exception{

		if(location == null || location.trim().length() == 0) {
			throw new Exception("Invalid data!!");
		}
		
		if(startDate == null && endDate == null ) {
			
			//list for object of files
			HashSet<FileIdentifier> list = new HashSet<FileIdentifier>();
			
			//no limit on records
			String query = "SELECT * FROM files where location='"+location+"';";		
			
			
			Statement statement = SqlConnection.getConnection().createStatement();
			ResultSet response = statement.executeQuery(query);
			while(response.next()) {
				list.add(new FileIdentifier(response.getInt("fileId")));
			}
			
			return list;
			
		}else if(startDate == null) {
		
			//verify the end date format 
			if(!DateValidation.dateValidate(endDate)) {
				//invalid format 
				throw new Exception("Invalid format!!");
			}
			

			//list for object of files
			HashSet<FileIdentifier> list = new HashSet<FileIdentifier>();
			
			
			String query = "SELECT * FROM files where location='"+location+"' and ((DATE(STR_TO_DATE(date,'%Y-%m-%d')) <= DATE(STR_TO_DATE('"+endDate+"','%Y-%m-%d')))) and date is not null;";
			
			
			Statement statement = SqlConnection.getConnection().createStatement();
			ResultSet response = statement.executeQuery(query);
			
			while(response.next()) {
				list.add(new FileIdentifier(response.getInt("fileId")));
			}
			
			return list;
					
		}else if(endDate == null) {
			//verify the format of start date
			if(!DateValidation.dateValidate(startDate)) {
				//invalid format 
				throw new Exception("Invalid format!!");
			}
			
			//list for object of files
			HashSet<FileIdentifier> list = new HashSet<FileIdentifier>();
			
			String query = "SELECT * FROM files where location='"+location+"' and ((DATE(STR_TO_DATE(date,'%Y-%m-%d')) >= DATE(STR_TO_DATE('"+startDate+"','%Y-%m-%d')))) and date is not null;";
			
			Statement statement = SqlConnection.getConnection().createStatement();
			ResultSet response = statement.executeQuery(query);
			
			
			while(response.next()) {
				list.add(new FileIdentifier(response.getInt("fileId")));
			}
			
			return list;
			
		}else if(startDate != null && endDate != null) {
			//verify the start date 
			if(!DateValidation.dateValidate(startDate) || !DateValidation.dateValidate(endDate)) {
				//invalid format 
				throw new Exception("Invalid format!!");
			}
			
			//list for object of files
			HashSet<FileIdentifier> list = new HashSet<FileIdentifier>();
			
			
			
			//verify the end date
			String query = "SELECT * FROM files \r\n"
					+ "where location='"+location+"'\r\n"
					+ "and  ((DATE(STR_TO_DATE(date,'%Y-%m-%d')) between DATE(STR_TO_DATE('"+startDate+"','%Y-%m-%d')) and  DATE(STR_TO_DATE('"+endDate+"','%Y-%m-%d')))) and date is not null";
			
			Statement statement = SqlConnection.getConnection().createStatement();
			ResultSet response = statement.executeQuery(query);
			
			
			while(response.next()) {
				list.add(new FileIdentifier(response.getInt("fileId")));
			}
			
			return list;
		}
		
		
		
		
		return null;
		
	
	}
	
	
	/**
	 * Report media by set of people
	 * @param people
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	List<FileIdentifier> findIndividualsMedia( Set<PersonIdentity> people, String startDate, String endDate) throws Exception{
		

		String subQuery = "";
		
		//form the sub-query
		Iterator<PersonIdentity> iterator = people.iterator();
		
		while(iterator.hasNext()) {
			subQuery = subQuery + "PersonId=" + iterator.next().person_id+" or ";
		}
		
		subQuery = subQuery + "null";
		
		//list for object of files
		List<FileIdentifier> list = new ArrayList<FileIdentifier>();
		
		
		
		if(startDate == null && endDate == null) {
			
			//no limit on records
			String query = "SELECT * FROM filewithperson \r\n"
					+ "join files as f\r\n"
					+ "on f.fileId = filewithperson.fileId and f.date is not null\r\n"
					+ "group by f.fileId\r\n"
					+ "having "+subQuery+"\r\n"
					+ "order by f.date,f.fileLocation asc;";
			
			
			Statement statement = SqlConnection.getConnection().createStatement();
			ResultSet response = statement.executeQuery(query);
			
			while(response.next()) {
				list.add(new FileIdentifier(response.getInt("fileId")));
			}
			
		}else if(startDate == null ) {
			
			//date validation
			if(!DateValidation.dateValidate(endDate)) {
				//invalid format 
				throw new Exception("Invalid format!!");
			}
		
			String query = "SELECT * FROM filewithperson as fp\r\n"
					+ "join files as f\r\n"
					+ "on fp.fileId = f.fileId and (f.date is not null) and ("+subQuery+") \r\n"
					+ "group by fp.fileId\r\n"
					+ "having (((DATE(STR_TO_DATE(f.date,'%Y-%m-%d')) <= DATE(STR_TO_DATE('"+endDate+"','%Y-%m-%d')))))\r\n"
					+ "order by f.date,f.fileLocation asc;";
			
			
			Statement statement = SqlConnection.getConnection().createStatement();
			ResultSet response = statement.executeQuery(query);
			while(response.next()) {
				list.add(new FileIdentifier(response.getInt("fileId")));
			}
			
			
			
		}else if(endDate == null) {
			
			//date validation
			if(!DateValidation.dateValidate(startDate)) {
				//invalid format 
				throw new Exception("Invalid format!!");
			}
			
			String query = "SELECT * FROM filewithperson as fp\r\n"
					+ "join files as f\r\n"
					+ "on fp.fileId = f.fileId and (f.date is not null) and ("+ subQuery +")\r\n"
					+ "group by fp.fileId\r\n"
					+ "having (((DATE(STR_TO_DATE(f.date,'%Y-%m-%d')) >= DATE(STR_TO_DATE('"+startDate+"','%Y-%m-%d')))))\r\n"
					+ "order by f.date,f.fileLocation asc;";
			
			
			Statement statement = SqlConnection.getConnection().createStatement();
			ResultSet response = statement.executeQuery(query);
			while(response.next()) {
				list.add(new FileIdentifier(response.getInt("fileId")));
			}
			

		}else if(startDate != null && endDate != null){
			
			
			//date validation
			if(!DateValidation.dateValidate(endDate) || !DateValidation.dateValidate(startDate)) {
				//invalid format 
				throw new Exception("Invalid format!!");
			}
			
			String query = "SELECT * FROM filewithperson as fp\r\n"
					+ "join files as f\r\n"
					+ "on fp.fileId = f.fileId and (f.date is not null) and  ("+ subQuery +")\r\n"
					+ "group by fp.fileId\r\n"
					+ "having  ((DATE(STR_TO_DATE(f.date,'%Y-%m-%d')) between DATE(STR_TO_DATE('"+startDate+"','%Y-%m-%d')) and  DATE(STR_TO_DATE('"+endDate+"','%Y-%m-%d'))))\r\n"
					+ "order by f.date,f.fileLocation asc;";
			
			Statement statement = SqlConnection.getConnection().createStatement();
			ResultSet response = statement.executeQuery(query);
			while(response.next()) {
				list.add(new FileIdentifier(response.getInt("fileId")));
			}
			
		}
		

	    SqlConnection.closeConnection(SqlConnection.getConnection());
		return list;
		
	}
	

	/**
	 * Report media by person's immediate children
	 * @param person
	 * @return
	 * @throws Exception 
	 */
	List<FileIdentifier> findBiologicalFamilyMedia(PersonIdentity person) throws Exception{
		
		if(person == null) {
			throw new Exception("Invalid data!!");
		}
		
		//person id 
		int personId = person.person_id;
		
		//list of fileIdetifier
		List<FileIdentifier> list = new ArrayList<FileIdentifier>(); 
		
		String query = "SELECT fp.fileId as fId, f.date as date,f.fileLocation as name FROM parent as p\r\n"
				+ "join filewithperson as fp\r\n"
				+ "on p.ChildId =  fp.PersonId and p.ParentId="+personId+"\r\n"
				+ "join files as f\r\n"
				+ "on f.fileId = fp.fileId and f.date is not null\r\n"
				+ "group by f.fileId\r\n"
				+ "order by f.date,f.fileLocation asc;";
		
		Statement statement = SqlConnection.getConnection().createStatement();
		ResultSet response = statement.executeQuery(query);
		
		
		while(response.next()) {
			list.add(new FileIdentifier(response.getInt("fId")));
		}
		
	    SqlConnection.closeConnection(SqlConnection.getConnection());
		return list;
	}
	
}
