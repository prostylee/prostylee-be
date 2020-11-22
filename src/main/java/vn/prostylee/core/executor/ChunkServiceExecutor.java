package vn.prostylee.core.executor;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class ChunkServiceExecutor {

    private static final int CHUNK_SIZE = 50;

    private ChunkServiceExecutor() {}

    public static <T> int execute(List<T> collections, Function<Collection<T>, Integer> func) {
        return execute(collections, func, CHUNK_SIZE);
    }

    public static <T> int execute(List<T> collections, Function<Collection<T>, Integer> func, int chunkSize) {
        int totalSize = collections.size();
        int lastIndex = 0;
        int numberOfExecutedElements = 0;
        log.trace("ChunkServiceExecutor - Collection size " + totalSize);

        while (lastIndex < totalSize) {
            log.trace("ChunkServiceExecutor - lastIndex " + lastIndex);
            int toIndex = Math.min(chunkSize, totalSize);
            Collection<T> subCollections = collections.subList(lastIndex, toIndex);
            numberOfExecutedElements += func.apply(subCollections);
            lastIndex += chunkSize;
        }

        log.trace("ChunkServiceExecutor - numberOfExecutedElements " + numberOfExecutedElements);
        return numberOfExecutedElements;
    }
}
