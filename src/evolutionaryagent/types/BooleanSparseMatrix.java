/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.types;

import unalcol.types.collection.vector.sparse.SparseVector;

/**
 *
 * @author Camilo Alaguna
 */
public class BooleanSparseMatrix {
    SparseVector<SparseVector<Boolean>> matrix;

    public BooleanSparseMatrix() {
        matrix = new SparseVector<SparseVector<Boolean>>();
    }

    public boolean set(int xs, int ys) {
        try {
            return matrix.get(xs).set(ys, true);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            matrix.set(xs, new SparseVector<Boolean>());
            return matrix.get(xs).set(ys, true);
        }
    }

    public boolean del(int xs, int ys) {
        try {
            return matrix.get(xs).del(ys);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            return true;
        }
    }

    public Boolean get(int xs, int ys) {
        try {
            return matrix.get(xs).get(ys);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }
}
