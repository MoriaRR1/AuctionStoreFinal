package auctionstore.controller;

import auctionstore.constants.Pages;
import auctionstore.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ItemService itemService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("items", itemService.getPopularItems());
        return Pages.HOME;
    }
}
