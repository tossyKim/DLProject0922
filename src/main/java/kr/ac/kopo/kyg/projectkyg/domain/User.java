package kr.ac.kopo.kyg.projectkyg.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 메인키

    @Column(nullable = false, length = 50)
    private String name; // 이름

    @Column(nullable = false, unique = true, length = 50)
    private String username; // 로그인 아이디 (학번)

    @Column(nullable = false, length = 100)
    private String password; // 비밀번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // 이 어노테이션이 핵심입니다.
    private Role role; // enum 타입으로 변경
}