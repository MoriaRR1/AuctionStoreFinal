package auctionstore.service;

import auctionstore.domain.Order;
import auctionstore.domain.Item;
import auctionstore.domain.User;
import auctionstore.dto.request.ItemRequest;
import auctionstore.dto.request.SearchRequest;
import auctionstore.dto.response.MessageResponse;
import auctionstore.dto.response.UserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {

    Page<Item> getItems(Pageable pageable);

    Page<Item> searchItems(SearchRequest request, Pageable pageable);

    Page<User> getUsers(Pageable pageable);

    Page<User> searchUsers(SearchRequest request, Pageable pageable);

    Order getOrder(Long orderId);

    Page<Order> getOrders(Pageable pageable);

    Page<Order> searchOrders(SearchRequest request, Pageable pageable);

    Item getItemById(Long itemId);

    MessageResponse editItem(ItemRequest itemRequest, MultipartFile file);

    MessageResponse addItem(ItemRequest itemRequest, MultipartFile file);

    UserInfoResponse getUserById(Long userId, Pageable pageable);
}
