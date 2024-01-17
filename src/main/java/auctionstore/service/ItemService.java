package auctionstore.service;

import auctionstore.domain.Item;
import auctionstore.dto.request.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    Item getItemById(Long itemId);

    List<Item> getPopularItems();

    Page<Item> getItemsByFilterParams(SearchRequest searchRequest, Pageable pageable);

    Page<Item> searchItems(SearchRequest searchRequest, Pageable pageable);
}
