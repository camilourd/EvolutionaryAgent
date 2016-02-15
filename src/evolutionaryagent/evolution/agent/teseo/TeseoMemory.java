/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.evolution.agent.teseo;

import evolutionaryagent.evolution.agent.Memory;
import evolutionaryagent.types.SparseMatrix;
import unalcol.agents.Percept;
import unalcol.agents.simulate.util.SimpleLanguage;

/**
 *
 * @author Camilo Alaguna
 */
public class TeseoMemory  implements Memory {
    
    protected SparseMatrix<AgentSquareData> mark;
    protected SimpleLanguage language;
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
        mark = new SparseMatrix<AgentSquareData>();
        dir = NORTH;
        dx = new int[4];
        dy = new int[4];
        dx[NORTH] = -1; dx[EAST] = 0; dx[SOUTH] = 1; dx[WEST] =  0;
        dy[NORTH] =  0; dy[EAST] = 1; dy[SOUTH] = 0; dy[WEST] = -1;
        setActualPosition(0, 0);
    }
    
    public void setLanguage(SimpleLanguage language) {
		this.language = language;
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
        return !hasBeenExplored(actX, actY);
    }

    public void saveActualSquareData(Percept prcpt) {
    	AgentSquareData data = new AgentSquareData();
		linkNeighbours(data, prcpt);
		mark.set(actX, actY, data);
	}

	private void linkNeighbours(AgentSquareData agentSquareData, Percept prcpt) {
		for(int i = 0, d = dir; i < 4; ++i, d = (d + 1) % 4) {
			int x = moveX(d), y = moveY(d);
			if((Boolean)prcpt.getAttribute(language.getPercept(d)))
				link(agentSquareData, mark.get(x, y), d);
		}
	}

	private void link(AgentSquareData actual, AgentSquareData neighbour, int dir) {
		try {
			actual.neighbours[dir] = neighbour;
			neighbour.neighbours[(dir + 2) % 4] = actual;
		} catch(NullPointerException e) {}
	}

	public int getDir() {
		return dir;
	}

	public boolean hasBeenExplored(int x, int y) {
        return mark.get(x, y) != null;
    }
    
    public int moveX(int dir) {
        return actX + dx[(this.dir + dir) % 4];
    }
    
    public int moveY(int dir) {
        return actY + dy[(this.dir + dir) % 4];
    }
    
    public AgentSquareData getActualAgentSquareData() {
    	return mark.get(actX, actY);
    }
    
}
