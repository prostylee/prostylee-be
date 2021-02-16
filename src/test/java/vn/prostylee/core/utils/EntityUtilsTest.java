package vn.prostylee.core.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EntityUtilsTest {

    @Test
    void merge_NullTargets_ReturnSources() {
        Set<ItemEntityTest> sources = createSources();
        assertEquals(sources, EntityUtils.merge(sources, null, "id", ItemEntityTest.class));
    }

    @Test
    void merge_EmptyTargets_ReturnSources() {
        Set<ItemEntityTest> sources = createSources();
        assertTrue(EntityUtils.merge(sources, new HashSet<>(), "id", ItemEntityTest.class).isEmpty());
    }

    @Test
    void merge_NotEmptyTargets_ReturnSources() {
        Set<ItemEntityTest> sources = createSources();

        ItemDtoTest item2 = ItemDtoTest.builder().id(2L).name("Item 2 - updated").build();
        ItemDtoTest itemNew1 = ItemDtoTest.builder().name("Item 4").build();
        ItemDtoTest itemNew2 = ItemDtoTest.builder().name("Item 5").build();
        Set<ItemDtoTest> targets = new HashSet<>(Arrays.asList(item2, itemNew1, itemNew2));

        Set<ItemEntityTest> results = EntityUtils.merge(sources, targets, "id", ItemEntityTest.class);

        assertEquals(3, results.size());

        Optional<ItemEntityTest> item2AfterUpdated = results.stream().filter(item -> item.getId() != null && 2L == item.getId()).findFirst();
        assertTrue(item2AfterUpdated.isPresent());
        assertEquals("Item 2 - updated", item2AfterUpdated.get().getName());
        assertEquals("Description 2", item2AfterUpdated.get().getDescription());

        assertTrue(results.stream().anyMatch(item -> "Item 4".equals(item.getName())));
        assertTrue(results.stream().anyMatch(item -> "Item 5".equals(item.getName())));
    }

    private Set<ItemEntityTest> createSources() {
        Set<ItemEntityTest> sources = new HashSet<>();
        sources.add(ItemEntityTest.builder().id(1L).name("Item 1").build());
        sources.add(ItemEntityTest.builder().id(2L).name("Item 2").description("Description 2").build());
        sources.add(ItemEntityTest.builder().id(3L).name("Item 3").build());
        return sources;
    }
}
