package in.MohdSohel.Server_Background_Image_Remover.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.MohdSohel.Server_Background_Image_Remover.dto.UserDto;
import in.MohdSohel.Server_Background_Image_Remover.response.RemoveBgResponse;
import in.MohdSohel.Server_Background_Image_Remover.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhooks")
public class ClerkWebhookController {

    @Value("${clerk.webhook.secret}")
    private String webhookSecret;

    private final UserService userService;

    @PostMapping("/clerkwebhook")
    public ResponseEntity<?> handleClerkWebhook(@RequestHeader("svix-id") String svixId,
                                                @RequestHeader("svix-timestamp") String svixTimeStamp,
                                                @RequestHeader("svix-signature") String svixSignature,
                                                @RequestBody String payload){
        RemoveBgResponse response = null;
        try {
            boolean isValid = verifyWebhookSignature(svixId,svixTimeStamp,svixSignature,payload);

            if(!isValid){
                response = RemoveBgResponse.builder()
                        .StatusCode(HttpStatus.UNAUTHORIZED)
                        .data("Invalid webhook signature")
                        .success(false)
                        .build();

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payload);

            String eventType = jsonNode.path("type").asText();

            switch (eventType){
                case "user.created":
                    handleUserCreated(jsonNode.path("data"));
                    break;
                case "user.updated":
                    handleUserUpdated(jsonNode.path("data"));
                    break;
                case "user.deleted":
                    handleUserDeleted(jsonNode.path("data"));
                    break;
            }

            return ResponseEntity.ok().build();
        }
        catch (Exception e){
            response = RemoveBgResponse.builder()
                    .StatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(e.getMessage())
                    .success(false)
                    .build();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    private boolean verifyWebhookSignature(String svixId, String svixTimeStamp, String svixSignature, String payload){
//        TODO: Verify it Properly
        return true;
    }

    private void handleUserCreated(JsonNode data) {

            String clerkId = data.path("id").asText();
            String username = data.path("username").asText(null);
            String firstName = data.path("first_name").asText(null);
            String lastName = data.path("last_name").asText(null);

            // Resolve primary email safely
            String primaryEmailId = data.path("primary_email_address_id").asText();
            String email = null;

            for (JsonNode emailNode : data.path("email_addresses")) {
                if (emailNode.path("id").asText().equals(primaryEmailId)) {
                    email = emailNode.path("email_address").asText();
                    break;
                }
            }

            UserDto user = UserDto.builder()
                    .clerkId(clerkId)
                    .email(email)
                    .username(username)
                    .firstName(firstName)
                    .lastName(lastName)
                    .build();

            userService.saveUser(user);
        }


    private void handleUserUpdated(JsonNode data) {
        String clerkId = data.path("id").asText();
        String username = data.path("username").asText(null);
        String firstName = data.path("first_name").asText(null);
        String lastName = data.path("last_name").asText(null);
        String photoUrl = data.path("image_url").asText(null);
        String primaryEmailId = data.path("primary_email_address_id").asText();
        String email = null;

        for (JsonNode emailNode : data.path("email_addresses")) {
            if (emailNode.path("id").asText().equals(primaryEmailId)) {
                email = emailNode.path("email_address").asText();
                break;
            }
        }

        UserDto existingUser  = userService.getUserByClerkId(clerkId);

        existingUser.setEmail(email);
        existingUser.setFirstName(firstName);
        existingUser.setLastName(lastName);
        existingUser.setUsername(username);
        existingUser.setPhotoUrl(photoUrl);

        userService.saveUser(existingUser);
    }

    private void handleUserDeleted(JsonNode data) {
        String clerkId = data.path("id").asText();
        userService.deleteUserByClerkId(clerkId);
    }
}

