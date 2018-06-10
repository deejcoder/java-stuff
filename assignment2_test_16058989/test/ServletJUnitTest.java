/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
    @Author: Dylan Tonks (16058989)
    Advanced Web Development
    Assignment #2
*/

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;


public class ServletJUnitTest {
    
    public static final String SERVER_URL = "http://localhost:8084/assignment2_server_16058989/";
    
    private static HttpClient http;
    private static CookieStore cookieStore;
    
    public ServletJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        cookieStore = new BasicCookieStore();
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);
        http = builder.build();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {

    }
    
    @After
    public void tearDown() {
        System.out.println("<<==\n");
    }

    /**
     * Start tests the ability to start new games, make a move and get the game board.
     * @throws IOException 
     */
    @Test
    public void start() throws IOException {
        System.out.println("=>> Executing START test");
        
        HttpPost post = new HttpPost(SERVER_URL + "ttt/istart");
        HttpResponse response = http.execute(post);
        
        assertTrue(response.getStatusLine().getStatusCode() == 200);
        
        boolean session = false;
        for(Cookie cookie : cookieStore.getCookies()) {
            if(cookie.getName().equals("JSESSIONID")) session = true;
        }
        assertTrue(session);

    }
    
    /**
     * Similar to start(), except the computer is expected to move first,
     * to therefore it gets the state afterwards.
     * @throws IOException 
     */
    @Test
    public void ustart() throws IOException {
        System.out.println("=>> Executing USTART test");
        
        HttpPost post = new HttpPost(SERVER_URL + "ttt/ustart");
        HttpResponse response = http.execute(post);
        assertTrue(response.getStatusLine().getStatusCode() == 200);
        
        boolean session = false;
        for(Cookie cookie : cookieStore.getCookies()) {
            if(cookie.getName().equals("JSESSIONID")) session = true;
        }
        assertTrue(session);
        
        state();
    }
    
    /**
     * State tests the ability to get the game board.
     * @throws IOException 
     */
    @Test
    public void state() throws IOException {
        System.out.println("=>> Executing STATE test");
        
        HttpGet request = new HttpGet(SERVER_URL + "ttt/state");
        HttpResponse response = http.execute(request);
        
        assertTrue(response.getStatusLine().getStatusCode() == 200);
        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent())
        );
        
        String line = "";
        int lcount = 0;
        while((line = rd.readLine()) != null) {
            if(line.equals("")) continue;
            System.out.println(line);
            assertTrue(line.trim().length() == 3);
            lcount++;
        }
        
        assertTrue(lcount == 3);
    }
    
    /**
     * Fires a move at the server
     * @throws IOException 
     */
    @Test
    public void move() throws IOException {
        System.out.println("=>> Executing MOVE test");
        
        start();
        Random random = new Random();
        int x = random.nextInt(2);
        int y = random.nextInt(2);
        HttpPost post = new HttpPost(SERVER_URL + "ttt/move/x" + x + "y" + y);
        HttpResponse response = http.execute(post);
        assertTrue(response.getStatusLine().getStatusCode() == 200);
        state();
    }
    
    /**
     * Starts a new board, makes a valid move then an invalid move and prints out the state.
     * @throws IOException 
     */
    @Test
    public void invalidMove() throws IOException {
        System.out.println("=>> Executing invalidMOVE test");

        start();
        move();
        HttpPost post = new HttpPost(SERVER_URL + "ttt/move/x3y1");
        HttpResponse response = http.execute(post);
        System.out.println(response.getStatusLine().getStatusCode());
        assertTrue(response.getStatusLine().getStatusCode() == 400);
        state();
        
    }
    
    /**
     * Tests for the Winner Servlet, if it returns a winner.
     * Computer starts, then requests the winner
     * @throws IOException 
     */
    @Test
    public void winner() throws IOException {
        System.out.println("=>> Executing WINNER test");
        
        ustart();
        HttpGet get = new HttpGet(SERVER_URL + "ttt/won");
        HttpResponse response = http.execute(get);
        System.out.println(response.getStatusLine().getStatusCode());
        
        BufferedReader rd = new BufferedReader(
            new InputStreamReader(response.getEntity().getContent())
        );
        
        System.out.println(rd.readLine());
        //Don't want it to be stuck in an infinite loop...
        rd.close();
    }
    
    /**
     * Tests the PossibleMoves Servlet, tells the computer to start
     * then requests the remaining possible moves. Eight possible moves is
     * expected.
     * @throws IOException 
     */
    @Test
    public void possiblemoves() throws IOException {
        System.out.println("=>> Executing POSSIBLEMOVES test");
        
        ustart();
        HttpGet get = new HttpGet(SERVER_URL + "ttt/possiblemoves");
        HttpResponse response = http.execute(get);
        assertTrue(response.getStatusLine().getStatusCode() == 200);
        
        
        BufferedReader rd = new BufferedReader(
            new InputStreamReader(response.getEntity().getContent())
        );
        
        String line = "";
        int count = 0;
        while((line = rd.readLine()) != null) {
            System.out.println(line);
            count++;
        }
        count--; //last line is \n
        
        assertTrue(count == 7); //There should only be 8 moves left, 0 inclusive
        
        
    }

}
