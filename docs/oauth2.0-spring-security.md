# OAuth2.0 & OIDC

## OAuth
- 무료 & 개방형(Open Standard) 프로토콜
- IETF 표준 기반 & 오픈 웹 재단 라이선스 기반
- 비밀번호 공유 없이 서비스 간 안전한 권한 위임 가능

<br>

## OAuth 2.0
- OAuth 1.0을 개선하여 더 유연하고 보안성이 높은 방식으로 설계된 프로토콜

<br>

### OAuth 1.0과의 차이
- OAuth 2.0은 OAuth 1.0보다 보안, 확장성, 편의성이 크게 향상됨
- HTTPS를 기반으로 보안성이 강화되었으며, 서명(Signature) 방식이 필요 없음
- 4가지 Grant Type을 지원하여 다양한 환경에서 사용 가능
- 소셜 로그인, API 인증, IoT 등 다양한 플랫폼에서 활용 가능

<br>

### OAuth 2.0 특징
- 비밀번호를 공유하지 않고도 애플리케이션 간 데이터 접근 가능
- 사용자의 동의를 받아 OAuth 인증 서버가 액세스 토큰 발급
- 대표적인 플로우: Authorization Code Grant 방식
- 보안 강화를 위해 OpenID Connect(OIDC)와 함께 사용 가능

<br>

### 인증 흐름
1. 사용자가 애플리케이션에 권한 부여
2. 클라이언트가 인증 서버에 인가 코드(Authorization Code) 요청
3. 인가 코드를 사용해 엑세스 토큰(Access Token) 발급 요청
4. 엑세스 토큰을 사용해 리소스 서버에 데이터 요청

<br>

### 주요 인증 방식
- Authorization Code Grant : 인가 코드 기반으로 액세스 토큰 발급
- Implicit Grant (Deprecated) : 브라우저에서 직접 액세스 토큰 발급 (보안 취약)
- Client Credentials Grant : 사용자 개입 없이 서버 간 인증
- Resource Owner Password Credentials Grant : 사용자가 직접 ID/PW 입력하여 토큰 발급 (보안 취약)

<br>

### PKCE
- PKCE(Proof Key for Code Exchange)는 OAuth 2.0의 “Authorization Code Grant” 방식에서 보안성을 강화하기 위한 확장 기능
- 공개 클라이언트(Public Client, 즉 모바일 앱 & SPA 같은 비밀키 저장이 어려운 환경)에서 인가 코드 탈취 공격(Code Interception Attack)을 방지하기 위해 사용됨
- 기존 문제
  - 인가 코드가 리디렉트 URL을 통해 클라이언트로 전달됨
  - 중간자 공격(Man-in-the-Middle Attack)으로 인가 코드가 탈취될 가능성이 있음
  - 만약 공격자가 인가 코드를 가로채면, 악의적인 클라이언트가 Access Token을 발급받을 수 있음
- PKCE 동작 방식
  - Code Verifier”와 “Code Challenge”라는 두 개의 값을 사용해서 인가 코드 탈취 공격을 방어
  1. 클라이언트가 Code Verifier를 생성 (랜덤한 문자열)
  2. SHA-256 해싱을 적용하여 Code Challenge를 생성
  3. 클라이언트가 Code Challenge를 포함하여 인가 코드 요청
  4. 인가 서버는 Code Challenge를 저장한 후, 인가 코드 발급
  5. 클라이언트가 인가 코드와 함께 Code Verifier를 인증 서버에 전송
  6. 인가 서버가 Code Verifier를 확인한 후, Access Token 발급

<br>

## 주로 사용 용어

### Resource Owner(리소스 소유자)
- 보호된 리소스를 소유한 사용자(User) 또는 애플리케이션
- 최종적으로 인증을 승인하는 주체
- 리소스를 가지고 있는 사람 = 사용자(user)

<br>

### Client(클라이언트)
- OAuth 2.0을 사용하는 애플리케이션
- 사용자의 승인을 받아 리소스 서버에 요청하는 역할
- 클라이언트는 리소스 소유자의 인증 정보를 직접 저장하지 않고, Access Token을 이용해 API 요청
- 데이터를 요청하는 애플리케이션 = 사용자가 사용하는 앱

<br>

### Autorization Server(인증 서버)
- 사용자의 인증을 수행하고 Access Token을 발급하는 서버
- 리소스 소유자가 승인하면, 클라이언트에게 Authorization Code를 발급하고, 이를 통해 Access Token을 발급해 줌
- 사용자의 신원을 확인하고, 클라이언트에게 Access Token을 발급해주는 서버
- ex) 구글 로그인, 카카오 로그인

<br>

### Resource Server(리소스서버)
- Access Token을 검증한 후, 보호된 리소스를 제공하는 서버
- API 요청을 받아, 사용자의 데이터나 기능을 제공
- Access Token을 확인하고, 사용자의 데이터를 제공하는 서버(API 서버)
- ex) 구글 API 서버, 카카오 API 서버

<br>

### Scopes(스코프)
- 클라이언트가 리소스 소유자의 데이터에 어느 범위까지 접근할 수 있는지 정의
- 클라이언트가 사용자의 어떤 정보에 접근할 수 있는지를 결정하는 권한 목록
- ex) scope=email profile openid : 이메일 & 프로필 정보만 허용, 다른건 허용 X

<br>

## OpenID Connect(OIDC)

- OpenID Connect (OIDC)는 OAuth 2.0을 기반으로 사용자 인증(Authentication)을 추가한 프로토콜
- OAuth 2.0이 “권한 위임(Authorization)“을 위한 것이라면, OIDC는 “사용자 인증(Authentication)“을 위한 것
- OAuth 2.0은 “API 사용 권한을 주는 것”, OIDC는 “사용자가 누구인지 확인하는 것

| 비교 항목         | OAuth 2.0                              | OpenID Connect (OIDC)                 |
|------------------|----------------------------------------|---------------------------------------|
| 주요 목적       | API에 대한 권한 위임 (Authorization)       | 사용자 인증 (Authentication)            |
| 사용자 정보 제공 | 제공 X                                  | 사용자 정보 (ID Token) 제공             |
| 발급 토큰      | Access Token                           | ID Token + Access Token               |

<br>

### 핵심 용어

- ID Token : 사용자의 정보를 포함한 JSON Web Token (JWT
- UserInfo Endpoint : 사용자 정보를 가져오는 API
- Claims : ID Token에 포함된 사용자 정보 (이메일, 이름 등)

<br>

### ID Token
- 사용자가 인증되었음을 증명하는 JWT(Json Web Token) 형식의 토큰
- D Token에는 사용자의 정보를 담고 있음
- JWT 포맷이므로 쉽게 검증 가능
- UserInfo Endpoint 없이도 사용자 정보를 바로 확인할 수 있음

<br>

```
OIDC는 OAuth2.0에 향상된 기능이라고 생각하면 된다. 
```

<br><br><br>