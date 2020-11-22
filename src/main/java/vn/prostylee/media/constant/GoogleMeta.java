package vn.prostylee.media.constant;

import com.google.api.services.drive.model.File;

public final class GoogleMeta {

    /**
     * The Kind of Google Drive file
     */
    public static final String GOOGLE_KIND_OF_FOLDER = "application/vnd.google-apps.folder";

    /**
     * The Kind of Google Drive file
     */
    public static final String GOOGLE_KIND_OF_FILE = "drive#file";

    /**
     * The fields of {@link File} will get from Google Drive
     *
     * @see <a href="https://developers.google.com/drive/api/v3/reference/files">https://developers.google.com/drive/api/v3/reference/files</a>
     */
    public static final String GOOGLE_FIELDS_OF_FILE = "id, name, kind, mimeType, thumbnailLink, webContentLink, webViewLink, parents";

    private GoogleMeta() {
        super();
    }
}
