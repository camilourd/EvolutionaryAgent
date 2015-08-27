/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose RandomTool | Templates
 * and open the template in the editor.
 */
package EvolutionaryAgent.operators.binary;

import unalcol.clone.Clone;
import unalcol.optimization.operators.binary.BinaryArityTwo;
import unalcol.random.Random;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author Camilo Alaguna
 */
public class XOver extends BinaryArityTwo {

    public XOver(){}
    
    @Override
    public Vector<BitArray> generates(BitArray parent1, BitArray parent2) {
        try{
            BitArray x = (BitArray) Clone.get(parent1);
            BitArray y = (BitArray) Clone.get(parent2);
            int xoverpoint1 = Random.nextInt(x.size());
            int xoverpoint2 = Random.nextInt(y.size());
            
            BitArray child1 = new BitArray(xoverpoint1 + y.size() - xoverpoint2, false);
            BitArray child2 = new BitArray(xoverpoint2 + x.size() - xoverpoint1, false);
            distribute(x, y, child1, child2, xoverpoint1, xoverpoint2);
            
            Vector<BitArray> ret = new Vector<BitArray>();
            ret.add(new BitArray(child1));
            ret.add(new BitArray(child2));
            return ret;
        } catch(Exception e) { }
        return null;
    }
    
    public void shareOut(BitArray values, BitArray child1, BitArray child2,
            int point1, int point2) {
        for(int i = 0;i < values.size(); ++i) {
            if(i < point1) {
                child1.set(i, values.get(i));
            } else {
                child2.set(point2 + i - point1, values.get(i));;
            }
        }
    }
    
    public void distribute(BitArray x, BitArray y, BitArray child1, BitArray child2,
            int xoverpoint1, int xoverpoint2) {
        shareOut(x, child1, child2, xoverpoint1, xoverpoint2);
        shareOut(y, child2, child1, xoverpoint2, xoverpoint1);
    }
    
}
