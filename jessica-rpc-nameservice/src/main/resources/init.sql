CREATE TABLE `t_name_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `service_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '服务名称，全类名',
  `uri` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '服务地址，地址',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='以mysql为注册中心样例';