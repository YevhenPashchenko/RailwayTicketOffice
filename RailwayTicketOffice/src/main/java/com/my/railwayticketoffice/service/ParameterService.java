package com.my.railwayticketoffice.service;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Interface that provides a method that verify parameters which uses in {@link com.my.railwayticketoffice.command.Command} classes.
 *
 * @author Yevhen Pashchenko
 */
public interface ParameterService<T> {

    /**
     * Verify parameters that contain in map.
     * @param parameters - map that contain parameters which need to verify.
     * @return true if all parameters verified or false if not.
     */
    boolean check(Map<String, T> parameters, HttpSession session);
}
