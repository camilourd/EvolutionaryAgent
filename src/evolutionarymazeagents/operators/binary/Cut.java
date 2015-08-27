/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose RandomTool | Templates
 * and open the template in the editor.
 */
package EvolutionaryAgent.operators.binary;

import unalcol.clone.Clone;
import unalcol.optimization.operators.binary.BinaryArityOne;
import unalcol.random.Random;
import unalcol.types.collection.bitarray.BitArray;
import unalcol.types.collection.vector.Vector;

/**
 *
 * @author Camilo Alaguna
 */
public class Cut extends BinaryArityOne {

    @Override
    public Vector<BitArray> generates(BitArray parent) {
        try {
            BitArray genome = (BitArray) Clone.get(parent);
            int li = Random.nextInt(parent.size());
            int ls = Random.nextInt(parent.size());

            if(li > ls) {
                int t = li;
                li = ls;
                ls = t;
            }
            
            BitArray child1 = new BitArray(parent.size() - ls + li - 1, false);
            BitArray child2 = new BitArray(ls - li + 1, false);
            
            for(int i = 0; i < genome.size(); ++i)
                if(i < li)
                    child1.set(i, genome.get(i));
                else if(i > ls)
                    child1.set(i - ls - 1, genome.get(i));
                else
                    child2.set(i - li, genome.get(i));
            
            Vector<BitArray> ret = new Vector<BitArray>();
            ret.add(child1);
            ret.add(child2);
            return ret;
        } catch(Exception e) {}
        return null;
    }
    
}
