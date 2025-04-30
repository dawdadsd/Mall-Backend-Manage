package wu.platform.productManager.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.HashSet;
import java.util.Set;

/**
 * 商品分类实体
 * 支持树形结构的分类体系
 */
@Entity
@Table(name = "categories")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "level")
    private Integer level;
    
    @Column(name = "sort_order")
    private Integer sortOrder;
    
    @Column(name = "icon_url")
    private String iconUrl;
    
    @Column(name = "is_enabled")
    private Boolean isEnabled;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;
    
    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OrderBy("sortOrder asc")
    private Set<Category> children = new HashSet<>();
    
    /**
     * 添加子分类
     */
    public Category addChild(Category child)
    {
        children.add(child);
        child.setParent(this);
        child.setLevel(this.getLevel() != null ? this.getLevel() + 1 : 1);
        return this;
    }

    
    /**
     * 移除子分类
     */
    public Category removeChild(Category child) {
        children.remove(child);
        child.setParent(null);
        return this;
    }
    
    /**
     * 检查是否是叶子节点
     */
    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }
    
    /**
     * 检查是否是根节点
     */
    public boolean isRoot() {
        return parent == null;
    }
    
    /**
     * 启用分类
     */
    public void enable() {
        this.isEnabled = true;
    }
    
    /**
     * 禁用分类
     */
    public void disable() {
        this.isEnabled = false;
    }
} 