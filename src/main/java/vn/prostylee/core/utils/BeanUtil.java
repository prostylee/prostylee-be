package vn.prostylee.core.utils;

import vn.prostylee.core.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public final class BeanUtil {

	private BeanUtil() {
		super();
	}

	/**
	 * Copy the property values of the given source object into a new instance of the target type
	 * @param source The source object
	 * @param type The target class
	 * @param <T> The type of source
	 * @param <K> The type of target
	 * @return The target object
	 * @throws ApplicationException if can not copy the properties
	 */
	public static <T, K> T copyProperties(@NonNull K source, @NonNull Class<T> type) {
		try {
			Class<?> clazz = Class.forName(type.getName());
			T desc = createNewInstance(clazz);
			BeanUtils.copyProperties(source, desc);
			return desc;
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			log.error("Error copy properties: ", e);
			throw new ApplicationException("Can not copy properties", e);
		}
	}

	/**
	 * Merge all properties from the given source object to the given target object. This method will ignore all null values
	 * @param source The source object
	 * @param dest The target object
	 * @param <T> The source type
	 * @param <K> The target type
	 */
	public static <T, K> void mergeProperties(@NonNull K source, @NonNull T dest) {
		mergeProperties(source, dest, true);
	}

	/**
	 * Merge all properties from the given source object to the given target object
	 * @param source The source object
	 * @param dest The target object
	 * @param isIgnoreNull True will ignore all
	 * @param <T> The source type
	 * @param <K> The target type
	 */
	public static <T, K> void mergeProperties(@NonNull K source, @NonNull T dest, boolean isIgnoreNull) {
		if (isIgnoreNull) {
			BeanUtils.copyProperties(source, dest, getNullPropertyNames(source));
		} else {
			BeanUtils.copyProperties(source, dest);
		}
	}

	/**
	 * Copy the property values of the given list source object into a new instance of the list target
	 * @param srcList The list of source object
	 * @param type The target class
	 * @param <T> The type of source
	 * @param <K> The type of target
	 * @return The list of target object
	 */
	public static <T, K> List<T> listCopyProperties(Collection<K> srcList, @NonNull Class<T> type) {
		List<T> descList = new ArrayList<>();
		try {
			if (!CollectionUtils.isEmpty(srcList)) {
				Class<?> clazz = Class.forName(type.getName());
				for (K source : srcList) {
					T desc = createNewInstance(clazz);
					BeanUtils.copyProperties(source, desc);
					descList.add(desc);
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			log.error("Error list copy properties: ", e);
		}
		return descList;
	}

	private static <T> T createNewInstance(Class<?> clazz) throws IllegalAccessException, InstantiationException {
		return (T) clazz.newInstance();
	}

	private static String[] getNullPropertyNames(Object source) {
		final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
		return Stream.of(wrappedSource.getPropertyDescriptors())
				.map(FeatureDescriptor::getName)
				.filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
				.toArray(String[]::new);
	}
}
