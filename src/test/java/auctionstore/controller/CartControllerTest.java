package auctionstore.controller;

import auctionstore.util.TestConstants;
import auctionstore.constants.Pages;
import auctionstore.constants.PathConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/sql/create-items-before.sql", "/sql/create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/create-user-after.sql", "/sql/create-items-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(TestConstants.USER_EMAIL)
    @DisplayName("[200] GET /cart - Get Cart")
    public void getCart() throws Exception {
        mockMvc.perform(get(PathConstants.CART))
                .andExpect(status().isOk())
                .andExpect(view().name(Pages.CART))
                .andExpect(model().attribute("items", hasSize(2)));
    }

    @Test
    @WithUserDetails(TestConstants.USER_EMAIL)
    @DisplayName("[300] POST /cart/add - Add Item To Cart")
    public void addItemToCart() throws Exception {
        mockMvc.perform(post(PathConstants.CART + "/add")
                        .param("itemId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(PathConstants.CART));
    }

    @Test
    @WithUserDetails(TestConstants.USER_EMAIL)
    @DisplayName("[300] POST /cart/remove - Remove Item From Cart")
    public void removeItemFromCart() throws Exception {
        mockMvc.perform(post(PathConstants.CART + "/remove")
                        .param("itemId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(PathConstants.CART));
    }
}