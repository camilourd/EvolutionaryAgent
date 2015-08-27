/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.evolution.agent.teseo;

import evolutionaryagent.evolution.agent.Memory;
import evolutionaryagent.types.BooleanSparseMatrix;

/**
 *
 * @author Camilo Alaguna
 */
public class TeseoMemory  implements Memory {
    
    protected BooleanSparseMatrix mark;
    protected int actX, actY;
    protected int dir;
    protected int [] dx;
    protected int [] dy;
    
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    @Override
    public void init() {
        mark = new BooleanSparseMatrix();
        dir = NORTH;
        dx = new int[4];
        dy = new int[4];
        dx[NORTH] = -1; dx[EAST] = 0; dx[SOUTH] = 1; dx[WEST] =  0;
        dy[NORTH] =  0; dy[EAST] = 1; dy[SOUTH] = 0; dy[WEST] = -1;
        setActualPosition(0, 0);
        mark.set(actX, actY);
    }
    
    public void setActualPosition(int actX, int actY) {
        this.actX = actX;
        this.actY = actY;
    }
    
    public void rotate() {
        dir = (dir + 1) % 4;
    }
    
    public boolean advance() {
        setActualPosition(actX + dx[dir], actY + dy[dir]);
        if(!hasBeenExplored(actX, actY)) {
            mark.set(actX, actY);
            return true;
        }
        return false;
    }

    public boolean hasBeenExplored(int x, int y) {
        return mark.get(x, y);
    }
    
    public int moveX(int dir) {
        return actX + dx[(this.dir + dir) % 4];
    }
    
    public int moveY(int dir) {
        return actY + dy[(this.dir + dir) % 4];
    }
    
}
