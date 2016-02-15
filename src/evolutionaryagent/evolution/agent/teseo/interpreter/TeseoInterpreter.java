/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.evolution.agent.teseo.interpreter;

import evolutionaryagent.types.statement.Action;
import evolutionaryagent.evolution.agent.teseo.statement.Conditional;
import evolutionaryagent.evolution.agent.teseo.statement.Repeat;
import evolutionaryagent.types.statement.Statement;
import evolutionaryagent.types.statement.StatementSet;
import evolutionaryagent.evolution.agent.teseo.statement.While;
import unalcol.agents.Percept;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.list.Stack;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author Camilo Alaguna
 */
public class TeseoInterpreter extends BitArrayTeseoInterpreter {

    public final int STATEMENT_SIZE = 3;
    public final int NUMBER_SIZE = 4;
    public final int ACTION_SIZE = 2;
    
    public final int DIE = 1;
    public final int ADVANCE = 2;
    public final int ROTATE = 3;
    
    public final Class<?>[][][] statementTable = { 
        {{Conditional.class, Action.class}, {null, While.class}},
        {{Action.class, null}, {Repeat.class, Action.class}}
    };

    public TeseoInterpreter(BitArray instructions) {
        super(instructions);
    }
    
    @Override
    public Class<?> getNextStatementType() {
        return (areStatements())?
                statementTable[(instructions.get(head))? 1:0][(instructions.get(head + 1))? 1:0][(instructions.get(head + 2))? 1:0] : null;
    }
    
    @Override
    public void moveToNextStatement() {
        statementType = getNextStatementType();
        head += STATEMENT_SIZE;
    }

    @Override
    public boolean areStatements() {
        return isThereEnoughSpace(STATEMENT_SIZE);
    }

    @Override
    public String getDieAction() {
        return language.getAction(DIE);
    }

    @Override
    public Vector<Statement> compute(Percept prcpt) {
    	if(statementType != null) {
	        if(statementType.equals(Action.class))
	            return getActions(prcpt);
	        else if(statementType.equals(Repeat.class))
	            return getRepeat(prcpt);
	        else if(statementType.equals(Conditional.class))
	            return getConditional(prcpt);
	        else if(statementType.equals(While.class))
	            return getWhile(prcpt);
    	}
        return null;
    }
    
    protected Vector<Statement> getActions(Percept prcpt) {
        Vector<Statement> statements = new Vector<Statement>();
        if(isThereEnoughSpace(ACTION_SIZE)) {
            int dir = ((instructions.get(head))? 2:0) + ((instructions.get(head + 1))? 1:0);
            //if(dir == 3) System.out.println(head);
            if(!getPerception(prcpt, dir)) {
                statements.add(new Action(language.getAction(ADVANCE)));
                for(int i = 0;i < dir; ++i)
                    statements.add(new Action(language.getAction(ROTATE)));
            }
        }
        head += ACTION_SIZE;
        return statements;
    }
    
    private Vector<Statement> getRepeat(Percept prcpt) {
        Vector<Statement> statements = new Vector<Statement>();
        if(isThereEnoughSpace(NUMBER_SIZE)) {
            statements.add(new Repeat(readNumber(head, NUMBER_SIZE), head + NUMBER_SIZE));
            statements = compute(prcpt, statements);
        }
        return statements;
    }
    
    private Vector<Statement> getConditional(Percept prcpt) {
        Vector<Statement> statements = new Vector<Statement>();
        Conditional conditional = new Conditional(memory, this, head);
        if(!conditional.isAccommplished(prcpt)) {
            head = conditional.getEnd();
            ignore(prcpt, conditional);
        } else {
            head = conditional.getEnd();
            statements.add(conditional);
        }
        return statements;
    }
    
    private Vector<Statement> getWhile(Percept prcpt) {
        Vector<Statement> statements = new Vector<Statement>();
        statements.add(new While(new Conditional(memory, this, head)));
        return compute(prcpt, statements);
    }

    @Override
    public Vector<Statement> compute(Percept prcpt, Vector<Statement> information) {
        Vector<Statement> statements = new Vector<Statement>();
        if(information.size() > 0 && !information.get(0).getType().equals(Conditional.class)
        		&& !information.get(0).getType().equals(Action.class)) {
            StatementSet loop = (StatementSet) information.get(0);
            if(loop.areThereRepetitions(prcpt)) {
                head = loop.repeat();
                statements.add(loop);
            } else if(loop.getType().equals(While.class)) {
                head = loop.repeat();
                ignore(prcpt, loop);
            }
        }
        return statements;
    }

    private void ignore(Percept prcpt, Statement statement) {
        Stack<Statement> loops = new Stack<Statement>();
        loops.add(statement);
        while(!loops.isEmpty()) {
            moveToNextStatement();
            if(statementType != null) {
	            if(statementType.equals(Action.class))
	                head += ACTION_SIZE;
	            else if(statementType.equals(Repeat.class))
	                loops.add(getRepeat(prcpt).get(0));
	            else if(statementType.equals(Conditional.class))
	                loops.add(getConditional(prcpt).get(0));
	            else if(statementType.equals(While.class))
	                loops.add(getWhile(prcpt).get(0));
	            else
	                loops.pop();
            } else
                loops.pop();
        }
    }
    
}
