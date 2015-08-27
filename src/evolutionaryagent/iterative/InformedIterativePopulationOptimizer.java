/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.iterative;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import unalcol.math.logic.Predicate;
import unalcol.optimization.OptimizationFunction;
import unalcol.optimization.iterative.IterativePopulationOptimizer;
import unalcol.optimization.solution.Solution;
import unalcol.optimization.transformation.Transformation;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author Camilo Alaguna
 */
public class InformedIterativePopulationOptimizer<S> extends IterativePopulationOptimizer<S> {

    protected String filename = "";
    
    public InformedIterativePopulationOptimizer(Predicate condition,
            Transformation<S> transformation, Vector<Solution<S>> population,
            String filename) {
        super(condition, transformation, population);
        this.filename = filename;
    }
    
    @Override
    public Vector<Solution<S>> iteration( int k, OptimizationFunction<S> f, 
            Vector<Solution<S>> output ) {
        this.output = transformation.apply(output, f);
        generation = k+1;
        
        try {
            inform();
        } catch (IOException ex) {
            Logger.getLogger(InformedIterativePopulationOptimizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateTrace();
        return this.output;
    }
    
    protected void inform() throws IOException {
        Solution best = getBestSolution(this.output);
        FileWriter file = new FileWriter(filename, true);
        PrintWriter pw = new PrintWriter(file);
        pw.println(this.generation + " " + best.get().toString());
        file.close();
    }
    
    public Solution getBestSolution(Vector<Solution<S>> solutions) {
        double maxFitness = Double.MIN_VALUE;
        Solution candidate = null;
        for(int i = 0;i < solutions.size(); ++i)
            if(solutions.get(i).value() > maxFitness) {
                candidate = solutions.get(i);
                maxFitness = candidate.value();
            }
        return candidate;
    }
    
}
