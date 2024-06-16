package application;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Application_0_always_pass_Tests {

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        System.out.println("\nsetUpBeforeClass() runs before all @Test methods");
    }

    @BeforeEach
    public void setUpBeforeEach() throws Exception {
        System.out.println("setUpBeforeEach() runs before each @Test method");
    }

    @AfterEach
    public void tearDownAfterEach() throws Exception {
        System.out.println("tearDownAfterEach() runs before each @Test method");
    }

    @Test
    @Order(001)
    void test_001_always_pass() {
        int expected = 10;
        int actual = 10;
        assertEquals(expected, actual);
    }

    @Test
    @Order(002)
    void test_002_always_pass() {
        int expected = 10;
        int actual = 10;
        assertEquals(expected, actual);
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
        System.out.println("tearDownAfterClass() runs after all @Test methods");
    }
}