package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Service
public class InfoService {

    @Value("${server.port}")
    private final int port;
    private final Logger logger= LoggerFactory.getLogger(InfoService.class);

    public InfoService(@Value("${server.port}") int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getValue() {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start("not modified stream");
        long sum1 = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
        stopWatch.stop();
        stopWatch.start("modified stream");
        long sum2 = LongStream.rangeClosed(1, 1000000).sum();
        stopWatch.stop();
        logger.info(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
        return "sum1=" + sum1 + "sum2=" + sum2;
    }}
