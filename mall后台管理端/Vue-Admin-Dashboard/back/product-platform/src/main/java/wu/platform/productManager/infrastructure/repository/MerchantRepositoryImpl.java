package com.mall.productplatform.infrastructure.repository;

import com.mall.productplatform.domain.entity.Merchant;
import com.mall.productplatform.domain.repository.MerchantRepository;
import com.mall.productplatform.infrastructure.repository.jpa.MerchantJpaRepository;
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
