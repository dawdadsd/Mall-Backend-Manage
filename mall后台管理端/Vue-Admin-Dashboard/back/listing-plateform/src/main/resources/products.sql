INSERT INTO products (id, code, name, category_id, brand_id, supplier_id, cost_price, original_price, promotion_price, selling_price, stock, warning_stock, status, create_time, update_time)
VALUES
(1, 'P001', '智能手机', 1, 1, 1, 2000.00, 2500.00, 2200.00, 2300.00, 100, 10, 'published', NOW(), NOW()),
(2, 'P002', '家用电饭煲', 2, 2, 2, 150.00, 200.00, 180.00, 190.00, 50, 5, 'published', NOW(), NOW()),
(3, 'P003', '运动鞋', 3, 3, 3, 300.00, 400.00, 350.00, 360.00, 80, 8, 'approved', NOW(), NOW()),
(4, 'P004', '儿童玩具', 4, 4, 4, 50.00, 80.00, 60.00, 70.00, 200, 20, 'draft', NOW(), NOW()),
(5, 'P005', '护肤面膜', 5, 5, 5, 20.00, 30.00, 25.00, 28.00, 300, 30, 'pending', NOW(), NOW()),
(6, 'P006', '蓝牙耳机', 1, 1, 6, 100.00, 150.00, 120.00, 130.00, 120, 12, 'published', NOW(), NOW()),
(7, 'P007', '羽毛球拍', 3, 3, 7, 80.00, 120.00, 100.00, 110.00, 60, 6, 'unpublished', NOW(), NOW()),
(8, 'P008', '汽车坐垫', 8, 8, 8, 60.00, 100.00, 80.00, 90.00, 40, 4, 'approved', NOW(), NOW()),
(9, 'P009', '数码相机', 9, 9, 9, 1200.00, 1500.00, 1300.00, 1400.00, 30, 3, 'published', NOW(), NOW()),
(10, 'P010', '家用空调', 10, 10, 10, 2000.00, 2500.00, 2200.00, 2300.00, 20, 2, 'approved', NOW(), NOW());