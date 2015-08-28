/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.types.statement;


/**
 *
 * @author Camilo Alaguna
 */
public class Action implements Statement {
    protected String value;

    public Action(String value) {
         this.value = value;
    }
    
    public String getInstruction() {
        return value;
    }
    
    @Override
    public Class<Action> getType() {
        return Action.class;
    }
    
}
