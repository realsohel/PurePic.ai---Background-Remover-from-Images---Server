package in.MohdSohel.Server_Background_Image_Remover.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String clerkId;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    private String email;
    private String firstName;
    private String lastName;
    private String photoUrl;
    private Integer credits;

    @PrePersist
    public void prePersist(){
        if(credits==null){
            credits=5;
        }
    }


}
