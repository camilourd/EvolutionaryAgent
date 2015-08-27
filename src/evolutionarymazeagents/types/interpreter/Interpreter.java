/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EvolutionaryAgent.types.interpreter;

import EvolutionaryAgent.types.statement.Statement;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author Camilo Alaguna
 * @param <T>
 */
public interface Interpreter<T> {
    
    public boolean areStatements();
    public T getInstructions();
    public Class getNextStatementType();
    public Vector<Statement> compute(Percept prcpt);
    public Vector<Statement> compute(Percept prcpt, Vector<Statement> information);
    public void moveToNextStatement();
    public void restart();
    public void setLanguage(SimpleLanguage language);
    
}
