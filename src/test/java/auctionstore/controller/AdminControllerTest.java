package auctionstore.controller;

import auctionstore.util.TestConstants;
import auctionstore.constants.ErrorMessage;
import auctionstore.constants.Pages;
import auctionstore.constants.PathConstants;
import auctionstore.constants.SuccessMessage;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WithUserDetails(TestConstants.ADMIN_EMAIL)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create-items-before.sql", "/sql/create-user-before.sql", "/sql/create-orders-before.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/create-orders-after.sql", "/sql/create-user-after.sql", "/sql/create-Method...-after.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("[200] GET /admin/items - Get Items")
    public void getItems() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/items"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ITEMS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(12))));
    }

    @Test
    @DisplayName("[200] GET /admin/itemes/search - Search Itemes By itemer")
    public void searchItems_ByItemer() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/items/search")
                        .param("searchType", "itemer")
                        .param("text", "Creed"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ITEMS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(7))));
    }

    @Test
    @DisplayName("[200] GET /admin/items/search - Search Items By country")
    public void searchItems_ByCountry() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/items/search")
                        .param("searchType", "country")
                        .param("text", "Spain"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ITEMS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/items/search - Search Items By ItemTitle")
    public void searchItems_ItemTitle() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/items/search")
                        .param("searchType", "itemTitle")
                        .param("text", "Aventus"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ITEMS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/items/search - Get Users")
    public void getUsers() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/users"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ALL_USERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(3))));
    }

    @Test
    @DisplayName("[200] GET /admin/users/search - Search Users By email")
    public void searchUsers_ByEmail() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/users/search")
                        .param("searchType", "email")
                        .param("text", TestConstants.USER_EMAIL))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ALL_USERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/users/search - Search Users By First Name")
    public void searchUsers_ByFirstName() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/users/search")
                        .param("searchType", "firstName")
                        .param("text", TestConstants.USER_FIRST_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ALL_USERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/users/search - Search Users By Last Name")
    public void searchUsers_ByLastName() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/users/search")
                        .param("searchType", "lastName")
                        .param("text", TestConstants.USER_LAST_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ALL_USERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/order/111 - Get Order")
    public void getOrder() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/order/{orderId}", 111))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDER))
                .andExpect(model().attribute("order", hasProperty("id", Matchers.is(TestConstants.ORDER_ID))))
                .andExpect(model().attribute("order", hasProperty("totalPrice", Matchers.is(TestConstants.ORDER_TOTAL_PRICE))))
                .andExpect(model().attribute("order", hasProperty("firstName", Matchers.is(TestConstants.ORDER_FIRST_NAME))))
                .andExpect(model().attribute("order", hasProperty("lastName", Matchers.is(TestConstants.ORDER_LAST_NAME))))
                .andExpect(model().attribute("order", hasProperty("city", Matchers.is(TestConstants.ORDER_CITY))))
                .andExpect(model().attribute("order", hasProperty("address", Matchers.is(TestConstants.ORDER_ADDRESS))))
                .andExpect(model().attribute("order", hasProperty("email", Matchers.is(TestConstants.ORDER_EMAIL))))
                .andExpect(model().attribute("order", hasProperty("phoneNumber", Matchers.is(TestConstants.ORDER_PHONE_NUMBER))))
                .andExpect(model().attribute("order", hasProperty("postIndex", Matchers.is(TestConstants.ORDER_POST_INDEX))))
                .andExpect(model().attribute("order", hasProperty("items", hasSize(2))));
    }

    @Test
    @DisplayName("[200] GET /admin/orders - Get Orders")
    public void getOrders() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/orders/search - Search Orders By Email")
    public void searchOrders_ByEmail() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/orders/search")
                        .param("searchType", "email")
                        .param("text", TestConstants.USER_EMAIL))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/orders/search - Search Orders bt First Name")
    public void searchOrders_ByFirstName() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/orders/search")
                        .param("searchType", "firstName")
                        .param("text", TestConstants.USER_FIRST_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/orders/search - Search Orders By Last Name")
    public void searchOrders_ByLastName() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/orders/search")
                        .param("searchType", "lastName")
                        .param("text", TestConstants.USER_LAST_NAME))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ORDERS))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[200] GET /admin/item/1 - Get Item")
    public void getItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.ADMIN + "/item/{itemId}", TestConstants.ITEM_ID))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_EDIT_ITEM))
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
    @DisplayName("[404] GET /admin/item/111 - Get Item Not Found")
    public void getItem_NotFound() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/item/{itemId}", 111))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(ErrorMessage.ITEM_NOT_FOUND));
    }

    @Test
    @DisplayName("[300] POST /admin/edit/item - Edit Item")
    public void editItem() throws Exception {
        mockMvc.perform(multipart(PathConstants.ADMIN + "/edit/item")
                        .file(mockFile())
                        .param("id", String.valueOf(TestConstants.ITEM_ID))
                        .param("itemTitle", TestConstants.ITEM_TITLE)
                        .param("itemer", TestConstants.ITEMER)
                        .param("year", String.valueOf(TestConstants.YEAR))
                        .param("country", TestConstants.COUNTRY)
                        .param("itemGender", TestConstants.ITEM_GENDER)
                        .param("itemTopNotes", TestConstants.ITEM_TOP_NOTES)
                        .param("itemMiddleNotes", TestConstants.ITEM_MIDDLE_NOTES)
                        .param("itemBaseNotes", TestConstants.ITEM_BASE_NOTES)
                        .param("price", String.valueOf(TestConstants.PRICE))
                        .param("volume", TestConstants.VOLUME)
                        .param("type", TestConstants.TYPE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/items"))
                .andExpect(flash().attribute("messageType", "alert-success"))
                .andExpect(flash().attribute("message", SuccessMessage.ITEM_EDITED));
    }

    @Test
    @DisplayName("[200] POST /admin/edit/item - Edit Item Return Input Errors")
    public void editItem_ReturnInputErrors() throws Exception {
        mockMvc.perform(multipart(PathConstants.ADMIN + "/edit/item")
                        .file(mockFile())
                        .param("id", String.valueOf(TestConstants.ITEM_ID))
                        .param("itemTitle", "")
                        .param("itemer", "")
                        .param("year", "0")
                        .param("country", "")
                        .param("itemGender", "")
                        .param("itemTopNotes", "")
                        .param("itemMiddleNotes", "")
                        .param("itemBaseNotes", "")
                        .param("price", "0")
                        .param("volume", "")
                        .param("type", ""))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_EDIT_ITEM))
                .andExpect(model().attribute("itemTitleError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("itemerError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("yearError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("countryError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("itemGenderError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("itemTopNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("itemMiddleNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("itemBaseNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("priceError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("volumeError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("typeError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)));
    }

    @Test
    @DisplayName("[200] GET /admin/add/item - Get Add Item Page")
    public void getAddItemPage() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/add/item"))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ADD_ITEM));
    }

    @Test
    @DisplayName("[300] POST /admin/add/item - Add Item")
    public void addItem() throws Exception {
        mockMvc.perform(multipart(PathConstants.ADMIN + "/add/item")
                        .file(mockFile())
                        .param("itemTitle", TestConstants.ITEM_TITLE)
                        .param("itemer", TestConstants.ITEMER)
                        .param("year", String.valueOf(TestConstants.YEAR))
                        .param("country", TestConstants.COUNTRY)
                        .param("itemGender", TestConstants.ITEM_GENDER)
                        .param("itemTopNotes", TestConstants.ITEM_TOP_NOTES)
                        .param("itemMiddleNotes", TestConstants.ITEM_MIDDLE_NOTES)
                        .param("itemBaseNotes", TestConstants.ITEM_BASE_NOTES)
                        .param("price", String.valueOf(TestConstants.PRICE))
                        .param("volume", TestConstants.VOLUME)
                        .param("type", TestConstants.TYPE))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/items"))
                .andExpect(flash().attribute("messageType", "alert-success"))
                .andExpect(flash().attribute("message", SuccessMessage.ITEM_ADDED));
    }

    @Test
    @DisplayName("[200] POST /admin/add/item - Add Item Return Input Errors")
    public void addItem_ReturnInputErrors() throws Exception {
        mockMvc.perform(multipart(PathConstants.ADMIN + "/add/item")
                        .file(mockFile())
                        .param("itemTitle", "")
                        .param("itemer", "")
                        .param("year", "0")
                        .param("country", "")
                        .param("itemGender", "")
                        .param("itemTopNotes", "")
                        .param("itemMiddleNotes", "")
                        .param("itemBaseNotes", "")
                        .param("price", "0")
                        .param("volume", "")
                        .param("type", ""))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_ADD_ITEM))
                .andExpect(model().attribute("itemTitleError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("itemerError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("yearError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("countryError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("itemGenderError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("itemTopNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("itemMiddleNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("itemBaseNotesError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("priceError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("volumeError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)))
                .andExpect(model().attribute("typeError", is(ErrorMessage.FILL_IN_THE_INPUT_FIELD)));
    }

    @Test
    @DisplayName("[200] GET /admin/user/122 - Get User By Id")
    public void getUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(PathConstants.ADMIN + "/user/{itemId}", TestConstants.USER_ID))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.ADMIN_USER_DETAIL))
                .andExpect(model().attribute("user", hasProperty("id", Matchers.is(TestConstants.USER_ID))))
                .andExpect(model().attribute("user", hasProperty("email", Matchers.is(TestConstants.USER_EMAIL))))
                .andExpect(model().attribute("user", hasProperty("firstName", Matchers.is(TestConstants.USER_FIRST_NAME))))
                .andExpect(model().attribute("user", hasProperty("lastName", Matchers.is(TestConstants.USER_LAST_NAME))))
                .andExpect(model().attribute("user", hasProperty("city", Matchers.is(TestConstants.USER_CITY))))
                .andExpect(model().attribute("user", hasProperty("address", Matchers.is(TestConstants.USER_ADDRESS))))
                .andExpect(model().attribute("user", hasProperty("phoneNumber", Matchers.is(TestConstants.USER_PHONE_NUMBER))))
                .andExpect(model().attribute("user", hasProperty("postIndex", Matchers.is(TestConstants.USER_POST_INDEX))))
                .andExpect(model().attribute("page", hasProperty("content", hasSize(1))));
    }

    @Test
    @DisplayName("[404] GET /admin/user/123 - Get User By Id Not Found")
    public void getUserById_NotFound() throws Exception {
        mockMvc.perform(get(PathConstants.ADMIN + "/user/{itemId}", 123))
                .andExpect(status().isNotFound())
                .andExpect(status().reason(ErrorMessage.USER_NOT_FOUND));
    }

    private MockMultipartFile mockFile() throws IOException {
        FileInputStream inputFile = new FileInputStream(new File(TestConstants.FILE_PATH));
        return new MockMultipartFile("file", TestConstants.FILE_NAME, MediaType.MULTIPART_FORM_DATA_VALUE, inputFile);
    }
}
