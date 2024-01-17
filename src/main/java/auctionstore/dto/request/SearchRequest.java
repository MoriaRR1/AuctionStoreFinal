package auctionstore.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequest {
    private List<String> itemers;
    private List<String> genders;
    private Integer price = 0;
    private String searchType;
    private String text;
}
