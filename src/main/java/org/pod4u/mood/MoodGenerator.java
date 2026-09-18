package org.pod4u.mood;

import org.pod4u.serialisation.MoodDeserialiser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import java.io.IOException;

public class MoodGenerator {
    private Mood mood;

    public MoodGenerator() {

    }

    public void uploadPhoto(Path path, MoodDeserialiser deserialiser) throws IOException, InterruptedException {
        String BASE_URL = "https://api-us.faceplusplus.com/facepp/v3/detect";
        String boundaryString = UUID.randomUUID().toString();
        ArrayList<byte[]> parts = new ArrayList<>();
        try {
            String base64image = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
            HttpClient client = HttpClient.newHttpClient();
            HashMap<String, String> map = new HashMap<>();
            map.put("api_key", System.getenv("FACE_PLUS_API_KEY"));
            map.put("api_secret", System.getenv("FACE_PLUS_API_SECRET"));
            //map.put("face_tokens", System.getenv("FACE_TOKEN"));
            map.put("return_attributes", "emotion,beauty,skinstatus,mouthstatus,eyegaze,smiling,eyestatus,headpose");
            for (Map.Entry<String, String> entry : map.entrySet())
                parts.add(("--" + boundaryString + "\r\nContent-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n").getBytes());
            parts.add(("--" + boundaryString + "\r\nContent-Disposition: form-data; name=\"image_base64\"\r\n\r\n" + base64image + "\r\n--" + boundaryString + "--").getBytes());
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofByteArrays(parts);
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL)).header("Accept", "*/*").header("Content-Type", "multipart/form-data; boundary=" + boundaryString).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            IO.println(response.body());
            this.mood = deserialiser.deserialiseMood(response.body());
        } catch(Exception e) {
            IO.println(e);
        }
    }

    public Mood getMood() {
        return this.mood;
    }
}
