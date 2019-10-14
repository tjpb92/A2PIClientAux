package a2piclientaux;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Classe pour test A2PIClientAux
 *
 * @author Thierry Baribaud
 * @version 1.05
 */
public class A2PIClientAuxTest {

    public A2PIClientAuxTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void shouldReturnStatusOkay() {
        HttpResponse<JsonNode> jsonResponse;

        try {

            jsonResponse = Unirest.get("http://www.mocky.io/v2/5a9ce37b3100004f00ab5154")
                    .header("accept", "application/json").queryString("apiKey", "123")
                    .asJson();
            System.out.println("jsonResponse:"+jsonResponse.getBody()+", status:"+jsonResponse.getStatus());
            assertNotNull(jsonResponse.getBody());
            assertEquals(200, jsonResponse.getStatus());
        } catch (UnirestException ex) {
            Logger.getLogger(A2PIClientAuxTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }

    }
}
