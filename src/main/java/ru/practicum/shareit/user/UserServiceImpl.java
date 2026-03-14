package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto create(UserDto userDto) {
        // проверка уникальности email
        if (userStorage.existsByEmail(userDto.getEmail())) {
            throw new ConflictException("Пользователь с email " + userDto.getEmail() + " уже существует");
        }
        User user = UserMapper.toUser(userDto);
        User created = userStorage.create(user);
        return UserMapper.toUserDto(created);
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User existing = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));

        // обновляем только переданные поля
        if (userDto.getName() != null) {
            existing.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            // проверяем, что новый email не занят другим пользователем
            if (!userDto.getEmail().equals(existing.getEmail()) && userStorage.existsByEmail(userDto.getEmail())) {
                throw new ConflictException("Email " + userDto.getEmail() + " уже используется");
            }
            existing.setEmail(userDto.getEmail());
        }

        User updated = userStorage.update(existing);
        return UserMapper.toUserDto(updated);
    }

    @Override
    public UserDto getById(Long id) {
        User user = userStorage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        userStorage.delete(id);
    }
}
