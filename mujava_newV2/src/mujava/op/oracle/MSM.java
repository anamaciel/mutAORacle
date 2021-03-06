/**
 * Copyright (C) 2015  the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 
package mujava.op.oracle;


import java.io.IOException;
import java.io.PrintWriter;

import mujava.op.util.OracleMutantCodeWriter;
import openjava.mop.FileEnvironment;
import openjava.mop.OJClass;
import openjava.ptree.ClassDeclaration;
import openjava.ptree.CompilationUnit;
import openjava.ptree.ExpressionList;
import openjava.ptree.Literal;
import openjava.ptree.MethodCall;
import openjava.ptree.ParseTreeException;
/**
 * <p>Generate MSM (Modify String Message) mutants --
 *    Example: assertArrayEquals(String m1, int[], int[]) → assertArrayEquals(String m2, int[], int[])
 *    
 *    assertEquals
 *    assertArrayEquals
 *    assertNotEquals
 *    assertFalse
 *    assertTrue
 *    assertNull
 *    assertNotNull
 *    assertNotSame
 *    assertSame
 *    fail
 *    
 * </p>
 * @author Ana Maciel
 * @version 1.0
 */

public class MSM extends JUnit_OP
{

	public MSM(FileEnvironment file_env, ClassDeclaration cdecl, CompilationUnit comp_unit)
	{
		super( file_env, comp_unit );
	}


	public void visit( MethodCall p ) throws ParseTreeException
	{

		ExpressionList arguments = p.getArguments();
		//System.out.println(p.getName());

		if ((p.getName().equals("assertEquals"))||(p.getName().equals("assertNotEquals"))
				||(p.getName().equals("assertArrayEquals") ||(p.getName().equals("assertNotEquals"))
						||(p.getName().equals("assertNotSame") ||(p.getName().equals("assertSame")))))
		{
			try {
				//System.out.println("environment: " + getEnvironment());
				OJClass varType = arguments.get(1).getType(getEnvironment());
				//System.out.println(varType.getName());				   
				ExpressionList mutantArgs = new ExpressionList();
				//System.out.println("qtde argumentos: " + arguments.size());
				//System.out.println(arguments.get(0).getType(getEnvironment()).getName().contains("String"));
				if(arguments.size()==3 && arguments.get(0).getType(getEnvironment()).getName().contains("String")){
					mutantArgs.add(Literal.makeLiteral("Mutant MSM"));
					mutantArgs.add(arguments.get(1));
					mutantArgs.add(arguments.get(2));
					MethodCall mutant = new MethodCall(p.getReferenceExpr(), p.getName(), mutantArgs);
					//System.out.println(p);
					//System.out.println(mutant);
					outputToFile(p, mutant);
				}
				
				if(arguments.size()==4){						
					mutantArgs.add(Literal.makeLiteral("Mutant MSM"));
					mutantArgs.add(arguments.get(1));
					mutantArgs.add(arguments.get(2));
					mutantArgs.add(arguments.get(3));
					MethodCall mutant = new MethodCall(p.getReferenceExpr(), p.getName(), mutantArgs);
					//System.out.println(p);
					//System.out.println(mutant);
					outputToFile(p, mutant);
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if ((p.getName().equals("assertFalse"))||(p.getName().equals("assertTrue"))
				||(p.getName().equals("assertNull") ||(p.getName().equals("assertNotNull"))))
		{


			ExpressionList mutantArgs = new ExpressionList();
			//System.out.println("qtde argumentos: " + arguments.size());

			if(arguments.size()==2){						
				mutantArgs.add(Literal.makeLiteral("Mutant MSM"));
				mutantArgs.add(arguments.get(1));
				MethodCall mutant = new MethodCall(p.getReferenceExpr(), p.getName(), mutantArgs);
				//System.out.println(p);
				//System.out.println(mutant);
				outputToFile(p, mutant);
			}
		}

		if ((p.getName().equals("fail")))
		{

			ExpressionList mutantArgs = new ExpressionList();
			//System.out.println("qtde argumentos: " + arguments.size());

			if(arguments.size()==1){						
				mutantArgs.add(Literal.makeLiteral("Mutant MSM"));
				MethodCall mutant = new MethodCall(p.getReferenceExpr(), p.getName(), mutantArgs);
				//System.out.println(p);
				//System.out.println(mutant);
				outputToFile(p, mutant);
			}

			


		}

	}

	/**
	 * Write MSM mutants to files
	 * @param original_field
	 * @param mutant
	 */
	public void outputToFile(MethodCall original_field, MethodCall mutant)
	{
		if (comp_unit == null) 
			return;

		String f_name;
		num++;
		f_name = getSourceNameOracle("MSM");
		String mutant_dir = getMuantID("MSM");

		try 
		{
			PrintWriter out = getPrintWriter(f_name);
			System.out.println("f_name: " + f_name);
			MSM_Writer writer = new MSM_Writer(mutant_dir, out);
			writer.setMutant(original_field, mutant);
			//System.out.println(mutant);
			//System.out.println(currentMethodSignature);
			writer.setMethodSignature(currentMethodSignature);
			comp_unit.accept( writer );
			out.flush();  
			out.close();
			
			OracleMutantCodeWriter.writeAnnotations(f_name);
		} catch ( IOException e ) {
			System.err.println( "fails to create " + f_name );
		} catch ( ParseTreeException e ) {
			System.err.println( "errors during printing " + f_name );
			e.printStackTrace();
		}
	}
}
