package com.my.railwayticketoffice.pagination;

import com.my.railwayticketoffice.Util;
import com.my.railwayticketoffice.entity.Train;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that do pagination for main.jsp.
 *
 * @author Yevhen Pashchenko
 */
public class MainPagePagination implements Pagination {

    @Override
    public List<Train> paginate(List<Train> trains, int currentPage) {
        return trains.stream()
                .skip((long) (currentPage - 1) * Util.getNumberTrainOnPage())
                .limit(Util.getNumberTrainOnPage())
                .collect(Collectors.toList());
    }
}
