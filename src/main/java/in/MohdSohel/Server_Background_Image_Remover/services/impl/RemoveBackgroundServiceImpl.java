package in.MohdSohel.Server_Background_Image_Remover.services.impl;

import in.MohdSohel.Server_Background_Image_Remover.clients.ClipDropClient;
import in.MohdSohel.Server_Background_Image_Remover.services.RemoveBackgroundService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RemoveBackgroundServiceImpl implements RemoveBackgroundService {

    @Value("${clipdrop.api-key}")
    private String apiKey;

    private final ClipDropClient clipDropClient;
    @Override
    public byte[] removeBackground(MultipartFile file) {
        return clipDropClient.removeBackground(file, apiKey);
    }
}
