import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class BiologicalRelation {
	
	int cousinship=0;
	int removal=0;
	
	
	//getter method of cousinship
	int getCousinship() {
		return this.cousinship;
	}
	
	//getter method of removal
	int getRemoval() {
		return this.removal;
	}
	
	//setter method of cousinship
	void setCousinship(int cousinship) {
		this.cousinship = cousinship;
	}
	
	//setter method of removal 
	void setRemoval(int removal) {
		this.removal = removal;
	}
	
	

	
	/**
	 * Find the relation between individuals 
	 * @param person1
	 * @param person2
	 * @return
	 * @throws Exception 
	 */
	BiologicalRelation findRelation( PersonIdentity person1, PersonIdentity person2 ) throws Exception {
		
		if(person1 == null || person2 == null ) {
			throw new Exception("Invalid parameter!!");
		}
		
		//get the id of person 
		int person1Id = person1.person_id;
		int person2Id = person2.person_id;
		
		//array list for all path from root 
		ArrayList<String> p1 = new ArrayList<>();
		ArrayList<String> p2 = new ArrayList<>();
		
		//array list of possible LCA
		ArrayList<LogicalGraph> lca_list = new ArrayList<>();
		
		//final variable 
		int lowest_degree = 0;
		int cousinShip = 0;
		int levelOfSepation = 0;
		
		//get the path from their tree 
		String query = "WITH recursive person1 (PersonId, name, ParentId) AS (\r\n"
				+ "	select PersonId,name,p.ParentId from person \r\n"
				+ "	left join parent as p \r\n"
				+ "	on person.personId = p.ChildId\r\n"
				+ "),\r\n"
				+ "category_path (PersonId, name, path) AS\r\n"
				+ "(\r\n"
				+ "  SELECT PersonId, name , CONCAT(PersonId) AS path\r\n"
				+ "    FROM person1\r\n"
				+ "    WHERE ParentId IS NULL\r\n"
				+ "  UNION ALL\r\n"
				+ "  SELECT c.PersonId, c.name,  CONCAT(c.PersonId, '-', cp.path)\r\n"
				+ "    FROM category_path AS cp JOIN person1 AS c\r\n"
				+ "      ON cp.PersonId = c.ParentId\r\n"
				+ ")\r\n"
				+ "SELECT * FROM category_path\r\n"
				+ "where  personId="+person1Id+" OR personId= "+person2Id+"\r\n"
				+ "ORDER BY path;\r\n";
		
		
		Statement statement = SqlConnection.getConnection().createStatement();
		ResultSet response = statement.executeQuery(query);
		
		while(response.next()) {
			int personId = response.getInt("PersonId");
			String path = response.getString("path");
			
			if(personId == person1Id) {
				//store it in arrylist 1
				p1.add(path);			
			}else {
				//store it in arraylist 2
				p2.add(path);
			}
		}
	
		
		//start comparing and manipulating the path for final solution
		for (int i = 0; i < p1.size(); i++) {
			for (int j = 0; j < p2.size(); j++) {
				String string1 = p1.get(i);
				String string2 = p2.get(j);

				String str1[] = string1.split("-");
				String str2[] = string2.split("-");

				List<String> a1 = new ArrayList<String>();
				List<String> a2 = new ArrayList<String>();
				
				a1 = Arrays.asList(str1);
				a2 = Arrays.asList(str2);
				
				
				
				for (int k = 0; k < a1.size(); k++) {
					for (int k2 = 0; k2 < a2.size(); k2++) {
						
						if(a1.get(k).equalsIgnoreCase(a2.get(k2))) {
							int total_degree = k + k2;
							List<String> l1 = a1;
							List<String> l2 = a2;
							int gen_of_x = k;
							int gen_of_y = k2;
							int cousinship = Math.min(gen_of_x, gen_of_y) - 1;
							int level_of_sepration =  Math.abs(gen_of_x - gen_of_y);
							
							//store all component in logicalgraph
							LogicalGraph lg = new LogicalGraph();
							lg.total_degree=total_degree;
							lg.l1=l1;
							lg.l2=l2;
							lg.gen_of_x=gen_of_x;
							lg.gen_of_y=gen_of_y;
							lg.cousinship=cousinship;
							lg.level_of_sepration=level_of_sepration;
							
							//store object in arraylist
							lca_list.add(lg);
							
							
							break;
						}
					}
				}
			}
		}
		
		//find lca with lowest degree
		LogicalGraph result =  Collections.min(lca_list, Comparator.comparing(s -> s.getTotalDegree()));
		
		//set cousinship 
		BiologicalRelation b = new BiologicalRelation();
		b.setCousinship(result.cousinship);
		b.setRemoval(result.level_of_sepration);
	 	
	    SqlConnection.closeConnection(SqlConnection.getConnection());
		
	    return b;
	}
	
	
	/**
	 * Report all descendents 
	 * @param person
	 * @param generations
	 * @return
	 * @throws Exception 
	 */
	Set<PersonIdentity> descendents( PersonIdentity person, Integer generations ) throws Exception{
		
		if(person == null ) {
			throw new Exception("Invalid parameter!!");
		}
		
		//get person id and generation 
		int person_id = person.person_id;
		int gen = generations;
		
		//list for all decendants 
		Set<PersonIdentity> descendents = new HashSet<>();
		
		//query 
		String query ="WITH RECURSIVE descendant AS (\r\n"
				+ "    SELECT  ParentId,\r\n"
				+ "            ChildId,\r\n"
				+ "            0 as level\r\n"
				+ "    FROM parent\r\n"
				+ "    WHERE ParentId = "+person_id+"\r\n"
				+ " \r\n"
				+ "    UNION ALL\r\n"
				+ " \r\n"
				+ "    SELECT  p.ChildId,\r\n"
				+ "            p.parentId,\r\n"
				+ "            level + 1\r\n"
				+ "    FROM parent p\r\n"
				+ "JOIN descendant d\r\n"
				+ "ON p.ParentId = d.ParentId\r\n"
				+ ")\r\n"
				+ " \r\n"
				+ "SELECT  d.ChildId AS descendant_id,\r\n"
				+ "        p1.ChildId AS ancestor_id,\r\n"
				+ "        d.level\r\n"
				+ "FROM descendant d\r\n"
				+ "JOIN parent p1\r\n"
				+ "ON d.parentId = p1.ChildId\r\n"
				+ "group by d.ParentId\r\n"
				+ "having level  <=" + gen + "\r\n"
				+ "ORDER BY level;";
		
	
		Statement statement = SqlConnection.getConnection().createStatement();
		ResultSet response = statement.executeQuery(query);
		
		while(response.next()) {
			int personId = response.getInt("ancestor_id");
			String name = "";
			
			//get info about that person 
			String query2 = "select * from person where PersonId="+personId+";";
			Statement statement2 = SqlConnection.getConnection().createStatement();
			ResultSet response2 = statement2.executeQuery(query2);
			while(response2.next()) {
				name = response2.getString("name");
			}
			
			PersonIdentity p = new PersonIdentity(personId);
			p.name=name;
			
			descendents.add(p);
			
		}
		
	    SqlConnection.closeConnection(SqlConnection.getConnection());
        
	    return descendents;
	}
	

	
	/**
	 * Report all ancentores
	 * @param person
	 * @param generations
	 * @return
	 * @throws Exception 
	 */
	Set<PersonIdentity> ancestores( PersonIdentity person, Integer generations ) throws Exception{
		
		if(person == null ) {
			throw new Exception("Invalid parameter!!");
		}
		
		//get person id and generation 
		int person_id = person.person_id;
		int gen = generations;
		
		//list for all ancentores 
		Set<PersonIdentity> ancestores = new HashSet<>();
		
		//query 
		String query ="WITH RECURSIVE ancestor AS (\r\n"
				+ "    SELECT  ChildId,\r\n"
				+ "            parentId,\r\n"
				+ "            1 AS level\r\n"
				+ "    FROM parent\r\n"
				+ "    WHERE ChildId = "+person_id+"\r\n"
				+ " \r\n"
				+ "    UNION ALL\r\n"
				+ " \r\n"
				+ "    SELECT  per.ChildId,\r\n"
				+ "            per.parentId,\r\n"
				+ "            level + 1\r\n"
				+ "    FROM parent per\r\n"
				+ "JOIN ancestor d\r\n"
				+ "ON per.ChildId = d.parentId\r\n"
				+ ")\r\n"
				+ " \r\n"
				+ "SELECT  \r\n"
				+ "        a.parentId AS ancestor_id,\r\n"
				+ "        d.level\r\n"
				+ "FROM ancestor d\r\n"
				+ "JOIN parent a\r\n"
				+ "ON d.ChildId = a.ChildId\r\n"
				+ "where level <="+gen+"\r\n"
				+ "group by  ancestor_id,level";
		
	
		Statement statement = SqlConnection.getConnection().createStatement();
		ResultSet response = statement.executeQuery(query);
		
		while(response.next()) {
			int personId = response.getInt("ancestor_id");
			
			String name = "";
			
			//get info about that person 
			String query2 = "select * from person where PersonId="+personId+";";
			Statement statement2 = SqlConnection.getConnection().createStatement();
			ResultSet response2 = statement2.executeQuery(query2);
			
			while(response2.next()) {
				name = response2.getString("name");
			}
		
			
			PersonIdentity p = new PersonIdentity(personId);
			p.name=name;
			
			ancestores.add(p);
			
		}
		

	    SqlConnection.closeConnection(SqlConnection.getConnection());
		
        return ancestores;
	}
	
	
		
	/**
	 * record parent child relationship
	 * @param parent
	 * @param child
	 * @return
	 * @throws Exception 
	 */
	Boolean recordChild( PersonIdentity parent, PersonIdentity child ) throws Exception {
		
		if(parent == null || child == null ) {
			throw new Exception("Invalid parameter!!");
		}
		
		int parentId = parent.person_id;
		int childId = child.person_id;
		
		//check this relationship is exist
		PreparedStatement prepStatement1 = SqlConnection.getConnection().prepareStatement("SELECT * FROM parent where ParentId=? and ChildId=?;");
		prepStatement1.setInt(1,parentId);
		prepStatement1.setInt(2,childId);
	    
	    ResultSet response1 = prepStatement1.executeQuery();
	    if(response1.next()) {
	    	//record is present
	    	return false;
	    }	
		
		
		//add relation 
		PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("insert into parent(ParentId,ChildId) values(?,?)");
	    prepStatement.setInt(1,parentId);
	    prepStatement.setInt(2,childId);
	    
	    int response = prepStatement.executeUpdate();
	    
	 	prepStatement.close();
	    SqlConnection.closeConnection(SqlConnection.getConnection());
	    
	    if(response == 0) {
	    	return false;
        }else {
        	return true;
        }
	}
	
	
	/**
	 * Add partnering
	 * @param partner1
	 * @param partner2
	 * @return
	 * @throws Exception 
	 */
	Boolean recordPartnering( PersonIdentity partner1, PersonIdentity partner2 ) throws Exception {
		
		if(partner1 == null || partner2 == null ) {
			throw new Exception("Invalid parameter!!");
		}
		
		int partner1_id = partner1.person_id;
		int partner2_id = partner2.person_id;
		
		//validation of duplicate value
		PreparedStatement prepStatement1 = SqlConnection.getConnection().prepareStatement("SELECT * FROM marriage where partner1=? and partner2=?;");
		prepStatement1.setInt(1,partner1_id);
		prepStatement1.setInt(2,partner2_id);
	    
	    ResultSet response1 = prepStatement1.executeQuery();
	    
	    if(response1.next()) {
	    	//record is present
	    	return false;
	    }	
		
		//add relation 
		PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("insert into marriage(partner1,partner2) values(?,?)");
	    prepStatement.setInt(1,partner1_id);
	    prepStatement.setInt(2,partner2_id);
	    
	    int response = prepStatement.executeUpdate();
	    
	    prepStatement.close();
	    SqlConnection.closeConnection(SqlConnection.getConnection());
	    
	    if(response == 0) {
	    	return false;
        }else {
        	return true;
        }
	}
	
	
	
	/**
	 * Add dissolution
	 * @param partner1
	 * @param partner2
	 * @return
	 * @throws Exception 
	 */
	Boolean recordDissolution( PersonIdentity partner1, PersonIdentity partner2 ) throws Exception {
		
		if(partner1 == null || partner2 == null ) {
			throw new Exception("Invalid parameter!!");
		}
		
		int partner1_id = partner1.person_id;
		int partner2_id = partner2.person_id;
		
		//remove partnering
		PreparedStatement prepStatement1 = SqlConnection.getConnection().prepareStatement("DELETE FROM marriage WHERE partner1=? and partner2=?;");
	    prepStatement1.setInt(1,partner1_id);
	    prepStatement1.setInt(2,partner2_id);
	    int response1 = prepStatement1.executeUpdate();
		
		
		//add disolution relation 
		PreparedStatement prepStatement = SqlConnection.getConnection().prepareStatement("insert into divorce(partner1,partner2) values(?,?)");
	    prepStatement.setInt(1,partner1_id);
	    prepStatement.setInt(2,partner2_id);
	    
	    int response = prepStatement.executeUpdate();
	    
	    prepStatement.close();
	    SqlConnection.closeConnection(SqlConnection.getConnection());
	    
	    if(response == 0) {
	    	return false;
        }else {
        	return true;
        }
	}
	
}
