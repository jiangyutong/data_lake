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
-- Table structure for dl_link
-- ----------------------------
DROP TABLE IF EXISTS "public"."dl_link";
CREATE TABLE "public"."dl_link" (
"guid" varchar(32) COLLATE "default" NOT NULL,
"url" varchar(255) COLLATE "default" NOT NULL,
"username" varchar(255) COLLATE "default" NOT NULL,
"password" varchar(100) COLLATE "default" NOT NULL,
"type" varchar(1) COLLATE "default" NOT NULL,
"drive" varchar(255) COLLATE "default" NOT NULL,
"opt_person" varchar(32) COLLATE "default" NOT NULL,
"opt_time" timestamp(0) COLLATE "default" NOT NULL,
"create_time" timestamp(0) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;
COMMENT ON TABLE "public"."dl_link" IS '文件';
COMMENT ON COLUMN "public"."dl_link"."guid" IS 'guid';
COMMENT ON COLUMN "public"."dl_link"."url" IS '文件名称';
COMMENT ON COLUMN "public"."dl_link"."username" IS '文件大小';
COMMENT ON COLUMN "public"."dl_link"."password" IS '存储位置';
COMMENT ON COLUMN "public"."dl_link"."type" IS '类型';
COMMENT ON COLUMN "public"."dl_link"."drive" IS 'cvs文件的属性';


-- ----------------------------
-- Alter Sequences Owned By 
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table dl_link
-- ----------------------------
ALTER TABLE "public"."dl_link" ADD PRIMARY KEY ("guid");
