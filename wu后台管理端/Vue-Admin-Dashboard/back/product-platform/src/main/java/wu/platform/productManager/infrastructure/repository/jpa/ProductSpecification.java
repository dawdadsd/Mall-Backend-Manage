package wu.platform.productManager.infrastructure.repository.jpa;

import wu.platform.productManager.domain.entity.Category;
import wu.platform.productManager.domain.entity.Merchant;
import wu.platform.productManager.domain.entity.Product;
import wu.platform.productManager.domain.vo.ProductStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> byCriteria(Long categoryId, Long merchantId, ProductStatus status, String keyword) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (categoryId != null) {
                Join<Product, Category> categoryJoin = root.join("category");
                predicates.add(criteriaBuilder.equal(categoryJoin.get("id"), categoryId));
            }

            if (merchantId != null) {
                Join<Product, Merchant> merchantJoin = root.join("merchant");
                predicates.add(criteriaBuilder.equal(merchantJoin.get("id"), merchantId));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (StringUtils.hasText(keyword)) {
                // Search in product name and description (can be extended)
                Predicate namePredicate = criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
                Predicate descriptionPredicate = criteriaBuilder.like(root.get("description"), "%" + keyword + "%");
                // Add more fields to search if needed (e.g., tags, SKU codes)
                predicates.add(criteriaBuilder.or(namePredicate, descriptionPredicate));
            }
            
            // Combine predicates with AND
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
} 