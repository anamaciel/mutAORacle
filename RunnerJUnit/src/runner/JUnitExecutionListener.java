package runner;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;


public class JUnitExecutionListener extends RunListener {


	public void testRunStarted(Description description) throws Exception {
		//System.out.println("Number of tests to execute: " + description.testCount());
	}

	public void testRunFinished(Result result) throws Exception {
		//System.out.println("Number of tests executed: " + result.getRunCount());
	}

	public void testStarted(Description description) throws Exception {
		System.out.print("MethodName: " + description.getMethodName()+" ");
	}

	public void testFinished(Description description) throws Exception {
		//  System.out.println("Finished: " + description.getMethodName());
		System.out.println();
	}

	public void testFailure(Failure failure) throws Exception {
		System.out.print(" Failed ");// + failure.getDescription().getMethodName());
	}

	public void testAssumptionFailure(Failure failure) {
		System.out.print(" Failed ");// + failure.getDescription().getMethodName());
	}

	public void testIgnored(Description description) throws Exception {
		//  System.out.print("Ignored ");// + description.getMethodName());
	}
	
	public void makeReportOriginal(String file){
		
	}
}