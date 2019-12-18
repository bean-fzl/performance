package com.performance.oa.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.performance.oa.entity.Properties;

public interface PropertiesDao
        extends PagingAndSortingRepository<Properties, Long> {
    Properties findByName(String name);
}
