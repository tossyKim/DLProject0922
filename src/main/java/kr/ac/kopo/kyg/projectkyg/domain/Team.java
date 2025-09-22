package kr.ac.kopo.kyg.projectkyg.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 메인키

    @Column(nullable = false, unique = true, length = 50)
    private String name; // 팀명

    @Column(nullable = false, length = 100)
    private String password; // 비밀번호

    @Column(nullable = false, unique = true, length = 50)
    private String manager; // 관리자

    @Column(nullable = false)
    private String description;
}