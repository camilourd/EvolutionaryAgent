/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.evolution.agent.teseo.interpreter;

import evolutionaryagent.evolution.agent.teseo.TeseoMemory;
import evolutionaryagent.types.interpreter.InterpreterManager;
import evolutionaryagent.types.statement.Action;
import evolutionaryagent.types.statement.Statement;
import unalcol.agents.Percept;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author Camilo Alaguna
 */
public class BitArrayTeseoInterpreterManager extends InterpreterManager<BitArray> {
    
    protected TeseoMemory memory;

    public BitArrayTeseoInterpreterManager(BitArrayTeseoInterpreter interpreter) {
        super(interpreter);
    }

    public void setMemory(TeseoMemory memory) {
        this.memory = memory;
        ((BitArrayTeseoInterpreter)this.interpreter).setMemory(memory);
    }

    @Override
    public String getNextInstruction(Percept prcpt) {
        while(!isThereAction())
            if(statements.isEmpty() && !interpreter.areStatements())
                statements.push(new Action(((BitArrayTeseoInterpreter)interpreter).getDieAction()));
            else
                manageStatements(prcpt);
        return ((Action)statements.pop()).getInstruction();
    }
    
    public boolean isThereAction() {
        return !statements.isEmpty() && statements.get().getType() == Action.class;
    }
    
    protected void manageStatements(Percept prcpt) {
        Class<?> type = interpreter.getNextStatementType();
        interpreter.moveToNextStatement();
        manageCase(prcpt, type);
    }

    private void manageCase(Percept prcpt, Class<?> type) {
        if(type != null)
            computeStatement(prcpt);
        else if(!statements.isEmpty())
            closeStatementSet(prcpt);
    }

    private void computeStatement(Percept prcpt) {
        Vector<Statement> stms = interpreter.compute(prcpt);
        addStatements(stms);
    }

    private void closeStatementSet(Percept prcpt) {
        Vector<Statement> inputs = new Vector<Statement>();
        inputs.add(statements.pop());
        Vector<Statement> stms = interpreter.compute(prcpt, inputs);
        addStatements(stms);
    }

    protected void addStatements(Vector<Statement> stms) throws IndexOutOfBoundsException {
        if(stms != null)
            for(int i = 0; i < stms.size(); ++i)
                this.statements.push(stms.get(i));
    }
    
}
