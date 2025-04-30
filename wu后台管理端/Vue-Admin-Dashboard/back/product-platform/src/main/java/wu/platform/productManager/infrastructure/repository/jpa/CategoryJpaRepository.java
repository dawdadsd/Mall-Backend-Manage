package wu.platform.productManager.infrastructure.repository.jpa;

import wu.platform.productManager.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 分类JPA仓储接口
 * 提供基础的CRUD操作和自定义查询
 */
public interface CategoryJpaRepository extends JpaRepository<Category, Long> {

    /**
     * 查找顶级分类（没有父分类的分类）
     */
    List<Category> findByParentIsNull();

    /**
     * 根据父分类ID查找子分类
     */
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId")
    List<Category> findByParentId(@Param("parentId") Long parentId);

    /**
     * 根据分类名称模糊查询
     */
    List<Category> findByNameContaining(String name);

    /**
     * 查询指定层级的分类
     */
    List<Category> findByLevel(Integer level);

    /**
     * 根据父分类ID和排序值查询分类
     */
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId ORDER BY c.sortOrder ASC")
    List<Category> findByParentIdOrderBySortOrder(@Param("parentId") Long parentId);
} 