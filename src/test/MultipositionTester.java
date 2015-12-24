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
		BitArray instructions = new BitArray("11011101100111111111111001011101111111011100111110111011101100111111111111001111111111110010111011111110111001111101111001011101111111011000010111101111000001001011101110100110000001000010111101111000001001011101110100110000100010111011101011111111001011101111111011000010111101111000000");
		
		JFileChooser file = new JFileChooser( "." );
        file.showOpenDialog(file);
        
        Vector<Pair<Integer, Integer>> initialPositions = new Vector<Pair<Integer, Integer>>();
        
        Evaluator[] threads = new Evaluator[10];
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
