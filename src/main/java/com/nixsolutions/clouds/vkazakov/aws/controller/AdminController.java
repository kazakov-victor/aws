package com.nixsolutions.clouds.vkazakov.aws.controller;

import com.nixsolutions.clouds.vkazakov.aws.dto.UserDto;
import com.nixsolutions.clouds.vkazakov.aws.entity.User;
import com.nixsolutions.clouds.vkazakov.aws.mapper.UserMapper;
import com.nixsolutions.clouds.vkazakov.aws.service.S3BucketService;
import com.nixsolutions.clouds.vkazakov.aws.service.UserService;
import com.nixsolutions.clouds.vkazakov.aws.validate.UserValidator;
import com.nixsolutions.clouds.vkazakov.aws.validate.XSSCleaner;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/rest")
public class AdminController {
    private final UserMapper userMapper;
    private final UserService userService;
    private final XSSCleaner xssCleaner;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final S3BucketService s3BucketService;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    /**
     * Returns list of all users.
     */
    @GetMapping("/")
    public @ResponseBody
    List<UserDto> listUsers() {
        return userMapper.toDto(userService.findAll());
    }

    /**
     * Saves user in database
     * if login or email are not already in use
     */
//    @PostMapping("/" )
//    public ResponseEntity<?> saveUser(@RequestParam UserDto userDto) {
//        userDto = xssCleaner.cleanFields(userDto);
//        Map<String, String> errors = UserValidator.validate(userDto);
//        errors = userChecker.userCheck(userDto, errors);
//
//
//        if (errors.isEmpty()) {
//            User user = userMapper.toEntity(userDto);
//            String passNew = bCryptPasswordEncoder.encode(user.getPassword());
//            user.setPassword(passNew);
//            userService.save(user);
//
//            UserDto userDtoResult = userMapper.toDto(user);
//            userDtoResult.setPassword("");
//            return ResponseEntity.status(201).body(userDtoResult);
//        }
//        return ResponseEntity.status(422).body(errors);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        if (isIdWrong(id)) {
            return ResponseEntity.notFound().build();
        }
        User user = userService.findById(id);
        if (user != null) {
            userService.deleteById(id);
            return ResponseEntity.ok("User was delete!");
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Finds user by id
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findUserById(@PathVariable Long id) {
        if (isIdWrong(id)) {
            return ResponseEntity.status(422).build();
        }
        User user = userService.findById(id);
        if (user != null) {
            UserDto userDto = userMapper.toDto(user);
            userDto.setPassword("");
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<UserDto> findUserByLogin(@PathVariable Optional<String> username) {
        String usernameFromPath;
        if (username.isPresent()) {
            usernameFromPath = username.get();
        } else {
            return ResponseEntity.status(422).build();
        }

        String usernameClean = xssCleaner.cleanValue(usernameFromPath);
        User user = userService.findUserByUsername(usernameClean);
        if (user != null) {
            UserDto userDto = userMapper.toDto(user);
            userDto.setPassword("");
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Edits exist user
     */
    @PostMapping("/edit/{id}")
    public ResponseEntity<Map<String, String>> editUser(@ModelAttribute UserDto userDto,
                                @PathVariable Long id) {
        Map<String, String> errors = new HashMap<>();
        if (isIdWrong(id)) {
            errors.put("common", "Wrong id!");
            return ResponseEntity.status(422).body(errors);
        }

        if (userDto == null) {
            errors.put("entity", "There is no entity!");
        } else {
            userDto = xssCleaner.cleanFields(userDto);
            errors = UserValidator.validate(userDto);
            if(userDto.getPassword().equals("")){
               errors.remove("password","Please insert password!");
            }
            String usernameNotEdit = userService.findById(id).getUsername();

            User userEmailCheck = userService.findUserByEmail(userDto.getEmail());
            if (userEmailCheck != null && !(userEmailCheck.getId().equals(id))) {
                errors.put("email", "This email is already in use!");
            }
            if (errors.isEmpty()) {
                User userOld = userService.findById(id);
                User userNew = userMapper.toEntity(userDto);
                userNew.setId(id);
                userNew.setUsername(usernameNotEdit);
                if(!userNew.getPassword().equals("")) {
                   userNew.setPassword(bCryptPasswordEncoder.encode(userNew.getPassword()));
                } else {
                    userNew.setPassword(userOld.getPassword());
                }
                //delete old photo
                s3BucketService.deleteFile(userOld.getPhotoLink());
                userService.save(userNew);
                errors.put("entity", "Entity was update!");
                return ResponseEntity.status(201).body(errors);
            }
        }
        return ResponseEntity.status(422).body(errors);
    }

    /**
     * Checks is id correct
     * If it is wrong return true or false in another case
     */
    private boolean isIdWrong(Long id) {
        String ID_REGEX = "^[0-9]+$";
        Pattern pattern = Pattern.compile(ID_REGEX);
        Matcher matcher = pattern.matcher(String.valueOf(id));
        User user = userService.findById(id);
        return id == null || !matcher.find() || user == null;
    }
}


