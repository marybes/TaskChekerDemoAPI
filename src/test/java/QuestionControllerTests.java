import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

public class QuestionControllerTests extends ApiTestBase {
    @Test
    public void getUserinfo_CheckStatusCode_expect200() {
    given()
            .cookies(getAuthCookies())
        .when()
        .get("/api/public/questions/allSecure")
        .then().log().all()
                .assertThat().statusCode(200)
            .assertThat().body("id",hasSize(greaterThan(0)));

    }

}
