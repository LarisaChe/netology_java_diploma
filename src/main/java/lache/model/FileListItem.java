package lache.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileListItem {
    @JsonProperty("filename")
    private String filename;
    @JsonProperty("size")
    private Long size;

    public FileListItem(String filename, Long size) {
        this.filename = filename;
        this.size = size;
    }

    @Override
    public String toString() {
        return "File{" +
                "name='" + filename + '\'' +
                ", size=" + size +
                '}';
    }
}
