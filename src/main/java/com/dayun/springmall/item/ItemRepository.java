package com.dayun.springmall.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {   // <Entity 명, id 컬럼 타입>
    Page<Item> findPageBy(Pageable page);
}
