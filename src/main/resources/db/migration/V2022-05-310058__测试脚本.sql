DROP TABLE IF EXISTS `test_fly_db_migration`;
CREATE TABLE `test_fly_db_migration`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;



INSERT INTO `test_fly_db_migration` VALUES (1, 'aa', 'cc');
INSERT INTO `test_fly_db_migration` VALUES (2, 'bb', 'dd');
