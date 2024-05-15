import javax.swing.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <h1>RadioController</h1>
 * Middleman between the RadioModel and the RadioView.
 * <p>
 * rv - The RadioView.
 * <p>
 * rm - The RadioModel.
 * <p>
 * timer - The update timer. It's meant to go off every hour.
 * <p>
 * currentChannelName - The name of the currently selected channel.
 * <p>
 * comboBoxShouldBeBlocked - Boolean saying if the combo box's actions
 * should be blocked.
 * <p>
 * cellTableShouldBeBlocked - Boolean saying if the table's actions
 * should be blocked.
 * <p>
 * updateIsHappening - Boolean making sure only onleone update can r
 * un at a time.
 * <p>
 * updateChannel - The channel to update.
 * <p>
 * loadingImage - The image used to tell the user the program is loading.
 *
 * @author  Victor Gustafsson, dv16vgn
 * @version 1.0
 * @since   2017-12-03
 */

public class RadioController {
    private RadioView rv;
    private RadioModel rm;
    private Timer timer = new Timer();
    private String currentChannelName;
    private boolean comboBoxShouldBeBlocked;
    private boolean cellTableShouldBeBlocked;
    private boolean updateIsHappening;
    private RadioChannel updateChannel;
    private ImageIcon loadingImage;

    public RadioController(RadioView rv, RadioModel rm) {
        this.rv = rv;
        this.rm = rm;
        timer.scheduleAtFixedRate(new UpdateTask(), 3600000, 3600000);
        this.currentChannelName = "";
        this.comboBoxShouldBeBlocked = false;
        this.cellTableShouldBeBlocked = false;
        this.updateIsHappening = false;
        this.updateChannel = null;
        this.loadingImage  = new ImageIcon(
                "src/main/resources/loading.png");
        this.rv.updateComboBoxContent(rm.getRadioChannels());
        this.rv.addMouseListenerToTable(new CellListener());
        this.rv.addClickListenerToMenuItem(0, new MenuItemListener());
        this.rv.addClickListenerToMenuItem(1, new MenuItemListener());
        this.rv.addComboBoxItemListener(new ComboItemListener());

    }

    /**
     * Listener for the table cells that displays radio program information.
     */

    class CellListener extends MouseAdapter {
        String pressedProgram;
        RadioProgram rp;

        public void mousePressed(MouseEvent e){
            if(!cellTableShouldBeBlocked){
                pressedProgram = rv.getTable().getModel().getValueAt
                        (rv.getTable().getSelectedRow(), 0).toString();
                new CellWorker().execute();
            }

        }

        class CellWorker extends SwingWorker{
            @Override
            protected Integer doInBackground(){
                rp = rm.getCurrentChannel().
                        getRadioProgramNamed(pressedProgram);
                return 0;
            }
            protected void done(){
                rv.changeTextAreaText(rp.getDescription());
                rv.changeImage(rp.getImage());
            }
        }
    }

    /**
     * Listener for the menu that either displays the about screen or updates
     * the table and resets the timer.
     */

    class MenuItemListener implements ActionListener{

        public void actionPerformed(ActionEvent actionEvent){
            String s = actionEvent.getActionCommand();
            if(s.equals("About")){
                JOptionPane.showMessageDialog(null, "RadioInfo - " +
                        "By Victor Gustafsson.");
            }else if (s.equals("Update") && !updateIsHappening){

                Thread t = new Thread(new Runnable() {
                    public void run() {
                        update();
                    }
                });
                t.start();

                timer.cancel();
                timer = new Timer();
                timer.scheduleAtFixedRate(new UpdateTask(), 3600000, 3600000);
                updateIsHappening = false;
            }
        }
    }

    /**
     * Listener for the combo box that downloads table data if necessary
     * and then swaps to the desired channel.
     */

    class ComboItemListener implements ItemListener {

        public void itemStateChanged(ItemEvent itemEvent) {
            if (itemEvent.getStateChange() == ItemEvent.SELECTED &&
                    !comboBoxShouldBeBlocked) {

                Object item = itemEvent.getItem();
                currentChannelName = item.toString();
                cellTableShouldBeBlocked = true;
                if(currentChannelName.equals("")){
                    rv.updateProgramTable(null);
                    rv.changeImage(null);
                    rv.changeTextAreaText("");
                }else{
                    rv.changeImage(loadingImage);
                    rv.changeTextAreaText("");
                    new ComboWorker().execute();
                }
            }
        }

        class ComboWorker extends SwingWorker<Integer, Integer> {

            @Override
            protected Integer doInBackground() {
                rm.setCurrentChannel(rm.getRadioChannelByNameOf(
                        currentChannelName));
                if(rm.getCurrentChannel().getPrograms().size()==0){
                    rm.addTablePagesCount(rm.getCurrentChannel());
                    rm.updateSchedule(rm.getCurrentChannel());
                }
                return 0;
            }

            protected void done(){
                if(!currentChannelName.equals("")){
                    rv.changeImage(rm.getCurrentChannel().getImage());
                    if(rm.getCurrentChannel().hasDescription()){
                        rv.changeTextAreaText(rm.getCurrentChannel().
                                getDescription());
                    }
                    rv.updateProgramTable(rm.getCurrentChannel().
                            getPrograms());
                    rv.setMarkedRow(rm.getCurrentChannel().
                            getMarkedProgram());
                }
                cellTableShouldBeBlocked = false;
            }
        }
    }

    /**
     * A timerTask that runs an update.
     */

    class UpdateTask extends TimerTask {
        public void run() {
            update();
        }
    }

    /**
     * Scraps all table data and updates the current channel. Blocks the
     * combo box and table so they can't be triggered during an update. If the
     * empty channel is chosen, then nothing is actually updated, only cleared.
     */

    public void update(){
        comboBoxShouldBeBlocked = true;
        cellTableShouldBeBlocked = true;
        updateIsHappening = true;
        for (RadioChannel rc : rm.getRadioChannels()){
            rc.getPrograms().clear();
        }
        if(!currentChannelName.equals("")){
            updateChannel = rm.getRadioChannelByNameOf(currentChannelName);
            rm.setCurrentChannel(updateChannel);
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        rv.getComboBox().setSelectedItem(currentChannelName);
                        if(rm.getCurrentChannel().hasDescription()){
                            rv.changeTextAreaText(rm.getCurrentChannel().
                                    getDescription());
                        }else{
                            rv.changeTextAreaText("");
                        }

                        rv.changeImage(rm.getCurrentChannel().getImage());
                    }
                });
            } catch (Exception e) {
            /*Tells the user that there's an issue with the update*/
                JOptionPane.showMessageDialog(new JFrame(),
                        "Update failed. Please restart the program if the" +
                                " problem persists." +
                                "- " + e.getClass().getSimpleName());
            }
            rm.addTablePagesCount(updateChannel);
            rm.updateSchedule(updateChannel);
            rv.updateProgramTable(rm.getCurrentChannel().getPrograms());
        }
        comboBoxShouldBeBlocked = false;
        cellTableShouldBeBlocked = false;
        updateIsHappening = false;
    }

}



