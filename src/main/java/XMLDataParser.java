import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;

/**
 * <h1>XMLDataParser</h1>
 * Parser of InputSources containing RadioChannel/RadioProgram
 * data in XML format.
 * <p>
 * channelPagesCount - The amount of channel pages.
 * <p>
 * radioChannels - An array of channels to fill.
 * <p>
 *
 * @author  Victor Gustafsson, dv16vgn
 * @version 1.0
 * @since   2017-12-22
 */
public class XMLDataParser {

    private String channelPagesCount;
    private ArrayList<RadioChannel> radioChannels;

    public XMLDataParser (){
        this.channelPagesCount = "";
    }

    /**
     * Sifts through XML from the provided address and handles it depending
     * on type.
     * @param is The input source of XML to be parsed.
     * @param type The type of data to look for. 0 for channel pages,
     *            1 for channels, 2 for programs and 3 for program pages.
     * @param radioChannel Radio channel to add data to for option 2 & 3.
     */

    private void getData(InputSource is, int type, RadioChannel radioChannel){
        String typeName;
        DocumentBuilderFactory dbFactory;
        dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document doc;
        NodeList nList;

        switch (type){
            case 0: typeName = "pagination";
                    break;
            case 1: typeName = "channel";
                    break;
            case 2: typeName = "scheduledepisode";
                    break;
            case 3: typeName = "pagination";
                    break;
            default: System.err.println("Undefined type" +
                    " in XMLDataParser.GetData");
                    return;
        }

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(is);
            doc.getDocumentElement().normalize();
            nList = doc.getElementsByTagName(typeName);

            for (int temp = 0; temp < nList.getLength(); temp++){
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) nNode;
                    switch (type){
                        case 0:
                            handleChannelPageNode(e);
                            return;
                        case 1:
                            handleRadioChannelNode(e);
                            break;
                        case 2:
                            handleRadioProgramNode(e, radioChannel);
                            break;
                        case 3:
                            handleTablePageNode(e, radioChannel);
                            return;
                    }
                }
            }
        } catch (Exception e) {
            /* Catches all exceptions related to parsing
            (ParserConfigurationException, SAXException and IOException)
            , tells the user which one triggered and quits the program,
            since it can't run correctly without parsed data.*/

            JOptionPane.showMessageDialog(new JFrame(),
                    "Parsing failed due to " + e.getClass().getSimpleName());
            System.exit(1);
        }
    }

    /**
     * Reads a node for the amount of channel pages.
     * @param e The node to read.
     */

    private void handleChannelPageNode(Element e){
        this.channelPagesCount = e.getElementsByTagName
                ("totalpages").item(0).getTextContent();
    }

    /**
     * Reads a node for the amount of table pages.
     * @param e The node to read.
     *  @param radioChannel The channel to put the data in.
     */

    private void handleTablePageNode(Element e, RadioChannel radioChannel){
        radioChannel.setTablePageCount(
                e.getElementsByTagName("totalpages")
                        .item(0).getTextContent());
    }

    /**
     * Reads a node for a Radio Channel and adds the new channel to the list.
     * @param e The node to read.
     */

    private void handleRadioChannelNode(Element e){
        RadioChannel rc;
        rc = new RadioChannel(e.getAttribute("name"),
                e.getAttribute("id"));
        if(e.getElementsByTagName("tagline").item(0)!=null){
            rc.setDescription(e.getElementsByTagName("tagline").
                    item(0).getTextContent());
        }
        if (e.getElementsByTagName("image").item(0)!=null){
            rc.setImageAddress(e.getElementsByTagName
                    ("image").item(0).getTextContent());
            rc.downloadImage();
        }
        radioChannels.add(rc);
    }

    /**
     * Reads a node for a Radio Program and adds the
     * new program to the channel.
     * @param e The node to read.
     * @param radioChannel The channel to put the data in.
     */

    private void handleRadioProgramNode(Element e, RadioChannel radioChannel){
        RadioProgram rp;

        rp = new RadioProgram(
                e.getElementsByTagName("title").item(
                        0).getTextContent());
        if (e.getElementsByTagName("description").item(0)!=null){
            rp.setDescription(e.getElementsByTagName("description").item(0)
                    .getTextContent());
        }
        rp.setStartString(e.getElementsByTagName(
                "starttimeutc").item(0).getTextContent());
        rp.setEndString(e.getElementsByTagName(
                "endtimeutc").item(0).getTextContent());

        if (e.getElementsByTagName(
                "imageurl").item(0)!=null){
            rp.setImageAddress(e.getElementsByTagName(
                    "imageurl").item(0).getTextContent());
            rp.downloadImage();
        }
        rp.parseDates();
        radioChannel.addProgram(rp);
    }

    /**
     * Checks the XML for the amount of channel pages.
     * @param inputSource The source of XML.
     * @return The number of pages in String format.
     */

    public String getChannelPagesCountFromSource(InputSource inputSource){
        getData(inputSource, 0, null);
        return channelPagesCount;
    }

    /**
     * Checks the XML for the amount of table pages and puts it in the channel.
     * @param inputSource The source of XML.
     * @param radioChannel The channel to put the data in.
     */

    public void getChannelTableCountFromSource(InputSource inputSource,
                                               RadioChannel radioChannel){
        getData(inputSource, 3, radioChannel);
    }

    /**
     * Checks the XML for Radio Channels and adds them to the provided list.
     * @param inputSource The source of XML.
     * @param list A list of Radio Channels to fill.
     */

    public void getRadioChannelsFromSource(InputSource inputSource,
                                           ArrayList<RadioChannel> list){
        this.radioChannels = list;
        getData(inputSource, 1, null);
    }

    /**
     * Checks the XML for Radio Programs and adds them to the provided channel.
     * @param inputSource The source of XML.
     * @param radioChannel The channel to put the data in.
     */

    public void getRadioProgramsFromSource(InputSource inputSource, RadioChannel radioChannel){
        getData(inputSource, 2, radioChannel);
    }

}
