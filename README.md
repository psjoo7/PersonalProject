# 2022-2 모바일 프로그래밍 개인과제
#### 20160665 박성준
---------------

## 구현 환경<br>
1. 운영체제 : 맥OS
2. 안드로이드 스튜디오
3. SDK : 12
4. FTP 클라이언트 : 파일질라
5. 무료 호스팅 : 닷홈(dothome.co.kr)
6. 데이터베이스 : MySQL

## 프로젝트 계획 목표 및 구현
> ### 유저 관리 방식 - php와 MySQL <br>
<br>
php 프로그램을 매개로 MySQL 데이터베이스 서버에 유저 정보를 저장하는 것을 목표로 한다.<br>

#### 구현방법<br>
1. 안드로이드에서 MySQL에 직접 연결하는 것은 불가능하므로, 닷홈(dothome)을 통해 호스팅을 한다.
2. 닷홈에서 무료 호스팅한 도메인(psjoo7pproject.dothome.co.kr)에 php파일을 업로드 하기위해 파일질라를 이용한다.
3. php파일은 mysqli_connect를 이용해 MySQL과 연동하고, 각 로그인(Login.php), 회원가입(Register.php), ID중복체크(UserValidate.php) 동작을 수행하는 파일을 서버에 업로드한다.
4. 안드로이드에서 php를 통해 MySQL에 연동하기 위해선 인터넷 접근을 허용해야한다. 따라서 manifest 파일에서 권한을 설정해준다.
```
<uses-permission android:name="android.permission.INTERNET"/>
```
5. 각 동작에 따른 Request 파일을 만들어 연결해준다.

> ### 화면 구성 방식(1) - 로그인 화면<br>
1. RelativeLayout 사용
2. EditView : 회원ID / 비밀번호
3. Button : 로그인 / 회원가입 / 비회원주문

#### 구현방법<br>
1. Volley의 RequestQueue는 네트워크 작업 실행, 캐시 읽고 쓰기 및 응답 파싱을 위해 작업자 스레드를 관리한다.
2. 이를 이용해 EditView로 받은 정보를 담아 넘겨 데이터베이스에서 정보를 확인하고 로그인 하는 방식을 취한다.(이하 회원가입도 동일)
3. 회원가입 버튼을 누를 시, Intent를 사용해 회원가입 화면으로 넘어간다.
4. 로그인한 상태와 비회원인 상태를 나누기 위해 로그인한 상태로 상품 화면에 넘어가게 되면
```java
intent.putExtra("isMember", true);
intent.putExtra( "UserID", UserID );
intent.putExtra( "UserPwd", UserPwd );
intent.putExtra( "UserName", UserName );
intent.putExtra("UserPhone", UserPhone);
intent.putExtra("UserAdd", UserAdd);
```
를 활용해 회원인 상태로 상품화면으로 넘어갔는 표시와 함께 회원정보까지 전달한다.<br>
5. 비회원일 때,
```java
intent.putExtra("isMember", false);
```
로 구분지어 정보를 전달한다.
> ### 화면 구성 방식(2) - 회원가입 화면<br>
1. LinearLayout 사용<br>
2. EditView : ID(중복검사), 비밀번호(자릿수/특수키 등 규칙체크)<br>
3. TextView : 간략 약관<br>
4. RadioGroup - RadioButton : Decline / Accept<br>
5. 회원 정보 저장

#### 구현방법<br>
1. 중복검사는 RequestQueue를 통해 UserID의 중복여부를 검사하는 방식을 한다. 검사가 완료되어야만 회원가입이 진행되도록 한다.
2. 비밀번호의 규칙은 정규식을 통해 설정할 수 있다.
3. RadioGroup을 통해 RadioButton 두 개가 모두 눌리는 경우가 없도록 한다.
4. 회원 정보를 모두 입력받았지 않았거나, 비밀번호의 확인, 형식에 문제가 있을 시, 그리고 이용약관까지 모두 동의했을때만 회원가입이 될 수 있도록 한다.


> ### 화면 구성 방식(3)<br>
1. GridLayout 사용<br>
2. 상품리스트를 보여주는 화면<br>
3. 회원정보 버튼<br><br>

#### 구현방법<br>
1. 상품과 그 정보를 하나의 LinearLayout에 묶는다.
2. GridLayout의 row, column 개수를 맞춰, 배치한다.
3. 회원정보 버튼을 클릭시 첫 화면에서 보낸 isMember를 확인하기 위해
```java
isMember = getLoginIntent.getBooleanExtra("isMember", true);
ID = getLoginIntent.getStringExtra("UserID");
Pwd = getLoginIntent.getStringExtra("UserPwd");
name = getLoginIntent.getStringExtra("UserName");
phoneNum = getLoginIntent.getStringExtra("UserPhone");
address = getLoginIntent.getStringExtra("UserAdd");
```
를 통해 보내온 정보들을 확인한다.<br>
4. isMember가 true라면, 로그인을 해서 상품 화면에 온 것이므로, 나머지 정보들을 화면에 AlertDialog로 알림창에 보여준다.<br>
5. isMember가 false라면, 비회원이므로, 회원가입 여부를 묻는 알림창을 보여준다.
