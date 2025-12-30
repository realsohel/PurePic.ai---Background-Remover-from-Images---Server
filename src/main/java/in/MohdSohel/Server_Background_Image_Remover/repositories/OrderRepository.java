package in.MohdSohel.Server_Background_Image_Remover.repositories;

import in.MohdSohel.Server_Background_Image_Remover.entities.OrderEnt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEnt,Long> {

    Optional<OrderEnt> findByOrderId(String orderid);
}
