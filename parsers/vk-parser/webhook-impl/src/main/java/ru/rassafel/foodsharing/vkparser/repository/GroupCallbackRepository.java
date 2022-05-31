package ru.rassafel.foodsharing.vkparser.repository;

/**
 * @author rassafel
 */
public interface GroupCallbackRepository {
    boolean registerPost(Integer groupId, Integer postId);
}
