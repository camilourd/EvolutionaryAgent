/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.evolution.agent.teseo.interpreter;

import evolutionaryagent.evolution.agent.teseo.TeseoMemory;
import evolutionaryagent.types.interpreter.Interpreter;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.bitarray.BitArray;

/**
 *
 * @author Camilo Alaguna
 */
public abstract class BitArrayTeseoInterpreter implements Interpreter<BitArray> {
    
    protected SimpleLanguage language;
    protected BitArray instructions;
    protected Class statementType = null;
    protected TeseoMemory memory;
    protected int head;

    public BitArrayTeseoInterpreter(BitArray instructions) {
        this.instructions = instructions;
        this.head = 0;
    }
    
    public abstract String getDieAction();
    
    @Override
    public void restart() {
        this.head = 0;
    }
    
    @Override
    public void setLanguage(SimpleLanguage language) {
        this.language = language;
    }
    
    @Override
    public BitArray getInstructions() {
        return this.instructions;
    }
    
    protected boolean isThereEnoughSpace(int size) {
        return (head + size) <= instructions.size();
    }
    
    public int readNumber(int head, int size) {
        int number = 0;
        for(int i = 0; i < size; ++i)
            number = (number << 1) + ((instructions.get(head + i))? 1:0);
        return number;
    }
    
    public boolean getPerception(Percept prcpt, int dir) {
        return ((Boolean)prcpt.getAttribute(language.getPercept(dir)));
    }
    
    public void setMemory(TeseoMemory memory) {
        this.memory = memory;
    }
    
}
