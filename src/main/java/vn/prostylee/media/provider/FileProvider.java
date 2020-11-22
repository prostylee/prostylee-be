package vn.prostylee.media.provider;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.dto.TempFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

@Component
@Slf4j
public class FileProvider {

    private static final String UNIQUE_STRING_PREFIX = "Pic_tmp_";
    private final FolderProvider folderProvider;

    @Autowired
    public FileProvider(@Autowired FolderProvider folderHelper) {
        this.folderProvider = folderHelper;
    }

    /**
     * Convert a MultipartFile to TempFile
     * @param multipartFile The {@link MultipartFile}
     * @return The {@link TempFile}
     * @throws IOException
     */
    public TempFile convertToFile(MultipartFile multipartFile) throws IOException {
        String systemName = generateUniqueString();
        String fileName = multipartFile.getOriginalFilename();
        TempFile tmpFile = new TempFile(folderProvider.getTemporaryFolder().toFile(), systemName, fileName, FilenameUtils.getExtension(fileName));

        try (FileOutputStream fos = new FileOutputStream(tmpFile);) {
            fos.write(multipartFile.getBytes());
        } catch (Exception e) {
            log.error("TempFile generation has failed.", e);
        }
        return tmpFile;
    }

    /**
     * Create Unique file name
     * @param extension The extension of file
     * @return random string + extension
     */
    public String createUniqueFileName(String extension) {
        return generateUniqueString() + FilenameUtils.EXTENSION_SEPARATOR + extension;
    }

    /**
     * Create unique string
     * @return random string
     */
    public String generateUniqueString () {
        String pattern = "{0}_{1}_{2}";
        return MessageFormat.format(pattern, UNIQUE_STRING_PREFIX,
                UUID.randomUUID(),
                System.currentTimeMillis() + "");
    }
}
