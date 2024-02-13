/* 메인이미지 슬라이드 */
$(document).ready(function() {
    var images = ['resources/images/main_image01.png', 'resources/images/main_image02.png', 'resources/images/main_image03.png'];
    var currentIndex = 0;
    var imageInterval;

     function changeImage(index) {
		 // Dot 업데이트
    	updateDots(index);
		 
        var $currentImg = $('#main_img .img_current');
        var $nextImg = $('#main_img .img_next');

        // 다음 이미지 설정 및 투명도 초기화
        $nextImg.attr('src', images[index]).css('opacity', 0.02).show();

        // 현재 이미지 투명도 감소 및 다음 이미지 투명도 증가
        $currentImg.animate({ opacity: 0.2 }, 500);
        $nextImg.animate({ opacity: 1 }, 500, function() {
            // 현재 이미지 소스 변경 및 투명도 재설정
            $currentImg.attr('src', images[index]).css('opacity', 1);
            
            // 인덱스 업데이트
            currentIndex = index;
        });
    }

    function updateDots(index) {
        // 모든 dots에서 dot_active 클래스 제거
        $('#dots div').removeClass('dot_active').addClass('dot');

        // 현재 인덱스의 dot에 dot_active 클래스 추가
        $('#dots div').eq(index).removeClass('dot').addClass('dot_active');
    }

    // Dot 클릭 이벤트 핸들러
    $('#dots div').click(function() {
        var index = $(this).index(); // 클릭된 dot의 인덱스
        currentIndex = index;
        changeImage(index);
        restartInterval();
    });

    // 이미지 자동 변경 함수
    function startInterval() {
        imageInterval = setInterval(function() {
            currentIndex = (currentIndex + 1) % images.length;
            changeImage(currentIndex);
        }, 5000); // 5초 간격
    }

    // 인터벌 재시작 함수
    function restartInterval() {
        clearInterval(imageInterval);
        startInterval();
    }

    // 이미지 자동 변경 시작
    startInterval();
});