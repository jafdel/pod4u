package org.pod4u.account;

import at.favre.lib.crypto.bcrypt.BCrypt;

import org.pod4u.database.P4UDatabaseConnection;

import com.sun.net.httpserver.HttpExchange;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.InetSocketAddress;
import java.net.URI;
import java.awt.*;
import java.io.OutputStream;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;

import java.util.Random;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Authenticator {
    private SpotifyAccount account;
    private String email = "";
    private static boolean generatorReady = false;
    private final static String clientId = System.getenv("SPOTIFY_CLIENT_ID");
    private final static String codeChallenge = System.getenv("CODE_CHALLENGE");
    private final static String codeVerifier = System.getenv("CODE_VERIFIER");
    private static String accessToken;

    public boolean login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE email=?";
        Connection conn = P4UDatabaseConnection.getConnection();
        var stmt = conn.prepareStatement(sql);
        stmt.setObject(1, email);
        var rs = stmt.executeQuery();
        if (rs.next()) {
            String hash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
            if (BCrypt.verifyer().verify(password.toCharArray(), hash).verified) {
                this.email = email;
                return true;
            }
        }
        return false;
    }

    public boolean register(String firstName, String lastName, String email, String password) throws SQLException {
        String hash = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        String sql = "INSERT INTO Member (firstName, lastName, email, password) VALUES (?, ?, ?, ?)";
        Connection conn = P4UDatabaseConnection.getConnection();
        var stmt = conn.prepareStatement(sql);
        stmt.setObject(1, firstName);
        stmt.setObject(2, lastName);
        stmt.setObject(3, email);
        stmt.setObject(4, hash);
        return stmt.executeUpdate() > 0;
    }

    public void connect() throws InterruptedException, IOException {
        HttpClient client = HttpClient.newHttpClient();
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new Authenticator.RootHandler());
        server.start();
        String state = "";
        Random rand = new Random();
        for (int i=0; i<16;i++)
            state = state.concat(String.valueOf(rand.nextInt(9)));
        String url = "https://accounts.spotify.com/authorize?redirect_uri=http://127.0.0.1:8000/callback&client_id=" + Authenticator.clientId + "&response_type=code&code_challenge=" + Authenticator.codeChallenge + "&code_challenge_method=S256&scope=user-read-playback-state,user-modify-playback-state,streaming,user-read-currently-playing,user-library-read,user-read-private,user-read-email,playlist-read-private,user-top-read&state=" + state;
        HttpRequest initialRequest = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/x-www-form-urlencoded").GET().build();
        HttpResponse<String> response = client.send(initialRequest, HttpResponse.BodyHandlers.ofString());
        Desktop.getDesktop().browse(URI.create(url));
    }

    public void logout() {

    }

    public static boolean generatorReady() {
        return Authenticator.generatorReady;
    }

    static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException, IllegalStateException {
            if (Authenticator.clientId.length()==32 && exchange.getRequestHeaders().toString().contains("Sec-fetch-user=[?1]") && !Authenticator.generatorReady) {
                String output = "Hello, world!";
                exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
                exchange.sendResponseHeaders(200, output.getBytes().length);
                try (OutputStream os = exchange.getResponseBody()) {
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest tokenRequest = HttpRequest.newBuilder().uri(URI.create("https://accounts.spotify.com/api/token")).header("Content-Type", "application/x-www-form-urlencoded").POST(HttpRequest.BodyPublishers.ofString("redirect_uri=http://127.0.0.1:8000/callback&client_id=" + Authenticator.clientId +"&grant_type=authorization_code&" + exchange.getRequestURI().getQuery().split("&state=")[0] + "&code_verifier=" + Authenticator.codeVerifier)).build();
                    HttpResponse<String> response = client.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
                    Authenticator.accessToken = response.body().split("access_token\":\"")[1].split("\"")[0];
                    HttpRequest apiRequest = HttpRequest.newBuilder().uri(URI.create("https://api.spotify.com/v1/me")).header("Authorization", "Bearer " + Authenticator.accessToken).header("Content-Type", "application/x-www-form-urlencoded").GET().build();
                    response = client.send(apiRequest, HttpResponse.BodyHandlers.ofString());
                    IO.println(response.body());
                    SpotifyAccount.setUsername(response.body());
                    int offset = 0;
                    HttpRequest trackRequest;
                    do {
                        trackRequest = HttpRequest.newBuilder().uri(URI.create("https://api.spotify.com/v1/me/tracks?limit=50&offset=" + offset)).header("Authorization", "Bearer " + Authenticator.accessToken).header("Content-Type", "application/x-www-form-urlencoded").GET().build();
                        response = client.send(trackRequest, HttpResponse.BodyHandlers.ofString());
                        SpotifyAccount.addToTrackList(response.body());
                        IO.println(response.body());
                        offset = SpotifyAccount.getTrackListSize();
                    } while(offset<1000 && offset%50==0 && offset>0 && response.statusCode()==200);
                    Authenticator.generatorReady = true;
                } catch (InterruptedException e) {
                    IO.println(e);
                }
            } else if (Authenticator.clientId.length()!=32)
                throw new IllegalStateException("Error: invalid environment variable. Please set the Spotify client ID to a 32-character hexadecimal string.");
        }
    }

    public SpotifyAccount getAccount() {
        return this.account;
    }

    public String getEmail() {
        return this.email;
    }

    public String getToken() {
        return Authenticator.accessToken;
    }
}
