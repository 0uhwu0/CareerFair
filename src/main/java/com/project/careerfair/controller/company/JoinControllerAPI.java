package com.project.careerfair.controller.company;

import com.project.careerfair.domain.Company;
import com.project.careerfair.domain.Industry;
import com.project.careerfair.service.industry.IndustryService;
import com.project.careerfair.service.company.join.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("companyJoinControllerAPI ")
@Slf4j
@RequestMapping("/api/company/join/")
@PreAuthorize("hasAuthority('recruiter') or hasAuthority('admin') or hasAuthority('company')")
@RequiredArgsConstructor
public class JoinControllerAPI {

    private final JoinService joinService;

    private final IndustryService industryService;

    // 업종목록 불러오기
    @GetMapping("industry")
    public List<Industry> industryList() {
        return industryService.getIndustryList();
    }

    // 참여기업신청하기
    @PostMapping
    public ResponseEntity<Map<String, Object>> regCompany(
            Company company,
            @RequestParam("files") MultipartFile[] files,
            Authentication authentication) {
        boolean ok = false;
        Map<String, Object> response = new HashMap<>();

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        try {
            ok = joinService.create(company, files, authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (ok) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // 목록 불러오기
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCompanies(
            @RequestParam(value = "roundValue", required = false) String roundValue,
            Authentication authentication) {
        Map<String, Object> result = joinService.getList(roundValue, authentication.getName());
        return ResponseEntity.ok(result);
    }

    // 상세 불러오기
    @GetMapping("{companyId}")
    public ResponseEntity<Map<String, Object>> getCompany(@PathVariable("companyId") Integer companyId) {
        Map<String, Object> result = joinService.getDetail(companyId);
        return ResponseEntity.ok(result);
    }


    @PostMapping("{companyId}")
    public ResponseEntity<Map<String, Object>> modifyCompany(
            @PathVariable("companyId") Integer companyId,
            Company company,
            @RequestParam(value = "removeFiles", required = false) List<String> removeFileNames,
            @RequestParam(value = "files", required = false) MultipartFile[] files) {
        try {
            boolean ok = joinService.modify(company, files, removeFileNames);
            Map<String, Object> response = new HashMap<>();
            if (ok) {
                response.put("message", "수정되었습니다.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "수정에실패하였습니다.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
