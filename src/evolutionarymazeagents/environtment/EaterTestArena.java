/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EvolutionaryAgent.environtment;

import javax.swing.JFileChooser;
import unalcol.agents.Agent;
import unalcol.agents.examples.labyrinth.Labyrinth;
import unalcol.agents.examples.labyrinth.teseoeater.TeseoEaterLabyrinth;
import unalcol.agents.simulate.util.SimpleLanguage;

/**
 *
 * @author Camilo Alaguna
 */
public class EaterTestArena extends TestArena {

    public EaterTestArena(Agent agent, SimpleLanguage language, JFileChooser file, int xi, int yi) {
        super(agent, language, file, xi, yi);
    }
    
    @Override
    public Labyrinth newLabyrinthInstance(){
        labyrinth = new TeseoEaterLabyrinth( agent, new int[Labyrinth.DEFAULT_SIZE][Labyrinth.DEFAULT_SIZE], language );
        return labyrinth;
    }
    
}
