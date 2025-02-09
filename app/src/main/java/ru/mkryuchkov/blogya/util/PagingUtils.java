package ru.mkryuchkov.blogya.util;

import org.springframework.stereotype.Component;

@Component
public class PagingUtils {

    public Integer getOffset(Integer page, Integer pageSize) {
        return page * pageSize;
    }

}
