/*
모바일 환경에서는 로그인이 상당히 불편하기 때문에 대부분의 서비스에는
'remember-me' 라는 기능을 많이 사용

기존 로그인 유지 방법이 HttpSession 을 이용했던 것과 달리 remember-me라는 쿠키의 유효기간을 지정해서
쿠키를 브라우저에 저장하게하고 쿠키의 값을 특정 문자열을 보관시켜서 로그인 관련 정보를 유지하는 방식

스프링 시큐리티의 remember-me 를 이용하는 방법은 설정을 변경하는 것만으로 가능한데
지금 상황과 같이 커스텀 로그인 페이지를 만드는 경우에는 약간의 추가가 필요
*/

/*
 쿠키값을 대조할 수 있는 테이블 생성
   -> 쿠키값과 데이터베이스의 특정 필드를 대조
 persistent_login 이라는 이름의 테이블 생성
   -> 이 테이블의 이름은 스프링 시큐리티 내부에서 사용하기 때문에 변경하지 않도록 유의
 */

CREATE TABLE `persistent_logins`
(
    `username` VARCHAR(64) NOT NULL ,
    `series` VARCHAR(64) PRIMARY KEY ,
    `token` VARCHAR(64) NOT NULL ,
    `last_used` TIMESTAMP NOT NULL
)