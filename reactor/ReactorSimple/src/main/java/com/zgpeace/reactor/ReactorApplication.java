package com.zgpeace.reactor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
@Slf4j
public class ReactorApplication implements ApplicationRunner {
  public static void main(String[] args) {
    SpringApplication.run(ReactorApplication.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    //fluxInMainThread();
    //fluxInDifferentThread();
    //fluxOnErrorReturn();
    //fluxOnErrorResume();
    //fluxWithSubscribe();
    //fluxWithSubscribeAndChangePublishOnThreadOrder();
    fluxWithSubscribeAndExceptionOccur();

    return;
  }

  private void fluxWithSubscribeAndExceptionOccur() throws InterruptedException {
    Flux.range(1, 6)
        .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
        .publishOn(Schedulers.elastic())
        .doOnComplete(() -> log.info("Publisher Complete 1"))
        .map( i -> {
              log.info("Publish {}, {}", Thread.currentThread(), i);
              return 10 / (i - 3);
              //return i;
            }
        )
        .doOnComplete( () -> log.info("Publisher Complete 2"))
        .subscribeOn(Schedulers.single())
        .onErrorResume(e -> {
          log.error("Exception {}", e.toString());
          return Mono.just(-1);
        })
        //.onErrorReturn(-1)
        .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
            e -> log.error("error {}", e.toString()),
            () -> log.info("Subscriber Complete"),
            s -> s.request(4)
        );

    Thread.sleep(2000);
  }

  private void fluxWithSubscribeAndChangePublishOnThreadOrder() throws InterruptedException {
    Flux.range(1, 6)
        .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
        .publishOn(Schedulers.elastic())
        .doOnComplete(() -> log.info("Publisher Complete 1"))
        .map( i -> {
              log.info("Publish {}, {}", Thread.currentThread(), i);
              //return 10 / (i - 3);
              return i;
            }
        )
        .doOnComplete( () -> log.info("Publisher Complete 2"))
        .subscribeOn(Schedulers.single())
        .onErrorResume(e -> {
          log.error("Exception {}", e.toString());
          return Mono.just(-1);
        })
        //.onErrorReturn(-1)
        .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
            e -> log.error("error {}", e.toString()),
            () -> log.info("Subscriber Complete"),
            s -> s.request(4)
        );

    Thread.sleep(2000);
  }

  private void fluxWithSubscribe() throws InterruptedException {
    Flux.range(1, 6)
        .publishOn(Schedulers.elastic())
        .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
        .doOnComplete(() -> log.info("Publisher Complete 1"))
        .map( i -> {
              log.info("Publish {}, {}", Thread.currentThread(), i);
              //return 10 / (i - 3);
              return i;
            }
        )
        .doOnComplete( () -> log.info("Publisher Complete 2"))
        .subscribeOn(Schedulers.single())
        .onErrorResume(e -> {
          log.error("Exception {}", e.toString());
          return Mono.just(-1);
        })
        //.onErrorReturn(-1)
        .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
            e -> log.error("error {}", e.toString()),
            () -> log.info("Subscriber Complete"),
            s -> s.request(4)
        );

    Thread.sleep(2000);
  }

  private void fluxOnErrorResume() throws InterruptedException {
    Flux.range(1, 6)
        .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
        .doOnComplete(() -> log.info("Publisher Complete 1"))
        .publishOn(Schedulers.elastic())
        .map( i -> {
              log.info("Publish {}, {}", Thread.currentThread(), i);
              return 10 / (i - 3);
              //return i;
            }
        )
        .doOnComplete( () -> log.info("Publisher Complete 2"))
        .subscribeOn(Schedulers.single())
        .onErrorResume(e -> {
          log.error("Exception {}", e.toString());
          return Mono.just(-1);
        })
        //.onErrorReturn(-1)
        .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
            e -> log.error("error {}", e.toString()),
            () -> log.info("Subscriber Complete")
            //s -> s.request(4)
        );

    Thread.sleep(2000);
  }

  private void fluxOnErrorReturn() throws InterruptedException {
    Flux.range(1, 6)
        .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
        .doOnComplete(() -> log.info("Publisher Complete 1"))
        .publishOn(Schedulers.elastic())
        .map( i -> {
              log.info("Publish {}, {}", Thread.currentThread(), i);
              return 10 / (i - 3);
              //return i;
            }
        )
        .doOnComplete( () -> log.info("Publisher Complete 2"))
        .subscribeOn(Schedulers.single())
        //.onErrorResume(e -> {
        //  log.error("Exception {}", e.toString());
        //  return Mono.just(-1);
        //})
        .onErrorReturn(-1)
        .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
            e -> log.error("error {}", e.toString()),
            () -> log.info("Subscriber Complete")
            //s -> s.request(4)
        );

    Thread.sleep(2000);
  }


  private void fluxInDifferentThread() throws InterruptedException {
    Flux.range(1, 6)
        .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
        .doOnComplete(() -> log.info("Publisher Complete 1"))
        .publishOn(Schedulers.elastic())
        .map( i -> {
              log.info("Publish {}, {}", Thread.currentThread(), i);
              //return 10 / (i - 3);
              return i;
            }
        )
        .doOnComplete( () -> log.info("Publisher Complete 2"))
        .subscribeOn(Schedulers.single())
        //.onErrorResume(e -> {
        //  log.error("Exception {}", e.toString());
        //  return Mono.just(-1);
        //})
        //.onErrorReturn(-1)
        .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
            e -> log.error("error {}", e.toString()),
            () -> log.info("Subscriber Complete")
            //s -> s.request(4)
        );

    Thread.sleep(2000);
  }

  private void fluxInMainThread() throws InterruptedException {
    Flux.range(1, 6)
        .doOnRequest(n -> log.info("Request {} number", n)) // 注意顺序造成的区别
        .doOnComplete(() -> log.info("Publisher Complete 1"))
        //.publishOn(Schedulers.elastic())
        .map( i -> {
              log.info("Publish {}, {}", Thread.currentThread(), i);
              //return 10 / (i - 3);
              return i;
            }
        )
        .doOnComplete( () -> log.info("Publisher Complete 2"))
        //.subscribeOn(Schedulers.single())
        //.onErrorResume(e -> {
        //  log.error("Exception {}", e.toString());
        //  return Mono.just(-1);
        //})
        //.onErrorReturn(-1)
        .subscribe( i -> log.info("Subscribe {}: {}", Thread.currentThread(), i),
            e -> log.error("error {}", e.toString()),
            () -> log.info("Subscriber Complete")
            //s -> s.request(4)
        );

    Thread.sleep(2000);
  }


}

















