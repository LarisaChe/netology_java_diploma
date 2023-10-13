package lache.model;

import java.util.Date;
import jakarta.persistence.*;

@Entity(name = "files")
public class UserFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dt")
    private Date dt;
    @Column(name = "login", length = 32, nullable = false)
    private String login;
    @Column(name = "file_name", length = 1000, nullable = false)
    private String fileName;
    @Column(name = "size")
    private Long fileSize;
    @Column(name = "file_status")
    private Status fileStatus;

    public UserFile() {
    }

    public Long getId() {
        return id;
    }

    public Date getDt() {
        return dt;
    }

    public String getLogin() {
        return login;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public Status getFileStatus() {
        return fileStatus;
    }
}
