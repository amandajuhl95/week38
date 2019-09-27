package rest;

import entities.Person;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import static org.glassfish.grizzly.http.util.Header.ContentType;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    //Read this line from a settings-file  since used several places
    private static final String TEST_DB = "jdbc:mysql://localhost:3307/startcode_test";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private Person p1;
    private Person p2;
    private Person p3;
    private Person p4;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        System.setProperty("IS_TEST", TEST_DB);
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.CREATE);
        httpServer = startServer();

        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;

        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();
        System.clearProperty("IS_TEST");
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        p1 = new Person("Laura", "Saxtrup", "20141614");
        p2 = new Person("Jonas", "Haslund", "21534532");
        p3 = new Person("Amanda", "Juhl", "42441486");
        p4 = new Person("Amalie", "Landt", "20856221");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person").then().statusCode(200);
    }

    @Test
    public void testGetAllPersons() throws Exception {
        given()
                .contentType("application/json")
                .get("/person/all").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstname", hasItems("Laura",
                        "Jonas",
                        "Amanda",
                        "Amalie"),
                        "lastname", hasItems("Saxtrup", "Haslund", "Juhl", "Landt"));
    }

    @Test
    public void testGetPerson() throws Exception {
        given()
                .contentType("application/json")
                .get("/person/{id}", p1.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstname", equalTo("Laura"), "lastname", equalTo("Saxtrup"), "phone", equalTo("20141614"));

    }

    @Test
    public void testGetPersonError() {
        given()
                .contentType("application/json")
                .get("/person/{id}", 100).then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", equalTo(404), "message", equalTo("No person with provided id found"));

    }

    @Test
    public void testAddPerson() throws Exception {

        String payload = "{\n"
                + "  \"firstname\": \"Benjamin\", \n"
                + "  \"lastname\": \"Kongshaug\", \n"
                + "  \"phone\": \"20141614\"\n"
                + "}";

        given().contentType("application/json")
                .body(payload)
                .post("/person/add").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstname", equalTo("Benjamin"), "lastname", equalTo("Kongshaug"), "phone", equalTo("20141614"));
    }

    @Test
    public void testEditPerson() throws Exception {

        String payload = "{\n"
                + "  \"firstname\": \"Laura\", \n"
                + "  \"lastname\": \"Nielsen\", \n"
                + "  \"phone\": \"20141614\"\n"
                + "}";

        given().contentType("application/json")
                .body(payload)
                .put("/person/edit/" + p1.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstname", equalTo("Laura"), "lastname", equalTo("Nielsen"), "phone", equalTo("20141614"));
    }

    @Test
    public void testEditPersonError() {
           String payload = "{\n"
                + "  \"firstname\": \"Laura\", \n"
                + "  \"lastname\": \"Nielsen\", \n"
                + "  \"phone\": \"20141614\"\n"
                + "}";
           
        given()
                .body(payload)
                .contentType("application/json")
                .put("/person/edit/{id}", 100).then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", equalTo(404), "message", equalTo("Could not edit person, the provided Id not found"));

    }

    @Test
    public void testRemovePerson() throws Exception {

        given().contentType("application/json")
                .delete("/person/delete/" + p1.getId()).then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstname", equalTo("Laura"), "lastname", equalTo("Saxtrup"), "phone", equalTo("20141614"));
    }
    
     @Test
    public void testRemovePersonError() {
           
        given()
                .contentType("application/json")
                .delete("/person/delete/{id}", 100).then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", equalTo(404), "message", equalTo("Could not delete person, the provided Id not found"));

    }

}
