/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EvolutionaryAgent.evolution.agent.teseo.statement;

import EvolutionaryAgent.types.statement.StatementSet;
import unalcol.agents.Percept;

/**
 *
 * @author Camilo Alaguna
 */
public class While extends StatementSet {

    protected Conditional condition;
    protected int maxRepetitions;

    public While(Conditional condition, int maxRepetitions) {
        this.condition = condition;
        this.maxRepetitions = maxRepetitions;
    }
    
    @Override
    public boolean areThereRepetitions(Percept prcpt) {
        if(maxRepetitions-- > 0)
            return condition.isAccommplished(prcpt);
        return false;
    }
    
    @Override
    public int repeat() {
        return condition.getEnd();
    }

    @Override
    public Class getType() {
        return While.class;
    }
    
}
