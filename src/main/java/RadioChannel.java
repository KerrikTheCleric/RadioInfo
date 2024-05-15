import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * <h1>RadioChannel</h1>
 * Representation of a radio channel.
 * <p>
 * name - The name of the channel.
 * <p>
 * id - The id of the channel.
 * <p>
 * tablePageCount - The amount of program table pages the channel has.
 * <p>
 * imageAddress - The address to the channel's image.
 * <p>
 * programs - An array of programs belonging to the channel.
 * <p>
 * markedProgram - The number of the most recently expired program.
 * <p>
 * image - The image of the channel.
 *
 * @author  Victor Gustafsson, dv16vgn
 * @version 1.0
 * @since   2017-12-03
 */

public class RadioChannel {
    private String name;
    private String id;
    private String tablePageCount;
    private String imageAddress;
    private String description;
    private ArrayList<RadioProgram> programs;
    private int markedProgram;
    private ImageIcon image;

    RadioChannel(String name, String id){
        this.name = name;
        this.id = id;
        this.tablePageCount = null;
        this.description = null;
        this.programs = new ArrayList<RadioProgram>();
        this.image = null;
    }

    /**
     * Gets a program with the same name as the provided string.
     * @param name The name of the desired program.
     * @return The desired program. Returns null if it cannot be found.
     */

    public RadioProgram getRadioProgramNamed(String name){
        for(RadioProgram rc: programs){
            if(rc.getName().equals(name)){
                return rc;
            }
        }
        return null;
    }

    /**
     * Filters out programs that are outside of the time span when compared to
     * currentTime.
     * @param timeSpan The amount of hours + 1 back in time and forward in
     *                time a program is allowed to be.
     * @param currentTime The current time to compare the programs to.
     */

    public void filterRadioPrograms(int timeSpan, Calendar currentTime){
        int i = 0;
        while (i<this.programs.size()){
            RadioProgram rp = this.programs.get(i);
            long hours = ChronoUnit.HOURS.between(currentTime.toInstant(),
                    rp.getStartTime().toInstant());
            if(hours < (-(timeSpan+1)) || hours > timeSpan){
                this.programs.remove(i);
            }else{
                i++;
            }
        }
    }

    /**
     * Marks the most recently ended program.
     * @param currentTime The current time to compare the programs to.
     */

    public void markProgram(Calendar currentTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd | HH:mm:ss");
        Date date1;
        Date date2;
        int i = 0;

        try {
            date1 = sdf.parse(currentTime.get(Calendar.YEAR) + "-" +
                    currentTime.get(Calendar.MONTH)  + "-" +
                    currentTime.get(Calendar.DAY_OF_MONTH) + " | " +
                    currentTime.get(Calendar.HOUR_OF_DAY) + ":" +
                    currentTime.get(Calendar.MINUTE) + ":" +
                    currentTime.get(Calendar.SECOND));
            while(i<this.programs.size()){
                RadioProgram rp = this.programs.get(i);
                Calendar c2 = rp.getEndTime();
                date2 = sdf.parse(c2.get(Calendar.YEAR) + "-" +
                        c2.get(Calendar.MONTH)  + "-" +
                        c2.get(Calendar.DAY_OF_MONTH) + " | " +
                        c2.get(Calendar.HOUR_OF_DAY) + ":" +
                        c2.get(Calendar.MINUTE) + ":" +
                        c2.get(Calendar.SECOND));
                if(date1.compareTo(date2) > 0){
                    this.markedProgram = i;
                }
                i++;
            }
        } catch (ParseException e) {
            /* Tells the user of the exception and sets the marked program
            to 0.
            */

            JOptionPane.showMessageDialog(new JFrame(),
                    "Failed to mark past programs.");
            this.markedProgram = 0;
        }
    }

    /**
     * Setter for the image address of the channel.
     * @param imageAddress The new image address.
     */

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    /**
     * Getter for the marked program.
     * @return The marked program.
     */

    public int getMarkedProgram() {
        return markedProgram;
    }

    /**
     * Setter for the channel description.
     * @param description The new channel description.
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the channel description.
     * @return The channel description.
     */

    public String getDescription() {
        return description;
    }

    /**
     * Getter for the name of the channel.
     * @return The name of the channel.
     */

    public String getName(){
        return name;
    }

    /**
     * Getter for the id of the channel.
     * @return The id of the channel.
     */

    public String getId() {
        return id;
    }

    /**
     * Adds a program to the channel.
     * @param rp The program to be added.
     */

    public void addProgram(RadioProgram rp){
        programs.add(rp);
    }

    /**
     * Getter for the programs of the channel.
     * @return The programs of the channel.
     */

    public ArrayList<RadioProgram> getPrograms() {
        return programs;
    }

    /**
     * Setter for the amount of program pages.
     * @param tablePageCount The amount of program pages.
     */

    public void setTablePageCount(String tablePageCount) {
        this.tablePageCount = tablePageCount;
    }

    /**
     * Getter for the amount of program pages.
     * @return The amount of program pages.
     */

    public String getTablePageCount() {
        return tablePageCount;
    }

    /**
     * Downloads an image and sets it to the channel if the address is legit.
     */

    public void downloadImage() {
        if(imageAddress != null){
            try {
                URL address = new URL(imageAddress);
                image = new ImageIcon(address);
            } catch (MalformedURLException e) {
                /*Do nothing, as a null image will be replaced by
                 the default image in the view.*/
            }
        }
    }

    /**
     * Getter for the channel image.
     * @return The channel image.
     */

    public ImageIcon getImage() {
        return image;
    }

    /**
     * Checks if the channel has a description.
     * @return True if there is a description, else returns false.
     */

    public boolean hasDescription(){
        if(description!=null){
            return true;
        }
        return false;
    }
}
