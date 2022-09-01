package com.my.railwayticketoffice.pagination;

import com.my.railwayticketoffice.Util;
import com.my.railwayticketoffice.entity.Train;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for methods from {@link MainPagePagination}.
 *
 * @author Yevhen Pashchenko
 */
public class MainPagePaginationTest {

    /**
     * Test for method paginate from {@link MainPagePagination}.
     */
    @Test
    public void testPaginate() {

        List<Train> trains = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Train train = new Train();
            train.setId(i);
            trains.add(train);
        }

        for (int i = 0; i < Math.ceil((double) trains.size() / Util.getNumberTrainOnPage()); i++) {
            List<Train> paginatedTrains = trains.subList(i * Util.getNumberTrainOnPage(), i * Util.getNumberTrainOnPage() + Util.getNumberTrainOnPage());
            assertEquals(paginatedTrains, new MainPagePagination().paginate(trains, i + 1));
        }
    }
}
