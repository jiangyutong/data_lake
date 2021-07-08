/*
 Navicat Premium Data Transfer

 Source Server         : 122.112.165.39
 Source Server Type    : PostgreSQL
 Source Server Version : 130000
 Source Host           : 122.112.165.39:5432
 Source Catalog        : postgres
 Source Schema         : db

 Target Server Type    : PostgreSQL
 Target Server Version : 130000
 File Encoding         : 65001

 Date: 19/10/2020 16:46:21
*/


-- ----------------------------
-- Table structure for test_db
-- ----------------------------
DROP TABLE IF EXISTS "db"."test_db";
CREATE TABLE "db"."test_db" (
  "guid" varchar(32) COLLATE "pg_catalog"."default" NOT NULL,
  "k" varchar(255) COLLATE "pg_catalog"."default",
  "v" varchar(255) COLLATE "pg_catalog"."default",
  "name" varchar(255) COLLATE "pg_catalog"."default",
  "type" char(1) COLLATE "pg_catalog"."default",
  "opt_time" timestamp(6),
  "opt_person" varchar(255) COLLATE "pg_catalog"."default",
  "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "db"."test_db"."guid" IS 'guid';
COMMENT ON COLUMN "db"."test_db"."k" IS 'key';
COMMENT ON COLUMN "db"."test_db"."v" IS 'value';
COMMENT ON COLUMN "db"."test_db"."name" IS '名称';
COMMENT ON COLUMN "db"."test_db"."type" IS '类型 A字符 B整形 C浮点 D时间 E百分比';
COMMENT ON TABLE "db"."test_db" IS '数据字典';

-- ----------------------------
-- Records of test_db
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table test_db
-- ----------------------------
ALTER TABLE "db"."test_db" ADD CONSTRAINT "test_db_pkey" PRIMARY KEY ("guid");
