
import javax.swing.*;
import org.xml.sax.InputSource;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * <h1>RadioModel</h1>
 * Handles heavy work.
 * <p>
 * parser - An XML parser that provides the model with data.
 * <p>
 * radioChannels - An array of channels.
 * <p>
 * currentChannel - The current channel.
 * <p>
 * channelPagesCount - The amount of channel pages.
 * <p>
 * currentTime - The current time as a Calendar.
 * <p>
 * channelAddress - The address to the channels.
 * <p>
 * programsAddress - The address to the programs.
 *
 * @author  Victor Gustafsson, dv16vgn
 * @version 1.0
 * @since   2017-12-03
 */

public class RadioModel {

    private XMLDataParser parser;
    private ArrayList<RadioChannel> radioChannels;
    private RadioChannel currentChannel;
    private String channelPagesCount;
    private Calendar currentTime;
    private String channelsAddress = "http://api.sr.se/api/v2/channels/";
    private String programsAddress = "http://api.sr.se/api/v2/" +
            "scheduledepisodes?";

    /**
     * Standard constructor that creates a RadioModel that downloads
     * channels from the API.
     */

    public RadioModel(){
        parser = new XMLDataParser();
        radioChannels= new ArrayList<RadioChannel>();
        addChannelPagesCount();
        addRadioChannels();
    }

    /**
     * Special constructor that creates an empty RadioModel used for testing.
     * @param i Used to signify that the empty constructor is desired.
     */

    public RadioModel(int i){
        parser = new XMLDataParser();
        radioChannels= new ArrayList<RadioChannel>();
    }

    /**
     * Checks the API for the amount of channel pages.
     */

    private void addChannelPagesCount() {
        try {
            URL url = new URL(channelsAddress);
            InputSource source = new InputSource(url.openStream());
            channelPagesCount = parser.getChannelPagesCountFromSource(source);

        } catch (Exception e) {
            /*Tells the user that there's an issue with the address
            * and quits the program since it needs data from the API.*/
            JOptionPane.showMessageDialog(new JFrame(),
                    "Channel address issue. Download of channel pages " +
                            "count failed. - " + e.getClass().getSimpleName());
            System.exit(1);
        }
    }

    /**
     * Checks the API for the amount of program table pages for
     * the provided channel and puts it in the channel.
     * @param radioChannel The channel to check.
     */

    public void addTablePagesCount(RadioChannel radioChannel){
        try {
            URL url = new URL(programsAddress +
                    "channelid=" + radioChannel.getId());
            InputSource source = new InputSource(url.openStream());
            parser.getChannelTableCountFromSource(source, radioChannel);
        } catch (Exception e) {
            /*Tells the user that there's an issue with the address
            * and quits the program since it needs data from the API.*/
            JOptionPane.showMessageDialog(new JFrame(),
                    "Program address issue. Download of table pages count" +
                            " failed. - " + e.getClass().getSimpleName());
            System.exit(1);
        }
    }

    /**
     * Adds radio channels to the RadioModel.
     */

    private void addRadioChannels(){
        try {
            URL url = new URL(channelsAddress);
            InputSource source = new InputSource(url.openStream());
            parser.getRadioChannelsFromSource(source, radioChannels);
            int i = Integer.parseInt(channelPagesCount);
            int j = 2;
            while(j <= i){
                url = new URL(channelsAddress + "?page=" + j);
                source = new InputSource(url.openStream());
                parser.getRadioChannelsFromSource(source, radioChannels);
                j++;
            }
        } catch (Exception e) {
            /*Tells the user that there's an issue with the address
            * and quits the program since it needs data from the API.*/
            JOptionPane.showMessageDialog(new JFrame(),
                    "Channel address issue. Download of radio channels" +
                            " failed. - " + e.getClass().getSimpleName());
            System.exit(1);
        }
    }

    /**
     * Adds programs surrounding the current time to the provided channel
     * and filters them.
     * @param radioChannel The channel to add programs to.
     */

    public void updateSchedule(RadioChannel radioChannel){
        currentTime = Calendar.getInstance();
        String address = programsAddress + "channelid="
                + radioChannel.getId();
        String date = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        for(int i=0; i<3; i++){
            switch (i){
                case 0:
                    currentTime.add(Calendar.DAY_OF_MONTH, -1);
                    date = df.format(currentTime.getTime());
                    break;
                case 1:
                    currentTime.add(Calendar.DAY_OF_MONTH, 1);
                    date = df.format(currentTime.getTime());
                    break;
                case 2:
                    currentTime.add(Calendar.DAY_OF_MONTH, 1);
                    date = df.format(currentTime.getTime());
                    break;
            }
            addSchedule(address + "&date=" + date, radioChannel);
        }
        Calendar c = Calendar.getInstance();
        radioChannel.filterRadioPrograms(11, c);
        c = Calendar.getInstance();
        radioChannel.markProgram(c);
    }

    /**
     * Adds programs from the provided address to the provided channel.
     * @param address The address to the programs.
     * @param radioChannel the channel to add programs to.
     */

    private void addSchedule(String address, RadioChannel radioChannel){
        try {
            URL url = new URL(address);
            InputSource source = new InputSource(url.openStream());
            parser.getRadioProgramsFromSource(source, radioChannel);
            int i = Integer.parseInt(radioChannel.getTablePageCount());
            int j = 2;
            while(j <= i){
                url = new URL(address + "&page=" + j);
                source = new InputSource(url.openStream());
                parser.getRadioProgramsFromSource(source, radioChannel);
                j++;
            }
        } catch (Exception e) {
            /*Tells the user that there's an issue with the address
            * and quits the program since it needs data from the API.*/
            JOptionPane.showMessageDialog(new JFrame(),
                    "Program address issue. Download of programs" +
                            " failed. - " + e.getClass().getSimpleName());
            System.exit(1);
        }
    }

    /**
     * Getter for the ArrayList of Radio Channels.
     * @return The ArrayList of Radio Channels.
     */

    public ArrayList<RadioChannel> getRadioChannels() {
        return radioChannels;
    }

    /**
     * Gets a channel with the same name as the provided string.
     * @param name The name of the desired channel.
     * @return The desired channel. Returns null if it cannot be found.
     */

    public RadioChannel getRadioChannelByNameOf(String name){
        for(RadioChannel rc : radioChannels){
            if(rc.getName().equals(name)){
                return rc;
            }
        }
        return null;
    }

    public XMLDataParser getParser() {
        return parser;
    }

    /**
     * Setter for the current Radio Channel.
     * @param currentChannel The new current channel.
     */

    public void setCurrentChannel(RadioChannel currentChannel) {
        this.currentChannel = currentChannel;
    }

    /**
     * Getter for the current channel.
     * @return The current channel.
     */

    public RadioChannel getCurrentChannel() {
        return currentChannel;
    }
}
