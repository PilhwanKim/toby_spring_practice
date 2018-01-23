# 4장 - 예외

## Intro

* 개발자가 신경쓰기 귀찮은 영역 - 예외처리
* 이번 장에서는...
  * 스프링의 데이터 엑세스 기능의 예외처리
  * 예외를 처리하는 베스트 프랙티스

## 4.1. 사라진 SQLException

```java
    public void deleteAll() throws SQLException {
        this.jdbcContext.executeSql("delete from users");
    }
```

```java
    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }
```

* SQLException 의 행방은?

### 4.1.1. 초난감 예외처리

* 대표적인 초난감 예외처리 대표주자들!
  * 예외 블랙홀
    ```java
        try {

        } catch(SQLException e){
        }
    ```
    * 예외를 받고 아무것도 하지 않음
    * 발생한 예외를 무시해버리는 상황 발생
    * 이런 코드를 작성시 문제는 나중에 오류가 있어 예외발생시 어디에서 문제가 생기는지 전혀 알수 없게 되버린다는 점이다.
    ```java
    } catch(SQLException e){
        System.out.println(e);
    }
    ```

    ```java
    } catch(SQLException e){
        e.printStackTrace();
    }
    ```
    * 위의 둘도 문제가 많음
    * 콘솔 로그에만 찍히게 되는데, 누가 직접 콘솔을 계속 보고 있지 않는 한 이 메시지를 볼수 없다.
    * SQLException의 발생 이유
      * SQL문법에러
      * DB에서 처리할수 없을 정도의 데이터 액세스 로직에 심각한 버그
      * 서버가 죽음 혹은 네트워크가 끊김
    * 최소한 이렇게라도 해야 함
    ```java
    } catch(SQLException e){
        e.printStackTrace();
        System.exit(1);
    }
    ```

  * 무의미하고 무책임한 throws
    * 메소드 선언에 throws Exception을 기계적으로 붙임
    ```java
        public void method1() throws Exception {
            method2();
        }
        public void method2() throws Exception {
            method3();
        }
        public void method3() throws Exception {
            ...
        }
    ```
    * 남용하면 throw 선언이 의미없어져 버림. 
    * 실행 중 정말 예외 상황이 발생할 수 밖에 없는 메소드인지 부르는(사용하는) 쪽에선 알수 없게 되어버림
* 예외처리를 할때 반드시 지켜야 될 핵심 원칙(둘중에 하나가 되어야...)
  * 예외는 적절하게 복구
  * 작업을 중단시키고 운영자 또는 개발자에게 분명하게 통보

### 4.1.2. 예외의 종류와 특징

* 어떻게 처리해야 하는가? 오랫동안 논쟁이 되온 부분
* 특히 체크 예외(cheched exception)의 명시적으로 처리가 필요한 예외를 다루는 방법이 큰 이슈였음

* 예외는 크게 3가지 종류
  * Error
    * java.lang.Error 클래스와 그 서브 클래스
    * VM에서 비정상적인 상황 발생할 경우
    * 애플리케이션에서 해결할 수 없는 상황인 경우(OutOfMemoryError ThreadDeath 등)
    * 개발자가 신경쓰지 않아도 됨
  * Exception

    ![Exception의 2가지 종류](images/4-1.PNG)

    * java.lang.Exception 클래스와 그 서브 클래스
    * 애플리케이션 코드 작업중에 예외상황이 발생할 경우에 사용
    * 다시 체크예외와 언체크 예외로 나뉨
      * Exception과 체크 예외
        * RuntimeException을 상속하지 않음
        * 반드시 catch문으로 예외를 잡아 처리하거나 throws 선언이 필요
      * RuntimeException과 언체크/런타임 예외
        * RuntimeException 클래스와 그 서브 클래스
        * 명시적인 예외처리를 강제하지 않음. 즉 catch 문이나 throws 선언을 꼭 하지 않아도 됨
        * 주로 프로그램의 오류가 있을 때 발생하도록 의도한 것 - 예) NullPointerException

### 4.1.3. 예외처리 방법

* 예외 복구
  * 예외상황을 파악하고 문제를 해결해서 정상 상태로 돌려놓는 것
  * 예1) 사용자가 요청한 파일을 읽으려고 시도했는데 해당 파일이 없다거나 다른 문제가 있어 IOException이 발생
    * 사용자에게 다른 파일을 이용하도록 안내함
  * 예2) 네트워크가 불안해서 가끔 접속이 안되는 시스템. 원격DB 서버 접속 실패해서 SQLException이 발생한 경우
    * 일정 시간 대기후 접속 재시도. 이것을 정해진 횟수만큼 시도. 실패했다면 복구 포기

```java
    int maxretry = MAX_RETRY;
    while(maxretry -- >0) {
        try {
            ...     // 예외가 발생할 가능성이 있는 시도
            return; // 작업 성공
        catch(SomeException e) {
            // 로그 출력. 정해진 시간만큼 대기
        finally {
            // 리소스 반납. 정리 작업
        }
        throw new RetryFailedException(); // 최대 재시도 횟수를 넘기면 직접 예외 발생.
```

* 예외 처리 회피
  * 예외처리를 자신이 담당하지 않고 자신을 호출한 쪽으로 던져버리는 것
  * throws문으로 선언해서 예외가 발생하면 알아서 던저지게 하거나 catch문으로 일단 예외를 잡은 후에 로그를 남기고 다시 예외를 던지는 것(rethrow)
  * 자신이 사용하는 쪽에서 예외를 다루는게 최선의 방법일 때 사용
  * 예) JdbcTemplate에서 사용하는 콜백 오브젝트는 ResultSet이나 PrepareStatement 등을 이용해서 작업하다 발생하는 SQLException을 자신이 처리하지 않고 템플릿으로 던져버림
    * SQLException 처리는 콜백오브젝트의 역할이 아니라 템플릿의 역할이라고 생각하기 때문
  * 이와같이 명확히 예외를 다루는 것이 자신이 사용하는 쪽에서 해야한다고 판단될때 써야한다.

  * 예외처리 회피 1

```java
    public void add() throws SQLException {
        // JDBC API
    }
```

* 예외처리 회피2

```java
    public void add() throws SQLException {
        try {
            // JDBC API
        } catch(SQLException e) {
            // 로그 출력
            throw e;
        }
    }
```

* 예외 전환(exeception translation)
  * 발생한 예외를 그대로 넘기지 않고 적절한 예외로 전환해 던짐
  * 보통 2가지 목적으로 사용
    * 내부에 발생한 예외를 좀더 적절하고 분명한 의미를 가진 예외로 바꿔서 던지기 위해
      * 예) 새로운 사용자를 등록하려고 시도했을 때 아이디가 같은 사용자가 있어 DB에서 JDBC API의 SQLException 발생시킴. DAO에서는 이 정보를 좀더 의미있게 해석해서 SQLException에러를 잡아서 DuplicationUserIdException 같은 예외를 정의해서 던짐.
    * 예외를 처리하기 쉽고 단순하게 만들기 위해 포장하는 것
      * 예외처리를 강제하는 체크예외를 언체크 예외(런타임 예외)로 바꾸는 경우
      * 대부분 서버환경에서는 애플리케이션 코드에서 처리하지 않고 전달된 예외들을 일괄적으로 다룰수 있는 기능을 제공함
      * 어차피 복구못할 예외라면 애플리케이션 코드에서는 런타임 예외로 포장해서 던지고 예외처리 서비스 등을 이용해 로그를 남기고, 관리자에게 메일로 통보, 사용자에게 안내 메시지를 보여주는 것이 바람직

  * 체크 예외를 언체크 예외로 포장해서 던짐(SQLException -> EJBException)

```java
    try {
        OrderHome orderHome = EJBHomeFactorY.getlnstance().getOrderHome();
        Order order = orderHome.findByPrimaryKey(Integer id);
    } catch (NamingException ne) (
        throw new EJBException(ne);
    } catch (SQLException se) (
        throw new EJBException(se);
    } catch (RemoteException re ) (
        throw new EJBException(re)
    }
```

### 4.1.4. 예외처리 전략

* 런타임 예외의 보편화
  * 일반적으로 
    * 체크 예외 - 일반적 예외
    * 언체크 예외 - 시스템 장애나 프로그램상의 오류에 사용
  * 예외처리 강제
    * API 사용하는 개발자의 실수 방지를 위한 배려일수도
    * 예외를 다루고 싶지 않은 귀차니즘의 원인 일수도
  * 애플릿, AWT, 스윙 - 독립형 어플리케이션
    * 통제 불가능한 시스템 예외라도 애플리케이션의 작업이 중단되지 않게 해주고 상황을 최대한 복구해야함
  * 자바 엔터프라이즈 서버 환경
    * 수많은 사용자 동시 요청을 처리해야 함
    * 각 요청은 독립적 작업
    * 하나의 요청 처리중 예외 발생시 해당 작업만 중단하면 됨
    * 예외 발생시 사용자와 커뮤니케이션 하면서 복구할 수 있는 수단이 없음
    * 예외 상황을 미리 파악하고, 예외가 발생치 않도록 차단하는 게 좋음
    * 빨리 요청의 작업 취소 후, 서버 관리자나 개발자에게 통보해야 함
    * 즉 체크예외는 점점 사용도가 떨어지고 있음
    * 대부분 런타임 예외로 처리하는 경향
    * 언체크라도 언제든지 예외를 catch로 잡을수도 있음. 즉 선택적이라 더더욱 런타임 에러로 처리

* add() 메소드의 예외 처리

```java
    public void add(User user) throws DuplicateUserIdException, SQLException {
        try {
            // JDBC를 이용해 user 정보를 DB에 추가하는 코드 또는
            // 그런 기능을 가진 다른 SQLException을 던지는 메소드를 호출하는 코드
        } catch(SQLException e) {
            // ErrorCode가 MySQL의 "Duplicate Entry(1062)"이면 예외 전환
            if (e.QetErrorCode() == MysQIErrorNumbers.ER_DUP_ENTRY)
                throw new DuplicateUserIdException();
        } else
            throw e; // 그 외의 경우는 SQLException 그대로
```

  * SQLException 은 대부분 복구 불가능한 예외이므로 throws 를 계속 이어가게 해는 것 보다 런타임 예외로 포장해 주는 것이 좋다.
  * DuplicateUserIdException 도 굳이 체크 예외로 둘 필요가 없음. 어디에서는 잡아 처리할 수 있기 때문.
  * 다만 명시적으로 throws는 선언하는게 좋음. 그래야 add() 메소드를 사용하는 개발자엑 의미 있는 정보가 전달됨
  * 둘다 언체크 에외로 변환시키면 다음과 같이 코드를 바꿔야 함

  * DuplicateUserldException 을 런타임 예외로 구현
  ```java
    public class DuplicateUserldException extends RuntimeException {
        public DuplicateUserldException(Throwable cause) {
            super (cause);
        }
    }
  ```

  * add() 메소드 코드

  ```java
    public void add(User user) throws DuplicateUserIdException {
        try {
            // JDBC를 이용해 user 정보를 DB에 추가하는 코드 또는
            // 그런 기능을 가진 다른 SQLException을 던지는 메소드를 호출하는 코드
        } catch(SQLException e) {
            if (e.QetErrorCode() == MysQIErrorNumbers.ER_DUP_ENTRY)
                throw new DuplicateUserIdException(e); // 예외 전환
        } else
            throw new RuntimeException(e); // 예외 포장
  ```

* 낙관적인 예외처리 기법
  * 복구할 수 있는 예외는 없다는 가정
  * 어짜피 시스템 레벨에서 알아서 처리
  * 꼭 필요한 경우는 런타임 예외라도 잡아서 복구함
* 비관적인 예외처리 기법
  * 일단 잡고 보도록 강제하는 체크 예외

* 애플리케이션 예외
  * 애플리케이션 자체의 로직에 의해 의도적으로 발생시키고 반드시 catch해서 조치를 취하도록 요구하는 예외
  * 예제) 사용자가 요청한 금액을 은행계좌에서 출금하는 기능을 가진 메소드
    * 대략적인 로직
      * 현재 잔고 확인
      * 허용하는 범위를 넘어선 출금 요청시 출금작업 중단
      * 경고를 사용자에게 보냄
    * 메소드를 설계하는 2가지 방법
      * 첫번째, 정상적인 출금처리를 했을 경우와 잔고 부족이 발생했을 경우에 각각 다른 종류의 리턴 값을 돌려준다.
        * 정상 출금 - 리턴값이 요청금액 자체
        * 잔고 부족 - 0또는 -1 같은 특별값 리턴
        * 문제점
          * 리턴값을 명확하게 코드화 하지 않으면 혼란이 생김
          * 결과 값을 확인하는 조건문이 자주 등장함. 코드가 지저분해지고 흐름파악 힘듦.
      * 두번째, 정상적 흐름을 따르는 코드는 그대로 두고, 잔고 부족과 같은 예외상황에서는 비즈니스적인 의미를 띈 예외를 던지도록 한다.
        * 잔고 부족인 경우 - InsufficientBalenceException등을 던짐
        * 의도적으로 체크예외를 둠 - 개발자가 잊지않고 특정상황에 대한 처리를 하게 함

      ```java
        try {
            BigDecimal balance = account .withdraw(amount);
            ...
            // 정상적인 처리 결과를 출력하도록 진행
        catch(InsufficientBalanceException e) { // 체크 예외
            // InsufficientBalanceException에 담긴 인출 가능한 잔고금액 정보를 가져옴
            BigDecimal avai lFunds = e.getAvailFunds();
            ...
            // 잔고 부족 안내 메시지를 준비하고 이를 출력하도록 진행
        }
      ```

### 4.1.5. SQLException은 어떻게 되었나?

* SQLException은 복구 가능한 예외인가?
  * 99%의 경우 코드 레벨에서 복구할 방법이 없음
  * 대부분의 발생이유
    * 프로그램의 오류 또는 개발자의 부주의
      * SQL 문법이 틀림
      * 제약조건 위반  
    * 통제할 수 없는 외부상황
      * DB 서버 다운
      * 네트워크 불안정
      * DB 커넥션 풀이 꽉 참
  * 결국 관리자나 개발자에게 예외 발생을 알리는 방법 밖에 없음
  * SQLException을 잡아서 무언가 처리할 것이 거의 없음
  * 가능한한 의미있는 언체크/런타임 예외로 전환해서 던지는 것이 나음
  * 스프링은 JdbcTemplate에서 이와같은 예외처리 전략을 따름
    * **(중요!)SQLException을 런타임 예외 DataAccessException으로 포장해 던짐**
    * 스프링을 사용하는 측은 꼭 필요한 경우에만 catch 해서 처리하면 됨