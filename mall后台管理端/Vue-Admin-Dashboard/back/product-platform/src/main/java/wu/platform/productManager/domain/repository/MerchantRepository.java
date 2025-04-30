package com.mall.productplatform.domain.repository;

import com.mall.productplatform.domain.entity.Merchant;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MerchantRepository{
    Optional<Merchant> findById(Long id);
}
