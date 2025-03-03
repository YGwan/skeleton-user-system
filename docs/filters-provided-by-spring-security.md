# 스프링 시큐리티에서 제공되는 필터

### 1. DisableEncodeUrlFilter
- URL로 간주되지 않는 부분을 포함하지 않도록 설정
- DefaultSecurityFilterChain에 기본적으로 등록되는 필터로 가장 첫번째에 위치한다.
- 필터가 등록되는 목적은 url 파라미터에 세션 id가 인코딩되어 로그로 유출되는 것을 방지한다.
- 기존의 response에서는 url + session Id도 같이 전달해주는데, 이러한 유출을 방지하기 위해 단순히 url만 리턴한다.

<br>

### 2. WebAsyncManagerIntegrationFilter
- 비동기로 처리되는 작업에 대해 알맞은 시큐리티 컨텍스트(세션)을 적용
- 필터가 등록되는 목적은 서블릿단에서 비동기 작업을 수행할 때 서블릿 입출력 쓰레드와 작업 쓰레드가 동일한 SecurityContextHolder의 SecurityContext 영역을 참조할 수 있도록 도와준다.
- 즉, SecurityContextHolder의 ThreadLocal 전략에 따라 동일한 쓰레드에서만 SecurityContext에 접근할 수 있는데, 비동기 방식의 경우 하나의 작업을 2개의 쓰레드로 수행하기 때문에 이 부분을 보완하기 위해 필터가 존재한다.

<br>

### 3. SecurityContextHolderFilter
- Security Session 방식에서 주로 사용
- 접근한 유저에 대해 시큐리티 컨텍스트 관리
- 필터가 등록되는 목적은 이전 요청을 통해 이미 인증된 사용자 정보를 현재 요청의SecurityContextHolder의 SecurityContext에 할당하는 역할을 수행하고, 현재 요청이 끝나면 SecurityContext를 초기화한다.

<br>

### 4. HeaderWriterFilter
- 보안을 위한 응답 헤더 추가 (X-Frame-Options, X-XSS-Protection and X-Content-Type-Options)
- 필터가 등록되는 목적은 HTTP 응답 헤더에 사용자 보호를 위한 시큐리티 관련 헤더를 추가하는 필터이다.

<br>

### 5. CorsFilter
- CORS 설정 필터
- API 서버를 구축하여 프론트 & 백엔드의 오리진이 다르면 발생하는 CORS 문제 해결 필요
- 필터가 등록되는 목적은 CorsConfigurationSource 값을 설정하여 CORS 처리를 설정하는 필터이다.

<br>

### 6. CsrfFilter
- CSRF 방어 필터
- 사용자의 의지와 무관하게 해커가 강제로 사용자의 브라우저를 통해 서버측으로 특정한 요청을 보내도록 공격하는 방식 (CSRF 공격)
- HTTP 메서드 중 GET, HEAD, TRACE, OPTIONS 메서드를 제외한 요청에 대해서 검증 진행

<br>

### 7. LogoutFilter
- 로그아웃 요청 처리 시작점 GET : “/logout”
- 필터가 등록되는 목적은 인증 후 생성되는 사용자 식별 정보에 대해 로그아웃 핸들러를 돌며 로그아웃을 수행하는 필터이다.
- CompositeLogoutHandler 클래스에서 등록되어 있는 모든 로그아웃 핸들러는 순회하면서 로그아웃을 수행한다.
- 로그아웃 핸들러들은 LogoutHandler 인터페이스를 구현한 구현 클래스들로 이루어져 있으며 커스텀 핸들러 생성시에도 위 인터페이스 기반으로 작성해야 한다.
- 기본 제공 핸들러
    - SecurityContextLogoutHandler : SecurityContextHolder에 존재하는 SecurityContext 초기화
    - CookieClearingLogoutHandler : SecurityFilterChain의 logout 메소드에서 지정한 쿠키 삭제
    - HeaderWriterLogoutHandler : 클라이언트에게 반환될 헤더 조작
    - LogoutSuccessEventPublishingLogoutHandler : 로그아웃 성공 후 특정 이벤트 실행

<br>

### 8. UsernamePasswordAuthenticationFilter
- username/password 기반 로그인 처리 시작점 POST : “/login”
- 로그인 요청 처리 관련 필터
- form 형태의 username/password 데이터를 받아 인증 클래스에게 값을 넘겨주는 역할 수행
- 커스텀 SecurityFilterChain을 생성하면 자동 등록이 안되기 때문에 등록해야됨.
- username/password를 Form, JSON 방식으로 보낸다고해서 기본적인 로그인 방식은 변하지 않기 때문에 추상 클래스인 AbstractAuthenticationProcessingFilter를 정의하고 각각의 방식에 따라 필터를 구현해서 사용한다.
- 인증 과정
    1. 인증 결과가 존재하면 SecurityContext에 값 저장
    2. 로그인 성공 처리 & 로그인 실패 시 처리 추가로 진행
- ※ 상황에 맞는 인증 진행 로직 구현 방법 → attemptAuthentication 메서드 구현

<br>

### 9. DefaultLoginPageGeneratingFilter
- 로그인 경로에 기본 로그인 페이지를 응답하는 역할을 수행한다.
- 폼 형식을 직접 줄 일이 많지 않기 때문에 크게 고려하지 않아도 된다.

<br>

### 10. DefaultLogoutPageGeneratingFilter
- 로그아웃 페이지를 응답하는 역할을 수행한다.
- 폼 형식을 직접 줄 일이 많지 않기 때문에 크게 고려하지 않아도 된다.

<br>

### 11. BasicAuthenticationFilter
- http basic 기반 로그인 처리 시작점
- basic 인증이란
    - username/password를 입력하면 브라우저가 매 요청 시 BASE64로 인코딩하여 Authorization 헤더에 넣어서 전송한다. 서버는 요청에 대해 username/password만 확인 후 사용자를 기억하지 않기 때문에 매 요청시 Authorization 헤더가 요구됨
    - 스프링 시큐리티를 사용하면 매번 재인증 하는 것이 아닌세션에 값을 저장해 유저를 기억함
    - 별도의 Html 파일이 필요가 없다.

- Form 인증방식
    - form 태그에 username/password을 입력 후 브라우저에서 서버로 전송하면 서버는 상태에 알맞는 세션 또는 jwt을 생성하여 사용자를 기억할 수 있도록 한다.

<br>

### 12. RequestCacheAwareFilter
- 이전 요청 정보가 존재하면 처리 후 현재 요청 판단
- 동작 예시
    1. 로그인하지 않은 사용자가 권한이 필요한 “/my” 경로에 접근
    2. 권한 없음 예외가 발생하고 핸들러에서 “/my” 경로를 기억 후 핸들 작업 수행
    3. 스프링 시큐리티가 “/login” 창을 띄움
    4. username/password를 입력 후 인증을 진행
    5. 로그인 이후 저장되어 있는 “/my” 경로를 불러내서 실행

<br>

### 12. SecurityContextHolderAwareRequestFilter
- ServletRequest에 서블릿 요청에 스프링 시큐리티 API를 다룰 수 있는 메소드를 추가하기 위해서이다.
- 추가되는 스프링 시큐리티 API 메서드
    - authenticate() : 사용자가 인증 여부를 확인하는 메소드
    - login() : 사용자가 AuthenticationManager를 활용하여 인증을 진행하는 메소드
    - logout() : 사용자가 로그아웃 핸들러를 호출할 수 있는 메소드
    - AsyncContext.start() : Callable를 사용하여 비동기 처리를 진행할 때 SecurityContext를 복사하도록 설정하는 메소드

<br>

### 14. AnonymousAuthenticationFilter
- 최초 접속으로 인증 정보가 없고, 인증을 하지 않았을 경우 세션에 익명 사용자 설정
- 여러 필터를 거치면서 현재 지점까지 SecurityContext값이 null인 경우 Anonymous 값을 넣어주기 위해 사용된다.

<br>

### 15. ExceptionTranslationFilter
- 인증 및 접근 예외에 대한 처리
- 이 필터 이후에 발생하는 인증, 인가 예외를 핸들링하기 위해 사용된다.

<br>

### 16. AuthorizationFilter
- 경로 및 권한별 인가 (구. filterSecurityIntercepter)
- SecurityFilterChain의 authorizeHttpRequests()를 통해 인가 작업을 진행한 값에 따라 최종적으로 인가를 수행한다.

<br>

### ● SecurityFilterChain에 커스텀 필터 등록 방법

```
- 특정 필터 이전
http.addFilterBefore(추가할필터, 기존필터.class);

- 특정 필터 위치
http.addFilterAt(추가할필터, 기존필터.class);

- 특정 필터 이후
http.addFilterAfter(추가할필터, 기존필터.class);
```

<br><br><br>