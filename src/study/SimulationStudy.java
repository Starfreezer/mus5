package study;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import simulation.lib.Simulator;
import simulation.lib.counter.*;
import simulation.lib.histogram.ContinuousHistogram;
import simulation.lib.histogram.DiscreteHistogram;
import simulation.lib.randVars.RandVar;
import simulation.lib.randVars.continous.ErlangK;
import simulation.lib.randVars.continous.Exponential;
import simulation.lib.randVars.continous.HyperExponential;
import simulation.lib.rng.StdRNG;
import simulation.lib.statistic.IStatisticObject;

/**
 * Represents a simulation study. Contains diverse counters for statistics and
 * program/simulator parameters. Starts the simulation.
 */
public class SimulationStudy {
	 /*
	 * TODO Problem 5.1 - Configure program arguments here
	 * TODO Problem 5.1.1 - nInit and lBatch
	 * TODO Problem 5.1.3 - Add attributes to configure your E[ST] and E[IAT] for the simulation
	 * Here you can set the different parameters for your simulation
	 * Note: Units are real time units (seconds).
	 * They get converted to simulation time units in setSimulationParameters.
	 */
	protected long cNInit = 100000;
	public double cCvar = 2; //<- configuration Parameter for Cvar[IAT] = {0.5, 1, 2}
	protected long lBatch = 1;
	protected int maxLagAcc = 20;
	protected static boolean RUN_REPEATED_SIM = false;

	// 5.1.3
	protected final double cMeanST = 1.0; // E[ST]
	public double cSystemUtilization = 0.95; // p, can be set to any value in [0.05, 0.95] in steps of 0.05
	protected double cMeanIAT = cMeanST / cSystemUtilization; // E[IAT] = E[ST] / p

	/**
	 * Main method
	 * 
	 * @param args
	 *            - none
	 */
	public static void main(String[] args) {
		/*
		 * create simulation object
		 */
		Simulator sim = new Simulator();
		/*
		 * run simulation
		 */
		/**
		 * TASK 6
		 * UNCOMMENT CODE IN REPORT FUNCTION FOR OUTPUT
		 */
		if(RUN_REPEATED_SIM){
			runBatchMeansExperiment();
		}
		sim.start();
		/*
		 * print out report
		 */
		sim.report();
	}

	public static void runBatchMeansExperiment() {
		double[] cvarOptions = {0.5, 1.0, 2.0};
		double[] utilizationSteps = generateUtilizationSteps(0.05, 0.95, 0.05);
		System.out.println("STEPS " + Arrays.toString(utilizationSteps));

		for (double cvar : cvarOptions) {
			for (double utilization : utilizationSteps) {
				Simulator sim = new Simulator(cvar, utilization);

				sim.start();
				sim.report();
			}
		}
	}

	private static double[] generateUtilizationSteps(double start, double end, double step) {
		int size = (int) Math.round((end - start) / step) + 1;
		double[] steps = new double[size];
		for (int i = 0; i < size; i++) {
			steps[i] = start + i * step;
		}
		return steps;
	}

	// 5.1.3
	public void setSystemUtilization(double utilization) {
		if (utilization < 0.05 || utilization > 0.95) {
			throw new IllegalArgumentException("System utilization must be in [0.05, 0.95].");
		}
		double granularity = 0.05;
		double mod = utilization % granularity;
		// Allow for floating point imprecision
		if (Math.abs(mod) > 1e-9 && Math.abs(mod - granularity) > 1e-9) {
			throw new IllegalArgumentException("System utilization must be in steps of 0.05.");
		}
		this.cSystemUtilization = utilization;
		this.cMeanIAT = this.cMeanST / this.cSystemUtilization;
	}

	// PARAMETERS
	/**
	 * Turn on/off debug report in console.
	 */
	protected boolean isDebugReport = true;

	/**
	 * Turn on/off report in csv-files.
	 */
	protected boolean isCsvReport = true;

	/**
	 * inter arrival time of customers (in simulation time).
	 */
	public long interArrivalTime;

	/**
	 * service time of a customer (in simulation time).
	 */
	public long serviceTime;

	/**
	 * Number of customers for initialization.
	 */
	public long nInit;

	/**
	 * Length of batches.
	 */
	public long batchLength;

	/**
	 * Coefficient of variation.
	 */
	public double cVar;

	/**
	 * Random number generator for inter arrival times.
	 */
	public RandVar randVarInterArrivalTime;

	/**
	 * random number generator for service times
	 */
	public RandVar randVarServiceTime;

	// STATISTICS
	/**
	 * Map that contains all statistical relevant object such as counters and
	 * histograms.
	 */
	public HashMap<String, IStatisticObject> statisticObjects;

	/**
	 * Maximum queue size.
	 */
	public long maxQS;

	/**
	 * Minimum queue size.
	 */
	public long minQS;

	/**
	 * Number of batches in simulation.
	 */
	public long numBatches;

	/*
	 * TODO Problem 5.1 - naming your statistic objects
	 * Here you have to set some names (as Sting objects) for all your statistic objects
	 * They are later used to retrieve them from the dictionary
	 */
	// Strings used for receiving statisticobjects later in the dictionary.
	public String dtcWaitingTime = "discreteTimeCounterWaitingTime";
	public String dthWaitingTime = "discreteTimeHistogramWaitingTime";
	public String dtcServiceTime = "discreteTimeCounterServiceTime";
	public String dthServiceTime = "discreteTimeHistogramServiceTime";
	public String ctcQueueOccupancy = "continuousTimeCounterQueueOccupancy";
	public String cthQueueOccupancy = "continuousTimeHistogramQueueOccupancy";
	public String ctcServerUtilization = "continuousTimeCounterServerUtilization";
	public String cthServerUtilization = "continuousTimeHistogramServerUtilization";
	public String dtcBatchWaitingTime = "discreteTimeCounterBatchWaitingTime";
	public String tempdtcBatchWaitingTime = "temporaryDiscreteTimeCounterBatchWaitingTime";
	public String dtcBatchServiceTime = "discreteTimeCounterBatchServiceTime";
	public String tempdtcBatchServiceTime = "temporaryDiscreteTimeCounterBatchServiceTime";
	public String ccreBatchWaitingTime = "confidenceCounterWithRelativeErrorBatchWaitingTime";
	public String ccreWaitingTime = "confidenceCounterWithRelativeErrorWaitingTime";


	// 5.1.4 Variables to keep track of confidence intervals for customer waiting times.
	public String dccWaitingTimeCustomer5x = "confidenceCounterForIndividualCustomerWaitingTime5x";
	public String dccWaitingTimeCustomerBatch = "confidenceCounterForBatchCustomerWaitingTime";
	public String dccWaitingTimeCustomer = "confidenceCounterForCustomerWaitingTime";

	public long numWaitingTimeExceeds5TimesServiceTime;
	public long numBatchWaitingTimeExceeds5TimesBatchServiceTime;
	public long numWaitingTimeExceeds0;
	public long numWaitingTimeExceeds0Batch;
	public String dtacBatchWaitingTime = "discreteTimeAutocorrelationCounterBatchWaitingTime";

	private Simulator simulator;


	/**
	 * Constructor
	 * @param sim Simulator instance.
	 */
	public SimulationStudy(Simulator sim) {
		simulator = sim;
		simulator.setSimTimeInRealTime(1000);
		setSimulationParameters();
		initStatistics();
	}
	public SimulationStudy(Simulator sim, double cVar, double utilization) {
		simulator = sim;
		simulator.setSimTimeInRealTime(1000);
		this.cCvar = cVar;
		this.cSystemUtilization = utilization;
		setSimulationParameters();
		initStatistics();
	}


	/**
	 * Sets simulation parameters, converts real time to simulation time if
	 * needed.
	 */
	private void setSimulationParameters() {

		/*
		 * TODO Problem 5.1.1 - Set simulation parameters
		 * Hint: Take a look at the attributes of this class which have no usages yet (This may be indicated by your IDE)
		 */
		// this.nInit = cNInit;
		// this.cVar = ...
		this.nInit = this.cNInit;
		this.cVar = this.cCvar;
		this.batchLength = this.lBatch;



		/*
		 * TODO Problem 5.1.2 - Create random variables for IAT and ST
		 * You may use different random variables for this.randVarInterArrivalTime, since Cvar[IAT] = {0.5, 1, 2}
		 * You can use this.cVar as a configuration parameter for Cvar[IAT]
		 * !!! Make sure to use StdRNG objects with different seeds !!!
		 */
		RandVar iavRandVar = null;
		if(this.cCvar < 1 && this.cCvar > 0) {
			System.out.println("Choosing ErlangK");
			iavRandVar = new ErlangK(new StdRNG(1337),1,1);
			iavRandVar.setMeanAndCvar(cMeanIAT, cCvar);


		} else if (this.cCvar > 1) {
			System.out.println("Choosing HyperExponential");
			iavRandVar = new HyperExponential(new StdRNG(1337));
			iavRandVar.setMean(cMeanIAT);
			iavRandVar.setCvar(cCvar);

		}
		else {
			System.out.println("Choosing Exponential");
			iavRandVar = new Exponential(new StdRNG(1337),1);
			iavRandVar.setMeanAndCvar(cMeanIAT, cCvar);

		}


		RandVar serviceTimeRandVar = new Exponential(new StdRNG(420),cMeanST);

		this.randVarInterArrivalTime = iavRandVar;
		this.randVarServiceTime = serviceTimeRandVar;
		System.out.println("SETTTING PARAMETERS");



	}

	/**
	 * Initializes statistic objects. Adds them into Hashmap.
	 */
	private void initStatistics() {
		maxQS = Long.MIN_VALUE;
		minQS = Long.MAX_VALUE;

		// Init numBatches
		numBatches = 0;

		statisticObjects = new HashMap<>();
		statisticObjects.put(dtcWaitingTime, new DiscreteCounter("waiting time/customer"));
		statisticObjects.put(dthWaitingTime, new DiscreteHistogram("waiting_time_per_customer", 80, 0, 80));

		statisticObjects.put(dtcServiceTime, new DiscreteCounter("service time/customer"));
		statisticObjects.put(dthServiceTime, new DiscreteHistogram("service_time_per_customer", 80, 0, 80));

		statisticObjects.put(ctcQueueOccupancy, new ContinuousCounter("queue occupancy/time", simulator));
		statisticObjects.put(cthQueueOccupancy,
				new ContinuousHistogram("queue_occupancy_over_time", 80, 0, 80, simulator));

		statisticObjects.put(ctcServerUtilization, new ContinuousCounter("server utilization/time", simulator));
		statisticObjects.put(cthServerUtilization,
				new ContinuousHistogram("server_utilization_over_time", 80, 0, 80, simulator));

		/*
		 * TODO Problem 5.1.1 - Create a DiscreteConfidenceCounterWithRelativeError
		 * In order to check later if the simulation can be terminated according to the condition
		 */
		statisticObjects.put(ccreBatchWaitingTime, new DiscreteConfidenceCounterWithRelativeError("confidence batch waiting time/customer", 0.1));
		statisticObjects.put(tempdtcBatchWaitingTime, new DiscreteCounter("temp batch waiting time/customer"));



		/*
		 * TODO Problem 5.1.4 - Create counter to calculate the mean waiting time with batch means method
		 */
		statisticObjects.put(dtcBatchWaitingTime, new DiscreteCounter("batch waiting time/customer"));
		statisticObjects.put(dccWaitingTimeCustomer, new DiscreteConfidenceCounter("customer waiting time/customer"));

		/*
		 * TODO Problem 5.1.4 - Provide means to keep track of E[WT] > 5 * E[ST]
		 * !!! This is also called "waiting probability" in the sheet !!!
		 */
		numWaitingTimeExceeds5TimesServiceTime = 0;
		numBatchWaitingTimeExceeds5TimesBatchServiceTime = 0;
		numWaitingTimeExceeds0 = 0;
		numWaitingTimeExceeds0Batch = 0;



		/*
		 * TODO Problem 5.1.4 - Create confidence counter for individual waiting time samples
		 */
		statisticObjects.put(dccWaitingTimeCustomer5x, new DiscreteConfidenceCounter("Customer waiting time exceeds 5x service time",0.1));

		/*
		 * TODO Problem 5.1.4 - Create confidence counter for to count waiting times with batch means method
		 */
		statisticObjects.put(dccWaitingTimeCustomerBatch, new DiscreteConfidenceCounter("Customer batch waiting time exceeds 5x service time",0.1));

		/*
		 * TODO Problem 5.1.5 - Create a DiscreteAutocorrelationCounter for batch means
		 */
		statisticObjects.put(dtacBatchWaitingTime, new DiscreteAutocorrelationCounter("Autocorrelation Batch waiting time",maxLagAcc));
	}


	/**
	 * Report results. Print to console if isDebugReport = true. Print to csv
	 * files if isCsvReport = true. Note: Histogramms are only printed to csv
	 * files.
	 */
	public void report() {
		String sd = new SimpleDateFormat("yyyyMMdd_HHmmss_").format(new Date(System.currentTimeMillis()));
		String destination = sd + this.getClass().getSimpleName();

		if (isCsvReport) {
			File file = new File(destination);
			file.mkdir();
			for (IStatisticObject so : statisticObjects.values()) {
				so.csvReport(destination);
			}
		}
		if (isDebugReport) {
			/*
			 * TODO Problem 5.1 - Output reporting information!
			 * Print your statistic objects which are needed to answer the questions in the exercise sheet
			 */
			// for (IStatisticObject so : statisticObjects.values()) {
			// 	System.out.println(so.report());
			// }

			/** TASK 6 */
			if(RUN_REPEATED_SIM){
				System.out.println("========================================");
				System.out.println("System Utilization (ρ): " + cSystemUtilization);
				System.out.println("Coefficient of Variation (cvar[IAT]): " + cCvar);

				// Mean waiting time with confidence interval
				System.out.println("---------- Mean Waiting Time (Batch Means) ----------");
				System.out.println(statisticObjects.get(ccreBatchWaitingTime).report());

				// Waiting probability (batch-based)
				System.out.println("---------- Waiting Probability (WT > 5×E[ST]) ----------");
				System.out.println(statisticObjects.get(dccWaitingTimeCustomerBatch).report());
			}
			else{
				// NORMAL OUTPUT
				 System.out.println(statisticObjects.get(ccreBatchWaitingTime).report());
				 System.out.println(statisticObjects.get(dtcWaitingTime).report());
				 System.out.println(statisticObjects.get(dtcServiceTime).report());
				 System.out.println(statisticObjects.get(tempdtcBatchWaitingTime).report());

				 System.out.println("Total Customers: " + ((DiscreteCounter) statisticObjects.get(dtcWaitingTime)).getNumSamples());
				 System.out.println("Total Customers that experienced wait time : " + numWaitingTimeExceeds0 + "\nTotal Customers that waited 5x longer than expected service time: " + numWaitingTimeExceeds5TimesServiceTime);
				 System.out.println("Probability of experiencing wait time 5x expected service time: " + (double) numWaitingTimeExceeds5TimesServiceTime / (double) numWaitingTimeExceeds0 );

				 System.out.println("###################################### Confidence Counter individual Customers ####################################");
				 System.out.println(statisticObjects.get(dccWaitingTimeCustomer).report());
				 System.out.println(statisticObjects.get(dccWaitingTimeCustomer5x).report());

				 System.out.println("########################################### BATCHES ###############################################################");
				 System.out.println("Total Batches: " + numBatches);
				 System.out.println("Total Batches with wait time : " + numWaitingTimeExceeds0Batch);
				 System.out.println("Total Batches were mean wait time exceeded 5x expected service time: " + numBatchWaitingTimeExceeds5TimesBatchServiceTime);
				 System.out.println("Probability of experiencing wait time 5x expected service time per batch: " + (double) numBatchWaitingTimeExceeds5TimesBatchServiceTime / (double) numWaitingTimeExceeds0Batch );

				 System.out.println("######################################## Batch Confidence Counter ###############################################################");
				 System.out.println(statisticObjects.get(dccWaitingTimeCustomerBatch).report());

				 System.out.println("####################################### Batch Autocorrelation #############################################################");
				 System.out.println(statisticObjects.get(dtacBatchWaitingTime).report());

			}






		}

	}
}
