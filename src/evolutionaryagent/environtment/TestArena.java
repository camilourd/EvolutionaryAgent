/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package evolutionaryagent.environtment;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import unalcol.agents.Action;
import unalcol.agents.Agent;
import unalcol.agents.examples.labyrinth.Labyrinth;
import unalcol.agents.examples.labyrinth.LabyrinthDrawer;
import unalcol.agents.simulate.gui.SimpleView;
import unalcol.agents.simulate.gui.WorkingPanel;
import unalcol.agents.simulate.util.SimpleLanguage;


/**
 *
 * @author Camilo Alaguna
 */
public abstract class TestArena extends JFrame {

    protected SimpleLanguage language = null;

    protected String fileDir = ".";
    protected String fileName = null;
    protected Thread thread = null;
    protected Agent agent;
    protected Labyrinth labyrinth = null;
    protected SimpleView view;
    protected String title = "Labyrinth";
    
    protected GridLayout gridLayout2 = new GridLayout();
    protected WorkingPanel drawArea = new WorkingPanel( new LabyrinthDrawer( ) );
    protected BorderLayout borderLayout2 = new BorderLayout();
    protected JPanel jPanel2 = new JPanel();
    protected BorderLayout borderLayout1 = new BorderLayout();
    
    protected int xi, yi;
    
    private Object lock = new Object();
    
    public TestArena(Agent agent, SimpleLanguage language, JFileChooser file, int xi, int yi) {
        view = new SimpleView( drawArea );
        this.agent = agent;
        this.language = language;
        labyrinth = this.newLabyrinthInstance();
        this.xi = xi;
        this.yi = yi;
        this.initLabyrinth();
        try {
            jbInit();
            fileDir = file.getSelectedFile().getAbsolutePath();
            fileName = file.getSelectedFile().getAbsolutePath();
            loadFile();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public abstract Labyrinth newLabyrinthInstance();
    
    public void initLabyrinth(){
        labyrinth = this.newLabyrinthInstance();
        labyrinth.setAgentPosition( 0, xi, yi, 0);
        labyrinth.setDelay(10);
        drawArea.getDrawer().setEnvironment( labyrinth );
        labyrinth.registerView(view);
    }

    private void jbInit() {
        this.setSize(new Dimension(430, 500));
        this.setTitle(title);
        this.getContentPane().setLayout(borderLayout2);
        jPanel2.setLayout(borderLayout1);

        gridLayout2.setColumns(2);
        gridLayout2.setRows(3);
        
        jPanel2.add(drawArea,  BorderLayout.CENTER);
        this.getContentPane().add(jPanel2,  BorderLayout.CENTER);
        // Closing the window
        this.addWindowListener( new WindowAdapter(){
          public void windowClosing( WindowEvent e ){
            labyrinth.stop();
            thread = null;
            System.exit(0);
          } } );
    }

    protected void loadFile(){
        this.setTitle(title + " ["+fileName+"]");
        this.initLabyrinth();
        labyrinth.load( fileName );
        view();
    }

    public void view(){
        drawArea.update();
    }

    public void runTest() throws InterruptedException {
        synchronized(lock) {
            agent = labyrinth.getAgent();
            changeThreadState();
            while(agent.status != Action.DIE) lock.wait(500);
            changeThreadState();
        }
    }
    
    protected void changeThreadState() {
        if( thread == null ){
          thread = new Thread( labyrinth );
          labyrinth.run();
        }else{
          labyrinth.stop();
          thread = null;
        }
    }
    
}
