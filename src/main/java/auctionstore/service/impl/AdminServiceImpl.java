package auctionstore.service.impl;

import auctionstore.domain.User;
import auctionstore.dto.response.UserInfoResponse;
import auctionstore.service.AdminService;
import auctionstore.constants.ErrorMessage;
import auctionstore.constants.SuccessMessage;
import auctionstore.domain.Order;
import auctionstore.domain.Item;
import auctionstore.dto.request.ItemRequest;
import auctionstore.dto.request.SearchRequest;
import auctionstore.dto.response.MessageResponse;
import auctionstore.repository.OrderRepository;
import auctionstore.repository.ItemRepository;
import auctionstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<Item> getItems(Pageable pageable) {
        return itemRepository.findAllByOrderByPriceAsc(pageable);
    }

    @Override
    public Page<Item> searchItems(SearchRequest request, Pageable pageable) {
        return itemRepository.searchItems(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> searchUsers(SearchRequest request, Pageable pageable) {
        return userRepository.searchUsers(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Order getOrder(Long orderId) {
        return orderRepository.getById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ORDER_NOT_FOUND));
    }

    @Override
    public Page<Order> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);

    }

    @Override
    public Page<Order> searchOrders(SearchRequest request, Pageable pageable) {
        return orderRepository.searchOrders(request.getSearchType(), request.getText(), pageable);
    }

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.ITEM_NOT_FOUND));
    }

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse editItem(ItemRequest itemRequest, MultipartFile file) {
        return saveItem(itemRequest, file, SuccessMessage.ITEM_ADDED);
    }

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse addItem(ItemRequest itemRequest, MultipartFile file) {
        return saveItem(itemRequest, file, SuccessMessage.ITEM_ADDED);
    }

    @Override
    public UserInfoResponse getUserById(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.USER_NOT_FOUND));
        Page<Order> orders = orderRepository.findOrderByUserId(userId, pageable);
        return new UserInfoResponse(user, orders);
    }

    private MessageResponse saveItem(ItemRequest itemRequest, MultipartFile file, String message) throws IOException {
        Item item = modelMapper.map(itemRequest, Item.class);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + "/" + resultFilename));
            item.setFilename(resultFilename);
        }
        itemRepository.save(item);
        return new MessageResponse("alert-success", message);
    }
}
