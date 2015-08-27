/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EvolutionaryAgent.evolution.agent;

import unalcol.agents.AgentProgram;
import unalcol.agents.simulate.util.SimpleLanguage;

/**
 *
 * @author Camilo Alaguna
 */
public abstract class EvolutionaryAgentProgram implements AgentProgram {
    
    protected SimpleLanguage language;
    protected double fitness;
    
    public EvolutionaryAgentProgram() {
        this.fitness = 0.0;
    }
    
    public void setLanguage(  SimpleLanguage language ){
        language = language;
    }
    
    public double getFitness() {
        return this.fitness;
    }
    
    public abstract String toString();
    
}
