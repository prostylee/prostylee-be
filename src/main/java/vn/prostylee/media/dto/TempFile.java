package vn.prostylee.media.dto;

import lombok.Getter;
import lombok.ToString;

import java.io.File;

@Getter
@ToString
public class TempFile extends File {

    private String businessName;

    private String extension;

    /**
     * @param parent parent folder
     * @param systemName system unique name
     * @param businessName display name
     */
    public TempFile(File parent, String systemName, String businessName, String extension) {
        super(parent, systemName);
        this.businessName = businessName;
        this.extension = extension;
    }
}