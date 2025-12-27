package in.MohdSohel.Server_Background_Image_Remover.controllers;

import in.MohdSohel.Server_Background_Image_Remover.dto.UserDto;
import in.MohdSohel.Server_Background_Image_Remover.response.RemoveBgResponse;
import in.MohdSohel.Server_Background_Image_Remover.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor

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
}
