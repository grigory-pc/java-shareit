package ru.practicum.shareit.requests.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long>, CrudRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByUserIdOrderByCreatedDesc(long userId);

    ItemRequest findById(long requestId);

    List<ItemRequest> findAllByUserIdIsNot(long userId, Pageable pageable);
}