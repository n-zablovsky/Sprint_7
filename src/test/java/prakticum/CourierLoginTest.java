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
import static org.junit.Assert.assertNotNull;

public class CourierLoginTest {
    CourierSteps courierSteps = new CourierSteps();
    private int id;

    @Test
    @DisplayName("Курьер может авторизоваться, успешный запрос возвращает 'id'")
    public void courierCanLogIn() {
        Courier courier = Courier.random();
        ValidatableResponse createResponse = courierSteps.createCourier(courier);
        var credentials = CourierCredentials.fromCourier(courier);
        ValidatableResponse loginResponse = courierSteps.logIn(credentials);
        id = loginResponse.extract().path("id");
        loginResponse.statusCode(HttpURLConnection.HTTP_OK);
        assertNotNull(loginResponse.extract().body().path("id"));
    }

    @Test
    @DisplayName("Логин курьера без входных данных")
    public void loginCourierMissingAllParamsIsFailed() {
        Courier courier = Courier.random();
        ValidatableResponse createResponse = courierSteps.createCourier(courier);
        CourierCredentials courierCredentials = new CourierCredentials("", "");
        ValidatableResponse loginResponse = courierSteps.logIn(courierCredentials);
        loginResponse.statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        loginResponse.body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Логин курьера без логина")
    public void loginCourierMissingLoginIsFailed() {
        Courier courier = Courier.random();
        ValidatableResponse createResponse = courierSteps.createCourier(courier);
        CourierCredentials courierCredentials = new CourierCredentials("", "pass1234");
        ValidatableResponse loginResponse = courierSteps.logIn(courierCredentials);
        loginResponse.statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        loginResponse.body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Логин курьера без пароля")
    public void loginCourierMissingPasswordIsFailed() {
        Courier courier = Courier.random();
        ValidatableResponse createResponse = courierSteps.createCourier(courier);
        CourierCredentials courierCredentials = new CourierCredentials("Petfed", "");
        ValidatableResponse loginResponse = courierSteps.logIn(courierCredentials);
        loginResponse.statusCode(HttpURLConnection.HTTP_BAD_REQUEST);
        loginResponse.body("message", equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Вход c неправильным логином")
    public void loginCourierWithIncorrectLogin() {
        Courier courier = Courier.random();
        ValidatableResponse createResponse = courierSteps.createCourier(courier);
        CourierCredentials correctCredentials = CourierCredentials.fromCourier(courier);
        CourierCredentials incorrectLoginCredentials = new CourierCredentials("wrong_login", correctCredentials.getPassword());
        ValidatableResponse loginResponse = courierSteps.logIn(incorrectLoginCredentials);
        loginResponse.statusCode(HttpURLConnection.HTTP_NOT_FOUND); // или HttpURLConnection.HTTP_NOT_FOUND
        loginResponse.body("message", equalTo("Учетная запись не найдена"));
    }
    @Test
    @DisplayName("Вход c неправильным паролем")
    public void loginCourierWithIncorrectPassword() {
        Courier courier = Courier.random();
        ValidatableResponse createResponse = courierSteps.createCourier(courier);
        CourierCredentials correctCredentials = CourierCredentials.fromCourier(courier);
        CourierCredentials incorrectLoginCredentials = new CourierCredentials(correctCredentials.getLogin(), "wrong_password");
        ValidatableResponse loginResponse = courierSteps.logIn(incorrectLoginCredentials);
        loginResponse.statusCode(HttpURLConnection.HTTP_NOT_FOUND); // или HttpURLConnection.HTTP_NOT_FOUND
        loginResponse.body("message", equalTo("Учетная запись не найдена"));
    }


    @After
    public void deleteCourier() {
        if (id > 0) {
            courierSteps.delete(id);
        }
    }

}
