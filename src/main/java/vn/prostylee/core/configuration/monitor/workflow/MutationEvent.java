package vn.prostylee.core.configuration.monitor.workflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This define a mutation events.
 */
public interface MutationEvent {

    Logger log = LoggerFactory.getLogger(MutationEvent.class);

    /**
     * This event is triggered before creating.
     *
     * @param request The request data
     * @param <T>     The request data type
     */
    default <T> void onPreCreate(T request) {
        log.debug("onPreCreate with request={}", request);
    }

    /**
     * This event is triggered after created.
     *
     * @param request  The request data
     * @param response The response data
     * @param <T>      The request data type
     * @param <R>      The response data type
     */
    default <T, R> void onPostCreate(T request, R response) {
        log.debug("onPostCreate with request={}, response={}", request, response);
    }

    /**
     * This event is triggered before updating.
     *
     * @param id      The primary key
     * @param request The request data
     * @param <ID>    The primary key type
     * @param <T>     The request data type
     */
    default <ID, T> void onPreUpdate(ID id, T request) {
        log.debug("onPreUpdate with id={}, request={}", id, request);
    }

    /**
     * This event is triggered after updated.
     *
     * @param id      The primary key
     * @param request  The request data
     * @param response The response data
     * @param <ID>    The primary key type
     * @param <T>      The request data type
     * @param <R>      The response data type
     */
    default <ID, T, R> void onPostUpdate(ID id, T request, R response) {
        log.debug("onPostUpdate with id={}, request={}, response={}", id, request, response);
    }

    /**
     * This event is triggered before deleting.
     *
     * @param id   The primary key
     * @param <ID> The primary key type
     */
    default <ID> void onPreDelete(ID id) {
        log.debug("onPreDelete with id={}", id);
    }

    /**
     * This event is triggered after deleted.
     *
     * @param id   The primary key
     * @param <ID> The primary key type
     * @param <R>      The response data type
     */
    default <ID, R> void onPostDelete(ID id, R response) {
        log.debug("onPostDelete with id={}, response={}", id, response);
    }
}
