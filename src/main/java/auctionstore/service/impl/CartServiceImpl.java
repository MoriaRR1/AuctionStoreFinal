package auctionstore.service.impl;

import auctionstore.service.CartService;
import auctionstore.service.UserService;
import auctionstore.domain.Item;
import auctionstore.domain.User;
import auctionstore.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserService userService;
    private final ItemRepository itemRepository;

    @Override
    public List<Item> getItemsInCart() {
        User user = userService.getAuthenticatedUser();
        return user.getItemList();
    }

    @Override
    @Transactional
    public void addItemToCart(Long itemId) {
        User user = userService.getAuthenticatedUser();
        Item item = itemRepository.getOne(itemId);
        user.getItemList().add(item);
    }

    @Override
    @Transactional
    public void removeItemFromCart(Long itemId) {
        User user = userService.getAuthenticatedUser();
        Item item = itemRepository.getOne(itemId);
        user.getItemList().remove(item);
    }
}
