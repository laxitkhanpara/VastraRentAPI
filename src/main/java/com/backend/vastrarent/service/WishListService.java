package com.backend.vastrarent.service;


import com.backend.vastrarent.dto.WishListDto.*;
import jakarta.transaction.Transactional;

import java.util.List;

public interface WishListService {
    
    @Transactional
    WishListDTO addWish(WishListRequest request);

    @Transactional
    List<WishListDTO> wishByOwner(Long id);

    @Transactional
    List<WishListDTO> wishByProduct(Long id);


    boolean removeWish(Long id);
}
