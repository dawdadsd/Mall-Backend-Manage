package wu.platform.productManager.infrastructure.repository;

import wu.platform.productManager.domain.entity.Merchant;
import wu.platform.productManager.domain.repository.MerchantRepository;
import wu.platform.productManager.infrastructure.repository.jpa.MerchantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商家仓储实现类
 * 负责商家实体的持久化和查询
 */
@Repository
@RequiredArgsConstructor
public class MerchantRepositoryImpl implements MerchantRepository {
    
    private final MerchantJpaRepository merchantJpaRepository;
    
    @Override
    public Optional<Merchant> findById(Long id) {
        return merchantJpaRepository.findById(id);
    }
    
    @Override
    public Merchant save(Merchant merchant) {
        return merchantJpaRepository.save(merchant);
    }
    
    @Override
    public List<Merchant> findAll() {
        return merchantJpaRepository.findAll();
    }
    
    @Override
    public Page<Merchant> findAll(Pageable pageable) {
        return merchantJpaRepository.findAll(pageable);
    }
    
    @Override
    public void delete(Merchant merchant) {
        merchantJpaRepository.delete(merchant);
    }
    
    @Override
    public void deleteById(Long id) {
        merchantJpaRepository.deleteById(id);
    }
}
