package com.boyoi.core.generator.entity;

import java.io.Serializable;

/**
 * 附加属性对象
 *
 * @author ZhouJL
 * @date 2019/2/14 10:08
 */
public class Configuration implements Serializable {
    private String author;
    private String packageName;
    private Path path;
    private Db db;
    private boolean isCover;
    private String authorZh;
    private String fileUrl;

    public String getFileUrl() {
        return fileUrl;
    }


    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getAuthorZh() {
        return authorZh;
    }

    public void setAuthorZh(String authorZh) {
        this.authorZh = authorZh;
    }

    public boolean getIsCover() {
        return isCover;
    }

    public void setIsCover(boolean isCover) {
        this.isCover = isCover;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPackageName() {
        return packageName == null ? "" : packageName + ".";
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public Db getDb() {
        return db;
    }

    public void setDb(Db db) {
        this.db = db;
    }

    public static class Db {
        private String url;
        private String username;
        private String password;

        public Db() {
        }

        public Db(String url, String username, String password) {
            this.url = url;
            this.username = username;
            this.password = password;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class Path {
        private String controller;
        private String service;
        private String interf;
        private String dao;
        private String entity;
        private String mapper;
        private String group;
        private String message;

        public Path() {
        }

        public Path(String controller, String service, String interf, String dao, String entity, String mapper, String group, String message) {
            this.controller = controller;
            this.service = service;
            this.interf = interf;
            this.dao = dao;
            this.entity = entity;
            this.mapper = mapper;
            this.group = group;
            this.message = message;
        }

        public String getController() {
            return controller == null ? "" : controller;
        }

        public void setController(String controller) {
            this.controller = controller;
        }

        public String getService() {
            return service == null ? "" : service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getInterf() {
            return interf;
        }

        public void setInterf(String interf) {
            this.interf = interf;
        }

        public String getDao() {
            return dao == null ? "" : dao;
        }

        public void setDao(String dao) {
            this.dao = dao;
        }

        public String getEntity() {
            return entity == null ? "" : entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public String getMapper() {
            return mapper == null ? "" : mapper;
        }

        public void setMapper(String mapper) {
            this.mapper = mapper;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
