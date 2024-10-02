/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80032 (8.0.32)
 Source Host           : localhost:3306
 Source Schema         : university_attendance

 Target Server Type    : MySQL
 Target Server Version : 80032 (8.0.32)
 File Encoding         : 65001

 Date: 02/10/2024 22:12:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for attendanceappeal
-- ----------------------------
DROP TABLE IF EXISTS `attendanceappeal`;
CREATE TABLE `attendanceappeal`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `student_id` bigint NOT NULL,
                                     `course_id` bigint NOT NULL,
                                     `appeal_begin_time` datetime NULL DEFAULT NULL,
                                     `appeal_end_time` datetime NULL DEFAULT NULL,
                                     `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                     `reason` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                     `image` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '考勤申诉图片',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `attendanceappeal_course_detail_id_fk`(`course_id` ASC) USING BTREE,
                                     INDEX `attendanceappeal_student_id_fk`(`student_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考勤申请' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of attendanceappeal
-- ----------------------------

-- ----------------------------
-- Table structure for course_attendance
-- ----------------------------
DROP TABLE IF EXISTS `course_attendance`;
CREATE TABLE `course_attendance`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `student_id` bigint NOT NULL,
                                      `course_id` bigint NOT NULL,
                                      `status` bigint NOT NULL COMMENT '0为未考勤，1为考勤，2为缺勤，3为请假',
                                      `time` datetime NULL DEFAULT NULL,
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `attendance_result_course_id_fk`(`course_id` ASC) USING BTREE,
                                      INDEX `attendance_result_students_id_fk`(`student_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 195 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '考勤结果表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_attendance
-- ----------------------------
INSERT INTO `course_attendance` VALUES (97, 5, 1, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (98, 5, 2, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (99, 5, 3, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (100, 5, 4, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (101, 5, 5, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (102, 5, 6, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (103, 5, 7, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (104, 5, 8, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (105, 5, 9, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (106, 5, 10, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (107, 5, 11, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (108, 5, 12, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (109, 5, 13, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (110, 5, 14, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (111, 5, 15, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (112, 5, 16, 0, '2023-09-23 00:16:20');
INSERT INTO `course_attendance` VALUES (113, 6, 1, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (114, 6, 2, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (115, 6, 3, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (116, 6, 4, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (117, 6, 5, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (118, 6, 6, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (119, 6, 7, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (120, 6, 8, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (121, 6, 9, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (122, 6, 10, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (123, 6, 11, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (124, 6, 12, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (125, 6, 13, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (126, 6, 14, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (127, 6, 15, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (128, 6, 16, 0, '2023-09-23 00:16:28');
INSERT INTO `course_attendance` VALUES (129, 7, 1, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (130, 7, 2, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (131, 7, 3, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (132, 7, 4, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (133, 7, 5, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (134, 7, 6, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (135, 7, 7, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (136, 7, 8, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (137, 7, 9, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (138, 7, 10, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (139, 7, 11, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (140, 7, 12, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (141, 7, 13, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (142, 7, 14, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (143, 7, 15, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (144, 7, 16, 0, '2023-09-23 00:16:41');
INSERT INTO `course_attendance` VALUES (145, 8, 1, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (146, 8, 2, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (147, 8, 3, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (148, 8, 4, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (149, 8, 5, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (150, 8, 6, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (151, 8, 7, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (152, 8, 8, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (153, 8, 9, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (154, 8, 10, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (155, 8, 11, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (156, 8, 12, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (157, 8, 13, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (158, 8, 14, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (159, 8, 15, 0, '2023-09-23 00:16:47');
INSERT INTO `course_attendance` VALUES (160, 8, 16, 0, '2023-09-23 00:16:47');

-- ----------------------------
-- Table structure for course_detail
-- ----------------------------
DROP TABLE IF EXISTS `course_detail`;
CREATE TABLE `course_detail`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `course_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '课程名称',
                                  `semester` int NULL DEFAULT NULL COMMENT '0代表上学期，1代表下学期',
                                  `week` smallint NULL DEFAULT NULL,
                                  `weekday` smallint NULL DEFAULT NULL,
                                  `section_start` tinyint NULL DEFAULT NULL,
                                  `section_end` tinyint NULL DEFAULT NULL,
                                  `begin_time` datetime NOT NULL COMMENT '这节课的开始时间',
                                  `end_time` datetime NULL DEFAULT NULL,
                                  `course_teacher` bigint NOT NULL COMMENT '课程对应的教师',
                                  `school_open_time` datetime NULL DEFAULT NULL,
                                  `course_place` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                  `longitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                  `latitude` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `course_teacher__fk`(`course_teacher` ASC) USING BTREE,
                                  CONSTRAINT `course_teacher__fk` FOREIGN KEY (`course_teacher`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_detail
-- ----------------------------
INSERT INTO `course_detail` VALUES (1, '算法与数据结构', 202301, 7, 5, 7, 8, '2023-10-13 15:50:00', '2023-10-13 17:30:00', 11, '2023-08-28 00:00:00', '西一-206', '119.192459', '26.050283');
INSERT INTO `course_detail` VALUES (2, '算法与数据结构', 202301, 8, 5, 7, 8, '2023-10-20 15:50:00', '2023-10-20 17:30:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (3, '算法与数据结构', 202301, 9, 5, 7, 8, '2023-10-27 15:50:00', '2023-10-27 17:30:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (4, '算法与数据结构', 202301, 10, 5, 7, 8, '2023-11-03 15:50:00', '2023-11-03 17:30:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (5, '算法与数据结构', 202301, 11, 5, 7, 8, '2023-11-10 15:50:00', '2023-11-10 17:30:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (6, '算法与数据结构', 202301, 12, 5, 7, 8, '2023-11-17 15:50:00', '2023-11-17 17:30:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (7, '算法与数据结构', 202301, 13, 5, 7, 8, '2023-11-24 15:50:00', '2023-11-24 17:30:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (8, '算法与数据结构', 202301, 14, 5, 7, 8, '2023-12-01 15:50:00', '2023-12-01 17:30:00', 11, '2023-08-28 00:00:00', '西一-206', '119.192325', '26.050066');
INSERT INTO `course_detail` VALUES (9, '算法与数据结构', 202301, 7, 3, 3, 4, '2023-10-11 10:20:00', '2023-10-11 12:00:00', 11, '2023-08-28 00:00:00', '西一-206', '119.192335', '26.050013');
INSERT INTO `course_detail` VALUES (10, '算法与数据结构', 202301, 8, 3, 3, 4, '2023-10-18 10:20:00', '2023-10-18 12:00:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (11, '算法与数据结构', 202301, 9, 3, 3, 4, '2023-10-25 10:20:00', '2023-10-25 12:00:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (12, '算法与数据结构', 202301, 10, 3, 3, 4, '2023-11-01 10:20:00', '2023-11-01 12:00:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (13, '算法与数据结构', 202301, 11, 3, 3, 4, '2023-11-08 10:20:00', '2023-11-08 12:00:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (14, '算法与数据结构', 202301, 12, 3, 3, 4, '2023-11-15 10:20:00', '2023-11-15 12:00:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (15, '算法与数据结构', 202301, 13, 3, 3, 4, '2023-11-22 10:20:00', '2023-11-22 12:00:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (16, '算法与数据结构', 202301, 14, 3, 3, 4, '2023-11-29 10:20:00', '2023-11-29 12:00:00', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');
INSERT INTO `course_detail` VALUES (18, 'test', 202301, 5, 3, 3, 4, '2023-10-02 08:07:29', '2023-10-03 23:49:28', 11, '2023-08-28 00:00:00', '西一-206', '119.191903', '26.050397');

-- ----------------------------
-- Table structure for leaveapplication
-- ----------------------------
DROP TABLE IF EXISTS `leaveapplication`;
CREATE TABLE `leaveapplication`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '申诉ID，整数类型，作为主键',
                                     `student_id` bigint NOT NULL COMMENT '学生ID',
                                     `course_id` bigint NULL DEFAULT NULL,
                                     `leave_place` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                                     `appeal_begin_time` datetime NOT NULL,
                                     `appeal_end_time` datetime NOT NULL,
                                     `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '0为未通过，1为通过，2为不通过',
                                     `reason` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                                     `image` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请假图片',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `leaveapplication_course_detail_id_fk`(`course_id` ASC) USING BTREE,
                                     INDEX `leaveapplication_student_id_fk`(`student_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '请假表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of leaveapplication
-- ----------------------------
INSERT INTO `leaveapplication` VALUES (40, 17, 1, '宿舍', '2023-10-27 15:50:00', '2023-10-27 15:50:00', '2', '宿舍炸了', 'https://flying-pig-z.oss-cn-beijing.aliyuncs.com/461ee45d-aac8-4d13-9f11-cdd8ce1e16d4.jpeg');
INSERT INTO `leaveapplication` VALUES (41, 6, 15, '宿舍', '2023-10-02 08:21:57', '2023-10-09 23:50:28', '0', '原因', 'https://flying-pig-z.oss-cn-beijing.aliyuncs.com/2ff6b7c1-9c4e-42b4-9c3f-243c94f3ca16.jpg');

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '学生ID',
                            `no` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号',
                            `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
                            `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '年级',
                            `clas_s` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级',
                            `major` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专业',
                            `userid` bigint NULL DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `student_user_id_fk`(`userid` ASC) USING BTREE,
                            CONSTRAINT `student_user_id_fk` FOREIGN KEY (`userid`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1833774804830298115 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (5, '102201510', '阮瑞鹏', '2022', '5', '计算机与大数据学院/软件学院', 16);
INSERT INTO `student` VALUES (6, '102201607', '刘程城', '2022', '6', '计算机与大数据学院/软件学院', 17);
INSERT INTO `student` VALUES (7, '102201420', '林传昊', '2022', '4', '计算机与大数据学院/软件学院', 18);
INSERT INTO `student` VALUES (8, '102201601', '王铭垚', '2022', '6', '计算机与大数据学院/软件学院', 19);
INSERT INTO `student` VALUES (17, '102201604', '朱嘉翔', '2022', '6', '计算机与大数据学院/软件学院', 26);
INSERT INTO `student` VALUES (18, '832201115', '陈雯静', '2022', '1', '计算机与大数据学院/软件学院', 27);
INSERT INTO `student` VALUES (1833774794575224834, '2', '的撒的', '2024', '6', '计算机与大数据学院/软件学院', 1833774793631506434);
INSERT INTO `student` VALUES (1833774795665743874, '3', '啊发生的', '2024', '6', '计算机与大数据学院/软件学院', 1833774794575224835);
INSERT INTO `student` VALUES (1833774796626239490, '4', '三大暗访', '2024', '6', '计算机与大数据学院/软件学院', 1833774795665743875);
INSERT INTO `student` VALUES (1833774797632872449, '5', '阿第三方', '2024', '6', '计算机与大数据学院/软件学院', 1833774796626239491);
INSERT INTO `student` VALUES (1833774798660476930, '6', '啊防守打法', '2024', '6', '计算机与大数据学院/软件学院', 1833774797632872450);
INSERT INTO `student` VALUES (1833774799620972545, '7', '大事发生', '2024', '6', '计算机与大数据学院/软件学院', 1833774798660476931);
INSERT INTO `student` VALUES (1833774800526942210, '8', '的地方打广告', '2024', '6', '计算机与大数据学院/软件学院', 1833774799620972546);
INSERT INTO `student` VALUES (1833774801403551746, '9', '阿范德萨', '2024', '6', '计算机与大数据学院/软件学院', 1833774800526942211);
INSERT INTO `student` VALUES (1833774802515042306, '10', '阿斯顿发顺丰到付', '2024', '6', '计算机与大数据学院/软件学院', 1833774801403551747);
INSERT INTO `student` VALUES (1833774803798499329, '11', '多撒范德萨发', '2024', '6', '计算机与大数据学院/软件学院', 1833774802515042307);
INSERT INTO `student` VALUES (1833774804830298114, '12', '阿斯顿发斯蒂芬', '2024', '6', '计算机与大数据学院/软件学院', 1833774803798499330);

-- ----------------------------
-- Table structure for supervision_task
-- ----------------------------
DROP TABLE IF EXISTS `supervision_task`;
CREATE TABLE `supervision_task`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT,
                                     `course_id` bigint NOT NULL,
                                     `userid` bigint NULL DEFAULT NULL,
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `supervision_task_user_id_fk`(`userid` ASC) USING BTREE,
                                     INDEX `supervision_task_course_detail_id_fk`(`course_id` ASC) USING BTREE,
                                     CONSTRAINT `supervision_task_course_detail_id_fk` FOREIGN KEY (`course_id`) REFERENCES `course_detail` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                     CONSTRAINT `supervision_task_user_id_fk` FOREIGN KEY (`userid`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '督导_课程关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of supervision_task
-- ----------------------------
INSERT INTO `supervision_task` VALUES (55, 1, 18);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `menu_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'NULL' COMMENT '菜单名',
                             `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '权限标识',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '学生相关操作', 'sys:student:operation');
INSERT INTO `sys_menu` VALUES (2, '督导相关操作', 'sys:supervision:operation');
INSERT INTO `sys_menu` VALUES (3, '教师相关操作', 'sys:teacher:operation');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '角色权限字符串',
                             PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'Student', 'student');
INSERT INTO `sys_role` VALUES (2, 'Supervision', 'supervision');
INSERT INTO `sys_role` VALUES (3, 'Teacher', 'teacher');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
                                  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
                                  `menu_id` bigint NOT NULL DEFAULT 0 COMMENT '菜单id',
                                  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE,
                                  INDEX `role_menu_menu_id_fk`(`menu_id` ASC) USING BTREE,
                                  CONSTRAINT `role_menu_menu_id_fk` FOREIGN KEY (`menu_id`) REFERENCES `sys_menu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                  CONSTRAINT `role_menu_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1);
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (3, 3);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             `password` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
                             `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             `gender` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             `college` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
                             `user_type` int NULL DEFAULT NULL,
                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE INDEX `id`(`id` ASC) USING BTREE,
                             UNIQUE INDEX `no`(`no` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1833774803798499331 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (11, 'T0', '$2a$10$OCZxTFYUa3o0DUlLds66zOYJnzCTv/CTTV95lvnQ6U1vMVt2JqTvy', '王一蕾', '女', '计算机与大数据学院', 3);
INSERT INTO `sys_user` VALUES (16, '102201510', '$2a$10$nBAQdKdjD3crJblKXNMrW.VaN8LRy37HOQCDOl7It1KT7UbadhE6G', '阮瑞鹏', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (17, '102201607', '$2a$10$nBAQdKdjD3crJblKXNMrW.VaN8LRy37HOQCDOl7It1KT7UbadhE6G', '刘程城', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (18, '102201420', '$2a$10$CmbOQljnq56e0HXe.J7LbuZnunTSLaA6yWG7KhEOk./IvC0QtqAne', '林传昊', '男', '计算机与大数据学院', 2);
INSERT INTO `sys_user` VALUES (19, '102201601', '$2a$10$nBAQdKdjD3crJblKXNMrW.VaN8LRy37HOQCDOl7It1KT7UbadhE6G', '王铭垚', '女', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (26, '102201604', '$2a$10$nBAQdKdjD3crJblKXNMrW.VaN8LRy37HOQCDOl7It1KT7UbadhE6G', '朱嘉翔', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (27, '832201115', '$2a$10$XUUlRtaCgjc6y7KyLQ8Vg.v/BzkUw5SF.Tojru37qSpKJKz30bf5e', '陈雯静', '女', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (1833774793631506434, '2', '$2a$10$ioOq8wtnz1fWyc1yaCSfGOGhbC53XBK.suIsWY1BuWTL.rCOlCIS.', '的撒的', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (1833774794575224835, '3', '$2a$10$qDcYr7TE27PbrUk7CKcF7uvdiPGm87T7G9ZWlk.ILXt1xrR0L4qdm', '啊发生的', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (1833774795665743875, '4', '$2a$10$vwiB6jVcc7gQtxbhrQGzIegx8LDamHifLFhWMCM72FdIia8DrHjk.', '三大暗访', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (1833774796626239491, '5', '$2a$10$UnL9GjB.31E3j4v11ByQxu1olCiKtpmusppxeBIAfJ62pPK3yt/66', '阿第三方', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (1833774797632872450, '6', '$2a$10$CeAotDJaovPqUolEtmW1tuZ439j/j/8CUBsIiZITOAeAByKkE0Aae', '啊防守打法', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (1833774798660476931, '7', '$2a$10$xhAFbfMXHtw4rl0RNkuhI.WYQmYdi9XhiribypR3mEY4OIDKrH2Ae', '大事发生', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (1833774799620972546, '8', '$2a$10$HfDLxgtupq8sKdDX/lVaH.hWvhQqWfHAqjsV5nTu/2EiM8r42UP7K', '的地方打广告', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (1833774800526942211, '9', '$2a$10$4k3JK/rLCUVJPrKTPh2sC.NcBqpo0eVRwiUSEQ74e67teEknFPkrm', '阿范德萨', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (1833774801403551747, '10', '$2a$10$PpW2HtiRRLGz83S.cJxM.uSR6qFIoIEJnLnfM/vRslFDpUTTf5dB6', '阿斯顿发顺丰到付', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (1833774802515042307, '11', '$2a$10$6Jg52jeCots56iYBzoNMSOub2MuuvZqhbKNQAwUhtdK9za.Xkwdky', '多撒范德萨发', '男', '计算机与大数据学院', 1);
INSERT INTO `sys_user` VALUES (1833774803798499330, '12', '$2a$10$ZZgwkdy0ibiRL3JA3C2QH.I7ReZ12TDkavaEMat3XzV3vcmsFr5yW', '阿斯顿发斯蒂芬', '男', '计算机与大数据学院', 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
                                  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                  `role_id` bigint NOT NULL DEFAULT 0 COMMENT '角色id',
                                  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
                                  INDEX `sys_user_role_sys_role_id_fk`(`role_id` ASC) USING BTREE,
                                  CONSTRAINT `sys_user_role_sys_role_id_fk` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                  CONSTRAINT `sys_user_role_sys_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1833774803798499331 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (16, 1);
INSERT INTO `sys_user_role` VALUES (17, 1);
INSERT INTO `sys_user_role` VALUES (18, 1);
INSERT INTO `sys_user_role` VALUES (19, 1);
INSERT INTO `sys_user_role` VALUES (26, 1);
INSERT INTO `sys_user_role` VALUES (27, 1);
INSERT INTO `sys_user_role` VALUES (1833774793631506434, 1);
INSERT INTO `sys_user_role` VALUES (1833774794575224835, 1);
INSERT INTO `sys_user_role` VALUES (1833774795665743875, 1);
INSERT INTO `sys_user_role` VALUES (1833774796626239491, 1);
INSERT INTO `sys_user_role` VALUES (1833774797632872450, 1);
INSERT INTO `sys_user_role` VALUES (1833774798660476931, 1);
INSERT INTO `sys_user_role` VALUES (1833774799620972546, 1);
INSERT INTO `sys_user_role` VALUES (1833774800526942211, 1);
INSERT INTO `sys_user_role` VALUES (1833774801403551747, 1);
INSERT INTO `sys_user_role` VALUES (1833774802515042307, 1);
INSERT INTO `sys_user_role` VALUES (1833774803798499330, 1);
INSERT INTO `sys_user_role` VALUES (11, 3);

SET FOREIGN_KEY_CHECKS = 1;
