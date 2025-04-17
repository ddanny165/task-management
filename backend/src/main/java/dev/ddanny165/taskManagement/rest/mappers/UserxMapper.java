package dev.ddanny165.taskManagement.rest.mappers;

import dev.ddanny165.taskManagement.models.Userx;
import dev.ddanny165.taskManagement.rest.dto.UserxDto;
import dev.ddanny165.taskManagement.services.UserxService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope("application")
public class UserxMapper implements DTOMapper<Userx, UserxDto> {
    private UserxService userxService;

    public UserxMapper(UserxService userxService) {
        this.userxService = userxService;
    }

    @Override
    public UserxDto mapTo(Userx entity) {
        if (entity == null) {
            return null;
        }

        return new UserxDto(entity.getUsername(), entity.getPassword(), entity.getFirstName(),
                entity.getSecondName(), entity.getEmail());
    }

    @Override
    public Userx mapFrom(UserxDto dto) {
        if (dto == null) {
            return null;
        }

        Optional<Userx> optEntity = Optional.empty();
        if (dto.username() != null) {
            optEntity = userxService.findUserById(dto.username());
        }

        Userx entity = null;
        if (optEntity.isPresent()) {
            entity = optEntity.get();

            entity.setPassword(dto.password());
            entity.setEmail(dto.email());
            entity.setFirstName(dto.firstName());
            entity.setSecondName(dto.lastName());
        } else {
            entity = new Userx(dto.username(), dto.password(),
                    dto.firstName(), dto.lastName(), dto.email());
        }

        return entity;
    }
}
