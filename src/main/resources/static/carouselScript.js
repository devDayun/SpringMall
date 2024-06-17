var slideIndex = 1; // slideIndex를 1로 초기화
showSlides(slideIndex);

function plusSlides(n) {
    showSlides(slideIndex += n);
}

function currentSlides(n) {
    showSlides(slideIndex = n);
}

function showSlides(n) { // 파라미터 n은 슬라이드의 번호

    // HTML 문서에서 클래스명이 "mySlides"와 "dot"으로 지정된 모든 엘리먼트를 가져와서 각각 slides와 dots 배열에 저장
    var i;
    var slides = document.getElementsByClassName("mySlides");
    var dots = document.getElementsByClassName("dot");

    // 파라미터 n이 슬라이드의 개수를 벗어날 경우를 처리
    // n이 현재 슬라이드의 개수보다 크다면, 첫 번째 슬라이드로 이동
    if (n > slides.length) {
        slideIndex = 1;
    }
    // n이 1보다 작다면, 마지막 슬라이드로 이동
    if (n < 1) {
        slideIndex = slides.length
    }

    // 모든 슬라이드와 점들을 숨기는 역할, 각 슬라이드의 display 속성을 "none"으로 설정하여 화면에서 보이지 않도록
    for (i = 0; i < slides.length; i++) {
        slides[i].style.display = "none";
    }
    for (i = 0; i < dots.length; i++) {
        dots[i].className = dots[i].className.replace(" active", "");
    }

    // 현재 활성화된 슬라이드와 해당하는 점을 보이도록, 배열index 유의
    slides[slideIndex - 1].style.display = "block";
    dots[slideIndex - 1].className += " active";
}

// 자동 슬라이드 전환을 위한 함수
function autoSlide() {
    slideIndex++;
    showSlides(slideIndex);
}

// 3초마다 자동 슬라이드 전환
var slideInterval = setInterval(autoSlide, 3000);

// 슬라이드 전환 시 자동 슬라이드 타이머 재설정
function plusSlides(n) {
    clearInterval(slideInterval);
    slideIndex += n;
    showSlides(slideIndex);
    slideInterval = setInterval(autoSlide, 3000); // 3초마다 자동 슬라이드 타이머 다시 설정
}