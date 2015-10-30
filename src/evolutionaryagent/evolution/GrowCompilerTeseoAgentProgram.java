/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.evolution;

import evolutionaryagent.evolution.agent.teseo.InterpreterTeseoAgentProgram;
import evolutionaryagent.evolution.agent.teseo.interpreter.BitArrayTeseoInterpreterManager;
import evolutionaryagent.evolution.agent.teseo.interpreter.TeseoInterpreter;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.evolution.GrowingFunction;
import unalcol.types.collection.bitarray.BitArray;

/**
 *
 * @author Camilo Alaguna
 */
public class GrowCompilerTeseoAgentProgram extends GrowingFunction<BitArray, InterpreterTeseoAgentProgram> {
    
    @Override
    public InterpreterTeseoAgentProgram get(BitArray genome) {
        InterpreterTeseoAgentProgram program
                = new InterpreterTeseoAgentProgram(new BitArrayTeseoInterpreterManager(new TeseoInterpreter(genome)));
        program.setLanguage(getLanguage());
        return program;
    }
    
    public SimpleLanguage getLanguage() {
        return new SimpleLanguage( new String[]{"front", "right", "back", "left", "treasure",
                "afront", "aright", "aback", "aleft"},
                new String[]{"no_op", "die", "advance", "rotate"}
            );
    }
    
    @Override
    public BitArray set(InterpreterTeseoAgentProgram thing) {
        return thing.getInstructions();
    }
    
}
