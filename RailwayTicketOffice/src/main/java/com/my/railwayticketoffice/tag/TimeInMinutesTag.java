package com.my.railwayticketoffice.tag;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.time.LocalTime;

/**
 * Class that create tag that return time in minutes.
 *
 * @author Yevhen Pashchenko
 */
public class TimeInMinutesTag extends SimpleTagSupport {

    private LocalTime time;

    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Create tag that return time in minutes.
     * @throws IOException if {@link IOException} occurs.
     */
    @Override
    public void doTag() throws IOException {
        getJspContext().getOut().print(time.getMinute());
    }
}
