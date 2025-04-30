package wu.platform.productManager.infrastructure.repository.jpa;

import wu.platform.productManager.domain.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantJpaRepository extends JpaRepository<Merchant,Long> {
}
