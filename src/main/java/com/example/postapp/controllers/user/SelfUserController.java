package com.example.postapp.controllers.user;

import com.example.postapp.controllers.common.UpdateResultResponse;
import com.example.postapp.domain.models.UserDetailsImpl;
import com.example.postapp.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/self")
public class SelfUserController {
    @Autowired
    private UserRepository userRepo;

    @DeleteMapping("/")
    public ResponseEntity<?> delete(@AuthenticationPrincipal UserDetailsImpl user) {
        userRepo.deleteById(user.getId());
        return ResponseEntity.ok().body(new UpdateResultResponse("Ok"));
    }
}
