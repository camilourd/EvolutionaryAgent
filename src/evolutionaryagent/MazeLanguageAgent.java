/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent;

import evolutionaryagent.evolution.FitnessAgentProgram;
import evolutionaryagent.evolution.GrowCompilerTeseoAgentProgram;
import evolutionaryagent.iterative.InformedIterativePopulationOptimizer;
import evolutionaryagent.operators.binary.XOver;
import evolutionaryagent.evolution.agent.teseo.InterpreterTeseoAgentProgram;
import unalcol.algorithm.iterative.ForLoopCondition;
import unalcol.evolution.Individual;
import unalcol.evolution.IndividualInstance;
import unalcol.evolution.haea.HAEA;
import unalcol.evolution.haea.HaeaOperators;
import unalcol.evolution.haea.SimpleHaeaOperators;
import unalcol.instance.InstanceProvider;
import unalcol.instance.InstanceService;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.PopulationOptimizer;
import unalcol.optimization.operators.Operator;
import unalcol.optimization.operators.binary.Mutation;
import unalcol.optimization.operators.binary.Transposition;
import unalcol.optimization.selection.Selection;
import unalcol.optimization.selection.Tournament;
import unalcol.optimization.solution.Solution;
import unalcol.optimization.solution.SolutionInstance;
import unalcol.optimization.transformation.Transformation;
import unalcol.reflect.service.ServiceProvider;
import unalcol.reflect.util.ReflectUtil;
import unalcol.tracer.ConsoleTracer;
import unalcol.tracer.FileTracer;
import unalcol.tracer.Tracer;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.bitarray.BitArrayInstance;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author Camilo Alaguna
 */
public class MazeLanguageAgent {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Reflection
        ServiceProvider provider = ReflectUtil.getProvider("services/");
        
        // Search Space 
        int BITARRAYLENGTH = 50;
        BitArray array = new BitArray(BITARRAYLENGTH, true);
        InstanceService ikey = new BitArrayInstance();
        provider.register(ikey);
        provider.setDefault_service(InstanceService.class,BitArray.class,ikey);
        
        // Solution space
        GrowCompilerTeseoAgentProgram grow = new GrowCompilerTeseoAgentProgram();
        Solution<InterpreterTeseoAgentProgram> solution = new Individual<BitArray, InterpreterTeseoAgentProgram>(array, grow);
        SolutionInstance skey = new IndividualInstance( grow );
        provider.setDefault_service(InstanceService.class,Solution.class,skey);
        
        // Initial population
        int POPSIZE = 100;
        provider.register(skey);
        Vector<Solution<BitArray>> pop = InstanceProvider.get(solution, POPSIZE);
        
        // Function being optimized
        FitnessAgentProgram fitness = new FitnessAgentProgram();
        fitness.addInitialPosition(0, 0);
        fitness.addInitialPosition(14, 6);
        fitness.addInitialPosition(2, 13);
        fitness.addInitialPosition(9, 1);
        fitness.addInitialPosition(3, 11);
        fitness.addInitialPosition(11, 9);
        fitness.addInitialPosition(1, 1);
        fitness.addInitialPosition(2, 5);
        fitness.addInitialPosition(3, 5);
        fitness.addInitialPosition(13, 12);
        fitness.addInitialPosition(4, 13);
        fitness.addInitialPosition(8, 9);
        fitness.addInitialPosition(11, 10);
        fitness.addInitialPosition(13, 14);
        fitness.addInitialPosition(12, 3);
        fitness.addInitialPosition(7, 2);
        fitness.addInitialPosition(2, 2);
        fitness.addInitialPosition(0, 4);
        
        OptimizationFunction function =  fitness;
        // Evaluating the fitness of the initial population
        int numberOfThreads = 70;
        Solution.paralelEvaluate((Vector)pop, function, numberOfThreads);
        
        Mutation mutation = new Mutation();
        XOver xover = new XOver();
        Transposition transposition = new Transposition();
        
        // Genetic operators
        Operator[] opers = new Operator[]{xover, mutation, transposition};
        HaeaOperators haeaOperators = new SimpleHaeaOperators(opers);
        
        // Extra parent selection mechanism
        Selection selection = new Tournament(4);
        
        // HAEA Strategy
        
        
        // Genetic Algorithm Transformation
        Transformation transformation = new HAEA(haeaOperators, grow, selection );
        transformation.activateParalelEvaluation(numberOfThreads);
        
        // Evolution generations
        int MAXITER = 60;
        ForLoopCondition condition = new ForLoopCondition(MAXITER);
        
        // Evolutionary algorithm (is a population optimizer)
        PopulationOptimizer ea = new InformedIterativePopulationOptimizer(condition,
                transformation, pop, "bestPosition(18il).txt");
        
         // A console set tracer
        Tracer tracer = new ConsoleTracer(ea);
        // Adding the tracer collection to the given population optimizer (evolutionary algorithm)
        provider.register(tracer);
        tracer = new FileTracer(ea, "haea.txt", true);
        provider.register(tracer);
        
        
        ea.apply(function);
        
        tracer.close();
    }
    
}
