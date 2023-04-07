package ru.trueip.tnectupgrader.models.responses;


public class UpdatesModel {
    /**
     * name : ”tnect”
     * bundleid : ‘tnect.ru.tnect’
     * version : ‘1.0.1’
     * update_url : ’tnect.update-1.0.1.apk’
     */

    private String name;
    private String bundleid;
    private String version;
    private String update_url;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBundleid() {
        return bundleid;
    }

    public void setBundleid(String bundleid) {
        this.bundleid = bundleid;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }
}
