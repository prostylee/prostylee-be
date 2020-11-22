package vn.prostylee.core.utils;


import vn.prostylee.core.exception.FileConvertingException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLConnection;

public class FileUtil {

    public static InputStreamSource getInputStreamSource(String filePath) {
        return new InputStreamSource() {
            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(filePath);
            }
        };
    }

    public static InputStreamSource getInputStreamSource(byte[] bytes) {
        return new ByteArrayResource(bytes);
    }

    public static InputStreamSource getInputStreamSource(File file) {
        return new FileSystemResource(file);
    }

    public static File convertMultipartToFile(MultipartFile multipart) {
        File convFile = new File(multipart.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile);) {
            fos.write(multipart.getBytes());
        } catch (IOException e) {
            throw new FileConvertingException("Can't convert multipart to file", e);
        }
        return convFile;
    }

    public static String getMimeType(byte[] data) throws IOException {
        InputStream is = new BufferedInputStream(new ByteArrayInputStream(data));
        return URLConnection.guessContentTypeFromStream(is);
    }

}
