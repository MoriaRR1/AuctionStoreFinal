package auctionstore.service;

import auctionstore.dto.response.MessageResponse;
import auctionstore.dto.request.UserRequest;

public interface RegistrationService {

    MessageResponse registration(String captchaResponse, UserRequest user);

    MessageResponse activateEmailCode(String code);
}
