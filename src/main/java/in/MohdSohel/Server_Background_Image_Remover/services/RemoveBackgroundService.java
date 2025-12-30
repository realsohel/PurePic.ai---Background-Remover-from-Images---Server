package in.MohdSohel.Server_Background_Image_Remover.services;

import org.springframework.web.multipart.MultipartFile;

public interface RemoveBackgroundService {

    byte[] removeBackground(MultipartFile file);
}
