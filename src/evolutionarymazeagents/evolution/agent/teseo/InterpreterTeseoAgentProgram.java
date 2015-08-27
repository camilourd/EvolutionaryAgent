/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EvolutionaryAgent.evolution.agent.teseo;

import EvolutionaryAgent.evolution.agent.EvolutionaryAgentProgram;
import EvolutionaryAgent.evolution.agent.teseo.interpreter.BitArrayTeseoInterpreter;
import EvolutionaryAgent.evolution.agent.teseo.interpreter.BitArrayTeseoInterpreterManager;
import unalcol.agents.Action;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.bitarray.BitArray;

/**
 *
 * @author Camilo Alaguna
 */
public class InterpreterTeseoAgentProgram extends EvolutionaryAgentProgram {

    protected TeseoMemory memory;
    protected BitArrayTeseoInterpreterManager manager;
    protected int actionsCount;
    protected double discoverFitness = 0.0;
    protected int maxNumberOfActions;
    
    public static final int FRONT = 0;
    public static final int EXIT = 4;
    public static final int DIE = 2;

    public InterpreterTeseoAgentProgram(BitArrayTeseoInterpreterManager manager, int maxNumberOfActions) {
        this.manager = manager;
        this.maxNumberOfActions = maxNumberOfActions;
    }

    @Override
    public void setLanguage(SimpleLanguage language) {
        super.setLanguage(language);
        manager.getInterpreter().setLanguage(language);
    }
    
    @Override
    public Action compute(Percept prcpt) {
        String action = "die";
        if(!isThereEndedCondition(prcpt)) {
            action = getNextAction(prcpt);
            act(action);
        } else if(isInTheExit(prcpt))
            fitness += 1000000.0 - discoverFitness;
        return new Action(action);
    }
    
    private boolean isThereEndedCondition(Percept prcpt) {
        return isInTheExit(prcpt) || !areThereActions();
    }
    
    public boolean isInTheExit(Percept prcpt) {
        return ((BitArrayTeseoInterpreter)manager.getInterpreter()).getPerception(prcpt, EXIT);
    }
    
    public boolean areThereActions() {
        return actionsCount <= maxNumberOfActions;
    }
    
    private String getNextAction(Percept prcpt) {
        String action = "no_op";
        do {
            action = manager.getNextInstruction(prcpt);
        } while(!isValidAction(prcpt, action));
        return action;
    }
    
    protected boolean isValidAction(Percept prcpt, String action) {
        if(action.equals("advance"))
            return !((BitArrayTeseoInterpreter)manager.getInterpreter()).getPerception(prcpt, FRONT);
        return true;
    }
    
    protected void act(String action) {
        if(action.equals("advance"))
            advance();
        else if(action.equals("rotate"))
            rotate();
    }
    
    protected void rotate() {
        memory.rotate();
        fitness -= 0.01;
        ++actionsCount;
    }
    
    protected void advance() {
        if(memory.advance()) {
            fitness += 1.0;
            discoverFitness += 1.0;
        }
        fitness -= 0.01;
        ++actionsCount;
    }
    
    public BitArray getInstructions() {
        return (BitArray) manager.getInstructions();
    }

    @Override
    public void init() {
        actionsCount = 0;
        memory = new TeseoMemory();
        memory.init();
        manager.restart();
        manager.setMemory(memory);
    }

    @Override
    public String toString() {
        return getInstructions().toString();
    }
    
}
