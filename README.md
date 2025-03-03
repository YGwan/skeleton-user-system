# skeleton user system used by Spring security

<br>

## 스프링 시큐리티 요청 처리 흐름

1. 사용자가 자격증명 없이 보안 페이지에 처음으로 접근하려고 시도한다.
2. AuthorizationFilter, AbstractAuthenticationProcessingFilter, DefaultLoginPageGeneratingFilter 등과 같은 필터가 작동하여 최종 사용자에게
   자격 증명을 입력할 수 있는 로그인 페이지를 표시한다.
3. 사용자가 사용자 이름과 비밀번호를 입력하면, 해당 정보가 다시 Spring Securiy 필터에 의해 처리된다.
4. UsernamePasswordAuthenticationFilter와 같은 필터가 요청에서 자격 증명에 필요한 username & password을 추출하고, 이를 기반으로 Authentication 객체를
   채운다.
5. 채워진 인증 객체는 AuthenticationManager의 구현체인 ProviderManger에 입력으로 제공된다.
6. providerManger 내부에 authenticate() 메서드에서 모든 적용 가능한 AuthenticationProvider을 반복하여 처리한다. (기본적으로는
   DaoAuthenticationProvider)
7. 반복되는 AuthenticationProvider의 authenticate() 메서드에서 UserDetailService (UserDetailManager)와 PasswordEncoder 클래스를 이용하여 유저
   인증을 진행한다.
8. 인증이 성공하면 이에 대한 세부 정보를 ProviderManager로 반환하고, 이를 다시 Spring Security로 전달하여 SpringContext에 저장된다.

<br>

## 스프링 시큐리티에서 제공하는 여러 필터들 정의

- [스프링 시큐리티에서 제공하는 여러 필터들 정의](docs/filters-provided-by-spring-security.md)
- 모든 필터들을 다 사용하는 것은 아니다.
- 상황에 맞게 필터를 사용하거나, 기본적으로 제공하는 필터들을 커스텀해서 주로 사용한다.

<br>

## 스프링 시큐리티 사용 시 알아두면 좋은 개념들

### SecurityConetxtHolder

- SecurityContextHolder > SecurityContext > Authentication 의 구조를 가짐
- Authentication
   - Principle (인증 주체 - 이름)
   - Credentials (비밀번호)
   - Authorities (권한)
- 기본적으로 ThreadLocal 방식으로 저장함

<br>

### UserDetailsService, UserDetailsManager
- 사용자 계정을 처리하는 클래스
- 단순히 사용자를 조회하는 로직을 구현할 경우 UserDetailService만 구현하면 됨 (loadUserByUsername) : 반환 타입 UserDetails
- 최종 사용자의 CRUD를 다 구현할 경우 UserDetailsManager를 구현해야됨
   - createUser, updateUser, deleteUser, changePassword, userExists
   - 대표적인 UserDetailsManager 구현체
      - InMemoryUserDetailsManager, JdbcUSerDetailsManager
      - 이러한 것들은 spring security에서 제공하는 것이기 때문에 제한 사항이 존재 → Customizing 필요
- 대부분 UserDetailsManager의 경우 유저 생성, 업데이트, 삭제 등은 별도의 api로 처리되는 경우가 많기 때문에 userDetailsService를 주로 커스터마이징해서 사용한다.
   - loadUserByUsername()만 재정의해주면 된다.
- Spring security 내부에서 사용자는 사용자 이름, 자격 증명, 권한 등이 포함된 UserDetails로 표현된다.
   - UserDetails의 구현체는 spring security에서 제공하는 user가 있다.

<br>

### AuthenticationProvider
- UserDetailsService & UserDetailsManager + PasswordEncoder을 사용해서 유저를 검증함
- 유저가 검증이 완료되면, Authentication 객체를 만들어 이를 반환함
- 구현해야 할 메서드
   - authenticate()
      - authentication 객체는 로그인 시도한 사용자의 인증 정보를 담고 있습니다.
      - 인증이 성공하면 Authentication 객체를 반환해야 합니다.
      - 인증이 실패하면 AuthenticationException을 던집니다.
   - supports(Class<?> authentication)
      - AuthenticationProvider가 처리할 수 있는 Authentication 타입을 결정하는 역할
      - Spring Security는 여러 개의 AuthenticationProvider를 가질 수 있어 AuthenticationManager는 등록된 여러 AuthenticationProvider 중에서 supports()가 true를 반환하는 AuthenticationProvider만 사용

<br>

### Authentication
- Principle : 유저에 대한 정보 (주로 UserDetails를 사용함)
- getName() : username
- getAuthorities() : 권한 정보
- getCredentials() : 비밀번호
- 그렇다면, UserDetails랑 다른게 무엇인가?
   - isAuthenticated(), setAuthenticated() 같이 인증이 성공했는지 여부를 식별하는데 도움이 되는 메서드 존재
   - 인증이 완료된 후 비밀번호 같이 세부 정보를 지우기 위한 eraseCredentials() 존재
   - 구분 이유
      - userDetails에는 인증이 완료된 후에는 필요 없는 정보들이 많음( isAccountNonExpired(), isAccountNonLocked(), isEnabled() 등
      - 이러한 메서드 & 값들을 외부에 노출하지 않기 위해서 Autentication
- Authentication 대표적인 구현체 : UsernamePasswordAuthenticationToken

```
UserDetails

- UserDetails는 저장 시스템에서 사용자 정보를 가져오려고 할 때, 모든 시나리오에서 사용됨
- UserDetailsManager, UserDetailsService의 구현 내부에서 수행됨

Authentication

- 인증이 성공했는지 여부를 결정하려고 할 때 모든 시나리오에서 사용됨
- AuthenticationProvider, AuthenticationManage의 구현 내부에서 사용됨
```

<br>

### 예외 처리

- 인증 예외 처리 (AuthenticationException)
   - 발생 시점: 사용자가 올바른 인증 정보 없이 보호된 리소스에 접근할 때
   - HTTP 응답 코드: 401 Unauthorized
   - 핸들러: AuthenticationEntryPoint로 예외를 위임하여 처리

- 권한 예외 처리 (AccessDeniedException)
   - 사용자는 인증되었지만 특정 리소스에 대한 접근 권한이 없을 때
   - HTTP 응답 코드: 403 Forbidden
   - AccessDeniedHandler로 예외를 위임하여 처리

<br>

### BCrypt 해시 구조
- 크게 4가지 부분으로 존재
```bash
$2a$10$XvVw1HyOzzKf0Jfbi3PoLOsm7l7.P.tVxovfFP7pkjJXYp3hCmPqS
```

1. $2a$ → 알고리즘 버전 (BCrypt 버전 정보)
2. 10$ → cost factor (해시 반복 횟수, 숫자가 클수록 보안 강화)
3. XvVw1HyOzzKf0Jfbi3PoLO → salt 값 (22자)
4. sm7l7.P.tVxovfFP7pkjJXYp3hCmPqS → 해시된 비밀번호 값

<br>

## Spring Security 권한 처리

### 1. URL 기반 권한 처리 (HttpSecurity)
   - 특정 URL 패턴에 대한 접근 권한 설정
   ``` java
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http
           .authorizeHttpRequests(auth -> auth
               .requestMatchers("/admin/**").hasRole("ADMIN")   // ADMIN만 접근 가능
               .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN") // USER, ADMIN 가능
               .requestMatchers("/public/**").permitAll() // 모든 사용자 접근 가능
               .anyRequest().authenticated() // 그 외 요청은 인증된 사용자만 가능
           )
       return http.build();
   } 
   ```
   - permitAll() : 누구나 접근 가능
   - authenticated() : 인증된 사용자만 접근 가능
   - hasRole('ROLE_ADMIN') : 특정 역할(Role) 보유자만 가능
   - hasAnyRole('ROLE_ADMIN', 'ROLE_USER')	: 여러 역할(Role) 중 하나라도 보유하면 가능
   - hasAuthority('PERMISSION_NAME') : 특정 권한(Permission) 보유자만 가능
   - denyAll()	: 누구도 접근할 수 없음

<br>

### 2. 메서드 기반 권한 처리 (@PreAuthorize, @PostAuthorize)
   - 메서드 단위에서 사용자의 역할(Role)이나 특정 조건을 검사하여 실행 제한
   ``` java
   @PreAuthorize("hasRole('ADMIN')")
   public void deleteUser(Long userId) {
       userRepository.deleteById(userId);
   }
   
   @PostAuthorize("returnObject.owner == authentication.principal.username")
   public Order getOrder(Long orderId) {
       return orderRepository.findById(orderId).orElseThrow();
   }
   ```
   - @PreAuthorize (메서드 실행 전에 권한 검사)
   - @PostAuthorize (메서드 실행 후 반환값을 확인하여 권한 검사)

<br>

### 3. 필터 기반 권한 처리 (Security Filter)
   - Spring Security는 요청을 처리하는 필터 체인을 가지고 있어, 특정 필터에서 권한을 직접 체크
   - (ex) jwtAuthenticatedFilter, UsernamePasswordAuthenticationFilter

<br>

## ● OAuth2.0 & OIDC
- [OAuth 정리](docs/oauth2.0-spring-security.md)
- 예시 코드

### 1. 의존성 추가
    ``` java
    // OAuth2 Cilent
    implementation 'org.springframework.security:spring-security-oauth2-client'
    
    // Oauth2 Resource Server
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-resource-server'
    ```

<br>

### 2. 제공할 소셜 로그인 측 설정
    - Client ID 발급
    - Client Secret ID 확인
    - redirect URL 지정 등

<br>

### 3. Config 파일 생성
``` java
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.
            // 기타 처리
            .formLogin(Customizer.withDefaults())
            .oauth2Login(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration google = googleClientRegistration();
        ClientRegistration github = githubClientRegistration();
        return new InMemoryClientRegistrationRepository(google, github);
    }

    private ClientRegistration googleClientRegistration() {
        return CommonOAuth2Provider.GOOGLE.getBuilder("google").clientId("[발급한 클라이언트 ID]")
                .clientSecret("[발급한 client security id]").build();
    }

    private ClientRegistration githubClientRegistration() {
        return CommonOAuth2Provider.GITHUB.getBuilder("github").clientId("[발급한 클라이언트 ID]")
                .clientSecret("[발급한 client security id]").build();
    }
}

```
- 기본적으로 google, github, facebook, okta는 ClientRegistration을 제공한다. (spring security 측에서 제공해줌)
- kakao나 다른 것을 추가하려면 별도의 ClientRegistration을 만들어야 한다.
``` java

    @Bean
    ClientRegistrationRepository clientRegistrationRepository() {
        ClientRegistration kakao = kakaoClientRegistration();
        return new InMemoryClientRegistrationRepository(kakao);
    }
    
    @Bean
    public ClientRegistration kakaoClientRegistration() {
        ClientRegistration kakaoRegistration = ClientRegistration.withRegistrationId("kakao")
            .clientId("YOUR_KAKAO_CLIENT_ID")
            .clientSecret("YOUR_KAKAO_CLIENT_SECRET")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri("{baseUrl}/login/oauth2/code/kakao")
            .scope("profile_nickname", "profile_image", "account_email")
            .authorizationUri("https://kauth.kakao.com/oauth/authorize")
            .tokenUri("https://kauth.kakao.com/oauth/token")
            .userInfoUri("https://kapi.kakao.com/v2/user/me")
            .userNameAttributeName("id")
            .clientName("Kakao")
            .build();
            
```

<br>

### 4. Config 파일 대신 환경 변수 파일로 처리하는 방법
``` java
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}

spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}

spring.security.oauth2.client.registration.kakao.client-id=YOUR_KAKAO_CLIENT_ID
spring.security.oauth2.client.registration.kakao.client-secret=YOUR_KAKAO_CLIENT_SECRET
spring.security.oauth2.client.registration.kakao.client-name=Kakao
spring.security.oauth2.client.registration.kakao.provider=kakao
spring.security.oauth2.client.registration.kakao.authorization-grant-type=authorization_code
spring.security.oauth2.client.registration.kakao.redirect-uri={baseUrl}/login/oauth2/code/{registrationId}
spring.security.oauth2.client.registration.kakao.scope=profile_nickname, profile_image, account_email

spring.security.oauth2.client.provider.kakao.authorization-uri=https://kauth.kakao.com/oauth/authorize
spring.security.oauth2.client.provider.kakao.token-uri=https://kauth.kakao.com/oauth/token
spring.security.oauth2.client.provider.kakao.user-info-uri=https://kapi.kakao.com/v2/user/me
spring.security.oauth2.client.provider.kakao.user-name-attribute=id
```

<br><br><br>