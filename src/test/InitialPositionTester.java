package test;

import evolutionaryagent.evolution.agent.teseo.InterpreterTeseoAgentProgram;
import evolutionaryagent.evolution.agent.teseo.interpreter.BitArrayTeseoInterpreterManager;
import evolutionaryagent.evolution.agent.teseo.interpreter.TeseoInterpreter;
import unalcol.agents.Agent;
import unalcol.agents.examples.labyrinth.Labyrinth;
import unalcol.agents.examples.labyrinth.LabyrinthDrawer;
import unalcol.agents.examples.labyrinth.teseo.TeseoMainFrame;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.bitarray.BitArray;

public class InitialPositionTester {
	
	private static SimpleLanguage getLanguage(){
	    return  new SimpleLanguage( new String[]{"front", "right", "back", "left", "exit",
	        "afront", "aright", "aback", "aleft"},
	    		new String[]{"no_op", "die", "advance", "rotate"});
	}
	
	public static void main( String[] argv ){
	    BitArray instructions = new BitArray("1101111110110110001001111110110000001000000001111111111110011000111110011011111111111101010101010100110110001101101010110110111011111111111000110001010000010111111101110100111000001111101010011011101111101010101001111101101100010011111111010000010100001101101110010110110001001111110100100010010000001000110101");

	    System.out.println(instructions.size());
	    InterpreterTeseoAgentProgram program
	                = new InterpreterTeseoAgentProgram(
	                		new BitArrayTeseoInterpreterManager(
	                				new TeseoInterpreter(instructions)));

	    program.setLanguage(getLanguage());
	    program.init();
	    LabyrinthDrawer.DRAW_AREA_SIZE = 600;
	    LabyrinthDrawer.CELL_SIZE = 40;
	    Labyrinth.DEFAULT_SIZE = 15;
	    Agent agent = new Agent( program );
	    TeseoMainFrame frame = new TeseoMainFrame( agent, getLanguage() );
	    frame.setVisible(true);
	  }
	
}
