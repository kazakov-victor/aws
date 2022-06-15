package com.nixsolutions.clouds.vkazakov.aws.controller;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.mapper.UserMapper;
import com.nixsolutions.clouds.vkazakov.aws.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/rest")
public class AdminController {
    private final UserMapper userMapper;
    private final UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/")
    public @ResponseBody
    List<UserDto> listUsers() {
        return userMapper.toDto(userService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") Long id) {
        return userService.getDeleteResponse(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id) {
        return userService.getUserByIdResponse(id);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<UserDto> findUserByLogin(@PathVariable String username) {
        return userService.getFindUserResponse(username);
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<String> editUser(@ModelAttribute UserDto userDto,
                                                        @PathVariable Long id) {
        return userService.getEditResponse(userDto, id);
    }

}


