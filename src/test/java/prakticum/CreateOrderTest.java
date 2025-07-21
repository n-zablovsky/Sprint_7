package prakticum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.order.Order;
import org.example.order.OrderSteps;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.HttpURLConnection;

import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    OrderSteps orderSteps = new OrderSteps();
    private int trackId;
    private final String[] color;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "цвет самоката. Тестовые данные: \"GREY\", \"GREY\", \"BLACK\", \"BLACK\", null")
    public static Object[][] getColorData() {
        return new Object[][]{
                {new String[]{"GREY"}},
                {new String[]{"BLACK"}},
                {new String[]{"GREY", "BLACK"}},
                {new String[]{null}},
        };
    }

    @Test
    @DisplayName("Проверка создание заказа с разными цветами самоката")
    public void createOrder() {
        Order order = new Order("Петр", "Федоров", "Москва, улица Охотный ряд, 33", 4, "8 900 555 44 88", 1, "2025-07-17", "Пожалуйста, доставьте самокат с полным зарядом", color);
        ValidatableResponse orderCreateResponse = orderSteps.createOrder(order);
        orderCreateResponse.statusCode(HttpURLConnection.HTTP_CREATED);
        trackId = orderCreateResponse.extract().path("track");
        assertNotNull(trackId);

    }

    @After
    public void tearDown() {
        if (trackId > 0) {
            orderSteps.cancelOrder(trackId);
        }
    }

}
