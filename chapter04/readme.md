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