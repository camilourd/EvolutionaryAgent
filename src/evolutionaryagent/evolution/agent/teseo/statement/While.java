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
public class While extends StatementSet {

    protected Conditional condition;

    public While(Conditional condition) {
        this.condition = condition;
    }
    
    @Override
    public boolean areThereRepetitions(Percept prcpt) {
        return condition.isAccommplished(prcpt);
    }
    
    @Override
    public int repeat() {
        return condition.getEnd();
    }

    @Override
    public Class<While> getType() {
        return While.class;
    }
    
}
