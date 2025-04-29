INSERT INTO product_data_quality (id, product_id, clean_status, data_source, data_version, problem_count, problem_details, quality_score, source_system, create_time, update_time, last_clean_time)
VALUES
(1, 1, 'completed', 'ERP', 'v1.0', 0, NULL, 95, 'systemA', NOW(), NOW(), NOW()),
(2, 2, 'completed', 'ERP', 'v1.0', 1, '描述不完整', 90, 'systemA', NOW(), NOW(), NOW()),
(3, 3, 'in_progress', 'WMS', 'v1.1', 2, '图片缺失;价格异常', 85, 'systemB', NOW(), NOW(), NOW()),
(4, 4, 'pending', 'OMS', 'v1.0', 0, NULL, 92, 'systemC', NOW(), NOW(), NOW()),
(5, 5, 'verified', 'ERP', 'v1.2', 1, '库存异常', 88, 'systemA', NOW(), NOW(), NOW()),
(6, 6, 'completed', 'ERP', 'v1.0', 0, NULL, 97, 'systemA', NOW(), NOW(), NOW()),
(7, 7, 'failed', 'WMS', 'v1.1', 3, '描述不全;图片模糊;价格异常', 70, 'systemB', NOW(), NOW(), NOW()),
(8, 8, 'completed', 'OMS', 'v1.0', 0, NULL, 93, 'systemC', NOW(), NOW(), NOW()),
(9, 9, 'verified', 'ERP', 'v1.2', 1, '主图缺失', 89, 'systemA', NOW(), NOW(), NOW()),
(10, 10, 'completed', 'ERP', 'v1.0', 0, NULL, 96, 'systemA', NOW(), NOW(), NOW());