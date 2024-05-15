import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Calendar;
import static org.junit.Assert.*;

/**
 * <h1>RadioChannelTest</h1>
 * <p>
 * rc - Channel to test.
 * rp1 - Program used for tests.
 * rp2 - Program used for tests.
 * rp3 - Program used for tests.
 * c - Calendar used for tests.
 *
 * @author  Victor Gustafsson, dv16vgn
 * @version 1.0
 * @since   2017-12-23
 */
public class RadioChannelTest {

    @Test
    public void getRadioProgramNamedSomethingTest() throws Exception {
        assertEquals("Gusto", rc.getRadioProgramNamed("Gusto").getName());
    }

    @Test
    public void filterRadioProgramsTest() throws Exception {
        rc.filterRadioPrograms(11, c);
        assertEquals(1, rc.getPrograms().size());
    }

    @Test
    public void markProgramTest() throws Exception {
        rc.markProgram(c);
        assertEquals(0, rc.getMarkedProgram());
    }

    @Before
    public void setUp(){
        rc = new RadioChannel("P3", "164");

        rp1 = new RadioProgram("Fiver");
        rp1.setStartString("2017-12-10T05:00:00Z");
        rp1.setEndString("2017-12-10T05:02:00Z");
        rp1.parseDates();

        rp2 = new RadioProgram("Gusto");
        rp2.setStartString("2017-12-10T18:00:00Z");
        rp2.setEndString("2017-12-10T18:02:00Z");
        rp2.parseDates();

        rp3 = new RadioProgram("News");
        rp3.setStartString("2017-12-12T23:00:00Z");
        rp3.setEndString("2017-12-12T23:02:00Z");
        rp3.parseDates();

        rc.addProgram(rp1);
        rc.addProgram(rp2);
        rc.addProgram(rp3);

        c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2017);
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.DAY_OF_MONTH, 10);
        c.set(Calendar.HOUR_OF_DAY, 18);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
    }

    @After
    public void tearDown() throws Exception {
        rc = null;
    }

    RadioChannel rc;
    RadioProgram rp1;
    RadioProgram rp2;
    RadioProgram rp3;
    Calendar c;

}