package prakticum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.order.OrderSteps;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.junit.Assert.assertNotNull;

public class GetOrderListTest {
    OrderSteps orderSteps = new OrderSteps();


    @Test
    @DisplayName("Проверка получения списка заказов")
    public void getListOfOrders() {
        ValidatableResponse getOrderListResponse = orderSteps.getOrderList();
        getOrderListResponse.statusCode(HttpURLConnection.HTTP_OK);
        assertNotNull(getOrderListResponse.extract().path("orders"));
    }

}
