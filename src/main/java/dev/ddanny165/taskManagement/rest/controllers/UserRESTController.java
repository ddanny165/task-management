package dev.ddanny165.taskManagement.rest.controllers;

import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.rest.dto.UserxDto;
import dev.ddanny165.taskManagement.rest.dto.error.ErrorDto;
import dev.ddanny165.taskManagement.rest.dto.error.ErrorType;
import dev.ddanny165.taskManagement.rest.mappers.UserxMapper;
import dev.ddanny165.taskManagement.services.UserxService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRESTController {
    private final UserxService userxService;
    private final UserxMapper userxMapper;

    public UserRESTController(UserxService userxService, UserxMapper userxMapper) {
        this.userxService = userxService;
        this.userxMapper = userxMapper;
    }

    // TODO: remove later, implemented in order to test the functionality of the REST controller
    @GetMapping("")
    public List<Userx> getAll() {
        return userxService.findAllUsers();
    }

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody UserxDto userxDto) {
        if (userxDto.username() == null || userxDto.username().equals("")) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.USERNAME_EQUALS_TO_NULL,
                    "The username of a new user can not be equal to null or empty!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userxDto.firstName() == null || userxDto.lastName() == null
                || userxDto.email() == null || userxDto.password() == null) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.INVALID_NULL_PARAMETER,
                    "At least one of the provided parameters is equal to null!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userxService.doesAlreadyExistWithSuchUsername(userxDto.username())) {
            return new ResponseEntity<>(new ErrorDto(ErrorType.USERNAME_ALREADY_EXISTS,
                    "The user with a provided username already exists in our system!"),
                    HttpStatus.BAD_REQUEST);
        }

        Userx userx = userxService.saveUser(userxMapper.mapFrom(userxDto));
        return new ResponseEntity<>(userxMapper.mapTo(userx), HttpStatus.CREATED);
    }

    // TODO: login user
    // POST endpoint "/auth/login"
    // Request Body: username + password
    // Response: JWT token

    // TODO: update user
    // Update user information such as email/password/firstName/LastName
    // method parameters: @RequestBody Userx userxDto, Authentication authentication
    // Request Body: UserxDto
    // Authentication via JWT token

    // TODO: get authenticated user information
    // method parameters: Authentication authentication
    // Authentication via JWT token (should be included in the request header)
}
