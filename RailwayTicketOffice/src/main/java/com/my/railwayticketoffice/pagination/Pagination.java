package com.my.railwayticketoffice.pagination;

import com.my.railwayticketoffice.entity.Train;

import java.util.List;

/**
 * Interface that provides a method that do pagination.
 */
public interface Pagination {

    /**
     * Method that do pagination.
     * @param trains - list of {@link Train}.
     * @param currentPage - current page.
     * @return - list of {@link Train} that are represented on current page.
     */
    List<Train> paginate(List<Train> trains, int currentPage);
}
