package wu.platform.productManager.domain.repository;

import wu.platform.productManager.domain.entity.Merchant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 商家仓储接口
 * 定义领域模型 Merchant 的持久化和检索能力
 */
@Repository
public interface MerchantRepository {
    /**
     * 根据ID查找商家
     * @param id 商家ID
     * @return 可能包含商家的 Optional
     */
    Optional<Merchant> findById(Long id);
    
    /**
     * 保存商家
     * @param merchant 商家实体
     * @return 持久化后的商家实体
     */
    Merchant save(Merchant merchant);
    
    /**
     * 查找所有商家
     * @return 商家列表
     */
    List<Merchant> findAll();
    
    /**
     * 分页查询所有商家
     * @param pageable 分页参数
     * @return 商家分页结果
     */
    Page<Merchant> findAll(Pageable pageable);
    
    /**
     * 删除商家
     * @param merchant 商家实体
     */
    void delete(Merchant merchant);
    
    /**
     * 根据ID删除商家
     * @param id 商家ID
     */
    void deleteById(Long id);
}
