# 협업 관련

## 📌 github 규칙

- main에 커밋 금지(보호 걸어놓음, 메인 빌드용)
- 브랜치 네이밍 컨벤션 준수
  - 이슈 번호
  - `WB-1`

## 📌 이슈 관리

- [JIRA 활용](https://watermelon-clap.atlassian.net/jira/software/projects/CLAP/boards/1)
  - 에픽을 스프린트 단위로 한다.
  - 에픽은 도메인 단위로 한다.
  - 스프린트는 1주일의 주기를 가진다.
  - 스프린트 시작 시 백로그에서 이번주에 진행할 작업을 가져와 스프린트에 채운다.
  - 스프린트 종료 시 회고를 통해 작업 진행속도 및 작업 분배에 대해 논의한다.

<img width="691" alt="image" src="https://github.com/user-attachments/assets/637d80b6-e830-4dea-aea1-e49798aa4c3a">

## 📌 커밋 컨벤션

### 기본 규칙:

```
라벨명[서버 종류]: 한 줄짜리 description `단, 개발 용어를 제외하고는 무조건 한글만 허용`

- 설명 1(최대한 자세하게)
- 설명 2
```

### 라벨 설명

```
feat: 새로운 기능 추가 및 삭제. 사용자가 조금이라도 변화를 감지할 수 있으면 무조건 feat로 분리.
fix: 버그 수정
refactor: feat와 fix를 제외한 모든 코드 수정
chore: 별로 중요하지 않은 변경 사항 `e.g., 파일명 변경, 폴더 구조 변경, 오타 수정, 주석 제거, etc.`
build: 빌드 관련 설정
config: 개발환경 관련 설정(프로젝트 초기 설정도 포함)
style: 코드  
res: 리소스 관련 (String, Color 등) 파일 `안드로이드 한정`
```

### 서버 종류 설명

```
[api-server] : api 서버
[gateway-server] : gateway 서버
[all] : 레포 전체 적용 ex) README
```

### 커밋 한번에는 100줄 이상의 변화를 넣지 않는다.

<br/>

## 📌 PR 규칙

- 상위 이슈 하나를 해결하면 PR로 올린다.
- PR을 기준으로 CI/CD가 작동하므로 타 패키지에 불필요한 수정을 하지 않는다.
- 최소 한명의 Reviewer을 설정한다.
- 해당 Reviewer의 Approve와 빌드 및 테스트 성공이 되었을 경우에만 Merge 가능

<br/>

## 📌 PR 템플릿

```
## 연관된 이슈

- ex) #이슈번호, #이슈번호

## 작업 내용

- 이번 PR에서 작업한 내용을 간략히 설명해주세요(이미지 첨부 가능)

## 리뷰어 멘션

- `@1번`, `@2번` 과 같이 부여한 리뷰어를 멘션해주세요(slack 알림을 위해)

## 스크린샷 (선택)

## 리뷰 요구사항(선택)

- 리뷰어가 특별히 봐주었으면 하는 부분이 있다면 작성해주세요

- ex) 메서드 XXX의 이름을 더 잘 짓고 싶은데 혹시 좋은 명칭이 있을까요?
```

# 설계

## 📌 ERD

<img width="1351" alt="image" src="https://github.com/user-attachments/assets/277243e6-9014-456a-876c-ef2fbfc91881" />




## 📌 개발 중점 사항

1. 기획, 유저의 입장을 충분히 이해하며 개발하기
2. 비용, 관리해야하는 리소스의 최소화
3. 테스트를 기반으로 동작하는 안정적인 서비스 배포 (Spring Rest Docs를 사용하여 API 명세서 구현)

## 📌 아키텍처
### 서버 아키텍처
![_최종_아키텍처 drawio (1)](https://github.com/user-attachments/assets/f5b3034c-4733-43a7-aa47-236213145130)
### 배포 아키텍처
<img width="701" alt="스크린샷 2024-08-26 오후 2 39 01" src="https://github.com/user-attachments/assets/095964c5-8a99-4e2c-aeac-91fa4e6841f2">
