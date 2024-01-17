package auctionstore.service;

import auctionstore.domain.Item;

import java.util.List;

public interface CartService {

    List<Item> getItemsInCart();

    void addItemToCart(Long itemId);

    void removeItemFromCart(Long itemId);
}
