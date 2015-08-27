/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EvolutionaryAgent.evolution;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import EvolutionaryAgent.environtment.SimpleTestArena;
import EvolutionaryAgent.environtment.TestArena;
import EvolutionaryAgent.evolution.agent.EvolutionaryAgentProgram;
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

    protected int xi = 0, yi = 0;
    public JFileChooser file;

    public FitnessAgentProgram() {
        file = new JFileChooser( "." );
        file.showOpenDialog(file);
    }

    public void setInitialPosition(int x, int y) {
        this.xi = x;
        this.yi = y;
    }
    
    @Override
    public Double apply(EvolutionaryAgentProgram program) {
        try {
            program.init();
            Agent agent = new Agent(program);
            TestArena frame = new SimpleTestArena( agent, getLanguage(), file, xi, yi);
            LabyrinthDrawer.DRAW_AREA_SIZE = 600;
            LabyrinthDrawer.CELL_SIZE = 40;
            Labyrinth.DEFAULT_SIZE = 15;
            frame.setVisible(true);
            frame.runTest();
            frame.dispose();
            //System.out.println("Fitness: " + program.fitness);
            return program.getFitness();
        } catch (InterruptedException ex) {
            Logger.getLogger(FitnessAgentProgram.class.getName()).log(Level.SEVERE, null, ex);
            return Double.MIN_VALUE;
        }
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
