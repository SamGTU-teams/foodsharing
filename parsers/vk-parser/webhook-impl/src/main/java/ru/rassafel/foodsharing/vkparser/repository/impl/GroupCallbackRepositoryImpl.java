package ru.rassafel.foodsharing.vkparser.repository.impl;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.rassafel.foodsharing.vkparser.repository.GroupCallbackRepository;

import static java.util.Objects.requireNonNullElse;

/**
 * @author rassafel
 */
@RequiredArgsConstructor
@Repository
public class GroupCallbackRepositoryImpl implements GroupCallbackRepository {
    private final Cache<Integer, Integer> callbackCache;

    @Override
    public boolean registerPost(Integer groupId, Integer postId) {
        Integer lastPostId = getLastPostId(groupId);
        if (requireNonNullElse(lastPostId, -1) > postId) {
            return false;
        }
        callbackCache.put(groupId, postId);
        return true;
    }

    public Integer getLastPostId(Integer groupId) {
        return callbackCache.getIfPresent(groupId);
    }
}
