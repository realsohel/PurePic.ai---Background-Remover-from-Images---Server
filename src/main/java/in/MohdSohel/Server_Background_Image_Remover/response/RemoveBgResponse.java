package in.MohdSohel.Server_Background_Image_Remover.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RemoveBgResponse {

    private boolean success;
    private HttpStatus StatusCode;
    private Object data;
}
