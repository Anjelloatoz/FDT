package Design_tool_prototype1;

import javax.swing.JFrame;
import bibliothek.gui.DockController;
import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.station.split.SplitDockGrid;

public class HelloDock extends JFrame{
    DockController controller = new DockController();
    SplitDockStation station = new SplitDockStation();


    HelloDock(){
        controller.add(station);
        SplitDockGrid grid = new SplitDockGrid();
        grid.addDockable(0, 0, 2, 1, new DefaultDockable("N"));
        grid.addDockable(0, 1, 1, 1, new DefaultDockable("SW"));
        grid.addDockable(1, 1, 1, 1, new DefaultDockable("SE"));
        
        station.dropTree(grid.toTree());
        this.add(station.getComponent());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(20, 20, 400, 400);
        this.setVisible(true);
    }

    public static void main(String args[]){
        HelloDock hd = new HelloDock();
    }
}
