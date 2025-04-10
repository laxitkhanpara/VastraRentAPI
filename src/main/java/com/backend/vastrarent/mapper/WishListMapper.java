package com.backend.vastrarent.mapper;

import com.backend.vastrarent.dto.WishListDto.*;
import com.backend.vastrarent.model.WishList;

public class WishListMapper {

    public static WishListDTO todto(WishList wish){
        return WishListDTO.builder()
                .id(wish.getId())
                .product(wish.getProduct())
                .user(wish.getUser())
                .build();
    }
}
