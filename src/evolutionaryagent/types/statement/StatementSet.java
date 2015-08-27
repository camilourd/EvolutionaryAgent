/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.types.statement;

import unalcol.agents.Percept;

/**
 *
 * @author Camilo Alaguna
 */
public abstract class StatementSet implements Statement {

    public abstract boolean areThereRepetitions(Percept prcpt);
    public abstract int repeat();
    
}
