package test;

import javax.swing.JFileChooser;

import evolutionaryagent.environtment.SimpleTestArena;
import evolutionaryagent.environtment.TestArena;
import evolutionaryagent.evolution.agent.teseo.InterpreterTeseoAgentProgram;
import evolutionaryagent.evolution.agent.teseo.interpreter.BitArrayTeseoInterpreterManager;
import evolutionaryagent.evolution.agent.teseo.interpreter.TeseoInterpreter;
import evolutionaryagent.types.Pair;
import unalcol.agents.Agent;
import unalcol.agents.examples.labyrinth.Labyrinth;
import unalcol.agents.examples.labyrinth.LabyrinthDrawer;
import unalcol.agents.examples.labyrinth.teseoeater.TeseoEaterLanguage;
import unalcol.agents.simulate.util.SimpleLanguage;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.vector.Vector;

public class MultipositionTester {

	public static void main(String[] args) throws InterruptedException {
		BitArray instructions = new BitArray("11001010011111010101000100111110101011110110010010011111010011111100100001011111111010101111011001001001111101001111110010000101111011111111010010001001010011111100100001011110111111110100100010010111100001000101110010001111111010100010100111111111110101101111111001101110100001111111101001111101111101111010101111011001001001111101010100010011111010101111011001010011111111111011001000010100001101001111010011111111010010001001011111111001010101010010111110100000111000101010111110010100111111110111010101000100111110101011110110010100111111111010011111010011111100100001011110111111110100100010010100111111001000010111101111111101001000100101111000010001011100100011111110101000101001111111111101011011111110011011101000011111111010011111011111011111100100101111010100001011111111101101010101011110100100010010111100001000101110010001111111010100010100111111111110101101111111001101110100001111111101001111101111101111010101111011001001001111101010100010011111010101111011001010011111111111011101000010100001101001111010011111111010010001001011111111001010101010010111110100000111000101010111110010100111111110111010101000100111110101011110110010100111111111110111010001111100101111111111011011001111110000111010101110000111111111010000101010001000100101001111110010000101111011111111010010001001011110000100010111001000111111101010001010011111111111010010011111011111011111111111111010100011111111111000100101111000011111100010011111000100111110101011110110111111000011101111111101001000100101111111100101111001011000010100101011111111101111000001011111010010000010101111111110100101010001001111101010111101100101101111001011000010100011011101111111111100101000011010011110100111111110100100010010111111110010101010100101111101000001110001010101111100101001111100011111111100010");
		JFileChooser file = new JFileChooser( "." );
        file.showOpenDialog(file);
        
        Vector<Pair<Integer, Integer>> initialPositions = new Vector<Pair<Integer, Integer>>();
        
        Evaluator[] threads = new Evaluator[50];
        for(int i = 0; i < threads.length; ++i)
        	threads[i] = null;
        
        for(int i = 0;i < 15;++i)
        	for(int j = 0; j < 15; ++j) {
        		int th = -1;
        		while(th < 0)
        			for(int k = 0; k < threads.length; ++k)
        				if(threads[k] == null || threads[k].state == Evaluator.EVALUATED)
        					th = k;
        		threads[th] = new Evaluator(instructions, initialPositions, file,
        				i, j);
        		threads[th].start();
        	}
        int th = 0;
        do {
        	th = 0;
        	for(int k = 0; k < threads.length; ++k)
				if(threads[k] != null && threads[k].state == Evaluator.EVALUATING)
					th++;
        } while(th > 0);
        System.out.println("Locations solved");
        for(Pair<Integer, Integer> location: initialPositions)
        	System.out.println(location.first + "," + location.second);
	}
	
	public static class Evaluator extends Thread {
		
		protected BitArray instructions;
		protected Vector<Pair<Integer, Integer>> positions;
		public int state = EVALUATING;
		protected JFileChooser file;
		protected int X, Y;
		
		public static final int EVALUATING = 0;
		public static final int EVALUATED = 1;
		
		public Evaluator(BitArray instructions, Vector<Pair<Integer, Integer>> positions,
				JFileChooser file, int X, int Y) {
			this.instructions = (BitArray) instructions.clone();
			this.positions = positions;
			this.file = file;
			this.X = X;
			this.Y = Y;
		}
		
		public synchronized void addPosition(Pair<Integer, Integer> position) {
			positions.add(position);
		}
		
		public TestArena builtArena(Agent agent) throws InterruptedException {
	    	TestArena frame = new SimpleTestArena( agent, getLanguage(), file);
	        LabyrinthDrawer.DRAW_AREA_SIZE = 600;
	        LabyrinthDrawer.CELL_SIZE = 40;
	        Labyrinth.DEFAULT_SIZE = 15;
	        return frame;
	    }

		@Override
		public void run() {
			state = EVALUATING;
			InterpreterTeseoAgentProgram program
	        = new InterpreterTeseoAgentProgram(
	        		new BitArrayTeseoInterpreterManager(
	        				new TeseoInterpreter(instructions)));
	        program.setLanguage(getLanguage());
	        
	        program.init();
    		Agent agent = new Agent(program);
    		try {
    			TestArena arena = builtArena(agent);
        		arena.setVisible(true);
        		arena.changeAgentPosition(X, Y, 0);
				arena.runTest();
				if(program.exit)
	    			addPosition(new Pair<Integer, Integer>(X, Y));
	    		arena.dispose();
			} catch (InterruptedException e) {}
			state = EVALUATED;
		}
		
	}
    
    private static SimpleLanguage getLanguage(){
        return new TeseoEaterLanguage(
            new String[]{"front", "right", "back", "left", "treasure",
                         "resource", "resource-color", "resource-shape", "resource-size",
                         "resource-weight", "resource-type", "energy_level" },
            new String[]{"no_op", "die", "advance", "rotate", "eat"}
        );
    }

}
