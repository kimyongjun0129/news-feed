package org.example.newsfeed.post.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

// 페이지로 감싸기
@Getter
public class PageDto<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    // Page 래핑
    // Page는 메서드를 통해 값을 제공
    // 그냥 Page로 response를 받아도 데이터가 담겨있지만
    // Pageable을 바탕으로 계산해서 알려주는 것이기 때문에
    // 직접 메서드를 사용해 값을 넣어주고 response를 반환한다
    public PageDto(Page<T> pageData) {
        this.content = pageData.getContent();
        this.page = pageData.getNumber();
        this.size = pageData.getSize();
        this.totalElements = pageData.getTotalElements();
        this.totalPages = pageData.getTotalPages();
        this.last = pageData.isLast();
    }
}