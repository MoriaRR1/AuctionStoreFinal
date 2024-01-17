package auctionstore.repository;

import auctionstore.domain.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByIdIn(List<Long> itemsIds);

    Page<Item> findAllByOrderByPriceAsc(Pageable pageable);

    @Query("SELECT item FROM Item item WHERE " +
            "(CASE " +
            "   WHEN :searchType = 'itemTitle' THEN UPPER(item.itemTitle) " +
            "   WHEN :searchType = 'country' THEN UPPER(item.country) " +
            "   ELSE UPPER(item.itemer) " +
            "END) " +
            "LIKE UPPER(CONCAT('%',:text,'%')) " +
            "ORDER BY item.price ASC")
    Page<Item> searchItems(String searchType, String text, Pageable pageable);

    @Query("SELECT item FROM Item item " +
            "WHERE (coalesce(:itemers, null) IS NULL OR item.itemer IN :itemers) " +
            "AND (coalesce(:genders, null) IS NULL OR item.itemGender IN :genders) " +
            "AND (coalesce(:priceStart, null) IS NULL OR item.price BETWEEN :priceStart AND :priceEnd) " +
            "ORDER BY item.price ASC")
    Page<Item> getItemsByFilterParams(
            List<String> itemers,
            List<String> genders,
            Integer priceStart,
            Integer priceEnd,
            Pageable pageable);
}
