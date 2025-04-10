package com.backend.vastrarent.controller;

import com.backend.vastrarent.dto.WishListDto.*;
import com.backend.vastrarent.service.WishListService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/wishList")
@RequiredArgsConstructor
public class WishController {

    private final WishListService wishListService;

    @GetMapping("byOwner/{id}")
    public ResponseEntity<List<WishListDTO>>  wishListByOwner(@PathVariable Long id){
        return ResponseEntity.ok(wishListService.wishByOwner(id));
    }

    @GetMapping("byProduct/{id}")
    public ResponseEntity<List<WishListDTO>> WishOfProduct(@PathVariable Long id){
        return ResponseEntity.ok(wishListService.wishByProduct(id));
    }

    @PostMapping
    public ResponseEntity<WishListDTO> addWish(@Valid @RequestBody WishListRequest request){
        return new ResponseEntity<>(wishListService.addWish(request), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  removeWish(Long id){
        wishListService.removeWish(id);
        return ResponseEntity.noContent().build();
    }

}
