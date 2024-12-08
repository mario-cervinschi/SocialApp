package com.example.reteasocialafx.repository;

import com.example.reteasocialafx.domain.Prietenie;
import com.example.reteasocialafx.domain.Utilizator;
import com.example.reteasocialafx.util.paging.Page;
import com.example.reteasocialafx.util.paging.Pageable;

import java.util.UUID;

public interface FriendshipRepository extends PagingRepository<UUID, Prietenie> {
    //List<Integer> getYears();

    Page<Prietenie> findAllFollowingOnPage(Pageable pageable, String uID);
    Page<Prietenie> findAllFollowersOnPage(Pageable pageable, String uID);
}