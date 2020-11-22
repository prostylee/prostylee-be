package vn.prostylee.media.provider.async;

import java.util.List;
import java.util.concurrent.Future;

/**
 * The base class for helping asynchronous tasks
 */
public class BaseAsyncProvider {

	/**
	 * Check all futures are done
	 */
	public <T> boolean isAllFutureDone(List<Future<T>> futures) {
		for (Future<?> future : futures) {
			if (!future.isDone()) {
				return false;
			}
		}
		return true;
	}

}
