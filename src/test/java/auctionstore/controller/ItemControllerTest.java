package auctionstore.controller;

import auctionstore.util.TestConstants;
import auctionstore.constants.ErrorMessage;
import auctionstore.constants.Pages;
import auctionstore.constants.PathConstants;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create-items-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/create-items-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("[200] GET /item/1 - Get Items")
    public void getPerfumeById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.ITEM + "/{itemId}", TestConstants.ITEM_ID))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ITEM))
                .andExpect(model().attribute("item", hasProperty("id", Matchers.is(TestConstants.ITEM_ID))))
                .andExpect(model().attribute("item", hasProperty("itemTitle", Matchers.is(TestConstants.ITEM_TITLE))))
                .andExpect(model().attribute("item", hasProperty("itemer", Matchers.is(TestConstants.ITEMER))))
                .andExpect(model().attribute("item", hasProperty("year", Matchers.is(TestConstants.YEAR))))
                .andExpect(model().attribute("item", hasProperty("country", Matchers.is(TestConstants.COUNTRY))))
                .andExpect(model().attribute("item", hasProperty("itemGender", Matchers.is(TestConstants.ITEM_GENDER))))
                .andExpect(model().attribute("item", hasProperty("itemTopNotes", Matchers.is(TestConstants.ITEM_TOP_NOTES))))
                .andExpect(model().attribute("item", hasProperty("itemMiddleNotes", Matchers.is(TestConstants.ITEM_MIDDLE_NOTES))))
                .andExpect(model().attribute("item", hasProperty("itemBaseNotes", Matchers.is(TestConstants.ITEM_BASE_NOTES))))
                .andExpect(model().attribute("item", hasProperty("filename", Matchers.is(TestConstants.FILENAME))))
                .andExpect(model().attribute("item", hasProperty("price", Matchers.is(TestConstants.PRICE))))
                .andExpect(model().attribute("item", hasProperty("volume", Matchers.is(TestConstants.VOLUME))))
                .andExpect(model().attribute("item", hasProperty("type", Matchers.is(TestConstants.TYPE))));
    }

    @Test
    @DisplayName("[404] GET /item/111 - Get Item By Id NotFound")
    public void getItemById_NotFound() throws Exception {
        mockMvc.perform(get(PathConstants.ITEM + "/{itemId}", 111))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(ErrorMessage.ITEM_NOT_FOUND));
    }

    @Test
    @DisplayName("[200] GET /item - Get Items By Filter Params")
    public void getItemsByFilterParams() throws Exception {
        mockMvc.perform(get(PathConstants.ITEM))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ITEMS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(12))));
    }

    @Test
    @DisplayName("[200] GET /item - Get Items By Filter Params: itemers")
    public void getItemsByFilterParams_Itemers() throws Exception {
        mockMvc.perform(get(PathConstants.ITEM)
                        .param("itemers", "Creed"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ITEMS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(7))));
    }

    @Test
    @DisplayName("[200] GET /item - Get Items By Filter Params: itemers, genders")
    public void getItemsByFilterParams_ItemersAndGenders() throws Exception {
        mockMvc.perform(get(PathConstants.ITEM)
                        .param("itemers", "Creed")
                        .param("genders", "male"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ITEMS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(3))));
    }

    @Test
    @DisplayName("[200] GET /item - Get Items By Filter Params: itemers, genders, price")
    public void getItemsByFilterParams_ItemersAndGendersAndPrice() throws Exception {
        mockMvc.perform(get(PathConstants.ITEM)
                        .param("itemers", "Creed", "Dior")
                        .param("genders", "male")
                        .param("price", "60"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ITEMS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(5))));
    }

    @Test
    @DisplayName("[200] GET /item/search - Search Items By Itemer")
    public void searchItems_ByItemer() throws Exception {
        mockMvc.perform(get(PathConstants.ITEM + "/search")
                        .param("searchType", "itemer")
                        .param("text", "Creed"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ITEMS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(7))));
    }

    @Test
    @DisplayName("[200] GET /item/search - Search Items By Country")
    public void searchItems_ByCountry() throws Exception {
        mockMvc.perform(get(PathConstants.ITEM + "/search")
                        .param("searchType", "country")
                        .param("text", "Spain"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ITEMS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /item/search - Search Items By Item Title")
    public void searchItems_ItemTitle() throws Exception {
        mockMvc.perform(get(PathConstants.ITEM + "/search")
                        .param("searchType", "itemTitle")
                        .param("text", "Aventus"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ITEMS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }
}