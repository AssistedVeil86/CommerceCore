package com.colibrihub.CommerceCore.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private Long totalItems;
    private int totalPages;
    private Boolean first;
    private Boolean last;
    private Boolean empty;

    public static<T> PagedResponse<T> of (Page<T> page){
        var response = new PagedResponse<T>();

        var currentPage = page.getNumber();
        var content = page.getContent();

        response.setContent(content);
        response.setPage(currentPage);
        response.setSize(page.getSize());
        response.setTotalItems(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setFirst(currentPage == 0);
        response.setLast(currentPage > page.getTotalElements() - 1);
        response.setEmpty(content.isEmpty());

        return response;
    }
}
