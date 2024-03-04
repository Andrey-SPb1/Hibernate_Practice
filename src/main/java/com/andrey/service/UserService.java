package com.andrey.service;

import com.andrey.dao.UserRepository;
import com.andrey.dto.UserCreateDto;
import com.andrey.dto.UserReadDto;
import com.andrey.entity.User;
import com.andrey.mapper.Mapper;
import com.andrey.mapper.UserCreateMapper;
import com.andrey.mapper.UserReadMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public Long create(UserCreateDto userDto) {
        // validation
        User user = userCreateMapper.mapFrom(userDto);
        return userRepository.save(user).getId();
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        return findById(id, userReadMapper);
    }

    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJpaHintName(),
                userRepository.getEntityManager().getEntityGraph("withCompany")
        );
        return userRepository.findById(id, properties)
                .map(mapper::mapFrom);
    }

    @Transactional
    public boolean delete(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.ifPresent(user -> userRepository.delete(user.getId()));
        return optionalUser.isPresent();
    }

}
