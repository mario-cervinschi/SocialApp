package com.example.reteasocialafx.repository;

import com.example.reteasocialafx.domain.Entity;
import com.example.reteasocialafx.util.paging.Pageable;
import com.example.reteasocialafx.util.paging.Page;

public interface PagingRepository<ID , E extends Entity<ID>> extends Repository<ID, E> {
    Page<E> findAllFollowingOnPage(Pageable pageable, String uID);
    Page<E> findAllFollowersOnPage(Pageable pageable, String uID);
}

