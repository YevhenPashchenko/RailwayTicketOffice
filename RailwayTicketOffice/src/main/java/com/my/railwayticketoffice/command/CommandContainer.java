package com.my.railwayticketoffice.command;

import java.util.HashMap;
import java.util.Map;


/**
 * Class that contains map in which collect all classes objects that built at command pattern
 * and their string keys for their call.
 *
 * @author Yevhen Pashchenko
 */
public class CommandContainer {

    private static final Map<String, Command> commands;

    static {
        commands = new HashMap<>();
    }

    /**
     * Returns class object that built at command pattern by it string key.
     * @param commandName - string key.
     * @return object implements {@link Command} interface.
     */
    public static Command getCommand(String commandName) {
        return commands.get(commandName);
    }
}
