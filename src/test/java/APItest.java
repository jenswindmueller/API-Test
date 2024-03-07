import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class APItest {

     /**
     * This test checks the response from the API. It asserts that the age of the person is greater than 50 and the nationality is 'US'.
     */
    @Test
    public void checkResponse() {
        Response response = RestAssured.get("https://randomuser.me/api");
        String responseBody = response.getBody().asString();
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray results = jsonObject.getJSONArray("results");

        JSONObject result = results.getJSONObject(0);
        int age = result.getJSONObject("dob").getInt("age");
        String nationality = result.getString("nat");
        System.out.println("Response Code: " + response.getStatusCode());
        System.out.println("Age: " + age);
        System.out.println(nationality);
        assertAll("person",
            () -> assertTrue(age > 50, "Age is less than 50"),
            () -> assertEquals("US", nationality)
        );
    }
   
    public List<JSONObject> fetchUsers() {
        Response response = RestAssured.get("https://randomuser.me/api/?results=10");
        String responseBody = response.getBody().asString();
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray results = jsonObject.getJSONArray("results");
    
        // Convert JSONArray to List<JSONObject>
        List<JSONObject> jsonList = new ArrayList<>();
        for (int i = 0; i < results.length(); i++) {
            jsonList.add(results.getJSONObject(i));
        }
    
        return jsonList;
    }
    
    public List<JSONObject> sortUsers(List<JSONObject> users) {
        // Sort List<JSONObject> by first name
        users.sort(Comparator.comparing(o -> o.getJSONObject("name").getString("first")));
        // Sort list by first name reversed
        // users.sort(Comparator.comparing((JSONObject o) -> o.getJSONObject("name").getString("first")).reversed());
        // users.sort(Comparator.comparing(o -> o.getJSONObject("dob").getInt("age")));
        // users.sort(Comparator.comparing((JSONObject o) -> o.getJSONObject("dob").getInt("age")).reversed());
        return users;
    }
    @Test
    public void fetchAndSortUsers() {
        // Fetch users
        List<JSONObject> users = fetchUsers();
        // Sort users
        List<JSONObject> sortedUsers = sortUsers(users);
        // Print each user
        sortedUsers.forEach(user -> System.out.println(user.getJSONObject("name").getString("first")));
    }
}
