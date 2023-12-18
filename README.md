# Spring Stream Video
## 실행
`./gradlew bootrun`
## 컨텍스트
### 로컬 동영상 이용
1. 동영상을 담을 디렉터리를 생성 후에, application.properties에 경로를 아래와 같이 기록
`video.dir={디렉터리 경로}`
2. 디렉터리 내에 아래 형태로 파일 저장(확장자는 .MOV 고정)
`{파일 id}.MOV`
3. 요청 시 param에 파일 id 포함
`ex. /videos?id={파일 id}`
