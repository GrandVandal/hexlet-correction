package io.hexlet.typoreporter.web;

import io.hexlet.typoreporter.service.TypoService;
import io.hexlet.typoreporter.service.dto.typo.ReportedTypo;
import io.hexlet.typoreporter.service.dto.typo.TypoReport;
import io.hexlet.typoreporter.web.exception.WorkspaceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;

import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
public class WorkspaceApi {

    private final TypoService service;

    @PostMapping("/typos")
    @CrossOrigin(
        originPatterns = {"*"},
        allowCredentials = "true"
    )
    public ResponseEntity<ReportedTypo> addTypoReport(Authentication authentication,
                                                      @Valid @RequestBody TypoReport typoReport,
                                                      UriComponentsBuilder builder) {
        final var wksIdStr = authentication.getName();
        try {
            final var id = Long.parseLong(wksIdStr);
            final var uri = builder.path("/workspace").pathSegment(wksIdStr).path("/typos").build().toUri();
            return created(uri).body(service.addTypoReport(typoReport, id));
        } catch (NumberFormatException e) {
            throw new WorkspaceNotFoundException(wksIdStr, e);
        }
    }
}
