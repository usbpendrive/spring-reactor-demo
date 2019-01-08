package org.ragnarok.reactor;

import org.ragnarok.reactor.domain.ToDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Configuration
public class FluxExample {
    static private Logger LOG = LoggerFactory.getLogger(FluxExample.class);

    @Bean
    public CommandLineRunner runFluxExample() {
        return args -> {
            EmitterProcessor<ToDo> stream = EmitterProcessor.create();
            Mono<List<ToDo>> promise = stream
                    .filter(ToDo::isCompleted)
                    .doOnNext(s -> LOG.info("FLUX >> ToDo: {}", s.getDescription()))
                    .collectList()
                    .subscribeOn(Schedulers.single());
            stream.onNext(new ToDo("Read a Book", true));
            stream.onNext(new ToDo("Workout in the morning"));
            stream.onNext(new ToDo("Organize my room", true));
            stream.onNext(new ToDo("Go to the car wash", true));
            stream.onNext(new ToDo("SP1 is coming in 2018", true));
            stream.onComplete();
            promise.block();
        };
    }
}
