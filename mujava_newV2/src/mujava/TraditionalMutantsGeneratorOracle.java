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
 
/**
 * <p>Generate traditional mutants according to selected 
 *         operator(s) from gui.GenMutantsMain. 
 *         The original version is loaded, mutated, and compiled. 
 *         Outputs (mutated source and class files) are in 
 *         the traditional-mutants folder. </p>
 *         
 * <p>Currently available traditional mutation operators:
 *         (1) AORB: Arithmetic Operator Replacement (Binary),    
 *         (2) AORU: Arithmetic Operator Replacement (Unary), 
 *         (3) AORS: Arithmetic Operator Replacement (Short-cut), 
 *         (4) AODU: Arithmetic Operator Deletion (Unary), 
 *         (5) AODS: Arithmetic Operator Deletion (Short-cut), 
 *         (6) AOIU: Arithmetic Operator Insertion (Unary), 
 *         (7) AOIS: Arithmetic Operator Insertion (Short-cut), 
 *         (8) ROR: Rational Operator Replacement, 
 *         (9) COR: Conditional Operator Replacement,  
 *        (10) COD: Conditional Operator Deletion, 
 *        (11) COI: Conditional Operator Insertion,  
 *        (12) SOR: Shift Operator Replacement, 
 *        (13) LOR: Logical Operator Replacement, 
 *        (14) LOI: Logical Operator Insertion, 
 *        (15) LOD: Logical Operator Deletion, 
 *        (16) ASRS: Assignment Operator Replacement (short-cut) 
 * </p>
 * @author Yu-Seung Ma
 * @version 1.0
*/
package mujava;

import openjava.ptree.*;
import java.io.*;
import mujava.op.basic.*;
import mujava.op.util.*;
import mujava.util.Debug;


public class TraditionalMutantsGeneratorOracle extends MutantsGeneratorOracle
{
   String[] traditionalOp;

   public TraditionalMutantsGeneratorOracle(File f) 
   {
      super(f);
      traditionalOp = MutationSystemOracle.tm_operators;
   }
   
   public TraditionalMutantsGeneratorOracle(File f, boolean debug) 
   {
      super (f, debug);
      traditionalOp = MutationSystemOracle.tm_operators;
   }

   public TraditionalMutantsGeneratorOracle(File f, String[] tOP) 
   {
      super(f);
      traditionalOp = tOP;
   }

   /** 
    * Verify if the target Java source and class files exist, 
    * generate traditional mutants
    */
   void genMutants()
   {
      if (comp_unit == null)
      {
         System.err.println (original_file + " is skipped.");
      }
      
      ClassDeclarationList cdecls = comp_unit.getClassDeclarations();
      
      if (cdecls == null || cdecls.size() == 0) 
         return;

      if (traditionalOp != null && traditionalOp.length > 0)
      {
	     Debug.println("* Generating traditional mutants");
	     System.out.println("* Generating traditional mutants");
         MutationSystemOracle.clearPreviousTraditionalMutants();

         MutationSystemOracle.MUTANT_PATH = MutationSystemOracle.TRADITIONAL_MUTANT_PATH;

         CodeChangeLog.openLogFile();

         genTraditionalMutants(cdecls);

         CodeChangeLog.closeLogFile();
      }
   }

   /**
    * Compile traditional mutants into bytecode 
    */
   public void compileMutants()
   {
      if (traditionalOp != null && traditionalOp.length > 0)
      {
         try
         {
            Debug.println("* Compiling traditional mutants into bytecode");
            System.out.println("* Compiling traditional mutants into bytecode");
            String original_tm_path = MutationSystemOracle.TRADITIONAL_MUTANT_PATH;
            File f = new File(original_tm_path, "method_list");
            FileReader r = new FileReader(f);
            BufferedReader reader = new BufferedReader(r);
            String str = reader.readLine();
            
            while (str != null)
            {
               MutationSystemOracle.MUTANT_PATH = original_tm_path + "/" + str;
               super.compileMutants();
               str = reader.readLine();
            }
            reader.close();
            MutationSystemOracle.MUTANT_PATH = original_tm_path;
         } catch (Exception e)
         {
        	e.printStackTrace();
            System.err.println("Error at compileMutants() in TraditionalMutantsGeneratorOracle.java");
         }
      }
   }

   /**
    * Apply selected traditional mutation operators: 
    *      AORB, AORS, AODU, AODS, AOIU, AOIS, ROR, COR, COD, COI,
    *      SOR, LOR, LOI, LOD, ASRS, SID, SWD, SFD, SSD 
    * @param cdecls
    */
   void genTraditionalMutants(ClassDeclarationList cdecls)
   {
	  System.out.print("M�TODO genTraditionalMutants");
      for (int j=0; j<cdecls.size(); ++j)
      {
         ClassDeclaration cdecl = cdecls.get(j);
         //take care of the case for generics
         String tempName = cdecl.getName();
         if(tempName.indexOf("<") != -1 && tempName.indexOf(">")!= -1)        	 
        	 tempName = tempName.substring(0, tempName.indexOf("<")) + tempName.substring(tempName.lastIndexOf(">") + 1, tempName.length());
         	 System.out.println("tempName: " + tempName);	

         if (tempName.equals(MutationSystemOracle.CLASS_NAME))
         {
            mujava.op.util.Mutator mutant_op;
               boolean AOR_FLAG = false;
     
               try
               {
                  //generate a list of methods from the original java class
            	  //System.out.println("MutationSystemOracle.MUTANT_PATH: " + MutationSystemOracle.MUTANT_PATH);
                  File f = new File(MutationSystemOracle.MUTANT_PATH, "method_list");
                  FileOutputStream fout = new FileOutputStream(f);
                  PrintWriter out = new PrintWriter(fout);

                  mutant_op = new CreateDirForEachMethod(file_env, cdecl, comp_unit, out);

                  comp_unit.accept(mutant_op);
                  out.flush();  
                  out.close();
               } catch (Exception e)
               {
                  System.err.println("Error in writing method list");
                  return;
               }
         }
      }
   }
}