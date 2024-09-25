package com.opendart.user_service.service;

import java.util.ArrayList;
import java.util.List;
import java.net.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opendart.user_service.entity.ShoppingCart;
import com.opendart.user_service.entity.User;
import com.opendart.user_service.payload.request.LoginRequest;
import com.opendart.user_service.payload.request.SignupRequest;
import com.opendart.user_service.payload.request.UpdateUserRequest;
import com.opendart.user_service.payload.response.JwtResponse;
import com.opendart.user_service.payload.response.MessageResponse;
import com.opendart.user_service.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import reactor.core.publisher.Mono;

@Service
public class UserService2 {
    @Value("${jwt.issuer_uri}")
    String jwtIssuerUri;

    @Value("${jwt.client_id}")
    String jwtClientId;

    @Value("${jwt.client_secret}")
    String jwtClientSecret;

    @Value("${jwt.grant_type}")
    String jwtGrantType;

    @Value("${jwt.scope}")
    String jwtScope;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;


    @SuppressWarnings("unused")
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        User user;
        try {
            user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("User not found!"));
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("User credentials are not valid"));
        }

        HttpClient httpClient = HttpClients.createDefault();


        String accessToken = "";
        try {
            String sorgu = jwtIssuerUri.replaceAll(" ", "%20");


            List<BasicNameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("grant_type", jwtGrantType));
            params.add(new BasicNameValuePair("client_id", jwtClientId));
            params.add(new BasicNameValuePair("client_secret", jwtClientSecret));
            params.add(new BasicNameValuePair("scope", jwtScope));
            //Uri sorguUri ="http://localhost:8080/realms/java-microservice-realm/protocol/openid-connect/token?grant_type=client_credentials&client_id=spring-cloud-client&client_secret=AJufwN1eof8P5Gga5Y9atCFqdMuZEuKd&scope=openid offline_access";
            // HttpPost httpPost = new HttpPost(sorgu);
            //HttpPost httpPost = new HttpPost("http://localhost:8080/realms/java-microservice-realm/protocol/openid-connect/token?grant_type=client_credentials&client_id=spring-cloud-client&client_secret=AJufwN1eof8P5Gga5Y9atCFqdMuZEuKd&scope=openid offline_access");
            // httpPost.setEntity(new UrlEncodedFormEntity(params));
            //HttpResponse response = httpClient.execute(httpPost);

            // Handle the response
            // String responseBody = EntityUtils.toString(response.getEntity());
            WebClient client = WebClient.create();

            MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();

            bodyValues.add("grant_type", jwtGrantType);
            bodyValues.add("client_id", jwtClientId);
            bodyValues.add("client_secret", jwtClientSecret);
            bodyValues.add("scope", jwtScope);

            String gelenCevap = client.post()
                    .uri(new URI(jwtIssuerUri))
                    //.header("Authorization", "SECRET_TOKEN")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromMultipartData(bodyValues))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            accessToken = extractAccessToken(gelenCevap);
            // accessToken = extractAccessToken(responseBody);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity
                .ok(new JwtResponse(accessToken, user.getId(), user.getUsername(), user.getEmail()));
    }

    private static String extractAccessToken(String jsonResponse) {
        // Use Jackson for JSON parsing
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            return rootNode.path("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is already in use!"));
        }

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        // Create new user's account
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    public ResponseEntity<?> deleteUser(Long userId) {
        try {
            // Check if the user exists
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found!"));

            try {
                // Check user's shopping cart
                ShoppingCart shoppingCart = restTemplate.getForObject(
                        "http://SHOPPING-CART-SERVICE/api/shopping-cart/by-name/" + user.getUsername(),
                        ShoppingCart.class);

                restTemplate.delete("http://SHOPPING-CART-SERVICE/api/shopping-cart/" + shoppingCart.getId());
            } catch (Exception e) {
                // If shopping cart not found, continue with user deletion
            }

            // Delete the user
            userRepository.delete(user);

            return ResponseEntity.ok(new MessageResponse("User account deleted successfully!"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new MessageResponse("Internal Server Error"));
        }
    }

    public ResponseEntity<?> updateUser(Long userId, UpdateUserRequest updateUserRequest) {
        try {
            // Check if the user exists
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found!"));

            // Update password if provided
            if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().isEmpty()) {
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(encoder.encode(updateUserRequest.getPassword()));
            }

            // Update email if provided
            if (updateUserRequest.getEmail() != null && !updateUserRequest.getEmail().isEmpty()) {
                if (userRepository.existsByEmail(updateUserRequest.getEmail())) {
                    return ResponseEntity.badRequest().body(new MessageResponse("Email is already in use!"));
                }
                user.setEmail(updateUserRequest.getEmail());
            }

            // Save the updated user
            userRepository.save(user);

            return ResponseEntity.ok(new MessageResponse("User account updated successfully!"));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(new MessageResponse("Internal Server Error"));
        }
    }
}