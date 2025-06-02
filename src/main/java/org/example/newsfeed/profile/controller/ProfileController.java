package org.example.newsfeed.profile.controller;


import jakarta.validation.Valid;
import org.example.newsfeed.profile.dto.FindProfileResponseDto;
import org.example.newsfeed.profile.dto.UpdateProfileRequestDto;
import org.example.newsfeed.profile.dto.UpdateProfileResponseDto;
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

    /**
     * @author : parkjj0408
     * @param   id    프로필 조회 하고 싶은 사람의 PK 값
     * @return        조회된 프로필 정보를 담은 ResponseEntity
     */
    @GetMapping("/{id}/profile")
    public ResponseEntity<FindProfileResponseDto> findProfile(@PathVariable Long id) {
        FindProfileResponseDto findProfileResponseDto = profileService.findProfile(id);
        return new ResponseEntity<>(findProfileResponseDto, HttpStatus.OK);

    }

    /**
     * @author : parkjj0408
     * @param   id          프로필 조회 하고 싶은 사람의 PK 값
     * @param   requestDto  수정할 프로필 정보가 담긴 요청 DTO
     * @return              조회된 프로필 정보를 담은 ResponseEntity
     */
    @PatchMapping("/{id}/profile")
    public ResponseEntity<UpdateProfileResponseDto> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProfileRequestDto requestDto){
        UpdateProfileResponseDto updateProfileResponseDto = profileService.updateProfile(id,requestDto);
        return new ResponseEntity<>(updateProfileResponseDto,HttpStatus.OK);
    }
}



