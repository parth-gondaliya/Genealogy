import java.util.List;

/**
 * @author Parth Gondaliya
 *
 */

public class LogicalGraph {
	int total_degree;
	List<String> l1 ;
	List<String> l2 ;
	int gen_of_x ;
	int gen_of_y ;
	int cousinship;
	int level_of_sepration;
	
	//----------------------------------------------------------------------------------
	
	/**
	 * get total degree
	 * @return
	 */
	public int getTotalDegree() {
         return this.total_degree;
    }
	

	/**
	 * get cousinship
	 * @return
	 */
	public int cousinShip() {
        return this.cousinship;
    }
	

	/**
	 * get level sepration
	 * @return
	 */
	public int getLevelOfSepration() {
        return this.level_of_sepration;
    }

}
