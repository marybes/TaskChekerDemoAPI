import static org.hamcrest.Matchers.*;

import com.google.gson.Gson;
import models.UserInfo;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UserControllerTest extends ApiTestBase {
    @Test
    public void getUserinfo_CheckStatusCode_expect200() throws Exception {
        String userUid = UserApi.createUser();
        UserApi.getUserInfo(userUid)
                .then().log().all()
                .assertThat().statusCode(200);
    }


    @Test
    public void getUserInfo_checkBodyHasNameAndUid() throws Exception {
        String userUid = UserApi.createUser();
        UserApi.getUserInfo(userUid)
                .then().log().all()
                .assertThat().body("$", hasKey("name"))
                .assertThat().body("$", hasKey("uid"));


    }

    @Test
    public void getUserinfo_CheckStatusCodeAndMessage_withIncorrectId() {
        String uid = "IncorractId";
        UserApi.getUserInfo(uid)
                .then()
                .assertThat().statusCode(404)
                .assertThat().body("message", equalTo("User with uid " + uid + " not found"));
    }

    @Test
    public void getUserinfo_returnCorrectUid_() throws Exception {
        String userUid = UserApi.createUser();
        var jsonString = UserApi.getUserInfo(userUid).getBody().asString();
        var user = new Gson().fromJson(jsonString, UserInfo.class);
        Assert.assertEquals(user.uid, userUid);
    }

    @Test
    public void getUserinfo_CheckPropertiesForUserWhoStartedTheTest() throws Exception {
        String userUid = UserApi.createUser();
        UserApi.postStart(userUid);
        UserApi.getUserInfo(userUid)
                .then().log().all()
                .assertThat().body("$", hasKey("name"))
                .assertThat().body("$", hasKey("uid"))
                .assertThat().body("$", hasKey("startTime"))
                .assertThat().body("$", hasKey("endTime"))
                .assertThat().body("$", hasKey("isStarted"));
    }
}