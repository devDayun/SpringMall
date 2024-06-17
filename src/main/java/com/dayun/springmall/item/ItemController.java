package com.dayun.springmall.item; // 이 파일의 경로, package를 작성하지 않으면 해당 class를 다른 곳에서 사용할 수 없음

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j  // 로그 선언
@RequestMapping("/springmall")
@Controller // 데이터 또는 html을 보내는 기능을 담당 [중요] HTTP 요청이 정상인지 검증
@RequiredArgsConstructor    // lombok 문법 -> 생성자 만들어 줌 (의존성 주입 위해)
public class ItemController {   // 비슷한 API는 모아두자 이후에 찾기 쉽도록, 하나의 클래스엔 비슷한 기능의 함수만 보관하는 게 좋다.
    /**
     * 코드를 짜는 방법
     * 1) 한글로 기능이 어떻게 동작하는지 자세히 적는다
     * 2) 코드로 번역한다
     */

    private final ItemService itemService;

    /**
     * Pagination 만들기
     * https://zepinos.tistory.com/28
     * https://eunoia07.tistory.com/entry/MSSQL-%EA%B2%8C%EC%8B%9C%ED%8C%90-paging%EB%A5%BC-%EC%B2%98%EB%A6%AC%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95-3%EA%B0%80%EC%A7%80
     * <p>
     * 필수 조건
     * 1. 한 페이지에 출력할 게시물 수 (ex) 5개)
     * 2. 한 화면에 출력할 페이지 수( ex) << < 1 2 3 4 5 > >> )
     * 3. 현재 페이지 번호
     * <p>
     * 구현 순서
     * 1. 전체 게시글 수 구하기 (14개) / 쿼리 : select count(*) from item;
     * 2. 각 페이지에 출력할 게시물 범위 계산 (현재 페이지가 2페이지일 때를 예로 듬)
     * -> 시작 값 (현재 페이지 - 1) * 한 페이지에 출력할 게시물 수 + 1 (2-1) * 5 + 1 = 6
     * -> 종료 값 (현재 페이지 * 한 페이지에 출력할 게시물 수) (2 * 5) = 10
     * 3. 전체 페이지 수 계산 -> 나머지 값이 있으면 1을 더해주기(마지막 게시물 출력 위해)
     * 전체 게시글 수 / 한 페이지에 출력할 게시물 수 14 / 5 = 3 (2.xx)
     * 4. (추가) 이전 페이지 바로가기 ( < )
     */

    @GetMapping()
    String mainPage() {
        return "main";
    }

    @GetMapping("/list/{pageNumber}")
    String itemListPage(Model model, @PathVariable Integer pageNumber) {  // Pagination 전체 데이터 조회
        // 꺼낸 데이터 넣기 -> HTML에 데이터를 넣기
        model.addAttribute("pages", itemService.itemListPage(pageNumber));
        // @Controller 는 반환 값이 String 이면 뷰 이름으로 인식된다. 그래서 뷰를 찾고 뷰가 랜더링 된다.
        return "itemList";
    }

    @GetMapping("/list")
    String itemList(Model model) {  // 전체 데이터 조회
        // 꺼낸 데이터 넣기 -> HTML에 데이터를 넣기
        model.addAttribute("items", itemService.itemList());
        // @Controller 는 반환 값이 String 이면 뷰 이름으로 인식된다. 그래서 뷰를 찾고 뷰가 랜더링 된다.
        return "itemList";
    }

    // 1) 만든 상품 등록 폼에 접근 (/add로 접속 시 -> 상품 등록 폼이 나온다)
    @GetMapping("/add")
    String itemAddForm() {
        return "itemWrite";
    }

    // 2) 폼에서 전송버튼을 누르면 서버로 보낸다, URL 작명 시 명사가 좋다
    @PostMapping("/add")
    String itemAdd(@Validated @ModelAttribute Item item) {
        /**
         * 서버에서 폼을 검사(validation)한다
         * 검증 내용이 타입 체크, 간단한 필드 내용이기 때문에 Controller에서 처리하기로 결정
         * 비즈니스 로직에 가까운 경우(결제 시 잔액 부족등의 검사)는 Service에서 처리하는 게 좋다
         * -> 특정 필드에 대한 검증 로직을 편리하게 작성하기 위해 gradle에 Bean Validaiton을 등록했다
         * -> 도메인(테이블)에 검증 내용을 작성한다
         * -> 검증하고자 하는 도메인 파라미터 앞에 @Validated annotation을 붙여야 한다
         */
        itemService.saveItem(item);
        return "redirect:/list";    // url 이름이다. html을 적는 게 아니다.
    }

    @GetMapping("/items/{id}")
    String itemDetail(@PathVariable Long id, Model model) {

        Optional<Item> result = itemService.itemDetail(id);
        if (result.isPresent()) {    // 데이터가 있는지 체크하는 것이 중요 , 데이터가 없다면 서버 다운
            // 꺼낸 데이터 넣기
            model.addAttribute("items", result.get());
            return "itemDetail";
        } else {    // 데이터가 없는 경우 다른 페이지로 돌아가거나 에러페이지를 만들어서 보여주기
            return "error";
        }

    }

    @GetMapping("/edit/{id}")
    String itemEditForm(@PathVariable Long id, Model model) {
        // DB에서 해당 id에 맞는 데이터를 찾아온다
        Optional<Item> result = itemService.itemDetail(id);

        if (result.isPresent()) {   // 데이터가 있다면
            // 수정 폼에 넣어준다.
            model.addAttribute("items", result.get());
            return "itemEdit";
        } else {
            return "redirect:/list";
        }
    }

    /**
     * 서버에서 모르는 정보는
     * 1. 유저에게 보내라고 하거나
     * 2. DB 조회해보거나
     */
    @PostMapping("/edit/{id}")
    String itemEdit(@PathVariable Long id, @Validated @ModelAttribute Item item) {
        itemService.itemEdit(id, item);
        return "redirect:/items/{id}";
    }

    @DeleteMapping("/delete")
    ResponseEntity<String> itemDelete(@RequestParam Long id) {  // queryString으로 보낸 데이터 출력
        itemService.itemDelete(id);
        return ResponseEntity.status(200).body("삭제 완료");
    }

}
