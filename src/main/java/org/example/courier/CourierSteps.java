package org.example.courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.base.BaseConfig;


public class CourierSteps extends BaseConfig {
    private static final String COURIER_URL = "/api/v1/courier";
    private static final String LOGIN_URL = "/api/v1/courier/login";

    @Step("Создание курьера")
    public ValidatableResponse createCourier(Courier courier) {
        return spec()
                .and()
                .body(courier)
                .when()
                .post(COURIER_URL)
                .then().log().all();
    }
    @Step("Войти")
    public ValidatableResponse logIn(CourierCredentials creds) {
        return spec()
                .and()
                .body(creds)
                .when()
                .post(LOGIN_URL)
                .then().log().all();
    }
    @Step("Удаление курьера")
    public ValidatableResponse delete(int id){
        return spec()
                .and()
                .when()
                .delete(COURIER_URL+ "/" + id)
                .then().log().all();
    }


}
