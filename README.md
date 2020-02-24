Smart Diffuser Application (Android)
======================

# 1. 개요
## 1.1. 주제
SmartDiffuser App  (Android)

## 1.2. 목적 / 목표
센서와의 통신 기능을 내장하는 Smart Diffuser 제어 App을 개발하여사용자에게 보다 편리한 서비스를 제공한다.
3가지의 모드를 각각 독립적으로 제어하는 서비스를 제공한다.

## 1.3. 개발 범위
*  **수면모드**<br>
-수면에 도움이 되는 디퓨저를 제어한다. (강, 약)<br>
-가습 모드와 mix 가능

*  **집중모드**<br>
-집중에 도움이 되는 디퓨저를 제어한다. (강, 약)<br>
-가습 모드와 mix 가능

*  **가습모드**<br>
-가습에 도움이 되는 디퓨저를 제어한다. (강, 약)<br>
-수면, 집중 모드와 mix 가능

## 1.4. 개발 환경
* Android Studio 3.4

****
# 2. 구성
## 2.1. 시스템 구성도
![시스템 구성](https://github.com/Jeongwonseok/Portfolio_JWS/blob/master/image/smart/시스템구성.png)

## 2.2. SW 구성
<img src="https://github.com/Jeongwonseok/Portfolio_JWS/blob/master/image/smart/구성.png" width="300" height="300">

****
# 3. 제공 기능
![메인](https://github.com/Jeongwonseok/Portfolio_JWS/blob/master/image/smart/메인.png)
* **수면 모드**
```
* 사용자에게 수면 모드의 강, 약 제어를 제공한다.
* 클릭 시, 수면 모드 가동 시간을 입력받는다.
* 가동 시간 입력 후, 딜레이를 통해 타이머 기능을 제공한다.
```

* **집중 모드**
```
* 사용자에게 집중 모드의 강, 약 제어를 제공한다.
* 클릭 시, 집중 모드 가동 시간을 입력받는다.
* 가동 시간 입력 후, 딜레이를 통해 타이머 기능을 제공한다.
```

* **가습 모드**
```
* 사용자에게 가습 모드의 강, 약 제어를 제공한다.
* 클릭 시, 가습 모드 가동 시간을 입력받는다.
* 가동 시간 입력 후, 딜레이를 통해 타이머 기능을 제공한다.
```

****
# 4. 사용 기술
## 4.1. 블루투스 통신 (BlutoothSPP)
![통신1](https://github.com/Jeongwonseok/Portfolio_JWS/blob/master/image/smart/통신1.png)
```
설명 : 블루투스 변수를 선언하고, 생성자를 정의한다.
```
![통신2](https://github.com/Jeongwonseok/Portfolio_JWS/blob/master/image/smart/통신2.png)
```
설명 : 아두이노에서 온 데이터를 data 배열에 넣어 합친 후, message를 통해 Toast 메시지를 출력한다.
```
![통신3](https://github.com/Jeongwonseok/Portfolio_JWS/blob/master/image/smart/통신3.png)
```
설명 : 블루투스 연결 버튼 변수를 선언하고, 페어링 되어있는 기기 목록 출력 화면을 생성한다.
```
![통신4](https://github.com/Jeongwonseok/Portfolio_JWS/blob/master/image/smart/통신4.png)
```
설명 : 블루투스 중지 및 시작 함수를 선언한다.
```

## 4.2. 핸들러
![핸들러1](https://github.com/Jeongwonseok/Portfolio_JWS/blob/master/image/smart/핸들러1.png)
```
설명
- 블루투스 연결 완료 후, 사용 준비가 되면 자동으로 실행되는 setup() 함수를 정의한다.
- 종료 버튼 클릭 시, 이전에 진행중이던 예약를 전부 지운다. (Handler 사용)
```
![핸들러2](https://github.com/Jeongwonseok/Portfolio_JWS/blob/master/image/smart/핸들러2.png)
![핸들러3](https://github.com/Jeongwonseok/Portfolio_JWS/blob/master/image/smart/핸들러3.png)
```
설명
- 메인스레드와 서브스레드 간에 Handler를 통해 메시지를 전달하여 메시지 큐에 저장하는 방식의 통신을 사용한다.
- FIFO(First In First OUT) 방식으로 먼저 전달 받은 메시지를 먼저 처리한다.
```

****
# 5. 부록
## 5.1. 참여 목록
* 한양대 에리카 4학년 학생 외주 작업 (한양대학교)
