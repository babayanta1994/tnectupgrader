package ru.trueip.tnectupgrader.models;

public class Apps {
    private String name;
    private String branch;
    private String bundleid;
    private String curVersion;
    private String lastVersion;
    private String description;
    private String updateUrl;

    public Apps(String name, String branch, String bundleid, String curVersion, String lastVersion, String updateUrl,String description) {
        this.name = name;
        this.branch = branch;
        this.bundleid = bundleid;
        this.curVersion = curVersion;
        this.lastVersion = lastVersion;
        this.updateUrl = updateUrl;
        this.description = description;
    }

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

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBundleid() {
        return bundleid;
    }

    public void setBundleid(String bundleid) {
        this.bundleid = bundleid;
    }

    public String getCurVersion() {
        return curVersion;
    }

    public void setCurVersion(String curVersion) {
        this.curVersion = curVersion;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }
}
