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

    private static final Map<String, Command> COMMANDS;

    static {
        COMMANDS = new HashMap<>();

        COMMANDS.put("mainPage", new MainPageCommand());
        COMMANDS.put("getTrains", new GetTrainsCommand());
        COMMANDS.put("showRoute", new ShowRouteCommand());
        COMMANDS.put("userRegistration", new UserRegistrationCommand());
        COMMANDS.put("userLogin", new UserLoginCommand());
        COMMANDS.put("userLogout", new UserLogoutCommand());
        COMMANDS.put("userEdit", new UserEditCommand());
        COMMANDS.put("ticketPage", new TicketPageCommand());
        COMMANDS.put("buyTicket", new BuyTicketCommand());
        COMMANDS.put("addTrain", new AddTrainCommand());
        COMMANDS.put("deleteTrain", new DeleteTrainCommand());
        COMMANDS.put("editTrain", new EditTrainCommand());
        COMMANDS.put("deleteStationFromTrainRoute", new DeleteStationFromTrainRouteCommand());
        COMMANDS.put("addStationToTrainRoute", new AddStationToTrainRouteCommand());
        COMMANDS.put("editStationDataOnTrainRoute", new EditStationDataOnTrainRouteCommand());
        COMMANDS.put("addStation", new AddStationCommand());
        COMMANDS.put("deleteStation", new DeleteStationCommand());
        COMMANDS.put("editStation", new EditStationCommand());
        COMMANDS.put("addTrainToSchedule", new AddTrainToScheduleCommand());
        COMMANDS.put("deleteTrainFromSchedule", new DeleteTrainFromScheduleCommand());
        COMMANDS.put("changeMainPageLocale", new ChangeMainPageLocaleCommand());
        COMMANDS.put("changeRoutePageLocale", new ChangeRoutePageLocaleCommand());
        COMMANDS.put("changeTicketPageLocale", new ChangeTicketPageLocaleCommand());
        COMMANDS.put("changeSuccessPageLocale", new ChangeSuccessPageLocaleCommand());
        COMMANDS.put("confirmRegistration", new ConfirmRegistrationCommand());
    }

    /**
     * Returns class object that built at command pattern by it string key.
     * @param commandName - string key.
     * @return object implements {@link Command} interface.
     */
    public static Command getCommand(String commandName) {
        return COMMANDS.get(commandName);
    }
}
