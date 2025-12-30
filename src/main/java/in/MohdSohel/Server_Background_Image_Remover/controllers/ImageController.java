package in.MohdSohel.Server_Background_Image_Remover.controllers;

import in.MohdSohel.Server_Background_Image_Remover.dto.UserDto;
import in.MohdSohel.Server_Background_Image_Remover.response.RemoveBgResponse;
import in.MohdSohel.Server_Background_Image_Remover.services.RemoveBackgroundService;
import in.MohdSohel.Server_Background_Image_Remover.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final RemoveBackgroundService removeBackgroundService;
    private final UserService userService;

    @PostMapping("/remove-background")
    public ResponseEntity<?> removeBackground(@RequestParam("file")MultipartFile file, Authentication authentication){
        log.info("Generating image");
        RemoveBgResponse response = null;
        Map<String, Object> responseMap = new HashMap<>();
        try {
            if (authentication == null || authentication.getName() == null || authentication.getName().isEmpty()) {
                log.error("Username is empty");
                response = RemoveBgResponse.builder()
                        .StatusCode(HttpStatus.FORBIDDEN)
                        .data("User does not have permission to access the resource.")
                        .success(false)
                        .build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            UserDto userDTO = userService.getUserByClerkId(authentication.getName());

            if(userDTO.getCredits()==0){
                log.error("No Credits left while generating the image");
                responseMap.put("message", "No credits left. Please purchase.");
                responseMap.put("creditBalance: ", userDTO.getCredits());

                response = RemoveBgResponse.builder()
                        .success(false)
                        .data(responseMap)
                        .StatusCode(HttpStatus.OK)
                        .build();
                return ResponseEntity.ok(response);
            }

            byte[] imageBytes = removeBackgroundService.removeBackground(file);     
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            userDTO.setCredits(userDTO.getCredits()-1);
            userService.saveUser(userDTO);

            log.info("Background has been successfully removed from the image.");
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(base64Image);

        }catch (Exception e){
            log.error("Creating Images failed");
            response = RemoveBgResponse.builder()
                    .StatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data("Some error occured while creating the image. Please try again later")
                    .success(false)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
