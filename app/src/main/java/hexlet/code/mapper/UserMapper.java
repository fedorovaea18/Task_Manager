package hexlet.code.mapper;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.model.User;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeMapping
    public void encryptPassword(UserCreateDTO userCreateDTO) {
        var password = userCreateDTO.getPassword();
        userCreateDTO.setPassword(passwordEncoder.encode(password));
    }

    @BeforeMapping
    public void encryptPasswordUpdate(UserUpdateDTO userUpdateDTO, @MappingTarget User user) {
        var password = userUpdateDTO.getPassword();
        if (password != null && password.isPresent()) {
            user.setPasswordDigest(passwordEncoder.encode(password.get()));
        }
    }

    @Mapping(source = "password", target = "passwordDigest")
    public abstract User map(UserCreateDTO userCreateDTO);

    public abstract UserDTO map(User user);

    public abstract void update(UserUpdateDTO userUpdateDTO, @MappingTarget User user);
}
