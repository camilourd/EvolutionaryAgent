/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.evolution;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import evolutionaryagent.environtment.SimpleTestArena;
import evolutionaryagent.environtment.TestArena;
import evolutionaryagent.evolution.agent.EvolutionaryAgentProgram;
import evolutionaryagent.types.Pair;
import unalcol.agents.Agent;
import unalcol.agents.examples.labyrinth.Labyrinth;
import unalcol.agents.examples.labyrinth.LabyrinthDrawer;
import unalcol.agents.examples.labyrinth.teseoeater.TeseoEaterLanguage;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.optimization.OptimizationFunction;

/**
 *
 * @author Camilo Alaguna
 */
public class FitnessAgentProgram extends OptimizationFunction<EvolutionaryAgentProgram> {

    protected JFileChooser file;
    protected ArrayList<Pair<Integer, Integer>> initialPositions;

    public FitnessAgentProgram() {
        file = new JFileChooser( "." );
        file.showOpenDialog(file);
        initialPositions = new ArrayList<Pair<Integer, Integer>>();
    }

    public void addInitialPosition(int x, int y) {
    	if(x >= 0 && y >= 0 && x < 15 && y < 15)
    		initialPositions.add(new Pair<Integer, Integer>(x, y));
    }
    
    @Override
    public Double apply(EvolutionaryAgentProgram program) {
        try {
            Agent agent = new Agent(program);
            TestArena frame = builtArena(agent);
            frame.setVisible(true);
            
            double fitness = 0;
            for(Pair<Integer, Integer> pos : initialPositions) {
            	frame.changeAgentPosition(pos.first, pos.second, 0);
	            frame.runTest();
            	fitness += program.getFitness();
            }
            frame.dispose();
            return fitness;
        } catch (InterruptedException ex) {
            Logger.getLogger(FitnessAgentProgram.class.getName()).log(Level.SEVERE, null, ex);
            return Double.MIN_VALUE;
        }
    }
    
    public TestArena builtArena(Agent agent) throws InterruptedException {
    	TestArena frame = new SimpleTestArena( agent, getLanguage(), file);
        LabyrinthDrawer.DRAW_AREA_SIZE = 600;
        LabyrinthDrawer.CELL_SIZE = 40;
        Labyrinth.DEFAULT_SIZE = 15;
        return frame;
    }
    
    private SimpleLanguage getLanguage(){
        return new TeseoEaterLanguage(
            new String[]{"front", "right", "back", "left", "treasure",
                         "resource", "resource-color", "resource-shape", "resource-size",
                         "resource-weight", "resource-type", "energy_level" },
            new String[]{"no_op", "die", "advance", "rotate", "eat"}
        );
    }
    
}
