package in.MohdSohel.Server_Background_Image_Remover.services.impl;

import in.MohdSohel.Server_Background_Image_Remover.dto.UserDto;
import in.MohdSohel.Server_Background_Image_Remover.entities.User;
import in.MohdSohel.Server_Background_Image_Remover.repositories.UserRepository;
import in.MohdSohel.Server_Background_Image_Remover.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto saveUser(UserDto userDto) {
        log.info("UserServiceImpl saveUser");
        Optional<User> optionalUser = userRepository.findByClerkId(userDto.getClerkId());

        if(optionalUser.isPresent()){
            log.info("User is already present..");
            User existingUser = optionalUser.get();

            existingUser.setEmail(userDto.getEmail());
            existingUser.setUsername(userDto.getUsername());
            existingUser.setFirstName(userDto.getFirstName());
            existingUser.setLastName(userDto.getLastName());
            existingUser.setPhotoUrl(userDto.getPhotoUrl());

            if(userDto.getCredits() != null){
                existingUser.setCredits(userDto.getCredits());
            }
            existingUser=userRepository.save(existingUser);

            log.info(("Saved Existing User.."));
            return entityToDto(existingUser);
        }

        log.info(("New user is creating.."));
        User newUser = dtoToEntity(userDto);
        userRepository.save(newUser);

        log.info(("Saved New User.."));
        return entityToDto(newUser);
    }

    private User dtoToEntity(UserDto userDto) {
        return User.builder()
                .clerkId(userDto.getClerkId())
                .username(userDto.getUsername())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .photoUrl(userDto.getPhotoUrl())
                .build();
    }

    private UserDto entityToDto(User user) {
        return UserDto.builder()
                .clerkId(user.getClerkId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .photoUrl(user.getPhotoUrl())
                .credits(user.getCredits())
                .build();
    }
}
