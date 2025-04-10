package com.backend.vastrarent.service.impl;

import com.backend.vastrarent.dto.WishListDto.*;
import com.backend.vastrarent.mapper.WishListMapper;
import com.backend.vastrarent.model.WishList;
import com.backend.vastrarent.repository.WishListRepository;
import com.backend.vastrarent.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListServiceImple implements WishListService {

    private final WishListRepository wishListRepository;
    @Override
    public WishListDTO addWish(WishListRequest request) {
        WishList wish = WishList.builder()
                            .id(request.getId())
                            .user(request.getUser())
                            .product(request.getProduct()).build();
        wishListRepository.save(wish);
        return WishListMapper.todto(wish);

    }

    @Override
    public List<WishListDTO> wishByOwner(Long id) {
        List<WishList> wishList = wishListRepository.findByUser_Id(id);

        return wishList.stream().map(WishListMapper ::todto).collect(Collectors.toList());
    }

    @Override
    public List<WishListDTO> wishByProduct(Long id) {
        List<WishList> wish = wishListRepository.findByProduct_Id(id);
        return wish.stream().map(WishListMapper ::todto).collect(Collectors.toList());
    }

    @Override
    public boolean removeWish(Long id) {
        if (!wishListRepository.existsById(id)) {
            return false;
        }
        wishListRepository.deleteById(id);
        return true;
    }
}
