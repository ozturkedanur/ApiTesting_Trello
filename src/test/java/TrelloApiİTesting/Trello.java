package TrelloApiİTesting;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;

import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;
import io.restassured.response.Response;

public class Trello {

    public String baseUri = "https://api.trello.com";
    public String key = "aa743f86030c8819776bd9691b7202e4";
    public String token = "ATTA81807432ca4746853cf775b503c7fce30fd3a120bd01ce806e11c1390e3d5207CD479C02";
    public String boardId ;
    public String listId ;
    public String cardId ;

    @Test(priority = 1)
    public void createBroad() {
        RestAssured.baseURI = baseUri;
        String board_name = "New Board " + (int)(Math.random()*100);
        System.out.println(board_name);

       Response response = given()
        .queryParam("key", "aa743f86030c8819776bd9691b7202e4")
                .queryParam("token", "ATTA81807432ca4746853cf775b503c7fce30fd3a120bd01ce806e11c1390e3d5207CD479C02")
                .queryParam("name", board_name)
                .header("Content-Type", "application/json").
        when()
                .post("/1/boards").
        then()
                .assertThat().statusCode(200).and().
                contentType(ContentType.JSON).and().
                body("name", equalTo(board_name)).
        extract().response();

        String jsonResponse = response.asString();

        JsonPath responseBody = new JsonPath(jsonResponse);
        System.out.println("Board Id : " + responseBody.get("id"));
        boardId = responseBody.get("id");

    }
    @Test(priority = 2)
    public void createList() {
        RestAssured.baseURI = baseUri;
        String list_name = "New List " + (int)(Math.random()*100);
        System.out.println(list_name);

        Response response = (Response) given()
                .queryParam("key", key)
                .queryParam("token", token)
                .queryParam("name", list_name)
                .queryParam("idBoard", boardId)
                .header("Content-Type", "application/json").
                when()
                .post("/1/lists").
                then()
                .assertThat().statusCode(200).and().
                contentType(ContentType.JSON).and().
                body("name", equalTo(list_name)).
        extract().response();

        String jsonResponse = response.asString();
        JsonPath responseBody = new JsonPath(jsonResponse);
        System.out.println("List Id : " + responseBody.get("id"));
        listId = responseBody.get("id");
    }

    @Test(priority = 3)
    public void createCard() {
        RestAssured.baseURI = baseUri;

        Response response = (Response) given()
                .queryParam("key", key)
                .queryParam("token", token)
                .queryParam("idList", listId)
                .header("Content-Type", "application/json").
                when()
                .post("/1/cards").
                then()
                .assertThat().statusCode(200).and().
                contentType(ContentType.JSON).and().
                extract().response();

        String jsonResponse = response.asString();
        JsonPath responseBody = new JsonPath(jsonResponse);
        System.out.println("Card Id : " + responseBody.get("id"));
        cardId = responseBody.get("id");
    }

    @Test(priority = 4)
    public void CardUpdate() {
        RestAssured.baseURI = baseUri;

        String requestBody = "{\"name\": \"Guncel\" }";


        given().queryParam("key", key)
                .queryParam("token", token)
                .body(requestBody)
                .header("Content-Type", "application/json").
                when()
                .put("/1/cards/" +cardId).
                then()
                .assertThat().statusCode(200).and().
                contentType(ContentType.JSON).and();
    }

    @Test(priority = 5)
    public void CardDelete() {
        RestAssured.baseURI = baseUri;

        given().queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json").
                when()
                .delete("/1/cards/" +cardId).
                then()
                .assertThat().statusCode(200).and().
                contentType(ContentType.JSON).and();
    }

    @Test(priority = 6)
    public void BoardDelete() {
        RestAssured.baseURI = baseUri;

        given().queryParam("key", key)
                .queryParam("token", token)
                .header("Content-Type", "application/json").
                when()
                .delete("/1/boards/" +boardId).
                then()
                .assertThat().statusCode(200).and().
                contentType(ContentType.JSON).and();
    }

}
