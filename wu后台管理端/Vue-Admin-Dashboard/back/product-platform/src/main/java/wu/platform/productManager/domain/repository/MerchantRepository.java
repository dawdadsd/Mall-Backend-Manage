package wu.platform.productManager.domain.repository;

import wu.platform.productManager.domain.entity.Merchant;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MerchantRepository{
    Optional<Merchant> findById(Long id);
}
