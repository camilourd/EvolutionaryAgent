/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.environtment;

import javax.swing.JFileChooser;
import unalcol.agents.Agent;
import unalcol.agents.examples.labyrinth.Labyrinth;
import unalcol.agents.examples.labyrinth.teseo.TeseoLabyrinth;
import unalcol.agents.simulate.util.SimpleLanguage;

/**
 *
 * @author Camilo Alaguna
 */
public class SimpleTestArena extends TestArena {

    public SimpleTestArena(Agent agent, SimpleLanguage language, JFileChooser file) {
        super(agent, language, file);
    }

    @Override
    public Labyrinth newLabyrinthInstance() {
        labyrinth = new TeseoLabyrinth( agent, new int[Labyrinth.DEFAULT_SIZE][Labyrinth.DEFAULT_SIZE], language );
        return labyrinth;
    }
    
}
