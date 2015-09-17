/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.evolution.agent.teseo.statement;

import evolutionaryagent.evolution.agent.teseo.AgentSquareData;
import evolutionaryagent.evolution.agent.teseo.TeseoMemory;
import evolutionaryagent.evolution.agent.teseo.interpreter.TeseoInterpreter;
import evolutionaryagent.types.statement.Statement;
import unalcol.agents.Percept;

/**
 *
 * @author Camilo Alaguna
 */
public class Conditional implements Statement {
    
    protected TeseoMemory memory;
    protected TeseoInterpreter interpreter;
    protected int start, end;

    public Conditional(TeseoMemory memory, TeseoInterpreter interpreter, int start) {
        this.memory = memory;
        this.interpreter = interpreter;
        this.start = this.end = start;
    }
    
    public boolean isAccommplished(Percept prcpt) {
        ConditionResult result = evaluate(prcpt);
        this.end = result.end;
        return result.result;
    }

    public int getEnd() {
        return this.end;
    }

    @Override
    public Class<Conditional> getType() {
        return Conditional.class;
    }
    
    public ConditionResult evaluate(Percept prcpt) {
        return evaluateCondition(prcpt, this.start);
    }
    
    public ConditionResult evaluateCondition(Percept prcpt, int head) {
        if(areEnoughBits(head, 2)) {
            if(interpreter.getInstructions().get(head))
                if(interpreter.getInstructions().get(head + 1))
                    return isPossibleRath(prcpt, head + 2);
                else
                    return areEquals(prcpt, head + 2);
            else
                if(interpreter.getInstructions().get(head + 1))
                    return orOperator(prcpt, head + 2);
                else
                    return andOperator(prcpt, head + 2);
        }
        return new ConditionResult(false, head + 2);
    }
    
    public boolean areEnoughBits(int head, int size) {
        return head + size < interpreter.getInstructions().size();
    }
    
    public ConditionResult isPossibleRath(Percept prcpt, int head) {
        if(areEnoughBits(head, 4)) {
            int numberOfDirections = interpreter.readNumber(head, 4);
            if(areEnoughBits(head + 4, numberOfDirections << 1)) {
                int end = head + 4 + (numberOfDirections << 1);
                return new ConditionResult(arePossibleMovements(head + 4, end), end);
            }
        }
        return new ConditionResult(false, interpreter.getInstructions().size());
    }
    
    public boolean arePossibleMovements(int head, int end) {
    	AgentSquareData position = memory.getActualAgentSquareData();
        for(; head < end; head += 2) {
            position = position.neighbours[getDirection(head)];
            if(position == null)
            	return false;
        }
        return true;
    }
    
    public int getDirection(int head) {
        return (interpreter.getInstructions().get(head)? 2:0)
                + (interpreter.getInstructions().get(head + 1)? 1:0);
    }
    
    public ConditionResult areEquals(Percept prcpt, int head) {
        if(areEnoughBits(head, 4)) {
            for(int i = 0; i < 4; ++i)
                if(interpreter.getPerception(prcpt, i) != interpreter.getInstructions().get(head + i))
                    return new ConditionResult(false, head + 4);
            return new ConditionResult(true, head + 4);
        }
        return new ConditionResult(false, head + 4);
    }
    
    public ConditionResult andOperator(Percept prcpt, int head) {
        ConditionResult left = evaluateCondition(prcpt, head);
        ConditionResult right = evaluateCondition(prcpt, left.end);
        return new ConditionResult(left.result && right.result, right.end);
    }
    
    public ConditionResult orOperator(Percept prcpt, int head) {
        ConditionResult left = evaluateCondition(prcpt, head);
        ConditionResult right = evaluateCondition(prcpt, left.end);
        return new ConditionResult(left.result || right.result, right.end);
    }
    
}
