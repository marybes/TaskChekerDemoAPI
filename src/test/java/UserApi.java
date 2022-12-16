import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.internal.http.Status;
import io.restassured.response.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class UserApi extends ApiTestBase{
    public static Response getUserInfo(String o) {
    return given()
            .pathParam("uid", o)
            .when()
            .get("/api/public/user/info/{uid}");
}
    private static String getUserCsvTemplate() throws URISyntaxException, IOException {
        URL url = UserApi.class.getResource("fileForPOSTimport.csv");
        assert url != null;
        Path path = Paths.get(url.toURI());
        return Files.readString(path, StandardCharsets.UTF_8);

}
    public static String createUser() throws Exception {
        UUID newUserId = UUID.randomUUID();
        String csvTemplate = getUserCsvTemplate();
        String csvPayload = csvTemplate.replace("{{PUT_UUID_HERE}}", newUserId.toString());

        Response response =
                given()
                        .cookies(getAuthCookies())
                        .header("Content-Type", "multipart/form-data")
                        .multiPart(new MultiPartSpecBuilder(
                                new ByteArrayInputStream(csvPayload.getBytes(StandardCharsets.UTF_8)))
                                .controlName("file")
                                .mimeType("text/plain")
                                .build())
                        .when()
                        .post("/api/public/user/import");

        if (!Status.SUCCESS.matches(response.statusCode())) {
            throw new Exception("User was not created");
        }
        return newUserId.toString();
    }

    public static Response postStart(String uid) {
        return given()
                .pathParam("uid", uid)
                .when()
                .post("/api/public/user/start/{uid}");
    }
}

