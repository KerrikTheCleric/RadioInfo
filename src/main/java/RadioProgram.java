import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * <h1>RadioProgram</h1>
 * Representation of a radio program.
 * <p>
 * name - The name of the program.
 * <p>
 * description - The description of the program.
 * <p>
 * imageAddress - The address of the program's image.
 * <p>
 * startString - String version of the program's start time.
 * <p>
 * endString - String version of the program's end time.
 * <p>
 * startTime - Calendar version of the program's start time.
 * <p>
 * endTime - Calendar version of the program's end time.
 * <p>
 * image - The image of the program.
 *
 * @author  Victor Gustafsson, dv16vgn
 * @version 1.0
 * @since   2017-12-03
 */

public class RadioProgram {
    private String name;
    private String description;
    private String imageAddress;
    private String startString;
    private String endString;
    private Calendar startTime;
    private Calendar endTime;
    private ImageIcon image;

    public RadioProgram(String name){
        this.name = name;
        this.description = null;
        this.imageAddress = null;
        this.startString = null;
        this.endString = null;
        this.startTime = Calendar.getInstance();
        this.startTime.set(Calendar.MILLISECOND, 0);
        this.endTime = Calendar.getInstance();
        this.endTime.set(Calendar.MILLISECOND, 0);
        image = null;
    }

    /**
     * Parses the string versions of the start and end times into a Calendar
     * each.
     */

    public void parseDates(){
        String[] startPieces = startString.split("T");
        parseYMD(startPieces[0], startTime);
        parseHMS(startPieces[1], startTime);

        String[] endPieces = endString.split("T");
        parseYMD(endPieces[0], endTime);
        parseHMS(endPieces[1], endTime);
    }

    /**
     * Parses the Year, Month and Days of the string into the Calendar.
     * @param ymd The string to be parsed.
     * @param c The Calendar that will contain the parsed data.
     */

    private void parseYMD(String ymd, Calendar c){
        String[] pieces = ymd.split("-");
        int year = Integer.parseInt(pieces[0]);
        int month = Integer.parseInt(pieces[1]);
        int day = Integer.parseInt(pieces[2]);
        month--;

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
    }

    /**
     * Parses the Hour, Minute and Second of the string into the Calendar.
     * @param hms The string to be parsed.
     * @param c The Calendar that will contain the parsed data.
     */

    private void parseHMS(String hms, Calendar c){
        String[] pieces = hms.split(":");

        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(pieces[0]));
        c.set(Calendar.MINUTE, Integer.parseInt(pieces[1]));
        pieces[2] = pieces[2].substring(0, pieces[2].length() - 1);
        c.set(Calendar.SECOND, Integer.parseInt(pieces[2]));
    }

    /**
     * Getter for the name of the program.
     * @return The name of the program.
     */

    public String getName(){
        return name;
    }

    /**
     * Setter for the description of the program.
     * @param description The new description of the program.
     */

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for the description of the program.
     * @return The description of the program.
     */

    public String getDescription() {
        return description;
    }

    /**
     * Setter for the image address of the program.
     * @param imageAddress The new image address.
     */

    public void setImageAddress(String imageAddress) {
        this.imageAddress = imageAddress;
    }

    /**
     * Setter for the start string of the program.
     * @param startString The new start string of the program.
     */

    public void setStartString(String startString) {
        this.startString = startString;
    }

    /**
     * Setter for the start string of the program.
     * @param endString The new end string of the program.
     */

    public void setEndString(String endString) {
        this.endString = endString;
    }

    /**
     * Getter for the start time as a Calendar.
     * @return The start time as a Calendar.
     */

    public Calendar getStartTime() {
        return startTime;
    }

    /**
     * Getter for the end time as a Calendar.
     * @return The end time as a Calendar.
     */

    public Calendar getEndTime() {
        return endTime;
    }

    /**
     * Special getter that gets a formatted version of the start time.
     * @return A formatted version of the start time.
     */

    public String getFormattedStartTime(){
        DateFormat df = new SimpleDateFormat("MM/dd | HH:mm:ss");
        return (df.format(startTime.getTime()));
    }

    /**
     * Special getter that gets a formatted version of the end time.
     * @return A formatted version of the end time.
     */

    public String getFormattedEndTime(){
        DateFormat df = new SimpleDateFormat("MM/dd | HH:mm:ss");
        return (df.format(endTime.getTime()));
    }

    /**
     * Downloads an image and sets it to the program if the address is legit.
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
     * Getter for the program image.
     * @return The program image.
     */

    public ImageIcon getImage() {
        return image;
    }
}
