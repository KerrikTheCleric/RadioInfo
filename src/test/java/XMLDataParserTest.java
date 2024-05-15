import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import java.io.FileInputStream;

/**
 * <h1>XMLDataParserTest</h1>
 * <p>
 * parser - The parser to test.
 * <p>
 * rc - RadioChannel used to house some data for assertions.
 * <p>
 * fis - File stream to be converted to an InputSource.
 * <p>
 * is - Input Source to parse.
 * <p>
 * channelsFile - File path used for channel-related tests.
 * <p>
 * programsFile - File path used for program-related tests.
 * <p>
 *
 * @author  Victor Gustafsson, dv16vgn
 * @version 1.0
 * @since   2017-12-22
 */
public class XMLDataParserTest {

    @Test
    public void parseChannelPagesCountTest() throws Exception {
        String channelPages;
        fis = new FileInputStream(channelsFile);
        is = new InputSource(fis);
        channelPages = parser.getChannelPagesCountFromSource(is);
        Assert.assertEquals("6", channelPages);
    }

    @Test
    public void parseTablePagesCountTest() throws Exception {
        fis = new FileInputStream(programsFile);
        is = new InputSource(fis);
       parser.getChannelTableCountFromSource(is,rc);
        Assert.assertEquals("6",rc.getTablePageCount());
    }

    @Test
    public void parseCorrectAmountOfRadioProgramsTest() throws Exception {
        fis = new FileInputStream(programsFile);
        is = new InputSource(fis);
        parser.getRadioProgramsFromSource(is,rc);
        Assert.assertEquals(10, rc.
                getPrograms().size());
    }

    @Test
    public void parseCorrectRadioProgramTest() throws Exception {
        fis = new FileInputStream(programsFile);
        is = new InputSource(fis);
        parser.getRadioProgramsFromSource(is, rc);
        Assert.assertEquals("Vaken", rc.
                getRadioProgramNamed("Vaken").getName());
    }

    @Before
    public void setUp(){
        parser = new XMLDataParser();
        rc = new RadioChannel("P3", "164");
    }

    @After
    public void tearDown() throws Exception {
        parser = null;
        rc = null;
    }

    private XMLDataParser parser;
    private RadioChannel rc;
    private FileInputStream fis;
    private InputSource is;
    private String channelsFile = "src/main/resources/channels.xml";
    private String programsFile = "src/main/resources/p3programsedit.xml";

}