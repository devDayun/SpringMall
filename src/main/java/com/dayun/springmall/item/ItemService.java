package com.dayun.springmall.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service    // 비즈니스 로직 -> 검증(validation), DB 입출력
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public void saveItem(Item item) {   // form에서 보낸 데이터를 해당 타입으로 변환
        // 검증 로직 추가 필요
        itemRepository.save(item);

    }

    public List<Item> itemList() {

        // Item 테이블의 데이터 꺼내기
        return itemRepository.findAll();
    }

    public Page<Item> itemListPage(Integer pageNumber) {
        return itemRepository.findPageBy(PageRequest.of(pageNumber-1,5, Sort.Direction.DESC));
    }

    public Optional<Item> itemDetail(Long id) {   // URL 파라미터 문법 사용 -> 비슷한 URL API 여러개 만들 필요없다
        /**
         * JPA에서 id에 맞는 데이터 꺼내기
         * 즉 @PathVariable로 가져온 id 값(유저가 url 파라미터에 입력한 값)을 넣기
         * Optional 타입 -> 비어있을 수도 있고 <>안에 든 값일 수도 있다
         */
        return itemRepository.findById(id);
    }

    public void itemEdit(Long id, Item item) {
        /**
         * 내가 시도한 방법
         * 파라미터에 Item item 추가
         * Optional<Item> result = itemRepository.findById(id);
         * result.get().setItemName(item.getItemName());
         * result.get().setItemPrice(item.getItemPrice());
         */

        // 새로 배운 방법
        // Item item = new Item();
        item.setId(id); // DB에 해당 id 행이 있으면 아래의 setter 함수 내용을 덮어쓴다
        item.setItemName(item.getItemName());
        item.setItemPrice(item.getItemPrice());
        itemRepository.save(item);

    }

    public void itemDelete(Long id) {
        itemRepository.deleteById(id);
    }
}
