package in.MohdSohel.Server_Background_Image_Remover.services;


import in.MohdSohel.Server_Background_Image_Remover.dto.UserDto;

public interface UserService {

    UserDto saveUser(UserDto userDto);
    UserDto getUserByClerkId(String clerkId);
    void deleteUserByClerkId(String clerkId);
}
