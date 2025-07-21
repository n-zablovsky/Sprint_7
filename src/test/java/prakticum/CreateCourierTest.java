package prakticum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.example.courier.Courier;
import org.example.courier.CourierCredentials;
import org.example.courier.CourierSteps;
import org.junit.After;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class CreateCourierTest {
    CourierSteps courierSteps = new CourierSteps();
    private int id;


    @Test
    @DisplayName("Курьера можно создать, успешный запрос возвращает ok: true, запрос возвращает правильный код ответа")
    public void courierCanBeCreated() {
        Courier courier = Courier.random();
        ValidatableResponse createResponse = courierSteps.createCourier(courier);
        createResponse.statusCode(HttpURLConnection.HTTP_CREATED).extract().path("ok");
        var credentials = CourierCredentials.fromCourier(courier);
        ValidatableResponse loginResponse = courierSteps.logIn(credentials);
        loginResponse.statusCode(HttpURLConnection.HTTP_OK);
        id = loginResponse.extract().path("id");
        assertNotEquals(0, id);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров, если создать пользователя с логином, который уже есть, возвращается ошибка")
    public void cannotCreateDuplicateCourier() {
        Courier courier = Courier.random();
        ValidatableResponse createResponse = courierSteps.createCourier(courier);
        ValidatableResponse createResponseTwice = courierSteps.createCourier(courier);
        createResponse.statusCode(HttpURLConnection.HTTP_CREATED).extract().path("ok");
        createResponseTwice.statusCode(HttpURLConnection.HTTP_CONFLICT);
        String errorMessage = createResponseTwice.extract().path("message");
        assertEquals("Этот логин уже используется. Попробуйте другой.", errorMessage);

    }

    @Test
    @DisplayName("Если нет логина, запрос возвращает ошибку")
    public void checkCannotCreateCourierWithoutLogin() {
        Courier courier = new Courier(null, "1234", "Petr");
        ValidatableResponse createResponse = courierSteps.createCourier(courier);
        createResponse.statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        createResponse.body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    @DisplayName("Если нет пароля, запрос возвращает ошибку")
    public void checkCannotCreateCourierWithoutPassword() {
        Courier courier = new Courier("Petfedr", null, "Petr");
        ValidatableResponse createResponse = courierSteps.createCourier(courier);
        createResponse.statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        createResponse.body("message", equalTo("Недостаточно данных для создания учетной записи"));

    }

    @After
    public void deleteCourier() {
        if (id > 0) {
            courierSteps.delete(id);
        }
    }
}
