/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EvolutionaryAgent.types.interpreter;

import EvolutionaryAgent.types.statement.Statement;
import unalcol.agents.Percept;
import unalcol.types.collection.list.Stack;

/**
 *
 * @author Camilo Alaguna
 */
public abstract class InterpreterManager<T> {
    
    protected Interpreter interpreter;
    protected Stack<Statement> statements;
    
    public InterpreterManager(Interpreter<T> compiler) {
        this.interpreter = compiler;
        this.statements = new Stack<Statement>();
    }
    
    public abstract String getNextInstruction(Percept prcpt);
    
    public void restart() {
        statements = new Stack<Statement>();
        interpreter.restart();
    }

    public T getInstructions() {
        return (T) interpreter.getInstructions();
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }
    
}
