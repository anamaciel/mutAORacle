package runner;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;


public class JUnitExecutionListener extends RunListener {


	public void testRunStarted(Description description) throws Exception {
		//System.out.println("Number of tests to execute: " + description.testCount());
		//TestClass.arquivo+= "Number of tests to execute: " + description.testCount()+"\n";
	}

	public void testRunFinished(Result result) throws Exception {
		//System.out.println("Number of tests executed: " + result.getRunCount());
		//TestClass.arquivo+= "Number of tests to execute: " + result.getRunCount()+"\n";
	}

	public void testStarted(Description description) throws Exception {
		//System.out.print("MethodName: " + description.getMethodName()+" ");
		TestClass.arquivo+= "MethodName: " + description.getMethodName()+" ";
	}

	public void testFinished(Description description) throws Exception {
		//  System.out.println("Finished: " + description.getMethodName());
		//System.out.println();
		TestClass.arquivo+= "\n";
	}

	public void testFailure(Failure failure) throws Exception {
		//System.out.print(" Failed ");// + failure.getDescription().getMethodName());
		TestClass.arquivo+=" Failed ";
	}

	public void testAssumptionFailure(Failure failure) {
		//System.out.print(" Failed ");// + failure.getDescription().getMethodName());
		TestClass.arquivo+=" Failed ";
	}

	public void testIgnored(Description description) throws Exception {
		//  System.out.print("Ignored ");// + description.getMethodName());
	}
	
	public void makeReportOriginal(String file){
		
	}
}