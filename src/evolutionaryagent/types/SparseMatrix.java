package evolutionaryagent.types;

import unalcol.types.collection.vector.sparse.SparseVector;

public class SparseMatrix<T> {
	
	SparseVector<SparseVector<T>> matrix;
	
	public SparseMatrix() {
        matrix = new SparseVector<SparseVector<T>>();
    }
	
	public T get(int xs, int ys) {
		try {
			return matrix.get(xs).get(ys);
		} catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return null;
        }
    }
	
	public void set(int xs, int ys, T val) {
		if(val != null) {
			try {
	            matrix.get(xs).set(ys, val);
	        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
	            matrix.set(xs, new SparseVector<T>());
	            matrix.get(xs).set(ys, val);
	        }
		}
	}
	
	public boolean del(int xs, int ys) {
		try {
			boolean result = matrix.get(xs).del(ys);
			if(matrix.get(xs).size() == 0)
				matrix.del(xs);
            return result;
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            return true;
        }
	}

}
