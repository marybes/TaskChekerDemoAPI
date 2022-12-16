import io.restassured.RestAssured;
import io.restassured.http.Cookies;

import java.io.IOException;
import java.util.Properties;

public class ApiTestBase {
    public ApiTestBase() {
        Properties prop = new Properties();
        try{
            prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
            RestAssured.baseURI = prop.getProperty("api.uri");
            RestAssured.port = Integer.parseInt(prop.getProperty("api.port"));
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }
    protected static Cookies getAuthCookies() {
        return RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", "Rx8TMYPgz@ZUzb")
                .formParam("password", "64MX9j?csngTQv")
                .when()
                .post ("/login")
                .getDetailedCookies();
    }
}
