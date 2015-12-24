package test;

import evolutionaryagent.types.Pair;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.list.Stack;

public class Translation {

	public static int OPERATION = 0;
	public static int CONDITION = 1;
	public static int REPETITION = 2;
	public static int WHILE = 3;
	public static int END = 4;
	
	public static void main(String[] args) {
		BitArray instructions = new BitArray("1101111110110110001001111110110000001000000001111111111110011000111110011011111111111101010101010100110110001101101010110110111011111111111000110001010000010111111101110100111000001111101010011011101111101010101001111101101100010011111111010000010100001101101110010110110001001111110100100010010000001000110101");
		
		String[] statements = {
				"conditional", "operation", "end", "while",
				"operation", "end", "repeat", "operation"};
		String[] movements = {"front", "right", "back", "left"};
		
		Stack<Integer> states = new Stack<Integer>();
		int p = 0;
		
		while(p + 3 <= instructions.size()) {
			BitArray statement = instructions.subBitArray(p, p + 3);
			int st = getNumber(statement);
			p += 3;
			states.add(getType(st));
			int spaces = (states.get() == END)? states.size() : states.size() + 1;
			System.out.println(" " + statement + spaces(spaces) + statements[st]);
			
			if(states.get() == OPERATION) {
				if(p + 2 <= instructions.size()) {
					BitArray operation = instructions.subBitArray(p, p + 2);
					System.out.println("  " + operation + spaces(states.size() + 2) + movements[getNumber(operation)]);
				}
				p += 2;
				states.del();
			} else if(states.get() == END) {
				for(int i = 0; i < 2; ++i)
					if(states.size() > 0)
						states.del();
			} else if(states.get() == REPETITION) {
				if(p + 4 <= instructions.size()) {
					BitArray times = instructions.subBitArray(p, p + 4);
					System.out.println(times + spaces(states.size() + 1) + getNumber(times) + " times");
				}
				p += 4;
			} else
				p = printCondition(p, instructions, states.size() + 2);
		}
	}
	
	public static int AND = 0;
	public static int OR = 1;
	public static int EQUALS = 2;
	public static int POSSPATH = 3;
	
	private static int printCondition(int p, BitArray instructions, int spaces) {
		Stack<Pair<BitArray, Integer>> acc = new Stack<Pair<BitArray, Integer>>();
		Pair<BitArray, Integer> node = null;
		
		if(p + 2 <= instructions.size()) {
			node = new Pair<BitArray, Integer>(
				instructions.subBitArray(p, p + 2),
				0);
			acc.add(node);
		}
		p += 2;
		
		String[] conditionFunctions = {"and", "or", "perceptionsAreEqualsTo", "isPossiblePath"};
		String[] movements = {"front", "right", "back", "left"};
		
		while(acc.size() > 0) {
			node = acc.get();
			acc.del();
			int type = getNumber(node.first);
			
			if(type < EQUALS && node.second == 0) {
				System.out.println("  " + node.first + spaces(spaces + acc.size()) 
					+ conditionFunctions[type]
				);
				if(acc.size() > 0)
					acc.get().second++;
			} else if(type == EQUALS) {
				System.out.println("  " + node.first + spaces(spaces + acc.size())
					+ conditionFunctions[type]
				);
				if(p + 4 <= instructions.size()) {
					BitArray prcpt = instructions.subBitArray(p, p + 4);
					System.out.println(prcpt + spaces(spaces + acc.size() + 1)
						+ '[' + prcpt.get(0) + ',' + prcpt.get(1) + ','
						+ prcpt.get(2) + ',' + prcpt.get(3) + ']'
					);
				}
				p += 4;
				if(acc.size() > 0)
					acc.get().second++;
			} else if(type == POSSPATH) {
				System.out.println("  " + node.first + spaces(spaces + acc.size())
					+ conditionFunctions[type]
				);
				
				BitArray number = new BitArray("0");
				if(p + 4 <= instructions.size()) {
					number = instructions.subBitArray(p, p + 4);
					System.out.println(number + spaces(spaces + acc.size() + 1)
						+ getNumber(number) + " movements"
				    );
				}
				p += 4;
				
				int n = getNumber(number);
				if(p + (n<<1) <= instructions.size()) {
					for(int i = 0; i < n; ++i) {
						BitArray movement = instructions.subBitArray(p, p + 2);
						System.out.println("  " + movement + spaces(spaces + acc.size() + 2)
							+ movements[getNumber(movement)]
						);
						p += 2;
					}
				} else {
					p += n<<1;
				}
				if(acc.size() > 0)
					acc.get().second++;
			}
			
			if(type < EQUALS && node.second <= 2) {
				acc.add(new Pair<BitArray, Integer>(node.first, node.second + 1));
				if(p + 2 <= instructions.size()) {
					node = new Pair<BitArray, Integer>(
						instructions.subBitArray(p, p + 2),
						0);
					acc.add(node);
				}
				p += 2;
			}
		}
		return p;
	}

	public static int getType(int st) {
		if(st == 0) return CONDITION;
		if(st == 6) return REPETITION;
		if(st == 3) return WHILE;
		if(st == 2 || st == 5) return END;
		return OPERATION;
	}
	
	public static int getNumber(BitArray number) {
		int n = 0;
		for(int i = 0; i < number.size(); ++i)
			n = (n << 1) + ((number.get(i))? 1:0);
		return n;
	}
	
	public static String spaces(int number) {
		String output = "";
		for(int i = 0; i <= (number<<1); ++i)
			output += ' ';
		return output;
	}

}
