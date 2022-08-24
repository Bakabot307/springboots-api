package com.shopMe.demo.service;

import com.shopMe.demo.model.User;
import com.shopMe.demo.model.WishList;
import com.shopMe.demo.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class WishListService {
    @Autowired
    private WishListRepository wishListRepository;

    public void createWishlist(WishList wishList) {
        wishListRepository.save(wishList);
    }

    public List<WishList> readWishList(User user) {
        return wishListRepository.findAllByUserOrderByCreatedDateDesc(user);
    }
}
