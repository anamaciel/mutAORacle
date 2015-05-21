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



package mujava;

import openjava.ptree.*;
import java.io.*;
import java.util.HashMap;
import java.util.List;

import mujava.op.basic.*;
import mujava.op.util.*;
import mujava.util.Debug;
/**
 * <p>
 * Description: New tranditional mutants generator class build exclusively for command line version
 * </p>
 * 
 * @author Lin Deng
 * @version 1.0
 * 
 */

public class TraditionalMutantsGeneratorCLI extends TraditionalMutantsGenerator {
	
	HashMap<String, List<String>> traditionalOpMap;
	

	public TraditionalMutantsGeneratorCLI(File f) {
		super(f);
	}

	public TraditionalMutantsGeneratorCLI(File f, boolean debug) {
		super(f, debug);
	}

	public TraditionalMutantsGeneratorCLI(File f, String[] tOP) {
		super(f, tOP);
	}

	public TraditionalMutantsGeneratorCLI(File f, HashMap<String, List<String>> traditionalOps)
	{
		super(f);
		traditionalOpMap = traditionalOps;
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

     // if (traditionalOp != null && traditionalOp.length > 0)
      {
	     Debug.println("* Generating traditional mutants");
         MutationSystem.clearPreviousTraditionalMutants();
         MutationSystem.MUTANT_PATH = MutationSystem.TRADITIONAL_MUTANT_PATH;
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
            String original_tm_path = MutationSystem.TRADITIONAL_MUTANT_PATH;
            File f = new File(original_tm_path, "method_list");
            FileReader r = new FileReader(f);
            BufferedReader reader = new BufferedReader(r);
            String str = reader.readLine();
            
            while (str != null)
            {
               MutationSystem.MUTANT_PATH = original_tm_path + "/" + str;
               super.compileMutants();
               str = reader.readLine();
            }
            reader.close();
            MutationSystem.MUTANT_PATH = original_tm_path;
         } catch (Exception e)
         {
            System.err.println("Error at compileMutants() in TraditionalMutantsGenerator.java");
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
      for (int j=0; j<cdecls.size(); ++j)
      {
         ClassDeclaration cdecl = cdecls.get(j);

         if (cdecl.getName().equals(MutationSystem.CLASS_NAME))
         {
            mujava.op.util.Mutator mutant_op;
               boolean AOR_FLAG = false;
     
               try
               {
                  File f = new File(MutationSystem.MUTANT_PATH, "method_list");
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
