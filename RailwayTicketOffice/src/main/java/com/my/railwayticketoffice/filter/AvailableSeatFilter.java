package com.my.railwayticketoffice.filter;

import com.my.railwayticketoffice.entity.Train;

import java.util.Map;

/**
 * Class that check that the reserved seats on the train are not yet occupied.
 */
public class AvailableSeatFilter implements SeatFilter {

    @Override
    public boolean check(Train train, Map<String, String[]> ticketParameters) {
        String[] carriagesNumbers = ticketParameters.get("carriage");
        String[] carriagesSeats = ticketParameters.get("seat");
        for (int i = 0; i < carriagesNumbers.length; i++) {
            int finalI = i;
            boolean check = train.getCarriages().values().stream()
                    .filter(carriage -> carriage.getNumber() == Integer.parseInt(carriagesNumbers[finalI]))
                    .anyMatch(carriage -> carriage.getSeats().contains(Integer.parseInt(carriagesSeats[finalI])));
            if (!check) {
                return false;
            }
        }
        return true;
    }
}
