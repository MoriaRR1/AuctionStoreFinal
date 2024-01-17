package auctionstore.service.impl;

import auctionstore.domain.User;
import auctionstore.service.UserService;
import auctionstore.constants.ErrorMessage;
import auctionstore.constants.SuccessMessage;
import auctionstore.domain.Order;
import auctionstore.dto.request.ChangePasswordRequest;
import auctionstore.dto.request.EditUserRequest;
import auctionstore.dto.request.SearchRequest;
import auctionstore.dto.response.MessageResponse;
import auctionstore.repository.OrderRepository;
import auctionstore.repository.UserRepository;
import auctionstore.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class UserServiceImpl implements UserService {

    @Value("${upload.path}")
    private String uploadPath;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public User getAuthenticatedUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(principal.getUsername());
    }

    @Override
    public Page<Order> searchUserOrders(SearchRequest request, Pageable pageable) {
        User user = getAuthenticatedUser();
        return orderRepository.searchUserOrders(user.getId(), request.getSearchType(), request.getText(), pageable);
    }

    @Override
    @Transactional
    public MessageResponse editUserInfo(EditUserRequest request) {
        User user = getAuthenticatedUser();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setCity(request.getCity());
        user.setAddress(request.getAddress());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPostIndex(request.getPostIndex());
        return new MessageResponse("alert-success", SuccessMessage.USER_UPDATED);
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

    @Override
    @SneakyThrows
    @Transactional
    public MessageResponse addItem(ItemRequest itemRequest, MultipartFile file) {
        return saveItem(itemRequest, file, SuccessMessage.ITEM_ADDED);
    }


    @Override
    @Transactional
    public MessageResponse changePassword(ChangePasswordRequest request) {
        if (request.getPassword() != null && !request.getPassword().equals(request.getPassword2())) {
            return new MessageResponse("passwordError", ErrorMessage.PASSWORDS_DO_NOT_MATCH);
        }
        User user = getAuthenticatedUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return new MessageResponse("alert-success", SuccessMessage.PASSWORD_CHANGED);
    }
}
