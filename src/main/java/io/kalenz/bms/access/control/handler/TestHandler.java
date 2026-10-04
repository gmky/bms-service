package io.kalenz.bms.access.control.handler;

import io.kalenz.bms.access.control.repository.ActionRepository;
import io.kalenz.bms.masking.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TestHandler {
    private final ActionRepository actionRepository;

    @Scheduled(cron = "0 * * * * *")
    public void test() {
        log.info("Handle test event");
        var actions = actionRepository.findAll();
        log.info("List of action: {}", JsonUtil.toString(actions));
    }
}
