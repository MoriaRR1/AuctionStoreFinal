package auctionstore.service;

import auctionstore.domain.Order;
import auctionstore.domain.User;
import auctionstore.dto.request.ChangePasswordRequest;
import auctionstore.dto.request.EditUserRequest;
import auctionstore.dto.request.ItemRequest;
import auctionstore.dto.request.SearchRequest;
import auctionstore.dto.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;


public interface UserService {

    User getAuthenticatedUser();

    Page<Order> searchUserOrders(SearchRequest request, Pageable pageable);

    MessageResponse editUserInfo(EditUserRequest request);

    MessageResponse changePassword(ChangePasswordRequest request);

    MessageResponse addItem(ItemRequest itemRequest, MultipartFile file);
}
