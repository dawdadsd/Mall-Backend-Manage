package wu.platform.productManager.infrastructure.repository;

import wu.platform.productManager.domain.entity.Merchant;
import wu.platform.productManager.domain.repository.MerchantRepository;
import wu.platform.productManager.infrastructure.repository.jpa.MerchantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MerchantRepositoryImpl implements MerchantRepository {
    
    private final MerchantJpaRepository merchantJpaRepository;
    
    @Override
    public Optional<Merchant> findById(Long id) {
        return merchantJpaRepository.findById(id);
    }
}
