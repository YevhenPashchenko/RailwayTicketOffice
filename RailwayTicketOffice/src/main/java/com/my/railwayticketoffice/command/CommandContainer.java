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

        COMMANDS.put("addCarriageToTrain", new AddCarriageToTrainCommand());
        COMMANDS.put("addCarriageType", new AddCarriageTypeCommand());
        COMMANDS.put("addStation", new AddStationCommand());
        COMMANDS.put("addStationToTrainRoute", new AddStationToTrainRouteCommand());
        COMMANDS.put("addTrain", new AddTrainCommand());
        COMMANDS.put("addTrainToSchedule", new AddTrainToScheduleCommand());
        COMMANDS.put("buyTicket", new BuyTicketCommand());
        COMMANDS.put("changeMainPageLocale", new ChangeMainPageLocaleCommand());
        COMMANDS.put("changeRoutePageLocale", new ChangeRoutePageLocaleCommand());
        COMMANDS.put("changeSeatsPageLocale", new ChangeSeatsPageLocaleCommand());
        COMMANDS.put("changeSuccessPageLocale", new ChangeSuccessPageLocaleCommand());
        COMMANDS.put("changeTicketPageLocale", new ChangeTicketPageLocaleCommand());
        COMMANDS.put("chooseSeatsPage", new ChooseSeatsPageCommand());
        COMMANDS.put("confirmRegistration", new ConfirmRegistrationCommand());
        COMMANDS.put("deleteCarriageFromTrain", new DeleteCarriageFromTrainCommand());
        COMMANDS.put("deleteCarriageType", new DeleteCarriageTypeCommand());
        COMMANDS.put("deleteStation", new DeleteStationCommand());
        COMMANDS.put("deleteStationFromTrainRoute", new DeleteStationFromTrainRouteCommand());
        COMMANDS.put("deleteTrain", new DeleteTrainCommand());
        COMMANDS.put("deleteTrainFromSchedule", new DeleteTrainFromScheduleCommand());
        COMMANDS.put("editCarriageNumberInTrain", new EditCarriageNumberInTrainCommand());
        COMMANDS.put("editCarriageType", new EditCarriageTypeCommand());
        COMMANDS.put("editStation", new EditStationCommand());
        COMMANDS.put("editStationDataOnTrainRoute", new EditStationDataOnTrainRouteCommand());
        COMMANDS.put("editTrain", new EditTrainCommand());
        COMMANDS.put("getTrains", new GetTrainsCommand());
        COMMANDS.put("mainPage", new MainPageCommand());
        COMMANDS.put("showRoute", new ShowRouteCommand());
        COMMANDS.put("ticketPage", new TicketPageCommand());
        COMMANDS.put("userEdit", new UserEditCommand());
        COMMANDS.put("userLogin", new UserLoginCommand());
        COMMANDS.put("userLogout", new UserLogoutCommand());
        COMMANDS.put("userRegistration", new UserRegistrationCommand());
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
