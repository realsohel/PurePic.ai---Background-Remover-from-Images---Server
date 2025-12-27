package in.MohdSohel.Server_Background_Image_Remover.controllers;

import in.MohdSohel.Server_Background_Image_Remover.dto.UserDto;
import in.MohdSohel.Server_Background_Image_Remover.response.RemoveBgResponse;
import in.MohdSohel.Server_Background_Image_Remover.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/hello")
    public String hello(){
        return "Hello World";
    }

    @PostMapping("/createupdateuser")
    public ResponseEntity<RemoveBgResponse> createOrUpdate(@RequestBody UserDto userDto, Authentication authentication){
        RemoveBgResponse response = null;
        try {
            if(!authentication.getName().equals(userDto.getClerkId())){
                response =  RemoveBgResponse.builder()
                        .success(false)
                        .StatusCode(HttpStatus.FORBIDDEN)
                        .data("User does not have permission to access.")
                        .build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }
            UserDto user = userService.saveUser(userDto);
            response =  RemoveBgResponse.builder()
                    .success(true)
                    .StatusCode(HttpStatus.OK)
                    .data(user)
                    .build();
            return  ResponseEntity.status(HttpStatus.OK).body(response);

        }catch(Exception exception){
            response =  RemoveBgResponse.builder()
                    .success(false)
                    .StatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data(exception.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/credits")
    public ResponseEntity<RemoveBgResponse> getUserCredits(Authentication authentication){
        RemoveBgResponse response = null;
        log.info("Fetching user credits");
        try {
            if(authentication.getName().isEmpty() && authentication.getName()==null){
                log.error("Username is empty");
                response = RemoveBgResponse.builder()
                        .StatusCode(HttpStatus.FORBIDDEN)
                        .data("User does not have permission to access the resource.")
                        .success(false)
                        .build();
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
            }

            String clerkId = authentication.getName();
            UserDto user =  userService.getUserByClerkId(clerkId);
            Map<String,Integer> map = new HashMap<>();
            map.put("credits",user.getCredits());

            response = RemoveBgResponse.builder()
                    .StatusCode(HttpStatus.OK)
                    .data(map)
                    .success(true)
                    .build();

            log.info("Fetched user credits");
            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch(Exception exception){
            log.error("Fetching user credits failed");
            response = RemoveBgResponse.builder()
                    .StatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                    .data("Some error occured while getting the credits. Please try again later")
                    .success(false)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
