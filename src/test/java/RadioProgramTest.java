import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Calendar;
import static org.junit.Assert.*;

/**
 * <h1>RadioProgramTest</h1>
 * <p>
 * rp - Program to test.
 * c - Calendar used for tests.
 *
 * @author  Victor Gustafsson, dv16vgn
 * @version 1.0
 * @since   2017-12-23
 */
public class RadioProgramTest {
    @Test
    public void parseDatesCheckStartTimeTest() throws Exception {
        rp.parseDates();
        assertEquals(0, c.compareTo(rp.getStartTime()));
    }

    @Test
    public void parseDatesCheckEndTimeTest() throws Exception {
        rp.parseDates();
        c.set(Calendar.MINUTE, 2);
        assertEquals(0, c.compareTo(rp.getEndTime()));
    }

    @Before
    public void setUp(){
        rp = new RadioProgram("Moogle News");
        rp.setStartString("2017-12-10T05:00:00Z");
        rp.setEndString("2017-12-10T05:02:00Z");

        c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2017);
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.DAY_OF_MONTH, 10);
        c.set(Calendar.HOUR_OF_DAY, 5);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
    }

    @After
    public void tearDown() throws Exception {
        rp = null;
    }

    RadioProgram rp;
    Calendar c;

}