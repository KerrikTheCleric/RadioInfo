import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;

import java.io.FileInputStream;

import static org.junit.Assert.*;

/**
 * <h1>RadioModelTest</h1>
 * <p>
 * m - The RadioModel to test.
 * <p>
 * fis - File stream to be converted to an InputSource.
 * <p>
 * is - Input Source to parse.
 * <p>
 * channelsFile - File path used for channel-related tests.
 * <p>
 *
 * @author  Victor Gustafsson, dv16vgn
 * @version 1.0
 * @since   2017-12-23
 */
public class RadioModelTest {
    @Test
    public void parseCorrectAmountOfRadioChannelsTest() throws Exception {
        fis = new FileInputStream(channelsFile);
        is = new InputSource(fis);
        m.getParser().getRadioChannelsFromSource(is, m.getRadioChannels());
        Assert.assertEquals(10, m.getRadioChannels().size()) ;
    }

    @Test
    public void parseCorrectRadioChannelTest() throws Exception {
        fis = new FileInputStream(channelsFile);
        is = new InputSource(fis);
        m.getParser().getRadioChannelsFromSource(is, m.getRadioChannels());
        Assert.assertEquals("P2", m.getRadioChannelByNameOf("P2").getName());
    }

    @Before
    public void setUp(){
        m = new RadioModel(0);
    }

    @After
    public void tearDown() throws Exception {
        m = null;
    }

    private RadioModel m;
    private FileInputStream fis;
    private InputSource is;
    private String channelsFile = "src/main/resources/channels.xml";
}