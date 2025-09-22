package kr.ac.kopo.kyg.projectkyg.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class MainController {

    public static List<Map<String, Object>> teams = new ArrayList<>();

    @GetMapping("/main")
    public String mainPage(Model model, Authentication authentication) {
        model.addAttribute("teams", teams);

        String username = Optional.ofNullable(authentication)
                .map(Authentication::getName)
                .orElse("Guest");

        model.addAttribute("username", username);

        boolean isManager = Optional.ofNullable(authentication)
                .map(auth -> auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
                .orElse(false);
        model.addAttribute("isManager", isManager);

        return "main";
    }

    @GetMapping("/teams/create")
    public String createTeamForm(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "createTeam";
    }

    @PostMapping("/teams/save")
    public String saveTeam(@RequestParam String name, @RequestParam String description, @RequestParam String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String managerId = authentication.getName();

        Map<String, Object> newTeam = new HashMap<>();
        newTeam.put("id", teams.size() + 1);
        newTeam.put("name", name);
        newTeam.put("description", description);
        newTeam.put("manager", managerId);
        newTeam.put("password", password);

        teams.add(newTeam);
        return "redirect:/main";
    }

    @GetMapping("/projects/{teamId}")
    public String showProjects(@PathVariable int teamId, Model model, Authentication authentication) {
        Map<String, Object> team = teams.stream()
                .filter(t -> t.get("id").equals(teamId))
                .findFirst()
                .orElse(null);

        if (team == null) {
            return "redirect:/main";
        }

        // AssignmentController의 teamAssignments 맵에서 과제 목록을 가져옴
        List<Map<String, Object>> assignments = AssignmentController.teamAssignments.getOrDefault(teamId, new ArrayList<>());

        boolean isManager = Optional.ofNullable(authentication)
                .map(auth -> auth.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
                .orElse(false);

        model.addAttribute("team", team);
        model.addAttribute("assignments", assignments);
        model.addAttribute("isManager", isManager);
        return "projects";
    }

    @GetMapping("/teams/join")
    public String joinTeam(Model model) {
        model.addAttribute("teams", teams);
        return "joinTeam";
    }

    @PostMapping("/teams/join")
    public String joinTeamSubmit(@RequestParam int teamId, @RequestParam String password) {
        Optional<Map<String, Object>> teamToJoin = teams.stream()
                .filter(team -> team.get("id").equals(teamId) && team.get("password").equals(password))
                .findFirst();

        if (teamToJoin.isPresent()) {
            System.out.println("팀 참가 성공: " + teamToJoin.get().get("name"));
        } else {
            System.out.println("팀 참가 실패: 팀 ID 또는 비밀번호가 올바르지 않습니다.");
        }

        return "redirect:/main";
    }
}