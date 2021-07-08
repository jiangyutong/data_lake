/*
Navicat PGSQL Data Transfer

Source Server         : A47.111.5.166
Source Server Version : 90619
Source Host           : 47.111.5.166:5432
Source Database       : postgres
Source Schema         : public

Target Server Type    : PGSQL
Target Server Version : 90619
File Encoding         : 65001

Date: 2020-09-16 09:40:00
*/


-- ----------------------------
-- Table structure for dl_json
-- ----------------------------
DROP TABLE IF EXISTS "public"."dl_json";
CREATE TABLE "public"."dl_json" (
"guid" varchar(32) COLLATE "default" NOT NULL,
"name" varchar(100) COLLATE "default" NOT NULL,
"size" int(5) COLLATE "default" NOT NULL,
"address" varchar(100) COLLATE "default" NOT NULL,
"type" varchar(1) COLLATE "default" NOT NULL,
"attribute" varchar(2048) COLLATE "default" NOT NULL,
"length" int(5) NOT NULL,
"keywords" varchar(255) NOT NULL,
"opt_person" varchar(32) COLLATE "default",
"opt_time" timestamp(0) COLLATE "default",
"create_time" timestamp(0) COLLATE "default",
)
WITH (OIDS=FALSE)

;
COMMENT ON TABLE "public"."dl_json" IS '文件';
COMMENT ON COLUMN "public"."dl_json"."guid" IS 'guid';
COMMENT ON COLUMN "public"."dl_json"."name" IS '文件名称';
COMMENT ON COLUMN "public"."dl_json"."size" IS '文件大小';
COMMENT ON COLUMN "public"."dl_json"."address" IS '存储位置';
COMMENT ON COLUMN "public"."dl_json"."type" IS '类型';
COMMENT ON COLUMN "public"."dl_json"."attribute" IS 'cvs文件的属性';
COMMENT ON COLUMN "public"."dl_json"."length" IS 'cvs多少数据';
COMMENT ON COLUMN "public"."dl_json"."keywords" IS '关键字';

-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table dl_json
-- ----------------------------
ALTER TABLE "public"."dl_json" ADD PRIMARY KEY ("guid");
