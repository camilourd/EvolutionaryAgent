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
	    BitArray instructions = new BitArray("110101111111101000110110011001000011111001110101101110001110001010101010100001011111110100100001110100010101101110001011111100110010101010001111011100010110001110000111111110100001010101010101010101000010111111101001000011101000101");

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
