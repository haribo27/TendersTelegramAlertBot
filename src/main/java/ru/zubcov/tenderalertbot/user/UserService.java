package ru.zubcov.tenderalertbot.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo repo;

    public User registerIfNotExists(org.telegram.telegrambots.meta.api.objects.User tgUser) {

        return repo.findByTelegramId(tgUser.getId())
                .orElseGet(() -> {
                    User user = new User(
                            tgUser.getId(),
                            tgUser.getUserName(),
                            tgUser.getFirstName(),
                            tgUser.getLastName());
                    return repo.save(user);
                });
    }
}
