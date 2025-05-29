package org.example.newsfeed.profile.controller;


import org.example.newsfeed.profile.dto.FindProfileResponseDto;
import org.example.newsfeed.profile.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    //프로필 조회
    @GetMapping("/{id}/profile")
    public ResponseEntity<FindProfileResponseDto> findProfile(@PathVariable Long id) {
        FindProfileResponseDto findProfileResponseDto = profileService.findProfile(id);
        return new ResponseEntity<>(findProfileResponseDto, HttpStatus.OK);

    }


}



