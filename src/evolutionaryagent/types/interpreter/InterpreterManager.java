/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.types.interpreter;

import evolutionaryagent.types.statement.Statement;
import unalcol.agents.Percept;
import unalcol.types.collection.list.Stack;

/**
 *
 * @author Camilo Alaguna
 */
public abstract class InterpreterManager<T> {
    
    protected Interpreter<T> interpreter;
    protected Stack<Statement> statements;
    
    public InterpreterManager(Interpreter<T> interpreter) {
        this.interpreter = interpreter;
        this.statements = new Stack<Statement>();
    }
    
    public abstract String getNextInstruction(Percept prcpt);
    
    public void restart() {
        statements = new Stack<Statement>();
        interpreter.restart();
    }

    public T getInstructions() {
        return interpreter.getInstructions();
    }

    public Interpreter<T> getInterpreter() {
        return interpreter;
    }
    
}
