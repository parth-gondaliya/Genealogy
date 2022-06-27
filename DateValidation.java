import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateValidation {
	
	/**
	 * 
	 * date validation in function (YYYY-MM-DD)
	 * @param date
	 * @return
	 */
	public static boolean dateValidate(String date) {
		//date validation 
				//YYYY-DD-MM
				//compile regex
				final Pattern p1 = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
				
				//match regex
				final Matcher m1 = p1.matcher(date);
				
				boolean found1 = m1.find();
				
				if(found1) {
					//check the valid date 
					String str1[] = date.split("-");
					List<String> a1 = new ArrayList<String>();
					a1 = Arrays.asList(str1);
					
					//check the month (<12)
					int month = Integer.parseInt(a1.get(1));
					
					if(month > 12) {
						return false;
					}
					//check the day (<31)
					int day = Integer.parseInt(a1.get(2));
					
					if(day > 31) {
						return false;
					}
				}
			
				
				if(!found1) {
					//invalid format
					return false;							
				}
				
		return true;
	}
	

	/**
	 * input date validation
	 * @param date
	 * @return
	 */
	public static boolean inputDateValidation(String date) {
		//date validation 
		
		
		//YYYY-DD-MM
		//compile regex
		final Pattern p1 = Pattern.compile("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
		
		//match regex
		final Matcher m1 = p1.matcher(date);
		
		boolean found1 = m1.find();
		
		
		if(found1) {
			//check the valid date 
			String str1[] = date.split("-");
			List<String> a1 = new ArrayList<String>();
			a1 = Arrays.asList(str1);
			
			//check the month (<12)
			int month = Integer.parseInt(a1.get(1));
			if(month > 12) {
				return false;
			}
			
			//check the day (<31)
			int day = Integer.parseInt(a1.get(2));
			if(day > 31) {
				return false;
			}
		}
		
		//YYYY-MM
		//compile regex
		final Pattern p2 = Pattern.compile("^[0-9]{4}-[0-9]{2}$");
		
		//match regex
		final Matcher m2 = p2.matcher(date);
		
		boolean found2 = m2.find();
			
		if(found2) {
			//check the valid date 
			String str1[] = date.split("-");
			List<String> a1 = new ArrayList<String>();
			a1 = Arrays.asList(str1);
			
			//check the month (<12)
			int month = Integer.parseInt(a1.get(1));
			if(month > 12) {
				return false;
			}
		
		}
		
	
		//YYYY
		final Pattern p3 = Pattern.compile("^[0-4]{4}$");
		
		//match regex
		final Matcher m3 = p3.matcher(date);
		
		boolean found3 = m3.find();
		
		
		if(!found1 && !found2 && !found3) {
			//invalid format
			return false;							
		}
		
		return true;
		
	}
	
	
	
}
