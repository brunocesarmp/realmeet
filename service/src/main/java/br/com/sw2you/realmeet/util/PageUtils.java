package br.com.sw2you.realmeet.util;

import static java.util.Objects.isNull;
import org.apache.commons.lang3.StringUtils;

import br.com.sw2you.realmeet.exception.InvalidOrderByFieldException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageUtils {

    private PageUtils() {
    }

    public static Pageable newPageable(Integer page, Integer limit, int maxLimit, String orderBy, List<String> validSortFields) {
        int definedPage = isNull(page) ? 0 : page;
        int definedLimit = isNull(limit) ? maxLimit : Math.min(limit, maxLimit);
        Sort definedSort = parseOrderByFields(orderBy, validSortFields);
        return PageRequest.of(definedPage, definedLimit, definedSort);
    }

    private static Sort parseOrderByFields(String orderBy, List<String> validSortFields) {

        if (isNull(validSortFields) || validSortFields.isEmpty()) {
            throw new IllegalArgumentException("No valid sortable fields were defined");
        }

        if (StringUtils.isBlank(orderBy)) {
            return Sort.unsorted();
        }

        return Sort.by(
                Stream
                        .of(orderBy.split(","))
                        .map(value -> {
                                    String fieldName;
                                    Sort.Order order;

                                    if (value.startsWith("-")) {
                                        fieldName = value.substring(1);
                                        order = Sort.Order.desc(fieldName);
                                    } else {
                                        fieldName = value;
                                        order = Sort.Order.asc(fieldName);
                                    }

                                    if (!validSortFields.contains(fieldName)) {
                                        throw new InvalidOrderByFieldException();
                                    }
                                    return order;
                                }

                        ).collect(Collectors.toList())
        );

    }

}
