package scripts.JarGenerator;
	import java.util.List;


	public class Utils {
		
		/**
		 * Method to calculate average wait time from a List of times
		 * @param times 
		 * @return
		 */
		public static int calculateAverage(List<Integer> times) {
			Integer sum = 0;
			if (!times.isEmpty()) {
				for (Integer holder : times) {
					sum += holder;
				}
				return sum.intValue() / times.size();
			}
			return sum;
		}
	}
