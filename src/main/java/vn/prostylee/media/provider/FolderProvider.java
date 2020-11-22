package vn.prostylee.media.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import vn.prostylee.core.configuration.properties.TempFolderProperties;
import vn.prostylee.core.exception.MissingConfigurationException;
import vn.prostylee.media.exception.ConfigurationException;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The helper class to get the path to folders in application
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class FolderProvider {

	private final TempFolderProperties tempFolderProperties;

	@PostConstruct
	public void init() {
		if (StringUtils.isEmpty(tempFolderProperties.getLocation())) {
			throw new MissingConfigurationException("Missing the configuration of folder.temp");
		}

		File tempFolder = Paths.get(tempFolderProperties.getLocation()).normalize().toFile();
		log.info("app.folder.temp.location = " + tempFolder.getAbsolutePath());
		if (!tempFolder.exists() || !tempFolder.isDirectory()) {
			boolean isCreated = tempFolder.mkdir();
			log.info("Folder {} was created = {}", tempFolderProperties.getLocation(), isCreated);
		}
		if (!tempFolder.isDirectory()) {
			throw new ConfigurationException("Template folder is not existed. Please check temporary folder at " + tempFolderProperties.getLocation());
		}
	}

	/**
	 * Get temporary folder
	 * 
	 * @return Path
	 */
	public Path getTemporaryFolder() {
		return this.from(tempFolderProperties.getLocation());
	}

	/**
	 * Get path of file or folder
	 * 
	 * @param filePath The given file path
	 * @return Path
	 */
	public Path from(String filePath) {
		try {
			return Paths.get(filePath).toAbsolutePath().normalize();
		} catch (Exception ex) {
			throw new ConfigurationException("Could not create the directory: " + filePath, ex);
		}
	}

	/**
	 * Get path of file or folder
	 *
	 * @param filePath The given file path
	 * @return Path
	 */
	public Path from(String filePath, boolean createNewIfNotExists) {
		try {
			Path path = Paths.get(filePath).toAbsolutePath().normalize();
			if (createNewIfNotExists) {
				Files.createDirectories(path);
			}
			return path;
		} catch (Exception ex) {
			throw new ConfigurationException("Could not create the directory: " + filePath, ex);
		}
	}
}
