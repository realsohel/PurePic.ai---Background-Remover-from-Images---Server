package in.MohdSohel.Server_Background_Image_Remover.repositories;

import in.MohdSohel.Server_Background_Image_Remover.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByClerkId(String clerkId);
    Optional<User> findByClerkId(String clerkId);
}
