package com.smusic.app.service.cloudstorage.yandex.pojo;

import java.util.Date;


public class Resource {
    private static final String DIR_TYPE = "dir";

    private ResourceList _embedded;
    private String name;
    private Date created;
    private String resource_id;
    private Date modified;
    private String path;
    private String type;
    private long revision;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getRevision() {
        return revision;
    }

    public void setRevision(long revision) {
        this.revision = revision;
    }

    public ResourceList get_embedded() {
        return _embedded;
    }

    public void set_embedded(ResourceList _embedded) {
        this._embedded = _embedded;
    }

    public boolean isDirectory() {
        return DIR_TYPE.equals(type);
    }

    @Override
    public String toString() {
        return "Resource{" +
                "_embedded=" + _embedded +
                ", name='" + name + '\'' +
                ", created=" + created +
                ", resource_id='" + resource_id + '\'' +
                ", modified=" + modified +
                ", path='" + path + '\'' +
                ", type='" + type + '\'' +
                ", revision=" + revision +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (revision != resource.revision) return false;
        if (name != null ? !name.equals(resource.name) : resource.name != null) return false;
        if (created != null ? !created.equals(resource.created) : resource.created != null) return false;
        if (resource_id != null ? !resource_id.equals(resource.resource_id) : resource.resource_id != null)
            return false;
        if (modified != null ? !modified.equals(resource.modified) : resource.modified != null) return false;
        if (path != null ? !path.equals(resource.path) : resource.path != null) return false;
        return type != null ? type.equals(resource.type) : resource.type == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (created != null ? created.hashCode() : 0);
        result = 31 * result + (resource_id != null ? resource_id.hashCode() : 0);
        result = 31 * result + (modified != null ? modified.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (int) (revision ^ (revision >>> 32));
        return result;
    }
}
