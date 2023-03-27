package jpabook.jpashop.service;

import jpabook.jpashop.controller.BookForm;
import lombok.Getter;

@Getter
public class UpdateItemDto {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

    public UpdateItemDto formToDto(BookForm form) {
        this.name = form.getName();
        this.price = form.getPrice();
        this.stockQuantity = form.getStockQuantity();
        this.author = form.getAuthor();
        this.isbn = form.getIsbn();
        return this;
    }
}
