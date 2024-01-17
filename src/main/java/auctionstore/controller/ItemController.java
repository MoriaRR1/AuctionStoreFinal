package auctionstore.controller;

import auctionstore.constants.Pages;
import auctionstore.constants.PathConstants;
import auctionstore.utils.ControllerUtils;
import auctionstore.dto.request.SearchRequest;
import auctionstore.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(PathConstants.ITEM)
public class ItemController {

    private final ItemService itemService;
    private final ControllerUtils controllerUtils;

    @GetMapping("/{itemId}")
    public String getItemById(@PathVariable Long itemId, Model model) {
        model.addAttribute("item", itemService.getItemById(itemId));
        return Pages.ITEM;
    }

    @GetMapping
    public String getItemsByFilterParams(SearchRequest request, Model model, Pageable pageable) {
        controllerUtils.addPagination(request, model, itemService.getItemsByFilterParams(request, pageable));
        return Pages.ITEMS;
    }

    @GetMapping("/search")
    public String searchItems(SearchRequest request, Model model, Pageable pageable) {
        controllerUtils.addPagination(request, model, itemService.searchItems(request, pageable));
        return Pages.ITEMS;
    }
}
