/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.evolution.agent.teseo.statement;

import evolutionaryagent.types.statement.StatementSet;
import unalcol.agents.Percept;

/**
 *
 * @author Camilo Alaguna
 */
public class Repeat extends StatementSet {

    int repetitions, start;

    public Repeat(int repetitions, int start) {
        this.repetitions = repetitions;
        this.start = start;
    }
    
    @Override
    public boolean areThereRepetitions(Percept prcpt) {
        return repetitions > 0;
    }
    
    @Override
    public int repeat() {
        --repetitions;
        return start;
    }
    
    @Override
    public Class getType() {
        return Repeat.class;
    }
    
}
