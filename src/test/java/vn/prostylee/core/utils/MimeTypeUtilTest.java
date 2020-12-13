package vn.prostylee.core.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MimeTypeUtilTest {

    @Test
    public void getMimeTypeTest() {
        Assertions.assertEquals(MimeTypeUtil.DEFAULT_MIME_TYPE, MimeTypeUtil.getMimeType(null));
        Assertions.assertEquals(MimeTypeUtil.DEFAULT_MIME_TYPE, MimeTypeUtil.getMimeType(" "));
        Assertions.assertEquals(MimeTypeUtil.DEFAULT_MIME_TYPE, MimeTypeUtil.getMimeType("."));
        Assertions.assertEquals("application/zip", MimeTypeUtil.getMimeType("filename.zip"));
        Assertions.assertEquals("application/zip", MimeTypeUtil.getMimeType(".zip"));
        Assertions.assertEquals("application/zip", MimeTypeUtil.getMimeType("zip"));
        Assertions.assertEquals(MimeTypeUtil.DEFAULT_MIME_TYPE, MimeTypeUtil.getMimeType("unkonwn.filename"));
    }
}
