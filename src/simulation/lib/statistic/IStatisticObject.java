package simulation.lib.statistic;
/**
 * Interface for statistic objects such as Counters and Histograms.
 */
public interface IStatisticObject {
	/**
	 * Count sample.
	 * @param x counter value
	 */
	public void count(double x);
	/**
	 * Report to console.
	 * @return
	 */
	public String report();
	
	/**
	 * Report to csv-file.
	 * @param outputdir filepath
	 */
	public void csvReport(String outputdir);
}
