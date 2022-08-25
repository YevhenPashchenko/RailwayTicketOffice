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

        commands.put("mainPage", new MainPageCommand());
        commands.put("getTrains", new GetTrainsCommand());
        commands.put("showRoute", new ShowRouteCommand());
        commands.put("userRegistration", new UserRegistrationCommand());
        commands.put("userLogin", new UserLoginCommand());
        commands.put("userLogout", new UserLogoutCommand());
        commands.put("userEdit", new UserEditCommand());
        commands.put("ticketPage", new TicketPageCommand());
        commands.put("buyTicket", new BuyTicketCommand());
        commands.put("addTrain", new AddTrainCommand());
        commands.put("deleteTrain", new DeleteTrainCommand());
        commands.put("editTrain", new EditTrainCommand());
        commands.put("deleteStationFromTrainRoute", new DeleteStationFromTrainRouteCommand());
        commands.put("addStationToTrainRoute", new AddStationToTrainRouteCommand());
        commands.put("editStationDataOnTrainRoute", new EditStationDataOnTrainRouteCommand());
        commands.put("addStation", new AddStationCommand());
        commands.put("deleteStation", new DeleteStationCommand());
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
