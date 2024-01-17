package auctionstore.controller;

import auctionstore.constants.Pages;
import auctionstore.constants.PathConstants;
import auctionstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.CART)
public class CartController {

    private final CartService cartService;

    @GetMapping
    public String getCart(Model model) {
        model.addAttribute("items", cartService.getItemsInCart());
        return Pages.CART;
    }

    @PostMapping("/add")
    public String addItemToCart(@RequestParam("itemId") Long itemId) {
        cartService.addItemToCart(itemId);
        return "redirect:" + PathConstants.CART;
    }

    @PostMapping("/remove")
    public String removeItemFromCart(@RequestParam("itemId") Long itemId) {
        cartService.removeItemFromCart(itemId);
        return "redirect:" + PathConstants.CART;
    }
}
