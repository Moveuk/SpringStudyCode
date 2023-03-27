package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    //준영속 엔티티 수정시 merge를 이용한 방법.
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    //준영속 엔티티 수정시 변경감지(더티체킹)을 이용한 방법.
    @Transactional
    public void updateItem(Long itemId, UpdateItemDto updateItemDto) {
        Book findItem = (Book) itemRepository.findOne(itemId);

        findItem.setName(updateItemDto.getName());
        findItem.setPrice(updateItemDto.getPrice());
        findItem.setStockQuantity(updateItemDto.getStockQuantity());
        findItem.setAuthor(updateItemDto.getAuthor());
        findItem.setIsbn(updateItemDto.getIsbn());
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
