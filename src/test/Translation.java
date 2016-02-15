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
		BitArray instructions = new BitArray("11001010011111010101000100111110101011110110010010011111010011111100100001011111111010101111011001001001111101001111110010000101111011111111010010001001010011111100100001011110111111110100100010010111100001000101110010001111111010100010100111111111110101101111111001101110100001111111101001111101111101111010101111011001001001111101010100010011111010101111011001010011111111111011001000010100001101001111010011111111010010001001011111111001010101010010111110100000111000101010111110010100111111110111010101000100111110101011110110010100111111111010011111010011111100100001011110111111110100100010010100111111001000010111101111111101001000100101111000010001011100100011111110101000101001111111111101011011111110011011101000011111111010011111011111011111100100101111010100001011111111101101010101011110100100010010111100001000101110010001111111010100010100111111111110101101111111001101110100001111111101001111101111101111010101111011001001001111101010100010011111010101111011001010011111111111011101000010100001101001111010011111111010010001001011111111001010101010010111110100000111000101010111110010100111111110111010101000100111110101011110110010100111111111110111010001111100101111111111011011001111110000111010101110000111111111010000101010001000100101001111110010000101111011111111010010001001011110000100010111001000111111101010001010011111111111010010011111011111011111111111111010100011111111111000100101111000011111100010011111000100111110101011110110111111000011101111111101001000100101111111100101111001011000010100101011111111101111000001011111010010000010101111111110100101010001001111101010111101100101101111001011000010100011011101111111111100101000011010011110100111111110100100010010111111110010101010100101111101000001110001010101111100101001111100011111111100010");
		// 10 locations                       110011011100111111110111100100110011011011011110011101100110010110111101111001110011101100111001111011110010011001110010100111101000110111001110010011001010110110100011001111110111111111010010011101010111001001100111001010010010011111000100101100011100110011000100111010010010001110010011101100010011100111001110110011100111101111001001100111001010011110100011011100110110011001011011110111100101001001001111101101001100001001100

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
