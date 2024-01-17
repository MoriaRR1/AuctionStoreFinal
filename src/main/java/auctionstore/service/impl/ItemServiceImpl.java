package auctionstore.service.impl;

import auctionstore.constants.ErrorMessage;
import auctionstore.domain.Item;
import auctionstore.dto.request.SearchRequest;
import auctionstore.repository.ItemRepository;
import auctionstore.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper;

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ITEM_NOT_FOUND));
    }

    @Override
    public List<Item> getPopularItems() {
        List<Long> itemIds = Arrays.asList(26L, 43L, 46L, 106L, 34L, 76L, 82L, 85L, 27L, 39L, 79L, 86L);
        return itemRepository.findByIdIn(itemIds);
    }

    @Override
    public Page<Item> getItemsByFilterParams(SearchRequest request, Pageable pageable) {
        Integer startingPrice = request.getPrice();
        Integer endingPrice = startingPrice + (startingPrice == 0 ? 500 : 50);
        return itemRepository.getItemsByFilterParams(
                request.getItemers(),
                request.getGenders(),
                startingPrice,
                endingPrice,
                pageable);
    }

    @Override
    public Page<Item> searchItems(SearchRequest request, Pageable pageable) {
        return itemRepository.searchItems(request.getSearchType(), request.getText(), pageable);
    }
}
